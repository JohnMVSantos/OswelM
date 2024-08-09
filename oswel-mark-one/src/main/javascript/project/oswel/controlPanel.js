/* Oswell Application Front-End Avatar.
 * 
 * Copyright (C) 2024 John Santos <johnmarivsantos@gmail.com>
 */

// TODO: This causes complications for allowing different options for the input.
Promise.all([
    faceapi.nets.tinyFaceDetector.loadFromUri('../../../resources/models'),
    faceapi.nets.faceLandmark68Net.loadFromUri('../../../resources/models'),
    faceapi.nets.faceRecognitionNet.loadFromUri('../../../resources/models'),
    faceapi.nets.faceExpressionNet.loadFromUri('../../../resources/models'),
]).then(
    initializeFaceDetection
)

function onInputChange(info){
    switch(info.value){
        case "face":
            initializeFaceDetection();
            break;
        case "markers":
            initializeMarkers();
            break;
        case "sliders":
            initializeSliders();
            break;
    }
}

function initializeFaceDetection() {
    document.getElementById("sliders").style.display="none";
    document.getElementById("detectionCanvas").style.display="block";
    document.getElementById("calibrateBtn").style.display="none";
    document.getElementById("cameraOutput").style.display="block";
    
    if(video){
        video.pause(); 
        video=null;
    }

    navigator.mediaDevices.getUserMedia({video:true}).
        then(function(data) {
            video=document.createElement("video");
            video.srcObject=data;
            video.play();
            video.onloadeddata=function() {
                camCanvas.width=video.videoWidth;
                camCanvas.height=video.videoHeight;
                const displaySize={width:170,height:130}

                const detectionCanvas = faceapi.createCanvasFromMedia(video)
                document.getElementById("cameraOutput").append(detectionCanvas)
                faceapi.matchDimensions(detectionCanvas,displaySize)

                // Every 100ms.
                setInterval(async () => {
                    const detections = await faceapi.detectAllFaces(
                        video,
                        new faceapi.TinyFaceDetectorOptions()
                    ).withFaceLandmarks().withFaceExpressions()
                    const resizedDetections=faceapi.resizeResults(detections,displaySize)
                    detectionCanvas.getContext('2d').clearRect(0,0,detectionCanvas.width,detectionCanvas.height)
                    if(DEBUG){
                        faceapi.draw.drawDetections(detectionCanvas,resizedDetections)
                        faceapi.draw.drawFaceLandmarks(detectionCanvas,resizedDetections)
                        faceapi.draw.drawFaceExpressions(detectionCanvas,resizedDetections)
                    }
                    processDetections(detections)
                }, 100)

            }
        }).catch(function(err){
            console.log(err);
            video.pause();
        });
}

function initializeMarkers() {
    document.getElementById("sliders").style.display="none";
    document.getElementById("detectionCanvas").style.display="none";
    document.getElementById("calibrateBtn").style.display="block";
    document.getElementById("cameraOutput").style.display="block";
    
    if(video){
        video.pause();
        video=null;
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
    document.getElementById("detectionCanvas").style.display="none";
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

function toggleCamera(info){
    if(info.checked){
        document.getElementById("cameraOutput").style.display="block";
    } else {
        document.getElementById("cameraOutput").style.display="none";
    }
}

function increaseSpeed(button, limit) {
    const numberInput = button.parentElement.querySelector('.number');
    var value = parseInt(numberInput.innerHTML, 10);
    if(isNaN(value)) value = 0;
    if(limit && value >= limit) return;
    numberInput.innerHTML = value+1;
    Physics.speed.x=value+1;
    Physics.speed.y=value+1;
}
  
function decreaseSpeed(button) {
    const numberInput = button.parentElement.querySelector('.number');
    var value = parseInt(numberInput.innerHTML, 10);
    if(isNaN(value)) value = 0;  
    if(value < 1) return;
    numberInput.innerHTML = value-1;
    Physics.speed.x=value-1;
    Physics.speed.y=value-1;
}

function increaseGravity(button, limit) {
    const numberInput = button.parentElement.querySelector('.number');
    var value = parseFloat(numberInput.innerHTML);
    if(isNaN(value)) value = 0.00;
    if(value >= limit) return;
    numberInput.innerHTML = value+0.001;
    Physics.G[1]=value+0.001;
    Physics.G[1]=value+0.001;
}
  
function decreaseGravity(button) {
    const numberInput = button.parentElement.querySelector('.number');
    var value = parseFloat(numberInput.innerHTML);
    if(isNaN(value)) value = 0.00;  
    if(value < 0) return;
    numberInput.innerHTML = value-0.001;
    Physics.G[1]=value-0.001;
    Physics.G[1]=value-0.001;
}