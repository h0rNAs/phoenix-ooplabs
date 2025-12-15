<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MathHub - Табулированная функция</title>
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

        .tabulated-example {
            background: rgba(255, 165, 0, 0.1);
            border: 2px solid rgba(255, 165, 0, 0.3);
            border-radius: 10px;
            padding: 20px;
            margin-top: 30px;
        }

        .dark-theme .tabulated-example {
            background: rgba(255, 165, 0, 0.1);
            border-color: rgba(255, 165, 0, 0.4);
        }

        .example-title {
            font-weight: 600;
            color: var(--orange-accent);
            margin-bottom: 10px;
            font-size: 16px;
        }

        .example-table {
            width: 100%;
            border-collapse: collapse;
            margin: 15px 0;
            background: white;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }

        .dark-theme .example-table {
            background: #4a5568;
            box-shadow: 0 2px 8px rgba(0,0,0,0.3);
        }

        .example-table th {
            background: var(--orange-accent);
            color: white;
            padding: 12px;
            text-align: center;
            font-weight: 600;
        }

        .dark-theme .example-table th {
            background: var(--orange-hover);
        }

        .example-table td {
            padding: 10px;
            text-align: center;
            border-bottom: 1px solid #eee;
        }

        .dark-theme .example-table td {
            border-bottom-color: #718096;
        }

        .example-table td {
            color: #333;
        }

        .dark-theme .example-table td {
            color: #e2e8f0;
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
    </style>
</head>
<body>
<jsp:include page="header.jsp" />
<main style="max-width: 1000px; margin: 0 auto; padding: 20px;">
    <h1 style="text-align: center; margin-bottom: 10px;">
        Табулированные функции
    </h1>

    <p style="text-align: center; margin-bottom: 30px; max-width: 800px; margin-left: auto; margin-right: auto;">
        Табулированные функции позволяют задавать зависимость через набор точек (x, y).
        Это полезно для представления экспериментальных данных или функций, которые сложно описать аналитически.
    </p>

    <div class="function-types">
        <div class="function-type-card">
            <div class="function-type-icon">📍</div>
            <div class="function-type-title">Точечное задание</div>
            <div class="function-type-description">
                Создайте функцию путем указания конкретных точек:
            </div>
            <ul class="function-type-features">
                <li>Задавайте любое количество точек</li>
                <li>Первые 2 точки обязательны</li>
                <li>Автоматическая сортировка по X</li>
                <li>Визуализация всех точек на графике</li>
            </ul>
        </div>

        <div class="function-type-card">
            <div class="function-type-icon">📈</div>
            <div class="function-type-title">Линейная интерполяция</div>
            <div class="function-type-description">
                Между точками используется линейная интерполяция:
            </div>
            <ul class="function-type-features">
                <li>Плавное соединение точек</li>
                <li>Вычисление значений между точками</li>
                <li>Работа в композитных функциях</li>
                <li>Автоматическое определение диапазона</li>
            </ul>
        </div>

        <div class="function-type-card">
            <div class="function-type-icon">🔗</div>
            <div class="function-type-title">Интеграция с другими типами</div>
            <div class="function-type-description">
                Табулированные функции можно комбинировать:
            </div>
            <ul class="function-type-features">
                <li>Использовать в композитных функциях</li>
                <li>Сочетать с простыми функциями</li>
                <li>Создавать сложные зависимости</li>
                <li>Экспортировать и импортировать данные</li>
            </ul>
        </div>
    </div>

    <div class="tabulated-example">
        <div class="example-title">📋 Пример табулированной функции:</div>

        <table class="example-table">
            <thead>
            <tr>
                <th>№</th>
                <th>Координата X</th>
                <th>Координата Y</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>1</td>
                <td>-2.0</td>
                <td>4.0</td>
            </tr>
            <tr>
                <td>2</td>
                <td>-1.0</td>
                <td>1.0</td>
            </tr>
            <tr>
                <td>3</td>
                <td>0.0</td>
                <td>0.0</td>
            </tr>
            <tr>
                <td>4</td>
                <td>1.0</td>
                <td>1.0</td>
            </tr>
            <tr>
                <td>5</td>
                <td>2.0</td>
                <td>4.0</td>
            </tr>
            </tbody>
        </table>

        <div style="margin-top: 15px; font-size: 14px; color: #666;">
            <div><strong>Примечание:</strong> Это точки параболы y = x².</div>
            <div style="margin-top: 5px;">В калькуляторе между точками будет проведена линейная интерполяция.</div>
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