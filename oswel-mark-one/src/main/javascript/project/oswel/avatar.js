class Avatar{
    constructor(lookAt){
        this.complexions=new Complexions();
        this.eye=new Eye();
        this.nose=new Nose();
        this.lookAt=lookAt;
        this.particles=[
            new Particle([this.lookAt.x,this.lookAt.y],true),
            new Particle([this.lookAt.x,this.lookAt.y+0.3])
        ];
    }

    updateParticles(){
        this.particles.forEach(p=>{
            p.update();
        });
    }

    drawParticles(ctx){
        this.particles.forEach(p=>{
            p.draw(ctx);
        });
    }

    draw(ctx){
        ctx.strokeStyle="rgb(76,78,80)";
        this.#drawHead(ctx);

        ctx.save();
        ctx.translate(this.lookAt.xOffset*0.005,0);
        ctx.scale(1-Math.abs(this.lookAt.xOffset)*0.04,1);
        this.#drawBody(ctx);
        ctx.restore();
        
        this.updateParticles();
        this.drawParticles(ctx);

        if(DEBUG){
            drawAxis(lookAt);
            drawPoint(this.lookAt, "A");
        }
    }

    #drawHead(ctx) {
        ctx.save();

        const topPoint={
            x:Math.min(0.030, this.lookAt.x),
            y:Math.min(-0.785, this.lookAt.y-0.730)
        }
        const verticalSquish=1-Math.abs(this.lookAt.yOffset*0.2);
        const bottomPoint={
            x:Math.min(0.075, this.lookAt.x),
            y:this.lookAt.y+(0.625-Math.min(0,this.lookAt.yOffset)*0.28)*verticalSquish,
        }

        this.#drawBoundary(topPoint.x,topPoint.y,bottomPoint.x,bottomPoint.y,ctx);
        ctx.scale(-1,1);
        this.#drawBoundary(-topPoint.x,topPoint.y,-bottomPoint.x,bottomPoint.y,ctx);
        ctx.restore();

        this.eye.draw(this.lookAt,ctx);
        this.complexions.drawFaceDetails(this.lookAt,ctx);
        this.nose.draw(this.lookAt,ctx);
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
        ctx.quadraticCurveTo(0.07,0.58,bottomX,bottomY);

        ctx.stroke();
        ctx.closePath();
        ctx.fillStyle=skinTone;
        ctx.fill();

        ctx.beginPath();

        ctx.moveTo(0.15,0.30);
        ctx.lineTo(0.08+bottomX*0.15,0.50);  

        ctx.stroke();
    }

    #drawBody(ctx) {
        ctx.beginPath();

        // Drawing Neck
        ctx.moveTo(-0.07+this.lookAt.xOffset*0.01,0.55+this.lookAt.yOffset*0.01);
        ctx.quadraticCurveTo(-0.05,0.70,-0.06,0.80);
        ctx.moveTo(0.07+this.lookAt.xOffset*0.01,0.55+this.lookAt.yOffset*0.01);
        ctx.quadraticCurveTo(0.05,0.70,0.06,0.80);
        
        ctx.stroke();
    }
}