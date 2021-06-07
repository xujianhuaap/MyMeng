#version 300 es

uniform mat4 uMVPMatrix;
uniform mat4 uMMatrix;
uniform vec3 uCamera;
uniform vec3 uLightLocation;

in vec3 aPosition;
in vec2 aTexCoor;
in vec3 aNormal;//法向量

out vec2 vTexCoor;//纹理
out vec4 vAmbient;//环境光
out vec4 vDiffUse;//(漫)反色光
out vec4 vSpecular;//(镜面)反色光

void pointLight(
    in vec3 lightPosition,
    in vec4 ambientLight,
    in vec4 diffUseLight,
    in vec4 specularLight,
    inout vec4 ambient,
    inout vec4 diffUse,
    inout vec4 specular
){
    //环境光计算
    ambient = ambientLight;



    //1. 标准化法向量
    vec3 normalTarget = aPosition + aNormal;
    vec3 newNormal = (uMMatrix*vec4(normalTarget,1)).xyz - (uMMatrix*vec4(aPosition,1)).xyz;
    newNormal = normalize(newNormal);

    //标准化观察点
    vec3 eye = uCamera - (uMMatrix*vec4(aPosition,1)).xyz;
    eye = normalize(eye);

    //标准化光源
    vec3 light = uLightLocation - (uMMatrix*vec4(aPosition,1)).xyz;
    light = normalize(light);

    //漫色光反色计算
    float nDotViewPosition = max(0.0, dot(newNormal,light));
    diffUse = diffUseLight * nDotViewPosition;

    //镜面反色光计算
    vec3 viewAndLightVec = normalize(eye + light);
    float LightStrength = 50.0;
    float nDotViewAndLight = dot(newNormal,viewAndLightVec);
    float powerFactor = max(0.0, pow(nDotViewAndLight,LightStrength);
    specular = specularLight*powerFactor;

}

void main(){

    gl_Position = uMVPMatrix*vec4(aPosition,1);
    vTexCoor = aTexCoor;
    vec4 ambientTemp=vec4(0.0,0.0,0.0,0.0);
    vec4 diffuseTemp=vec4(0.0,0.0,0.0,0.0);
    vec4 specularTemp=vec4(0.0,0.0,0.0,0.0);
    vec3 normalTemp = normalize(aNormal);
    pointLight(
        normalTemp,
        uLightLocation,
        vec4(0.05,0.05,0.05,1.0),
        vec4(1.0,1.0,1.0,1.0),
        vec4(0.3,0.3,0.3,1.0),
        ambientTemp,
        diffuseTemp,
        specularTemp
    )

    vAmbient = ambientTemp;
    vDiffUse = diffuseTemp;
    vSpecular = specularTemp;
}