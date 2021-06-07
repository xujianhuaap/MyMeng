#version 300 es
precision mediump float;
unifrom sampler2D sTexDay;
unifrom sampler2D sTexNight;

in vec4 vAmbient;
in vec4 vDiffUse;
in vec4 vSpecular;
in vec2 vTexCoor;

out vec4 fragColor;

void main(){
  vec4 finalDayColor  = texture(sTexDay,vTexCoor);
  finalDayColor = finalDayColor*vAmbient + finalDayColor*vDiffUse + finalDayColor*vSpecular;

  vec4 finalNightColor = texture(sTexNight, vTexCoor);
  finalNightColor = finalNightColor*vAmbient + finalNightColor*vDiffUse + finalNightColor * vSpecular;

  if(vDiffUse > 0.21){

    fragColor = finalDayColor;
   } else if(vDiffUse < 0.05){
     fragColor = finalNightColor;
   } else {
     float t=(vDiffuse.x-0.05)/0.16;
     fragColor = finalDayColor * t + (1-t)*finalNightColor;
  }
}