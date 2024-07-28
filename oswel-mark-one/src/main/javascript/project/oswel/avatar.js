/* Oswell Application Front-End Avatar.
 * 
 * Copyright (C) 2024 John Santos <johnmarivsantos@gmail.com>
 */

class Avatar{
    constructor(lookAt,skinTone){
        this.complexions=new Complexions();
        this.eye=new Eye(skinTone);
        this.nose=new Nose();
        this.mouth=new Mouth();
        this.neck=new Neck(skinTone);
        this.body=new Body(skinTone);
        this.lookAt=lookAt;
        this.skinTone=skinTone;

        this.leftWire=new Pendulum({x:-0.37, y:0.83});
        this.rightWire=new Pendulum({x:+0.37, y:0.83});   
    }

    draw(ctx){
        ctx.strokeStyle="rgb(76,78,80)";

        const topPoint={
            x:Math.min(0.030, this.lookAt.x),
            y:Math.min(-0.785, this.lookAt.y-0.730)
        }
        // Version 1
        // const verticalSquish=1-Math.abs(this.lookAt.yOffset*0.2);
        // const bottomPoint={
        //     x:Math.min(0.075, this.lookAt.x),
        //     y:this.lookAt.y+(0.625-Math.min(0,this.lookAt.yOffset)*0.28)*verticalSquish,
        // }
        // Version 2: More range of motion in the y-axis.
        const verticalSquish=1+(this.lookAt.yOffset*0.35);
        const bottomPoint={
            x:Math.min(0.075, this.lookAt.x),
            y:Math.min(this.lookAt.y+(0.625-Math.min(0,this.lookAt.yOffset)*0.30)*verticalSquish,0.60),
        }

        // Slight movements to the body left/right.
        ctx.save();
        const xTranslate=this.lookAt.xOffset*0.005;
        const xScale=1-Math.abs(this.lookAt.xOffset)*0.04
        ctx.translate(xTranslate,0);
        ctx.scale(xScale,1);
        this.body.draw(ctx,bottomPoint);
        ctx.restore();

        this.#drawHead(ctx,topPoint,bottomPoint);

        this.leftWire.update(xTranslate,xScale);
        this.leftWire.draw(ctx);
        this.rightWire.update(xTranslate,xScale);
        this.rightWire.draw(ctx);
        
        if(DEBUG){
            drawAxis(lookAt);
            drawPoint(this.lookAt, "A");
        }
    }

    #drawHead(ctx,topPoint,bottomPoint) {
        ctx.save();

        this.neck.draw(bottomPoint,ctx);
        this.#drawBoundary(topPoint.x,topPoint.y,bottomPoint.x,bottomPoint.y,ctx);
        ctx.scale(-1,1);
        this.#drawBoundary(-topPoint.x,topPoint.y,-bottomPoint.x,bottomPoint.y,ctx);
        
        ctx.restore();

        this.eye.draw(this.lookAt,ctx);
        this.complexions.drawFaceDetails(this.lookAt,ctx);
        this.nose.draw(this.lookAt,ctx);
        this.mouth.draw(this.lookAt,ctx);
    }

    #drawBoundary(topX,topY,bottomX,bottomY,ctx) {
        ctx.beginPath();

        // Draw top portion
        ctx.moveTo(topX,topY);
        ctx.quadraticCurveTo(0.09,-0.77,0.15,-0.70);
        ctx.lineTo(0.15,-0.63);
        ctx.quadraticCurveTo(0.28,-0.41,0.23,-0.040);
        // Draw bottom portion
        ctx.lineTo(0.23,0.06);
        ctx.quadraticCurveTo(0.21,0.18,0.15,0.30);
        ctx.quadraticCurveTo(0.14,0.40,0.10,0.49);
        ctx.quadraticCurveTo(0.07,bottomY-0.015,bottomX,bottomY); // 0.58 bezier y.

        ctx.stroke();
        ctx.closePath();

        ctx.fillStyle=this.skinTone;
        ctx.fill();

        ctx.beginPath();

        ctx.moveTo(0.15,0.30);
        ctx.lineTo(0.08+bottomX*0.15,0.50);  

        ctx.stroke();
    }
}