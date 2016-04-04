package org.gicentre.tests;

import org.gicentre.handy.HandyRecorder;
import org.gicentre.handy.HandyRenderer;
import org.gicentre.utils.FrameTimer;

import processing.core.PApplet;
import processing.core.PConstants;

// *****************************************************************************************
/** Test the handy recorder for 3d sketches. 'H' to toggle sketchy rendering, up and 
 *  down arrows to change degree of sketchiness.
 *  @author Jo Wood, giCentre, City University London.
 *  @version 2.0, 3rd April, 2016
 */ 
// *****************************************************************************************

/* This file is part of Handy sketchy drawing library. Handy is free software: you can 
 * redistribute it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Handy is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this
 * source code (see COPYING.LESSER included with this source code). If not, see 
 * http://www.gnu.org/licenses/.
 */

public class HandyRecorder3dTest extends PApplet 
{
	// ------------------------------ Starter method ------------------------------- 

	/** Creates a simple application to test handy line drawing.
	 *  @param args Command line arguments (ignored). 
	 */
	public static void main(String[] args)
	{   
		PApplet.main(new String[] {"org.gicentre.tests.HandyRecorder3dTest"});
	}

	// ----------------------------- Object variables ------------------------------

	private HandyRenderer h;			// Does the sketchy rendering.
	private boolean isHandy;			// Toggles sketchy rendering on and off
	private float roughness;			// Degree of sketchiness.
	private HandyRecorder handyRec;		// For sketchy rendering with normal processing commands.
	private FrameTimer timer;			// For rendering speed reporting.
	private float fov;					// Field of view of camera.

	// ---------------------------- Processing methods -----------------------------

	/** Initial window settings prior to setup().
	 */
	public void settings()
	{   
		size(800,600,P3D);		
		pixelDensity(displayDensity());		// Use platform's maximum display density.
	}

	/** Sets up the sketch.
	 */
	@Override
	public void setup()
	{   
		noStroke(); 
		roughness = 2;
		fov = PI/6;
		timer = new FrameTimer();

		h = new HandyRenderer(this);
		h.setRoughness(roughness);
		h.setFillGap(1);
		handyRec = new HandyRecorder(h);
	}

	/** Draws some 3d boxes that slowly ride and fall.
	 */
	@Override
	public void draw()
	{
		timer.displayFrameRate();
		h.setSeed(1234);		// To stop jittering.

		beginRecord(handyRec);

		// Set up camera position first.
		beginCamera();
		camera();
		rotateX(radians(65));
		rotateZ(radians(25));
		translate(width*0.2f, -height*1.3f, -width*0.9f);
		endCamera();
		
		rotateZ(radians(-mouseX));		// Rotate view with mouse movement.

		// Perspective transformation.
		float cameraZ = (height/2f) / tan(fov/2f);
		perspective(fov, (float)(width)/(float)height, cameraZ/10f, cameraZ*10f);

		// Rendering style.
		background(255);
		stroke(0);
		strokeWeight(3);

		// Set up lighting.
		lights();
		pointLight(51, 102, 126, 35, 40, 36);
		ambientLight(10,10, 30);
		directionalLight(25, 50, 25, 0.1f, 0.1f, -1);
		spotLight(60, 0, 0, 80, 20, 40, -1, 0, 0, PI/2, 2);
		lightFalloff(1f, 0.001f, 0f);
		lightSpecular(204, 204, 204);
		
		// Base mat.
		fill(70);
		noFill();
		box(950,950,0);
		fill(255);
		
		// Draw a grid of moving buildings.
		for (int y=-400; y<=400; y+=100)
		{
			for (int x=-400; x<=400; x+=100)
			{
				float d = 1000*pow(noise(0.01f*x, 0.01f*y,millis()*0.00002f), 5);
				pushMatrix();
				translate(x,y,d/2);
				box(80, 80, d);
				popMatrix();
			}
		}

		endRecord();	// End of sketchy recording.

		// Some perspective lines in a non sketchy style
		strokeWeight(0.5f);
		stroke(100,150,255);
		
		line(0,0,0,9999999,0,0);
		line(0,-400,0,9999999,-400,0);
		line(0,400,0,9999999,400,0);
		
		line(0,0,0,-9999999,0,0);
		line(0,-400,0,-9999999,-400,0);
		line(0,400,0,-9999999,400,0);
		
		line(0,0,0,0,9999999,0);
		line(-400,0,0,-400,9999999,0);
		line(400,0,0,400,9999999,0);
		
		line(0,0,0,0,-9999999,0);
		line(-400,0,0,-400,-9999999,0);
		line(400,0,0,400,-9999999,0);
		
	}

	/** Responds to key presses to alter appearance of sketch shapes.
	 */
	@Override
	public void keyPressed()
	{
		if ((key =='h') || (key =='H'))
		{
			isHandy = !isHandy;
			h.setIsHandy(isHandy);
		}

		if (key == PConstants.CODED)
		{
			if (keyCode == PConstants.UP)
			{
				roughness *= 1.1;
				h.setRoughness(roughness);
			}
			else if (keyCode == PConstants.DOWN)
			{
				roughness *= 0.9;
				h.setRoughness(roughness);
			}
			else if (keyCode == PConstants.LEFT)
			{
				fov *= 0.9;
				
			}
			else if (keyCode == PConstants.RIGHT)
			{
				fov *= 1.1;
			}
		}
	}
}