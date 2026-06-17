import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import {
  AuthResponse,
  LoginRequest,
  PerfilUpdateRequest,
  PerfilUsuario,
  RegistroRequest
} from './models';

@Injectable({ providedIn: 'root' })
export class SigclinApiService {
  private readonly apiUrl = 'http://localhost:8080/api/v1';

  constructor(private readonly http: HttpClient) {}

  registrar(request: RegistroRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/registro`, request);
  }

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/login`, request);
  }

  recuperarPassword(email: string): Observable<string> {
    return this.http.post(`${this.apiUrl}/auth/recuperar-password`, { email }, { responseType: 'text' });
  }

  verificarEmail(idUsuario: number): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/auth/verificar-email/${idUsuario}`, {});
  }

  actualizarPerfil(idUsuario: number, request: PerfilUpdateRequest): Observable<PerfilUsuario> {
    return this.http.put<PerfilUsuario>(`${this.apiUrl}/perfil/${idUsuario}`, request);
  }

  configurarNotificaciones(idUsuario: number, activas: boolean): Observable<PerfilUsuario> {
    return this.http.put<PerfilUsuario>(`${this.apiUrl}/perfil/${idUsuario}/notificaciones`, { activas });
  }
}
