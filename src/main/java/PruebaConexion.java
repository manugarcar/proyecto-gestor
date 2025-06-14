import util.ConexionBaseDatos;

import java.sql.Connection;
import java.sql.SQLException;
import util.ConexionBaseDatos;

public class PruebaConexion {
    public static void main(String[] args) throws SQLException {

        try (Connection conn = ConexionBaseDatos.obtenerConexion()) {
            if (conn != null) {
                System.out.println("Conexión exitosa");
                ConexionBaseDatos.cierraConexion(conn);
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
