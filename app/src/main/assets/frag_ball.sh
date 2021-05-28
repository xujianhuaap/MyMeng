#version 300 es
precision mediump float;
uniform float uR;
in vec2 mcLongLat;
in vec3 aPosition;
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
   		color = vec3(0.678,0.231,0f);
   }
   else {
   		color = vec3(1.0,0f,1.0);
   }
   fragColor=vec4(color,1.0f);
}