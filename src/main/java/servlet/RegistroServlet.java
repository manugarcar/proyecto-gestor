package servlet;

import dao.UsuarioMetodos;
import jakarta.servlet.annotation.WebServlet;
import model.Usuario;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

@WebServlet({"/RegistroServlet", "/registro"})
public class RegistroServlet extends HttpServlet {
    private UsuarioMetodos usuarioMetodos;

    // Esta variable se usa para validar el foprmato de un email

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");


    @Override
    public void init() throws ServletException {
        usuarioMetodos = new UsuarioMetodos();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Te manda a la pagina de registro
        response.sendRedirect("registro.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String email = request.getParameter("email");

        // Mira si los campos estan completos y si no lo sestan te refresca la pagina con un mensaje de error
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                confirmPassword == null || confirmPassword.trim().isEmpty() ||
                email == null || email.trim().isEmpty()) {

            request.setAttribute("error", "Por favor, completa todos los campos");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        // Mira si las contraseñas coinciden
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Las contraseñas no coinciden");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        // Esto mira si la contraseña tiene como minimo 6 caracteres
        if (password.length() < 6) {
            request.setAttribute("error", "La contraseña debe tener al menos 6 caracteres");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }


        if (!EMAIL_PATTERN.matcher(email).matches()) {
            request.setAttribute("error", "Por favor, introduce un email válido");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        try {

            if (usuarioMetodos.usuarioYaExiste(username.trim())) {
                request.setAttribute("error", "El nombre de usuario ya está en uso");
                request.getRequestDispatcher("registro.jsp").forward(request, response);
                return;
            }


            Usuario nuevoUsuario = new Usuario(username.trim(), password, email.trim());

            if (usuarioMetodos.crearUsuario(nuevoUsuario)) {

                request.setAttribute("mensaje", "Usuario registrado correctamente. Ahora puedes iniciar sesión.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Error al registrar el usuario. Inténtalo de nuevo.");
                request.getRequestDispatcher("registro.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error interno del servidor. Inténtalo más tarde.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }
}