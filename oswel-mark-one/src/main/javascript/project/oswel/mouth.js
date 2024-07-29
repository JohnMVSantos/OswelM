/* Oswell Application Front-End Avatar.
 * 
 * Copyright (C) 2024 John Santos <johnmarivsantos@gmail.com>
 */

class Mouth{
    constructor(){
        this.x=0;
        this.y=0;
    }

    draw(ref,ctx){
        ctx.save();

        ctx.scale(1-Math.abs(ref.xOffset)*0.34,1-Math.abs(ref.yOffset*0.20));
        ctx.translate(ref.x,ref.y);

        // tip is at 0.08.
        const mouthCorner={
            x:-ref.xOffset*0.01,
        }

        const mouthStretch={
            x:this.x*0.02,
            y:this.y*0.07,
        }

        // Middle lip (moving).
        ctx.beginPath();

        ctx.strokeStyle="grey";
        ctx.fillStyle="rgba(128,128,128,0.4)";

        // bottom.
        ctx.moveTo(-0.08+mouthCorner.x-mouthStretch.x,0.43);
        ctx.quadraticCurveTo(-0.01,0.42+mouthStretch.y,0.00,0.440+mouthStretch.y*0.50);
        ctx.quadraticCurveTo(0.01,0.42+mouthStretch.y,0.08+mouthCorner.x+mouthStretch.x,0.43);
        
        // top.
        ctx.quadraticCurveTo(0.01,0.42,0.00,0.440-mouthStretch.y*0.02/0.07);
        ctx.quadraticCurveTo(-0.01,0.42,-0.08+mouthCorner.x-mouthStretch.x,0.43);
        
        ctx.stroke();
        ctx.fill();

        // Bottom Lip
        ctx.beginPath();

        ctx.strokeStyle="lightgrey";
        ctx.moveTo(-0.08+mouthCorner.x-mouthStretch.x,0.43);
        ctx.quadraticCurveTo(0.00,0.50+this.y*0.07,0.08+mouthCorner.x+mouthStretch.x,0.43);

        ctx.stroke();

        // Top Lip
        ctx.beginPath();

        ctx.moveTo(-0.08+mouthCorner.x-mouthStretch.x,0.43);
        ctx.lineTo(-0.01,0.40);
        ctx.lineTo(0.00,0.42);

        ctx.lineTo(0.01,0.40);
        ctx.lineTo(0.08+mouthCorner.x+mouthStretch.x,0.43);

        ctx.stroke();

        // Nose Connection
        ctx.beginPath();

        ctx.moveTo(-0.015,0.40);
        ctx.quadraticCurveTo(-0.0151,0.385,-0.01,0.34);
        ctx.moveTo(0.015,0.40);
        ctx.quadraticCurveTo(0.0151,0.385,0.01,0.34);
        
        ctx.stroke();
        ctx.restore();
    }

}