

function drawHead() {

    ctx.save();
    drawBoundary(B.x, B.y, C.x, C.y);
    ctx.scale(-1,1);
    drawBoundary(-B.x, B.y, -C.x, C.y);
    ctx.restore();

    drawEyes(A);
    drawFaceDetails(A);
    drawNose(A);
}

function drawBoundary(bx, by, cx, cy) {
    ctx.beginPath();

    // Draw top portion
    ctx.moveTo(bx, by);
    ctx.quadraticCurveTo(0.09,-0.77,0.15,-0.70);
    ctx.lineTo(0.15,-0.63);
    ctx.quadraticCurveTo(0.28,-0.41,0.23, -0.040);
    // Draw bottom portion
    ctx.lineTo(0.23,0.06);
    ctx.quadraticCurveTo(0.21,0.18,0.15,0.30);
    ctx.quadraticCurveTo(0.14,0.40,0.10,0.49);
    ctx.quadraticCurveTo(0.07,0.58,cx, cy);

    ctx.stroke();
    ctx.closePath();
    ctx.fillStyle=skinTone;
    ctx.fill();

    ctx.beginPath();

    ctx.moveTo(0.15,0.30);
    ctx.lineTo(0.08+cx*0.15,0.50);  

    ctx.stroke();
}

function drawFaceDetails(ref) {
    ctx.save();
    ctx.translate(ref.x, ref.y+0.040);
    ctx.beginPath();

    ctx.moveTo(-0.02,-0.04);
    ctx.quadraticCurveTo(0.00,-0.05,0.02,-0.04);
    ctx.moveTo(-0.04,0.02)
    ctx.quadraticCurveTo(0.00,-0.01,0.04,0.02);

    ctx.stroke();

    drawFaceLines(ref.xOffset, ref.yOffset);
    ctx.scale(-1,1);
    drawFaceLines(-ref.xOffset, ref.yOffset);

    ctx.restore();
}

function drawFaceLines(xOffset, yOffset) {
    ctx.save();
    const scaleX = Math.max(0,xOffset);
    const scaleY = yOffset;
    ctx.scale(1-scaleX*0.55, 1-Math.abs(scaleY*0.5));

    ctx.beginPath();

    const outerMostPoint={
        x:0.15 - Math.min(xOffset*0.085, 0),
        y:-0.70 - Math.max(0, yOffset*0.65)
    }

    ctx.moveTo(outerMostPoint.x,outerMostPoint.y);
    ctx.lineTo(0.13,-0.41);
    ctx.quadraticCurveTo(0.11,-0.28,0.06,-0.17);
    ctx.quadraticCurveTo(0.06,-0.11,0.02,-0.04);
    ctx.lineTo(0.06,0.00);
    ctx.moveTo(0.04,0.06);
    ctx.quadraticCurveTo(0.11,0.17,0.18,0.23);
    ctx.restore();

    ctx.stroke();
}

function drawEyes(ref) {
    ctx.save();
    ctx.translate(ref.x, ref.y);
    drawEye(Math.max(0,ref.xOffset), ref.yOffset);
    ctx.scale(-1, 1);
    drawEye(Math.max(0,-ref.xOffset), ref.yOffset);
    ctx.restore();
}

function drawEye(scaleX, scaleY) {
    ctx.save();
    ctx.scale(1-scaleX*0.34,1-Math.abs(scaleY*0.20));
    ctx.beginPath();

    ctx.moveTo(0.05,0.08);
    ctx.quadraticCurveTo(0.17,0.15,0.20,0.01);
    ctx.quadraticCurveTo(0.13,-0.04,0.05,0.06);
    ctx.lineTo(0.05,0.08);

    ctx.stroke();
    
    ctx.beginPath();

    ctx.fillStyle="rgba(40,147,181,0.3)";
    ctx.moveTo(0.09,0.02);
    ctx.bezierCurveTo(0.08,0.13,0.175,0.10,0.14,-0.005);

    ctx.stroke();
    ctx.fill();
    
    ctx.beginPath();

    ctx.arc(0.12,0.04,0.015,0,Math.PI*2);
    ctx.fillStyle="rgba(211,246,252,0.3)";

    ctx.fill();
    ctx.restore();
}

function drawNose(ref) {
    ctx.save();
    ctx.scale(1-Math.abs(ref.xOffset)*0.34,1-Math.abs(ref.yOffset*0.20));
    ctx.translate(ref.x, ref.y);
    ctx.beginPath();

    ctx.moveTo(0.00,0.13);

    const tip = {
        x: ref.xOffset*0.12,
        y: 0.33
    }

    ctx.quadraticCurveTo(tip.x, tip.y, 0.00,0.335);
    ctx.lineTo(-0.045,0.315);
    ctx.moveTo(0.00,0.335);
    ctx.lineTo(0.045,0.315);

    ctx.stroke();

    ctx.beginPath();

    ctx.strokeStyle="white";
    ctx.moveTo(-0.045,0.315);
    ctx.quadraticCurveTo(-0.07,0.30,-0.05,0.26);
    ctx.moveTo(0.045,0.315);
    ctx.quadraticCurveTo(0.07,0.30,0.05,0.26);

    ctx.stroke();
    ctx.restore();
}