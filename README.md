# SIGCLIN

Proyecto SIGCLIN - Sprint 1.

Este README es para levantar el sistema y correr las pruebas principales.

## 1. Abrir el proyecto

Abrir la carpeta principal:

```powershell
cd "C:\Users\####\My project\SIGCLIN-"
```

Dentro deben estar estas carpetas:

```text
backend
frontend
```

Nota: el archivo `sigclin_db.backup` es respaldo de base de datos. No borrarlo

## 2. Levantar el backend

Abrir una terminal y ejecutar:

```powershell
cd "C:\Users\###\My project\SIGCLIN-\backend"
mvnw.cmd spring-boot:run
```

Debe aparecer algo como:

```text
Tomcat started on port 8080
Started BackendApplication
```

Dejar esta terminal abierta.

## 3. Levantar el frontend

Abrir otra terminal y ejecutar:

```powershell
cd "C:\Users\####\My project\SIGCLIN-\frontend"
npm install
npm start
```

Normalmente abre en:

```text
http://localhost:4200
```

Si Angular muestra otro puerto, usar el link que salga en la terminal.

## 4. Probar la aplicacion

Con backend y frontend levantados, probar:

- registro de usuario;
- login;
- recuperacion de cuenta;
- perfil y notificaciones.

## 5. Tests del frontend

En una terminal:

```powershell
cd "C:\Users\marco\My project\SIGCLIN-\frontend"
npm.cmd test -- --watch=false --browsers=ChromeHeadless
```

Resultado esperado:

```text
TOTAL: 16 SUCCESS
```

Para ver Karma en navegador:

```powershell
npm.cmd test
```

Abrir:

```text
http://localhost:9876
```

## 6. Tests del backend

En una terminal:

```powershell
cd "C:\Users\marco\My project\SIGCLIN-\backend"
mvnw.cmd test
```

Resultado esperado:

```text
Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## 7. Puertos importantes

```text
Frontend: http://localhost:4200
Backend:  http://localhost:8080
Karma:    http://localhost:9876
```

`localhost:4200` es la aplicacion.

`localhost:9876` es solo para ver pruebas de Karma.
