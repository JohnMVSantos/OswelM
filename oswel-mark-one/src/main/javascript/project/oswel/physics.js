
class Physics{
    static G=[0,0.01];

    static updatePhysicsItem(items){
        items.forEach(i=>{
            i.update();
        });
    }

    static drawPhysicsItem(items,ctx){
        items.forEach(i=>{
            i.draw(ctx);
        });
    }
}

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
        newLocation=add(newLocation, Physics.G);
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

class Segment{
    constructor(particleA,particleB){
        this.particleA=particleA;
        this.particleB=particleB;
        this.length=distance(particleA.location,particleB.location);
    }

    update(){
        const diffVector=subtract(this.particleA.location, 
            this.particleB.location);
        const magn=magnitude(diffVector);

        const diff=magn-this.length;
        const norm=normalize(diffVector);
        
        if(!this.particleA.isFixed && !this.particleB.isFixed){
            this.particleA.location=add(
                this.particleA.location,
                scale(norm,-diff/2)
            );
            this.particleB.location=add(
                this.particleB.location,
                scale(norm,+diff/2)
            );
        }else if(!this.particleA.isFixed){
            this.particleA.location=add(
                this.particleA.location,
                scale(norm,-diff)
            );
        }else if(!this.particleB.isFixed){
            this.particleB.location=add(
                this.particleB.location,
                scale(norm,+diff)
            );
        }
    }

    draw(ctx){
        ctx.beginPath();
        ctx.strokeStyle="red";
        ctx.moveTo(...this.particleA.location);
        ctx.lineTo(...this.particleB.location);
        ctx.stroke();
    }
}