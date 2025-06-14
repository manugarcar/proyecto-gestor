package servlet;

import dao.TareaMetodos;
import jakarta.servlet.annotation.WebServlet;
import model.Prioridad;
import model.Tarea;
import model.Usuario;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet({"/TareaServlet", "/tareas"})
public class TareaServlet extends HttpServlet {
    private TareaMetodos tareaMetodos;

    @Override
    public void init() throws ServletException {
        tareaMetodos = new TareaMetodos();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // mira si el usuario esta logueado y si no te manda a la pagina de login
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String accion = request.getParameter("accion");

        if (accion == null) {
            accion = "listar";
        }

        switch (accion) {
            case "listar":
                listarTareas(request, response, usuario);
                break;
            case "completar":
                completarTarea(request, response, usuario);
                break;
            case "eliminar":
                eliminarTarea(request, response, usuario);
                break;
            default:
                listarTareas(request, response, usuario);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String accion = request.getParameter("accion");

        switch (accion) {
            case "crear":
                crearTarea(request, response, usuario);
                break;
            case "actualizar":
                actualizarTarea(request, response, usuario);
                break;
            default:
                listarTareas(request, response, usuario);
                break;
        }
    }

    private void listarTareas(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {
        try {
            List<Tarea> tareas = tareaMetodos.cogeTareasPorUsuario(usuario.getId());

            // lista las tareas segun los filtros que le demos
            String filtro = request.getParameter("filtro");
            if (filtro != null) {
                switch (filtro) {
                    case "pendientes":
                        tareas = tareas.stream()
                                .filter(t -> !t.isCompletada())
                                .collect(Collectors.toList());
                        break;
                    case "completadas":
                        tareas = tareas.stream()
                                .filter(Tarea::isCompletada)
                                .collect(Collectors.toList());
                        break;
                    case "alta":
                        tareas = tareas.stream()
                                .filter(t -> t.getPrioridad() == Prioridad.ALTA)
                                .collect(Collectors.toList());
                        break;
                }
            }

            request.setAttribute("tareas", tareas);
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar las tareas");
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
        }
    }

    private void crearTarea(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {

        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");
        String fechaVencimientoStr = request.getParameter("fechaVencimiento");
        String prioridadStr = request.getParameter("prioridad");

        // validamos los campos obligatiorios y si no te refresca la pagina con un mensaje de error
        if (titulo == null || titulo.trim().isEmpty()) {
            request.setAttribute("error", "El título es obligatorio");
            request.getRequestDispatcher("nueva-tarea.jsp").forward(request, response);
            return;
        }

        try {

            Tarea nuevaTarea = new Tarea();
            nuevaTarea.setUsuarioID(usuario.getId());
            nuevaTarea.setTitulo(titulo.trim());
            nuevaTarea.setDescripcion(descripcion != null ? descripcion.trim() : null);
            nuevaTarea.setCompletada(false);

            // miramos la fecha de vencimiento y si no es correcta refresca la pagina y te da error
            if (fechaVencimientoStr != null && !fechaVencimientoStr.trim().isEmpty()) {
                try {
                    LocalDate fechaVencimiento = LocalDate.parse(fechaVencimientoStr);
                    nuevaTarea.setFechaVencimiento(fechaVencimiento);
                } catch (DateTimeParseException e) {
                    request.setAttribute("error", "Formato de fecha inválido");
                    request.getRequestDispatcher("nueva-tarea.jsp").forward(request, response);
                    return;
                }
            }

            // esto sirve para la prioridad de las tareas
            try {
                Prioridad prioridad = Prioridad.valueOf(prioridadStr.toUpperCase());
                nuevaTarea.setPrioridad(prioridad);
            } catch (IllegalArgumentException e) {
                nuevaTarea.setPrioridad(Prioridad.MEDIA); // de normal las tareas van a tener prioridad media
            }

            // guardamos en la base de datos
            if (tareaMetodos.crearTarea(nuevaTarea)) {
                request.setAttribute("mensaje", "Tarea creada exitosamente");
                response.sendRedirect("TareaServlet?accion=listar");
            } else {
                request.setAttribute("error", "Error al crear la tarea");
                request.getRequestDispatcher("nueva-tarea.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error interno del servidor");
            request.getRequestDispatcher("nueva-tarea.jsp").forward(request, response);
        }
    }

    private void actualizarTarea(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");
        String fechaVencimientoStr = request.getParameter("fechaVencimiento");
        String prioridadStr = request.getParameter("prioridad");

        try {
            int tareaId = Integer.parseInt(idStr);


            Tarea tarea = tareaMetodos.cogeTareaPorID(tareaId);

            if (tarea == null || tarea.getUsuarioID() != usuario.getId()) {
                request.setAttribute("error", "Tarea no encontrada o no autorizada");
                response.sendRedirect("TareaServlet?accion=listar");
                return;
            }


            if (titulo == null || titulo.trim().isEmpty()) {
                request.setAttribute("error", "El título es obligatorio");
                request.getRequestDispatcher("editar-tarea.jsp?id=" + tareaId).forward(request, response);
                return;
            }


            tarea.setTitulo(titulo.trim());
            tarea.setDescripcion(descripcion != null ? descripcion.trim() : null);


            if (fechaVencimientoStr != null && !fechaVencimientoStr.trim().isEmpty()) {
                try {
                    LocalDate fechaVencimiento = LocalDate.parse(fechaVencimientoStr);
                    tarea.setFechaVencimiento(fechaVencimiento);
                } catch (DateTimeParseException e) {
                    request.setAttribute("error", "Formato de fecha inválido");
                    request.getRequestDispatcher("editar-tarea.jsp?id=" + tareaId).forward(request, response);
                    return;
                }
            } else {
                tarea.setFechaVencimiento(null);
            }


            try {
                Prioridad prioridad = Prioridad.valueOf(prioridadStr.toUpperCase());
                tarea.setPrioridad(prioridad);
            } catch (IllegalArgumentException e) {
                tarea.setPrioridad(Prioridad.MEDIA);
            }


            if (tareaMetodos.actualizarTarea(tarea)) {
                request.setAttribute("mensaje", "Tarea actualizada exitosamente");
                response.sendRedirect("TareaServlet?accion=listar");
            } else {
                request.setAttribute("error", "Error al actualizar la tarea");
                request.getRequestDispatcher("editar-tarea.jsp?id=" + tareaId).forward(request, response);
            }

        } catch (NumberFormatException e) {
            response.sendRedirect("TareaServlet?accion=listar");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error interno del servidor");
            response.sendRedirect("TareaServlet?accion=listar");
        }
    }

    private void completarTarea(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");

        try {
            int tareaId = Integer.parseInt(idStr);


            Tarea tarea = tareaMetodos.cogeTareaPorID(tareaId);

            if (tarea == null || tarea.getUsuarioID() != usuario.getId()) {
                request.setAttribute("error", "Tarea no encontrada o no autorizada");
                response.sendRedirect("TareaServlet?accion=listar");
                return;
            }

            if (tareaMetodos.marcarComoCompletada(tareaId)) {
                request.setAttribute("mensaje", "Tarea marcada como completada");
            } else {
                request.setAttribute("error", "Error al completar la tarea");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID de tarea inválido");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error interno del servidor");
        }

        response.sendRedirect("TareaServlet?accion=listar");
    }

    private void eliminarTarea(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");

        try {
            int tareaId = Integer.parseInt(idStr);


            Tarea tarea = tareaMetodos.cogeTareaPorID(tareaId);

            if (tarea == null || tarea.getUsuarioID() != usuario.getId()) {
                request.setAttribute("error", "Tarea no encontrada o no autorizada");
                response.sendRedirect("TareaServlet?accion=listar");
                return;
            }

            if (tareaMetodos.borrarTarea(tareaId)) {
                request.setAttribute("mensaje", "Tarea eliminada correctamente");
            } else {
                request.setAttribute("error", "Error al eliminar la tarea");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID de tarea inválido");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error interno del servidor");
        }

        response.sendRedirect("TareaServlet?accion=listar");
    }
}