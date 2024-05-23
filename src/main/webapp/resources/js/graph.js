const canvas = document.getElementById('coordinateCanvas');
const centerX = canvas.width / 2;
const centerY = canvas.height / 2;
const pxR = 50;
const ctx = canvas.getContext('2d');

var sessionStorage = window.sessionStorage;

var rValue = sessionStorage.getItem("r-value") == null ? sessionStorage.setItem("r-value", "2.0") : sessionStorage.getItem("r-value");

function updateR(event, ui) {
    let rInput = document.getElementById("user-request:r_output");
    sessionStorage.setItem("r-value", rInput.innerText);
    rValue = sessionStorage.getItem("r-value");
    drawGraph();
    drawPointsFromTable();
}


document.addEventListener('DOMContentLoaded', function () {
    rValue = sessionStorage.getItem("r-value");
    drawGraph();
    drawPointsFromTable();
});


canvas.addEventListener('click', function (event) {
        if (rValue == null) alert("Can't find coordinates, please choose value for r")
        else {
            let loc = windowToCanvas(canvas, event.clientX, event.clientY);
            let r = rValue;
            let x = xFromCanvas(loc.x);
            let y = yFromCanvas(loc.y);

            addDotFromCanvas(
                [
                    {name: "x", value: x.toString()},
                    {name: "y", value: y.toString()},
                    {name: "r", value: r.toString()}
                ]
            )
        }


    }
);


function drawPoints() {
    let points = JSON.parse(sessionStorage.getItem('points'));
    for (const pointString of points) {
        const point = JSON.parse(pointString);
        drawPoint(point.x, point.y, true);
    }
}

function updateTextInputs(x, y) {
    document.getElementById("x").setAttribute("value", x);
    document.getElementById("y").setAttribute("value", y);

}




function xFromCanvas(x) {
    return (x - centerX) / pxR;
}

function yFromCanvas(y) {
    return (centerY - y) / pxR;
}

function xToCanvas(x) {
    return (x * pxR) + centerX;
}

function yToCanvas(y) {
    return centerY - (y * pxR);
}

function windowToCanvas(canvas, x, y) {
    let bbox = canvas.getBoundingClientRect();
    return {
        x: x - bbox.left * (canvas.width / bbox.width),
        y: y - bbox.top * (canvas.height / bbox.height)
    };
}


function drawGraph() {
    const axisLength = 500;
    const r = pxR * rValue;

    ctx.clearRect(0, 0, canvas.width, canvas.height);
    shadeRegion(ctx, centerX, centerY, r);

    // Draw X-axis
    drawArrow(ctx, -centerX, centerY, canvas.width, centerY);
    drawTicks(ctx, centerX, centerY, axisLength, 'x');
    drawAxisLabel(ctx, 'X', canvas.width - 10, centerY + 20);

    // Draw Y-axis
    drawArrow(ctx, centerX, centerY * 2, centerX, 0);
    drawTicks(ctx, centerX, centerY, axisLength, 'y');
    drawAxisLabel(ctx, 'Y', centerX - 20, 10);

    if (rValue) {
        drawAxisLabel(ctx, 'R', centerX + r, centerY - 5);
        drawAxisLabel(ctx, 'R/2', centerX + r / 2, centerY - 5);
        drawAxisLabel(ctx, 'R', centerX + 5, centerY - r);
        drawAxisLabel(ctx, 'R/2', centerX + 5, centerY - r / 2);
        drawAxisLabel(ctx, '- R', centerX - r, centerY - 5);
        drawAxisLabel(ctx, '- R/2', centerX - r / 2, centerY - 5);
        drawAxisLabel(ctx, '- R', centerX + 5, centerY + r);
        drawAxisLabel(ctx, '- R/2', centerX + 5, centerY + r / 2);
    }


    // points


}

function drawPoint(x, y, res) {

    x = xToCanvas(x);
    y = yToCanvas(y);
    ctx.beginPath();
    if (res) {
        ctx.fillStyle = 'rgb(6,246,23)';
        ctx.strokeStyle = 'rgb(6,246,23)';
    } else {
        ctx.fillStyle = 'rgb(248,0,0)';
        ctx.strokeStyle = 'rgb(248,0,0)';
    }
    ctx.moveTo(x, y);
    // ctx.fillRect(x, y, 3, 3);
    ctx.arc(x, y, 2.8, 0, Math.PI * 2);
    ctx.fill();
    ctx.stroke();
    ctx.fillStyle = 'rgba(245,87,245,0.73)';
    ctx.strokeStyle = 'rgb(0,0,0)';

}


function drawPointsFromTable() {
    var table = document.getElementById('results-table'); // Получаем таблицу по её id
    var rows = table.getElementsByTagName('tr'); // Получаем все строки таблицы

    // Проходимся по каждой строке таблицы, начиная с 1, чтобы пропустить заголовок
    if (rows[1].getElementsByTagName('td').innerText != "Данные не найдены." ) {
        for (let i = 0; i < rows.length; i++) {
            let cells = rows[i].getElementsByTagName('td'); // Получаем ячейки текущей строки

            if (cells.length >= 2) {
                let x = cells[0].innerText; // Значение X в первой ячейке
                let y = cells[1].innerText; // Значение Y во второй ячейке
                let r = cells[2].innerText; // Значение R
                let result = cells[4].innerText; // Значение Result в пятой ячейке
                // Обработка значений x, y, resul

                if (Number.parseFloat(r) === Number.parseFloat(rValue)) {
                    drawPoint(x, y, result === "kill");
                }
            }
        }
    }
}


function drawArrow(context, fromX, fromY, toX, toY) {
    context.beginPath();
    context.moveTo(fromX, fromY);
    context.lineTo(toX, toY);
    context.stroke();

    // Draw arrowhead
    const headLength = 7;
    const angle = Math.atan2(toY - fromY, toX - fromX);
    context.lineTo(toX - headLength * Math.cos(angle - Math.PI / 6), toY - headLength * Math.sin(angle - Math.PI / 6));
    context.moveTo(toX, toY);
    context.lineTo(toX - headLength * Math.cos(angle + Math.PI / 6), toY - headLength * Math.sin(angle + Math.PI / 6));
    context.stroke();
}

function drawTicks(context, centerX, centerY, length, axis) {
    const numTicks = 11;
    const tickSpacing = length / (numTicks - 1);
    const tickSize = 5;
    context.beginPath();

    for (let i = 0; i < numTicks; i++) {
        const tickPosition = i * tickSpacing - length / 2;
        if (axis === 'x') {
            context.moveTo(centerX + tickPosition, centerY - tickSize / 2);
            context.lineTo(centerX + tickPosition, centerY + tickSize / 2);
        } else {
            context.moveTo(centerX - tickSize / 2, centerY + tickPosition);
            context.lineTo(centerX + tickSize / 2, centerY + tickPosition);
        }
    }

    context.stroke();
}

function drawAxisLabel(context, label, x, y) {
    context.fillStyle = 'rgba(0, 0, 0, 1)'; // Цвет и прозрачность заливки

    context.font = '14px Arial';
    context.fillText(label, x, y);
}

function shadeRegion(context, centerX, centerY, r) {
    context.fillStyle = 'rgba(255, 0, 255, 1)'; // Цвет и прозрачность заливки

    //part of circle
    context.beginPath();
    context.arc(centerX, centerY, r / 2, 0, Math.PI / 2, false);
    context.lineTo(centerX, centerY);
    context.closePath();
    context.fill();

    //squart
    context.beginPath();
    context.rect(centerX, centerY, r / 2, -r);
    context.fill();

    //tringale
    context.beginPath();
    context.moveTo(centerX, centerY);
    context.lineTo(centerX - r / 2, centerY);
    context.lineTo(centerX, centerY - r / 2);
    context.lineTo(centerX, centerY);
    context.fill();

}

