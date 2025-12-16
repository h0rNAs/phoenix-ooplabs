// Глобальная переменная для хранения точек табулированной функции
window.currentTabulatedPoints = [];

// Глобальная переменка для хранения доступных функций
window.availableFunctions = [];

// Глобальная переменная для хранения информации о конвертации
window.conversionCache = {};

let userId = getCookie("userId");

// Управление функциями
document.addEventListener('DOMContentLoaded', async function() {
    const menuBtn = document.getElementById('menuBtn');
    const closeMenu = document.getElementById('closeMenu');
    const functionsMenu = document.getElementById('functionsMenu');
    const addFunctionBtn = document.getElementById('addFunctionBtn');
    const typeSelector = document.getElementById('typeSelector');
    const typeOptions = document.querySelectorAll('.type-option');
    const resetViewBtn = document.getElementById('resetViewBtn');
    const loadBtn = document.getElementById('loadBtn');

    await loadAllFunctionsFromDB();

    if (menuBtn && functionsMenu) {
        menuBtn.addEventListener('click', () => {
            functionsMenu.classList.add('active');
            // Обновляем список доступных функций при открытии меню
            updateAvailableFunctions();
        });
    }

    if (closeMenu && functionsMenu) {
        closeMenu.addEventListener('click', () => {
            functionsMenu.classList.remove('active');
        });
    }

    if (addFunctionBtn && typeSelector) {
        addFunctionBtn.addEventListener('click', () => {
            typeSelector.style.display = typeSelector.style.display === 'none' ? 'block' : 'block';
        });
    }

    // Обработка выбора типа функции
    typeOptions.forEach(option => {
        option.addEventListener('click', function() {
            const type = this.getAttribute('data-type');
            createFunctionEditor(type);
            if (typeSelector) {
                typeSelector.style.display = 'none';
            }
        });
    });

    if (loadBtn) {
        loadBtn.addEventListener('click', () => {
            const userLoggedIn = document.querySelector('.user-name') !== null;
            if (userLoggedIn) {
                // Создаем невидимый input для выбора файла
                const fileInput = document.createElement('input');
                fileInput.type = 'file';
                fileInput.accept = '.json';
                fileInput.style.display = 'none';

                fileInput.addEventListener('change', function(e) {
                    if (e.target.files.length > 0) {
                        const file = e.target.files[0];
                        loadFunctionFromFile(file);
                    }
                    // Удаляем input после использования
                    if (fileInput.parentNode) {
                        fileInput.parentNode.removeChild(fileInput);
                    }
                });

                document.body.appendChild(fileInput);
                fileInput.click();
            } else {
                alert('Для загрузки необходимо авторизоваться');
            }
        });
    }

    if (resetViewBtn) {
        resetViewBtn.addEventListener('click', () => {
            if (window.calculator) {
                window.calculator.scale = 50;
                window.calculator.offsetX = 0;
                window.calculator.offsetY = 0;
                window.calculator.needsRedraw = true;
            }
        });
    }
});

async function loadAllFunctionsFromDB(){
    const url = 'http://localhost:8080/mathhub/api/functions?userId=' + userId;
    try {
        const responses = await fetch(url, {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            }
        });
        const datas = await responses.json();
        if (window.calculator) {
            datas.forEach(response => {
                if (response.type === 'SIMPLE'){
                    createFunctionEditor(response.type, response.id);
                    loadSimpleFunction(response.id, response);
                }
                else if (response.type === 'TABULATED') {
                    createFunctionEditor(response.type, response.id);
                    loadTabulatedFunction(response.id, response);
                }
            })
            datas.forEach(response => {
                if (response.type === 'COMPOSITE'){
                    updateAvailableFunctions();
                    const func1 = window.availableFunctions.find(f => f.id === response.definition.id1);
                    const func2 = window.availableFunctions.find(f => f.id === response.definition.id2);
                    console.info(func1, func2);
                    if (!func1 || !func2) {
                        deleteFunction(response.id, response.id, true);
                        return;
                    }
                    createFunctionEditor(response.type, response.id);
                    response.func1 = func1
                    response.func2 = func2
                    loadCompositeFunction(response.id, response);
                }
            })
            updateAvailableFunctions();
        }
    } catch (error) {
        console.info('Ошибка');
        console.error(error);
    }
}

// Обновление списка доступных функций
function updateAvailableFunctions() {
    if (!window.calculator) return;

    // Просто показываем все сохраненные функции, кроме тех, что в режиме редактирования
    window.availableFunctions = window.calculator.functions.filter(func =>
        document.getElementById(func.id) === null  // Не в режиме редактирования
    );

    /*console.log('Доступных функций для выбора:', window.availableFunctions.length);
    window.availableFunctions.forEach((func, i) => {
        console.log(`${i + 1}. ${func.name} (${func.type})`);
    });*/
}

// Создание редактора функции
function createFunctionEditor(type, functionId='-1') {
    let id = functionId;
    if (functionId === '-1') id = getRandomId();
    const functionsList = document.getElementById('functionsList');

    if (!functionsList) return;

    let editorHtml = '';

    if (type === 'SIMPLE') {
        editorHtml = `
            <div class="function-editor" id="${id}">
                <div class="function-header">
                    <span>Простая функция</span>
                    <div class="function-controls">
                        <button class="function-edit-btn" onclick="saveSimpleFunction('${id}', '${functionId}')" title="Сохранить">
                            <img src="images/choose-icon.svg" alt="Сохранить">
                        </button>
                        <button class="function-delete-btn" onclick="cancelEdit('${id}')" title="Отмена">
                            <img src="images/delete-icon.svg" alt="Отмена">
                        </button>
                    </div>
                </div>
                <div class="function-content">
                    <input type="text" 
                           id="${id}_name" 
                           class="function-name-input" 
                           placeholder="Введите название функции"
                           value="Простая функция">
                    
                    <div class="function-select-container">
                        <select class="simple-function-select" id="${id}_select">
                            <option value="x">f(x) = x</option>
                            <option value="x^2">f(x) = x²</option>
                            <option value="x^3">f(x) = x³</option>
                            <option value="sin(x)">f(x) = sin(x)</option>
                            <option value="cos(x)">f(x) = cos(x)</option>
                            <option value="tan(x)">f(x) = tan(x)</option>
                            <option value="exp(x)">f(x) = exp(x)</option>
                            <option value="log(x)">f(x) = log(x)</option>
                            <option value="sqrt(x)">f(x) = √x</option>
                            <option value="1/x">f(x) = 1/x</option>
                            <option value="abs(x)">f(x) = |x|</option>
                        </select>
                    </div>
                    
                    <div class="interval-settings" style="margin-top: 20px;">
                        <div style="margin-bottom: 15px;">
                            <label style="display: block; margin-bottom: 8px; font-weight: 600; color: var(--orange-accent);">
                                Настройки интервала:
                            </label>
                            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 12px; margin-bottom: 12px;">
                                <div>
                                    <label style="display: block; margin-bottom: 5px; font-size: 13px; color: #666;">От:</label>
                                    <input type="number" 
                                           id="${id}_from" 
                                           class="interval-input"
                                           value="-10" 
                                           step="0.5"
                                           style="width: 100%; padding: 10px; border: 2px solid #ddd; border-radius: 6px;">
                                </div>
                                <div>
                                    <label style="display: block; margin-bottom: 5px; font-size: 13px; color: #666;">До:</label>
                                    <input type="number" 
                                           id="${id}_to" 
                                           class="interval-input"
                                           value="10" 
                                           step="0.5"
                                           style="width: 100%; padding: 10px; border: 2px solid #ddd; border-radius: 6px;">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `;
    } else if (type === 'TABULATED') {
        currentTabulatedPoints = [
            {x: null, y: null},
            {x: null, y: null}
        ];

        editorHtml = `
            <div class="function-editor" id="${id}">
                <div class="function-header">
                    <span>Табулированная функция</span>
                    <div class="function-controls">
                        <button class="function-edit-btn" onclick="saveTabulatedFunction('${id}', '${functionId}')" title="Сохранить">
                            <img src="images/choose-icon.svg" alt="Сохранить">
                        </button>
                        <button class="function-delete-btn" onclick="cancelEdit('${id}')" title="Отмена">
                            <img src="images/delete-icon.svg" alt="Отмена">
                        </button>
                    </div>
                </div>
                <div class="function-content">
                    <input type="text" id="${id}_name" 
                           class="function-name-input"
                           placeholder="Введите название функции" 
                           value="Табулированная функция">
                    
                    <div class="points-counter">
                        <span id="${id}_filledCount" class="points-count">Заполнено: 0 точек</span>
                        <span class="points-requirement">Минимум: 2 точки</span>
                    </div>
                    
                    <div style="display: flex; gap: 12px; margin-bottom: 20px;">
                        <button type="button" class="function-action-btn add-point-btn" onclick="addNewPoint('${id}')">
                            Добавить точку
                        </button>
                        <button type="button" class="function-action-btn sort-points-btn" onclick="sortPoints('${id}')">
                            Сортировать
                        </button>
                    </div>
                    
                    <div class="tabulated-table-container">
                        <table class="tabulated-table">
                            <thead>
                                <tr>
                                    <th>№</th>
                                    <th>Координата X</th>
                                    <th>Координата Y</th>
                                    <th>Действие</th>
                                </tr>
                            </thead>
                            <tbody id="${id}_points_body">
                            </tbody>
                        </table>
                    </div>
                    
                    <div class="function-info">
                        <div class="function-info-title">Как работать:</div>
                        <div>Заполните значения x и y для минимум 2 точек</div>
                        <div style="margin-top: 8px; font-size: 13px;">Первые 2 точки - обязательные для заполнения</div>
                    </div>
                </div>
            </div>
        `;

        functionsList.insertAdjacentHTML('afterbegin', editorHtml);

        // Для табулированной функции сразу обновляем таблицу
        updatePointsTable(id);
        updateFilledCount(id);

        return; // Выходим, так как уже вставили HTML
    } else if (type === 'COMPOSITE') {
        editorHtml = createCompositeFunctionEditor(id, -1);
    }

    functionsList.insertAdjacentHTML('afterbegin', editorHtml);

    if (type === 'COMPOSITE') {
        // Инициализация селектов для композитной функции
        initializeCompositeFunctionSelects(id);
    }
}

// Создание редактора композитной функции
function createCompositeFunctionEditor(id, functionId) {
    return `
        <div class="function-editor" id="${id}">
            <div class="function-header">
                <span>Оперируемая функция</span>
                <div class="function-controls">
                    <button class="function-edit-btn" onclick="saveCompositeFunction('${id}', '${functionId}')" title="Сохранить">
                        <img src="images/choose-icon.svg" alt="Сохранить">
                    </button>
                    <button class="function-delete-btn" onclick="cancelEdit('${id}')" title="Отмена">
                        <img src="images/delete-icon.svg" alt="Отмена">
                    </button>
                </div>
            </div>
            <div class="function-content">
                <input type="text" id="${id}_name" 
                       class="function-name-input"
                       placeholder="Введите название функции" 
                       value="Оперируемая функция">
                
                <div class="composite-controls" style="margin-bottom: 20px;">
                    <div style="margin-bottom: 15px;">
                        <label style="display: block; margin-bottom: 8px; font-weight: 600; color: var(--orange-accent);">
                            Выберите первую функцию (f):
                        </label>
                        <select class="function-select" id="${id}_func1" style="width: 100%; padding: 12px; border-radius: 6px; border: 2px solid #ddd;">
                            <option value="">-- Выберите функцию --</option>
                        </select>
                    </div>
                    
                    <div style="margin-bottom: 15px;">
                        <label style="display: block; margin-bottom: 8px; font-weight: 600; color: var(--orange-accent);">
                            Операция:
                        </label>
                        <select class="operation-select" id="${id}_operation" style="width: 100%; padding: 12px; border-radius: 6px; border: 2px solid #ddd;">
                            <option value="add">Сложение (f(x) + g(x))</option>
                            <option value="subtract">Вычитание (f(x) - g(x))</option>
                            <option value="multiply">Умножение (f(x) * g(x))</option>
                            <option value="divide">Деление (f(x) / g(x))</option>
                            <option value="compose">Сложная (f(g(x)))</option>
                        </select>
                    </div>
                    
                    <div style="margin-bottom: 15px;">
                        <label style="display: block; margin-bottom: 8px; font-weight: 600; color: var(--orange-accent);">
                            Выберите вторую функцию (g):
                        </label>
                        <select class="function-select" id="${id}_func2" style="width: 100%; padding: 12px; border-radius: 6px; border: 2px solid #ddd;">
                            <option value="">-- Выберите функцию --</option>
                        </select>
                    </div>
                    
                    <div class="preview" style="margin-top: 20px; padding: 15px; background: rgba(255, 165, 0, 0.1); border-radius: 8px; border: 2px solid rgba(255, 165, 0, 0.3);">
                        <div style="font-weight: 600; color: var(--orange-accent); margin-bottom: 8px;">
                            Предварительный просмотр:
                        </div>
                        <div id="${id}_preview" style="font-family: 'Courier New', monospace; font-size: 14px; color: #333;">
                            h(x) = f(x) ? g(x)
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `;
}

// Инициализация селектов для композитной функции
function initializeCompositeFunctionSelects(functionId) {
    updateAvailableFunctions();

    const func1Select = document.getElementById(`${functionId}_func1`);
    const func2Select = document.getElementById(`${functionId}_func2`);
    const operationSelect = document.getElementById(`${functionId}_operation`);
    const previewElement = document.getElementById(`${functionId}_preview`);

    if (!func1Select || !func2Select || !operationSelect || !previewElement) return;

    // Заполняем селекты доступными функциями
    populateFunctionSelect(func1Select);
    populateFunctionSelect(func2Select);

    // Обновляем превью при изменениях
    const updatePreview = () => {
        const func1Id = func1Select.value;
        const func2Id = func2Select.value;
        const operation = operationSelect.value;

        let previewText = 'h(x) = ';

        const func1 = window.availableFunctions.find(f => f.id === func1Id);
        const func2 = window.availableFunctions.find(f => f.id === func2Id);

        if (func1 && func2) {
            const func1Name = func1.name || 'f';
            const func2Name = func2.name || 'g';

            switch(operation) {
                case 'add':
                    previewText = `h(x) = ${func1Name}(x) + ${func2Name}(x)`;
                    break;
                case 'subtract':
                    previewText = `h(x) = ${func1Name}(x) - ${func2Name}(x)`;
                    break;
                case 'multiply':
                    previewText = `h(x) = ${func1Name}(x) * ${func2Name}(x)`;
                    break;
                case 'divide':
                    previewText = `h(x) = ${func1Name}(x) / ${func2Name}(x)`;
                    break;
                case 'compose':
                    previewText = `h(x) = ${func1Name}(${func2Name}(x))`;
                    break;
            }
        } else {
            previewText = 'h(x) = f(x) ? g(x)';
        }

        previewElement.textContent = previewText;
    };

    func1Select.addEventListener('change', updatePreview);
    func2Select.addEventListener('change', updatePreview);
    operationSelect.addEventListener('change', updatePreview);

    updatePreview();
}

// Заполнение селекта функциями
function populateFunctionSelect(selectElement) {
    if (!selectElement || !window.availableFunctions) return;

    // Очищаем текущие опции (кроме первой пустой)
    while (selectElement.options.length > 1) {
        selectElement.remove(1);
    }

    // Добавляем доступные функции
    window.availableFunctions.forEach(func => {
        const option = document.createElement('option');
        option.value = func.id;

        let displayText = func.name || 'Без названия';
        if (func.type === 'SIMPLE') {
            displayText += ' (простая)';
        } else if (func.type === 'TABULATED') {
            displayText += ` (табулированная, ${func.definition.edits?.length || 0} точек)`;
        } else if (func.type === 'COMPOSITE') {
            displayText += ' (Оперируемая)';
        }

        option.textContent = displayText;
        selectElement.appendChild(option);
    });
}

// Сохранение композитной функции
window.saveCompositeFunction = async function(id, functionId) {
    const editor = document.getElementById(id);
    if (!editor) return;

    const nameInput = document.getElementById(`${id}_name`);
    const func1Select = document.getElementById(`${id}_func1`);
    const func2Select = document.getElementById(`${id}_func2`);
    const operationSelect = document.getElementById(`${id}_operation`);

    if (!nameInput || !nameInput.value.trim()) {
        showNotification('Введите название функции', '#ff4444', 'error');
        return;
    }

    if (!func1Select || !func1Select.value) {
        showNotification('Выберите первую функцию', '#ff4444', 'error');
        return;
    }

    if (!func2Select || !func2Select.value) {
        showNotification('Выберите вторую функцию', '#ff4444', 'error');
        return;
    }

    if (func1Select.value === func2Select.value) {
        showNotification('Выберите разные функции', '#ff4444', 'error');
        return;
    }

    const func1 = window.availableFunctions.find(f => `${f.id}` === func1Select.value);
    const func2 = window.availableFunctions.find(f => `${f.id}` === func2Select.value);

    if (!func1 || !func2) {
        showNotification('Одна из выбранных функций не найдена', '#ff4444', 'error');
        return;
    }

    const name = nameInput.value.trim();
    const operation = operationSelect.value;

    const functionData = {
        userId: userId,
        name: name,
        type: 'COMPOSITE',
        definition: {
            type: 'COMPOSITE',
            id1: parseInt(func1.id, 10),
            id2: parseInt(func2.id, 10),
            operation: operation
        }
    }

    console.info(functionData);
    const savedFunction = await saveFunctionToDB(functionId, functionData);
    console.info(savedFunction);
    if (!savedFunction){
        console.info('null при сохранении');
        return;
    }
    savedFunction.func1 = func1;
    savedFunction.func2 = func2;

    loadCompositeFunction(id, savedFunction);
    showNotification(`Оперируемая функция "${name}" сохранена`, '#4CAF50');
    updateAvailableFunctions();
};

function loadCompositeFunction(id, data) {
    const editor = document.getElementById(id);
    data.uiId = `${id}`;
    if (window.calculator) {
        window.calculator.addFunction(data);
    }

    let operationSymbol = '';
    let operationText = '';
    switch(data.definition.operation) {
        case 'add':
            operationSymbol = '+';
            operationText = 'сложение';
            break;
        case 'subtract':
            operationSymbol = '-';
            operationText = 'вычитание';
            break;
        case 'multiply':
            operationSymbol = '*';
            operationText = 'умножение';
            break;
        case 'divide':
            operationSymbol = '/';
            operationText = 'деление';
            break;
        case 'compose':
            operationSymbol = '∘';
            operationText = 'композиция';
            break;
    }

    const functionHtml = `
        <div class="function-item" id="display_${id}">
            <div class="function-type">Оперируемая функция</div>
            <div class="function-header">
                <span class="function-name">${data.name}</span>
                <div class="function-controls">
                    <button class="function-edit-btn" onclick="editCompositeFunction('${id}')" id="${id}_editComposite" title="Редактировать">
                        <img src="images/edit-icon.svg" alt="Редактировать">
                    </button>
                    <button class="function-delete-btn" onclick="deleteFunction('${id}', '${data.id}')" title="Удалить">
                        <img src="images/delete-icon.svg" alt="Удалить">
                    </button>
                </div>
            </div>
            <div class="function-expression">
                ${data.definition.operation === 'compose'
        ? `h(x) = ${data.func1.name}(${data.func2.name}(x))`
        : `h(x) = ${data.func1.name}(x) ${operationSymbol} ${data.func2.name}(x)`}
            </div>
            <div style="font-size: 12px; color: #666; margin-top: 10px; display: flex; gap: 15px;">
                <span>Операция: ${operationText}</span>
                <span>Тип: ${data.func1.type}/${data.func2.type}</span>
            </div>
        </div>
    `;

    editor.outerHTML = functionHtml;
}

// Редактирование композитной функции
window.editCompositeFunction = function(id) {
    if (!window.calculator) return;

    const func = window.calculator.functions.find(f => f.uiId === id);
    console.info(func);
    if (!func || func.type !== 'COMPOSITE') return;
    console.info('123');

    // Загружаем доступные функции
    updateAvailableFunctions();

    const editorHtml = createCompositeFunctionEditor(id, func.id);

    const displayElement = document.getElementById('display_' + id);
    if (displayElement) {
        displayElement.outerHTML = editorHtml;

        // Заполняем данные
        const nameInput = document.getElementById(`${id}_name`);
        const func1Select = document.getElementById(`${id}_func1`);
        const func2Select = document.getElementById(`${id}_func2`);
        const operationSelect = document.getElementById(`${id}_operation`);

        if (nameInput) nameInput.value = func.name || '';
        if (operationSelect) operationSelect.value = func.definition.operation || 'add';

        // Заполняем селекты и выбираем текущие функции
        setTimeout(() => {
            if (func1Select) {
                populateFunctionSelect(func1Select);
                if (func.func1) {
                    func1Select.value = func.func1.id;
                }
            }

            if (func2Select) {
                populateFunctionSelect(func2Select);
                if (func.func2) {
                    func2Select.value = func.func2.id;
                }
            }

            // Обновляем превью
            const previewElement = document.getElementById(`${id}_preview`);
            if (previewElement) {
                if (func.func1 && func.func2) {
                    const func1Name = func.func1.name || 'f';
                    const func2Name = func.func2.name || 'g';
                    const operation = func.definition.operation || 'add';

                    let previewText = '';
                    switch(operation) {
                        case 'add':
                            previewText = `h(x) = ${func1Name}(x) + ${func2Name}(x)`;
                            break;
                        case 'subtract':
                            previewText = `h(x) = ${func1Name}(x) - ${func2Name}(x)`;
                            break;
                        case 'multiply':
                            previewText = `h(x) = ${func1Name}(x) * ${func2Name}(x)`;
                            break;
                        case 'divide':
                            previewText = `h(x) = ${func1Name}(x) / ${func2Name}(x)`;
                            break;
                        case 'compose':
                            previewText = `h(x) = ${func1Name}(${func2Name}(x))`;
                            break;
                    }
                    previewElement.textContent = previewText;
                }
            }
        }, 100);

        window.calculator.removeFunction(id);
    }
};

// Функция для добавления новой точки
window.addNewPoint = function(functionId) {
    currentTabulatedPoints.push({
        x: null,
        y: null
    });

    updatePointsTable(functionId);
    updateFilledCount(functionId);

    const tableBody = document.getElementById(`${functionId}_points_body`);
    if (tableBody && tableBody.lastChild) {
        tableBody.lastChild.scrollIntoView({behavior: 'smooth', block: 'nearest'});
    }
}

// Функция для сортировки точек по X
window.sortPoints = function(functionId) {
    const filledPoints = currentTabulatedPoints.filter(p => p.x !== null);
    const emptyPoints = currentTabulatedPoints.filter(p => p.x === null);

    filledPoints.sort((a, b) => a.x - b.x);

    currentTabulatedPoints = [...filledPoints, ...emptyPoints];

    updatePointsTable(functionId);

    showNotification('Точки отсортированы по возрастанию X', '#4CAF50');
}

// Функция для удаления точки
window.removeTabulatedPoint = function(functionId, index) {
    if (currentTabulatedPoints.length <= 2) {
        showNotification('Нельзя удалить базовые точки. Минимум 2 точки должно остаться.', '#ff4444', 'error');
        return;
    }

    if (index < 2) {
        showNotification('Нельзя удалить обязательные базовые точки.', '#ff4444', 'error');
        return;
    }

    if (confirm(`Удалить точку №${index + 1}?`)) {
        currentTabulatedPoints.splice(index, 1);
        updatePointsTable(functionId);
        updateFilledCount(functionId);
        showNotification('Точка удалена', '#ff9800', 'warning');
    }
}

// Функция для обновления таблицы точек
function updatePointsTable(functionId) {
    const pointsBody = document.getElementById(`${functionId}_points_body`);
    if (!pointsBody) return;

    pointsBody.innerHTML = '';

    currentTabulatedPoints.forEach((point, index) => {
        const row = document.createElement('tr');
        const isBasic = index < 2; // Теперь только 2 обязательные точки

        row.innerHTML = `
            <td style="text-align: center; font-weight: 600; color: ${isBasic ? '#FFA500' : '#45B7D1'};">
                ${index + 1}
                ${isBasic ? '<div style="font-size: 11px; color: #888; margin-top: 2px;">обязательная</div>' : ''}
            </td>
            <td>
                <input type="number" 
                       class="tabulated-input"
                       value="${point.x !== null ? point.x : ''}" 
                       placeholder="Введите x"
                       step="any"
                       onchange="updatePointValue('${functionId}', ${index}, 'x', this.value)"
                       style="min-height: 40px; box-sizing: border-box;">
            </td>
            <td>
                <input type="number" 
                       class="tabulated-input"
                       value="${point.y !== null ? point.y : ''}" 
                       placeholder="Введите y"
                       step="any"
                       onchange="updatePointValue('${functionId}', ${index}, 'y', this.value)"
                       style="min-height: 40px; box-sizing: border-box;">
            </td>
            <td style="text-align: center;">
                ${index >= 2 ? `
                    <button onclick="removeTabulatedPoint('${functionId}', ${index})" 
                            class="tabulated-delete-btn">
                        Удалить
                    </button>
                ` : `
                    <span style="color: #666; font-size: 12px; padding: 8px 0; display: inline-block; font-weight: 500;">
                        Обязательная
                    </span>
                `}
            </td>
        `;
        pointsBody.appendChild(row);
    });
}

// Функция для обновления значения точки
window.updatePointValue = function(functionId, index, field, value) {
    if (value === '') {
        currentTabulatedPoints[index][field] = null;
    } else {
        const numValue = parseFloat(value);

        if (isNaN(numValue)) {
            showNotification('Пожалуйста, введите корректное число', '#ff4444', 'error');
            const input = event ? event.target : null;
            if (input) {
                input.value = '';
                currentTabulatedPoints[index][field] = null;
            }
            updateFilledCount(functionId);
            return;
        }

        currentTabulatedPoints[index][field] = numValue;
    }

    updateFilledCount(functionId);
}

// Функция для обновления счетчика заполненных точек
function updateFilledCount(functionId) {
    const filledCount = currentTabulatedPoints.filter(p =>
        p.x !== null && p.y !== null && !isNaN(p.x) && !isNaN(p.y)
    ).length;

    const countElement = document.getElementById(`${functionId}_filledCount`);
    if (countElement) {
        countElement.textContent = `Заполнено: ${filledCount} точек`;

        if (filledCount >= 2) {
            countElement.className = 'points-count valid';
        } else {
            countElement.className = 'points-count';
        }
    }
}

async function saveFunctionToDB(functionId, functionData) {
    console.info(functionId, functionData);
    const url = 'http://localhost:8080/mathhub/api/functions';
    const method = functionId === '-1' ? 'POST' : 'PUT';

    if (method === 'PUT') functionData.id = parseInt(functionId, 10);

    try {
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(functionData)
        });
        console.info(response);
        functionData = await response.json();
        return functionData;
    } catch (error) {
        console.info('Ошибка');
        console.error(error);
    }
    return null;
}

// Функция для сохранения простой функции
window.saveSimpleFunction = async function(id, functionId) {
    const editor = document.getElementById(id);
    if (!editor) return;

    const nameInput = document.getElementById(`${id}_name`);
    const select = editor.querySelector('.simple-function-select');
    const fromInput = document.getElementById(`${id}_from`);
    const toInput = document.getElementById(`${id}_to`);

    if (!nameInput || !nameInput.value.trim()) {
        showNotification('Введите название функции', '#ff4444', 'error');
        return;
    }

    // Проверка интервала
    const from = parseFloat(fromInput.value);
    const to = parseFloat(toInput.value);

    if (isNaN(from) || isNaN(to)) {
        showNotification('Введите корректные значения интервала', '#ff4444', 'error');
        return;
    }

    if (from >= to) {
        showNotification('Значение "От" должно быть меньше значения "До"', '#ff4444', 'error');
        return;
    }

    const name = nameInput.value.trim();
    const expressionName = select.options[select.selectedIndex].text;

    const functionData = {
        userId: parseInt(userId, 10),
        name: name,
        type: 'SIMPLE',
        definition: {
            type: 'SIMPLE',
            function: getFromExpression(expressionName.split('=')[1].trim()),
            xFrom: from,
            xTo: to
        }
    }

    const savedFunction = await saveFunctionToDB(functionId, functionData);
    if (savedFunction === undefined){
        console.info('null при сохранении');
        return;
    }

    loadSimpleFunction(id, savedFunction);
    showNotification(`Функция "${name}" сохранена`, '#4CAF50');
    updateAvailableFunctions();
}

function loadSimpleFunction(id, data) {
    const editor = document.getElementById(id);
    data.uiId = `${id}`;
    if (window.calculator) {
        window.calculator.addFunction(data);
    }
    const functionHtml = `
        <div class="function-item" id="display_${id}">
            <div class="function-type">Простая функция</div>
            <div class="function-header">
                <span class="function-name">${data.name}</span>
                <div class="function-controls">
                    <button class="function-edit-btn" onclick="editSimpleFunction('${id}')" title="Редактировать">
                        <img src="images/edit-icon.svg" alt="Редактировать">
                    </button>
                    <button class="function-delete-btn" onclick="deleteFunction('${id}', '${data.id}')" title="Удалить">
                        <img src="images/delete-icon.svg" alt="Удалить">
                    </button>
                </div>
            </div>
            <div class="function-expression">${getExpressionName(data.definition.function)}</div>
            <div style="font-size: 12px; color: #666; margin-top: 10px; display: flex; gap: 15px;">
                <span>Интервал: [${data.definition.xFrom.toFixed(1)}, ${data.definition.xTo.toFixed(1)}]</span>
            </div>
        </div>
    `;
    editor.outerHTML = functionHtml;
}

// Функция для сохранения табулированной функции
window.saveTabulatedFunction = async function(id, functionId) {
    const filledPoints = currentTabulatedPoints.filter(p =>
        p.x !== null && p.y !== null && !isNaN(p.x) && !isNaN(p.y)
    );

    if (filledPoints.length < 2) {
        showNotification('Заполните как минимум 2 точки (x и y для каждой)', '#ff4444', 'error');
        return;
    }

    const nameInput = document.getElementById(`${id}_name`);
    if (!nameInput || !nameInput.value.trim()) {
        showNotification('Введите название функции', '#ff4444', 'error');
        return;
    }

    const name = nameInput.value.trim();

    const unfilledBasicPoints = currentTabulatedPoints
        .slice(0, 2)
        .filter(p => p.x === null || p.y === null || isNaN(p.x) || isNaN(p.y))
        .length;

    if (unfilledBasicPoints > 0) {
        showNotification('Заполните все обязательные точки (первые 2)', '#ff4444', 'error');
        return;
    }


    const functionData = {
        userId: userId,
        name: name,
        type: 'TABULATED',
        definition: {
            type: 'TABULATED',
            edits: filledPoints
        }
    };
    const savedFunction = await saveFunctionToDB(functionId, functionData);
    if (savedFunction === undefined){
        console.info('null при сохранении');
        return;
    }

    loadTabulatedFunction(id, savedFunction);
    showNotification(`Функция "${name}" сохранена с ${filledPoints.length} точками`, '#4CAF50');
    updateAvailableFunctions();
}

function loadTabulatedFunction(id, data) {
    const filledPoints = data.definition.edits;
    let pointsStr;
    data.uiId = `${id}`;

    if (window.calculator) {
        window.calculator.addFunction(data);
    }

    if (filledPoints.length <= 5) {
        pointsStr = filledPoints.map(p => `(${p.x.toFixed(2)}, ${p.y.toFixed(2)})`).join(', ');
    } else {
        const firstTwo = filledPoints.slice(0, 2);
        const lastTwo = filledPoints.slice(-2);
        pointsStr = `${firstTwo.map(p => `(${p.x.toFixed(2)}, ${p.y.toFixed(2)})`).join(', ')} ... ${lastTwo.map(p => `(${p.x.toFixed(2)}, ${p.y.toFixed(2)})`).join(', ')}`;
    }

    const minX = Math.min(...filledPoints.map(p => p.x)).toFixed(2);
    const maxX = Math.max(...filledPoints.map(p => p.x)).toFixed(2);

    const functionHtml = `
        <div class="function-item" id="display_${id}">
            <div class="function-type">Табулированная функция</div>
            <div class="function-header">
                <span class="function-name">${data.name}</span>
                <div class="function-controls">
                    <button class="function-edit-btn" onclick="editTabulatedFunction('${id}')" title="Редактировать">
                        <img src="images/edit-icon.svg" alt="Редактировать">
                    </button>
                    <button class="function-delete-btn" onclick="deleteFunction('${id}', '${data.id}')" title="Удалить">
                        <img src="images/delete-icon.svg" alt="Удалить">
                    </button>
                </div>
            </div>
            <div class="function-expression">
                ${filledPoints.length} точек: ${pointsStr}
            </div>
            <div style="font-size: 12px; color: #666; margin-top: 10px; display: flex; gap: 15px;">
                <span>Диапазон X: [${minX}, ${maxX}]</span>
                <span>Точек: ${filledPoints.length}</span>
            </div>
        </div>
    `;

    const editor = document.getElementById(id);
    if (editor) {
        editor.outerHTML = functionHtml;
    }

    currentTabulatedPoints = [];

    const functionsMenu = document.getElementById('functionsMenu');
    if (functionsMenu) {
        functionsMenu.classList.remove('active');
    }
}

window.cancelEdit = function(functionId) {
    const element = document.getElementById(functionId);
    if (element) {
        element.remove();
        showNotification('Редактирование отменено', '#ff9800', 'warning');
    }
}

window.deleteFunction = function(id, functionId, withoutConfirm=false) {
    if (withoutConfirm || confirm('Удалить эту функцию?')) {
        if (functionId !== undefined) {
            const url = 'http://localhost:8080/mathhub/api/functions/' + functionId;
            fetch(url, {
                method: 'DELETE'
            }).then(r => {});
        }
        if (window.calculator) {
            window.calculator.removeFunction(id);
        }
        const displayElement = document.getElementById('display_' + id);
        if (displayElement) {
            displayElement.remove();
            showNotification('Функция удалена', '#ff9800', 'warning');
        }
    }
}

window.editSimpleFunction = function(id) {
    if (!window.calculator) return;

    const func = window.calculator.functions.find(f => f.uiId === id);
    if (!func) return;

    const from = func.interval ? func.interval.from : -10;
    const to = func.interval ? func.interval.to : 10;
    const pointsCount = func.interval ? func.interval.points : 21;

    const editorHtml = `
        <div class="function-editor" id="${id}">
            <div class="function-header">
                <span>Редактирование функции</span>
                <div class="function-controls">
                    <button class="function-edit-btn" onclick="saveSimpleFunction('${id}', '${func.id}')" title="Сохранить">
                        <img src="images/choose-icon.svg" alt="Сохранить">
                    </button>
                    <button class="function-delete-btn" onclick="cancelEdit('${id}')" title="Отмена">
                        <img src="images/delete-icon.svg" alt="Отмена">
                    </button>
                </div>
            </div>
            <div class="function-content">
                <input type="text" 
                       id="${id}_name" 
                       class="function-name-input" 
                       placeholder="Введите название функции"
                       value="${func.name || 'Простая функция'}">
                
                <div class="function-select-container">
                    <select class="simple-function-select" id="${id}_select">
                        <option value="x" ${func.expression === 'x' ? 'selected' : ''}>f(x) = x</option>
                        <option value="x^2" ${func.expression === 'x^2' ? 'selected' : ''}>f(x) = x²</option>
                        <option value="x^3" ${func.expression === 'x^3' ? 'selected' : ''}>f(x) = x³</option>
                        <option value="sin(x)" ${func.expression === 'sin(x)' ? 'selected' : ''}>f(x) = sin(x)</option>
                        <option value="cos(x)" ${func.expression === 'cos(x)' ? 'selected' : ''}>f(x) = cos(x)</option>
                        <option value="tan(x)" ${func.expression === 'tan(x' ? 'selected' : ''}>f(x) = tan(x)</option>
                        <option value="exp(x)" ${func.expression === 'exp(x)' ? 'selected' : ''}>f(x) = exp(x)</option>
                        <option value="log(x)" ${func.expression === 'log(x)' ? 'selected' : ''}>f(x) = log(x)</option>
                        <option value="sqrt(x)" ${func.expression === 'sqrt(x)' ? 'selected' : ''}>f(x) = √x</option>
                        <option value="1/x" ${func.expression === '1/x' ? 'selected' : ''}>f(x) = 1/x</option>
                        <option value="abs(x)" ${func.expression === 'abs(x)' ? 'selected' : ''}>f(x) = |x|</option>
                    </select>
                </div>
                
                <div class="interval-settings" style="margin-top: 20px;">
                    <div style="margin-bottom: 15px;">
                        <label style="display: block; margin-bottom: 8px; font-weight: 600; color: var(--orange-accent);">
                            Настройки интервала:
                        </label>
                        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 12px; margin-bottom: 12px;">
                            <div>
                                <label style="display: block; margin-bottom: 5px; font-size: 13px; color: #666;">От:</label>
                                <input type="number" 
                                       id="${id}_from" 
                                       class="interval-input"
                                       value="${from}" 
                                       step="0.5"
                                       style="width: 100%; padding: 10px; border: 2px solid #ddd; border-radius: 6px;">
                            </div>
                            <div>
                                <label style="display: block; margin-bottom: 5px; font-size: 13px; color: #666;">До:</label>
                                <input type="number" 
                                       id="${id}_to" 
                                       class="interval-input"
                                       value="${to}" 
                                       step="0.5"
                                       style="width: 100%; padding: 10px; border: 2px solid #ddd; border-radius: 6px;">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `;

    const displayElement = document.getElementById('display_' + id);
    if (displayElement) {
        displayElement.outerHTML = editorHtml;
        window.calculator.removeFunction(id);
    }
}

window.editTabulatedFunction = function(id) {
    if (!window.calculator) return;

    const func = window.calculator.functions.find(f => f.uiId === id);
    if (!func || func.type !== 'TABULATED') return;

    currentTabulatedPoints = [];
    const points = func.definition.edits;

    if (points.length >= 2) {
        currentTabulatedPoints = [
            {x: points[0].x, y: points[0].y},
            {x: points[1].x, y: points[1].y}
        ];

        for (let i = 2; i < points.length; i++) {
            currentTabulatedPoints.push({x: points[i].x, y: points[i].y});
        }
    } else {
        currentTabulatedPoints = [
            {x: null, y: null},
            {x: null, y: null}
        ];
    }
    console.info(currentTabulatedPoints);

    const editorHtml = `
        <div class="function-editor" id="${id}">
            <div class="function-header">
                <span>Редактирование табулированной функции</span>
                <div class="function-controls">
                    <button class="function-edit-btn" onclick="saveTabulatedFunction('${id}', '${func.id}')" title="Сохранить">
                        <img src="images/choose-icon.svg" alt="Сохранить">
                    </button>
                    <button class="function-delete-btn" onclick="cancelEdit('${id}')" title="Отмена">
                        <img src="images/delete-icon.svg" alt="Отмена">
                    </button>
                </div>
            </div>
            <div class="function-content">
                <input type="text" id="${id}_name" 
                       class="function-name-input"
                       placeholder="Введите название функции" 
                       value="${func.name || 'Табулированная функция'}">
                
                <div class="points-counter">
                    <span id="${id}_filledCount" class="points-count">Заполнено: ${currentTabulatedPoints.filter(p => p.x !== null && p.y !== null).length} точек</span>
                    <span class="points-requirement">Минимум: 2 точки</span>
                </div>
                
                <div style="display: flex; gap: 12px; margin-bottom: 20px;">
                    <button type="button" class="function-action-btn add-point-btn" onclick="addNewPoint('${id}')">
                        Добавить точку
                    </button>
                    <button type="button" class="function-action-btn sort-points-btn" onclick="sortPoints('${id}')">
                        Сортировать
                    </button>
                </div>
                
                <div class="tabulated-table-container">
                    <table class="tabulated-table">
                        <thead>
                            <tr>
                                <th>№</th>
                                <th>Координата X</th>
                                <th>Координата Y</th>
                                <th>Действие</th>
                            </tr>
                        </thead>
                        <tbody id="${id}_points_body">
                        </tbody>
                    </table>
                </div>
                
                <div class="function-info">
                    <div class="function-info-title">Как работать:</div>
                    <div>Заполните значения x и y для минимум 2 точек</div>
                    <div style="margin-top: 8px; font-size: 13px;">Первые 2 точки - обязательные для заполнения</div>
                </div>
            </div>
        </div>
    `;

    const displayElement = document.getElementById('display_' + id);
    if (displayElement) {
        displayElement.outerHTML = editorHtml;

        updatePointsTable(id);
        updateFilledCount(id);

        window.calculator.removeFunction(id);
    }
}

// ============================================
// НОВАЯ СИСТЕМА: Сохранение и загрузка функций
// ============================================

// Функция для скачивания функции в новом формате
window.downloadFunction = function(functionId) {
    if (!window.calculator) return;

    const func = window.calculator.functions.find(f => f.id === functionId);
    if (!func) return;

    const data = transformToJsonFile(func);
    data.metadata = {
        exportDate: new Date().toISOString(),
        exportFrom: 'MathHub Graphing Calculator'
    }

    // Преобразуем в JSON
    const jsonData = JSON.stringify(data, null, 2);

    // Создаем имя файла на основе имени функции
    let fileName = func.name || 'function';

    // Очищаем имя файла от недопустимых символов
    fileName = fileName
        .replace(/[<>:"/\\|?*]/g, '') // Убираем недопустимые символы для файлов
        .replace(/\s+/g, '_') // Заменяем пробелы на подчеркивания
        .replace(/[^a-zA-Z0-9а-яА-Я_\-]/g, '') // Убираем все кроме букв, цифр, _, -
        .substring(0, 50); // Ограничиваем длину имени файла

    // Если имя файла пустое после очистки
    if (!fileName.trim()) {
        fileName = 'function';
    }

    // Формируем окончательное имя файла
    fileName = `${fileName}.json`;

    // Создаем Blob и скачиваем
    const blob = new Blob([jsonData], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = fileName;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);

    showNotification(`Функция "${func.name}" скачана как "${fileName}"`, '#4CAF50');
};

function transformToJsonFile(func){
    const data = {
        type: func.type,
        name: func.name,
        definition: func.definition,
    }

    if (func.type === 'SIMPLE'){
        const definition = {
            type: func.definition.type,
            function: func.definition.function,
            xFrom: func.definition.xFrom,
            xTo: func.definition.xTo,
        }
        data.definition = definition;
    } else if (func.type === 'COMPOSITE') {
        const definition = {
            type: func.definition.type,
            operation: func.definition.operation
        }
        data.definition = definition;
        data.func1 = transformToJsonFile(func.func1);
        data.func2 = transformToJsonFile(func.func2);
    }

    return data;
}

// Функция для скачивания всех функций в новом формате
function downloadAllFunctions() {
    if (!window.calculator || window.calculator.functions.length === 0) {
        showNotification('Нет функций для сохранения', '#ff4444', 'error');
        return;
    }

    const functionsData = window.calculator.functions.map(func => {
        const funcData = {
            type: func.type.toUpperCase(), // Тип в заглавных буквах
            name: func.name || 'Без названия'
        };

        if (func.type === 'simple') {
            funcData.expression = func.expression;

            if (func.interval) {
                funcData.interval = {
                    from: func.interval.from,
                    to: func.interval.to,
                    points: func.interval.points
                };
            } else {
                funcData.interval = {
                    from: -10,
                    to: 10,
                    points: 21
                };
            }

        } else if (func.type === 'tabulated') {
            funcData.points = func.points;
            if (func.convertedFrom) funcData.convertedFrom = func.convertedFrom;
            if (func.originalInterval) funcData.originalInterval = func.originalInterval;

        } else if (func.type === 'composite') {
            funcData.operation = func.operation;
            funcData.func1 = func.func1 ? {
                name: func.func1.name,
                type: func.func1.type.toUpperCase()
            } : null;
            funcData.func2 = func.func2 ? {
                name: func.func2.name,
                type: func.func2.type.toUpperCase()
            } : null;
        }

        return funcData;
    });

    const data = {
        functions: functionsData,
        metadata: {
            exportDate: new Date().toISOString(),
            exportFrom: 'MathHub Graphing Calculator',
            version: '1.0',
            totalFunctions: window.calculator.functions.length
        }
    };

    const jsonData = JSON.stringify(data, null, 2);

    // Создаем имя файла для всех функций
    const currentDate = new Date();
    const dateStr = currentDate.toISOString().split('T')[0]; // Только дата
    const timeStr = currentDate.toTimeString().split(' ')[0].replace(/:/g, '-'); // Время без двоеточий

    const fileName = `MathHub_Functions_${dateStr}_${timeStr}.json`;

    const blob = new Blob([jsonData], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = fileName;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);

    showNotification(`Все функции сохранены в "${fileName}"`, '#4CAF50');
}

// Функция для загрузки функции из файла
function loadFunctionFromFile(file) {
    const reader = new FileReader();

    reader.onload = async function(e) {
        try {
            const jsonData = JSON.parse(e.target.result);

            if (!jsonData.type) {
                throw new Error('Некорректный формат файла');
            } else {
                // Загружаем одну функцию
                await loadSingleFunction(jsonData);
            }

        } catch (error) {
            showNotification(`Ошибка загрузки: ${error.message}`, '#ff4444', 'error');
            console.error('Ошибка загрузки функции:', error);
        }
    }

    reader.onerror = function() {
        showNotification('Ошибка чтения файла', '#ff4444', 'error');
    };

    reader.readAsText(file);
}

// Загрузка одной функции
async function loadSingleFunction(data) {
    if (!data.metadata || !data.metadata.exportFrom) {
        showNotification('Файл создан не в MathHub', '#ff9800', 'warning');
    }

    data.userId = userId;

    if (data.type === 'SIMPLE') {
        const savedFunction = await saveFunctionToDB('-1', data);
        createFunctionEditor(savedFunction.type, savedFunction.id);
        loadSimpleFunction(savedFunction.id, savedFunction);
    } else if (data.type === 'TABULATED') {
        const savedFunction = await saveFunctionToDB('-1', data);
        createFunctionEditor(savedFunction.type, savedFunction.id);
        loadTabulatedFunction(savedFunction.id, savedFunction);
    } else if (data.type === 'COMPOSITE') {
        const id = getRandomId();
        createFunctionEditor(data.type, id);
        data.id = id;
        loadCompositeFunction(id, data);
        document.getElementById(`${id}_editComposite`).remove();
    } else {
        throw new Error(`Неизвестный тип функции: ${data.type}`);
    }

    updateAvailableFunctions();
}

// Вспомогательная функция для создания expressionName из expression
function getExpressionName(expression) {
    const expressionMap = {
        'x': 'f(x) = x',
        'x^2': 'f(x) = x²',
        'x^3': 'f(x) = x³',
        'sin': 'f(x) = sin(x)',
        'cos': 'f(x) = cos(x)',
        'tan': 'f(x) = tan(x)',
        'exp': 'f(x) = exp(x)',
        'log': 'f(x) = log(x)',
        'sqrt': 'f(x) = √x',
        '1/x': 'f(x) = 1/x',
        'abs': 'f(x) = |x|'
    };

    return expressionMap[expression] || `f(x) = ${expression}`;
}

function getFromExpression(expression) {
    const expressionMap = {
        'x²': 'x^2',
        'x³': 'x^3',
        'sin(x)': 'sin',
        'cos(x)': 'cos',
        'tan(x)': 'tan',
        'exp(x)': 'exp',
        'log(x)': 'log',
        '√x': 'sqrt',
        '|x|': 'abs',
    };

    return expressionMap[expression] || expression;
}

// Вспомогательная функция для показа уведомлений
function showNotification(message, color, type = 'success') {
    const oldNotifications = document.querySelectorAll('.custom-notification');
    oldNotifications.forEach(notification => {
        if (notification.style.opacity === '0' || notification.style.opacity === '') {
            notification.remove();
        }
    });

    const notification = document.createElement('div');
    notification.className = `custom-notification ${type}`;
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: ${color};
        color: white;
        padding: 16px 24px;
        border-radius: 10px;
        z-index: 10000;
        font-weight: 500;
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        display: flex;
        align-items: center;
        gap: 12px;
        max-width: 350px;
        animation: slideIn 0.3s ease-out;
    `;

    notification.innerHTML = `<span>${message}</span>`;

    document.body.appendChild(notification);

    setTimeout(() => {
        notification.style.opacity = '0';
        notification.style.transition = 'opacity 0.3s';
        setTimeout(() => notification.remove(), 300);
    }, 3000);
}

function getRandomId() {
    return 'func_' + Date.now();
}

function getCookie(name) {
    const cookies = document.cookie.split(';');
    for (let cookie of cookies) {
        const [cookieName, cookieValue] = cookie.trim().split('=');
        if (cookieName === name) {
            return decodeURIComponent(cookieValue || '');
        }
    }
    return null;
}