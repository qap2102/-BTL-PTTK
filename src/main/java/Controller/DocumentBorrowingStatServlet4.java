package Controller;

import DAO.DocumentBorrowingStatDAO4;
import Model.DocumentBorrowingStat4;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet(name="DocumentBorrowingStatServlet4",urlPatterns={"/DocumentBorrowingStatServlet4"})
public class DocumentBorrowingStatServlet4 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        request.setAttribute("loaded",true);
        if (request.getParameter("startDate")!=null || request.getParameter("endDate")!=null) {
            try {
                Date[] period = StatisticsSupport4.period(request);
                try (DocumentBorrowingStatDAO4 dao = new DocumentBorrowingStatDAO4()) {
                    List<DocumentBorrowingStat4> statistics = dao.getDocumentStatistics(period[0],period[1]);
                    request.setAttribute("statistics",statistics);
                    request.setAttribute("totalDocuments",statistics.size());
                    request.setAttribute("totalBorrowCount",statistics.stream().mapToLong(DocumentBorrowingStat4::getTotalBorrowCount).sum());
                    request.setAttribute("searched",true);
                }
            } catch (IllegalArgumentException ex) {
                request.setAttribute("error",ex.getMessage()); response.setStatus(400);
            } catch (IllegalStateException ex) {
                getServletContext().log("Unable to load document statistics.",ex);
                request.setAttribute("error","Không thể tải thống kê lúc này. Vui lòng thử lại sau."); response.setStatus(503);
            }
        }
        request.getRequestDispatcher("/Manager4/DocumentBorrowingStatUI4.jsp").forward(request,response);
    }
}
