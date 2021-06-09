#version 300 es
uniform mat4 uMVPMatrix;
uniform mat4 uMMatrix;
uniform vec3 uCamera;
uniform vec3 uLightLocation;

in vec2 aTexCoor;
in vec3 aPosition;
in vec3 aNormal;

out vec2 vTexCoor;
out vec4 vAmbient;
out vec4 vDiffUse;
out vec4 vSpecular;

void pointLight(
  in vec3 normal,
  in vec3 lightPosition,
  in vec4 lightAmbient,
  in vec4 lightDiffUse,
  in vec4 lightSpecular,
  inout vec4 ambient, //环境光强度
  inout vec4 diffUse,//漫反射强度
  inout vec4 specular//镜面反射强度
){
  //环境光计算
  ambient = lightAmbient;

  //标准化法向量
  vec3 normalTarget = aPosition + normal;//法向量移动到指定位置
  //基本变换(移动,缩放,旋转)后的法向量
  vec3 newNormal = (uMMatrix*vec4(normalTarget,1)).xyz - (uMMatrix*vec4(aPosition,1)).xyz;
  //规格化法向量
  newNormal = normalize(newNormal);

  //光照位置计算
  vec3 lightLocation = normalize(lightPosition - (uMMatrix * vec4(aPosition,1)).xyz);
  lightLocation = normalize(lightLocation);

  //计算漫反射
  float dotForLightAndNormal = max(0.0,dot(newNormal,lightLocation));
  diffUse = lightDiffUse * dotForLightAndNormal;

  //视角位置计算
  vec3 viewLocation = uCamera - (uMMatrix * vec4(aPosition,1)).xyz;
  viewLocation = normalize(viewLocation);
  //综合光源和视角
  vec3 eyeAndLightLocation = normalize(viewLocation + lightLocation);

  //计算镜面反色
  float dotNormalAnd = dot(newNormal,eyeAndLightLocation);
  float sunShineStrength = 50.0;
  float powerFactor = max(0.0,pow(dotForLightAndNormal,sunShineStrength));
  specular = lightSpecular * powerFactor;
}

void main(){
  gl_Position = uMVPMatrix * vec4(aPosition,1);
  vTexCoor = aTexCoor;
  vec3 inputNormal = normalize(aNormal);
  vec4 inputAmbient = vec4(0.05,0.05,0.025,1.0);
  vec4 inputDiffUse = vec4(1.0,1.0,0.5,1.0);
  vec4 inputSpecular = vec4(0.3,0.3,0.15,1.0);

  vec4 ambient = vec4(0.0,0.0,0.0,0.0);
  vec4 diffUse = vec4(0.0,0.0,0.0,0.0);
  vec4 specular = vec4(0.0,0.0,0.0,0.0);

  pointLight(inputNormal,uLightLocation,inputAmbient,
    inputDiffUse,inputSpecular,ambient,diffUse,specular);
  vAmbient = ambient;
  vDiffUse = diffUse;
  vSpecular = specular;

}