<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/registro.css">
    <meta charset="UTF-8">
    <title>Registro - Gestor de Tareas</title>
</head>
<body>
    <div>
        <h1>Crear Cuenta</h1>


        <% if(request.getAttribute("error") != null) { %>
            <div style="color: red; margin-bottom: 15px;">
                <p><%= request.getAttribute("error") %></p>
            </div>
        <% } %>


        <% if(request.getAttribute("mensaje") != null) { %>
            <div style="color: green; margin-bottom: 15px;">
                <p><%= request.getAttribute("mensaje") %></p>
            </div>
        <% } %>

        <form action="RegistroServlet" method="post">
            <div style="margin-bottom: 15px;">
                <label for="username">Nombre de Usuario:</label><br>
                <input type="text" id="username" name="username" required maxlength="50"
                       value="<%= request.getParameter("username") != null ? request.getParameter("username") : "" %>"
                       placeholder="Ingresa tu nombre de usuario">
            </div>

            <div style="margin-bottom: 15px;">
                <label for="email">Correo Electrónico:</label><br>
                <input type="email" id="email" name="email" required maxlength="100"
                       value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>"
                       placeholder="ejemplo@correo.com">
            </div>

            <div style="margin-bottom: 15px;">
                <label for="password">Contraseña:</label><br>
                <input type="password" id="password" name="password" required minlength="6"
                       placeholder="Mínimo 6 caracteres">
            </div>

            <div style="margin-bottom: 15px;">
                <label for="confirmPassword">Confirmar Contraseña:</label><br>
                <input type="password" id="confirmPassword" name="confirmPassword" required minlength="6"
                       placeholder="Repite tu contraseña">
            </div>

            <div style="margin-bottom: 15px;">
                <input type="submit" value="Crear Cuenta" style="padding: 10px 20px;">
            </div>
        </form>

        <div>
            <p>¿Ya tienes una cuenta? <a href="login.jsp">Inicia Sesión</a></p>
        </div>
    </div>
</body>
</html>