package dao;

import model.Usuario;
import util.ConexionBaseDatos;

import java.sql.*;

public class UsuarioMetodos {

    // este metodo valida el login del usuario

    public Usuario validarLogin(String usuario, String contrasena) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ?";

        try (Connection conn = ConexionBaseDatos.obtenerConexion()) {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, usuario);
            stm.setString(2, contrasena);

            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Usuario usuarioEncontrado = new Usuario();
                usuarioEncontrado.setId(rs.getInt("id"));
                usuarioEncontrado.setUsuario(rs.getString("usuario"));
                usuarioEncontrado.setContrasena(rs.getString("contrasena"));
                usuarioEncontrado.setEmail(rs.getString("email"));

                Timestamp ts = rs.getTimestamp("fecha_registro");
                if (ts != null) {
                    usuarioEncontrado.setFechaRegistro(ts.toLocalDateTime());
                }

                return usuarioEncontrado;
            }
        }
        return null; // Solo retorna null si no encuentra el usuario
    }

    public boolean crearUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (usuario, contrasena, email) VALUES (?, ?, ?)";

        try (Connection conn = ConexionBaseDatos.obtenerConexion()) {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, usuario.getUsuario());
            stm.setString(2, usuario.getContrasena());
            stm.setString(3, usuario.getEmail());

            int filasCambiadas = stm.executeUpdate(); // Esto ejecuta la consulta que hemos preparado y almacena el numero de filas cambiadas en la variable
            return filasCambiadas > 0; // Si el numero de filas cambiadas es mayor que 0 significa que se ha insertado el usuario
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean usuarioYaExiste(String usuario) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE usuario = ?";
        try (Connection conn = ConexionBaseDatos.obtenerConexion()) {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, usuario);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) { // Si el resultado tiene por lo menos una fila significa que existe el usuario
                int count = rs.getInt(1); // Esto obtiene el valor de la primera columna de la primera fila
                return count > 0; // Si el numero de usuarios es mayor que 0 significa que existe el usuario
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Usuario cogeUsuarioPorID(int id) {
        String sql = "SELECT * FROM usuarios WHERE id =?";
        try (Connection conn = ConexionBaseDatos.obtenerConexion()) {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Usuario usuarioEncontrado = new Usuario();
                usuarioEncontrado.setId(rs.getInt("id"));
                usuarioEncontrado.setUsuario(rs.getString("usuario"));
                usuarioEncontrado.setContrasena(rs.getString("contrasena"));
                usuarioEncontrado.setEmail(rs.getString("email"));

                Timestamp ts = rs.getTimestamp("fecha_registro");
                if (ts != null) {
                    usuarioEncontrado.setFechaRegistro(ts.toLocalDateTime());
                }
                return usuarioEncontrado;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}

