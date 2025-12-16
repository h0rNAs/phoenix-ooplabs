<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MathHub - Личный кабинет</title>
    <link rel="stylesheet" href="css/styles.css">
    <script src="js/script.js" defer></script>
</head>
<body>
<jsp:include page="header.jsp" />
<main>
    <%
        String user = (String) session.getAttribute("username");
        if (user == null) {
            response.sendRedirect("login.jsp");
        } else {
    %>
    <h1>Добро пожаловать, <%= user %>!</h1>
    <p>Это ваш личный кабинет.</p>
    <a href="logout.jsp">Выйти</a>
    <%
        }
    %>
</main>
</body>
</html>