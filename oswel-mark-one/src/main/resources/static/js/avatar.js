/* Oswell Application Front-End Avatar.
 * 
 * Copyright (C) 2024 John Santos <johnmarivsantos@gmail.com>
 */

class Avatar{
    constructor(lookAt,skinTone){
        this.head=new Head(skinTone);
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
        const verticalSquish=1+(this.lookAt.yOffset*0.35);
        const bottomPoint={
            x:Math.min(0.075, this.lookAt.x),
            y:Math.min(
                this.lookAt.y+(0.625-Math.min(0,this.lookAt.yOffset)*0.30)*verticalSquish,
                0.60
            ),
        }

        this.body.draw(ctx,bottomPoint,this.lookAt.xOffset);
        this.neck.draw(ctx,bottomPoint);
        this.head.draw(ctx,topPoint,bottomPoint,this.lookAt);

        // this.leftWire.update(xTranslate,xScale);
        // this.leftWire.draw(ctx);
        // this.rightWire.update(xTranslate,xScale);
        // this.rightWire.draw(ctx);
        
        if(DEBUG){
            drawAxis(ctx,lookAt);
            drawPoint(ctx,this.lookAt,"A");
        }
    }
}