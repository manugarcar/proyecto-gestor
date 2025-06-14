<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Usuario" %>
<%@ page import="model.Tarea" %>
<%@ page import="dao.TareaMetodos" %>

<%
    // Verificar si el usuario está logueado
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Obtener la tarea a editar
    String idParam = request.getParameter("id");
    Tarea tarea = null;

    if (idParam != null) {
        try {
            int tareaId = Integer.parseInt(idParam);
            TareaMetodos tareaMetodos = new TareaMetodos();
            tarea = tareaMetodos.cogeTareaPorID(tareaId);

            // Verificar que la tarea pertenece al usuario logueado
            if (tarea == null || tarea.getUsuarioID() != usuario.getId()) {
                response.sendRedirect("TareaServlet?accion=listar");
                return;
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("TareaServlet?accion=listar");
            return;
        }
    } else {
        response.sendRedirect("TareaServlet?accion=listar");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/dashboard_tareas.css">
    <meta charset="UTF-8">
    <title>Editar Tarea - Gestor de Tareas</title>
</head>
<body>
    <div>
        <header>
            <h1>Editar Tarea</h1>
            <div>
                <span>Usuario: <%= usuario.getUsuario() %></span>
                <a href="LogoutServlet">Cerrar Sesión</a>
            </div>
        </header>

        <nav>
            <a href="TareaServlet?accion=listar">← Volver a Mis Tareas</a>
        </nav>

        <% if(request.getAttribute("error") != null) { %>
            <div>
                <p style="color: red;"><%= request.getAttribute("error") %></p>
            </div>
        <% } %>

        <div>
            <form action="TareaServlet" method="post">
                <input type="hidden" name="accion" value="actualizar">
                <input type="hidden" name="id" value="<%= tarea.getId() %>">

                <div>
                    <label for="titulo">Título de la tarea:</label><br>
                    <input type="text" id="titulo" name="titulo" required maxlength="200"
                           value="<%= tarea.getTitulo() %>"
                           placeholder="Ej: Completar informe mensual">
                </div>
                <br>

                <div>
                    <label for="descripcion">Descripción:</label><br>
                    <textarea id="descripcion" name="descripcion" rows="4" cols="50"
                              placeholder="Describe los detalles de la tarea..."><%= tarea.getDescripcion() != null ? tarea.getDescripcion() : "" %></textarea>
                </div>
                <br>

                <div>
                    <label for="fechaVencimiento">Fecha límite:</label><br>
                    <input type="date" id="fechaVencimiento" name="fechaVencimiento"
                           value="<%= tarea.getFechaVencimiento() != null ? tarea.getFechaVencimiento().toString() : "" %>">
                    <br>
                    <small>Opcional - Dejalo vacío si no tiene fecha límite</small>
                </div>
                <br>

                <div>
                    <label for="prioridad">Prioridad:</label><br>
                    <select id="prioridad" name="prioridad" required>
                        <option value="BAJA" <%= tarea.getPrioridad().toString().equals("BAJA") ? "selected" : "" %>>Baja</option>
                        <option value="MEDIA" <%= tarea.getPrioridad().toString().equals("MEDIA") ? "selected" : "" %>>Media</option>
                        <option value="ALTA" <%= tarea.getPrioridad().toString().equals("ALTA") ? "selected" : "" %>>Alta</option>
                    </select>
                </div>
                <br>

                <div>
                    <button type="submit">Actualizar Tarea</button>
                    <button type="button" onclick="window.location.href='TareaServlet?accion=listar'">Cancelar</button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>