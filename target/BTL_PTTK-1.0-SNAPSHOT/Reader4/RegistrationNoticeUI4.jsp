<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Controller.WebSupport4" %>
<%@ page import="Model.*" %>
<%
    ReaderCard4 card = (ReaderCard4) request.getAttribute("card");
    if (card == null) {
        response.sendRedirect(request.getContextPath() + "/Reader4/MainMenuReaderUI4.jsp");
        return;
    }
    Reader4 reader = card.getReaderInfo();
    ReaderCategory4 category = reader.getReaderCategory();
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký thành công - LibMan</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/style4.css">
</head>
<body>
<main>
    <h1>Thông báo đăng ký</h1>
    <p class="success" role="status">Đăng ký thẻ bạn đọc thành công!</p>
    <dl>
        <dt>Mã thẻ</dt><dd><%= card.getId() %></dd>
        <dt>Họ và tên</dt><dd><%= WebSupport4.html(WebSupport4.fullName(reader)) %></dd>
        <dt>Mã bạn đọc</dt><dd><%= WebSupport4.html(reader.getReaderCode()) %></dd>
        <dt>Loại bạn đọc</dt><dd><%= WebSupport4.html(category.getReaderType()) %></dd>
        <dt>Trạng thái</dt><dd>Đang hoạt động</dd>
    </dl>
    <div class="actions"><a class="button" href="<%= request.getContextPath() %>/Reader4/MainMenuReaderUI4.jsp">OK</a></div>
</main>
</body>
</html>
