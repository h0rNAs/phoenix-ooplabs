<%@ page contentType="text/html;charset=UTF-8"%>
<%
    String user = (String) session.getAttribute("username");
    boolean showAuthModal = user == null;

    String action = request.getParameter("action");
    if (action != null) {
        String username = request.getParameter("username");
        String id = request.getParameter("id");

        if (id != null && !id.trim().isEmpty()) {
            Cookie idCookie = new Cookie("userId", id);
            idCookie.setPath("/");
            response.addCookie(idCookie);
        }

        if (username != null && !username.trim().isEmpty()) {
            session.setAttribute("username", username);
            session.setAttribute("userId", id);
            response.sendRedirect("index.jsp");
            return;
        }
    }
%>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MathHub - Графический калькулятор</title>
    <link rel="stylesheet" href="css/styles.css">
    <script src="js/script.js" defer></script>
    <script src="js/graph-calculator.js" defer></script>
    <script src="js/functions-manager.js" defer></script>
    <style>
        /* Стили для модального окна авторизации */
        #authModal {
            display: <%= showAuthModal ? "flex" : "none" %>;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.7);
            z-index: 2000;
            align-items: center;
            justify-content: center;
            backdrop-filter: blur(3px);
        }

        .modal-content {
            background: white;
            border-radius: 12px;
            padding: 30px;
            width: 90%;
            max-width: 400px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
            position: relative;
        }

        .dark-theme .modal-content {
            background: #2d3748;
            color: white;
        }

        .modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 25px;
            padding-bottom: 15px;
            border-bottom: 2px solid var(--orange-accent);
        }

        .modal-header h3 {
            margin: 0;
            font-size: 22px;
            color: var(--orange-accent);
        }

        .close-modal {
            background: none;
            border: none;
            font-size: 28px;
            cursor: pointer;
            color: #666;
            width: 36px;
            height: 36px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 50%;
            transition: all 0.3s ease;
        }

        .close-modal:hover {
            background: rgba(255, 165, 0, 0.1);
            color: var(--orange-accent);
            transform: rotate(90deg);
        }

        .dark-theme .close-modal {
            color: #a0aec0;
        }

        .dark-theme .close-modal:hover {
            color: var(--orange-accent);
        }

        .auth-tabs {
            display: flex;
            margin-bottom: 25px;
            border-bottom: 2px solid #e0e0e0;
        }

        .dark-theme .auth-tabs {
            border-bottom-color: #4a5568;
        }

        .auth-tab {
            flex: 1;
            padding: 14px;
            background: none;
            border: none;
            cursor: pointer;
            font-size: 16px;
            font-weight: 500;
            color: #666;
            transition: all 0.3s ease;
            border-bottom: 3px solid transparent;
        }

        .dark-theme .auth-tab {
            color: #a0aec0;
        }

        .auth-tab.active {
            color: var(--orange-accent);
            border-bottom-color: var(--orange-accent);
            font-weight: 600;
        }

        .auth-tab:hover:not(.active) {
            background: rgba(255, 165, 0, 0.05);
            color: var(--orange-accent);
        }

        .auth-form {
            display: none;
        }

        .auth-form.active {
            display: block;
        }

        .auth-input {
            width: 100%;
            padding: 14px;
            margin-bottom: 15px;
            border: 2px solid #ddd;
            border-radius: 8px;
            font-size: 15px;
            box-sizing: border-box;
            transition: all 0.3s ease;
            background: white;
            color: #333;
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
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            margin-top: 10px;
        }

        .auth-button:hover {
            background: var(--orange-hover);
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(255, 140, 0, 0.3);
        }

        .error-message {
            background: #ff4444;
            color: white;
            padding: 12px;
            border-radius: 8px;
            margin-top: 15px;
            font-size: 14px;
            text-align: center;
            animation: fadeIn 0.3s ease;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(-10px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .success-message {
            background: #4CAF50;
            color: white;
            padding: 12px;
            border-radius: 8px;
            margin-top: 15px;
            font-size: 14px;
            text-align: center;
            animation: fadeIn 0.3s ease;
        }

        /* Блокировка интерфейса при неавторизованном доступе */
        .calculator-main:not(.authenticated) {
            filter: blur(3px);
            pointer-events: none;
            user-select: none;
        }

        /* Сообщение о необходимости авторизации */
        .auth-required-message {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background: rgba(0, 0, 0, 0.8);
            color: white;
            padding: 20px 30px;
            border-radius: 10px;
            z-index: 1500;
            text-align: center;
            font-size: 18px;
            backdrop-filter: blur(5px);
            border: 2px solid var(--orange-accent);
        }

        .dark-theme .auth-required-message {
            background: rgba(45, 55, 72, 0.95);
        }

        /* Стиль для отображения координат */
        .coordinates-display {
            position: absolute;
            top: 20px;
            left: 20px;
            background: rgba(255, 255, 255, 0.9);
            padding: 8px 15px;
            border-radius: 20px;
            font-size: 14px;
            font-weight: 500;
            color: #333;
            z-index: 100;
            border: 2px solid var(--orange-accent);
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            pointer-events: none;
        }

        .dark-theme .coordinates-display {
            background: rgba(45, 55, 72, 0.9);
            color: white;
            border-color: var(--orange-accent);
        }

        /* Стиль для уведомления о загрузке */
        .upload-notification {
            position: fixed;
            bottom: 20px;
            left: 50%;
            transform: translateX(-50%);
            background: var(--orange-accent);
            color: white;
            padding: 12px 24px;
            border-radius: 8px;
            z-index: 1000;
            font-weight: 500;
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
            animation: slideUp 0.3s ease-out;
        }

        @keyframes slideUp {
            from {
                opacity: 0;
                transform: translate(-50%, 20px);
            }
            to {
                opacity: 1;
                transform: translate(-50%, 0);
            }
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp" />

<main class="calculator-main <%= user == null ? "not-authenticated" : "authenticated" %>">
    <div class="graph-panel" id="graphPanel">
        <div class="coordinates-display" id="coordinatesDisplay">x: 0, y: 0</div>
        <canvas id="graphCanvas"></canvas>
    </div>

    <div class="control-panel">
        <button class="menu-btn" id="menuBtn" title="Меню функций">
            <img src="images/menu-icon.svg" alt="Меню" class="btn-icon">
        </button>

        <div class="action-buttons">
            <button class="action-btn" id="loadBtn" title="Загрузить функцию">
                <img src="images/load-icon.svg" alt="Загрузить" class="btn-icon">
            </button>
            <button class="action-btn" id="resetViewBtn" title="Сбросить вид">
                <img src="images/reset-icon.svg" alt="Сбросить" class="btn-icon">
            </button>
        </div>
    </div>

    <div class="functions-menu" id="functionsMenu">
        <div class="menu-header">
            <h3>Управление функциями</h3>
            <button class="close-btn" id="closeMenu" title="Закрыть">
                <img src="images/close-icon.svg" alt="Закрыть">
            </button>
        </div>

        <div class="menu-content">
            <button class="add-function-btn" id="addFunctionBtn">
                Создать новую функцию
            </button>

            <div class="function-type-selector" id="typeSelector">
                <button class="type-option" data-type="SIMPLE">Простая функция</button>
                <button class="type-option" data-type="TABULATED">Табулированная функция</button>
                <button class="type-option" data-type="COMPOSITE">Оперируемая функция</button>
            </div>

            <div class="functions-list" id="functionsList">
                <!-- Список функций -->
            </div>
        </div>
    </div>

    <% if (user == null) { %>
    <div class="auth-required-message">
        <div style="font-size: 24px; margin-bottom: 10px;">🔒</div>
        <div>Для использования калькулятора требуется авторизация</div>
        <div style="font-size: 14px; margin-top: 10px; opacity: 0.8;">Нажмите в любом месте, чтобы открыть окно авторизации</div>
    </div>
    <% } %>
</main>

<!-- Модальное окно авторизации -->
<% if (showAuthModal) { %>
<div id="authModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Авторизация в MathHub</h3>
            <button class="close-modal" onclick="closeAuthModal()">×</button>
        </div>

        <div class="auth-tabs">
            <button class="auth-tab active" onclick="switchTab('login')">Вход</button>
            <button class="auth-tab" onclick="switchTab('register')">Регистрация</button>
        </div>

        <form id="loginForm" class="auth-form active">
            <input type="text" id="loginUsername" name="username" placeholder="Логин" required class="auth-input">
            <input type="password" id="loginPassword" name="password" placeholder="Пароль" required class="auth-input">
            <button type="button" onclick="login()" class="auth-button">Войти</button>
        </form>

        <form id="registerForm" class="auth-form">
            <input type="text" id="regUsername" name="username" placeholder="Логин" required class="auth-input">
            <input type="password" id="regPassword" name="password" placeholder="Пароль" required class="auth-input">
            <input type="password" id="regConfirmPassword" name="confirmPassword" placeholder="Подтвердите пароль" required class="auth-input">
            <button type="button" onclick="register()" class="auth-button">Зарегистрироваться</button>
        </form>

        <% if ("login".equals(action)) { %>
        <div class="error-message">Неверный логин или пароль!</div>
        <% } else if ("register".equals(action)) { %>
        <div class="error-message">Пароли не совпадают!</div>
        <% } %>

        <div style="margin-top: 20px; padding-top: 20px; border-top: 1px solid #e0e0e0; text-align: center; font-size: 14px; color: #666;">
            <div>Для тестирования можете использовать:</div>
            <div style="margin-top: 5px; font-weight: 600;">Логин: test | Пароль: test</div>
        </div>
    </div>
</div>
<% } %>

<script>
    // Функция закрытия модального окна авторизации
    function closeAuthModal() {
        if (!confirm('Авторизация обязательна для использования калькулятора. Вы уверены, что хотите закрыть?')) return;
        document.getElementById('authModal').style.display = 'none';
    }

    // Функция переключения между вкладками входа и регистрации
    function switchTab(tabName) {
        document.querySelectorAll('.auth-tab').forEach(tab => tab.classList.remove('active'));
        document.querySelectorAll('.auth-form').forEach(form => form.classList.remove('active'));

        if (tabName === 'login') {
            document.getElementById('loginForm').classList.add('active');
            document.querySelector('.auth-tab:nth-child(1)').classList.add('active');
        } else {
            document.getElementById('registerForm').classList.add('active');
            document.querySelector('.auth-tab:nth-child(2)').classList.add('active');
        }
    }

    async function login(){
        const username = document.getElementById('loginUsername').value;
        const password = document.getElementById('loginPassword').value;
        const authUrl = 'http://localhost:8080/mathhub/api/users/auth?username=' +
            username + '&password=' + password;
        const userUrl = 'http://localhost:8080/mathhub/api/users?username=' + username;

        try {
            const auth = await fetch(authUrl, {
                method: 'GET',
                headers: {
                    'Accept': 'application/json'
                }
            });
            const isAuth = await auth.json();
            if (isAuth === false) window.location.reload();
            const user = await fetch(userUrl, {
                method: 'GET',
                headers: {
                    'Accept': 'application/json'
                }
            });
            const userData = await user.json();
            if (auth.ok && user.ok){
                window.location.href = 'index.jsp?action=login&username=' +
                    encodeURIComponent(username) + '&id=' + userData.id;
            }
        } catch (error) {
            window.location.reload();
        }
    }

    async function register() {
        const username = document.getElementById('regUsername').value;
        const password = document.getElementById('regPassword').value;
        const confirmPassword = document.getElementById('regConfirmPassword').value;
        const url = 'http://localhost:8080/mathhub/api/users';

        if (password === confirmPassword) {
            const userData = {
                username: username,
                password: password
            };
            console.info(url);
            try {
                const response = await fetch(url, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept': 'application/json'
                    },
                    body: JSON.stringify(userData)
                });
                const data = await response.json();
                if (response.ok && data != null) {
                    window.location.href = 'index.jsp?action=login&username=' +
                        encodeURIComponent(username) + '&id=' + data.id;
                }
            } catch (error) {
                window.location.reload();
            }
        }
    }

    // Автоматическое открытие модального окна при клике на заблокированный интерфейс
    document.addEventListener('DOMContentLoaded', function() {
        <% if (user == null) { %>
        // Открываем модальное окно при загрузке страницы
        document.getElementById('authModal').style.display = 'flex';

        // Также открываем при клике на заблокированный интерфейс
        document.querySelector('.calculator-main').addEventListener('click', function() {
            document.getElementById('authModal').style.display = 'flex';
        });

        // И при клике на сообщение о необходимости авторизации
        document.querySelector('.auth-required-message').addEventListener('click', function() {
            document.getElementById('authModal').style.display = 'flex';
        });
        <% } else { %>
        // Если пользователь авторизован, активируем функции калькулятора
        document.querySelector('.calculator-main').classList.add('authenticated');
        <% } %>
    });
</script>
</body>
</html>