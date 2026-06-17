export interface PerfilUsuario {
  idUsuario: number;
  email: string;
  nombres: string;
  apellidos: string;
  fotoPerfilUrl?: string;
  preferencias?: string;
  notificacionesActivas: boolean;
  emailVerificado: boolean;
}

export interface AuthResponse {
  mensaje: string;
  tokenSesion: string;
  usuario: PerfilUsuario;
}

export interface RegistroRequest {
  email: string;
  password: string;
  nombres: string;
  apellidos: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface PerfilUpdateRequest {
  nombres: string;
  apellidos: string;
  fotoPerfilUrl?: string;
  preferencias?: string;
}
