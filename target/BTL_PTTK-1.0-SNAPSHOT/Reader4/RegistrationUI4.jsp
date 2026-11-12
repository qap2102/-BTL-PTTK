<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Controller.WebSupport4" %>
<%@ page import="java.util.*,Model.ReaderCategory4" %>
<%
    if (request.getAttribute("form") == null && request.getAttribute("error") == null) {
        response.sendRedirect(request.getContextPath() + "/RegistrationServlet4");
        return;
    }
    Map<String, String> form = (Map<String, String>) request.getAttribute("form");
    if (form == null) form = Collections.emptyMap();
    Map<String, String> errors = (Map<String, String>) request.getAttribute("errors");
    if (errors == null) errors = Collections.emptyMap();
    List<ReaderCategory4> categories = (List<ReaderCategory4>) request.getAttribute("categories");
    if (categories == null) categories = Collections.emptyList();
    boolean disabled = Boolean.TRUE.equals(request.getAttribute("alreadyRegistered"))
            || Boolean.TRUE.equals(request.getAttribute("unavailable")) || categories.isEmpty();
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký làm thẻ bạn đọc - LibMan</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/style4.css">
</head>
<body>
<main>
    <h1>Đăng ký làm thẻ bạn đọc</h1>
    <p class="hint">Vui lòng kiểm tra và nhập đầy đủ thông tin trước khi đăng ký.</p>
    <% if (request.getAttribute("error") != null) { %>
        <p class="notice error" role="alert"><%= WebSupport4.html(request.getAttribute("error")) %></p>
    <% } %>
    <% if (!errors.isEmpty()) { %>
        <p class="notice error" role="alert">Vui lòng kiểm tra các thông tin được đánh dấu bên dưới.</p>
    <% } %>
    <% if (categories.isEmpty() && request.getAttribute("error") == null) { %>
        <p class="notice error" role="alert">Chưa có loại bạn đọc để đăng ký. Vui lòng liên hệ thư viện.</p>
    <% } %>
    <form method="post" action="<%= request.getContextPath() %>/RegistrationServlet4">
        <input type="hidden" name="csrfToken" value="<%= WebSupport4.csrf(session) %>">
        <div class="field">
            <label for="fullName">Họ và tên</label>
            <input id="fullName" name="fullName" autocomplete="name" maxlength="255" required value="<%= WebSupport4.html(form.get("fullName")) %>" aria-describedby="fullNameError">
            <span class="field-error" id="fullNameError"><%= WebSupport4.html(errors.get("fullName")) %></span>
        </div>
        <div class="field">
            <label for="dateOfBirth">Ngày sinh</label>
            <input type="date" id="dateOfBirth" name="dateOfBirth" autocomplete="bday" required value="<%= WebSupport4.html(form.get("dateOfBirth")) %>" aria-describedby="dateOfBirthError">
            <span class="field-error" id="dateOfBirthError"><%= WebSupport4.html(errors.get("dateOfBirth")) %></span>
        </div>
        <div class="field">
            <label for="phoneNumber">Số điện thoại</label>
            <input type="tel" id="phoneNumber" name="phoneNumber" autocomplete="tel" maxlength="16" pattern="\+?[0-9]{9,15}" required value="<%= WebSupport4.html(form.get("phoneNumber")) %>" aria-describedby="phoneNumberError">
            <span class="field-error" id="phoneNumberError"><%= WebSupport4.html(errors.get("phoneNumber")) %></span>
        </div>
        <div class="field">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" autocomplete="email" maxlength="255" required value="<%= WebSupport4.html(form.get("email")) %>" aria-describedby="emailError">
            <span class="field-error" id="emailError"><%= WebSupport4.html(errors.get("email")) %></span>
        </div>
        <div class="field">
            <label for="readerCategoryId">Loại bạn đọc</label>
            <select id="readerCategoryId" name="readerCategoryId" required aria-describedby="categoryError">
                <option value="">-- Chọn loại bạn đọc --</option>
                <% for (ReaderCategory4 category : categories) { %>
                    <option value="<%= category.getId() %>" <%= String.valueOf(category.getId()).equals(form.get("readerCategoryId")) ? "selected" : "" %>><%= WebSupport4.html(category.getReaderType()) %> — tối đa <%= category.getMaxBorrowQuantity() %> cuốn / <%= category.getBorrowPeriod() %> ngày</option>
                <% } %>
            </select>
            <span class="field-error" id="categoryError"><%= WebSupport4.html(errors.get("readerCategoryId")) %></span>
        </div>
        <div class="field">
            <label for="readerCode">Mã bạn đọc</label>
            <input id="readerCode" name="readerCode" maxlength="255" required value="<%= WebSupport4.html(form.get("readerCode")) %>" aria-describedby="readerCodeError">
            <span class="field-error" id="readerCodeError"><%= WebSupport4.html(errors.get("readerCode")) %></span>
        </div>
        <div class="actions">
            <button type="submit" <%= disabled ? "disabled" : "" %>>Đăng ký</button>
            <a class="button secondary" href="<%= request.getContextPath() %>/Reader4/MainMenuReaderUI4.jsp">Hủy</a>
        </div>
    </form>
</main>
<% if (request.getAttribute("error") != null || !errors.isEmpty()) { %>
<dialog id="registrationFeedback" aria-labelledby="feedbackTitle">
    <h2 id="feedbackTitle">Thông báo</h2>
    <p><%= WebSupport4.html(request.getAttribute("error") != null ? request.getAttribute("error") : "Vui lòng nhập đầy đủ và kiểm tra lại thông tin đăng ký.") %></p>
    <form method="dialog"><button type="submit">OK</button></form>
</dialog>
<script>
    const feedback = document.getElementById('registrationFeedback');
    if (typeof feedback.showModal === 'function') feedback.showModal();
</script>
<% } %>
</body>
</html>
