var canvas = document.getElementById('artmachine');
var context = canvas.getContext('2d');

var x = canvas.width / 2;
var y = canvas.height / 2;

var radius = 400;
var width = 8;
var depth = 5;

/* clipping */
context.save();
context.beginPath();
context.arc(x, y, radius + width/2-1, 0, 2 * Math.PI, false);
context.clip();

/* initialization */
context.strokeStyle = 'black';

drawCircle(x, y, radius, depth)


function drawCircle(cx, cy, cradius, cdepth){
	if(cdepth == 0) return;
	if((cy - y) < 0 && cy + cradius < y - radius) return;
	if((cy - y) > 0 && cy - cradius > y + radius) return;
	if((cx - x) < 0 && cx + cradius < x - radius) return;
	if((cx - x) > 0 && cx - cradius > x + radius) return;

	context.lineWidth = width / (Math.pow(2.0, depth-cdepth+1));

	context.beginPath();
	context.arc(cx, cy, cradius, 0.0, 2.0 * Math.PI, false);
	context.stroke();

	context.beginPath();
	context.arc(cx, cy+cradius, cradius, 0.0, 2.0 * Math.PI, false);
	context.stroke();

	context.beginPath();
	context.arc(cx, cy-cradius, cradius, 0.0, 2.0 * Math.PI, false);
	context.stroke();
	var xlag = Math.sqrt(cradius*cradius - (cradius/2)*(cradius/2))
	context.beginPath();
	context.arc(cx+xlag, cy+cradius/2, cradius, 0.0, 2.0 * Math.PI, false);
	context.stroke();

	context.beginPath();
	context.arc(cx+xlag, cy-cradius/2, cradius, 0.0, 2.0 * Math.PI, false);
	context.stroke();

	context.beginPath();
	context.arc(cx-xlag, cy+cradius/2, cradius, 0.0, 2.0 * Math.PI, false);
	context.stroke();

	context.beginPath();
	context.arc(cx-xlag, cy-cradius/2, cradius, 0.0, 2.0 * Math.PI, false);
	context.stroke();

	
	drawCircle(cx, cy, cradius/2, cdepth - 1);
	drawCircle(cx, cy+cradius, cradius/2, cdepth - 1);
	drawCircle(cx, cy-cradius, cradius/2, cdepth - 1);
	drawCircle(cx+xlag, cy+cradius/2, cradius/2, cdepth - 1);
	drawCircle(cx+xlag, cy-cradius/2, cradius/2, cdepth - 1);
	drawCircle(cx-xlag, cy+cradius/2, cradius/2, cdepth - 1);
	drawCircle(cx-xlag, cy-cradius/2, cradius/2, cdepth - 1);

}



