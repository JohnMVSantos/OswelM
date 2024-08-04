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
    
    draw(ctx,ref) {
        ctx.save();

        ctx.translate(ref.x,ref.y);
        this.#drawEye(ctx,Math.max(0,ref.xOffset),ref.yOffset,ref.xOffset*0.015);
        ctx.scale(-1, 1);
        this.#drawEye(ctx,Math.max(0,-ref.xOffset),ref.yOffset,-ref.xOffset*0.015);

        ctx.restore();
    }

    #drawEye(ctx,scaleX,scaleY,azimuthLook) {
        ctx.save();

        ctx.scale(1-scaleX*0.34,1-Math.abs(scaleY*0.20));

        // Shift downwards when looking left and right.
        const rangeLook = scaleX*0.008;
        
        // Eye sockets.
        ctx.beginPath();

        ctx.fillStyle=this.skinTone;
        ctx.moveTo(0.05,0.08+rangeLook);
        ctx.quadraticCurveTo(0.17,0.15+rangeLook,0.20,0.01+rangeLook*2);
        ctx.quadraticCurveTo(0.13,-0.04+rangeLook,0.05,0.06+rangeLook);
        ctx.lineTo(0.05,0.08+rangeLook);

        ctx.stroke();
        ctx.fill();
        
        // Iris.
        ctx.beginPath();

        ctx.fillStyle=this.irisColor;
        ctx.moveTo(0.09+azimuthLook*1.5,0.02-azimuthLook);
        ctx.bezierCurveTo(
            0.08+azimuthLook,0.13+rangeLook,
            0.175+azimuthLook,0.10+rangeLook,
            0.14+azimuthLook*1.5,-0.005+rangeLook);

        ctx.stroke();
        ctx.fill();
        
        // Pupil.
        ctx.beginPath();

        ctx.arc(0.12+azimuthLook*1.5,0.04,0.015-Math.abs(azimuthLook*0.3),0,Math.PI*2);
        ctx.fillStyle=this.pupilColor;

        ctx.stroke();
        ctx.fill();

        ctx.restore();
    }
}