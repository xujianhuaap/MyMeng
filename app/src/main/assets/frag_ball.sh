#version 300 es
precision mediump float;
uniform float uR;
in vec3 aPosition;
in vec4 vAmbientLight;
in vec4 vDiffUse;
out vec4 fragColor;
void main(){
   vec3 color;
   float n = 8.0;
   float span = 2.0*uR/n;
   int i = int((aPosition.x + uR)/span);
   int j = int((aPosition.y + uR)/span);
   int k = int((aPosition.z + uR)/span);
   int whichColor = int(mod(float(i+j+k),2.0));
   if(whichColor == 1) {
   		color = vec3(0.678,0.231,0.129);
   }
   else {
   		color = vec3(1.0,1.0,1.0);
   }
   vec4 finalColor = vec4(color,0f);
   fragColor= finalColor*vAmbientLight + finalColor*vDiffUse;
}