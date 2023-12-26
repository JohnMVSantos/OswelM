

function drawAxis() {
    ctx.beginPath();

    // Draw the Axis References.
    ctx.strokeStyle = "lightgray";
    // Vertical Line
    ctx.moveTo(B.x, B.y);
    ctx.quadraticCurveTo(A.x,A.y,C.x,C.y);
    // Horizontal Line
    ctx.moveTo(-0.25,-0.040);
    ctx.quadraticCurveTo(A.x,A.y,0.25,-0.040);

    ctx.stroke();
}

function drawPoint(loc,label,rad=0.04){
    ctx.beginPath();

    // Visualize the control points.
    ctx.arc(loc.x,loc.y,rad,0,Math.PI*2);
    ctx.stroke();
    ctx.fillStyle="green";
    ctx.fill();
    ctx.fillStyle="white";
    ctx.font=(rad*1.6)+"px Arial";
    ctx.textAlign="center";
    ctx.textBaseline="middle";
    ctx.fillText(label, loc.x, loc.y+rad*0.15);

    ctx.stroke();
}