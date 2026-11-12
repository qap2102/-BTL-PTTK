<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Controller.WebSupport4" %>
<%@ page import="java.util.*,Model.*" %>
<%
    if (!Boolean.TRUE.equals(request.getAttribute("loaded"))) {
        response.sendRedirect(request.getContextPath()+"/DocumentBorrowingStatServlet4"); return;
    }
    Document4 document = (Document4) request.getAttribute("document");
    List<BorrowingReceiptDetail4> details = (List<BorrowingReceiptDetail4>) request.getAttribute("details");
    if (details == null) details = Collections.emptyList();
    String backLink = WebSupport4.url(request,"/DocumentBorrowingStatServlet4","startDate",request.getParameter("startDate"),"endDate",request.getParameter("endDate"));
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Các lần mượn tài liệu - LibMan</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/style4.css">
</head>
<body><main class="wide">
    <h1>Các lần mượn tài liệu</h1>
    <% if (request.getAttribute("error") != null) { %><p class="notice error" role="alert"><%= WebSupport4.html(request.getAttribute("error")) %></p><% } %>
    <% if (document != null) { %>
        <dl>
            <dt>Mã tài liệu</dt><dd><%= WebSupport4.html(document.getDocumentCode()) %></dd>
            <dt>Tên tài liệu</dt><dd><%= WebSupport4.html(document.getDocumentTitle()) %></dd>
            <dt>Tổng số lượt mượn</dt><dd><%= details.size() %></dd>
        </dl>
        <% if (details.isEmpty()) { %><p role="status">Không có lần mượn nào trong khoảng thời gian đã chọn.</p><% } else { %>
            <p class="hint">Chọn mã phiếu mượn để xem đầy đủ các bản tài liệu trong phiếu.</p>
            <div class="table-scroll"><table>
                <caption>Danh sách các lần mượn của tài liệu</caption>
                <thead><tr><th scope="col">STT</th><th scope="col">Mã bản tài liệu</th><th scope="col">Mã phiếu mượn</th><th scope="col">Mã bạn đọc</th><th scope="col">Ngày mượn</th></tr></thead>
                <tbody>
                <% int index=0; for (BorrowingReceiptDetail4 detail : details) {
                    BorrowingReceipt4 receipt = detail.getBorrowingReceipt();
                    String link = WebSupport4.url(request,"/BorrowingReceiptDetailServlet4","id",String.valueOf(receipt.getId()),"documentCode",document.getDocumentCode(),"startDate",request.getParameter("startDate"),"endDate",request.getParameter("endDate")); %>
                    <tr><td><%= ++index %></td><td><%= WebSupport4.html(detail.getDocumentCopyInfo().getCopyCode()) %></td><td><a href="<%= WebSupport4.html(link) %>"><%= WebSupport4.html(receipt.getReceiptCode()) %></a></td><td><%= WebSupport4.html(receipt.getReader().getReaderCode()) %></td><td><%= WebSupport4.date(receipt.getBorrowDate()) %></td></tr>
                <% } %>
                </tbody>
            </table></div>
        <% } %>
    <% } %>
    <div class="actions"><a class="button secondary" href="<%= WebSupport4.html(backLink) %>">Quay lại thống kê</a></div>
</main></body>
</html>
