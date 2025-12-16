<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String user = (String) session.getAttribute("username");
    Long userId = Long.valueOf((String)session.getAttribute("userId"));
    if (user == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MathHub - Управление аккаунтом</title>
    <link rel="stylesheet" href="css/styles.css">
    <script src="js/script.js" defer></script>
    <style>
        .account-info {
            background: #f9f9f9;
            padding: 20px;
            border-radius: 12px;
            margin-bottom: 25px;
            border: 2px solid #ddd;
            box-shadow: 0 4px 12px rgba(0,0,0,0.05);
        }
        .dark-theme .account-info {
            background: #4a5568;
            border-color: #718096;
            color: white;
            box-shadow: 0 4px 12px rgba(0,0,0,0.2);
        }
        .info-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 16px;
            padding-bottom: 12px;
            border-bottom: 1px solid #eee;
        }
        .dark-theme .info-row {
            border-bottom-color: #718096;
        }
        .info-row:last-child {
            border-bottom: none;
            margin-bottom: 0;
            padding-bottom: 0;
        }
        .info-label {
            font-weight: 600;
            color: #555;
            font-size: 15px;
        }
        .dark-theme .info-label {
            color: #cbd5e0;
        }
        .info-value {
            font-weight: 500;
            color: #333;
            font-size: 15px;
        }
        .dark-theme .info-value {
            color: white;
        }
        .user-id {
            background: linear-gradient(135deg, #FFA500, #FF8C00);
            color: white;
            padding: 8px 16px;
            border-radius: 20px;
            font-weight: 600;
            font-size: 15px;
            letter-spacing: 1px;
            box-shadow: 0 2px 8px rgba(255, 140, 0, 0.3);
        }
        .dark-theme .user-id {
            background: linear-gradient(135deg, #FFA500, #FF8C00);
            box-shadow: 0 2px 8px rgba(255, 140, 0, 0.5);
        }
        .auth-input {
            width: 100%;
            padding: 14px;
            margin-bottom: 15px;
            border: 2px solid #ddd;
            border-radius: 8px;
            background: white;
            color: #333;
            box-sizing: border-box;
            font-size: 15px;
            transition: all 0.3s ease;
        }
        .dark-theme .auth-input {
            background: #4a5568;
            border-color: #718096;
            color: white;
        }
        .auth-input:focus {
            border-color: var(--orange-accent);
            outline: none;
            box-shadow: 0 0 0 3px rgba(255, 165, 0, 0.1);
        }
        .auth-button {
            width: 100%;
            padding: 14px;
            background: var(--orange-accent);
            color: white;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            transition: all 0.3s ease;
            font-size: 16px;
            font-weight: 600;
            margin-top: 5px;
        }
        .auth-button:hover {
            background: var(--orange-hover);
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(255, 140, 0, 0.3);
        }
        .success-message {
            color: #00aa00;
            text-align: center;
            margin-top: 15px;
            font-size: 14px;
            font-weight: 500;
            padding: 12px;
            background: rgba(0, 170, 0, 0.1);
            border-radius: 8px;
            border-left: 4px solid #00aa00;
        }
        .dark-theme .success-message {
            color: #4CAF50;
            background: rgba(76, 175, 80, 0.1);
            border-left-color: #4CAF50;
        }
        .error-message {
            color: #ff4444;
            text-align: center;
            margin-top: 15px;
            font-size: 14px;
            font-weight: 500;
            padding: 12px;
            background: rgba(255, 68, 68, 0.1);
            border-radius: 8px;
            border-left: 4px solid #ff4444;
        }
        .dark-theme .error-message {
            color: #ff4444;
            background: rgba(255, 68, 68, 0.15);
            border-left-color: #ff4444;
        }
        .close-button {
            position: absolute;
            top: 15px;
            right: 15px;
            text-decoration: none;
            color: #666;
            font-size: 28px;
            width: 40px;
            height: 40px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 50%;
            transition: all 0.3s ease;
            font-weight: 300;
            background: rgba(0,0,0,0.05);
        }
        .dark-theme .close-button {
            color: #ccc;
            background: rgba(255,255,255,0.1);
        }
        .close-button:hover {
            background: rgba(255, 165, 0, 0.2);
            color: var(--orange-accent);
            transform: rotate(90deg);
        }
        .section-title {
            color: var(--orange-accent);
            font-size: 18px;
            margin: 25px 0 15px 0;
            padding-bottom: 10px;
            border-bottom: 2px solid rgba(255, 165, 0, 0.2);
        }
        .dark-theme .section-title {
            color: var(--orange-accent);
            border-bottom-color: rgba(255, 165, 0, 0.3);
        }
        .password-form {
            background: #f9f9f9;
            padding: 25px;
            border-radius: 12px;
            border: 2px solid #ddd;
            margin-bottom: 30px;
        }
        .dark-theme .password-form {
            background: #4a5568;
            border-color: #718096;
        }
        .form-description {
            color: #666;
            font-size: 14px;
            margin-bottom: 20px;
            line-height: 1.5;
        }
        .dark-theme .form-description {
            color: #a0aec0;
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp" />
<main style="max-width: 500px; margin: 0 auto; padding: 20px; position: relative;">
    <!-- Кнопка закрытия -->
    <a href="index.jsp" class="close-button" title="Вернуться к калькулятору">
        ×
    </a>

    <h2 style="text-align: center; margin-top: 20px; margin-bottom: 30px; color: var(--orange-accent);">Информация об аккаунте</h2>

    <div class="account-info">
        <div class="info-row">
            <span class="info-label">Логин:</span>
            <span class="info-value"><%= user %></span>
        </div>
        <div class="info-row">
            <span class="info-label">ID пользователя:</span>
            <span class="user-id">#<%= String.format("%06d", userId) %></span>
        </div>
        <div class="info-row">
            <span class="info-label">Тип аккаунта:</span>
            <span class="info-value" style="color: var(--orange-accent); font-weight: 600;">Стандартный</span>
        </div>
    </div>

    <h3 class="section-title">Смена пароля</h3>
    <div class="password-form">
        <p class="form-description">
            Для смены пароля введите текущий пароль, затем новый пароль и подтвердите его.
        </p>
        <form action="login.jsp" method="post">
            <input type="password" name="currentPassword" placeholder="Текущий пароль" required class="auth-input">
            <input type="password" name="newPassword" placeholder="Новый пароль" required class="auth-input">
            <input type="password" name="confirmNewPassword" placeholder="Подтвердите новый пароль" required class="auth-input">
            <button type="submit" name="action" value="changePassword" class="auth-button">
                Сменить пароль
            </button>
        </form>
    </div>

    <%
        String action = request.getParameter("action");
        if ("changePassword".equals(action)) {
            String currentPassword = request.getParameter("currentPassword");
            String newPassword = request.getParameter("newPassword");
            String confirmNewPassword = request.getParameter("confirmNewPassword");

            String storedPassword = (String) session.getAttribute("password");

            if (storedPassword != null && storedPassword.equals(currentPassword)) {
                if (newPassword.equals(confirmNewPassword)) {
                    if (newPassword.length() < 6) {
                        out.println("<div class='error-message'>Пароль должен содержать минимум 6 символов!</div>");
                    } else {
                        session.setAttribute("password", newPassword);
                        out.println("<div class='success-message'>✅ Пароль успешно изменен!</div>");
                    }
                } else {
                    out.println("<div class='error-message'>❌ Новые пароли не совпадают!</div>");
                }
            } else {
                out.println("<div class='error-message'>❌ Неверный текущий пароль!</div>");
            }
        }
    %>
</main>
</body>
</html>