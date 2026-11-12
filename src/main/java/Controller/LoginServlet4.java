package Controller;

import DAO.MemberDAO4;
import Model.Member4;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "LoginServlet4", urlPatterns = {"/login", "/LoginServlet4"})
public class LoginServlet4 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Member4 member = (Member4) session.getAttribute("member");
        if (member != null) {
            response.sendRedirect(request.getContextPath() + WebSupport4.home(member));
            return;
        }
        WebSupport4.csrf(session);
        request.getRequestDispatcher("/Member4/LoginUI4.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        request.setAttribute("userName", userName);
        if (!WebSupport4.validCsrf(request)) {
            request.setAttribute("error", "Phiên đăng nhập đã hết hạn. Vui lòng thử lại.");
        } else if (userName == null || userName.trim().isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập tên đăng nhập và mật khẩu.");
        } else {
            try (MemberDAO4 dao = new MemberDAO4()) {
                Member4 member = dao.login(userName.trim(), password);
                if (member != null) {
                    request.getSession().invalidate();
                    HttpSession session = request.getSession(true);
                    session.setAttribute("member", member);
                    WebSupport4.csrf(session);
                    response.sendRedirect(request.getContextPath() + WebSupport4.home(member));
                    return;
                }
                request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
            } catch (IllegalStateException ex) {
                getServletContext().log("Login failed due to database or account configuration.", ex);
                request.setAttribute("error", "Không thể đăng nhập lúc này. Vui lòng kiểm tra kết nối cơ sở dữ liệu và thử lại.");
                response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            }
        }
        WebSupport4.csrf(request.getSession());
        request.getRequestDispatcher("/Member4/LoginUI4.jsp").forward(request, response);
    }
}
