varying vec2 uv;
uniform mat4 mvpMatrix;
uniform mat4 projection;

void main(){
	uv = gl_MultiTexCoord0.xy;
	gl_Position = projection * mvpMatrix * gl_Vertex;  
}