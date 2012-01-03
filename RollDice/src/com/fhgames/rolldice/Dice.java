package com.fhgames.rolldice;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.hardware.SensorEvent;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.view.MotionEvent;


public class Dice extends GLSurfaceView implements Renderer {
	
	/** Cube instance */
	private Cube cube1;	
	private Cube cube2;
	
	/* Rotation values */
	private float xrot;					//X Rotation
	private float yrot;					//Y Rotation

	/* Rotation speed values */
	private float xspeed;				//X Rotation Speed ( NEW )
	private float yspeed;				//Y Rotation Speed ( NEW )
	
	private float z = -5.0f;			//Depth Into The Screen ( NEW )
	
	private int filter = 0;				//Which texture filter? ( NEW )
	private float tray;
	private float trax;
	

	
	/*
	 * These variables store the previous X and Y
	 * values as well as a fix touch scale factor.
	 * These are necessary for the rotation transformation
	 * added to this lesson, based on the screen touches. ( NEW )
	 */
	private float oldX;
    private float oldY;
	private final float TOUCH_SCALE = 1.5f;
	
	/** The Activity Context */
	private Context context;
	
	/**
	 * Instance the Cube object and set the Activity Context 
	 * handed over. Initiate the light buffers and set this 
	 * class as renderer for this now GLSurfaceView.
	 * Request Focus and set if focusable in touch mode to
	 * receive the Input from Screen and Buttons  
	 * 
	 * @param context - The Activity Context
	 */
	public Dice(Context context) {
		super(context);
		
		//Set this as Renderer
		this.setRenderer(this);
		//Request focus, otherwise buttons won't react
		this.requestFocus();
		this.setFocusableInTouchMode(true);
		
		//
		this.context = context;		

		cube1 = new Cube();
		cube2 = new Cube();
		
	}

	/**
	 * The Surface is created/init()
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {		
		
		//Settings
		gl.glDisable(GL10.GL_DITHER);				//Disable dithering ( NEW )
		gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 	//Black Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 
		
		//Load the texture for the cube once during Surface creation
		cube1.loadGLTexture(gl, this.context);
		cube2.loadGLTexture(gl, this.context);
	}

	/**
	 * Here we do our drawing
	 */
	public void onDrawFrame(GL10 gl) {
		
		//Clear Screen And Depth Buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		gl.glLoadIdentity();					//Reset The Current Modelview Matrix
	
		//Drawing
		gl.glTranslatef(-0.5f, diceY, z);			//Move z units into the screen
		gl.glScalef(0.2f, 0.2f, 0.2f); 			//Scale the Cube to 80 percent, otherwise it would be too large for the screen
		
		//Rotate around the axis based on the rotation matrix (rotation, x, y, z)
		gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);	//X
		gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);	//Y
	
		
		cube1.draw(gl, filter);					//Draw the Cube
		
		//Drawing
		gl.glLoadIdentity(); 
		gl.glTranslatef(0.5f, diceY, z);			
		gl.glScalef(0.2f, 0.2f, 0.2f); 			
		gl.glRotatef(xrot, 0.0f, 1.0f, 0.0f);	//X
		gl.glRotatef(yrot, 1.0f, 0.0f, 0.0f);	//Y
		cube2.draw(gl, filter);					//Draw the Cube
		
		//Change rotation factors
		xrot += xspeed;
		yrot += yspeed;
	}		

	/**
	 * If the surface changes, reset the view
	 */ 
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if(height == 0) { 						//Prevent A Divide By Zero By
			height = 1; 						//Making Height Equal One
		}

		gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
		gl.glLoadIdentity(); 					//Reset The Projection Matrix

		//Calculate The Aspect Ratio Of The Window
		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
		gl.glLoadIdentity(); 					//Reset The Modelview Matrix
				
	}
	
	/**
	 * Override the touch screen listener.
	 * 
	 * React to moves and presses on the touchscreen.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		/*
		float x = event.getX();
        float y = event.getY();
        
        //If a touch is moved on the screen
        if(event.getAction() == MotionEvent.ACTION_MOVE) {
        	//Calculate the change
         	float dx = x - oldX;
	        float dy = y - oldY;
        	
     		xrot += dy * TOUCH_SCALE;
	        yrot += dx * TOUCH_SCALE;

        } 

        
        //Remember the values
        oldX = x;
        oldY = y;*/
        
               
        
        
        //We handled the event
		return true;
	}

	
	float diceX,diceY,fx,fy;
	boolean rollStartedX = false;
	boolean rollStartedY = false;
	public void rollDice(SensorEvent se)
	{
		float x = se.values[0];
		float y = se.values[1]; 
		float dx = x - fx;
        float dy = y - fy;
        
        if(dx > 5)
        {
        	rollStartedX = true;
        	xrot += 20 * TOUCH_SCALE;
        	System.out.println("x:"+dx+"y:"+dy);
        }
        if(dy > 5)
        {
        	rollStartedY = true;
        	yrot += 20 * TOUCH_SCALE; 
        	System.out.println("x:"+dx+"y:"+dy);
        }
		fx = x;
		fy = y;
		
		if(dx < 1 && dy < 1 && rollStartedX && rollStartedY)   //Demek ki sallama durmus
			diceY = 1.0f; 
	}

}
