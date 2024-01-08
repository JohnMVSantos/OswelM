class Nose{
    constructor(){

    }
    
    draw(ref,ctx) {
        ctx.save();
        ctx.scale(1-Math.abs(ref.xOffset)*0.34,1-Math.abs(ref.yOffset*0.20));
        ctx.translate(ref.x,ref.y);
        ctx.beginPath();

        ctx.moveTo(0.00,0.13);

        const tip={
            x:ref.xOffset*0.12,
            y:0.33
        }

        ctx.quadraticCurveTo(tip.x,tip.y,0.00,0.335);
        ctx.lineTo(-0.045,0.315);
        ctx.moveTo(0.00,0.335);
        ctx.lineTo(0.045,0.315);

        ctx.stroke();

        ctx.beginPath();

        ctx.strokeStyle="white";
        ctx.moveTo(-0.045,0.315);
        ctx.quadraticCurveTo(-0.07,0.30,-0.05,0.26);
        ctx.moveTo(0.045,0.315);
        ctx.quadraticCurveTo(0.07,0.30,0.05,0.26);

        ctx.stroke();
        ctx.restore();
    }
}