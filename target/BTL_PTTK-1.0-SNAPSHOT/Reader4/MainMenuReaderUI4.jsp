<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Controller.WebSupport4" %>
<%@ page import="Model.Reader4" %>
<% Reader4 reader = (Reader4) session.getAttribute("member"); %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang chính bạn đọc - LibMan</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/style4.css">
</head>
<body>
<main>
    <h1>Trang chính bạn đọc</h1>
    <p>Xin chào, <strong><%= WebSupport4.html(WebSupport4.fullName(reader)) %></strong>.</p>
    <p>Mã bạn đọc: <%= WebSupport4.html(reader.getReaderCode()) %></p>
    <div class="actions">
        <a class="button" href="<%= request.getContextPath() %>/RegistrationServlet4">Đăng ký làm thẻ bạn đọc</a>
        <form method="post" action="<%= request.getContextPath() %>/logout">
            <input type="hidden" name="csrfToken" value="<%= WebSupport4.csrf(session) %>">
            <button class="secondary" type="submit">Đăng xuất</button>
        </form>
    </div>
</main>
</body>
</html>
