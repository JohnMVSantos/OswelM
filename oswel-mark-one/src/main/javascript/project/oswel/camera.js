/* Oswell Application Front-End Avatar.
 * 
 * Copyright (C) 2024 John Santos <johnmarivsantos@gmail.com>
 */

const camCanvas=document.getElementById("camCanvas");
let video=null;

navigator.mediaDevices.getUserMedia({video:true}).
    then(function(data) {
        video=document.createElement("video");
        video.srcObject=data;
        video.play();
        video.onloadeddata=function() {
            camCanvas.width=video.videoWidth;
            camCanvas.height=video.videoHeight;
        }

    }).catch(function(err){
        console.log(err);
    });

let constellationPoints={}

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

        drawPoint(camCtx,{x:face[0],y:face[1]},"F",20);
        drawPoint(camCtx,{x:chest[0],y:chest[1]},"C",20);

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
    const chestPoint=locs.find(p=>p[1]==Math.max(...locs.map(l=>l[1])));

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
    console.log(constellationPoints);
}