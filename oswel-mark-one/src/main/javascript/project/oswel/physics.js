const G=[0,0.0001];

class Particle{
    constructor(location,isFixed){
        this.location=location;
        this.oldLocation=location;
        this.isFixed=isFixed;
    }

    update(){
        if(this.isFixed){
            return;
        }
        const vel=subtract(this.location, this.oldLocation);
        let newLocation=add(this.location, vel);
        newLocation=add(newLocation, G);
        this.oldLocation=this.location;
        this.location=newLocation;
    }

    draw(ctx){
        ctx.beginPath();
        ctx.fillStyle="red";
        ctx.strokeStyle="white";
        const rad=0.03;
        ctx.arc(...this.location,rad,0,Math.PI*2);
        ctx.fill();
        ctx.stroke();
    }
}