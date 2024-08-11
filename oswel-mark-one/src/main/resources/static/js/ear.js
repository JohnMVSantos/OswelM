/* Oswell Application Front-End Avatar.
 * 
 * Copyright (C) 2024 John Santos <johnmarivsantos@gmail.com>
 */

class Ear{
    constructor(){

    }

    draw(ctx,ref){
        ctx.save();

        ctx.translate(ref.x,ref.y);

        this.#drawEar(
            ctx,
            Math.max(0,ref.xOffset),
            ref.yOffset,
            Math.min(0,ref.yOffset),
            Math.min(0,ref.xOffset),
        );
        ctx.scale(-1, 1);
        this.#drawEar(
            ctx,
            Math.max(0,-ref.xOffset),
            ref.yOffset,
            Math.min(0,ref.yOffset),
            Math.min(0,-ref.xOffset),
        );

        ctx.restore();
    }

    #drawEar(ctx,scaleX,scaleY,verticalScaler,horizontalScaler){
        ctx.save();

        const horizontalSquish=1-scaleX*0.39;
        const verticalSquish=1-Math.abs(scaleY*0.20);
        ctx.scale(horizontalSquish,verticalSquish);

        const outerMostPoint={ 
            x: 0.255-horizontalScaler*0.06,
            y: 0.02-verticalScaler*0.03,
        };

        const innerMostPoint={ 
            x: 0.235+horizontalScaler*0.01,
            y: 0.02-verticalScaler*0.03,
        };

        ctx.beginPath();
        
        ctx.fillStyle="rgba(128,128,128,0.4)";
        // Top left point.
        ctx.moveTo(0.24,-0.05);
        // Top right point.
        ctx.quadraticCurveTo((innerMostPoint.x+outerMostPoint.x)/2,-0.065,outerMostPoint.x-0.005,-0.05);
        // Bottom right point.
        ctx.quadraticCurveTo(outerMostPoint.x,outerMostPoint.y,outerMostPoint.x-0.005,0.09);
        // Bottom left point.
        ctx.quadraticCurveTo((innerMostPoint.x+outerMostPoint.x)/2,0.105,innerMostPoint.x-0.005,0.09);
        // Top left point.
        ctx.quadraticCurveTo(innerMostPoint.x,innerMostPoint.y,0.24,-0.05);

        ctx.stroke();
        ctx.fill();

        ctx.beginPath();

        ctx.fillStyle="rgba(211,246,252,0.98)";
        // Top left point.
        ctx.moveTo(0.245,-0.03);
        // Top right point.
        ctx.quadraticCurveTo((innerMostPoint.x+outerMostPoint.x)/2,-0.050,outerMostPoint.x-0.005,-0.03);
        // Bottom right point.
        ctx.quadraticCurveTo(outerMostPoint.x,outerMostPoint.y,outerMostPoint.x-0.005,0.07);
        // Bottom left point.
        ctx.quadraticCurveTo((innerMostPoint.x+outerMostPoint.x)/2,0.09,innerMostPoint.x,0.07);
        // Top left point.
        ctx.quadraticCurveTo(innerMostPoint.x,innerMostPoint.y,0.245,-0.03);

        ctx.stroke();
        ctx.fill();

        ctx.restore();
        ctx.stroke();
    }
}