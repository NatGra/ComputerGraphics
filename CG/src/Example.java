import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;


import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;

import lenz.opengl.AbstractSimpleBase;
import lenz.opengl.utils.ShaderProgram;
import lenz.opengl.utils.Texture;

/**
 * Folgende Funktionen muss Ihr Programm alle in mindestens einer Ausführung für die Bestnote enthalten:
 * 
 * Zwei verschiedene "volldimensionale" 3D-Objekte
 * Verwendung von Transformationen (translate, rotate, scale) für eine zeitgesteuerte Animation (z.B. fortlaufende Transformation)
 * Interaktionsmöglichkeit über Tastatur oder Maus (*)
 * Oberfläche mit Textur (Bilddatei im Projekt enthalten) (*)
 * Vertex- und Fragment-Shader für Phong-Schattierung
 * Vertex- und Fragment-Shader für eine einfache prozedurale Textur
 * Verwendung einer "eigenen" Matrix-Klasse (z.B. Matrix4f) in Shadern a la OpenGL 3.
 * Bonuspunkte gibt es für die Nutzung weiterer OpenGL 3-Techniken (Modelle als VAO/VBO, GLSL ab Version 1.5).
 * 
 * Die mit (*) gekennzeichneten Teile zählen halb so viel wie die anderen.**/


public class Example extends AbstractSimpleBase {
//	Keyboard-Steuerung
	float x = 0; 
	float y = 0;
	float z = -10;
	
//	Rotation
	float rotate = 0;
	float rotate2 = 0;
	
//	Textur
	Texture rost, falcon;
	
//	Shader
	ShaderProgram spTexture, spGouraud, spPhong, spProc;
	
//	Animation
	long lastFrame = 0;
	
	
	public static void main(String[] args) {
		new Example().start();
	}
	

	@Override
	protected void initOpenGL() {
		glMatrixMode(GL_PROJECTION);
		glFrustum(-5, 5, -5, 5, 8, 30);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_CULL_FACE);
		
		rost = new Texture("textur_rost.jpg", 3);
		falcon = new Texture("millenium-falcon_v1.png", 3);
		
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
//		glTexParameteri(GL_TEXTURE_MAG_FILTER, GL_NEAREST);

//		glShadeModel(GL_FLAT);  				//für Shading auskommentieren!!!
		
		
		spTexture = new ShaderProgram("texture");
		spGouraud = new ShaderProgram("gouraud");
		spPhong = new ShaderProgram("phong");
		spProc = new ShaderProgram("procTex");
		
//		glUniform3f(glGetUniformLocation(spGouraud.getId(), "lichtPos"), 1,7,12);
	}

	@Override
	protected void render() {
		
		rotate += 0.5f;
		rotate2 += 0.1f;
		
		glClear(GL_COLOR_BUFFER_BIT);
		
		glLoadIdentity();
		glTranslatef(-5, 0, -15);
		glRotatef(rotate, 1, 1, 1); 
		wuerfel();
		
		glLoadIdentity();
		glTranslatef(x, y, z);
		keyboard();
		raumschiff();
		
		//zeitgesteuerte Animation:		
		long now = Sys.getTime();
		double s =+ (now-lastFrame)/1000.0;

		if(s > 0.01){
			lastFrame = Sys.getTime();
			s = 0;
		
			glLoadIdentity();
			glTranslatef(2, 2, -10);
			glRotatef(rotate2, 0, 1, 1); 
			pyramide();
		}
			
	}
	
	public void wuerfel(){
//		glEnable(GL_TEXTURE_2D);
//		glUseProgram(spTexture.getId());
//		glBindTexture(GL_TEXTURE_2D, rost.getId());
		
		glUseProgram(spPhong.getId());
		
		glBegin(GL_QUADS); 		
		//front
		glColor3f( 1, 1, 1); 
		
		glNormal3f( 0, 0, 4);
		
		glTexCoord2f(0, 0);
		glVertex3f( -1,  1,  -1);
		glTexCoord2f(0, 1);
		glVertex3f( -1, -1,  -1);
		glTexCoord2f(1, 1);
		glVertex3f(  1, -1,  -1);
		glTexCoord2f(1,  0);
		glVertex3f(  1,  1,  -1);
		
		glEnd();
		glDisable(GL_TEXTURE_2D);
//		glUseProgram(0);

		glBegin(GL_QUADS);
		//rechts
		glColor3f( 0, 1, 1); 
		
		glNormal3f( 4, 0, 0);
		
		glVertex3f(  1,  1,  -1);
		glVertex3f(  1, -1,  -1);
		glVertex3f(  1, -1,  -3);
		glVertex3f(  1,  1,  -3);


		//hinten
		glColor3f( 0, 0, 1); 
		
		glNormal3f( 0, 0, -4);
		
		glVertex3f( -1,  1,  -3);  
		glVertex3f(  1,  1,  -3);  
		glVertex3f(  1, -1,  -3);
		glVertex3f( -1, -1,  -3);
		
		
		//links
		glColor3f( 1, 0, 0); 
		
		glNormal3f( -4, 0, 0);
		
		glVertex3f( -1,   1,  -3);
		glVertex3f( -1,  -1,  -3);
		glVertex3f( -1,  -1,  -1);
		glVertex3f( -1,   1,  -1);


		//oben
		glColor3f( 1, 0, 1); 
		
		glNormal3f( 0, 4, 0);
		
		glVertex3f(  1,  1,  -1);
		glVertex3f(  1,  1,  -3);
		glVertex3f( -1,  1,  -3);
		glVertex3f( -1,  1,  -1);


		//unten
		glColor3f( 0, 1, 0); 
		
		glNormal3f( 0, -4, 0);
		
		glVertex3f( -1, -1,  -1);
		glVertex3f( -1, -1,  -3);
		glVertex3f(  1, -1,  -3);
		glVertex3f(  1, -1,  -1);
		
		glEnd();
		glUseProgram(0);
		
	}

	
	public void pyramide(){
		
		glUseProgram(spProc.getId());
		glBegin(GL_TRIANGLES);
		
		// front
		glColor3f(1, 0, 0);			// rot
	    glVertex3f( 0,  1, 0);
	    glColor3f(0, 1, 0);			// grün
	    glVertex3f(-1, -1, 1);
	    glColor3f(0, 0, 1);			// blau
	    glVertex3f( 1, -1, 1);
	    
	    // hinten
	    glColor3f(1, 0, 0);			// rot
	    glVertex3f(0, 1, 0);
	    glColor3f(0, 1, 0);			// grün
	    glVertex3f(1, -1, -1);
	    glColor3f(0, 0, 1);			// blau
	    glVertex3f(-1, -1, -1);
	    
	    glEnd();
	    glUseProgram(0);
	    
		
		glUseProgram(spGouraud.getId());
		glBegin(GL_TRIANGLES);
	    // rechts
	    glColor3f(1, 0, 0);			// rot
	    glVertex3f(0, 1, 0);
	    glColor3f(0, 0, 1);			// blau
	    glVertex3f(1, -1, 1);
	    glColor3f(0, 1, 0);			// grün
	    glVertex3f(1, -1, -1);

	    glUseProgram(0);
	 
	    // links
	    glColor3f(1, 0, 0);			// rot
	    glVertex3f( 0, 1, 0);
	    glColor3f(0, 0, 1);			// blau
	    glVertex3f(-1, -1, -1);
	    glColor3f(0, 1, 0);			// grün
	    glVertex3f(-1, -1, 1);
	    glEnd();
	    
	    glBegin(GL_QUADS);
	    //unten
		glColor3f( 1, 0, 0); 		// rot
	    glVertex3f(-1, -1,  1);
	    glColor3f( 0, 1, 0); 		// blau
	    glVertex3f(-1, -1, -1);
	    glColor3f( 0, 0, 1); 		// grün
	    glVertex3f( 1, -1, -1);
	    glColor3f( 0, 0, 0); 		// schwarz
	    glVertex3f( 1, -1,  1);
		glEnd();
		
	}

	
	public void raumschiff(){	
		glUseProgram(spTexture.getId());
		
		glEnable (GL_BLEND);									// Träger-Objekt transparent
		glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, falcon.getId());
		
		glBegin(GL_QUADS); 		
		
		glTexCoord2f(0, 0);
		glVertex3f( -1.0f,  1.5f,  -1.0f);
		
		glTexCoord2f(0, 1);
		glVertex3f( -1.0f, -1.0f,  -1.0f);
		
		glTexCoord2f(1, 1);
		glVertex3f(  1.0f, -1.0f,  -1.0f);
		
		glTexCoord2f(1,  0);
		glVertex3f(  1.0f,  1.5f,  -1.0f);
		
		glEnd();
		glDisable(GL_TEXTURE_2D);
		
		glUseProgram(0);
	}
	
	
	public void keyboard(){

			
		Keyboard.next();
		if(Keyboard.getEventKeyState()){
			if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT) {
				x= x+0.1f;
				glRotatef(270, 0, 0, 1);
				
			}else if (Keyboard.getEventKey() == Keyboard.KEY_DOWN) {
				y= y-0.1f;
				glRotatef(180, 0, 0, 1);
				
			}else if (Keyboard.getEventKey() == Keyboard.KEY_UP) {
				y= y+0.1f;
				
			}else if (Keyboard.getEventKey() == Keyboard.KEY_LEFT) {
				x= x-0.1f;
				glRotatef(90, 0, 0, 1);
				
			}else if (Keyboard.getEventCharacter() == 'a') {
				z= z-0.1f;
				
			}else if (Keyboard.getEventCharacter() == 's') {
				z= z+0.1f;
			}
			
		}
		
	}
	
}
