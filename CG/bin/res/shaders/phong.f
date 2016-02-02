#version 120

varying vec3 N;
varying vec3 v;
uniform vec3 lichtPos = vec3(1,1,10);
varying vec4 color;


void main(void){

	vec3 L = normalize(lichtPos - v);   
	vec3 E = normalize(-v);
	vec3 R = normalize(-reflect(L,N));  

   //Ambienter Anteil:  
   float Iamb = 0.3;    

   //Diffuser Anteil:  
   float Idiff = max(dot(N,L), 0.0);

   //Spekularer Anteil:
   float Ispec = 1.0 * pow(max(dot(R,E),0.0),15);

  
   gl_FragColor = color * ( Iamb + Idiff + Ispec);     
}