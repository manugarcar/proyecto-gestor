<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/login.css">
    <meta charset="UTF-8">
    <title>Login - Gestor de Tareas</title>
</head>
<body>
    <div>
        <h1>Iniciar Sesión</h1>


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


        <form action="LoginServlet" method="post">
            <div>
                <label for="username">Nombre de Usuario:</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div>
                <label for="password">Contraseña:</label>
                <input type="password" id="password" name="password" required>
            </div>
            <div>
                <input type="submit" value="Iniciar Sesión">
            </div>
        </form>
    </div>
    <div>
        <p>¿No tienes una cuenta? <a href="registro.jsp">Regístrate</a></p>
    </div>
</body>
</html>