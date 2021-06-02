#version 300 es
precision mediump float;
uniform sampler2D sSampler;
in vec2 vTexture;
out vec4 fragColor;
void main(){
  fragColor = texture(sSampler,vTexture);
}