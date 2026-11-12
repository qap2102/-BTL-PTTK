<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Controller.WebSupport4" %>
<%@ page import="Model.Member4" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang chính quản lý - LibMan</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/style4.css">
</head>
<body><main>
    <h1>Trang chính quản lý</h1>
    <p>Xin chào, <strong><%= WebSupport4.html(WebSupport4.fullName((Member4) session.getAttribute("member"))) %></strong>.</p>
    <p><a class="button" href="<%= request.getContextPath() %>/Manager4/StatisticsMenuUI4.jsp">Xem báo cáo thống kê</a></p>
    <form method="post" action="<%= request.getContextPath() %>/logout">
        <input type="hidden" name="csrfToken" value="<%= WebSupport4.csrf(session) %>">
        <button class="secondary" type="submit">Đăng xuất</button>
    </form>
</main></body>
</html>
