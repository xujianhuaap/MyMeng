#version 300 es
uniform mat4 uMVPMatrix;
uniform mat4 uMMatrix;
uniform vec3 uLightPosition;
in vec3 aNormal;
in vec3 vPosition;
out vec4 vAmbientLight;
out vec4 vDiffUse;
out vec3 aPosition;

//散射光照计算方法
//向量数学中，两个向量的点积为两个向量夹角的余弦值乘以两个向量的模，而规格化后向量的模为1。因此，首先将两个向量规格化，再点积就可以求得两个向量夹角的余弦值。
//散射光最终强度=散射光强度×max（cos（入射角）,0）
//散射光照射结果=材质的反射系数×散射光最终强度
void pointLight (
  //法向量
  in vec3 normal,
   //散射光计算结果
  inout vec4 diffuse,
  //光源位置
  in vec3 lightLocation,
  //散色光强度
  in vec4 lightDiffuse
){
   //计算后的法向量
  vec3 normalTarget=vPosition+normal;
  vec3 newNormal=(uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(vPosition,1)).xyz;
  //法向量规格化
  newNormal=normalize(newNormal);
  vec3 vp= normalize(lightLocation-(uMMatrix*vec4(vPosition,1)).xyz);
  vp=normalize(vp);
  float nDotViewPosition=max(0.0,dot(newNormal,vp));
  diffuse=lightDiffuse*nDotViewPosition;
}

void main(){
  gl_Position = uMVPMatrix * vec4(vPosition,1);


  //初始化 最终结果向量
   vec4 diffuseTemp=vec4(0.0,0.0,0.0,0.0);
   //散射光计算
   pointLight(normalize(aNormal), diffuseTemp, uLightPosition, vec4(0.8,0.8,0.8,1.0));
   //输出散射光结果
   vDiffUse=diffuseTemp;
  aPosition = vPosition;
  vAmbientLight = vec4(0.15,0.15,0.15,1.0);
}
