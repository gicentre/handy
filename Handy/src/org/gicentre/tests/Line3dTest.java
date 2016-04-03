package org.gicentre.tests;

import org.gicentre.handy.HandyRenderer;

import processing.core.PApplet;
import processing.core.PConstants;

// *****************************************************************************************
/** Simple sketch to test handy 3d line drawing. 'H' to toggle sketchy rendering, up and 
 *  down arrows to change degree of sketchiness. Left and right arrows to change the vertex 
 *  overshoot. Move mouse to rotate cube.
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

public class Line3dTest extends PApplet 
{
	// ------------------------------ Starter method ------------------------------- 

	/** Creates a simple application to test handy line drawing.
	 *  @param args Command line arguments (ignored). 
	 */
	public static void main(String[] args)
	{   
		PApplet.main(new String[] {"org.gicentre.tests.Line3dTest"});
	}

	// ----------------------------- Object variables ------------------------------

	private HandyRenderer h;			// Does the sketchy rendering.
	private boolean isHandy;			// Toggles sketchy rendering on and off
	private float roughness;			// Degree of sketchiness.
	private float overshoot;			// Degree of vertex overshoot.
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
		noStroke(); 
		roughness = 1;
		overshoot = 1.1f;
		h = new HandyRenderer(this);
		h.setRoughness(roughness);
	}

	/** Draws some sketchy 3d lines.
	 */
	@Override
	public void draw()
	{
		background(255);
		h.setSeed(1969);
		float unitLen = 100;

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


		stroke(50,50,200,180);
		strokeWeight(1);
				
		line(-unitLen,  unitLen,  unitLen, unitLen,  unitLen,  unitLen);
		line( unitLen,  unitLen,  unitLen, unitLen, -unitLen,  unitLen);
		line( unitLen, -unitLen,  unitLen,-unitLen, -unitLen,  unitLen);
		line(-unitLen, -unitLen,  unitLen,-unitLen,  unitLen,  unitLen);

		line( unitLen,  unitLen,  unitLen, unitLen,  unitLen, -unitLen);
		line( unitLen,  unitLen, -unitLen, unitLen, -unitLen, -unitLen);
		line( unitLen, -unitLen, -unitLen, unitLen, -unitLen,  unitLen);

		line( unitLen,  unitLen, -unitLen,-unitLen,  unitLen, -unitLen);
		line(-unitLen,  unitLen, -unitLen,-unitLen, -unitLen, -unitLen);
		line(-unitLen, -unitLen, -unitLen, unitLen, -unitLen, -unitLen);

		line(-unitLen,  unitLen, -unitLen,-unitLen,  unitLen,  unitLen);
		line(-unitLen, -unitLen,  unitLen,-unitLen, -unitLen, -unitLen);
		
		stroke(0);
		strokeWeight(2);
		
		h.line(-unitLen*overshoot,  unitLen,  unitLen, unitLen*overshoot,  unitLen,  unitLen);
		h.line( unitLen,  unitLen*overshoot,  unitLen, unitLen, -unitLen*overshoot,  unitLen);
		h.line( unitLen*overshoot, -unitLen,  unitLen,-unitLen*overshoot, -unitLen,  unitLen);
		h.line(-unitLen, -unitLen*overshoot,  unitLen,-unitLen,  unitLen*overshoot,  unitLen);

		h.line( unitLen,  unitLen,  unitLen*overshoot, unitLen,  unitLen, -unitLen*overshoot);
		h.line( unitLen,  unitLen*overshoot, -unitLen, unitLen, -unitLen*overshoot, -unitLen);
		h.line( unitLen, -unitLen, -unitLen*overshoot, unitLen, -unitLen,  unitLen*overshoot);

		h.line( unitLen*overshoot,  unitLen, -unitLen,-unitLen*overshoot,  unitLen, -unitLen);
		h.line(-unitLen,  unitLen*overshoot, -unitLen,-unitLen, -unitLen*overshoot, -unitLen);
		h.line(-unitLen*overshoot, -unitLen, -unitLen, unitLen*overshoot, -unitLen, -unitLen);

		h.line(-unitLen,  unitLen, -unitLen*overshoot,-unitLen,  unitLen,  unitLen*overshoot);
		h.line(-unitLen, -unitLen,  unitLen*overshoot,-unitLen, -unitLen, -unitLen*overshoot);

		popMatrix(); 
	}

	/** Responds to key presses to alter appearance of sketch shapes.
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