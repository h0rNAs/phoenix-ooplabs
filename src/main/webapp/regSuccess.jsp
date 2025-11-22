<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Регистрация успешна</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 600px;
            margin: 50px auto;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .success-card {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            text-align: center;
            border-left: 4px solid #4CAF50;
        }
        .success-icon {
            font-size: 48px;
            color: #4CAF50;
            margin-bottom: 20px;
        }
        .user-info {
            background: #f9f9f9;
            padding: 15px;
            border-radius: 5px;
            margin: 20px 0;
            text-align: left;
        }
        .btn {
            display: inline-block;
            padding: 10px 20px;
            background: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            margin-top: 15px;
        }
    </style>
</head>
<body>
<div class="success-card">
    <div class="success-icon">✅</div>
    <h1>Регистрация успешна!</h1>
    <p>Добро пожаловать в наше сообщество</p>

    <div class="user-info">
        <h3>Ваши данные:</h3>
        <p><strong>Id = </strong> ${id}</p>
        <p><strong>Имя:</strong> ${username}</p>
        <p><strong>Город:</strong> ${password}</p>
    </div>

    <a href="index.jsp" class="btn">На главную</a>
    <a href="profile.jsp" class="btn">В личный кабинет</a>
</div>
</body>
</html>