varying vec4 color;

void main(void){
	vec4 farbe = color.bgra;
	farbe.r = sin(farbe.r*200.0);
	farbe.g = cos(farbe.g*400.0);
	farbe.b = sin(farbe.b*300.0)*sin(farbe.b*300.0);
	gl_FragColor = vec4( farbe.r, farbe.g, farbe.b, 1.0);
}