package com.lplus.graphics;

public class LplusShaderStr {

    public final static String strVertexShaderDefault =
	        "uniform mat4 uMVPMatrix;" +
	        "attribute vec4 aPosition;" +
	        "attribute vec2 aTextureCoord;" +
	        "varying vec2 vTextureCoord;" +
	        "void main() {" +
	        "  gl_Position = uMVPMatrix * aPosition;" +
	        "  vTextureCoord = aTextureCoord;" +
	        "}";
    

    public final static String strFragmentShaderDefault = 
	    	"precision mediump float;" +
		    "uniform sampler2D sTexture;" +
		    "uniform vec4 uColor;" +
		    "varying vec2 vTextureCoord;" +
		    "void main() {" +
		    "  gl_FragColor = texture2D(sTexture, vTextureCoord) * uColor;" +
		    "}";
    
    public final static String strVertexShader =
    		"uniform mat4	uMVPMatrix;" +
    		"uniform mat4	uModelMatrix;" +
    		"uniform mat4	uNormalMatrix;" +
    		"uniform vec3	uLightPosition[64];" +
    		"uniform float	uLightRange[64];" +
    		"uniform float	uLightIntensity[64];" +
    		"uniform int	uLightCount;" +
    		"uniform vec3	uCameraPosition;" +
    		"uniform vec3	uMaterialAmbient;" +
    		"uniform vec3	uMaterialDiffuse;" +
    		"uniform vec3	uMaterialSpecular;" +
    		"uniform float	uMaterialSpecularFactor;" +
    		"attribute vec4 aPosition;" +
    		"attribute vec4 aNormal;" +
    		"attribute vec2 aTextureCoord;" +
    		"varying vec3	vDiffuse;" +
    		"varying vec3	vSpecular;" +
    		"varying vec2	vTexCoord;" +
    		"void main(void) {" +
    		"	vTexCoord = aTextureCoord;" +
    		"	float diffuse = 0.0;" +
    		"	float specular = 0.0;" +
    		"	vec3 transPosition = vec3(uModelMatrix * aPosition);" +
    		"	vec3 transNormal = normalize(vec3(uNormalMatrix * aNormal));" +
    		"	vec3 viewVec = normalize(uCameraPosition - transPosition);" +
    		"	for (int i = 0; i < uLightCount; i++) {" +
    		"		vec3 lightDirection = uLightPosition[i] - transPosition;" +
    		"		float dist = length(lightDirection);" +
    		"		lightDirection = normalize(lightDirection);" +
    		"		float attenuation = uLightIntensity[i] * uLightRange[i] / (uLightRange[i] + dist);" +
    		"		diffuse += (clamp(dot(transNormal, lightDirection), 0.0, 1.0) * attenuation);" +
    		"		vec3 hVec = normalize(viewVec + lightDirection);" +
    		"		specular += pow(clamp(dot(hVec, transNormal), 0.0, 1.0), uMaterialSpecularFactor) * attenuation;" +
    		"	}" +
    		"	vDiffuse = uMaterialAmbient + (uMaterialDiffuse * diffuse);" +
    		"	vSpecular = uMaterialSpecular * specular;" +
    		"	gl_Position = uMVPMatrix * aPosition;" +
    		"}";
    
    public final static String strFragmentShader = 
    		"precision mediump float;" +
    		"uniform sampler2D sTexture;" +
    		"uniform vec4 uColor;" +
    		"varying vec2 vTexCoord;" +
    		"varying vec3 vDiffuse;" +
    		"varying vec3 vSpecular;" +
    	    "void main(void) {" +
    	    "	vec4 texColor = texture2D(sTexture, vTexCoord) * uColor;" +
    		"	gl_FragColor.rgb = clamp(vDiffuse * texColor.rgb + vSpecular, 0.0, 1.0);" +
    		"	gl_FragColor.a = clamp(texColor.a, 0.0, 1.0);" +
    	    "}";
}
