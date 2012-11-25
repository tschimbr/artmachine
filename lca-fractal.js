var canvas = document.getElementById('artmachine');
var context = canvas.getContext('2d');

context.lineWidth = 5;
context.strokeStyle = 'blue';
context.beginPath();
context.moveTo(100, 150);
context.lineTo(450, 50);
//context.stroke();


      var x = canvas.width / 2;
      var y = canvas.height / 2;
      var radius = 400;
      var startAngle = 0.0 * Math.PI;
      var endAngle = 2.0 * Math.PI;
      var counterClockwise = false;

      context.beginPath();
      context.arc(x, y, radius, startAngle, endAngle, counterClockwise);
      context.lineWidth = 3;

      // line color
      context.strokeStyle = 'black';
      context.stroke();
