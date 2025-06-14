package servlet;

import dao.UsuarioMetodos;
import jakarta.servlet.annotation.WebServlet;
import model.Usuario;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet({"/LoginServlet", "/login"})
public class LoginServlet extends HttpServlet {
    private UsuarioMetodos usuarioMetodos;

    @Override
    public void init() throws ServletException {
        usuarioMetodos = new UsuarioMetodos();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Esto te redirige a la página de login si el usuario no está logueado
        response.sendRedirect("login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Esto mira si los campos estan vacios y si estan te refresca la pagina de login y te pide que los completes
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Por favor, completa todos los campos");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        try {

            Usuario usuario = usuarioMetodos.validarLogin(username.trim(), password);

            if (usuario != null) {
                // Si el login se hace, guarda el usuario en la sesion
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);

                // Redirige al dashboard
                response.sendRedirect("TareaServlet?accion=listar");
            } else {

                request.setAttribute("error", "Usuario o contraseña incorrectos");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error interno del servidor. Inténtalo más tarde.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}