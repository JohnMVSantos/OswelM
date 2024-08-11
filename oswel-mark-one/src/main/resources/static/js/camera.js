/* Oswell Application Front-End Avatar.
 * 
 * Copyright (C) 2024 John Santos <johnmarivsantos@gmail.com>
 */

const camCanvas=document.getElementById("camCanvas");
const camCtx=camCanvas.getContext("2d");

// Properties used for the markers and the sliders option.
let video=null;
let constellationPoints={}

// Properties used for the face detection initial starting point.
let center={
    x:0,
    y:0,
}

function processDetections(detections){
    camCtx.drawImage(video,0,0);
    let maxScore=0.0;

    if(detections.length>0){
        let box=detections[0].detection._box
        // Get the most confident detection.
        for (let i=0;i<detections.length;i++) {
            if(detections[i]._score>maxScore){
                maxScore=detections[i].detection._score;
                // Contains keys: _x, _y, _width,_height
                box=detections[i].detection._box;
            }
        }
        // Normalize against the context dimensions.
        center.x = -1*0.2*(2*((box._x + box._width/2)/camCanvas.width)-1);
        center.y = 2*((box._y + box._height/2)/camCanvas.height)-1;
        updateLookAt({value:center.x},'x');
        updateLookAt({value:center.y},'y');
    }
}

function processImage(){
    camCtx.drawImage(video,0,0);
    const imgData=camCtx.getImageData(0,0,camCanvas.width,camCanvas.height);
    const locs=getMarkedLocations(imgData);

    if(DEBUG){
        for (let i=0;i<locs.length;i++){
            camCtx.beginPath();
            camCtx.fillStyle="red";
            camCtx.rect(...locs[i],1,1);
            camCtx.fill();
        }
    }
    if(locs.length>0){
        // Points where to look.
        const {face,chest}=getConstellation(locs);
        constellationPoints.face=face;
        constellationPoints.chest=chest;

        if(DEBUG){
            drawPoint(camCtx,{x:face[0],y:face[1]},"F",20);
            drawPoint(camCtx,{x:chest[0],y:chest[1]},"C",20);
        }

        // If d1=d2, then face stays still.
        // If d1>d2, then face should look up.
        if(constellationPoints.ref) {
            const d1=distance(face,chest);
            const d2=distance(constellationPoints.ref.face,constellationPoints.ref.chest);

            // const avg=average(locs);
            // const x=(avg[0]-imgData.width/2)/imgData.width;
            // const y=(avg[1]-imgData.height/2)/imgData.height;

            const diffY=3*(d2-d1)/imgData.height;
            const diffX=(face[0]-chest[0])/d1;

            updateLookAt({value:-diffX},'x');
            updateLookAt({value:diffY},'y');
        }
    }
}

function getConstellation(locs){
    // Map returns an array of all y values. P is the maximum (lowest) y point.
    let chestPoint=locs.find(p=>p[1]==Math.max(...locs.map(l=>l[1])));

    // Get the point centered on the face.
    let facePoint=locs[0];
    let maxDist=0;
    for(let i=0;i<locs.length;i++){
        const dist=distance(locs[i],chestPoint);
        if(dist>maxDist){
            maxDist=dist;
            facePoint=locs[i];
        }
    }

    // Averaging the values.
    const fSet=[];
    const cSet=[];
    for(let i=0;i<locs.length;i++){
        const fDist=distance(locs[i],facePoint);
        const cDist=distance(locs[i],chestPoint);
        const minDist=Math.min(fDist,cDist);
        if(minDist==fDist){
            fSet.push(locs[i]);
        } else if(minDist==cDist){
            cSet.push(locs[i]);
        }
    }

    facePoint=average(fSet);
    chestPoint=average(cSet);

    return {
        face:facePoint,
        chest:chestPoint,
    };
}

// Create a reference point and every point will be relative to the reference.
function calibrate(){
    constellationPoints.ref={
        face:constellationPoints.face,
        chest:constellationPoints.chest,
    }
}