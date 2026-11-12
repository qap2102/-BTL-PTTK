<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Controller.WebSupport4" %>
<%@ page import="java.util.*,Model.DocumentBorrowingStat4" %>
<%
    if (!Boolean.TRUE.equals(request.getAttribute("loaded"))) {
        String target = request.getParameter("startDate") == null && request.getParameter("endDate") == null
            ? request.getContextPath()+"/DocumentBorrowingStatServlet4"
            : WebSupport4.url(request,"/DocumentBorrowingStatServlet4","startDate",Objects.toString(request.getParameter("startDate"),""),"endDate",Objects.toString(request.getParameter("endDate"),""));
        response.sendRedirect(target); return;
    }
    List<DocumentBorrowingStat4> statistics = (List<DocumentBorrowingStat4>) request.getAttribute("statistics");
    if (statistics == null) statistics = Collections.emptyList();
    boolean searched = Boolean.TRUE.equals(request.getAttribute("searched"));
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thống kê tài liệu theo lượt mượn - LibMan</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/style4.css">
</head>
<body><main class="wide">
    <h1>Thống kê tài liệu theo số lượt mượn</h1>
    <form method="get" action="<%= request.getContextPath() %>/DocumentBorrowingStatServlet4">
        <div class="date-fields">
            <div class="field"><label for="startDate">Ngày bắt đầu</label><input type="date" id="startDate" name="startDate" required value="<%= WebSupport4.html(request.getParameter("startDate")) %>" aria-describedby="periodError"></div>
            <div class="field"><label for="endDate">Ngày kết thúc</label><input type="date" id="endDate" name="endDate" required value="<%= WebSupport4.html(request.getParameter("endDate")) %>" aria-describedby="periodError"></div>
        </div>
        <% if (request.getAttribute("error") != null) { %>
            <p class="notice error" id="periodError" role="alert"><%= WebSupport4.html(request.getAttribute("error")) %></p>
        <% } else { %><p id="periodError" class="hint">Thống kê theo ngày mượn, bao gồm cả hai ngày đã chọn.</p><% } %>
        <div class="actions">
            <button type="submit">Xem thống kê</button>
            <a class="button secondary" href="<%= request.getContextPath() %>/Manager4/StatisticsMenuUI4.jsp">Quay lại</a>
        </div>
    </form>
    <% if (searched) { %>
        <h2>Kết quả thống kê</h2>
        <div class="summary"><p>Tổng số tài liệu được mượn: <strong><%= request.getAttribute("totalDocuments") %></strong></p><p>Tổng số lượt mượn: <strong><%= request.getAttribute("totalBorrowCount") %></strong></p></div>
        <% if (statistics.isEmpty()) { %>
            <p class="notice" role="status">Không có dữ liệu thống kê trong khoảng thời gian đã chọn.</p>
            <dialog id="emptyNotice" aria-labelledby="emptyTitle"><h2 id="emptyTitle">Thông báo</h2><p>Không có dữ liệu thống kê.</p><form method="dialog"><button>OK</button></form></dialog>
            <script>const notice = document.getElementById('emptyNotice'); if (typeof notice.showModal === 'function') notice.showModal();</script>
        <% } else { %>
            <p class="hint">Chọn một tài liệu để xem các lần mượn. Danh sách sắp xếp theo số lượt mượn giảm dần.</p>
            <div class="table-scroll"><table>
                <caption>Danh sách tài liệu theo lượt mượn</caption>
                <thead><tr><th scope="col">STT</th><th scope="col">Mã tài liệu</th><th scope="col">Tên tài liệu</th><th scope="col">Tác giả</th><th scope="col">Số lượt mượn</th></tr></thead>
                <tbody>
                <% int index=0; for (DocumentBorrowingStat4 stat : statistics) {
                    String link = WebSupport4.url(request,"/DocumentBorrowingReceiptServlet4","documentCode",stat.getDocumentCode(),"startDate",request.getParameter("startDate"),"endDate",request.getParameter("endDate")); %>
                    <tr><td><%= ++index %></td><td><a href="<%= WebSupport4.html(link) %>"><%= WebSupport4.html(stat.getDocumentCode()) %></a></td><td><a href="<%= WebSupport4.html(link) %>"><%= WebSupport4.html(stat.getDocumentTitle()) %></a></td><td><%= WebSupport4.html(WebSupport4.authors(stat)) %></td><td class="number"><%= stat.getTotalBorrowCount() %></td></tr>
                <% } %>
                </tbody>
            </table></div>
        <% } %>
    <% } %>
</main></body>
</html>
