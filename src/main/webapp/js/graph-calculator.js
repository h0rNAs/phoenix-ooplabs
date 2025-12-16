class GraphingCalculator {
    constructor(canvas) {
        this.canvas = canvas;
        this.ctx = canvas.getContext('2d');
        this.functions = [];
        this.colors = ['#FF6B6B', '#4ECDC4', '#45B7D1', '#96CEB4', '#FFEAA7', '#DDA0DD', '#98D8C8', '#F7DC6F'];

        // Параметры системы координат
        this.scale = 50;
        this.offsetX = 0;
        this.offsetY = 0;
        this.isDragging = false;
        this.lastX = 0;
        this.lastY = 0;

        // Оптимизация отрисовки
        this.animationFrameId = null;
        this.needsRedraw = true;

        this.init();
    }

    init() {
        this.setupEventListeners();
        this.resize();
        this.createLegend();
        this.startAnimation();
    }

    setupEventListeners() {
        // Масштабирование колесом мыши
        this.canvas.addEventListener('wheel', (e) => {
            e.preventDefault();
            const rect = this.canvas.getBoundingClientRect();
            const mouseX = e.clientX - rect.left;
            const mouseY = e.clientY - rect.top;

            const worldX = this.toWorldX(mouseX);
            const worldY = this.toWorldY(mouseY);

            const zoomFactor = e.deltaY > 0 ? 0.8 : 1.2;
            const newScale = this.scale * zoomFactor;

            if (newScale < 5 || newScale > 1000) return;

            this.scale = newScale;

            const newMouseX = this.toScreenX(worldX);
            const newMouseY = this.toScreenY(worldY);

            this.offsetX += mouseX - newMouseX;
            this.offsetY += mouseY - newMouseY;

            this.needsRedraw = true;
        });

        // Перетаскивание
        this.canvas.addEventListener('mousedown', (e) => {
            this.isDragging = true;
            this.lastX = e.clientX;
            this.lastY = e.clientY;
            this.canvas.style.cursor = 'grabbing';
        });

        this.canvas.addEventListener('mousemove', (e) => {
            const rect = this.canvas.getBoundingClientRect();
            const mouseX = e.clientX - rect.left;
            const mouseY = e.clientY - rect.top;

            const worldX = this.toWorldX(mouseX);
            const worldY = this.toWorldY(mouseY);

            let displayX = this.formatNumberForDisplay(worldX);
            let displayY = this.formatNumberForDisplay(worldY);

            const display = document.getElementById('coordinatesDisplay');
            if (display) {
                display.textContent = `x: ${displayX}, y: ${displayY}`;
            }

            if (this.isDragging) {
                const deltaX = e.clientX - this.lastX;
                const deltaY = e.clientY - this.lastY;

                this.offsetX += deltaX;
                this.offsetY += deltaY;

                this.lastX = e.clientX;
                this.lastY = e.clientY;

                this.needsRedraw = true;
            }
        });

        this.canvas.addEventListener('mouseup', () => {
            this.isDragging = false;
            this.canvas.style.cursor = 'grab';
        });

        this.canvas.addEventListener('mouseleave', () => {
            this.isDragging = false;
            this.canvas.style.cursor = 'grab';
        });

        // Сброс вида
        const resetBtn = document.getElementById('resetViewBtn');
        if (resetBtn) {
            resetBtn.addEventListener('click', () => {
                this.scale = 50;
                this.offsetX = 0;
                this.offsetY = 0;
                this.needsRedraw = true;
            });
        }

        // Обработка изменения размера окна
        window.addEventListener('resize', () => {
            this.resize();
            this.needsRedraw = true;
        });
    }

    startAnimation() {
        const animate = () => {
            if (this.needsRedraw) {
                this.draw();
                this.needsRedraw = false;
            }
            this.animationFrameId = requestAnimationFrame(animate);
        };
        animate();
    }

    resize() {
        this.canvas.width = this.canvas.parentElement.clientWidth;
        this.canvas.height = this.canvas.parentElement.clientHeight;
        this.needsRedraw = true;
    }

    toWorldX(screenX) {
        return (screenX - this.canvas.width / 2 - this.offsetX) / this.scale;
    }

    toWorldY(screenY) {
        return -(screenY - this.canvas.height / 2 - this.offsetY) / this.scale;
    }

    toScreenX(worldX) {
        return worldX * this.scale + this.canvas.width / 2 + this.offsetX;
    }

    toScreenY(worldY) {
        return -worldY * this.scale + this.canvas.height / 2 + this.offsetY;
    }

    formatNumberForDisplay(num) {
        const absNum = Math.abs(num);

        if (absNum >= 10000 || (absNum > 0 && absNum < 0.001)) {
            return num.toExponential(2);
        }

        if (absNum >= 1000) {
            return Math.round(num).toString();
        }

        if (absNum >= 10) {
            return num.toFixed(1);
        }

        if (absNum >= 1) {
            return num.toFixed(2);
        }

        if (absNum >= 0.01) {
            return num.toFixed(3);
        }

        return num.toFixed(4);
    }

    draw() {
        this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
        this.drawGrid();
        this.drawAxes();
        this.drawCoordinateLabels();
        this.drawFunctions();
    }

    drawGrid() {
        const width = this.canvas.width;
        const height = this.canvas.height;
        const centerX = width / 2 + this.offsetX;
        const centerY = height / 2 + this.offsetY;

        const isDarkTheme = document.body.classList.contains('dark-theme');
        const gridColor = isDarkTheme ? '#4a5568' : '#e0e0e0';
        this.ctx.strokeStyle = gridColor;
        this.ctx.lineWidth = 1;

        let gridStep = this.scale;

        if (this.scale < 20) gridStep = this.scale * 5;
        else if (this.scale < 10) gridStep = this.scale * 10;
        else if (this.scale < 5) gridStep = this.scale * 20;

        if (this.scale > 100) gridStep = this.scale / 2;

        const startX = ((centerX % gridStep) + gridStep) % gridStep;
        const startY = ((centerY % gridStep) + gridStep) % gridStep;

        // Вертикальные линии
        this.ctx.beginPath();
        for (let x = startX; x < width; x += gridStep) {
            this.ctx.moveTo(x, 0);
            this.ctx.lineTo(x, height);
        }
        this.ctx.stroke();

        // Горизонтальные линии
        this.ctx.beginPath();
        for (let y = startY; y < height; y += gridStep) {
            this.ctx.moveTo(0, y);
            this.ctx.lineTo(width, y);
        }
        this.ctx.stroke();
    }

    drawAxes() {
        const width = this.canvas.width;
        const height = this.canvas.height;
        const centerX = width / 2 + this.offsetX;
        const centerY = height / 2 + this.offsetY;

        this.ctx.strokeStyle = '#FFA500';
        this.ctx.lineWidth = 2;

        // Ось Y
        this.ctx.beginPath();
        this.ctx.moveTo(centerX, 0);
        this.ctx.lineTo(centerX, height);
        this.ctx.stroke();

        // Ось X
        this.ctx.beginPath();
        this.ctx.moveTo(0, centerY);
        this.ctx.lineTo(width, centerY);
        this.ctx.stroke();

        // Стрелки на осях
        this.ctx.fillStyle = '#FFA500';

        // Стрелка на оси X (вправо)
        this.ctx.beginPath();
        this.ctx.moveTo(width - 10, centerY - 5);
        this.ctx.lineTo(width, centerY);
        this.ctx.lineTo(width - 10, centerY + 5);
        this.ctx.fill();

        // Стрелка на оси Y (вверх)
        this.ctx.beginPath();
        this.ctx.moveTo(centerX - 5, 10);
        this.ctx.lineTo(centerX, 0);
        this.ctx.lineTo(centerX + 5, 10);
        this.ctx.fill();
    }

    drawCoordinateLabels() {
        const width = this.canvas.width;
        const height = this.canvas.height;
        const centerX = width / 2 + this.offsetX;
        const centerY = height / 2 + this.offsetY;

        const isDarkTheme = document.body.classList.contains('dark-theme');
        const textColor = isDarkTheme ? 'white' : 'black';
        const labelColor = isDarkTheme ? '#a0aec0' : '#666';

        this.ctx.fillStyle = textColor;
        this.ctx.font = '12px Arial';

        // Подписи осей
        this.ctx.fillText('x', width - 15, centerY - 10);
        this.ctx.fillText('y', centerX + 10, 15);

        // Определяем шаг для отображения чисел
        let labelStep = this.scale;

        // Увеличиваем шаг при сильном уменьшении
        if (this.scale < 20) labelStep = this.scale * 5;
        else if (this.scale < 10) labelStep = this.scale * 10;
        else if (this.scale < 5) labelStep = this.scale * 20;

        // Уменьшаем шаг при сильном увеличении
        if (this.scale > 100) labelStep = this.scale / 2;
        if (this.scale > 200) labelStep = this.scale / 5;

        // Отображаем числа на оси X
        this.ctx.fillStyle = labelColor;
        this.ctx.font = '11px Arial';
        this.ctx.textAlign = 'center';
        this.ctx.textBaseline = 'top';

        const startX = Math.floor((-centerX) / labelStep) * labelStep;
        const endX = Math.ceil((width - centerX) / labelStep) * labelStep;

        for (let x = startX; x <= endX; x += labelStep) {
            if (Math.abs(x) < 0.001) continue; // Пропускаем 0 (он в центре)

            const screenX = centerX + x;
            const worldX = x / this.scale;

            if (screenX >= 20 && screenX <= width - 20) {
                const label = this.formatCoordinate(worldX);
                this.ctx.fillText(label, screenX, centerY + 8);

                // Маленькая отметка на оси
                this.ctx.fillStyle = '#FFA500';
                this.ctx.fillRect(screenX - 1, centerY - 3, 2, 6);
                this.ctx.fillStyle = labelColor;
            }
        }

        // Отображаем числа на оси Y
        this.ctx.textAlign = 'right';
        this.ctx.textBaseline = 'middle';

        const startY = Math.floor((-centerY) / labelStep) * labelStep;
        const endY = Math.ceil((height - centerY) / labelStep) * labelStep;

        for (let y = startY; y <= endY; y += labelStep) {
            if (Math.abs(y) < 0.001) continue; // Пропускаем 0 (он в центре)

            const screenY = centerY + y;
            const worldY = -y / this.scale;

            if (screenY >= 20 && screenY <= height - 20) {
                const label = this.formatCoordinate(worldY);
                this.ctx.fillText(label, centerX - 8, screenY);

                // Маленькая отметка на оси
                this.ctx.fillStyle = '#FFA500';
                this.ctx.fillRect(centerX - 3, screenY - 1, 6, 2);
                this.ctx.fillStyle = labelColor;
            }
        }

        // Ноль в центре
        this.ctx.fillStyle = '#FFA500';
        this.ctx.font = '12px Arial';
        this.ctx.textAlign = 'right';
        this.ctx.textBaseline = 'top';
        this.ctx.fillText('0', centerX - 8, centerY + 8);
    }

    formatCoordinate(num) {
        const absNum = Math.abs(num);

        if (absNum >= 1000) {
            return Math.round(num).toString();
        }

        if (absNum >= 100) {
            return num.toFixed(0);
        }

        if (absNum >= 10) {
            return num.toFixed(1);
        }

        if (absNum >= 1) {
            return num.toFixed(1);
        }

        if (absNum >= 0.01) {
            return num.toFixed(2);
        }

        if (absNum >= 0.001) {
            return num.toFixed(3);
        }

        if (absNum > 0) {
            return num.toExponential(1);
        }

        return '0';
    }

    drawFunctions() {
        this.functions.forEach((func, index) => {
            const color = this.colors[index % this.colors.length];

            if (func.type === 'SIMPLE') {
                this.drawSimpleFunction(func, color);
            } else if (func.type === 'TABULATED') {
                this.drawTabulatedFunction(func, color);
            } else if (func.type === 'COMPOSITE') {
                this.drawCompositeFunction(func, color);
            }
        });
    }

    drawSimpleFunction(func, color) {
        // Используем интервал из настроек функции, если он есть
        const from = func.definition ? func.definition.xFrom : -10;
        const to = func.definition ? func.definition.xTo : 10;
        const step = Math.max(1, 10 / this.scale);

        // Преобразуем мировые координаты в экранные
        const screenFrom = this.toScreenX(from);
        const screenTo = this.toScreenX(to);

        // Ограничиваем область отрисовки видимой частью экрана
        const visibleWidth = this.canvas.width;
        const drawFrom = Math.max(0, screenFrom);
        const drawTo = Math.min(visibleWidth, screenTo);

        this.ctx.strokeStyle = color;
        this.ctx.lineWidth = 2;
        this.ctx.beginPath();

        let isFirstPoint = true;
        let lastValidY = null;

        for (let screenX = drawFrom; screenX <= drawTo; screenX += step) {
            const x = this.toWorldX(screenX);

            // Проверяем, что точка находится в пределах интервала функции
            if (x < from || x > to) continue;

            let y;

            try {
                y = this.evaluateExpression(func.definition.function, x);

                if (isFinite(y) && !isNaN(y)) {
                    const screenY = this.toScreenY(y);

                    if (lastValidY !== null && Math.abs(y - lastValidY) > 100) {
                        isFirstPoint = true;
                    }

                    if (isFirstPoint) {
                        this.ctx.moveTo(screenX, screenY);
                        isFirstPoint = false;
                    } else {
                        this.ctx.lineTo(screenX, screenY);
                    }
                    lastValidY = y;
                } else {
                    isFirstPoint = true;
                    lastValidY = null;
                }
            } catch (e) {
                isFirstPoint = true;
                lastValidY = null;
            }
        }

        this.ctx.stroke();
    }

    drawTabulatedFunction(func, color) {
        const points = func.definition.edits;
        if (!points || points === 0) return;

        this.ctx.strokeStyle = color;
        this.ctx.lineWidth = 2;
        this.ctx.beginPath();

        let isFirstPoint = true;
        points.forEach((point, index) => {
            const screenX = this.toScreenX(point.x);
            const screenY = this.toScreenY(point.y);

            if (isFirstPoint) {
                this.ctx.moveTo(screenX, screenY);
                isFirstPoint = false;
            } else {
                this.ctx.lineTo(screenX, screenY);
            }
        });

        this.ctx.stroke();

        // Рисуем точки
        this.ctx.fillStyle = color;
        points.forEach(point => {
            const screenX = this.toScreenX(point.x);
            const screenY = this.toScreenY(point.y);

            this.ctx.beginPath();
            this.ctx.arc(screenX, screenY, 3, 0, 2 * Math.PI);
            this.ctx.fill();
        });
    }

    drawCompositeFunction(func, color) {
        if (!func.func1 || !func.func2) return;

        // Определяем интервал для отрисовки на основе видимой области
        const visibleFrom = this.toWorldX(0);
        const visibleTo = this.toWorldX(this.canvas.width);

        const step = Math.max(1, 10 / this.scale);

        this.ctx.strokeStyle = color;
        this.ctx.lineWidth = 2;
        this.ctx.beginPath();

        let isFirstPoint = true;
        let lastValidY = null;

        for (let screenX = 0; screenX < this.canvas.width; screenX += step) {
            const x = this.toWorldX(screenX);
            let y;

            try {
                y = this.evaluateCompositeFunction(func, x);

                if (isFinite(y) && !isNaN(y)) {
                    const screenY = this.toScreenY(y);

                    if (lastValidY !== null && Math.abs(y - lastValidY) > 100) {
                        isFirstPoint = true;
                    }

                    if (isFirstPoint) {
                        this.ctx.moveTo(screenX, screenY);
                        isFirstPoint = false;
                    } else {
                        this.ctx.lineTo(screenX, screenY);
                    }
                    lastValidY = y;
                } else {
                    isFirstPoint = true;
                    lastValidY = null;
                }
            } catch (e) {
                isFirstPoint = true;
                lastValidY = null;
            }
        }

        this.ctx.stroke();

        // Если это композиция с табулированной функцией, рисуем также точки
        if (func.func1.type === 'TABULATED' || func.func2.type === 'TABULATED') {
            this.drawCompositeFunctionPoints(func, color);
        }
    }

    drawCompositeFunctionPoints(func, color) {
        // Рисуем точки для табулированных функций в композиции
        this.ctx.fillStyle = color;
        this.ctx.strokeStyle = color;
        this.ctx.lineWidth = 1;

        // Рисуем точки для визуализации
        for (let screenX = 0; screenX < this.canvas.width; screenX += 20) {
            const x = this.toWorldX(screenX);
            try {
                const y = this.evaluateCompositeFunction(func, x);
                if (isFinite(y) && !isNaN(y)) {
                    const screenY = this.toScreenY(y);
                    this.ctx.beginPath();
                    this.ctx.arc(screenX, screenY, 2, 0, 2 * Math.PI);
                    this.ctx.fill();
                }
            } catch (e) {
                continue;
            }
        }
    }

    evaluateCompositeFunction(func, x) {
        const func1 = func.func1;
        const func2 = func.func2;
        const operation = func.definition.operation;

        let y1, y2;
        // Вычисляем значение первой функции
        if (func1.type === 'SIMPLE') {
            y1 = this.evaluateExpression(func1.definition.function, x);
        } else if (func1.type === 'TABULATED') {
            y1 = this.interpolateTabulatedFunction(func1, x);
        } else if (func1.type === 'COMPOSITE') {
            y1 = this.evaluateCompositeFunction(func1, x);
        }

        // Вычисляем значение второй функции
        if (operation === 'compose') {
            // Для композиции сначала вычисляем g(x)
            let gx;
            if (func2.type === 'SIMPLE') {
                gx = this.evaluateExpression(func2.definition.function, x);
            } else if (func2.type === 'TABULATED') {
                gx = this.interpolateTabulatedFunction(func2, x);
            } else if (func2.type === 'COMPOSITE') {
                gx = this.evaluateCompositeFunction(func2, x);
            }

            // Затем вычисляем f(g(x))
            if (isFinite(gx) && !isNaN(gx)) {
                if (func1.type === 'SIMPLE') {
                    return this.evaluateExpression(func1.definition.function, gx);
                } else if (func1.type === 'TABULATED') {
                    return this.interpolateTabulatedFunction(func1, gx);
                } else if (func1.type === 'COMPOSITE') {
                    return this.evaluateCompositeFunction(func1, gx);
                }
            }
            return NaN;
        } else {
            // Для арифметических операций вычисляем обе функции в точке x
            if (func2.type === 'SIMPLE') {
                y2 = this.evaluateExpression(func2.definition.function, x);
            } else if (func2.type === 'TABULATED') {
                y2 = this.interpolateTabulatedFunction(func2, x);
            } else if (func2.type === 'COMPOSITE') {
                y2 = this.evaluateCompositeFunction(func2, x);
            }

            // Выполняем операцию
            if (isFinite(y1) && !isNaN(y1) && isFinite(y2) && !isNaN(y2)) {
                switch(operation) {
                    case 'add':
                        return y1 + y2;
                    case 'subtract':
                        return y1 - y2;
                    case 'multiply':
                        return y1 * y2;
                    case 'divide':
                        return y2 !== 0 ? y1 / y2 : NaN;
                    default:
                        return NaN;
                }
            }
            return NaN;
        }
    }

    interpolateTabulatedFunction(func, x) {
        const points = func.definition.edits;
        if (!points || points.length === 0) return NaN;

        // Если x за пределами диапазона точек, возвращаем NaN
        if (x < points[0].x || x > points[points.length - 1].x) {
            return NaN;
        }

        // Линейная интерполяция
        for (let i = 0; i < points.length - 1; i++) {
            const p1 = points[i];
            const p2 = points[i + 1];

            if (x >= p1.x && x <= p2.x) {
                // Линейная интерполяция: y = y1 + (y2 - y1) * (x - x1) / (x2 - x1)
                const t = (x - p1.x) / (p2.x - p1.x);
                return p1.y + (p2.y - p1.y) * t;
            }
        }

        return NaN;
    }

    evaluateExpression(expression, x) {
        let expr = expression
            .replace(/sin/g, 'Math.sin(x)')
            .replace(/cos/g, 'Math.cos(x)')
            .replace(/tan/g, 'Math.tan(x)')
            .replace(/exp/g, 'Math.exp(x)')
            .replace(/log/g, 'Math.log(x)')
            .replace(/sqrt/g, 'Math.sqrt(x)')
            .replace(/abs/g, 'Math.abs(x)')
            .replace(/\(x\)/g, `(${x})`)
            .replace(/\^/g, '**')
            .replace(/1\/x/g, '1 / x')
        try {
            return eval(expr);
        } catch (e) {
            return NaN;
        }
    }

    addFunction(func) {
        this.functions.push(func);
        this.needsRedraw = true;
        this.updateLegend();
    }

    removeFunction(funcId) {
        this.functions = this.functions.filter(f => f.uiId !== funcId);
        this.needsRedraw = true;
        this.updateLegend();
    }

    createLegend() {
        // Удаляем старую легенду если есть
        const oldLegend = document.querySelector('.graph-legend');
        if (oldLegend) {
            oldLegend.remove();
        }

        // Создаем новую легенду
        const legend = document.createElement('div');
        legend.className = 'graph-legend';

        const title = document.createElement('div');
        title.className = 'legend-title';
        title.textContent = 'Функции';
        legend.appendChild(title);

        const list = document.createElement('div');
        list.id = 'legend-list';
        legend.appendChild(list);

        document.querySelector('.graph-panel').appendChild(legend);

        this.updateLegend();
    }

    updateLegend() {
        const list = document.getElementById('legend-list');
        if (!list) return;

        list.innerHTML = '';

        if (this.functions.length === 0) {
            const empty = document.createElement('div');
            empty.className = 'legend-empty';
            empty.textContent = 'Нет функций';
            list.appendChild(empty);
            return;
        }

        this.functions.forEach((func, index) => {
            const color = this.colors[index % this.colors.length];

            const item = document.createElement('div');
            item.className = 'legend-item';

            const itemContent = document.createElement('div');
            itemContent.className = 'legend-item-content';

            const colorBox = document.createElement('div');
            colorBox.className = 'legend-color';
            colorBox.style.backgroundColor = color;

            const name = document.createElement('div');
            name.className = 'legend-name';

            let displayName = func.name || `Функция ${index + 1}`;
            if (func.type === 'composite') {
                displayName += ' (композитная)';
            } else if (func.type === 'tabulated' && func.isConverted) {
                displayName += ' (конвертированная)';
            }

            name.textContent = displayName;
            name.title = displayName;

            const actions = document.createElement('div');
            actions.className = 'legend-actions';

            const downloadBtn = document.createElement('button');
            downloadBtn.className = 'legend-download-btn';
            downloadBtn.title = 'Скачать функцию';
            downloadBtn.innerHTML = '<img src="images/download-icon.svg" alt="Скачать">';
            downloadBtn.onclick = (e) => {
                e.stopPropagation(); // Предотвращаем срабатывание клика на элементе
                window.downloadFunction(func.id);
            };

            itemContent.appendChild(colorBox);
            itemContent.appendChild(name);
            actions.appendChild(downloadBtn);

            item.appendChild(itemContent);
            item.appendChild(actions);

            // Добавляем обработчик клика для выделения функции
            item.addEventListener('click', () => {
                // Можно добавить функциональность выделения функции на графике
                item.style.backgroundColor = 'rgba(255, 165, 0, 0.1)';
                setTimeout(() => {
                    item.style.backgroundColor = '';
                }, 1000);
            });

            list.appendChild(item);
        });
    }
}

// Инициализация калькулятора при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    const canvas = document.getElementById('graphCanvas');
    if (canvas) {
        const calculator = new GraphingCalculator(canvas);
        window.calculator = calculator;

        // Обновляем легенду при изменении темы
        const themeToggle = document.getElementById('theme-toggle');
        if (themeToggle) {
            themeToggle.addEventListener('click', () => {
                setTimeout(() => {
                    calculator.needsRedraw = true;
                    calculator.updateLegend();
                }, 100);
            });
        }

        // Следим за изменениями класса темы на body
        const observer = new MutationObserver((mutations) => {
            mutations.forEach((mutation) => {
                if (mutation.attributeName === 'class') {
                    setTimeout(() => {
                        calculator.needsRedraw = true;
                        calculator.updateLegend();
                    }, 100);
                }
            });
        });

        observer.observe(document.body, { attributes: true });
    }
});