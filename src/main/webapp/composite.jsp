<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MathHub - Композитная функция</title>
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
            cursor: pointer;
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

        .composite-example {
            background: rgba(255, 165, 0, 0.1);
            border: 2px solid rgba(255, 165, 0, 0.3);
            border-radius: 10px;
            padding: 20px;
            margin-top: 30px;
        }

        .dark-theme .composite-example {
            background: rgba(255, 165, 0, 0.1);
            border-color: rgba(255, 165, 0, 0.4);
        }

        .example-title {
            font-weight: 600;
            color: var(--orange-accent);
            margin-bottom: 10px;
            font-size: 16px;
        }

        .example-equation {
            font-family: 'Courier New', monospace;
            font-size: 16px;
            color: #333;
            background: rgba(255, 255, 255, 0.5);
            padding: 10px;
            border-radius: 6px;
            margin: 10px 0;
        }

        .dark-theme .example-equation {
            color: white;
            background: rgba(255, 255, 255, 0.1);
        }

        .how-to-list {
            margin: 25px 0;
        }

        .how-to-item {
            display: flex;
            align-items: center;
            margin-bottom: 15px;
            padding: 15px;
            background: #f0f0f0;
            border-radius: 8px;
            transition: all 0.3s ease;
        }

        .dark-theme .how-to-item {
            background: #4a5568;
        }

        .how-to-number {
            background: var(--orange-accent);
            color: white;
            width: 30px;
            height: 30px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
            margin-right: 15px;
            flex-shrink: 0;
        }

        .how-to-text {
            flex: 1;
            color: #333;
        }

        .dark-theme .how-to-text {
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

        .composite-example div:last-child {
            color: #666;
        }

        .dark-theme .composite-example div:last-child {
            color: #a0aec0;
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp" />
<main style="max-width: 1000px; margin: 0 auto; padding: 20px;">
    <h1 style="text-align: center; margin-bottom: 10px;">
        Композитные функции
    </h1>

    <p style="text-align: center; margin-bottom: 30px; max-width: 800px; margin-left: auto; margin-right: auto;">
        Композитные функции позволяют объединять уже созданные функции для создания новых, более сложных зависимостей.
        Вы можете комбинировать простые и табулированные функции различными способами.
    </p>

    <div class="how-to-list">
        <div class="how-to-item">
            <div class="how-to-number">1</div>
            <div class="how-to-text">
                <strong>Создайте базовые функции:</strong> Сначала создайте простые или табулированные функции, которые хотите объединить.
            </div>
        </div>

        <div class="how-to-item">
            <div class="how-to-number">2</div>
            <div class="how-to-text">
                <strong>Откройте меню функций:</strong> На главной странице калькулятора нажмите кнопку меню в левом нижнем углу.
            </div>
        </div>

        <div class="how-to-item">
            <div class="how-to-number">3</div>
            <div class="how-to-text">
                <strong>Создайте композитную функцию:</strong> Нажмите "Создать новую функцию" и выберите "Композитная функция".
            </div>
        </div>

        <div class="how-to-item">
            <div class="how-to-number">4</div>
            <div class="how-to-text">
                <strong>Выберите функции и операцию:</strong> Выберите две существующие функции и операцию для их объединения.
            </div>
        </div>
    </div>

    <div class="function-types">
        <div class="function-type-card">
            <div class="function-type-icon">➕</div>
            <div class="function-type-title">Арифметические операции</div>
            <div class="function-type-description">
                Объедините функции с помощью базовых арифметических операций:
            </div>
            <ul class="function-type-features">
                <li>Сложение: h(x) = f(x) + g(x)</li>
                <li>Вычитание: h(x) = f(x) - g(x)</li>
                <li>Умножение: h(x) = f(x) * g(x)</li>
                <li>Деление: h(x) = f(x) / g(x)</li>
            </ul>
        </div>

        <div class="function-type-card">
            <div class="function-type-icon">🔄</div>
            <div class="function-type-title">Композиция функций</div>
            <div class="function-type-description">
                Создайте сложные зависимости путем вложения функций:
            </div>
            <ul class="function-type-features">
                <li>h(x) = f(g(x))</li>
                <li>Работает с любыми типами функций</li>
                <li>Автоматическая интерполяция для табулированных</li>
                <li>Многоуровневая композиция</li>
            </ul>
        </div>

        <div class="function-type-card">
            <div class="function-type-icon">🎨</div>
            <div class="function-type-title">Гибкость и визуализация</div>
            <div class="function-type-description">
                Мощные возможности для анализа и визуализации:
            </div>
            <ul class="function-type-features">
                <li>Объединение разных типов функций</li>
                <li>Плавное отображение на графике</li>
                <li>Цветовое кодирование в легенде</li>
                <li>Редактирование и удаление</li>
            </ul>
        </div>
    </div>

    <div class="composite-example">
        <div class="example-title">📊 Пример композитной функции:</div>
        <div class="example-equation">
            Если f(x) = sin(x) и g(x) = x², то:
        </div>
        <div class="example-equation">
            h(x) = f(x) + g(x) = sin(x) + x²
        </div>
        <div class="example-equation">
            или композиция: h(x) = f(g(x)) = sin(x²)
        </div>

        <div style="margin-top: 15px; font-size: 14px;">
            <div><strong>Примечание:</strong> При работе с табулированными функциями используется линейная интерполяция.</div>
            <div style="margin-top: 5px;">Это позволяет вычислять значения даже для точек, не указанных явно в таблице.</div>
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