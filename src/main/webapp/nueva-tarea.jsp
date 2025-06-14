<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Usuario" %>

<%
    // Verificar si el usuario está logueado
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/dashboard_tareas.css">
    <meta charset="UTF-8">
    <title>Nueva Tarea - Gestor de Tareas</title>
</head>
<body>
    <div>
        <header>
            <h1>Nueva Tarea</h1>
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
                <input type="hidden" name="accion" value="crear">

                <div>
                    <label for="titulo">Título de la tarea:</label><br>
                    <input type="text" id="titulo" name="titulo" required maxlength="200"
                           value="<%= request.getParameter("titulo") != null ? request.getParameter("titulo") : "" %>"
                           placeholder="Ej: Completar informe mensual">
                </div>
                <br>

                <div>
                    <label for="descripcion">Descripción:</label><br>
                    <textarea id="descripcion" name="descripcion" rows="4" cols="50"
                              placeholder="Describe los detalles de la tarea..."><%= request.getParameter("descripcion") != null ? request.getParameter("descripcion") : "" %></textarea>
                </div>
                <br>

                <div>
                    <label for="fechaVencimiento">Fecha límite:</label><br>
                    <input type="date" id="fechaVencimiento" name="fechaVencimiento"
                           value="<%= request.getParameter("fechaVencimiento") != null ? request.getParameter("fechaVencimiento") : "" %>">
                    <br>
                    <small>Opcional - Deja vacío si no tiene fecha límite</small>
                </div>
                <br>

                <div>
                    <label for="prioridad">Prioridad:</label><br>
                    <select id="prioridad" name="prioridad" required>
                        <option value="BAJA" <%= "BAJA".equals(request.getParameter("prioridad")) ? "selected" : "" %>>Baja</option>
                        <option value="MEDIA" <%= "MEDIA".equals(request.getParameter("prioridad")) || request.getParameter("prioridad") == null ? "selected" : "" %>>Media</option>
                        <option value="ALTA" <%= "ALTA".equals(request.getParameter("prioridad")) ? "selected" : "" %>>Alta</option>
                    </select>
                </div>
                <br>

                <div>
                    <button type="submit">Crear Tarea</button>
                    <button type="button" onclick="window.location.href='TareaServlet?accion=listar'">Cancelar</button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>