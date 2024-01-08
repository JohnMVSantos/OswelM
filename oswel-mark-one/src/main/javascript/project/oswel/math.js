
// Vector Math
function lerp(start, end, percentage){
    // Linear Interpolation
    return start+(end-start)*percentage;
}

function match(c1,c2,thr) {
    return distance(c1,c2)<=thr;
}

function distance(p1,p2) {
    let dist=0;
    for(let i=0;i<p1.length;i++) {
        dist+=(p1[i]-p2[i])*(p1[i]-p2[i]);
    }
    return Math.sqrt(dist);
}

function average(locs) {
    const avg=[0,0];
    for(let i=0;i<locs.length;i++){
        avg[0]+=locs[i][0];
        avg[1]+=locs[i][1];
    }
    avg[0]/=locs.length;
    avg[1]/=locs.length;
    return avg;
}

function add(v1, v2){
    let newV=[];
    for(let i=0;i<v1.length;i++){
        newV[i]=v1[i]+v2[i];
    }
    return newV;
}

function subtract(v1, v2){
    let newV=[];
    for(let i=0;i<v1.length;i++){
        newV[i]=v1[i]-v2[i];
    }
    return newV;
}

function magnitude(v){
    return distance(v,new Array(v.length).fill(0));
}

function scale(v,scalar){
    let newV=[];
    for(let i=0;i<v.length;i++){
        newV[i]=v[i]*scalar;
    }
    return newV;
}

function normalize(v){
    return scale(v, 1/magnitude(v));
}