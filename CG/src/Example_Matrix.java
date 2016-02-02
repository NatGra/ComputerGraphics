import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
//import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import lenz.opengl.AbstractSimpleBase;
import lenz.opengl.utils.ShaderProgram;
import lenz.opengl.utils.Texture;


//import java.io.FileNotFoundException;
import java.nio.FloatBuffer;



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


public class Example_Matrix extends AbstractSimpleBase {
	
	Matrix4f projection = new Matrix4f();
	
//	Keyboard-Steuerung
	float x = 0; 
	float y = 0;
	float z = -10;
	
//	Rotation
	float rotate = 0;
	
//	Textur
	Texture falcon;
	
//	Shader
	ShaderProgram spTextureMatrix, spPhongMatrix, spProcMatrix;
	
//	Animation
	long lastFrame = 0;
	
	
	public static void main(String[] args) {
		new Example_Matrix().start();
	}
	

	@Override
	protected void initOpenGL() {
		falcon = new Texture("millenium-falcon_v1.png", 3);
				
		spTextureMatrix = new ShaderProgram("texture_matrix");
		spPhongMatrix = new ShaderProgram("phong_matrix");
		spProcMatrix = new ShaderProgram("procTex_matrix");

		glMatrixMode(GL_PROJECTION);
		glMatrixMode(GL_MODELVIEW);
		glShadeModel(GL_FLAT);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		
		int f = 100;
		int d = 1;
		int b = 2;
		int h = 2;
		
		projection.m00 = (2*d / b);
		projection.m11 = (2*d / h);
		projection.m22 = (-f-d) / (f-d);
		projection.m32 = (-2*f*d) / (f-d);
		projection.m23 = (-1);
		projection.m33 = 0;
		
		glUniform3f(glGetUniformLocation(spTextureMatrix.getId(), "lichtPos"), 0,7,12);
		glUniform3f(glGetUniformLocation(spPhongMatrix.getId(),   "lichtPos"), 0,7,12);	
		glUniform3f(glGetUniformLocation(spProcMatrix.getId(),    "lichtPos"), 0,1,5);

		projBuffer(spTextureMatrix, projection);
		projBuffer(spPhongMatrix, projection);
		projBuffer(spProcMatrix, projection);
		
	}

	@Override
	protected void render() {
		
		Matrix4f mvp = new Matrix4f();
		
		rotate += 0.02f;

 		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
 		
 		mvp.translate(new Vector3f(5, 5, -15));
 		mvp.rotate(rotate, new Vector3f(1, 0, 0));
 		mvp.rotate(rotate, new Vector3f(0, 1, 0));
		mvpBuffer(spPhongMatrix, mvp);
		wuerfel();
		mvp.setIdentity();
		
		mvp.translate(new Vector3f(-4, 0, -6));
 		mvp.rotate(rotate/4, new Vector3f(0, 1, 0));
 		mvp.rotate(rotate/4, new Vector3f(1, 0, 0));
		mvpBuffer(spProcMatrix, mvp);
		pyramide();
		mvp.setIdentity();
		
		//zeitgesteuerte Animation:		
		long now = Sys.getTime();
		double s =+ (now-lastFrame)/1000.0;

		if(s > 0.01){
			lastFrame = Sys.getTime();
			s = 0;
			
			mvp.translate(new Vector3f(x, y, z));
			keyboard(mvp);
			mvpBuffer(spTextureMatrix, mvp);
			raumschiff();
			mvp.setIdentity();

		}
			
	}
	
	
	public void wuerfel(){
		
		glBegin(GL_QUADS); 		
		//front
		glColor3f( 1, 1, 1); 	
		glNormal3f( 0, 0, 4);
		
		glVertex3f( -1,  1,  -1);
		glVertex3f( -1, -1,  -1);
		glVertex3f(  1, -1,  -1);
		glVertex3f(  1,  1,  -1);

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
		
	}
	
	public void pyramide(){
		glBegin(GL_TRIANGLES);
		
		// front
		glColor3f(1, 0, 0);			// rot
	    glVertex3f( 0,  1, 0);
	    glColor3f(0, 1, 0);			// grün
	    glVertex3f(-1, -1, 1);
	    glColor3f(0, 0, 1);			// blau
	    glVertex3f( 1, -1, 1);
	    
	    // links
	    glColor3f(1, 0, 0);			// rot
	    glVertex3f( 0, 1, 0);
	    glColor3f(0, 0, 1);			// blau
	    glVertex3f(-1, -1, -1);
	    glColor3f(0, 1, 0);			// grün
	    glVertex3f(-1, -1, 1);
	    
	    // hinten
	    glColor3f(1, 0, 0);			// rot
	    glVertex3f(0, 1, 0);
	    glColor3f(0, 1, 0);			// grün
	    glVertex3f(1, -1, -1);
	    glColor3f(0, 0, 1);			// blau
	    glVertex3f(-1, -1, -1);
	    
	    // rechts
	    glColor3f(1, 0, 0);			// rot
	    glVertex3f(0, 1, 0);
	    glColor3f(0, 0, 1);			// blau
	    glVertex3f(1, -1, 1);
	    glColor3f(0, 1, 0);			// grün
	    glVertex3f(1, -1, -1);
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
		
		glEnable (GL_BLEND);
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
	}

	
	public void keyboard(Matrix4f mvp){

			
		Keyboard.next();
		if(Keyboard.getEventKeyState()){
			if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT) {
				x= x+0.1f;
				mvp.rotate((float)((3*Math.PI)/2), new Vector3f(0, 0, 1));
				
			}else if (Keyboard.getEventKey() == Keyboard.KEY_DOWN) {
				y= y-0.1f;
				mvp.rotate((float)(Math.PI), new Vector3f(0, 0, 1));
				
			}else if (Keyboard.getEventKey() == Keyboard.KEY_UP) {
				y= y+0.1f;
				
			}else if (Keyboard.getEventKey() == Keyboard.KEY_LEFT) {
				x= x-0.1f;
				mvp.rotate((float)((Math.PI)/2), new Vector3f(0, 0, 1));
				
			}else if (Keyboard.getEventCharacter() == 'a') {
				z= z-0.1f;
				
			}else if (Keyboard.getEventCharacter() == 's') {
				z= z+0.1f;
			}
			
		}
		
	}

		
	public void mvpBuffer(ShaderProgram sp, Matrix4f mvp){
		FloatBuffer mvpBuffer = BufferUtils.createFloatBuffer(16);
		 mvp.store(mvpBuffer);
		 mvpBuffer.flip();
		 glUseProgram(sp.getId());
		 glUniformMatrix4(
				 glGetUniformLocation(sp.getId(), "mvpMatrix"), 
				 false, mvpBuffer);
		 
		 Matrix4f.invert(mvp,null).store(mvpBuffer);
		 mvpBuffer.flip();
		 glUniformMatrix4(
				 glGetUniformLocation(sp.getId(), "normalMatrix"), 
				 true, mvpBuffer); 
	}
	
	public void projBuffer(ShaderProgram sp, Matrix4f mvp){
		FloatBuffer mvpBuffer = BufferUtils.createFloatBuffer(16);
		 mvp.store(mvpBuffer);
		 mvpBuffer.flip();
		 glUseProgram(sp.getId());
		 glUniformMatrix4(
				 glGetUniformLocation(sp.getId(), "projection"),
				 false, mvpBuffer);
		 
	}
	
}
