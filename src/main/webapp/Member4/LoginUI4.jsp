<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Controller.WebSupport4" %>
<%@ page import="Model.Member4" %>
<%
    Member4 currentMember = (Member4) session.getAttribute("member");
    if (currentMember != null) {
        response.sendRedirect(request.getContextPath() + WebSupport4.home(currentMember));
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - LibMan</title>
    <style>
        * { box-sizing: border-box; }
        body {
            margin: 0;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 24px;
            font-family: Arial, sans-serif;
            background: #f3f4f6;
            color: #1f2937;
        }
        main {
            width: 100%;
            max-width: 360px;
            padding: 28px;
            background: white;
            border: 1px solid #d1d5db;
            border-radius: 8px;
        }
        h1 { margin: 0 0 24px; font-size: 24px; text-align: center; }
        label { display: block; margin-bottom: 8px; }
        input {
            width: 100%;
            padding: 10px;
            margin-bottom: 20px;
            border: 1px solid #9ca3af;
            border-radius: 4px;
            font: inherit;
        }
        button {
            width: 100%;
            padding: 11px;
            border: none;
            border-radius: 4px;
            background: #2563eb;
            color: white;
            font: inherit;
            cursor: pointer;
        }
        button:hover { background: #1d4ed8; }
        input:focus-visible, button:focus-visible {
            outline: 2px solid #2563eb;
            outline-offset: 3px;
        }
    </style>
</head>
<body>
    <main>
        <h1>Đăng nhập</h1>
        <% if (request.getAttribute("error") != null) { %>
            <p role="alert" style="color: #b91c1c;"><%= WebSupport4.html(request.getAttribute("error")) %></p>
        <% } %>
        <form id="loginForm" method="post" action="<%= request.getContextPath() %>/login">
            <input type="hidden" name="csrfToken" value="<%= WebSupport4.csrf(session) %>">
            <label for="userName">Tên đăng nhập</label>
            <input type="text" id="userName" name="userName" autocomplete="username" maxlength="255" value="<%= WebSupport4.html(request.getAttribute("userName")) %>" required>

            <label for="password">Mật khẩu</label>
            <input type="password" id="password" name="password" autocomplete="current-password" required>

            <button type="submit">Đăng nhập</button>
        </form>
    </main>
</body>
</html>
