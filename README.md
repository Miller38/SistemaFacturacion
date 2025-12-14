# 📄 Sistema de Facturación – Java Desktop

## 🧾 Descripción general

El **Sistema de Facturación** es una aplicación de escritorio desarrollada en **Java**, orientada a la gestión administrativa y comercial de un negocio. Permite administrar **clientes**, **productos** y **facturas**, aplicando el patrón de arquitectura **MVC (Modelo – Vista – Controlador)** y utilizando **MySQL** como sistema gestor de base de datos.

El proyecto fue creado con fines académicos y prácticos, aplicando buenas prácticas de programación, Programación Orientada a Objetos (POO) y separación de responsabilidades mediante el uso de DAO.

---

## 🛠️ Tecnologías utilizadas

* **Lenguaje:** Java
* **JDK:** 20
* **Tipo de aplicación:** Escritorio
* **Arquitectura:** MVC
* **Base de datos:** MySQL
* **Conector JDBC:** mysql-connector-j-8.4.0
* **IDE recomendado:** NetBeans

---

## 📁 Estructura del proyecto

![Estructura](img/Estructura.jpg)


##  Patrón de diseño aplicado

###  MVC – Modelo, Vista, Controlador

* **Modelo**

  * Clases de dominio (Cliente, Producto, Factura)
  * Clases DAO para el acceso a la base de datos

* **Vista**

  * Interfaces gráficas
  * Interacción directa con el usuario

* **Controlador**

  * Lógica del negocio
  * Comunicación entre la vista y el modelo

Este patrón facilita el mantenimiento, la escalabilidad y la claridad del código.

---

## 🗄️ Base de datos

El sistema utiliza **MySQL** para el almacenamiento de la información.

### 📌 Script SQL (MySQL)

```sql
CREATE DATABASE sistema_facturacion;
USE sistema_facturacion;

CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    documento VARCHAR(20) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL
);

CREATE TABLE facturas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT,
    fecha DATE,
    total DECIMAL(10,2),
    FOREIGN KEY (id_cliente) REFERENCES clientes(id)
);

CREATE TABLE detalle_factura (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_factura INT,
    id_producto INT,
    cantidad INT,
    subtotal DECIMAL(10,2),
    FOREIGN KEY (id_factura) REFERENCES facturas(id),
    FOREIGN KEY (id_producto) REFERENCES productos(id)
);
```

📌 **Nota:** Este script debe ejecutarse en MySQL antes de iniciar la aplicación.

---

## 🖼️ Capturas de pantalla

### Inicio de sesión

![Login del sistema](capturas/login.png)

### Menú principal

![Menú principal](capturas/menu_principal.png)

### Gestión de clientes

![Gestión de clientes](capturas/clientes.png)

### Gestión de productos

![Gestión de productos](capturas/productos.png)

### Facturación

![Facturación](capturas/facturacion.png)

---

## 🚀 Cómo ejecutar el proyecto

1. Clonar o descargar el proyecto
2. Abrir el proyecto en **NetBeans**
3. Verificar que el archivo `mysql-connector-j-8.4.0.jar` esté agregado en **Libraries**
4. Crear la base de datos en MySQL ejecutando el script SQL
5. Configurar la conexión en la clase `Conexion.java`
6. Ejecutar la clase principal del sistema

---

## ✅ Funcionalidades principales

* Gestión de clientes (CRUD)
* Gestión de productos (CRUD)
* Creación y registro de facturas
* Detalle de productos por factura
* Conexión a base de datos MySQL
* Arquitectura MVC

---

## 📌 Buenas prácticas aplicadas

* Uso del patrón DAO
* Separación por capas
* Programación Orientada a Objetos (POO)
* Conexión centralizada a la base de datos
* Código organizado y legible

---

## 📚 Requisitos del sistema

* Java JDK 20 o superior
* MySQL Server
* NetBeans IDE
* Sistema operativo Windows / Linux

---

## 👨‍💻 Autor

Proyecto desarrollado con fines académicos y de aprendizaje en desarrollo de software utilizando Java y bases de datos relacionales.

---

## 📄 Licencia

Este proyecto es de uso académico y educativo. Puede ser modificado y reutilizado libremente con fines de aprendizaje.

---

⭐ *Este proyecto sirve como base para sistemas administrativos, comerciales y de facturación desarrollados en Java.*



