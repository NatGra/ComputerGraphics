varying vec2 uv;
uniform sampler2D s;

void main(){
	gl_FragColor = texture2D(s,uv);
}