class Body{
    constructor(){
        
    }
    draw(ctx){
        ctx.beginPath();

        ctx.save();
        this.#drawDetails(ctx);
        ctx.scale(-1, 1);
        this.#drawDetails(ctx);
        ctx.restore();
    }

    #drawDetails(ctx){
        ctx.save();
        ctx.moveTo(0.09,0.94);
        ctx.quadraticCurveTo(0.10,0.83,0.19,0.79);
        ctx.lineTo(0.20,0.84);
        ctx.quadraticCurveTo(0.24,0.80,0.26,0.73);
        ctx.lineTo(0.27,0.73);

        ctx.moveTo(0.17,0.94);
        ctx.quadraticCurveTo(0.24,0.84,0.28,0.68);
        ctx.lineTo(0.29,0.68);

        ctx.stroke();
        ctx.restore();

    }
}