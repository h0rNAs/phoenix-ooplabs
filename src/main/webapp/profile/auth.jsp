<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Вход и регистрация</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 900px;
            margin: 50px auto;
            padding: 20px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }
        .container {
            display: flex;
            gap: 30px;
            background: white;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.3);
            overflow: hidden;
        }
        .form-section {
            flex: 1;
            padding: 40px;
        }
        .form-section:first-child {
            border-right: 1px solid #eee;
        }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #333;
        }
        input {
            width: 100%;
            padding: 12px;
            border: 2px solid #ddd;
            border-radius: 8px;
            box-sizing: border-box;
            font-size: 16px;
            transition: border-color 0.3s;
        }
        input:focus {
            border-color: #667eea;
            outline: none;
        }
        .btn {
            color: white;
            padding: 14px 20px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            width: 100%;
            font-size: 16px;
            font-weight: bold;
            transition: background-color 0.3s;
        }
        .btn-login {
            background: #4CAF50;
        }
        .btn-login:hover {
            background: #45a049;
        }
        .error {
            color: #d32f2f;
            background: #ffebee;
            padding: 12px;
            border-radius: 8px;
            margin: 15px 0;
            border-left: 4px solid #d32f2f;
        }
        .success {
            color: #388e3c;
            background: #e8f5e8;
            padding: 12px;
            border-radius: 8px;
            margin: 15px 0;
            border-left: 4px solid #388e3c;
        }
        .form-title {
            text-align: center;
            color: #333;
            margin-bottom: 30px;
            font-size: 24px;
        }
        .toggle-link {
            text-align: center;
            margin-top: 20px;
            color: #666;
        }
        .toggle-link a {
            color: #2196F3;
            text-decoration: none;
        }
        .toggle-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="form-section">
        <h2 class="form-title">Вход в систему</h2>

        <% if (request.getAttribute("error") != null) { %>
        <div class="error"><%= request.getAttribute("error") %></div>
        <% } %>

        <form action="auth" method="post">
            <input type="hidden" name="action" value="login">

            <div class="form-group">
                <label for="username">Имя пользователя:</label>
                <input type="text" id="username" name="username" required
                       value="<%= request.getAttribute("username") != null ? request.getAttribute("username") : "" %>">
            </div>

            <div class="form-group">
                <label for="password">Пароль:</label>
                <input type="password" id="password" name="password" required>
            </div>

            <button type="submit" class="btn btn-login">Зарегистрироваться/Войти</button>
        </form>
    </div>
</div>

<script>
    // Автофокус на соответствующем поле при ошибках
    window.onload = function() {
        <% if (request.getAttribute("error") != null) { %>
        document.getElementById('username').focus();
        <% } %>
    }
</script>
</body>
</html>