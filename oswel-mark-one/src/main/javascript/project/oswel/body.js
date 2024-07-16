class Body{
    constructor(){

    }
    
    draw(lookAt,ctx) {
        ctx.beginPath();

        // Drawing Neck
        ctx.moveTo(-0.07+lookAt.xOffset*0.01,0.55+lookAt.yOffset*0.01);
        ctx.quadraticCurveTo(-0.05,0.70,-0.06,0.80);
        ctx.moveTo(0.07+lookAt.xOffset*0.01,0.55+lookAt.yOffset*0.01);
        ctx.quadraticCurveTo(0.05,0.70,0.06,0.80);
        
        ctx.stroke();
        
    }
}