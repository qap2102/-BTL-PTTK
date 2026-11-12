<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Báo cáo thống kê - LibMan</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/style4.css">
</head>
<body><main>
    <h1>Báo cáo thống kê</h1>
    <p>Chọn báo cáo cần xem.</p>
    <p><a class="button" href="<%= request.getContextPath() %>/DocumentBorrowingStatServlet4">Thống kê tài liệu theo số lượt mượn</a></p>
    <div class="actions"><a class="button secondary" href="<%= request.getContextPath() %>/Manager4/MainMenuManagerUI4.jsp">Quay lại</a></div>
</main></body>
</html>
