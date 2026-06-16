const api = "http://localhost:8080/api/v1";
let usuarioId = localStorage.getItem("sigclin_usuario_id") || "";
let notificaciones = true;

function tokenLabel() {
  const node = document.getElementById("tokenLabel");
  if (!node) return;
  const token = localStorage.getItem("sigclin_token");
  node.textContent = token
    ? "Sesion activa: " + token.slice(0, 8) + "..."
    : usuarioId ? "Sesion activa" : "Sin sesion activa";
}

function escribirResultado(texto) {
  const perfilVisible = !document.getElementById("profileSection")?.classList.contains("hidden");
  const salida = perfilVisible
    ? document.getElementById("perfilSalida")
    : document.getElementById("salida");
  if (salida) salida.textContent = texto;
}

function actualizarEstadoSesion() {
  const token = localStorage.getItem("sigclin_token");
  const tieneSesion = Boolean(token || usuarioId);
  const authIntro = document.getElementById("authIntro");
  const authCard = document.getElementById("authCard");
  const profileSection = document.getElementById("profileSection");
  const profileHelp = document.getElementById("profileHelp");

  authIntro?.classList.toggle("hidden", tieneSesion);
  authCard?.classList.toggle("hidden", tieneSesion);
  profileSection?.classList.toggle("hidden", !tieneSesion);
  document.querySelectorAll("[data-requires-session]").forEach(node => {
    node.disabled = !tieneSesion;
  });
  if (profileHelp) {
    profileHelp.textContent = tieneSesion
      ? "Sesion iniciada. Desde aqui se completa HU02: edicion de perfil, preferencias, notificaciones y almacenamiento seguro."
      : "";
  }
  actualizarVistaFoto();
}

function mostrar(data) {
  if (typeof data === "string") {
    escribirResultado(data);
  } else {
    const usuario = data.usuario || data;
    cargarPerfilEnFormulario(usuario);
    const lineas = [
      data.mensaje ? "Mensaje: " + data.mensaje : "",
      usuario.email ? "Email: " + usuario.email : "",
      usuario.nombres ? "Usuario: " + usuario.nombres + " " + (usuario.apellidos || "") : "",
      usuario.emailVerificado !== undefined ? "Email verificado: " + (usuario.emailVerificado ? "Si" : "No") : "",
      usuario.notificacionesActivas !== undefined ? "Notificaciones: " + (usuario.notificacionesActivas ? "Activas" : "Inactivas") : "",
      (data.tokenSesion || data.token) ? "Token generado: " + (data.tokenSesion || data.token).slice(0, 12) + "..." : ""
    ].filter(Boolean);
    escribirResultado(lineas.length ? lineas.join("\n") : JSON.stringify(data, null, 2));
    if (usuario.emailVerificado !== undefined) actualizarEmailBadge(usuario.emailVerificado);
  }
  tokenLabel();
  actualizarEstadoSesion();
}

function cargarPerfilEnFormulario(usuario) {
  if (!usuario || typeof usuario !== "object") return;
  if (usuario.nombres && document.getElementById("perfilNombres")) perfilNombres.value = usuario.nombres;
  if (usuario.apellidos && document.getElementById("perfilApellidos")) perfilApellidos.value = usuario.apellidos;
  if (usuario.fotoPerfilUrl && document.getElementById("perfilFoto")) perfilFoto.value = usuario.fotoPerfilUrl;
  if (usuario.preferencias && document.getElementById("perfilPreferencias")) perfilPreferencias.value = usuario.preferencias;
  if (usuario.notificacionesActivas !== undefined && document.getElementById("perfilNotificaciones")) {
    perfilNotificaciones.checked = Boolean(usuario.notificacionesActivas);
    notificaciones = Boolean(usuario.notificacionesActivas);
  }
  actualizarVistaFoto();
}

function actualizarVistaFoto() {
  const input = document.getElementById("perfilFoto");
  const preview = document.getElementById("perfilFotoPreview");
  if (!input || !preview) return;
  const url = input.value.trim();
  preview.src = url || "";
  preview.classList.toggle("empty", !url);
}

function cargarFotoPerfil(file) {
  if (!file) return;
  if (!file.type.startsWith("image/")) {
    return mostrar("Selecciona un archivo de imagen PNG o JPG.");
  }
  if (file.size > 600 * 1024) {
    return mostrar("La imagen es muy grande para esta demo. Usa una foto menor a 600 KB.");
  }
  const reader = new FileReader();
  reader.onload = () => {
    const input = document.getElementById("perfilFoto");
    if (input) input.value = reader.result;
    actualizarVistaFoto();
  };
  reader.readAsDataURL(file);
}

function prepararCargaFoto() {
  const dropZone = document.getElementById("fotoDropZone");
  if (!dropZone) return;
  ["dragenter", "dragover"].forEach(eventName => {
    dropZone.addEventListener(eventName, event => {
      event.preventDefault();
      dropZone.classList.add("dragging");
    });
  });
  ["dragleave", "drop"].forEach(eventName => {
    dropZone.addEventListener(eventName, event => {
      event.preventDefault();
      dropZone.classList.remove("dragging");
    });
  });
  dropZone.addEventListener("drop", event => {
    cargarFotoPerfil(event.dataTransfer.files[0]);
  });
}

function actualizarEmailBadge(verificado) {
  const badge = document.getElementById("emailBadge");
  if (!badge) return;
  badge.textContent = verificado ? "Email verificado" : "Email pendiente";
  badge.className = verificado ? "badge ok" : "badge";
}

function mostrarPanelAuth(panel) {
  const paneles = {
    login: document.getElementById("panelLogin"),
    registro: document.getElementById("panelRegistro"),
    recuperar: document.getElementById("panelRecuperar")
  };
  const tabs = {
    login: document.getElementById("tabLogin"),
    registro: document.getElementById("tabRegistro"),
    recuperar: document.getElementById("tabRecuperar")
  };
  Object.values(paneles).forEach(node => node?.classList.remove("active"));
  Object.values(tabs).forEach(node => node?.classList.remove("active"));
  paneles[panel]?.classList.add("active");
  tabs[panel]?.classList.add("active");
}

async function requestJson(url, method, body) {
  const res = await fetch(url, {
    method,
    headers: { "Content-Type": "application/json" },
    body: body ? JSON.stringify(body) : undefined
  });
  const text = await res.text();
  try { return JSON.parse(text); } catch { return text; }
}

async function registrar() {
  const data = await requestJson(api + "/auth/registro", "POST", {
    email: regEmail.value,
    password: regPassword.value,
    nombres: regNombres.value,
    apellidos: regApellidos.value
  });
  guardarUsuario(data);
  mostrar(data);
}

async function login() {
  const data = await requestJson(api + "/auth/login", "POST", {
    email: loginEmail.value,
    password: loginPassword.value
  });
  if (data.tokenSesion || data.token) localStorage.setItem("sigclin_token", data.tokenSesion || data.token);
  guardarUsuario(data);
  mostrar(data);
}

function guardarUsuario(data) {
  if (data.tokenSesion || data.token) {
    localStorage.setItem("sigclin_token", data.tokenSesion || data.token);
  }
  if (data.usuario?.idUsuario) {
    usuarioId = data.usuario.idUsuario;
    localStorage.setItem("sigclin_usuario_id", usuarioId);
    actualizarEstadoSesion();
  }
}

async function verificarEmail() {
  if (!usuarioId) return mostrar("Primero registra o inicia sesion para obtener idUsuario.");
  const data = await requestJson(api + "/auth/verificar-email/" + usuarioId, "POST", {});
  mostrar(data);
}

async function recuperarPassword() {
  const recEmail = document.getElementById("recEmail");
  const loginEmail = document.getElementById("loginEmail");
  const email = recEmail?.value || loginEmail?.value || "";
  mostrar(await requestJson(api + "/auth/recuperar-password", "POST", { email }));
}

function cerrarSesion() {
  localStorage.removeItem("sigclin_token");
  localStorage.removeItem("sigclin_usuario_id");
  usuarioId = "";
  mostrarPanelAuth("login");
  mostrar("Sesion cerrada. Ingresa nuevamente para editar tu perfil.");
  actualizarEmailBadge(false);
  tokenLabel();
  actualizarEstadoSesion();
}

async function actualizarPerfil() {
  if (!usuarioId) return mostrar("Primero registra o inicia sesion para obtener idUsuario.");
  const data = await requestJson(api + "/perfil/" + usuarioId, "PUT", {
    nombres: perfilNombres.value,
    apellidos: perfilApellidos.value,
    fotoPerfilUrl: perfilFoto.value,
    preferencias: perfilPreferencias.value
  });
  mostrar(data);
}

async function guardarNotificaciones() {
  if (!usuarioId) return mostrar("Primero registra o inicia sesion para obtener idUsuario.");
  const checkbox = document.getElementById("perfilNotificaciones");
  notificaciones = checkbox ? checkbox.checked : !notificaciones;
  mostrar(await requestJson(api + "/perfil/" + usuarioId + "/notificaciones", "PUT", { activas: notificaciones }));
}

async function cargarUsuarios() {
  const body = document.getElementById("usuariosBody");
  if (!body) return;
  const res = await fetch(api + "/demo/usuarios");
  const usuarios = await res.json();
  if (!usuarios.length) {
    body.innerHTML = '<tr><td colspan="6">Aun no hay usuarios registrados.</td></tr>';
    return;
  }
  body.innerHTML = usuarios.map(u => `
    <tr>
      <td>${u.idUsuario}</td>
      <td>${u.email}</td>
      <td>${u.nombres || ""}</td>
      <td>${u.apellidos || ""}</td>
      <td>${u.emailVerificado ? "Si" : "No"}</td>
      <td>${u.notificacionesActivas ? "Activas" : "Inactivas"}</td>
    </tr>
  `).join("");
}

tokenLabel();
actualizarEstadoSesion();
prepararCargaFoto();
