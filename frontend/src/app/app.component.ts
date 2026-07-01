import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { AuthResponse, PerfilUsuario } from './models';
import { SigclinApiService } from './sigclin-api.service';

type PanelAuth = 'login' | 'registro' | 'recuperar';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  panelActivo: PanelAuth = 'login';
  mensaje = 'Elige ingresar o crear cuenta para iniciar la demostracion.';
  mensajeError = false;
  tokenSesion = localStorage.getItem('sigclin_token') || '';
  usuarioId = Number(localStorage.getItem('sigclin_usuario_id') || 0);
  usuario?: PerfilUsuario;
  fotoDragging = false;
  mostrarAlertaEmail: boolean = true;

  loginForm = {
    email: 'usuario@sigclin.pe',
    password: '123456'
  };

  registroForm = {
    email: 'usuario@sigclin.pe',
    password: '123456',
    nombres: 'Marco',
    apellidos: 'Estudiante'
  };

  recuperarEmail = 'usuario@sigclin.pe';

  perfilForm = {
    nombres: '',
    apellidos: '',
    fotoPerfilUrl: '',
    preferencias: 'Recordatorios por correo y atencion preferente en manana.',
    notificacionesActivas: true
  };

  constructor(private readonly api: SigclinApiService) {}

  get sesionActiva(): boolean {
    return Boolean(this.tokenSesion || this.usuarioId);
  }

  get estadoSesion(): string {
    return this.tokenSesion ? `Sesion activa: ${this.tokenSesion.slice(0, 8)}...` : 'Sin sesion activa';
  }

  mostrarPanel(panel: PanelAuth): void {
    this.panelActivo = panel;
    this.mensajeError = false;
    this.mensaje = panel === 'login'
      ? 'Ingresa tus credenciales para acceder al sistema.'
      : panel === 'registro'
        ? 'Completa los datos para crear una cuenta de usuario.'
        : 'Ingresa tu correo para generar una solicitud de recuperacion.';
  }

  registrar(): void {
    this.api.registrar(this.registroForm).subscribe({
      next: response => this.procesarAuth(response),
      error: error => this.mostrarError(error)
    });
  }

  login(): void {
    this.api.login(this.loginForm).subscribe({
      next: response => this.procesarAuth(response),
      error: error => this.mostrarError(error)
    });
  }

  recuperarPassword(): void {
    this.api.recuperarPassword(this.recuperarEmail).subscribe({
      next: () => {
        this.mensajeError = false;
        this.mensaje = 'Solicitud enviada. Revisa tu correo para continuar con la recuperacion de cuenta.';
      },
      error: error => this.mostrarError(error)
    });
  }

  verificarEmail(): void {
    if (!this.usuarioId) return;
    this.api.verificarEmail(this.usuarioId).subscribe({
      next: response => this.procesarAuth(response),
      error: error => this.mostrarError(error)
    });
  }

  guardarPerfil(): void {
    if (!this.usuarioId) return;
    this.api.actualizarPerfil(this.usuarioId, {
      nombres: this.perfilForm.nombres,
      apellidos: this.perfilForm.apellidos,
      fotoPerfilUrl: this.perfilForm.fotoPerfilUrl,
      preferencias: this.perfilForm.preferencias
    }).subscribe({
      next: usuario => this.procesarPerfil(usuario, 'Perfil actualizado correctamente'),
      error: error => this.mostrarError(error)
    });
  }

  guardarNotificaciones(): void {
    if (!this.usuarioId) return;
    this.api.configurarNotificaciones(this.usuarioId, this.perfilForm.notificacionesActivas).subscribe({
      next: usuario => this.procesarPerfil(usuario, 'Notificaciones actualizadas correctamente'),
      error: error => this.mostrarError(error)
    });
  }

  cerrarSesion(): void {
   localStorage.removeItem('sigclin_token');
    localStorage.removeItem('sigclin_usuario_id');
    this.tokenSesion = '';
    this.usuarioId = 0;
    this.usuario = undefined;
    
    this.loginForm.email = '';
    this.loginForm.password = '';

    this.panelActivo = 'login';
    this.mensaje = 'Sesion cerrada. Ingresa nuevamente para editar tu perfil.';
  }

  cargarFoto(file?: File): void {
    if (!file) return;
    if (!file.type.startsWith('image/')) {
      this.mensajeError = true;
      this.mensaje = 'Selecciona un archivo de imagen PNG o JPG.';
      return;
    }
    if (file.size > 600 * 1024) {
      this.mensajeError = true;
      this.mensaje = 'La imagen es muy grande para esta demo. Usa una foto menor a 600 KB.';
      return;
    }
    const reader = new FileReader();
    reader.onload = () => {
      this.perfilForm.fotoPerfilUrl = String(reader.result || '');
    };
    reader.readAsDataURL(file);
  }

  seleccionarFoto(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.cargarFoto(input.files?.[0]);
  }

  soltarFoto(event: DragEvent): void {
    event.preventDefault();
    this.fotoDragging = false;
    this.cargarFoto(event.dataTransfer?.files[0]);
  }

  private procesarAuth(response: AuthResponse): void {
    this.tokenSesion = response.tokenSesion;
    this.usuario = response.usuario;
    this.usuarioId = response.usuario.idUsuario;
    localStorage.setItem('sigclin_token', response.tokenSesion);
    localStorage.setItem('sigclin_usuario_id', String(response.usuario.idUsuario));
    this.cargarPerfil(response.usuario);
    this.mensajeError = false;
    this.mensaje = `${response.mensaje}\nUsuario: ${response.usuario.nombres} ${response.usuario.apellidos}\nEmail: ${response.usuario.email}`;

    setTimeout(() => {
      this.mostrarAlertaEmail = false;
    }, 5000);
  }

  private procesarPerfil(usuario: PerfilUsuario, mensaje: string): void {
    this.usuario = usuario;
    this.cargarPerfil(usuario);
    this.mensajeError = false;
    this.mensaje = mensaje;
  }

  private cargarPerfil(usuario: PerfilUsuario): void {
    this.perfilForm = {
      nombres: usuario.nombres || '',
      apellidos: usuario.apellidos || '',
      fotoPerfilUrl: usuario.fotoPerfilUrl || '',
      preferencias: usuario.preferencias || '',
      notificacionesActivas: Boolean(usuario.notificacionesActivas)
    };
  }

  private mostrarError(error: { error?: unknown; message?: string; status?: number }): void {
    this.mensajeError = true;

    if (typeof error.error === 'string' && error.error.trim()) {
      try {
        const detalle = JSON.parse(error.error) as { message?: string };
        this.mensaje = detalle.message || error.error;
      } catch {
        this.mensaje = error.error;
      }
      return;
    }

    if (error.error && typeof error.error === 'object' && 'message' in error.error) {
      this.mensaje = String((error.error as { message?: unknown }).message);
      return;
    }

    if (error.status === 400 && this.panelActivo === 'login') {
      this.mensaje = 'Credenciales invalidas. Revisa tu email o contrasena.';
      return;
    }

    if (error.status === 400 && this.panelActivo === 'registro') {
      this.mensaje = 'No se pudo crear la cuenta. Revisa los datos o usa otro correo.';
      return;
    }

    if (error.status === 400 && this.panelActivo === 'recuperar') {
      this.mensaje = 'No se pudo enviar la solicitud. Verifica que el correo este registrado.';
      return;
    }

    this.mensaje = error.message || 'Ocurrio un error al procesar la solicitud.';
  }
}
