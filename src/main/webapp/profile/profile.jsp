<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ru.ssau.tk.phoenix.ooplabs.dto.UserResponse" %>
<%
    UserResponse profileUser = (UserResponse) request.getAttribute("profileUser");
    UserResponse currentUser = (UserResponse) session.getAttribute("user");
    boolean isOwnProfile = currentUser != null && profileUser != null && currentUser.getId() == profileUser.getId();
%>
<html>
<head>
    <title>Профиль пользователя</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 600px; margin: 50px auto; padding: 20px; }
        .profile-card { background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        .user-info { background: #f9f9f9; padding: 20px; border-radius: 5px; margin: 20px 0; }
        .btn { display: inline-block; padding: 10px 20px; margin: 5px; text-decoration: none; border-radius: 5px; }
        .btn-primary { background: #2196F3; color: white; }
        .btn-secondary { background: #757575; color: white; }
        .error { color: #d32f2f; background: #ffebee; padding: 10px; border-radius: 5px; margin: 10px 0; }
        .search-form { margin: 20px 0; padding: 15px; background: #e3f2fd; border-radius: 5px; }
    </style>
</head>
<body>
<div class="profile-card">
    <h2>Профиль пользователя</h2>

    <% if (request.getAttribute("error") != null) { %>
    <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>

    <% if (profileUser != null) { %>
    <div class="user-info">
        <h3>Информация о пользователе</h3>
        <p><strong>ID:</strong> <%= profileUser.getId() %></p>
        <p><strong>Имя пользователя:</strong> <%= profileUser.getUsername() %></p>
    </div>

    <% if (isOwnProfile) { %>
    <div>
        <a href="update-password" class="btn btn-primary">Сменить пароль</a>
        <a href="logout" class="btn btn-secondary">Выйти</a>
    </div>
    <% } %>
    <% } %>

    <!-- Форма поиска пользователя -->
    <div class="search-form">
        <h3>Поиск пользователя</h3>
        <form action="search" method="get" style="display: flex; gap: 10px;">
            <input type="text" name="id" placeholder="Поиск по ID" style="flex: 1;">
            <input type="text" name="username" placeholder="Поиск по имени" style="flex: 1;">
            <button type="submit" class="btn btn-primary" style="padding: 10px;">Найти</button>
        </form>
    </div>

    <% if (currentUser != null && !isOwnProfile) { %>
    <a href="profile" class="btn btn-secondary">Мой профиль</a>
    <% } %>

    <% if (currentUser == null) { %>
    <p><a href="auth">Войдите</a> чтобы увидеть свой профиль</p>
    <% } %>
</div>
</body>
</html>