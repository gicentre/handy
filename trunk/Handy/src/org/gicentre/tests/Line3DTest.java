package org.gicentre.tests;

import org.gicentre.handy.HandyRenderer;

import processing.core.PApplet;
import processing.core.PConstants;

// *****************************************************************************************
/** Simple sketch to test handy 3d line drawing.
 *  @author Jo Wood, giCentre, City University London.
 *  @version 1.0, 4th April, 2012
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

@SuppressWarnings("serial")
public class Line3DTest extends PApplet 
{
	// ------------------------------ Starter method ------------------------------- 

	/** Creates a simple application to test handy line drawing.
	 *  @param args Command line arguments (ignored). 
	 */
	public static void main(String[] args)
	{   
		PApplet.main(new String[] {"org.gicentre.tests.Line3DTest"});
	}

	// ----------------------------- Object variables ------------------------------

	private HandyRenderer h;

	private float angle;
	private boolean isHandy;
	private float roughness;
	private float xmag, ymag = 0;
	private float newXmag, newYmag = 0; 

	// ---------------------------- Processing methods -----------------------------

	/** Sets up the sketch.
	 */
	public void setup()
	{   
		size(640, 360, P3D); 
		noStroke(); 
		roughness = 1;
		h = new HandyRenderer(this);
		h.setRoughness(roughness);
	}

	/** Draws some sketchy lines.
	 */
	public void draw()
	{
		background(255);
		h.setSeed(1969);
		float unitLen = 90;

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


		stroke(200,50,50,180);
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
		
		h.line(-unitLen,  unitLen,  unitLen, unitLen,  unitLen,  unitLen);
		h.line( unitLen,  unitLen,  unitLen, unitLen, -unitLen,  unitLen);
		h.line( unitLen, -unitLen,  unitLen,-unitLen, -unitLen,  unitLen);
		h.line(-unitLen, -unitLen,  unitLen,-unitLen,  unitLen,  unitLen);

		h.line( unitLen,  unitLen,  unitLen, unitLen,  unitLen, -unitLen);
		h.line( unitLen,  unitLen, -unitLen, unitLen, -unitLen, -unitLen);
		h.line( unitLen, -unitLen, -unitLen, unitLen, -unitLen,  unitLen);

		h.line( unitLen,  unitLen, -unitLen,-unitLen,  unitLen, -unitLen);
		h.line(-unitLen,  unitLen, -unitLen,-unitLen, -unitLen, -unitLen);
		h.line(-unitLen, -unitLen, -unitLen, unitLen, -unitLen, -unitLen);

		h.line(-unitLen,  unitLen, -unitLen,-unitLen,  unitLen,  unitLen);
		h.line(-unitLen, -unitLen,  unitLen,-unitLen, -unitLen, -unitLen);

		popMatrix(); 
	}

	@Override
	public void keyPressed()
	{
		if (key =='h')
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
			
			println(roughness);
		}
	}
}