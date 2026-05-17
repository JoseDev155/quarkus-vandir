CREATE DATABASE vandir_db;
USE vandir_db;
-- ------------------------------------------------------------------------------
-- 1. MÓDULO DE SEGURIDAD Y CONFIGURACIÓN (Panel de Administrador)
-- ------------------------------------------------------------------------------
-- Tabla de Usuarios (Soporta Login y Gestión de Usuarios)
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL, -- Aquí guardarás el hash de la contraseña
    rol ENUM('Administrador', 'Gerente', 'Vendedor') NOT NULL,
    estado ENUM('Activo', 'Bloqueado') DEFAULT 'Activo',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Configuración Global (Soporta la pantalla de Configuración)
CREATE TABLE configuracion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_empresa VARCHAR(150) NOT NULL,
    telefono_contacto VARCHAR(20),
    impuesto_iva DECIMAL(5,2) DEFAULT 13.00,
    moneda VARCHAR(10) DEFAULT 'USD ($)',
    ultima_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla de Registro de Respaldos (Soporta la pantalla de Respaldos)
CREATE TABLE respaldos_log (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_archivo VARCHAR(255) NOT NULL,
    fecha_generacion DATETIME NOT NULL,
    tamano_mb DECIMAL(8,2) NOT NULL,
    tipo ENUM('Automático', 'Manual') DEFAULT 'Manual',
    generado_por INT NULL,
    FOREIGN KEY (generado_por) REFERENCES usuarios(id) ON DELETE SET NULL
);

-- ------------------------------------------------------------------------------
-- 2. MÓDULO DE VENTAS Y CLIENTES (Panel de Vendedor)
-- ------------------------------------------------------------------------------

-- Tabla de Clientes (Soporta la pantalla "Mis Clientes")
CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ------------------------------------------------------------------------------
-- 3. MÓDULO DE INVENTARIO (Pantallas Consultar Precios e Inventario Global)
-- ------------------------------------------------------------------------------

-- Tabla de Categorías (Ayuda a organizar el inventario)
CREATE TABLE categorias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT
);

-- Tabla de Productos 
CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    categoria_id INT,
    stock_actual INT DEFAULT 0,
    stock_minimo INT DEFAULT 5, -- Para disparar las alertas rojas en el Dashboard del Gerente
    precio_unitario DECIMAL(10, 2) NOT NULL,
    estado ENUM('Activo', 'Inactivo') DEFAULT 'Activo',
    FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE SET NULL
);

-- ------------------------------------------------------------------------------
-- 4. MÓDULO TRANSACCIONAL (Punto de Venta y Reportes de Ventas)
-- ------------------------------------------------------------------------------

-- Cabecera de la Venta (Guarda los datos generales del ticket)
CREATE TABLE ventas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo_ticket VARCHAR(20) UNIQUE NOT NULL,
    usuario_id INT NOT NULL, -- ¿Qué vendedor hizo la venta?
    cliente_id INT NULL,     -- Puede ser nulo si es un "Cliente Frecuente/Anónimo"
    subtotal DECIMAL(10,2) NOT NULL,
    total_iva DECIMAL(10,2) NOT NULL,
    total_venta DECIMAL(10,2) NOT NULL,
    metodo_pago ENUM('Efectivo', 'Tarjeta de Crédito', 'Transferencia') NOT NULL,
    estado ENUM('Completado', 'Anulado') DEFAULT 'Completado',
    fecha_venta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE RESTRICT,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE SET NULL
);

-- Detalle de la Venta (Guarda qué productos exactos iban en el carrito)
CREATE TABLE detalle_ventas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    venta_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL,
    precio_aplicado DECIMAL(10,2) NOT NULL, -- Guarda el precio al momento de la venta (por si luego cambia en el inventario)
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (venta_id) REFERENCES ventas(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE RESTRICT
);

-- ------------------------------------------------------------------------------
-- 5. MÓDULO DE PROVEEDORES (Pantalla Órdenes de Compra - Gerente)
-- ------------------------------------------------------------------------------

-- Tabla de Proveedores
CREATE TABLE proveedores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    contacto VARCHAR(100),
    telefono VARCHAR(20),
    email VARCHAR(100)
);

-- Tabla de Órdenes de Compra
CREATE TABLE ordenes_compra (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo_orden VARCHAR(20) UNIQUE NOT NULL,
    proveedor_id INT NOT NULL,
    generada_por INT NOT NULL, -- ¿Qué gerente la pidió?
    total_estimado DECIMAL(10,2) NOT NULL,
    estado ENUM('Borrador', 'Enviada', 'En Tránsito', 'Recibida', 'Cancelada') DEFAULT 'Borrador',
    fecha_solicitud TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_recepcion DATETIME NULL,
    FOREIGN KEY (proveedor_id) REFERENCES proveedores(id) ON DELETE RESTRICT,
    FOREIGN KEY (generada_por) REFERENCES usuarios(id) ON DELETE RESTRICT
);


-- ==============================================================================
-- INSERCIÓN DE DATOS INICIALES (Para que puedas hacer login y pruebas)
-- ==============================================================================

-- 1. Insertar la configuración por defecto
INSERT INTO configuracion (nombre_empresa, telefono_contacto, impuesto_iva, moneda) 
VALUES ('Vandir Store S.A. de C.V.', '+503 2222-0000', 13.00, 'USD ($)');

-- 2. Insertar usuarios de prueba (Nota: En producción, el password debe estar hasheado)
INSERT INTO usuarios (nombre, email, password, rol) VALUES 
('Administrador Principal', 'admin@vandir.com', 'admin123', 'Administrador'),
('Laura Mendez', 'gerente@vandir.com', 'gerente123', 'Gerente'),
('Carlos Perez', 'vendedor@vandir.com', 'vendedor123', 'Vendedor');

-- 3. Insertar algunas categorías y productos iniciales
INSERT INTO categorias (nombre, descripcion) VALUES 
('Herramientas Eléctricas', 'Taladros, sierras, etc.'),
('Audio Automotriz', 'Bocinas, amplificadores y estéreos'),
('Accesorios', 'Cables, brocas, fusibles');

INSERT INTO productos (codigo, nombre, categoria_id, stock_actual, stock_minimo, precio_unitario) VALUES 
('HER-001', 'Taladro Inalámbrico 20V', 1, 15, 5, 120.00),
('AUD-001', 'Set de Bocinas 6.5 pulgadas', 2, 8, 4, 45.50),
('ACC-001', 'Set de Brocas (10 piezas)', 3, 2, 5, 15.00); -- Este disparará alerta por stock bajo