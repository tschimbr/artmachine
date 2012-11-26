var canvas = document.getElementById('artmachine');
var context = canvas.getContext('2d');

var x = canvas.width / 2;
var y = canvas.height / 2;

var radius = 400;
var width = 15;
var depth = 5;
var circleskeys = {};
var circles = [];
var numCircles = 0;

/* clipping */
context.save();
context.beginPath();
context.arc(x, y, radius + width/2-5, 0, 2 * Math.PI, false);
context.clip();

context.strokeStyle = 'gray';
/* circles. */
drawCircle(x, y, radius, depth)

/* arc drawing. */
context.strokeStyle = 'black';
var arcs = [];
arcs[0] = {'circle' : 0, 'width' : 6, 'start' : 0.0, 'end' : 1.5 * Math.PI};
arcs[1] = {'circle' : 145, 'width' : 2, 'start' : Math.PI, 'end' : 1.5 * Math.PI};
for(var i in arcs){
	arc = arcs[i];
	circle = circles[arc.circle];
	//alert(JSON.stringify(circle, undefined, 2));
	//alert(JSON.stringify(arc, undefined, 2));
	context.lineWidth = arc.width;
	context.beginPath();
	context.arc(circle.x, circle.y, circle.r, arc.start, arc.end, false);
	context.stroke();
}

//alert(JSON.stringify(circles[0], undefined, 2));

function drawCircle(cx, cy, cradius, cdepth){
	if(cdepth == 0) return;
	circle = Math.floor(cx*100) + "-" + Math.floor(cy*100) + "-" + Math.floor(cradius*100);
	if(circle in circleskeys) return;
	if(Math.sqrt((cy - y)*(cy - y)+(cx - x)*(cx - x)) > cradius + radius) return;

	context.lineWidth = width / (Math.pow(2.0, depth-cdepth+2));

	drawCircleOnCanvas(cx, cy, cradius, cdepth);
	drawCircleOnCanvas(cx, cy+cradius, cradius, cdepth);
	drawCircleOnCanvas(cx, cy-cradius, cradius, cdepth);
	var xlag = Math.sqrt(cradius*cradius - (cradius/2)*(cradius/2), cdepth)
	drawCircleOnCanvas(cx+xlag, cy+cradius/2, cradius, cdepth);
	drawCircleOnCanvas(cx+xlag, cy-cradius/2, cradius, cdepth);
	drawCircleOnCanvas(cx-xlag, cy+cradius/2, cradius, cdepth);
	drawCircleOnCanvas(cx-xlag, cy-cradius/2, cradius, cdepth);

	
	drawCircle(cx, cy, cradius/2, cdepth - 1);
	drawCircle(cx, cy+cradius, cradius/2, cdepth - 1);
	drawCircle(cx, cy-cradius, cradius/2, cdepth - 1);
	drawCircle(cx+xlag, cy+cradius/2, cradius/2, cdepth - 1);
	drawCircle(cx+xlag, cy-cradius/2, cradius/2, cdepth - 1);
	drawCircle(cx-xlag, cy+cradius/2, cradius/2, cdepth - 1);
	drawCircle(cx-xlag, cy-cradius/2, cradius/2, cdepth - 1);

}

function drawCircleOnCanvas(cx, cy, cradius, cdepth){
	circle = Math.floor(cx*100) + "-" + Math.floor(cy*100) + "-" + Math.floor(cradius*100);
	if(circle in circleskeys) return;
	c = {};
	c['x'] = cx;
	c['y'] = cy;
	c['r'] = cradius;
	c['depth'] = cdepth;
	circles[numCircles] = c;
	circleskeys[circle] = numCircles++;
	context.beginPath();
	context.arc(cx, cy, cradius, 0.0, 2.0 * Math.PI, false);
	context.stroke();
}


