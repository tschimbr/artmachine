var canvas = document.getElementById('artmachine');
var context = canvas.getContext('2d');

var x = canvas.width / 2;
var y = canvas.height / 2;

context.save();
context.beginPath();
context.arc(x, y, 402, 0, 2 * Math.PI, false);
context.clip();


context.lineWidth = 5;
context.strokeStyle = 'blue';
context.beginPath();
context.moveTo(100, 150);
context.lineTo(450, 50);
context.stroke();

var startAngle = 0.0 * Math.PI;
var endAngle = 2.0 * Math.PI;
var counterClockwise = false;

context.beginPath();
context.arc(x, y, 400, startAngle, endAngle, counterClockwise);
context.lineWidth = 3;

// line color
context.strokeStyle = 'black';
context.stroke();
