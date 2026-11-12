package Controller;

import Model.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter("/*")
public class AuthenticationFilter4 implements Filter {
    @Override
    public void doFilter(ServletRequest input, ServletResponse output, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) input;
        HttpServletResponse response = (HttpServletResponse) output;
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String path = request.getRequestURI().substring(request.getContextPath().length());
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("X-Content-Type-Options", "nosniff");
        if (path.startsWith("/assets/") || path.equals("/login") || path.equals("/LoginServlet4")
                || path.equals("/Member4/LoginUI4.jsp") || path.equals("/") || path.equals("/index.html")) {
            chain.doFilter(request, response);
            return;
        }
        HttpSession session = request.getSession(false);
        Member4 member = session == null ? null : (Member4) session.getAttribute("member");
        if (member == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        boolean readerRoute = path.startsWith("/Reader4/") || path.equals("/RegistrationServlet4");
        boolean managerRoute = path.startsWith("/Manager4/") || path.equals("/DocumentBorrowingStatServlet4")
                || path.equals("/DocumentBorrowingReceiptServlet4") || path.equals("/BorrowingReceiptDetailServlet4");
        if ((readerRoute && !(member instanceof Reader4)) || (managerRoute && !(member instanceof Manager4))
                || (path.startsWith("/Librarian4/") && !(member instanceof Librarian4))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền sử dụng chức năng này.");
            return;
        }
        chain.doFilter(request, response);
    }
}
