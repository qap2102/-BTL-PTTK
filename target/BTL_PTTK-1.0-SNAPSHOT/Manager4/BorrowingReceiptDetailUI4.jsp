<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Controller.WebSupport4" %>
<%@ page import="java.util.*,Model.*" %>
<%
    if (!Boolean.TRUE.equals(request.getAttribute("loaded"))) {
        response.sendRedirect(request.getContextPath()+"/DocumentBorrowingStatServlet4"); return;
    }
    BorrowingReceipt4 receipt = (BorrowingReceipt4) request.getAttribute("receipt");
    Map<Integer,Boolean> returnedStatus = (Map<Integer,Boolean>) request.getAttribute("returnedStatus");
    if (returnedStatus == null) returnedStatus = Collections.emptyMap();
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết phiếu mượn - LibMan</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/style4.css">
</head>
<body><main class="wide">
    <h1>Chi tiết phiếu mượn</h1>
    <% if (request.getAttribute("error") != null) { %><p class="notice error" role="alert"><%= WebSupport4.html(request.getAttribute("error")) %></p><% } %>
    <% if (receipt != null) { %>
        <dl>
            <dt>Mã phiếu mượn</dt><dd><%= WebSupport4.html(receipt.getReceiptCode()) %></dd>
            <dt>Bạn đọc</dt><dd><%= WebSupport4.html(WebSupport4.fullName(receipt.getReader())) %></dd>
            <dt>Mã bạn đọc</dt><dd><%= WebSupport4.html(receipt.getReader().getReaderCode()) %></dd>
            <dt>Email</dt><dd><%= WebSupport4.html(receipt.getReader().getEmail()) %></dd>
            <dt>Số điện thoại</dt><dd><%= WebSupport4.html(receipt.getReader().getPhoneNumber()) %></dd>
            <dt>Nhân viên cho mượn</dt><dd><%= WebSupport4.html(WebSupport4.fullName(receipt.getLibrarian())) %></dd>
            <dt>Ngày mượn</dt><dd><%= WebSupport4.date(receipt.getBorrowDate()) %></dd>
            <dt>Ngày hẹn trả</dt><dd><%= WebSupport4.date(receipt.getDueDate()) %></dd>
            <dt>Trạng thái phiếu</dt><dd><%= WebSupport4.html(receipt.getStatus()) %></dd>
        </dl>
        <h2>Danh sách bản tài liệu được mượn</h2>
        <div class="table-scroll"><table>
            <caption>Chi tiết các bản tài liệu trong phiếu mượn</caption>
            <thead><tr><th scope="col">STT</th><th scope="col">Mã bản tài liệu</th><th scope="col">Mã tài liệu</th><th scope="col">Tên tài liệu</th><th scope="col">Tình trạng lúc mượn</th><th scope="col">Trạng thái trả</th></tr></thead>
            <tbody>
            <% int index=0; for (BorrowingReceiptDetail4 detail : receipt.getBorrowingList()) {
                DocumentCopy4 copy = detail.getDocumentCopyInfo(); %>
                <tr class="<%= copy.getDocument().getDocumentCode().equals(request.getParameter("documentCode")) ? "selected-row" : "" %>"><td><%= ++index %></td><td><%= WebSupport4.html(copy.getCopyCode()) %></td><td><%= WebSupport4.html(copy.getDocument().getDocumentCode()) %></td><td><%= WebSupport4.html(copy.getDocument().getDocumentTitle()) %></td><td><%= WebSupport4.html(detail.getBorrowingCondition()) %></td><td><%= Boolean.TRUE.equals(returnedStatus.get(detail.getId())) ? "Đã trả" : "Chưa trả" %></td></tr>
            <% } %>
            </tbody>
        </table></div>
    <% } %>
    <div class="actions"><a class="button secondary" href="<%= WebSupport4.html(request.getAttribute("backLink")) %>">Quay lại</a></div>
</main></body>
</html>
