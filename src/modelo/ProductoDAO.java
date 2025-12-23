
package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Miller
 */
public class ProductoDAO {
    // ----------------------------- MÉTODO PARA CREAR UN NUEVO PRODUCTO ----------------------------- //
    public boolean crear(Producto producto)  {
        Connection conn = null;
        PreparedStatement pst = null;
        
        try {
            // 1. Obtener conexión
            conn = Conexion.getConnection();
            
             // 2. Crear consulta SQL con parámetros (previene inyección SQL)
             String sql  = "INSERT INTO productos (codigo, nombre, descripcion, precio, stock, "
                     + "stock_minimo, iva_porcentaje) VALUES (?, ?, ?, ?, ?, ?, ?)";
             
             // 3. Preparar la consulta
             pst = conn.prepareStatement(sql);
             
             // 4. Asignar valores a los parámetros (los ? en la consulta)
             pst.setString(1, producto.getCodigo());                      // ? 1 → codigo
             pst.setString(2,producto.getNombre());                      // ? 2 → nombre
             pst.setString(3,producto.getDescripcion());                // ? 3 → descripcion
             pst.setBigDecimal(4,producto.getPrecio());                   // ? 4 → precio
             pst.setInt(5,producto.getStock());                                  // ? 5 → stock
             pst.setInt(6,producto.getStockMinimo());                      // ? 6 → stock_minimo
             pst.setBigDecimal(7,producto.getIvaPorcentaje());      // ? 7 → iva_porcentaje
             
             // 5. Ejecutar la consulta
             int filasAfectadas = pst.executeUpdate();
             
             // 6. Retornar true si se insertó al menos una fila
             return filasAfectadas > 0;
             
        } catch (SQLException e) {
            System.err.println("Error al crear producto : " + e.getMessage());
            return false;
        } finally  {
            // 7. Cerrar recursos siempre (importante para no agotar conexiones)
            cerrarRecursos(conn, pst, null);
        }
    }
    
     // -----------------------------  MÉTODO PARA ACTUALIZAR UN PRODUCTO ------------------------------- //
    public boolean actualizar(Producto producto) {
        Connection conn = null;
        PreparedStatement pst = null;
        
        try {
            conn = Conexion.getConnection();
            String sql = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, "
                    + "stock = ?, stock_minimo = ?, ivaPorcentaje = ?, estado = ? WHERE id_producto = ? ";
            
            pst = conn.prepareStatement(sql);
            
            pst.setString(1,producto.getNombre());
            pst.setString(2,producto.getDescripcion());
            pst.setBigDecimal(3,producto.getPrecio());
            pst.setInt(4,producto.getStock());
            pst.setInt(5,producto.getStockMinimo());
            pst.setBigDecimal(6,producto.getIvaPorcentaje());
            pst.setString(7,producto.getEstado());
            pst.setInt(8,producto.getIdProducto());
            
            return pst.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto : " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(conn, pst, null);
        }
    }
    
    // --------------------------️ MÉTODO PARA "ELIMINAR" (marcar como inactivo) ----------------------------- //
    public boolean eliminar(int idProducto) {
        // No borra físicamente, solo cambia el estado a INACTIVO
        Connection conn = null;
        PreparedStatement pst = null;
        
        try {
            conn = Conexion.getConnection();
            String sql = "UPDATE productos SET estado = 'INACTIVO' WHERE idProducto = ?";
            
            pst = conn.prepareStatement(sql);
            
            pst.setInt(1, idProducto);
            
            return pst.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto : " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(conn, pst, null);
        }
    }
    
     // -------------- MÉTODO PARA BUSCAR PRODUCTO POR CÓDIGO (Usado al facturar) ------------------ //
    public Producto buscarPorCodigo(String codigo)  {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Producto producto = null;
        
        try {
            conn = Conexion.getConnection();
            String sql = "SELECT * FROM productos WHERE codigo = ? AND estado = 'ACTIVO'";
            
            pst = conn.prepareStatement(sql);
            pst.setString(1,codigo);                   // Busca por código
            rs = pst.executeQuery();                              // Ejecuta y obtiene resultados
            
            if(rs.next()) {
                // Si encontró el producto, crea un objeto Producto
                producto = new Producto();
                producto.setIdProducto(rs.getInt("id_producto"));
                producto.setCodigo(rs.getString("codigo"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setPrecio(rs.getBigDecimal("precio"));
                producto.setStock(rs.getInt("stock"));
                producto.setStockMinimo(rs.getInt("stock_minimo"));
               producto.setIvaPorcentaje(rs.getBigDecimal("iva_porcentaje"));
               producto.setEstado(rs.getString("estado"));
               
            } 
        } catch (SQLException e) {
            System.err.println("Error al buscar producto : " + e.getMessage());
        } finally {
            cerrarRecursos(conn, pst, null);
        }
        return producto;
    }
    
     // ------------------ MÉTODO PARA LISTAR TODOS LOS PRODUCTOS ACTIVOS --------------------------- //
    public List<Producto> listarTodos() {
        List<Producto> productos = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM productos WHERE estado = 'ACTIVO' ORDER BY nombre";
            
            rs = stmt.executeQuery(sql);
            
            while(rs.next()) {
                // Por cada fila en los resultados, crea un Producto
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("id_producto"));
                p.setCodigo(rs.getString("codigo"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setPrecio(rs.getBigDecimal("precio"));
                p.setStock(rs.getInt("stock"));
                p.setStockMinimo(rs.getInt("stock_minimo"));
                p.setIvaPorcentaje(rs.getBigDecimal("iva_porcentaje"));
                p.setEstado(rs.getString("estado"));
             
                productos.add(p);                    // Agrega a la lista
                        
            }
        } catch (SQLException e) {
            System.err.println("Error al listar productos : " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        return productos;
    }
    
     // -------------------- MÉTODO PARA BUSCAR POR NOMBRE (búsqueda parcial) ------------------------- //
    public List<Producto> buscarPorNombre(String nombre) {
        List<Producto> productos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConnection();
            // LIKE permite búsqueda parcial: "lap" encuentra "laptop"
            String sql = "SELECT * FROM productos WHERE nombre LIKE ? AND estado = 'ACTIVO'";
            
            pst = conn.prepareStatement(sql);
            pst.setString(1,"%"  + nombre + "%");                 // % significa "cualquier texto"
            rs = pst.executeQuery();
            
            while(rs.next())  {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("id_producto"));
                p.setCodigo(rs.getString("codigo"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setPrecio(rs.getBigDecimal("precio"));
                p.setStock(rs.getInt("stock"));
                p.setStockMinimo(rs.getInt("stock_minimo"));
                p.setIvaPorcentaje(rs.getBigDecimal("iva_porcentaje"));
                p.setEstado(rs.getString("estado"));
                
                productos.add(p);                
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar productos : " + e.getMessage());
        } finally {
            cerrarRecursos(conn, pst, rs);
        }
        return productos;
    }
    
    // -------------------- MÉTODO PARA PRODUCTOS CON STOCK BAJO (Alertas) -------------------------- //
    public List<Producto> listarStockBajo() {
        List<Producto> productos = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Conexion.getConnection();
            stmt = conn.createStatement();
             // Filtra productos donde stock <= stock_minimo
             String sql = "SELECT * FROM productos WHERE stock <= stock_minimo  AND estado = 'ACTIVO' ORDER BY  stock ASC";
             
             rs = stmt.executeQuery(sql);
             
             while(rs.next())  {
                 Producto p = new Producto();
                 p.setIdProducto(rs.getInt("id_producto"));
                 p.setCodigo(rs.getString("codigo"));
                 p.setNombre(rs.getString("nombre"));
                 p.setDescripcion(rs.getString("descripcion"));
                 p.setPrecio(rs.getBigDecimal("precio"));
                 p.setStock(rs.getInt("stock"));
                 p.setStockMinimo(rs.getInt("stock_minimo"));
                 p.setIvaPorcentaje(rs.getBigDecimal("iva_porcentaje"));
                 p.setEstado(rs.getString("estado"));
                 
                 productos.add(p);
                 
             }
        } catch (SQLException e) {
            System.err.println("Error al listar stock bajo : " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        return productos;
    }
            
    // -------------------- MÉTODO PARA ACTUALIZAR STOCK (usado al facturar) ---------------------------- //
    public boolean actualizarStock(int idProducto, int cantidadVendida) {
        Connection conn = null;
        PreparedStatement pst = null;
        
        try {
            conn = Conexion.getConnection();
            // Resta la cantidad vendida del stock disponible
            String sql = "UPDATE productos SET stock = stock - ? WHERE id_producto";
            
            pst = conn.prepareStatement(sql);
            pst.setInt(1,cantidadVendida);
            pst.setInt(2, idProducto);
            
            return pst.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error alactualizar stock : " + e.getMessage());
            return false;
        }finally  {
            cerrarRecursos(conn, pst, null);
        }
    }
    
    // ------------------ MÉTODO PARA CERRAR RECURSOS (evita fugas de memoria) ------------------------ //
    private void cerrarRecursos(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if(rs != null ) rs.close();
            if(stmt != null ) stmt.close();
            // No cerramos conn aquí para reutilizarla
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
     
}
