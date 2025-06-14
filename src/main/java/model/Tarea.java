package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Tarea {
    private int id;
    private int UsuarioID;
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private LocalDate fechaVencimiento;
    private Prioridad prioridad;
    private boolean completada;

    public Tarea() {
    }

    public Tarea(int usuarioID, String titulo, String descripcion, LocalDate fechaVencimiento, Prioridad prioridad,
            boolean completada) {
        UsuarioID = usuarioID;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaVencimiento = fechaVencimiento;
        this.prioridad = prioridad;
        this.completada = completada;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getUsuarioID() {
        return UsuarioID;
    }

    public void setUsuarioID(int usuarioID) {
        UsuarioID = usuarioID;
    }

    @Override
    public String toString() {
        return "model.Tarea{" + "id=" + id + ", UsuarioID=" + UsuarioID + ", titulo='" + titulo + '\'' + ", descripcion='"
                + descripcion + '\'' + ", fechaCreacion=" + fechaCreacion + ", fechaVencimiento=" + fechaVencimiento
                + ", prioridad=" + prioridad + ", completada=" + completada + '}';
    }
}
