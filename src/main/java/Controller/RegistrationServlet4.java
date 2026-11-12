package Controller;

import DAO.*;
import Model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.*;

@WebServlet(name = "RegistrationServlet4", urlPatterns = {"/RegistrationServlet4"})
public class RegistrationServlet4 extends HttpServlet {
    private static final String FORM = "/Reader4/RegistrationUI4.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Reader4 signedIn = signedInReader(request, response);
        if (signedIn == null) return;
        if ("success".equals(request.getParameter("view"))) {
            ReaderCard4 card = (ReaderCard4) request.getSession().getAttribute("registeredCard");
            if (card != null && card.getReaderInfo().getId() == signedIn.getId()) {
                request.getSession().removeAttribute("registeredCard");
                request.setAttribute("card", card);
                request.getRequestDispatcher("/Reader4/RegistrationNoticeUI4.jsp").forward(request, response);
                return;
            }
            response.sendRedirect(request.getContextPath() + WebSupport4.home(signedIn));
            return;
        }
        try (ReaderDAO4 dao = new ReaderDAO4()) {
            Reader4 reader = dao.getReaderById(signedIn.getId());
            if (reader == null) { response.sendError(404); return; }
            Map<String, String> form = new LinkedHashMap<>();
            form.put("fullName", WebSupport4.fullName(reader));
            form.put("dateOfBirth", reader.getDateOfBirth() == null ? "" : new Date(reader.getDateOfBirth().getTime()).toString());
            form.put("phoneNumber", reader.getPhoneNumber());
            form.put("email", reader.getEmail());
            form.put("readerCode", reader.getReaderCode());
            form.put("readerCategoryId", reader.getReaderCategory() == null ? "" : String.valueOf(reader.getReaderCategory().getId()));
            request.setAttribute("form", form);
            request.setAttribute("categories", new ReaderCategoryDAO4(dao.getConnection()).getAllReaderCategory());
            if (new ReaderCardDAO4(dao.getConnection()).hasReaderCard(reader)) {
                request.setAttribute("error", "Bạn đã có thẻ bạn đọc. Mỗi bạn đọc chỉ được đăng ký một thẻ.");
                request.setAttribute("alreadyRegistered", true);
            }
        } catch (IllegalStateException ex) {
            getServletContext().log("Unable to load registration form.", ex);
            request.setAttribute("error", "Không thể tải thông tin đăng ký. Vui lòng thử lại sau.");
            request.setAttribute("unavailable", true);
            response.setStatus(503);
        }
        request.getRequestDispatcher(FORM).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Reader4 signedIn = signedInReader(request, response);
        if (signedIn == null) return;
        if (!WebSupport4.validCsrf(request)) {
            response.sendError(403, "Phiên làm việc không hợp lệ. Vui lòng mở lại trang đăng ký.");
            return;
        }
        Map<String, String> form = new LinkedHashMap<>();
        for (String key : new String[]{"fullName", "dateOfBirth", "phoneNumber", "email", "readerCode", "readerCategoryId"}) {
            String value = request.getParameter(key);
            form.put(key, value == null ? "" : value.trim());
        }
        request.setAttribute("form", form);
        Map<String, String> errors = validate(form);
        request.setAttribute("errors", errors);
        try (ReaderCategoryDAO4 categories = new ReaderCategoryDAO4()) {
            request.setAttribute("categories", categories.getAllReaderCategory());
            if (errors.isEmpty()) {
                ReaderCategory4 category = categories.getReaderCategory(Integer.parseInt(form.get("readerCategoryId")));
                if (category == null) errors.put("readerCategoryId", "Vui lòng chọn loại bạn đọc hợp lệ.");
                Reader4 reader = new ReaderDAO4(categories.getConnection()).getReader(form.get("readerCode"));
                if (reader == null || reader.getId() != signedIn.getId()) {
                    errors.put("readerCode", "Mã bạn đọc phải thuộc tài khoản đang đăng nhập.");
                }
                if (errors.isEmpty()) {
                    String[] names = form.get("fullName").split("\\s+");
                    String middle = names.length > 2 ? String.join(" ", Arrays.copyOfRange(names, 1, names.length - 1)) : "";
                    reader.setFullName(new FullName4(0, names[names.length - 1], middle, names.length > 1 ? names[0] : ""));
                    reader.setDateOfBirth(Date.valueOf(form.get("dateOfBirth")));
                    reader.setEmail(form.get("email"));
                    reader.setPhoneNumber(form.get("phoneNumber"));
                    reader.setReaderCategory(category);
                    ReaderCard4 card = new ReaderCardDAO4(categories.getConnection()).createReaderCard(reader);
                    request.getSession().setAttribute("member", reader);
                    request.getSession().setAttribute("registeredCard", card);
                    response.sendRedirect(request.getContextPath() + "/RegistrationServlet4?view=success");
                    return;
                }
            }
        } catch (ReaderCardDAO4.CardAlreadyExistsException ex) {
            request.setAttribute("error", "Đã tồn tại thẻ bạn đọc với mã bạn đọc vừa nhập.");
            request.setAttribute("alreadyRegistered", true);
        } catch (IllegalStateException ex) {
            getServletContext().log("Unable to register reader card.", ex);
            request.setAttribute("error", "Chưa thể lưu đăng ký. Vui lòng thử lại sau.");
            response.setStatus(503);
        }
        request.getRequestDispatcher(FORM).forward(request, response);
    }

    private Reader4 signedInReader(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        Object member = session == null ? null : session.getAttribute("member");
        if (member == null) { response.sendRedirect(request.getContextPath() + "/login"); return null; }
        if (!(member instanceof Reader4)) { response.sendError(403); return null; }
        return (Reader4) member;
    }

    private Map<String, String> validate(Map<String, String> form) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (String key : form.keySet()) {
            if (form.get(key).isEmpty()) errors.put(key, "Vui lòng nhập đầy đủ thông tin.");
            else if (form.get(key).length() > 255) errors.put(key, "Thông tin không được dài quá 255 ký tự.");
        }
        if (!errors.containsKey("email") && !form.get("email").matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))
            errors.put("email", "Email không hợp lệ.");
        if (!errors.containsKey("phoneNumber") && !form.get("phoneNumber").matches("\\+?[0-9]{9,15}"))
            errors.put("phoneNumber", "Số điện thoại cần có từ 9 đến 15 chữ số.");
        if (!errors.containsKey("dateOfBirth")) {
            try {
                LocalDate birth = LocalDate.parse(form.get("dateOfBirth"));
                if (birth.isAfter(LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")))) errors.put("dateOfBirth", "Ngày sinh không được ở tương lai.");
            } catch (DateTimeParseException ex) { errors.put("dateOfBirth", "Ngày sinh không hợp lệ."); }
        }
        if (!errors.containsKey("readerCategoryId")) {
            try { if (Integer.parseInt(form.get("readerCategoryId")) < 1) throw new NumberFormatException(); }
            catch (NumberFormatException ex) { errors.put("readerCategoryId", "Vui lòng chọn loại bạn đọc hợp lệ."); }
        }
        return errors;
    }
}
