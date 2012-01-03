package com.fhgames.rolldice;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

/**
 * This class is an object representation of 
 * a Cube containing the vertex information,
 * texture coordinates, the vertex indices
 * and drawing functionality, which is called 
 * by the renderer.
 *  
 * @author Savas Ziplies (nea/INsanityDesign)
 */
public class Cube {

	/** The buffer holding the vertices */
	private FloatBuffer vertexBuffer;
	/** The buffer holding the texture coordinates */
	private FloatBuffer textureBuffer;
	/** The buffer holding the indices */
	private ByteBuffer indexBuffer;
	
	/** Our texture pointer */
	private int[] textures = new int[6];

	/** The initial vertex definition */	
	private float vertices[] = {
						-1.0f, -1.0f, 1.0f, 
						1.0f, -1.0f, 1.0f, 	
						-1.0f, 1.0f, 1.0f, 	
						1.0f, 1.0f, 1.0f ,
						
						1.0f, -1.0f, 1.0f,
						1.0f, -1.0f, -1.0f, 
						1.0f, 1.0f, 1.0f, 
						1.0f, 1.0f, -1.0f ,
						
						1.0f, -1.0f, -1.0f, 
						-1.0f, -1.0f, -1.0f, 
						1.0f, 1.0f, -1.0f, 
						-1.0f, 1.0f, -1.0f,
						
						-1.0f, -1.0f, -1.0f, 
						-1.0f, -1.0f, 1.0f, 
						-1.0f, 1.0f, -1.0f, 
						-1.0f, 1.0f, 1.0f ,
						
						-1.0f, -1.0f, -1.0f, 
						1.0f, -1.0f, -1.0f, 
						-1.0f, -1.0f, 1.0f, 
						1.0f, -1.0f, 1.0f,
						
						-1.0f, 1.0f, 1.0f, 
						1.0f, 1.0f, 1.0f, 
						-1.0f, 1.0f, -1.0f, 
						1.0f, 1.0f, -1.0f };

	/** The initial texture coordinates (u, v) */	
	private float texture[] = {
						0.0f, 0.0f,
			    		0.0f, 1.0f,
			    		1.0f, 0.0f,
			    		1.0f, 1.0f,

						
				        0.0f, 0.0f, 
						0.0f, 1.0f, 
						1.0f, 0.0f, 
						1.0f, 1.0f,
														
				        0.0f, 0.0f, 
						0.0f, 1.0f, 
						1.0f, 0.0f, 
						1.0f, 1.0f,
						
						0.0f, 0.0f, 
						0.0f, 1.0f, 
						1.0f, 0.0f, 
						1.0f, 1.0f,
						
						0.0f, 0.0f, 
						0.0f, 1.0f, 
						1.0f, 0.0f, 
						1.0f, 1.0f,
						
						0.0f, 0.0f, 
						0.0f, 1.0f, 
						1.0f, 0.0f, 
						1.0f, 1.0f
	};
	/** The initial indices definition */
	private byte indices[] = { 0, 1, 3, 0, 3, 2,
	4, 5, 7, 4, 7, 6,
	8, 9, 11, 8, 11, 10,	
	12, 13, 15, 12, 15, 14, 
	16, 17, 19, 16, 19, 18,
	20, 21, 23, 20, 23, 22 };
	
	private int bitmaps[] = {R.drawable.dice1, R.drawable.dice2,
							 R.drawable.dice3, R.drawable.dice4,
							 R.drawable.dice5, R.drawable.dice6};

	/**
	 * The Cube constructor.
	 * 
	 * Initiate the buffers.
	 */
	public Cube() {
		//
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		//
		byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);

		//
		indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}

	/**
	 * The object own drawing function.
	 * Called from the renderer to redraw this instance
	 * with possible changes in values.
	 * 
	 * @param gl - The GL Context
	 * @param filter - Which texture filter to be used
	 */
	public void draw(GL10 gl, int filter) {
		
		//Enable the vertex, texture state
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		//Set the face rotation
		gl.glFrontFace(GL10.GL_CCW);
		
		//Point to our buffers
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
				
		for(int i=0;i<6;i++)
		{
			//Bind the texture according to the set texture filter
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[i]);
			indexBuffer.position(6*i);    
			//Draw the vertices as triangles, based on the Index Buffer information
			gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_BYTE, indexBuffer);

		}
		
		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}

	/**
	 * Load the textures
	 * 
	 * @param gl - The GL Context
	 * @param context - The Activity context
	 */
	public void loadGLTexture(GL10 gl, Context context) {

		//Generate there texture pointer
		gl.glGenTextures(6, textures, 0);
				
		Bitmap bitmap = null;
		InputStream is = null;
		for(int i=0;i<6;i++)
		{
			is = context.getResources().openRawResource(bitmaps[i]);
			
			try {
				//BitmapFactory is an Android graphics utility for images
				bitmap = BitmapFactory.decodeStream(is);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[i]);
				gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
				gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
				//GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
				
				if(gl instanceof GL11) {
					gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
					GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
				//
				} else {
					buildMipmap(gl, bitmap);
				}
				
			} finally {
				//Always clear and close
				try {
					is.close();
					is = null;
				} catch (IOException e) {
				}
			}

			bitmap.recycle();
		}
		
		
	}
	
	private void buildMipmap(GL10 gl, Bitmap bitmap) {
		int level = 0;
		//
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();

		//
		while(height >= 1 || width >= 1) {
			//First of all, generate the texture from our bitmap and set it to the according level
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bitmap, 0);
			
			//
			if(height == 1 || width == 1) {
				break;
			}

			//Increase the mipmap level
			level++;

			//
			height /= 2;
			width /= 2;
			Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, width, height, true);
			
			//Clean up
			bitmap.recycle();
			bitmap = bitmap2;
		}
	}

}
