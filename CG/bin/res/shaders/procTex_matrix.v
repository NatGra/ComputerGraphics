varying vec4 color;
uniform mat4 mvpMatrix;
uniform mat4 projection;

void main(void){
	color = gl_Color;
	gl_Position = projection * mvpMatrix * gl_Vertex; 
}