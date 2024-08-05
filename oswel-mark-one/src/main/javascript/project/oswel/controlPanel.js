function onInputChange(info){
    switch(info.value){
        case "face detection":
            initializeCamera();
            break;
        case "markers":
            initializeCamera();
            break;
        case "sliders":
            initializeSliders();
            break;
    }
}

function initializeCamera() {
    document.getElementById("sliders").style.display="none";
    document.getElementById("calibrateBtn").style.display="block";
    document.getElementById("cameraOutput").style.display="block";
    
    if(video){
        video.pause();
    }
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
}

function initializeSliders(){
    contellationPoints={};
    document.getElementById("sliders").style.display="block";
    document.getElementById("calibrateBtn").style.display="none";
    document.getElementById("cameraOutput").style.display="none";

    if(video){
        video.pause();
        video=null;
    }
}

// Called when changing the sliders.
function updateLookAt(info,attr){   
    const p=lookAt;
    let yRange=p.yRange;
    let value=info.value;
    if(p.yNegRange){
        yRange=info.value<0?p.yNegRange:p.yRange;
        value=Math.abs(value);
    }
    switch(attr){
        case "x":
            p.xOffset=value;
            p.x=lerp(p.xRange[0],p.xRange[1],info.value);
            break;
        case "y":
            p.yOffset=value;
            p.y=lerp(yRange[0],yRange[1],value);
            break;
    } 
}

// Called when changing the sliders.
function updateMouth(info,attr){   
    switch(attr){
        case "x":
            avatar.head.mouth.x=info.value;
            break;
        case "y":
            avatar.head.mouth.y=info.value;
            break;
    } 
}

// Set speaking status.
function toggleSpeak(info){
    avatar.head.mouth.speak=info.checked;
}

function toggleDebug(info){
    DEBUG=info.checked;
}