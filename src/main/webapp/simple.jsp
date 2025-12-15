<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MathHub - Простая функция</title>
    <link rel="stylesheet" href="css/styles.css">
    <script src="js/script.js" defer></script>
    <style>
        .function-types {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 25px;
            margin-top: 30px;
        }

        .function-type-card {
            background: #f9f9f9;
            border: 2px solid #ddd;
            border-radius: 12px;
            padding: 25px;
            transition: all 0.3s ease;
        }

        .dark-theme .function-type-card {
            background: #4a5568;
            border-color: #718096;
            color: white;
        }

        .function-type-card:hover {
            border-color: var(--orange-accent);
            transform: translateY(-5px);
            box-shadow: 0 8px 20px rgba(255, 165, 0, 0.1);
        }

        .function-type-icon {
            font-size: 40px;
            text-align: center;
            margin-bottom: 15px;
            color: var(--orange-accent);
        }

        .function-type-title {
            font-size: 20px;
            font-weight: 600;
            margin-bottom: 10px;
            color: var(--orange-accent);
            text-align: center;
        }

        .function-type-description {
            color: #666;
            font-size: 14px;
            line-height: 1.5;
            margin-bottom: 20px;
        }

        .dark-theme .function-type-description {
            color: #a0aec0;
        }

        .function-type-features {
            list-style: none;
            padding-left: 0;
            margin: 15px 0;
        }

        .function-type-features li {
            padding: 8px 0;
            padding-left: 25px;
            position: relative;
            color: #555;
            font-size: 13px;
        }

        .dark-theme .function-type-features li {
            color: #cbd5e0;
        }

        .function-type-features li:before {
            content: "✓";
            position: absolute;
            left: 0;
            color: var(--orange-accent);
            font-weight: bold;
        }

        .simple-example {
            background: rgba(255, 165, 0, 0.1);
            border: 2px solid rgba(255, 165, 0, 0.3);
            border-radius: 10px;
            padding: 20px;
            margin-top: 30px;
        }

        .dark-theme .simple-example {
            background: rgba(255, 165, 0, 0.1);
            border-color: rgba(255, 165, 0, 0.4);
        }

        .example-title {
            font-weight: 600;
            color: var(--orange-accent);
            margin-bottom: 10px;
            font-size: 16px;
        }

        .function-list {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            gap: 15px;
            margin: 15px 0;
        }

        .function-item-example {
            background: white;
            border: 2px solid #eee;
            border-radius: 8px;
            padding: 15px;
            text-align: center;
            transition: all 0.3s ease;
        }

        .dark-theme .function-item-example {
            background: #4a5568;
            border-color: #718096;
        }

        .function-item-example:hover {
            border-color: var(--orange-accent);
            transform: translateY(-3px);
        }

        .function-equation {
            font-family: 'Courier New', monospace;
            font-size: 14px;
            color: #333;
            margin: 5px 0;
        }

        .dark-theme .function-equation {
            color: white;
        }

        .back-to-calc {
            display: inline-block;
            margin-top: 20px;
            padding: 12px 24px;
            background: var(--orange-accent);
            color: white;
            text-decoration: none;
            border-radius: 8px;
            font-weight: 600;
            transition: all 0.3s ease;
        }

        .back-to-calc:hover {
            background: var(--orange-hover);
            transform: translateY(-2px);
        }

        main h1 {
            color: var(--orange-accent);
        }

        main p {
            color: #666;
        }

        .dark-theme main p {
            color: #a0aec0;
        }

        .function-item-example div:last-child {
            color: #666;
            font-size: 12px;
        }

        .dark-theme .function-item-example div:last-child {
            color: #a0aec0;
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp" />
<main style="max-width: 1000px; margin: 0 auto; padding: 20px;">
    <h1 style="text-align: center; margin-bottom: 10px;">
        Простые функции
    </h1>

    <p style="text-align: center; margin-bottom: 30px; max-width: 800px; margin-left: auto; margin-right: auto;">
        Простые функции позволяют быстро создавать стандартные математические зависимости.
        Выберите из списка готовых функций или создайте свою на основе базовых операций.
    </p>

    <div class="function-types">
        <div class="function-type-card">
            <div class="function-type-icon">🧮</div>
            <div class="function-type-title">Базовые функции</div>
            <div class="function-type-description">
                Стандартные математические функции из школьного курса:
            </div>
            <ul class="function-type-features">
                <li>Линейные: f(x) = x</li>
                <li>Квадратичные: f(x) = x²</li>
                <li>Кубические: f(x) = x³</li>
                <li>Дробно-линейные: f(x) = 1/x</li>
            </ul>
        </div>

        <div class="function-type-card">
            <div class="function-type-icon">📐</div>
            <div class="function-type-title">Тригонометрические</div>
            <div class="function-type-description">
                Функции для работы с углами и периодическими процессами:
            </div>
            <ul class="function-type-features">
                <li>Синус: f(x) = sin(x)</li>
                <li>Косинус: f(x) = cos(x)</li>
                <li>Тангенс: f(x) = tan(x)</li>
                <li>Работа в радианах</li>
            </ul>
        </div>

        <div class="function-type-card">
            <div class="function-type-icon">📊</div>
            <div class="function-type-title">Специальные функции</div>
            <div class="function-type-description">
                Дополнительные функции для сложных вычислений:
            </div>
            <ul class="function-type-features">
                <li>Экспонента: f(x) = exp(x)</li>
                <li>Логарифм: f(x) = log(x)</li>
                <li>Корень: f(x) = √x</li>
                <li>Модуль: f(x) = |x|</li>
            </ul>
        </div>
    </div>

    <div class="simple-example">
        <div class="example-title">📋 Доступные простые функции:</div>

        <div class="function-list">
            <div class="function-item-example">
                <div class="function-equation">f(x) = x</div>
                <div>Линейная</div>
            </div>
            <div class="function-item-example">
                <div class="function-equation">f(x) = x²</div>
                <div>Квадратичная</div>
            </div>
            <div class="function-item-example">
                <div class="function-equation">f(x) = sin(x)</div>
                <div>Синус</div>
            </div>
            <div class="function-item-example">
                <div class="function-equation">f(x) = cos(x)</div>
                <div>Косинус</div>
            </div>
            <div class="function-item-example">
                <div class="function-equation">f(x) = exp(x)</div>
                <div>Экспонента</div>
            </div>
            <div class="function-item-example">
                <div class="function-equation">f(x) = log(x)</div>
                <div>Логарифм</div>
            </div>
            <div class="function-item-example">
                <div class="function-equation">f(x) = √x</div>
                <div>Корень</div>
            </div>
            <div class="function-item-example">
                <div class="function-equation">f(x) = |x|</div>
                <div>Модуль</div>
            </div>
        </div>

        <div style="margin-top: 15px; font-size: 14px; color: #666;">
            <div><strong>Примечание:</strong> Все функции можно использовать в композитных функциях.</div>
            <div style="margin-top: 5px;">Просто создайте простую функцию, затем используйте её при создании композитной.</div>
        </div>
    </div>

    <div style="text-align: center; margin-top: 40px;">
        <a href="index.jsp" class="back-to-calc">
            ← Перейти к калькулятору
        </a>
    </div>
</main>
</body>
</html>