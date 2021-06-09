#version 300 es
precision mediump float;
uniform sampler2D sTexture;

in vec2 vTexCoor;
in vec4 vAmbient;
in vec4 vDiffUse;
in vec4 vSpecular;

out vec4 fragColor;

void main(){
   vec4 finalColor = texture(sTexture,vTexCoor);
   fragColor = finalColor * vAmbient + finalColor * vDiffUse + finalColor * vSpecular;
}
