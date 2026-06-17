

CREATE DATABASE sigclin_db;


CREATE TABLE IF NOT EXISTS usuario (
    id_usuario BIGSERIAL PRIMARY KEY,
    email VARCHAR(120) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    nombres VARCHAR(120) NOT NULL,
    apellidos VARCHAR(120),
    foto_perfil_url TEXT,
    preferencias VARCHAR(250),
    notificaciones_activas BOOLEAN DEFAULT TRUE,
    email_verificado BOOLEAN DEFAULT FALSE,
    token_sesion VARCHAR(120),
    token_recuperacion VARCHAR(120),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_usuario_email ON usuario(email);
CREATE INDEX IF NOT EXISTS idx_usuario_token_sesion ON usuario(token_sesion);

CREATE TABLE IF NOT EXISTS paciente (
    id_paciente BIGSERIAL PRIMARY KEY,
    nombres VARCHAR(120),
    apellido_paterno VARCHAR(120),
    apellido_materno VARCHAR(120),
    num_documento VARCHAR(30),
    telefono VARCHAR(30),
    direccion VARCHAR(200),
    fecha_nacimiento DATE
);

CREATE TABLE IF NOT EXISTS historia_clinica (
    id_historia_clinica BIGSERIAL PRIMARY KEY,
    id_paciente BIGINT,
    codigo VARCHAR(50),
    fecha_apertura DATE,
    antecedentes VARCHAR(255),
    estado VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS medico (
    id_medico BIGSERIAL PRIMARY KEY,
    cmp VARCHAR(30),
    nombres VARCHAR(120),
    especialidad VARCHAR(120),
    estado VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS cita (
    id_cita BIGSERIAL PRIMARY KEY,
    id_paciente BIGINT,
    id_medico BIGINT,
    fecha DATE,
    hora TIME,
    especialidad VARCHAR(120),
    estado VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS orden_atencion (
    id_orden_atencion BIGSERIAL PRIMARY KEY,
    id_cita BIGINT,
    id_medico BIGINT,
    tipo VARCHAR(60),
    fecha TIMESTAMP,
    estado VARCHAR(30),
    motivo VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS receta (
    id_receta BIGSERIAL PRIMARY KEY,
    id_orden_atencion BIGINT,
    codigo VARCHAR(50),
    fecha DATE,
    indicaciones VARCHAR(255),
    estado VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS medicamento (
    id_medicamento BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(50),
    nombre VARCHAR(120),
    presentacion VARCHAR(120),
    stock INTEGER,
    precio NUMERIC(12,2)
);

CREATE TABLE IF NOT EXISTS stock_medicamento (
    id_stock_medicamento BIGSERIAL PRIMARY KEY,
    id_medicamento BIGINT,
    cantidad_disponible INTEGER,
    stock_minimo INTEGER,
    ubicacion VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS orden_medicamento (
    id_orden_medicamento BIGSERIAL PRIMARY KEY,
    id_receta BIGINT,
    id_medicamento BIGINT,
    fecha DATE,
    cantidad INTEGER,
    estado VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS deuda (
    id_deuda BIGSERIAL PRIMARY KEY,
    id_orden_atencion BIGINT,
    concepto VARCHAR(120),
    monto NUMERIC(12,2),
    estado VARCHAR(30),
    fecha_emision DATE
);

CREATE TABLE IF NOT EXISTS pago (
    id_pago BIGSERIAL PRIMARY KEY,
    id_deuda BIGINT,
    id_caja INTEGER,
    fecha TIMESTAMP,
    medio VARCHAR(60),
    monto NUMERIC(12,2),
    estado VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS comprobante (
    id_comprobante BIGSERIAL PRIMARY KEY,
    id_pago BIGINT,
    serie VARCHAR(20),
    numero VARCHAR(30),
    tipo VARCHAR(60),
    monto NUMERIC(12,2),
    fecha_emision TIMESTAMP
);

-- Datos de prueba opcionales.
INSERT INTO usuario (
    email,
    password_hash,
    nombres,
    apellidos,
    preferencias,
    notificaciones_activas,
    email_verificado
) VALUES (
    'usuario@sigclin.pe',
    '$2a$10$demo.hash.solo.referencia',
    'Marco',
    'Estudiante',
    'Recordatorios por correo',
    TRUE,
    FALSE
) ON CONFLICT (email) DO NOTHING;
