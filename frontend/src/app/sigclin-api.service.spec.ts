import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { SigclinApiService } from './sigclin-api.service';

describe('SigclinApiService', () => {
  let service: SigclinApiService;
  let httpMock: HttpTestingController;
  const apiUrl = 'http://localhost:8080/api/v1';

  // Se usa HttpClientTestingModule para probar las llamadas HTTP sin depender del backend real
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SigclinApiService]
    });

    service = TestBed.inject(SigclinApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  // HU01: valida que el frontend envie los datos de registro al endpoint correcto
  it('HU01 debe enviar registro al endpoint de autenticacion', () => {
    const request = {
      email: 'nuevo@sigclin.pe',
      password: '123456',
      nombres: 'Nuevo',
      apellidos: 'Usuario'
    };

    service.registrar(request).subscribe();

    const req = httpMock.expectOne(`${apiUrl}/auth/registro`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(request);
    req.flush({ mensaje: 'Registrado', tokenSesion: 'token', usuario: {} });
  });

  // HU02: valida que el login use POST y envie las credenciales esperadas
  it('HU02 debe enviar login al endpoint correcto', () => {
    const request = {
      email: 'usuario@sigclin.pe',
      password: '123456'
    };

    service.login(request).subscribe();

    const req = httpMock.expectOne(`${apiUrl}/auth/login`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(request);
    req.flush({ mensaje: 'Login correcto', tokenSesion: 'token', usuario: {} });
  });

  // HU02: la recuperacion devuelve texto plano, por eso se valida responseType text
  it('HU02 debe solicitar recuperacion de password como texto', () => {
    service.recuperarPassword('usuario@sigclin.pe').subscribe(response => {
      expect(response).toBe('Solicitud de recuperacion enviada');
    });

    const req = httpMock.expectOne(`${apiUrl}/auth/recuperar-password`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ email: 'usuario@sigclin.pe' });
    expect(req.request.responseType).toBe('text');
    req.flush('Solicitud de recuperacion enviada');
  });

  // HU03: valida que los cambios del perfil se envien al usuario autenticado
  it('HU03 debe actualizar perfil en el endpoint de perfil', () => {
    const request = {
      nombres: 'Marco',
      apellidos: 'Estudiante',
      fotoPerfilUrl: 'data:image/png;base64,AAA',
      preferencias: 'Recordatorios'
    };

    service.actualizarPerfil(1, request).subscribe();

    const req = httpMock.expectOne(`${apiUrl}/perfil/1`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(request);
    req.flush({ idUsuario: 1, email: 'usuario@sigclin.pe' });
  });

  // HU03: valida que la configuracion de notificaciones llegue al endpoint correcto
  it('HU03 debe actualizar notificaciones del usuario', () => {
    service.configurarNotificaciones(1, true).subscribe();

    const req = httpMock.expectOne(`${apiUrl}/perfil/1/notificaciones`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual({ activas: true });
    req.flush({ idUsuario: 1, notificacionesActivas: true });
  });
});
