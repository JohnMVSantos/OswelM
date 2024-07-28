class Body{
    constructor(skinTone){
        this.skinTone=skinTone;
    }
    draw(ctx){
        //ctx.beginPath();
        ctx.save();
        this.#drawDetails(ctx);
        ctx.scale(-1, 1);
        this.#drawDetails(ctx);
        ctx.restore();
    }

    #drawDetails(ctx){
        ctx.save();

        // Chest plate (main).
        ctx.beginPath();

        ctx.fillStyle="rgba(192,192,192,0.3)";
        ctx.moveTo(0.09,0.95);
        ctx.quadraticCurveTo(0.10,0.83,0.19,0.79);
        ctx.lineTo(0.20,0.84);
        ctx.quadraticCurveTo(0.24,0.80,0.25,0.75);
        ctx.lineTo(0.26,0.75);
        
        // Chest plate (main) border.
        ctx.quadraticCurveTo(0.23,0.84,0.21,0.88);
        ctx.quadraticCurveTo(0.20,0.86,0.18,0.87);
        ctx.quadraticCurveTo(0.17,0.79,0.11,0.95);
        ctx.lineTo(0.09,0.95);

        ctx.stroke();
        ctx.fill();

        // Chest plate (middle).
        ctx.beginPath();

        ctx.fillStyle="rgba(128,128,128,0.3)";
        ctx.moveTo(0.21,0.88);
        ctx.quadraticCurveTo(0.19,0.83,0.14,0.95);
        ctx.lineTo(0.11,0.95);
        ctx.quadraticCurveTo(0.17,0.79,0.18,0.87);

        ctx.stroke();
        ctx.fill();

        // Chest plate (outer).
        ctx.beginPath();

        ctx.fillStyle="rgba(192,192,192,0.3)";
        ctx.moveTo(0.21,0.88);
        ctx.quadraticCurveTo(0.19,0.92,0.17,0.95);
        ctx.lineTo(0.14,0.95);
        ctx.quadraticCurveTo(0.185,0.86,0.18,0.87);
        
        ctx.stroke();
        ctx.fill();

        // Disc (inner).
        ctx.beginPath();

        ctx.fillStyle="rgba(20,120,200,0.4)";
        ctx.moveTo(0.195,0.816);
        ctx.lineTo(0.217,0.820);
        ctx.lineTo(0.20,0.84);
        ctx.lineTo(0.195,0.816);

        ctx.stroke();
        ctx.fill();

        // Disc (middle).
        ctx.beginPath();
        
        ctx.fillStyle="rgba(30,130,190,0.38)";
        ctx.moveTo(0.19,0.80);
        ctx.quadraticCurveTo(0.21,0.79,0.225,0.805);
        ctx.lineTo(0.217,0.820);
        ctx.lineTo(0.195,0.816);
        ctx.lineTo(0.19,0.80);

        ctx.stroke();
        ctx.fill();

        // Disc (outer).
        ctx.beginPath();

        ctx.fillStyle="rgba(35,140,185,0.36)";
        ctx.moveTo(0.24,0.78);
        ctx.quadraticCurveTo(0.19,0.75,0.17,0.80);
        ctx.lineTo(0.19,0.79);
        ctx.lineTo(0.19,0.80);
        ctx.quadraticCurveTo(0.21,0.79,0.225,0.805);
        ctx.quadraticCurveTo(0.23,0.80,0.24,0.78);

        ctx.stroke();
        ctx.fill();

        // Shoulder shell 1 (inner).
        ctx.beginPath();

        ctx.fillStyle=this.skinTone;
        ctx.moveTo(0.17,0.95);
        ctx.quadraticCurveTo(0.24,0.84,0.28,0.68);
        ctx.lineTo(0.29,0.68);
        ctx.quadraticCurveTo(0.30,0.69,0.30,0.73);
        ctx.quadraticCurveTo(0.28,0.82,0.21,0.95);
        ctx.lineTo(0.17,0.95);

        ctx.stroke();
        ctx.fill();

        // Shoulder shell 2 (middle).
        ctx.beginPath();

        ctx.fillStyle="rgba(128,128,128,0.3)";
        ctx.moveTo(0.25,0.88);
        ctx.lineTo(0.26,0.91);
        ctx.quadraticCurveTo(0.30,0.83,0.30,0.74);
        ctx.quadraticCurveTo(0.28,0.82,0.25,0.88);

        ctx.stroke();
        ctx.fill();

        ctx.beginPath();

        ctx.fillStyle=this.skinTone;
        ctx.moveTo(0.26,0.91);
        ctx.quadraticCurveTo(0.30,0.83,0.30,0.74);
        ctx.quadraticCurveTo(0.33,0.76,0.33,0.77);
        ctx.quadraticCurveTo(0.315,0.84,0.26,0.95);
        ctx.lineTo(0.24,0.90);
        ctx.lineTo(0.25,0.88);

        ctx.stroke();
        ctx.fill();

        ctx.beginPath();

        ctx.fillStyle="rgba(128,128,128,0.3)";
        ctx.moveTo(0.26,0.95);
        ctx.lineTo(0.245,0.95);
        ctx.lineTo(0.232,0.92);
        ctx.lineTo(0.24,0.90);
        ctx.lineTo(0.26,0.95);
        
        ctx.stroke();
        ctx.fill();

        // Shoulder shell 3 (outside).
        ctx.beginPath();

        ctx.fillStyle="rgba(128,128,128,0.3)";
        ctx.moveTo(0.26,0.95);
        ctx.quadraticCurveTo(0.34,0.84,0.355,0.76);
        ctx.quadraticCurveTo(0.34,0.78,0.33,0.77);        
        ctx.quadraticCurveTo(0.32,0.82,0.26,0.95);

        ctx.stroke();
        ctx.fill();

        ctx.beginPath();

        ctx.fillStyle=this.skinTone;
        ctx.moveTo(0.355,0.76);
        ctx.quadraticCurveTo(0.38,0.79,0.38,0.81);
        ctx.quadraticCurveTo(0.35,0.88,0.31,0.95);
        ctx.lineTo(0.26,0.95);
        ctx.quadraticCurveTo(0.34,0.84,0.355,0.76);

        ctx.stroke();
        ctx.fill();

        // Collar bone 1 (top/main).
        ctx.beginPath();

        ctx.fillStyle="rgba(128,128,128,0.4)";
        ctx.moveTo(0.265,0.73);
        ctx.lineTo(0.25,0.70);
        ctx.quadraticCurveTo(0.18,0.67,0.11,0.59);
        ctx.quadraticCurveTo(0.105,0.605,0.11,0.62);
        ctx.quadraticCurveTo(0.18,0.68,0.24,0.71);
        ctx.lineTo(0.26,0.745);
        ctx.lineTo(0.265,0.73);

        ctx.stroke();
        ctx.fill()
    
        // Collar bone 2 (middle).
        ctx.beginPath();

        ctx.moveTo(0.25,0.75);
        ctx.lineTo(0.23,0.725);
        ctx.quadraticCurveTo(0.18,0.73,0.14,0.70);
        ctx.quadraticCurveTo(0.12,0.67,0.12,0.63);
        ctx.lineTo(0.11,0.62);
        ctx.quadraticCurveTo(0.11,0.66,0.12,0.69);
        ctx.quadraticCurveTo(0.13,0.71,0.14,0.715);
        ctx.quadraticCurveTo(0.17,0.74,0.225,0.745);
        ctx.lineTo(0.245,0.77);
        ctx.lineTo(0.25,0.75);

        ctx.stroke();
        ctx.fill();
        
        // Collar bone 3 (smallest).
        ctx.beginPath();

        ctx.moveTo(0.19,0.77);
        ctx.quadraticCurveTo(0.15,0.75,0.14,0.72);
        ctx.lineTo(0.123,0.70);
        ctx.quadraticCurveTo(0.15,0.77,0.18,0.78);
        ctx.lineTo(0.19,0.77);
        
        ctx.stroke();
        ctx.fill();

        ctx.restore();
    }
}