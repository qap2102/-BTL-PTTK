package Controller;

import DAO.*;
import Model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@WebServlet(name="BorrowingReceiptDetailServlet4",urlPatterns={"/BorrowingReceiptDetailServlet4"})
public class BorrowingReceiptDetailServlet4 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        try {
            int id;
            try { id = Integer.parseInt(request.getParameter("id")); if (id<1) throw new NumberFormatException(); }
            catch (NumberFormatException ex) { throw new IllegalArgumentException("Mã phiếu mượn không hợp lệ."); }
            String backLink = request.getContextPath()+"/DocumentBorrowingStatServlet4";
            if (request.getParameter("documentCode")!=null || request.getParameter("startDate")!=null || request.getParameter("endDate")!=null) {
                StatisticsSupport4.period(request);
                String code = StatisticsSupport4.code(request);
                backLink = WebSupport4.url(request,"/DocumentBorrowingReceiptServlet4","documentCode",code,"startDate",request.getParameter("startDate"),"endDate",request.getParameter("endDate"));
            }
            request.setAttribute("backLink",backLink);
            try (BorrowingReceiptDAO4 dao = new BorrowingReceiptDAO4()) {
                BorrowingReceipt4 receipt = dao.getBorrowingReceipt(id);
                if (receipt==null) { response.sendError(404,"Không tìm thấy phiếu mượn."); return; }
                List<BorrowingReceiptDetail4> details = new BorrowingReceiptDetailDAO4(dao.getConnection()).getDetailsByReceiptId(id);
                receipt.setBorrowingList(details.toArray(new BorrowingReceiptDetail4[0]));
                request.setAttribute("receipt",receipt);
                Map<Integer,Boolean> returnedStatus = new HashMap<>();
                ReturnReceiptDetailDAO4 returnDAO = new ReturnReceiptDetailDAO4(dao.getConnection());
                for (BorrowingReceiptDetail4 detail : details) {
                    returnedStatus.put(detail.getId(),returnDAO.isReturned(detail.getId()));
                }
                request.setAttribute("returnedStatus",returnedStatus);
            }
        } catch (IllegalArgumentException ex) {
            response.sendError(400,ex.getMessage()); return;
        } catch (IllegalStateException ex) {
            getServletContext().log("Unable to load borrowing receipt details.",ex);
            request.setAttribute("error","Không thể tải chi tiết phiếu mượn. Vui lòng thử lại sau."); response.setStatus(503);
        }
        request.setAttribute("loaded",true);
        request.getRequestDispatcher("/Manager4/BorrowingReceiptDetailUI4.jsp").forward(request,response);
    }
}
