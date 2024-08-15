/* Oswell Application Front-End Avatar.
 * 
 * Copyright (C) 2024 John Santos <johnmarivsantos@gmail.com>
 */

class Neck{
    constructor(skinTone){
        this.skinTone=skinTone;
    }
    
    draw(ctx,ref) {
        const xOffset = ref.x*0.085;
        const yOffset = (ref.y-0.58)*0.85;
        const adj = 3.5;
        
        // Bottom Chin
        ctx.beginPath();

        ctx.fillStyle="rgba(128,128,128,0.8)";
        ctx.moveTo(-0.04+xOffset*2.0,0.57+yOffset-xOffset*adj*0.35);
        ctx.quadraticCurveTo(-0.02,0.61,-0.01,0.64);
        ctx.lineTo(0.01,0.64);
        ctx.quadraticCurveTo(0.02,0.61,0.04+xOffset*2.0,0.57+yOffset+xOffset*adj*0.35);
        ctx.quadraticCurveTo(0.00,0.58+yOffset,-0.04+xOffset*2.0,0.57+yOffset-xOffset*adj*0.35);
        
        ctx.stroke();
        ctx.fill();

        // Middle Portion
        ctx.beginPath();

        ctx.fillStyle="rgba(128,128,128,0.7)";
        ctx.moveTo(-0.01,0.64);
        ctx.quadraticCurveTo(-0.01,0.85,-0.01,0.88+yOffset);
        ctx.lineTo(0.01,0.88+yOffset);
        ctx.quadraticCurveTo(0.01,0.85,0.01,0.64);
        ctx.lineTo(-0.01,0.64);

        ctx.stroke();
        ctx.fill();

        ctx.beginPath();

        ctx.fillStyle=this.skinTone;
        ctx.moveTo(-0.02,0.88+yOffset);
        ctx.lineTo(0.02,0.88+yOffset);
        ctx.lineTo(0.02,0.94+yOffset);
        ctx.lineTo(-0.02,0.94+yOffset);
        ctx.lineTo(-0.02,0.88+yOffset);

        ctx.stroke();
        ctx.fill();

        // Side reflections.
        ctx.save();

        this.#drawDetails(ctx,xOffset,yOffset+xOffset*adj);
        ctx.scale(-1, 1);
        this.#drawDetails(ctx,-xOffset,yOffset-xOffset*adj);

        ctx.restore();

    }

    #drawDetails(ctx,xOffset,yOffset){
        ctx.save();

        // Outer boundary.
        ctx.beginPath();

        ctx.fillStyle="rgba(40,147,181,0.9)";
        ctx.moveTo(0.11+xOffset,0.47+yOffset*0.009);
        ctx.quadraticCurveTo(0.10,0.67,0.14,0.78);
        ctx.quadraticCurveTo(0.14,0.79,0.13,0.80);
        ctx.quadraticCurveTo(0.09,0.66,0.09+xOffset,0.52+yOffset*0.009);
        ctx.quadraticCurveTo(0.105,0.45,0.11+xOffset,0.47+yOffset*0.009);
        
        ctx.stroke();
        ctx.fill();

        // Outer boundary bottom connection.
        ctx.beginPath();
        
        ctx.fillStyle="rgba(11,9,43,0.8)";
        ctx.moveTo(0.15,0.825);
        ctx.lineTo(0.145,0.76);
        ctx.quadraticCurveTo(0.14,0.80,0.125,0.80);
        ctx.lineTo(0.13,0.843);
        ctx.quadraticCurveTo(0.14,0.826,0.15,0.825);

        ctx.stroke();
        ctx.fill();

        // Hydraulics bottom.
        ctx.beginPath();

        ctx.fillStyle="rgba(128,128,128,0.9)";
        ctx.moveTo(0.088,0.82);
        ctx.lineTo(0.09,0.63);
        ctx.quadraticCurveTo(0.08,0.64,0.07,0.63);
        ctx.lineTo(0.07,0.74);
        ctx.quadraticCurveTo(0.08,0.77,0.088,0.82);

        ctx.stroke();
        ctx.fill();

        // Hydraulics top.
        ctx.beginPath();

        ctx.fillStyle="rgba(192,192,192,0.9)";
        ctx.moveTo(0.085,0.64);
        ctx.lineTo(0.085,0.52+yOffset);
        ctx.lineTo(0.075,0.53+yOffset);
        ctx.lineTo(0.075,0.64);
        ctx.quadraticCurveTo(0.080,0.63,0.085,0.64);

        ctx.stroke();
        ctx.fill();

        // Red wire.
        ctx.beginPath();
        
        ctx.fillStyle="rgba(166,49,30,0.8)";
        ctx.moveTo(0.07+xOffset,0.54+yOffset);
        ctx.quadraticCurveTo(0.05,0.70,0.09,0.825);
        ctx.lineTo(0.08,0.83);
        ctx.quadraticCurveTo(0.04,0.70,0.06+xOffset,0.55+yOffset);
        ctx.quadraticCurveTo(0.065,0.545,0.07+xOffset,0.54+yOffset);

        ctx.stroke();
        ctx.fill();

        // Black wire.
        ctx.beginPath();

        ctx.fillStyle="rgba(20,6,4,0.8)";
        ctx.moveTo(0.04,0.84);
        ctx.quadraticCurveTo(0.041,0.72,0.055+xOffset,0.56+yOffset);
        ctx.lineTo(0.045+xOffset,0.57+yOffset);
        ctx.lineTo(0.030,0.74);
        ctx.quadraticCurveTo(0.025,0.81,0.035,0.85);

        ctx.stroke();
        ctx.fill();

        // Yellow wire.
        ctx.beginPath();

        ctx.fillStyle="rgba(150,149,25,0.8)";
        ctx.moveTo(0.02+xOffset,0.62+yOffset*0.2);
        ctx.quadraticCurveTo(0.015,0.86,0.03,0.87);
        ctx.lineTo(0.04,0.85);
        ctx.quadraticCurveTo(0.025,0.86,0.03+xOffset,0.60+yOffset*0.2);
        ctx.quadraticCurveTo(0.02,0.61,0.02+xOffset,0.62+yOffset*0.2);

        ctx.stroke();
        ctx.fill();

        ctx.restore();
    }

}