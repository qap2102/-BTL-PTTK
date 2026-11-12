package Controller;

import DAO.*;
import Model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.*;

@WebServlet(name="DocumentBorrowingReceiptServlet4",urlPatterns={"/DocumentBorrowingReceiptServlet4"})
public class DocumentBorrowingReceiptServlet4 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        try {
            Date[] period = StatisticsSupport4.period(request);
            String code = StatisticsSupport4.code(request);
            try (DocumentDAO4 dao = new DocumentDAO4()) {
                Document4 document = dao.getDocument(code);
                if (document==null) { response.sendError(404,"Không tìm thấy tài liệu."); return; }
                List<BorrowingReceiptDetail4> details = new BorrowingReceiptDetailDAO4(dao.getConnection()).getListBorrowing(document.getId(),period[0],period[1]);
                // Full receipt metadata and the individual copies are fetched in bounded queries.
                Map<Integer,BorrowingReceipt4> receipts = new HashMap<>();
                for (BorrowingReceipt4 receipt : new BorrowingReceiptDAO4(dao.getConnection()).getListBorrowingReceipts(code,period[0],period[1])) receipts.put(receipt.getId(),receipt);
                for (BorrowingReceiptDetail4 detail : details) {
                    BorrowingReceipt4 receipt = receipts.get(detail.getBorrowingReceipt().getId());
                    if (receipt!=null) detail.setBorrowingReceipt(receipt);
                }
                request.setAttribute("document",document);
                request.setAttribute("details",details);
            }
        } catch (IllegalArgumentException ex) {
            response.sendError(400,ex.getMessage()); return;
        } catch (IllegalStateException ex) {
            getServletContext().log("Unable to load document borrowing records.",ex);
            request.setAttribute("error","Không thể tải danh sách các lần mượn. Vui lòng thử lại sau."); response.setStatus(503);
        }
        request.setAttribute("loaded",true);
        request.getRequestDispatcher("/Manager4/BorrowingReceiptUI4.jsp").forward(request,response);
    }
}
