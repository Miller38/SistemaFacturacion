-- 1. CREAR BASE DATOS
CREATE DATABASE sistema_facturacion;
USE sistema_facturacion;

-- 2. TABLA CLIENTES - Guarda informacion de los clientes
CREATE TABLE clientes (
	id_cliente INT PRIMARY KEY AUTO_INCREMENT,   -- ID único autoincremental
    cedula VARCHAR(20) UNIQUE NOT NULL,         -- RUC o cédula (único)
    nombre VARCHAR(100) NOT NULL,               -- Nombre completo
    apellido VARCHAR(100) NOT NULL,             -- Apellido completo
    direccion VARCHAR(100),                     -- Dirección
    telefono VARCHAR(15),                       -- Teléfono
    email VARCHAR(50),                           -- Correo electrónico
    estado ENUM('ACTIVO','INACTIVO') DEFAULT 'ACTIVO',     -- Estado del cliente
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP     -- Fecha de registro
);

-- 📦 3. TABLA PRODUCTOS - Guarda información de productos para vender
CREATE TABLE productos (
	id_producto INT PRIMARY KEY AUTO_INCREMENT,
	codigo VARCHAR(20) UNIQUE NOT NULL,            -- Código único del producto
    nombre VARCHAR(100) NOT NULL,                  -- Nombre del producto
    descripcion TEXT,                              -- Descripción detallada
    precio DECIMAL(10,2) NOT NULL CHECK (precio >= 0),     -- Precio (no negativo)
    stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0),       -- Cantidad disponible
    stock_minimo INT DEFAULT 5,                            -- Alerta cuando stock baja de este número
    iva_porcentaje DECIMAL(5,2) DEFAULT 12.00,             -- Porcentaje de IVA (ej: 12%)
    estado ENUM ('ACTIVO','INACTIVO') DEFAULT 'ACTIVO'
);

-- 🧾 4. TABLA FACTURAS (CABECERA) - Encabezado de cada factura
CREATE TABLE facturas(
	id_factura INT PRIMARY KEY AUTO_INCREMENT,          
    numero_factura VARCHAR (20) UNIQUE NOT NULL,         -- Número único: 001-001-000000001
    id_cliente INT NOT NULL,                             -- Cliente que compra
    fecha_emision DATETIME DEFAULT CURRENT_TIMESTAMP,    -- Fecha y hora automática
    subtotal DECIMAL(10,2) NOT NULL DEFAULT 0,           -- Suma de precios sin IVA
    iva DECIMAL(10,2) NOT NULL DEFAULT 0,                -- Total de IVA
    total DECIMAL(10,2) NOT NULL DEFAULT 0,              -- Subtotal + IVA
    forma_pago ENUM('EFECTIVO','TARJETA','TRANSFERENCIA') DEFAULT 'EFECTIVO',
    estado ENUM('PENDIENTE','PAGADA','ANULADA') DEFAULT 'PENDIENTE',
    observaciones TEXT,                                  -- Notas adicionales
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)        -- Relación con clientes
);

-- 📝 5. TABLA DETALLES_FACTURA - Productos individuales en una factura
CREATE TABLE detalles_factura(
	id_detalle INT PRIMARY KEY AUTO_INCREMENT,
    id_factura INT NOT NULL,              -- Factura a la que pertenece
    id_producto INT NOT NULL,             -- Producto vendido
    cantidad INT NOT NULL CHECK (cantidad > 0),      -- Cantidad comprada (mínimo 1)
    precio_unitario DECIMAL(10,2) NOT NULL,          -- Precio en el momento de la venta
    subtotal DECIMAL(10,2) NOT NULL,                 -- precio_unitario × cantidad
    iva DECIMAL(10,2) NOT NULL,                      -- IVA de este item
    total DECIMAL(10,2) NOT NULL,                    -- subtotal + iva
    FOREIGN KEY (id_factura) REFERENCES facturas(id_factura) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);

-- 🔢 6. TABLA SECUENCIALES - Controla los números de factura
CREATE TABLE secuenciales_factura(
	id_secuencial INT PRIMARY KEY AUTO_INCREMENT,
    establecimiento VARCHAR(3) NOT NULL DEFAULT '001',      -- Código establecimiento
    punto_emision VARCHAR(3) NOT NULL DEFAULT '001',        -- Código punto emisión
    secuencial_actual INT NOT NULL DEFAULT 0                -- Último número usado
);

-- 📊 7. INSERTAR DATOS DE PRUEBA
INSERT INTO clientes(cedula, nombre, telefono) VALUES
('0999999999', 'Cliente General', '0991234567'),
('0999999998', 'Empresa XYZ S.A.', '0997654321');

SELECT * FROM clientes WHERE cedula = '0999999998';
SELECT * FROM clientes;

INSERT INTO productos (codigo, nombre, precio, stock) VALUES 
('PROD-001', 'Laptop Dell Inspiron', 899.99, 10),
('PROD-002', 'Mouse Inalámbrico', 25.50, 50),
('PROD-003', 'Teclado Mecánico', 45.00, 30);

SELECT * FROM productos;
SELECT * FROM productos WHERE codigo = 'PROD-001';

INSERT INTO secuenciales_factura VALUES (1, '001', '001', 0);

SELECT * FROM secuenciales_factura;

show tables;

-- 🎯 8. TRIGGER - Reduce stock automáticamente al vender
DELIMITER $$
CREATE TRIGGER reducir_stock_venta
AFTER INSERT ON detalles_factura
FOR EACH ROW
BEGIN
    -- Resta la cantidad vendida del stock disponible
    UPDATE productos 
    SET stock = stock - NEW.cantidad 
    WHERE id_producto = NEW.id_producto;
END$$
DELIMITER ;