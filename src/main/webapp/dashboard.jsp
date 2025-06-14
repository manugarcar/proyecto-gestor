<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Usuario" %>
<%@ page import="model.Tarea" %>
<%@ page import="model.Prioridad" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

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
    <title>Dashboard - Gestor de Tareas</title>
</head>
<body>
    <div>

        <header>
            <h1>Gestor de Tareas</h1>
            <div>
                <span>Bienvenido, <%= usuario.getUsuario() %>!</span>
                <a href="LogoutServlet">Cerrar Sesión</a>
            </div>
        </header>


        <nav>
            <a href="TareaServlet?accion=listar">Mis Tareas</a>
            <a href="nueva-tarea.jsp">Nueva Tarea</a>
        </nav>


        <% if(request.getAttribute("mensaje") != null) { %>
            <div>
                <p style="color: green;"><%= request.getAttribute("mensaje") %></p>
            </div>
        <% } %>

        <% if(request.getAttribute("error") != null) { %>
            <div>
                <p style="color: red;"><%= request.getAttribute("error") %></p>
            </div>
        <% } %>


        <div>
            <h2>Mis Tareas</h2>

            <%
                List<Tarea> tareas = (List<Tarea>) request.getAttribute("tareas");
                if (tareas == null || tareas.isEmpty()) {
            %>
                <div>
                    <p>No tienes tareas creadas.</p>

                </div>
            <%
                } else {
            %>
                <div>

                    <div class="filtros-container">
                        <h3>Filtrar por:</h3>
                        <div class="filtros-botones">
                            <a href="TareaServlet?accion=listar" class="filtro-btn">Todas</a>
                            <a href="TareaServlet?accion=listar&filtro=pendientes" class="filtro-btn">Pendientes</a>
                            <a href="TareaServlet?accion=listar&filtro=completadas" class="filtro-btn">Completadas</a>
                            <a href="TareaServlet?accion=listar&filtro=alta" class="filtro-btn">Prioridad Alta</a>
                        </div>
                    </div>


                    <div>
                        <% for (Tarea tarea : tareas) { %>
                            <div style="border: 1px solid #ccc; margin: 10px 0; padding: 15px;">
                                <div>
                                    <h4 style="<%= tarea.isCompletada() ? "text-decoration: line-through;" : "" %>">
                                        <%= tarea.getTitulo() %>
                                    </h4>

                                    <p><strong>Descripción:</strong> <%= tarea.getDescripcion() != null ? tarea.getDescripcion() : "Sin descripción" %></p>

                                    <p><strong>Fecha límite:</strong>
                                        <% if (tarea.getFechaVencimiento() != null) { %>
                                            <%= tarea.getFechaVencimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) %>
                                            <%
                                                LocalDate hoy = LocalDate.now();
                                                if (tarea.getFechaVencimiento().isBefore(hoy) && !tarea.isCompletada()) {
                                            %>
                                                <span style="color: red; font-weight: bold;">(VENCIDA)</span>
                                            <% } else if (tarea.getFechaVencimiento().equals(hoy) && !tarea.isCompletada()) { %>
                                                <span style="color: orange; font-weight: bold;">(HOY)</span>
                                            <% } %>
                                        <% } else { %>
                                            Sin fecha límite
                                        <% } %>
                                    </p>

                                    <p><strong>Prioridad:</strong>
                                        <span style="color: <%= tarea.getPrioridad() == Prioridad.ALTA ? "red" :
                                                                  tarea.getPrioridad() == Prioridad.MEDIA ? "orange" : "green" %>;">
                                            <%= tarea.getPrioridad() %>
                                        </span>
                                    </p>

                                    <p><strong>Estado:</strong>
                                        <span style="color: <%= tarea.isCompletada() ? "green" : "orange" %>;">
                                            <%= tarea.isCompletada() ? "Completada" : "Pendiente" %>
                                        </span>
                                    </p>
                                </div>

                                <div>
                                    <% if (!tarea.isCompletada()) { %>
                                        <a href="TareaServlet?accion=completar&id=<%= tarea.getId() %>">Marcar como Completada</a> |
                                    <% } %>
                                    <a href="editar-tarea.jsp?id=<%= tarea.getId() %>">Editar</a> |
                                    <a href="TareaServlet?accion=eliminar&id=<%= tarea.getId() %>"
                                       onclick="return confirm('¿Estás seguro de que quieres eliminar esta tarea?')">Eliminar</a>
                                </div>
                            </div>
                        <% } %>
                    </div>
                </div>
            <% } %>
        </div>


        <div style="position: fixed; bottom: 20px; right: 20px;">
            <a href="nueva-tarea.jsp" style="background: #007bff; color: white; padding: 15px; text-decoration: none; border-radius: 50px;">
                + Nueva Tarea
            </a>
        </div>
    </div>
</body>
</html>