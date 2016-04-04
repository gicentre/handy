package org.gicentre.tests;

import org.gicentre.handy.HandyPresets;
import org.gicentre.handy.HandyRecorder;
import org.gicentre.handy.HandyRenderer;
import org.gicentre.utils.FrameTimer;

import processing.core.PApplet;
import processing.core.PConstants;

// *****************************************************************************************
/** Simple sketch to test handy 3d box rendering. 'H' to toggle sketchy rendering.
 *  Left and right arrows to change angle of hachures. Up and down arrows to change degree
 *  of sketchiness.
 *  @author Jo Wood, giCentre, City University London.
 *  @version 2.0, 3rd April, 2016.
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

public class BoxTest extends PApplet 
{
	// ------------------------------ Starter method ------------------------------- 

	/** Creates a simple application to test handy line drawing.
	 *  @param args Command line arguments (ignored). 
	 */
	public static void main(String[] args)
	{   
		PApplet.main(new String[] {"org.gicentre.tests.BoxTest"});
	}

	// ----------------------------- Object variables ------------------------------

	private HandyRenderer h;			// Does the sketchy rendering.
	private float angle;				// Hachure angle.
	private boolean isHandy;			// Toggles handy rendering on and off.
	private float roughness;			// Degree of sketchiness.
	private FrameTimer timer;			// For rendering speed reporting.
	private HandyRecorder handyRec;		// Handy recorder for sketchy still with standard processing methods.
	
	private float xmag, ymag = 0;		// Rotation parameters
	private float newXmag, newYmag = 0; 

	// ---------------------------- Processing methods -----------------------------

	/** Initial window settings prior to setup().
	 */
	@Override
	public void settings()
	{   
		size(640,640, P3D);					// Requires P3D for 3d rendering. 
		pixelDensity(displayDensity());		// Use platform's maximum display density.
	}
	
	/** Sets up the sketch.
	 */
	@Override
	public void setup()
	{   
		timer = new FrameTimer();
		roughness = 1.5f;
		angle = 45;
	
		h = HandyPresets.createMarker(this);
		h.setFillWeight(2);
		h.setRoughness(roughness);
		h.setHachureAngle(angle);
		h.setHachurePerturbationAngle(0);
		handyRec = new HandyRecorder(h);
		fill(180,80,80);		
	}

	/** Draws a 3d box and reference rectangle.
	 */
	@Override
	public void draw()
	{
		background(235,215,182);
		timer.displayFrameRate();
		h.setSeed(1969);
		float lengthA = 250;
		float lengthB = 170;
		float lengthC = 100;
		
		// 2D rectangle to check styles are consistent.		
		h.rect(5, 5, 150, 100);

		pushMatrix(); 

		translate(width/2, height/2, -30); 

		newXmag = mouseX/(float)(width)  * PConstants.TWO_PI;
		newYmag = mouseY/(float)(height) * PConstants.TWO_PI;

		float diff = xmag-newXmag;
		if (abs(diff) >  0.01) 
		{ 
			xmag -= diff/4.0; 
		}

		diff = ymag-newYmag;
		if (abs(diff) >  0.01) 
		{ 
			ymag -= diff/4.0; 
		}

		rotateX(-ymag); 
		rotateY(-xmag); 
		
		translate(0,-150,0);
		h.box(lengthA,lengthB,lengthC);
		
		// This version uses the handy recorder but should appear identical to the other box.
		beginRecord(handyRec);
		translate(0,300,0);
		box(lengthA,lengthB,lengthC);
		endRecord();
		
		popMatrix(); 
	}

	/** Responds to key presses to control appearance of shapes.
	 */
	@Override
	public void keyPressed()
	{
		if ((key =='h') || (key == 'H'))
		{
			isHandy = !isHandy;
			h.setIsHandy(isHandy);
		}

		if (key == PConstants.CODED)
		{
			if (keyCode == PConstants.LEFT)
			{
				angle--;
				h.setHachureAngle(angle);
			}
			else if (keyCode == PConstants.RIGHT)
			{
				angle++;
				h.setHachureAngle(angle);
			}
			else if (keyCode == PConstants.UP)
			{
				roughness *= 1.1;
				h.setRoughness(roughness);
			}
			else if (keyCode == PConstants.DOWN)
			{
				roughness *= 0.9;
				h.setRoughness(roughness);
			}
		}
	}
}