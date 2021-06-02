#version 300 es
uniform mat4 uMVPMatrix;
in vec3 aPosition;
in vec2 aTexture;
out vec2 vTexture;
void main(){
  gl_Position = uMVPMatrix * vec4(aPosition,1);
  vTexture = aTexture;
}