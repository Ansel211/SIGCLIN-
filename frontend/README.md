# SIGCLIN Frontend Angular

Frontend del Sprint 1 construido con Angular y Bootstrap.

## Historias cubiertas

- HU01: Registro e inicio de sesion.
- HU02: Gestion de perfil de usuario.

## Ejecutar

Primero levanta el backend Spring Boot en `http://localhost:8080`.

Luego:

```powershell
cd "C:\Users\marco\My project\SIGCLIN-\frontend"
npm install
npm start
```

Angular abrira normalmente en:

```text
http://localhost:4200
```

El frontend consume la API REST del backend:

```text
http://localhost:8080/api/v1
```
