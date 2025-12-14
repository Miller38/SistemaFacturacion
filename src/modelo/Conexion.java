package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Miller
 */
public class Conexion {

    //  CONFIGURACIÓN DE CONEXIÓN
    private static final String URL = "jdbc:mysql://localhost:3306/sistema_facturacion";
    private static final String USER = "root";                          // Tu usuario de MySQL
    private static final String PASSWORD = "";                      // Tu contraseña de MySQL

    private static Connection conexion = null;                       // Variable para guardar la conexión

    //  MÉTODO PARA OBTENER LA CONEXIÓN
    public static Connection getConnection() {
        try {
            // Si no hay conexión o está cerrada, crear una nueva
            if (conexion == null || conexion.isClosed()) {
                // 1. Cargar el driver de MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");
                 // 2. Establecer la conexión con la base de datos
                 conexion = DriverManager.getConnection(URL,USER,PASSWORD);
                 System.out.println("Conexion Exitosa a la base de datos");                 
            }
        } catch (ClassNotFoundException e) {
            // Error: No encontró el driver de MySQL
            System.err.println("Error : Driver de MYSQL no encontrado");
            System.err.println("📥 Descarga: https://dev.mysql.com/downloads/connector/j/");
            e.printStackTrace();            
        } catch(SQLException e) {
            // Error: No pudo conectar a la base de datos
            System.err.println("❌ Error al conectar con MySQL:");
            System.err.println("   • Verifica que MySQL esté encendido");
            System.err.println("   • Verifica usuario y contraseña");
            System.err.println("   • Verifica que la BD 'sistema_facturacion' exista");
            e.printStackTrace();
        }
         return conexion;
    }
     //  MÉTODO PARA CERRAR LA CONEXIÓN
    public static void cerrarConexion(){
        try {
            if(conexion != null && !conexion.isClosed()){
                conexion.close();
                System.out.println("Conexion cerrada correctamente");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexion");
            e.printStackTrace();
        }
    }
}
