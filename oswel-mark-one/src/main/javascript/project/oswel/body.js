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

        ctx.moveTo(0.17,0.95);
        ctx.quadraticCurveTo(0.24,0.84,0.28,0.68);
        ctx.lineTo(0.29,0.68);
        ctx.quadraticCurveTo(0.30,0.69,0.30,0.73);
        ctx.quadraticCurveTo(0.28,0.82,0.21,0.95);

        ctx.moveTo(0.11,0.95);
        ctx.quadraticCurveTo(0.17,0.79,0.18,0.87);
        ctx.moveTo(0.14,0.95);
        ctx.quadraticCurveTo(0.19,0.83,0.21,0.88);

        ctx.moveTo(0.27,0.95);
        ctx.quadraticCurveTo(0.33,0.84,0.355,0.75);
        ctx.quadraticCurveTo(0.38,0.79,0.38,0.81);
        ctx.quadraticCurveTo(0.35,0.88,0.31,0.95);

        ctx.moveTo(0.25,0.88);
        ctx.lineTo(0.26,0.91);
        ctx.quadraticCurveTo(0.30,0.83,0.30,0.74);
        ctx.quadraticCurveTo(0.33,0.76,0.33,0.77);
        ctx.quadraticCurveTo(0.315,0.84,0.26,0.95);
        ctx.lineTo(0.24,0.90);
        
        ctx.moveTo(0.245,0.95);
        ctx.quadraticCurveTo(0.22,0.90,0.235,0.91);
        ctx.moveTo(0.355,0.75);
        ctx.quadraticCurveTo(0.34,0.78,0.33,0.77);        

        ctx.moveTo(0.15,0.81);
        ctx.lineTo(0.145,0.76);
        ctx.quadraticCurveTo(0.14,0.80,0.125,0.80);
        ctx.lineTo(0.13,0.835);

        ctx.moveTo(0.20,0.82);
        ctx.lineTo(0.21,0.82);
        ctx.moveTo(0.19,0.80);
        ctx.quadraticCurveTo(0.21,0.79,0.225,0.805);

        // Collar bone.
        ctx.moveTo(0.27,0.73);
        ctx.lineTo(0.25,0.70);
        ctx.quadraticCurveTo(0.18,0.67,0.11,0.59);
        ctx.moveTo(0.26,0.74);
        ctx.lineTo(0.24,0.71);
        ctx.quadraticCurveTo(0.18,0.68,0.11,0.62);

        ctx.moveTo(0.25,0.75);
        ctx.lineTo(0.23,0.725);
        ctx.quadraticCurveTo(0.18,0.73,0.14,0.70);
        ctx.quadraticCurveTo(0.12,0.67,0.12,0.63);

        ctx.moveTo(0.245,0.77);
        ctx.lineTo(0.225,0.745);
        ctx.quadraticCurveTo(0.17,0.74,0.14,0.715);
        ctx.quadraticCurveTo(0.13,0.71,0.12,0.69);

        ctx.moveTo(0.19,0.77);
        ctx.quadraticCurveTo(0.15,0.75,0.14,0.72);

        ctx.moveTo(0.18,0.78);
        ctx.quadraticCurveTo(0.15,0.77,0.123,0.70);

        // Disc
        ctx.moveTo(0.24,0.78);
        ctx.quadraticCurveTo(0.19,0.75,0.17,0.80);
        
        ctx.stroke();
        ctx.restore();

    }
}