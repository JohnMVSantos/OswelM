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
    document.getElementById("speakBtn").style.display="none";
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
    document.getElementById("speakBtn").style.display="block";
    document.getElementById("calibrateBtn").style.display="none";
    document.getElementById("cameraOutput").style.display="none";

    if(video){
        video.pause();
        video=null;
    }
}