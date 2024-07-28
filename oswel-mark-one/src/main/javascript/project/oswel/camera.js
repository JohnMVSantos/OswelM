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