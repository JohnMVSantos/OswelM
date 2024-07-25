class Pendulum{
    constructor(initLoc){
        this.initLoc=initLoc;
        this.particles=[
            new Particle([initLoc.x,initLoc.y],true),
            new Particle([initLoc.x,initLoc.y+0.1])
        ],
        this.segments=[
            new Segment(this.particles[0],this.particles[1])
        ]
    }

    update(xTranslate,xScale){
        this.particles[0].location=[
            (this.initLoc.x+xTranslate)*xScale,
            this.initLoc.y
        ];
        Physics.updatePhysicsItem(this.particles);
        Physics.updatePhysicsItem(this.segments);
    }

    draw(ctx){
        Physics.drawPhysicsItem(this.particles,ctx);
        Physics.drawPhysicsItem(this.segments,ctx);
    }
}