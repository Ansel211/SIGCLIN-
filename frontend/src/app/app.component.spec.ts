import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';

import { AppComponent } from './app.component';
import { AuthResponse, PerfilUsuario } from './models';
import { SigclinApiService } from './sigclin-api.service';

describe('AppComponent - Sprint 1 SIGCLIN', () => {
  let fixture: ComponentFixture<AppComponent>;
  let component: AppComponent;
  let api: jasmine.SpyObj<SigclinApiService>;

  // Usuario base usado para simular respuestas del backend durante las pruebas.
  const usuarioMock: PerfilUsuario = {
    idUsuario: 1,
    email: 'usuario@sigclin.pe',
    nombres: 'Marco',
    apellidos: 'Estudiante',
    fotoPerfilUrl: 'data:image/png;base64,AAA',
    preferencias: 'Atencion por correo',
    notificacionesActivas: true,
    emailVerificado: false
  };

  const authResponseMock: AuthResponse = {
    mensaje: 'Inicio de sesion correcto',
    tokenSesion: 'token-demo-123',
    usuario: usuarioMock
  };

  beforeEach(async () => {
    // Se crea un mock del servicio API para probar el componente sin llamar al backend real.
    api = jasmine.createSpyObj<SigclinApiService>('SigclinApiService', [
      'registrar',
      'login',
      'recuperarPassword',
      'verificarEmail',
      'actualizarPerfil',
      'configurarNotificaciones'
    ]);

    localStorage.clear();

    await TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [
        { provide: SigclinApiService, useValue: api }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    localStorage.clear();
  });

  // HU01: valida la navegacion entre los paneles principales del flujo de acceso.
  it('HU01 debe cambiar entre paneles de login, registro y recuperacion', () => {
    component.mostrarPanel('registro');
    expect(component.panelActivo).toBe('registro');
    expect(component.mensaje).toContain('crear una cuenta');

    component.mostrarPanel('recuperar');
    expect(component.panelActivo).toBe('recuperar');
    expect(component.mensaje).toContain('recuperacion');

    component.mostrarPanel('login');
    expect(component.panelActivo).toBe('login');
    expect(component.mensaje).toContain('credenciales');
  });

  // HU01: verifica que, al registrarse, se guarde la sesion devuelta por el backend.
  it('HU01 debe registrar usuario y guardar la sesion devuelta por el backend', () => {
    api.registrar.and.returnValue(of(authResponseMock));

    component.registroForm = {
      email: 'nuevo@sigclin.pe',
      password: '123456',
      nombres: 'Nuevo',
      apellidos: 'Usuario'
    };

    component.registrar();

    expect(api.registrar).toHaveBeenCalledWith(component.registroForm);
    expect(component.tokenSesion).toBe('token-demo-123');
    expect(component.usuarioId).toBe(1);
    expect(localStorage.getItem('sigclin_token')).toBe('token-demo-123');
  });

  // HU01: valida un mensaje claro cuando el registro no puede completarse.
  it('HU01 debe mostrar alerta clara si el registro es rechazado', () => {
    api.registrar.and.returnValue(throwError(() => ({ status: 400, message: 'Http failure response' })));
    component.mostrarPanel('registro');

    component.registrar();

    expect(component.mensaje).toBe('No se pudo crear la cuenta. Revisa los datos o usa otro correo.');
    expect(component.mensajeError).toBeTrue();
  });

  // HU02: valida el inicio de sesion correcto y la carga de datos del usuario.
  it('HU02 debe iniciar sesion con email y contrasena correctos', () => {
    api.login.and.returnValue(of(authResponseMock));

    component.loginForm = {
      email: 'usuario@sigclin.pe',
      password: '123456'
    };

    component.login();

    expect(api.login).toHaveBeenCalledWith(component.loginForm);
    expect(component.usuario?.email).toBe('usuario@sigclin.pe');
    expect(component.estadoSesion).toContain('Sesion activa');
  });

  // HU02: valida el mensaje mostrado cuando el backend rechaza las credenciales.
  it('HU02 debe mostrar mensaje de error si el login falla', () => {
    api.login.and.returnValue(throwError(() => ({ error: 'Credenciales invalidas' })));

    component.login();

    expect(component.mensaje).toBe('Credenciales invalidas');
    expect(component.mensajeError).toBeTrue();
  });

  // HU02: valida un mensaje entendible cuando el backend responde error 400.
  it('HU02 debe mostrar alerta clara si la contrasena es incorrecta', () => {
    api.login.and.returnValue(throwError(() => ({ status: 400, message: 'Http failure response' })));

    component.login();

    expect(component.mensaje).toBe('Credenciales invalidas. Revisa tu email o contrasena.');
    expect(component.mensajeError).toBeTrue();
  });

  // HU02: valida el flujo para solicitar recuperacion de contrasena por correo.
  it('HU02 debe solicitar recuperacion de contrasena por correo', () => {
    api.recuperarPassword.and.returnValue(of('Se genero solicitud de recuperacion'));
    component.recuperarEmail = 'usuario@sigclin.pe';

    component.recuperarPassword();

    expect(api.recuperarPassword).toHaveBeenCalledWith('usuario@sigclin.pe');
    expect(component.mensaje).toContain('Revisa tu correo');
    expect(component.mensajeError).toBeFalse();
  });

  // HU02: valida un mensaje claro cuando el correo de recuperacion no es aceptado.
  it('HU02 debe mostrar alerta clara si no se puede recuperar la cuenta', () => {
    api.recuperarPassword.and.returnValue(throwError(() => ({ status: 400, message: 'Http failure response' })));
    component.mostrarPanel('recuperar');

    component.recuperarPassword();

    expect(component.mensaje).toBe('No se pudo enviar la solicitud. Verifica que el correo este registrado.');
    expect(component.mensajeError).toBeTrue();
  });

  // HU02: valida que no se muestre el JSON tecnico devuelto por el backend.
  it('HU02 debe mostrar solo el mensaje si el backend devuelve un error JSON', () => {
    api.recuperarPassword.and.returnValue(throwError(() => ({
      status: 400,
      error: '{"error":"Solicitud no valida","message":"No existe usuario con ese email","status":400}'
    })));
    component.mostrarPanel('recuperar');

    component.recuperarPassword();

    expect(component.mensaje).toBe('No existe usuario con ese email');
    expect(component.mensajeError).toBeTrue();
  });

  // HU03: valida que el formulario de perfil envie solo los datos editables.
  it('HU03 debe actualizar datos de perfil del usuario autenticado', () => {
    const usuarioActualizado = {
      ...usuarioMock,
      nombres: 'Marco Antonio',
      preferencias: 'Recordatorio por correo'
    };
    api.actualizarPerfil.and.returnValue(of(usuarioActualizado));
    component.usuarioId = 1;
    component.perfilForm = {
      nombres: 'Marco Antonio',
      apellidos: 'Estudiante',
      fotoPerfilUrl: 'data:image/png;base64,BBB',
      preferencias: 'Recordatorio por correo',
      notificacionesActivas: true
    };

    component.guardarPerfil();

    expect(api.actualizarPerfil).toHaveBeenCalledWith(1, {
      nombres: 'Marco Antonio',
      apellidos: 'Estudiante',
      fotoPerfilUrl: 'data:image/png;base64,BBB',
      preferencias: 'Recordatorio por correo'
    });
    expect(component.mensaje).toBe('Perfil actualizado correctamente');
  });

  // HU03: valida que el usuario pueda cambiar su preferencia de notificaciones.
  it('HU03 debe actualizar la configuracion de notificaciones', () => {
    api.configurarNotificaciones.and.returnValue(of({ ...usuarioMock, notificacionesActivas: false }));
    component.usuarioId = 1;
    component.perfilForm.notificacionesActivas = false;

    component.guardarNotificaciones();

    expect(api.configurarNotificaciones).toHaveBeenCalledWith(1, false);
    expect(component.mensaje).toBe('Notificaciones actualizadas correctamente');
  });
});
