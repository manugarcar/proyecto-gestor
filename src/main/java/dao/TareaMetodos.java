package dao;

import model.Prioridad;
import model.Tarea;
import util.ConexionBaseDatos;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TareaMetodos {

    public List<Tarea> cogeTareasPorUsuario(int UsuarioID) {
        List<Tarea> tareas = new ArrayList<>();
        String sql = "SELECT * FROM tareas WHERE usuario_id = ? ORDER BY fecha_creacion DESC";
        try (Connection conn = ConexionBaseDatos.obtenerConexion()) {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, UsuarioID);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Tarea tarea = new Tarea();
                tarea.setId(rs.getInt("id"));
                tarea.setUsuarioID(rs.getInt("usuario_id"));
                tarea.setTitulo(rs.getString("titulo"));
                tarea.setDescripcion(rs.getString("descripcion"));
                tarea.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
                tarea.setFechaVencimiento(rs.getDate("fecha_vencimiento").toLocalDate());
                tarea.setPrioridad(Prioridad.valueOf(rs.getString("prioridad")));
                tarea.setCompletada(rs.getBoolean("completada"));
                tareas.add(tarea);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tareas;
    }

    public boolean crearTarea(Tarea tarea) {
        String sql = "INSERT INTO tareas (usuario_id, titulo, descripcion, fecha_vencimiento, prioridad, completada) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBaseDatos.obtenerConexion()) {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, tarea.getUsuarioID());
            stm.setString(2, tarea.getTitulo());
            stm.setString(3, tarea.getDescripcion());
            stm.setDate(4, Date.valueOf(tarea.getFechaVencimiento()));
            stm.setString(5, tarea.getPrioridad().name());
            stm.setBoolean(6, tarea.isCompletada());

            int filasCambiadas = stm.executeUpdate();
            return filasCambiadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Tarea cogeTareaPorID(int id) {
        String sql = "SELECT * FROM tareas WHERE id = ?";

        try (Connection conn = ConexionBaseDatos.obtenerConexion()) {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Tarea tarea = new Tarea();
                tarea.setId(rs.getInt("id"));
                tarea.setUsuarioID(rs.getInt("usuario_id"));
                tarea.setTitulo(rs.getString("titulo"));
                tarea.setDescripcion(rs.getString("descripcion"));
                tarea.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
                tarea.setFechaVencimiento(rs.getDate("fecha_vencimiento").toLocalDate());
                tarea.setPrioridad(Prioridad.valueOf(rs.getString("prioridad")));
                tarea.setCompletada(rs.getBoolean("completada"));
                return tarea;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean actualizarTarea(Tarea tarea) {
        String sql = "UPDATE tareas SET titulo =?, descripcion =?, fecha_vencimiento =?, prioridad =?, completada =? WHERE id =?";
        try (Connection conn = ConexionBaseDatos.obtenerConexion()) {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, tarea.getTitulo());
            stm.setString(2, tarea.getDescripcion());
            stm.setDate(3, Date.valueOf(tarea.getFechaVencimiento()));
            stm.setString(4, tarea.getPrioridad().name());
            stm.setBoolean(5, tarea.isCompletada());
            stm.setInt(6, tarea.getId());

            int filasCambiadas = stm.executeUpdate();
            return filasCambiadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean marcarComoCompletada(int tareaId) {
        String sql = "UPDATE tareas SET completada = true WHERE id = ?";

        try (Connection conn = ConexionBaseDatos.obtenerConexion()) {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, tareaId);

            int filasCambiadas = stm.executeUpdate();
            return filasCambiadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean borrarTarea(int tareaId) {
        String sql = "DELETE FROM tareas WHERE id = ?";

        try (Connection conn = ConexionBaseDatos.obtenerConexion()) {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, tareaId);

            int filasCambiadas = stm.executeUpdate();
            return filasCambiadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
