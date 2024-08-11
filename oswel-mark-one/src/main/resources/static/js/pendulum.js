/* Oswell Application Front-End Avatar.
 * 
 * Copyright (C) 2024 John Santos <johnmarivsantos@gmail.com>
 */

class Pendulum{
    constructor(initLocation,length=0.1){
        this.initLocation=initLocation;
        this.particles=[
            new Particle([initLocation.x,initLocation.y],true),
            new Particle([initLocation.x,initLocation.y+length])
        ],
        this.segments=[
            new Segment(this.particles[0],this.particles[1])
        ]
    }

    update2(attachLoc){
        this.particles[0].location=attachLoc;
        Physics.updatePhysicsItems(this.particles);
        Physics.updatePhysicsItems(this.segments);
    }

    update(xTranslate,xScale){
        this.particles[0].location=[
            (this.initLocation.x+xTranslate)*xScale,
            this.initLocation.y
        ];
        Physics.updatePhysicsItems(this.particles);
        Physics.updatePhysicsItems(this.segments);
    }

    draw(ctx){
        Physics.drawPhysicsItems(this.particles,ctx);
        Physics.drawPhysicsItems(this.segments,ctx);
    }
}