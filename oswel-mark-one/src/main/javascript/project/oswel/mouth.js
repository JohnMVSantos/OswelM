class Mouth{
    constructor(){

    }

    draw(ref,ctx){
        ctx.save();
        ctx.scale(1-Math.abs(ref.xOffset)*0.34,1-Math.abs(ref.yOffset*0.20));
        ctx.translate(ref.x,ref.y);

        // tip is at 0.08
        const mouthCorner={
            x:-ref.xOffset*0.01
        }

        // Middle Lip
        ctx.beginPath();
        ctx.strokeStyle="grey";
        ctx.moveTo(mouthCorner.x-0.08,0.43);
        const tipTopLipLeft={
            x:(ref.xOffset-0.01)*0.05,
            y:0.42
        }
        ctx.quadraticCurveTo(-0.01,0.42,0.00,0.445);

        ctx.moveTo(mouthCorner.x+0.08,0.43);
        const tipTopLipRight={
            x:(ref.xOffset+0.01)*0.05,
            y:0.42
        }
        ctx.quadraticCurveTo(0.01,0.42,0.00,0.445);
        ctx.stroke();

        // Bottom Lip
        ctx.beginPath();
        ctx.strokeStyle="lightgrey";
        ctx.moveTo(mouthCorner.x-0.08,0.43);
        const tipBottomLip={
            x:ref.xOffset*0.12,
            y:0.50
        }
        ctx.quadraticCurveTo(0.00,0.50,mouthCorner.x+0.08,0.43);
        ctx.stroke();

        // Top Lip
        ctx.beginPath();
        ctx.moveTo(mouthCorner.x-0.08,0.43);
        ctx.lineTo(-0.01,0.40);
        ctx.lineTo(0.00,0.42);

        ctx.lineTo(0.01,0.40);
        ctx.lineTo(mouthCorner.x+0.08,0.43);
        ctx.stroke();

        // Nose Connection
        ctx.beginPath();
        ctx.moveTo(-0.015,0.40);
        ctx.quadraticCurveTo(-0.0151,0.385,-0.01,0.34);
        ctx.moveTo(0.015,0.40);
        ctx.quadraticCurveTo(0.0151,0.385,0.01,0.34);
        ctx.stroke();
        ctx.restore();
    }

}