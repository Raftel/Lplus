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
}
