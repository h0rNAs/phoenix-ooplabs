<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String user = (String) session.getAttribute("username");
%>
<header>
    <div class="logo">
        <span class="math-text">Math</span>
        <a href="index.jsp" class="hub-button"><span class="hub-text">Hub</span></a>
    </div>
    <div class="right-corner">
        <% if (user == null) { %>
        <span class="user-name" style="color: #666; font-size: 14px;">Не авторизован</span>
        <% } else { %>
        <a href="login.jsp" class="account-icon-button" title="Управление аккаунтом">
            <img src="images/user-icon.svg" alt="Аккаунт" class="account-icon">
            <span class="user-name"><%= user %></span>
        </a>
        <a href="logout.jsp" class="logout-icon-button" title="Выйти">
            <img src="images/logout-icon.svg" alt="Выйти" class="logout-icon">
        </a>
        <% } %>
        <button id="theme-toggle" class="theme-button">
            <img src="images/sun-icon.svg" alt="Светлая тема" class="theme-icon sun-icon">
            <img src="images/moon-icon.svg" alt="Тёмная тема" class="theme-icon moon-icon">
        </button>
    </div>
</header>