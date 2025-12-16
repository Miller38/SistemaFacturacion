package modelo;

import java.math.BigDecimal;

/**
 *
 * @author Miller
 */
public class Producto {

    //  ATRIBUTOS (Características de un producto)
    private int idProducto;                      // ID único en la base de datos
    private String codigo;                       // Código interno (ej: "PROD-001")
    private String nombre;                      // Nombre del producto
    private String descripcion;                // Descripción detallada
    private BigDecimal precio;              // Precio de venta (usa BigDecimal para dinero)
    private int stock;                             // Cantidad disponible
    private int stockMinimo;                // Mínimo antes de alertar
    private BigDecimal ivaPorcentaje; // Porcentaje de IVA (ej: 12.00)
    private String estado;                    // "ACTIVO" o "INACTIVO"
    
    // ️ CONSTRUCTORES (Métodos para crear objetos Producto)
    public Producto()   {
         // Constructor vacío para crear productos sin datos iniciales
         this.precio = BigDecimal.ZERO;
         this.ivaPorcentaje = new BigDecimal("12.00");
        this.estado = "ACTIVO";
    }

    public Producto(String codigo, String nombre, BigDecimal precio, int stock) {
        // Constructor para crear productos con datos básicos
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.ivaPorcentaje = new BigDecimal("12.00");
        this.estado = "ACTIVO";
        this.stockMinimo = 5;
    }
    
     // GETTERS Y SETTERS (Acceder y modificar atributos)

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public BigDecimal getIvaPorcentaje() {
        return ivaPorcentaje;
    }

    public void setIvaPorcentaje(BigDecimal ivaPorcentaje) {
        this.ivaPorcentaje = ivaPorcentaje;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    // MÉTODOS DE CÁLCULO
    public BigDecimal getPrecioConIva() {
        // Calcula: precio + (precio × iva_porcentaje / 100)
        if (precio != null && ivaPorcentaje != null) {
            BigDecimal ivaMonto = precio.multiply(ivaPorcentaje)
                                        .divide(new BigDecimal("100"));
            return precio.add(ivaMonto);
        }
        return precio;
    }
    
    public boolean tieneStockSuficiente(int cantidadRequerida) {
        // Verifica si hay suficiente stock para una venta
        return stock >= cantidadRequerida;
    }
    
    public boolean necesitaReponer() {
        // Alertar cuando el stock está por debajo del mínimo
        return stock <= stockMinimo;
    }
    
    // MÉTODO PARA MOSTRAR EL PRODUCTO COMO TEXTO
    @Override
    public String toString() {
        return codigo + " - " + nombre + " ($" + precio + ")";
    }
    
}
