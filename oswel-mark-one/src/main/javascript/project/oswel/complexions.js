class Complexions{
    constructor(){

    }
    drawFaceDetails(ref,ctx) {
        ctx.save();
        ctx.translate(ref.x,ref.y+0.040);
        ctx.beginPath();

        ctx.moveTo(-0.02,-0.04);
        ctx.quadraticCurveTo(0.00,-0.05,0.02,-0.04);
        ctx.moveTo(-0.04,0.02)
        ctx.quadraticCurveTo(0.00,-0.01,0.04,0.02);

        ctx.stroke();

        this.#drawFaceLines(ref.xOffset,ref.yOffset,ctx);
        ctx.scale(-1,1);
        this.#drawFaceLines(-ref.xOffset,ref.yOffset,ctx);

        ctx.restore();
    }

    #drawFaceLines(xOffset,yOffset,ctx) {
        ctx.save();
        const scaleX=Math.max(0,xOffset);
        const scaleY=yOffset;
        ctx.scale(1-scaleX*0.55,1-Math.abs(scaleY*0.5));

        ctx.beginPath();

        const outerMostPoint={
            x:0.15-Math.min(xOffset*0.085,0),
            y:-0.70-Math.max(0,yOffset*0.65)
        }

        ctx.moveTo(outerMostPoint.x,outerMostPoint.y);
        ctx.lineTo(0.13,-0.41);
        ctx.quadraticCurveTo(0.11,-0.28,0.06,-0.17);
        ctx.quadraticCurveTo(0.06,-0.11,0.02,-0.04);
        ctx.lineTo(0.06,0.00);
        ctx.moveTo(0.04,0.06);
        ctx.quadraticCurveTo(0.11,0.17,0.18,0.23);
        ctx.restore();

        ctx.stroke();
    }
}