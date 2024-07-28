/* Oswell Application Front-End Avatar.
 * 
 * Copyright (C) 2024 John Santos <johnmarivsantos@gmail.com>
 */

class Eye{
    constructor(skinTone) {
        this.skinTone=skinTone;
        this.pupilColor="rgba(211,246,252,0.3)";
        this.irisColor="rgba(40,147,181,0.3)";
    }
    
    draw(ref,ctx) {
        ctx.save();
        ctx.translate(ref.x,ref.y);
        this.#drawEye(Math.max(0,ref.xOffset),ref.yOffset,ctx);
        ctx.scale(-1, 1);
        this.#drawEye(Math.max(0,-ref.xOffset),ref.yOffset,ctx);
        ctx.restore();
    }

    #drawEye(scaleX,scaleY,ctx) {
        ctx.save();
        ctx.scale(1-scaleX*0.34,1-Math.abs(scaleY*0.20));

        // Eye sockets.
        ctx.beginPath();

        ctx.fillStyle=this.skinTone;
        ctx.moveTo(0.05,0.08);
        ctx.quadraticCurveTo(0.17,0.15,0.20,0.01);
        ctx.quadraticCurveTo(0.13,-0.04,0.05,0.06);
        ctx.lineTo(0.05,0.08);

        ctx.stroke();
        ctx.fill();
        
        // Iris.
        ctx.beginPath();

        ctx.fillStyle=this.irisColor;
        ctx.moveTo(0.09,0.02);
        ctx.bezierCurveTo(0.08,0.13,0.175,0.10,0.14,-0.005);

        ctx.stroke();
        ctx.fill();
        
        // Pupil.
        ctx.beginPath();

        ctx.arc(0.12,0.04,0.015,0,Math.PI*2);
        ctx.fillStyle=this.pupilColor;

        ctx.stroke();
        ctx.fill();

        ctx.restore();
    }
}