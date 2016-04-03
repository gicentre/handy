package org.gicentre.tests;

import org.gicentre.handy.HandyPresets;
import org.gicentre.handy.HandyRenderer;
import org.gicentre.utils.FrameTimer;

import processing.core.PApplet;
import processing.core.PConstants;


// *****************************************************************************************
/** Simple sketch to test handy 3d shape building. 'H' toggles sketchiness on or off. 'A'
 *  changes hachure angle. Left and right arrow keys change vertex overshoot. Up and down
 *  arrows change degree of sketchiness.
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

public class Vertex3DTest extends PApplet 
{
	// ------------------------------ Starter method ------------------------------- 

	/** Creates a simple application to test handy line drawing.
	 *  @param args Command line arguments (ignored). 
	 */
	public static void main(String[] args)
	{   
		PApplet.main(new String[] {"org.gicentre.tests.Vertex3DTest"});
	}

	// ----------------------------- Object variables ------------------------------

	private HandyRenderer h;			// Does the sketchy rendering.
	private boolean isHandy;			// Toggles sketchy rendering on and off
	private float roughness;			// Degree of sketchiness.
	private FrameTimer timer;			// For rendering speed reporting.
	private float overshoot;			// Degree of vertex overshoot.
	private float angle;				// Hachure angle.

	private float xmag, ymag = 0;		// Rotation parameters.
	private float newXmag, newYmag = 0; 

	// ---------------------------- Processing methods -----------------------------

	/** Initial window settings prior to setup().
	 */
	public void settings()
	{   
		size(640,360,P3D);		
		pixelDensity(displayDensity());		// Use platform's maximum display density.
	}
	
	/** Sets up the sketch.
	 */
	@Override
	public void setup()
	{   
		timer = new FrameTimer();
		roughness = 1.5f;
		overshoot = 1.1f;
		angle = 45;
		h = HandyPresets.createMarker(this);
		h.setRoughness(roughness);
		h.setHachureAngle(angle);
		h.setHachurePerturbationAngle(0);
		fill(180,80,80);		
	}

	/** Draws some sketchy lines.
	 */
	public void draw()
	{
		background(235,215,182);
		timer.displayFrameRate();
		h.setSeed(1969);
		h.setStrokeWeight(4);
		h.setStrokeColour(color(0));
		
		float lengthA = 100;
		float lengthB = 60;
		
		// 2D rectangle to check styles are consistent.		
		h.rect(5, 5, 50, 30);

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
						
		h.beginShape(QUADS); 
		 h.vertex(-lengthA,  lengthA,  lengthB);
		 h.vertex( lengthA,  lengthA,  lengthB);
		 h.vertex( lengthA, -lengthA,  lengthB);
		 h.vertex(-lengthA, -lengthA,  lengthB);

		 h.vertex( lengthA,  lengthA,  lengthB);
		 h.vertex( lengthA,  lengthA, -lengthB);
		 h.vertex( lengthA, -lengthA, -lengthB);
		 h.vertex( lengthA, -lengthA,  lengthB);
		  
		 h.vertex( lengthA,  lengthA, -lengthB);
		 h.vertex(-lengthA,  lengthA, -lengthB);
		 h.vertex(-lengthA, -lengthA, -lengthB);
		 h.vertex( lengthA, -lengthA, -lengthB);
		  
		 h.vertex(-lengthA,  lengthA, -lengthB);
		 h.vertex(-lengthA,  lengthA,  lengthB);
		 h.vertex(-lengthA, -lengthA,  lengthB);
		 h.vertex(-lengthA, -lengthA, -lengthB);

		 h.vertex(-lengthA,  lengthA, -lengthB);
		 h.vertex( lengthA,  lengthA, -lengthB);
		 h.vertex( lengthA,  lengthA,  lengthB);
		 h.vertex(-lengthA,  lengthA,  lengthB);
		  
		 h.vertex(-lengthA, -lengthA, -lengthB);
		 h.vertex( lengthA, -lengthA, -lengthB);
		 h.vertex( lengthA, -lengthA,  lengthB);
		 h.vertex(-lengthA, -lengthA,  lengthB);
		h.endShape();
		  
		// Pencil guide lines.
		h.setStrokeWeight(2);
		h.setStrokeColour(color(0,100));
		h.line(-lengthA*overshoot,lengthA,lengthB, lengthA*overshoot,lengthA,lengthB);
		h.line( lengthA,lengthA*overshoot, lengthB, lengthA, -lengthA*overshoot, lengthB);
		h.line( lengthA*overshoot, -lengthA,  lengthB,-lengthA*overshoot, -lengthA,  lengthB);
		h.line(-lengthA, -lengthA*overshoot,  lengthB,-lengthA,  lengthA*overshoot,  lengthB);

		h.line( lengthA,  lengthA,  lengthB*overshoot, lengthA,  lengthA, -lengthB*overshoot);
		h.line( lengthA,  lengthA*overshoot, -lengthB, lengthA, -lengthA*overshoot, -lengthB);
		h.line( lengthA, -lengthA, -lengthB*overshoot, lengthA, -lengthA,  lengthB*overshoot);

		h.line( lengthA*overshoot,  lengthA, -lengthB,-lengthA*overshoot,  lengthA, -lengthB);
		h.line(-lengthA,  lengthA*overshoot, -lengthB,-lengthA, -lengthA*overshoot, -lengthB);
		h.line(-lengthA*overshoot, -lengthA, -lengthB, lengthA*overshoot, -lengthA, -lengthB);

		h.line(-lengthA,  lengthA, -lengthB*overshoot,-lengthA,  lengthA,  lengthB*overshoot);
  		h.line(-lengthA, -lengthA,  lengthB*overshoot,-lengthA, -lengthA, -lengthB*overshoot);	

		popMatrix(); 
	}

	@Override
	public void keyPressed()
	{
		if ((key =='h') || (key == 'H'))
		{
			isHandy = !isHandy;
			h.setIsHandy(isHandy);
		}
		
		else if ((key == 'a') || (key == 'A'))
		{
			angle++;
			h.setHachureAngle(angle);
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
			else if ((keyCode == PConstants.LEFT) && (overshoot > 1))
			{
				overshoot *= 0.99;
			}
			else if (keyCode == PConstants.RIGHT)
			{
				overshoot *= 1.01;
			}
		}
	}
}