package com.fhgames.rolldice;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * The initial Android Activity, setting and initiating
 * the OpenGL ES Renderer Class @see Lesson07.java
 * 
 * @author Savas Ziplies (nea/INsanityDesign)
 */
public class Run extends Activity implements SensorEventListener {

	/** Our own OpenGL View overridden */
	private Dice dice;
	SensorManager sensorManager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setBackgroundColor(Color.DKGRAY);
		
		dice = new Dice(this);
		ll.addView(dice);
		setContentView(ll);
		
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);       
        sensorManager.registerListener(this, 
        		sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), 
        		SensorManager.SENSOR_DELAY_GAME);
	}
	
	/* (non-Javadoc)
	 * @see android.hardware.SensorEventListener#onAccuracyChanged(android.hardware.Sensor, int)
	 */
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		dice.rollDice(event); 		
		
	}


}