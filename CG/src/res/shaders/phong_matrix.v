varying vec3 N;
varying vec3 v;
varying vec4 color;
uniform mat4 mvpMatrix;
uniform mat4 projection;
uniform mat4 normalMatrix;

void main(){     
   v = vec3(mvpMatrix * gl_Vertex);       
   N = normalize(vec3(normalMatrix * vec4(gl_Normal, 0)));
   color = gl_Color;
   gl_Position = projection * mvpMatrix * gl_Vertex; 
}