/* Oswell Application Front-End Avatar.
 * 
 * Copyright (C) 2024 John Santos <johnmarivsantos@gmail.com>
 */

// Getting the mouseclick coordinates.
function getCoords(evt) {
    const x=(evt.x-canvas.width/2)/(canvas.width/2);
    const y=(evt.y-canvas.height/2)/(canvas.height/2)

    const offset = {
        x:x,
        y:y,
    }
    console.log(offset.x.toFixed(2)+","+offset.y.toFixed(2));
}

// Drawing MISC
function drawAxis(ctx,lookAt) {
    ctx.beginPath();

    // Draw the Axis References.
    ctx.strokeStyle = "lightgray";
    // Vertical Line
    ctx.moveTo(0.0, -0.80);
    ctx.quadraticCurveTo(lookAt.x,lookAt.y,0.0,0.60);
    // Horizontal Line
    ctx.moveTo(-0.25,0.0);
    ctx.quadraticCurveTo(lookAt.x,lookAt.y,0.25,0.0);

    ctx.stroke();
}

// Visualize the control points.
function drawPoint(ctx,loc,label,rad=0.04){
    ctx.beginPath();

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

// Image processing.
function getMarkedLocations(imgData, color=[0,0,255], threshold=150) {
    const locs=[];
    const data=imgData.data;
    for(let i=0;i<=data.length;i+=4){
        const r=data[i];
        const g=data[i+1];
        const b=data[i+2];
        if(match([r,g,b],color,threshold)){
            const pIndex=i/4;
            const y=Math.floor(pIndex/imgData.width);
            const x=pIndex%imgData.width;
            locs.push([x,y]);
        }
    }
    return locs;
}