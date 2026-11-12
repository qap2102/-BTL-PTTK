package Controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet4 extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!WebSupport4.validCsrf(request)) { response.sendError(403); return; }
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
