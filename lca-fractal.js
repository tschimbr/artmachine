var canvas = document.getElementById('artmachine');
var context = canvas.getContext('2d');

var x = canvas.width / 2;
var y = canvas.height / 2;

var radius = 400;
var width = 8;
var depth = 5;
var circles = {};
var numCircles = 0;

/* clipping */
context.save();
context.beginPath();
context.arc(x, y, radius + width/2-1, 0, 2 * Math.PI, false);
context.clip();

/* initialization */
context.strokeStyle = 'black';

drawCircle(x, y, radius, depth)

//alert(numCircles)

function drawCircle(cx, cy, cradius, cdepth){
	if(cdepth == 0) return;
	circle = Math.floor(cx*100) + "-" + Math.floor(cy*100) + "-" + Math.floor(cradius*100);
	if(circle in circles) return;
	if(Math.sqrt((cy - y)*(cy - y)+(cx - x)*(cx - x)) > cradius + radius) return;

	context.lineWidth = width / (Math.pow(2.0, depth-cdepth+1));

	drawCircleOnCanvas(cx, cy, cradius);
	drawCircleOnCanvas(cx, cy+cradius, cradius);
	drawCircleOnCanvas(cx, cy-cradius, cradius);
	var xlag = Math.sqrt(cradius*cradius - (cradius/2)*(cradius/2))
	drawCircleOnCanvas(cx+xlag, cy+cradius/2, cradius);
	drawCircleOnCanvas(cx+xlag, cy-cradius/2, cradius);
	drawCircleOnCanvas(cx-xlag, cy+cradius/2, cradius);
	drawCircleOnCanvas(cx-xlag, cy-cradius/2, cradius);

	
	drawCircle(cx, cy, cradius/2, cdepth - 1);
	drawCircle(cx, cy+cradius, cradius/2, cdepth - 1);
	drawCircle(cx, cy-cradius, cradius/2, cdepth - 1);
	drawCircle(cx+xlag, cy+cradius/2, cradius/2, cdepth - 1);
	drawCircle(cx+xlag, cy-cradius/2, cradius/2, cdepth - 1);
	drawCircle(cx-xlag, cy+cradius/2, cradius/2, cdepth - 1);
	drawCircle(cx-xlag, cy-cradius/2, cradius/2, cdepth - 1);

}

function drawCircleOnCanvas(cx, cy, cradius){
	circle = Math.floor(cx*100) + "-" + Math.floor(cy*100) + "-" + Math.floor(cradius*100);
	if(circle in circles) return;
	circles[circle] = numCircles++;
	context.beginPath();
	context.arc(cx, cy, cradius, 0.0, 2.0 * Math.PI, false);
	context.stroke();
}



