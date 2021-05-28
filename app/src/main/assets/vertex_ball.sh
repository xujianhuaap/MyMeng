#version 300 es
uniform mat4 uMVPMatrix;
in vec3 vPosition;
out vec3 aPosition;
void main(){
  gl_Position = uMVPMatrix * vec4(vPosition,1);
  aPosition = vPosition;
}
