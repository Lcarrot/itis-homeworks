const canvas = document.getElementById('canvas');
const ctx = canvas.getContext('2d');

let mouseX = 0;
let mouseY = 0;

let xx = 0;
let yy = 0;

const circleRadius = 50;
const pupilRadius = 10;

const centerX = 100;
const centerY = 100;
const r = 30;
orbit(ctx);
canvas.addEventListener("mousemove", setMousePosition, false);

function setMousePosition(e) {
    mouseX = e.clientX;
    mouseY = e.clientY;
    apply();
    ctx.clearRect(0, 0, 1000, 1000);
    orbit(ctx);
    apply();
}

function orbit(ctx) {
    ctx.beginPath();
    ctx.arc(centerX, centerY, circleRadius, 0, 2 * Math.PI);
    ctx.stroke();

    ctx.beginPath();
    ctx.arc(2*centerX, centerY, circleRadius, 0, 2 * Math.PI);
    ctx.stroke();
    ctx.beginPath();
}

function apply() {
    countXY(mouseX - centerX, mouseY - centerY, r);
    ctx.arc(xx + centerX, yy + centerY, pupilRadius, 0, 2 * Math.PI);
    ctx.fillStyle = "black";
    ctx.fill();
    ctx.beginPath();

    countXY(mouseX - 2*centerX, mouseY - centerY, r);
    ctx.arc(xx + 2*centerX, yy + centerY, pupilRadius, 0, 2 * Math.PI);
    ctx.fillStyle = "black";
    ctx.fill();
    ctx.beginPath();
}

let x12;
let y12;
let x22;
let y22;

function countXY(x1, y1) {
    let distance = countDistanceToPoint(x1, y1);
    if (distance < r*r) {
        xx = x1;
        yy = y1;
    } else {
        let newDistance = calcSqrtDistance(distance);
        countPoints(x1, y1, newDistance);
        let buf1 = countDistanceBetweenPoints(x12, y12, newDistance);
        let buf2 = countDistanceBetweenPoints(x22, y22, newDistance);
        if (buf2 < buf1) {
            xx = x22;
            yy = y22;
        } else {
            xx = x12;
            yy = y12;
        }
    }
}

function countDistanceToPoint(x1, y1) {
    return x1*x1 + y1*y1;
}

function countFirstPointX(x1, distance) {
    return (x1 * r) / (distance);
}

function countFirstPointY(y1, distance) {
    return (y1 * r) / (distance);
}

function countDistanceBetweenPoints(x12, y12, x1, y1) {
    return ((x1 - x12) * (x1 - x12) + (y1 - y12) * (y1 - y12));
}

function countPoints(x1, y1, distance) {
    x12 = countFirstPointX(x1, distance);
    y12 = countFirstPointY(y1, distance);
    x22 = (-1) * x12;
    y22 = (-1) * y12;
}

function calcSqrtDistance(distance) {
    return Math.sqrt(distance);
}
