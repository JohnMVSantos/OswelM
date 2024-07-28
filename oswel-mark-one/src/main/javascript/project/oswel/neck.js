/* Oswell Application Front-End Avatar.
 * 
 * Copyright (C) 2024 John Santos <johnmarivsantos@gmail.com>
 */

class Neck{
    constructor(){

    }
    
    draw(ref,ctx) {
        ctx.beginPath();

        const xOffset = ref.x*0.085;
        const yOffset = (ref.y-0.58)*0.85;
        const adj = 3.5;
        
        // Bottom Chin
        ctx.moveTo(-0.04+xOffset*2.0,0.575+yOffset-xOffset*adj*0.35);
        ctx.quadraticCurveTo(-0.02,0.61,-0.01,0.64);
        ctx.lineTo(0.01,0.64);
        ctx.quadraticCurveTo(0.02,0.61,0.04+xOffset*2.0,0.575+yOffset+xOffset*adj*0.35);

        // Middle Portion
        ctx.moveTo(-0.01,0.64);
        ctx.quadraticCurveTo(-0.01,0.85,-0.01,0.88+yOffset);
        ctx.moveTo(0.01,0.64);
        ctx.quadraticCurveTo(0.01,0.85,0.01,0.88+yOffset);
        ctx.rect(-0.02,0.88+yOffset,0.04,0.06);

        ctx.stroke();

        ctx.save();
        this.#drawDetails(xOffset,yOffset+xOffset*adj,ctx);
        ctx.scale(-1, 1);
        this.#drawDetails(-xOffset,yOffset-xOffset*adj,ctx);
        ctx.restore();

    }

    #drawDetails(xOffset,yOffset,ctx){
        ctx.save();

        ctx.beginPath();
        // Cylinders Right
        ctx.moveTo(0.02+xOffset,0.62+yOffset*0.2);
        ctx.quadraticCurveTo(0.015,0.86,0.03,0.87);
        ctx.moveTo(0.03+xOffset,0.60+yOffset*0.2);
        ctx.quadraticCurveTo(0.025,0.86,0.04,0.85);
    
        ctx.moveTo(0.04,0.84);
        ctx.quadraticCurveTo(0.041,0.72,0.055+xOffset,0.56+yOffset);
        ctx.moveTo(0.030,0.74);
        ctx.lineTo(0.045+xOffset,0.57+yOffset);

        // Inner boundary.
        ctx.moveTo(0.07+xOffset,0.55+yOffset);
        ctx.quadraticCurveTo(0.05,0.70,0.09,0.80);

        ctx.moveTo(0.06+xOffset,0.55+yOffset);
        ctx.quadraticCurveTo(0.04,0.70,0.08,0.82);

        ctx.stroke();
        
        // Outer boundary.
        ctx.beginPath();
        ctx.moveTo(0.11+xOffset,0.47+yOffset*0.009);
        // Connection of top boundaries to head.
        ctx.quadraticCurveTo(0.105,0.45,0.09+xOffset,0.52+yOffset*0.009);

        ctx.moveTo(0.11+xOffset,0.47+yOffset*0.009)
        ctx.quadraticCurveTo(0.10,0.67,0.14,0.78);

        ctx.moveTo(0.09+xOffset,0.52+yOffset*0.009);
        ctx.quadraticCurveTo(0.09,0.66,0.13,0.80);
        ctx.stroke();

        // Outer boundary bottom connection.
        ctx.beginPath();
        ctx.moveTo(0.15,0.81);
        ctx.lineTo(0.145,0.76);
        ctx.quadraticCurveTo(0.14,0.80,0.125,0.80);
        ctx.lineTo(0.13,0.835);

        ctx.stroke();
        ctx.closePath();
        

        ctx.beginPath();

        // Metal Brackets Right
        ctx.moveTo(0.04,0.94);
        ctx.quadraticCurveTo(0.01,0.88,0.06,0.81);
        ctx.lineTo(0.08,0.85);
        ctx.quadraticCurveTo(0.06,0.89,0.07,0.94);
        ctx.lineTo(0.04,0.94);
        
        ctx.moveTo(0.08,0.85);
        ctx.quadraticCurveTo(0.09,0.84,0.10,0.89);
        ctx.moveTo(0.07,0.84);
        ctx.quadraticCurveTo(0.10,0.80,0.11,0.86);

        ctx.moveTo(0.09,0.82);
        ctx.lineTo(0.09,0.80);
        ctx.lineTo(0.08,0.82);
        ctx.lineTo(0.08,0.83);

        ctx.moveTo(0.09,0.79);
        ctx.lineTo(0.09,0.63);
        ctx.quadraticCurveTo(0.08,0.64,0.07,0.63);
        ctx.lineTo(0.07,0.74);

        ctx.moveTo(0.085,0.64);
        ctx.lineTo(0.085,0.53);
        ctx.moveTo(0.075,0.64);
        ctx.lineTo(0.075,0.53);

        ctx.stroke();

        this.#drawBolt(0.06,0.85,0.008,"silver",ctx);
        this.#drawBolt(0.05,0.88,0.008,"silver",ctx);
        this.#drawBolt(0.05,0.91,0.008,"silver",ctx);
        ctx.restore();
    }

    #drawBolt(centerX,centerY,radius,fill,ctx){
        ctx.beginPath();
        ctx.arc(centerX,centerY,radius,0,2*Math.PI);
        ctx.fillStyle=fill;
        ctx.fill();
        ctx.stroke();
    }
}