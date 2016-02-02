varying vec2 green;
uniform sampler2D s;

void main(void){
	gl_FragColor = texture2D(s,green);
}