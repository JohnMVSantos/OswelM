/* Oswell Application Front-End Avatar.
 * 
 * Copyright (C) 2024 John Santos <johnmarivsantos@gmail.com>
 */

class Body{
    constructor(skinTone){
        this.skinTone=skinTone;
    }
    
    draw(ctx,bottomPoint,xOffset){
        ctx.save();

        // Slight movements to the body left/right.
        const xTranslate=xOffset*0.005;
        const xScale=1-Math.abs(xOffset)*0.04
        ctx.translate(xTranslate,0);
        ctx.scale(xScale*1.1,1);

        ctx.save();

        this.#drawBackground(ctx,bottomPoint);

        ctx.restore();

        ctx.save();

        this.#drawDetails(ctx);
        ctx.scale(-1, 1);
        this.#drawDetails(ctx);

        ctx.restore();

        ctx.restore();
    }

    #drawDetails(ctx){
        ctx.save();
    
        // Chest plate (main).
        ctx.beginPath();

        ctx.fillStyle="rgba(192,192,192,0.95)";
        ctx.moveTo(0.09,0.95);
        ctx.quadraticCurveTo(0.10,0.83,0.19,0.79);
        ctx.lineTo(0.20,0.84);
        ctx.quadraticCurveTo(0.24,0.80,0.25,0.75);
        ctx.lineTo(0.26,0.75);
        
        // Chest plate (main) border.
        ctx.quadraticCurveTo(0.23,0.84,0.21,0.88);
        ctx.quadraticCurveTo(0.20,0.86,0.18,0.87);
        ctx.quadraticCurveTo(0.17,0.79,0.11,0.95);
        ctx.lineTo(0.09,0.95);

        ctx.stroke();
        ctx.fill();

        // Chest plate (middle).
        ctx.beginPath();

        ctx.fillStyle="rgba(128,128,128,0.9)";
        ctx.moveTo(0.21,0.88);
        ctx.quadraticCurveTo(0.19,0.83,0.14,0.95);
        ctx.lineTo(0.11,0.95);
        ctx.quadraticCurveTo(0.17,0.79,0.18,0.87);

        ctx.stroke();
        ctx.fill();

        // Chest plate (outer).
        ctx.beginPath();

        ctx.fillStyle="rgba(192,192,192,1.0)";
        ctx.moveTo(0.21,0.88);
        ctx.quadraticCurveTo(0.19,0.92,0.17,0.95);
        ctx.lineTo(0.14,0.95);
        ctx.quadraticCurveTo(0.185,0.86,0.18,0.87);
        
        ctx.stroke();
        ctx.fill();

        // Disc (inner).
        ctx.beginPath();

        ctx.fillStyle="rgba(20,120,200,0.1.0)";
        ctx.moveTo(0.195,0.816);
        ctx.lineTo(0.217,0.820);
        ctx.lineTo(0.20,0.84);
        ctx.lineTo(0.195,0.816);

        ctx.stroke();
        ctx.fill();

        // Disc (middle).
        ctx.beginPath();
        
        ctx.fillStyle="rgba(30,130,190,0.95)";
        ctx.moveTo(0.19,0.80);
        ctx.quadraticCurveTo(0.21,0.79,0.225,0.805);
        ctx.lineTo(0.217,0.820);
        ctx.lineTo(0.195,0.816);
        ctx.lineTo(0.19,0.80);

        ctx.stroke();
        ctx.fill();

        // Disc (outer).
        ctx.beginPath();

        ctx.fillStyle="rgba(35,140,185,0.90)";
        ctx.moveTo(0.24,0.78);
        ctx.quadraticCurveTo(0.19,0.75,0.17,0.80);
        ctx.lineTo(0.19,0.79);
        ctx.lineTo(0.19,0.80);
        ctx.quadraticCurveTo(0.21,0.79,0.225,0.805);
        ctx.quadraticCurveTo(0.23,0.80,0.24,0.78);

        ctx.stroke();
        ctx.fill();

        // Shoulder shell 1 (inner).
        ctx.beginPath();

        ctx.fillStyle=this.skinTone;
        ctx.moveTo(0.17,0.95);
        ctx.quadraticCurveTo(0.24,0.84,0.28,0.68);
        ctx.lineTo(0.29,0.68);
        ctx.quadraticCurveTo(0.30,0.69,0.30,0.73);
        ctx.quadraticCurveTo(0.28,0.82,0.21,0.95);
        ctx.lineTo(0.17,0.95);

        ctx.stroke();
        ctx.fill();

        // Shoulder shell 2 (middle).
        ctx.beginPath();

        ctx.fillStyle="rgba(128,128,128,0.90)";
        ctx.moveTo(0.25,0.88);
        ctx.lineTo(0.26,0.91);
        ctx.quadraticCurveTo(0.30,0.83,0.30,0.74);
        ctx.quadraticCurveTo(0.28,0.82,0.25,0.88);

        ctx.stroke();
        ctx.fill();

        ctx.beginPath();

        ctx.fillStyle=this.skinTone;
        ctx.moveTo(0.26,0.91);
        ctx.quadraticCurveTo(0.30,0.83,0.30,0.74);
        ctx.quadraticCurveTo(0.33,0.76,0.33,0.77);
        ctx.quadraticCurveTo(0.315,0.84,0.26,0.95);
        ctx.lineTo(0.24,0.90);
        ctx.lineTo(0.25,0.88);

        ctx.stroke();
        ctx.fill();

        ctx.beginPath();

        ctx.fillStyle="rgba(128,128,128,0.90)";
        ctx.moveTo(0.26,0.95);
        ctx.lineTo(0.245,0.95);
        ctx.lineTo(0.232,0.92);
        ctx.lineTo(0.24,0.90);
        ctx.lineTo(0.26,0.95);
        
        ctx.stroke();
        ctx.fill();

        // Shoulder shell 3 (outside).
        ctx.beginPath();

        ctx.fillStyle="rgba(128,128,128,0.95)";
        ctx.moveTo(0.26,0.95);
        ctx.quadraticCurveTo(0.34,0.84,0.355,0.76);
        ctx.quadraticCurveTo(0.34,0.78,0.33,0.77);        
        ctx.quadraticCurveTo(0.32,0.82,0.26,0.95);

        ctx.stroke();
        ctx.fill();

        ctx.beginPath();

        ctx.fillStyle=this.skinTone;
        ctx.moveTo(0.355,0.76);
        ctx.quadraticCurveTo(0.38,0.79,0.38,0.81);
        ctx.quadraticCurveTo(0.35,0.88,0.31,0.95);
        ctx.lineTo(0.26,0.95);
        ctx.quadraticCurveTo(0.34,0.84,0.355,0.76);

        ctx.stroke();
        ctx.fill();

        // Shoulder shell 4.
        ctx.beginPath();

        ctx.fillStyle="rgba(192,192,192,0.8)";
        ctx.moveTo(0.38,0.81);
        ctx.quadraticCurveTo(0.40,0.87,0.39,0.90);
        ctx.quadraticCurveTo(0.36,0.92,0.31,0.95);
        ctx.quadraticCurveTo(0.35,0.88,0.38,0.81);

        ctx.stroke();
        ctx.fill();

        // Shoulder shell 5.
        ctx.beginPath();

        ctx.moveTo(0.31,0.95);
        ctx.lineTo(0.38,0.94);
        ctx.quadraticCurveTo(0.39,0.92,0.39,0.90);
        ctx.lineTo(0.31,0.95);

        ctx.stroke();
        ctx.fill();

        // Collar bone 1 (top/main).
        ctx.beginPath();

        ctx.fillStyle="rgba(128,128,128,0.0.95)";
        ctx.moveTo(0.265,0.73);
        ctx.lineTo(0.25,0.70);
        ctx.quadraticCurveTo(0.18,0.67,0.10,0.59);
        ctx.quadraticCurveTo(0.09,0.605,0.10,0.62);
        ctx.quadraticCurveTo(0.18,0.68,0.24,0.71);
        ctx.lineTo(0.26,0.745);
        ctx.lineTo(0.265,0.73);

        ctx.stroke();
        ctx.fill()
    
        // Collar bone 2 (middle).
        ctx.beginPath();

        ctx.fillStyle="rgba(128,128,128,0.95)";
        ctx.moveTo(0.25,0.75);
        ctx.lineTo(0.23,0.725);
        ctx.quadraticCurveTo(0.18,0.73,0.14,0.70);
        ctx.quadraticCurveTo(0.12,0.67,0.11,0.63); //
        ctx.lineTo(0.10,0.62); //
        ctx.quadraticCurveTo(0.11,0.66,0.12,0.69);
        ctx.quadraticCurveTo(0.13,0.71,0.14,0.715);
        ctx.quadraticCurveTo(0.17,0.74,0.225,0.745);
        ctx.lineTo(0.245,0.77);
        ctx.lineTo(0.25,0.75);

        ctx.stroke();
        ctx.fill();
        
        // Collar bone 3 (smallest).
        ctx.beginPath();

        ctx.fillStyle="rgba(128,128,128,0.95)";
        ctx.moveTo(0.19,0.77);
        ctx.quadraticCurveTo(0.15,0.75,0.14,0.72);
        ctx.lineTo(0.123,0.70);
        ctx.quadraticCurveTo(0.15,0.77,0.18,0.78);
        ctx.lineTo(0.19,0.77);
        
        ctx.stroke();
        ctx.fill();

        // Metal bracket.
        ctx.beginPath();

        ctx.fillStyle=this.skinTone;
        ctx.moveTo(0.04,0.94);
        ctx.quadraticCurveTo(0.01,0.88,0.06,0.81);
        ctx.lineTo(0.08,0.85);
        ctx.quadraticCurveTo(0.06,0.89,0.07,0.94);
        ctx.lineTo(0.04,0.94);

        ctx.stroke();
        ctx.fill();
        
        ctx.beginPath();

        ctx.fillStyle="rgba(11,9,43,1.0)";
        ctx.moveTo(0.085,0.85);
        ctx.quadraticCurveTo(0.09,0.84,0.10,0.89);
        ctx.lineTo(0.11,0.86);
        ctx.quadraticCurveTo(0.10,0.80,0.075,0.84);
        ctx.lineTo(0.085,0.85);

        ctx.stroke();
        ctx.fill();

        this.#drawBolt(0.06,0.85,0.008,"silver",ctx);
        this.#drawBolt(0.05,0.88,0.008,"silver",ctx);
        this.#drawBolt(0.05,0.91,0.008,"silver",ctx);

        ctx.restore();
    }

    #drawBackground(ctx,bottomPoint){
        ctx.beginPath();

        ctx.fillStyle="rgba(13,9,54,0.6)";
        ctx.moveTo(0.10,0.47);
        ctx.quadraticCurveTo(0.10,0.51,0.10,0.59);
        ctx.quadraticCurveTo(0.18,0.67,0.25,0.70);
        ctx.lineTo(0.265,0.73);
        ctx.lineTo(0.28,0.68);
        ctx.lineTo(0.29,0.68);
        ctx.quadraticCurveTo(0.305,0.71,0.30,0.74);
        ctx.lineTo(0.33,0.77);
        ctx.quadraticCurveTo(0.34,0.78,0.36,0.76);
        ctx.quadraticCurveTo(0.38,0.79,0.38,0.81);
        ctx.quadraticCurveTo(0.35,0.88,0.31,0.95);

        ctx.lineTo(-0.31,0.95);
        ctx.quadraticCurveTo(-0.35,0.88,-0.38,0.81);
        ctx.quadraticCurveTo(-0.38,0.79,-0.36,0.76);
        ctx.quadraticCurveTo(-0.34,0.78,-0.33,0.77);
        ctx.lineTo(-0.30,0.74);
        ctx.quadraticCurveTo(-0.305,0.71,-0.29,0.68);
        ctx.lineTo(-0.28,0.68);
        ctx.lineTo(-0.265,0.73);
        ctx.lineTo(-0.25,0.70);
        ctx.quadraticCurveTo(-0.18,0.67,-0.10,0.59);
        ctx.quadraticCurveTo(-0.10,0.51,-0.10,0.47);
        
        const yOffset = 1.225 + Math.log(bottomPoint.y);
        ctx.quadraticCurveTo(bottomPoint.x*0.7,yOffset,0.10,0.47);
        
        ctx.stroke();
        ctx.fill();
    }

    #drawBolt(centerX,centerY,radius,fill,ctx){
        ctx.beginPath();
        
        ctx.fillStyle=fill;
        ctx.arc(centerX,centerY,radius,0,2*Math.PI);

        ctx.stroke();
        ctx.fill();
    }
}