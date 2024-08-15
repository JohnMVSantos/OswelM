/* Oswell Application Front-End Avatar.
 * 
 * Copyright (C) 2024 John Santos <johnmarivsantos@gmail.com>
 */

class Head{
    constructor(skinTone){
        this.skinTone=skinTone;
        this.complexions=new Complexions();
        this.eye=new Eye(skinTone);
        this.ear=new Ear();
        this.nose=new Nose();
        this.mouth=new Mouth();
    }

    draw(ctx,topPoint,bottomPoint,ref){
        this.#drawBoundary(ctx,topPoint.x,topPoint.y,bottomPoint.x,bottomPoint.y);
        this.complexions.draw(ctx,ref);
        this.ear.draw(ctx,ref);
        this.eye.draw(ctx,ref);
        this.nose.draw(ctx,ref);
        this.mouth.draw(ctx,ref);
    }

    #drawBoundary(ctx,topX,topY,bottomX,bottomY) {
        // Not reflected along x-axis due to unusual line in the middle.
        ctx.beginPath();

        ctx.fillStyle=this.skinTone;
        // Draw top portion right
        ctx.moveTo(topX,topY);
        ctx.quadraticCurveTo(0.09,-0.77,0.15,-0.70);
        ctx.lineTo(0.15,-0.63);
        ctx.quadraticCurveTo(0.28,-0.41,0.23,-0.040);
        
        // Draw bottom portion right
        ctx.lineTo(0.23,0.06);
        ctx.quadraticCurveTo(0.21,0.18,0.15,0.30);
        ctx.quadraticCurveTo(0.14,0.40,0.10,0.49);
        ctx.quadraticCurveTo(0.07,bottomY-0.015,bottomX,bottomY);
        
        // Draw bottom portion left
        ctx.quadraticCurveTo(-0.07,bottomY-0.015,-0.10,0.49);
        ctx.quadraticCurveTo(-0.14,0.40,-0.15,0.30);
        ctx.quadraticCurveTo(-0.21,0.18,-0.23,0.06);
        ctx.lineTo(-0.23,-0.040);

        // Draw top portion left
        ctx.quadraticCurveTo(-0.28,-0.41,-0.15,-0.63);
        ctx.lineTo(-0.15,-0.70);
        ctx.quadraticCurveTo(-0.09,-0.77,-topX,topY);

        ctx.stroke();
        ctx.fill();

        ctx.beginPath();

        ctx.moveTo(0.15,0.30);
        ctx.lineTo(0.08+bottomX*0.15,0.50);  

        ctx.moveTo(-0.15,0.30);
        ctx.lineTo(-0.08-bottomX*0.15,0.50);  

        ctx.stroke();
        
    }
}