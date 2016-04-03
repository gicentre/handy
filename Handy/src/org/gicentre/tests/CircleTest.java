package org.gicentre.tests;

import org.gicentre.handy.HandyRenderer;
import org.gicentre.utils.move.ZoomPan;

import processing.core.PApplet;
import processing.core.PConstants;

//*****************************************************************************************
/** Draws a set of randomly positioned ellipses with sketchy shaded interiors. Can zoom and
 *  pan by dragging mouse; 'R' to reset zoom/pan. 'H' to toggle sketchy rendering. Left and
 *  right arrows to change angle of hachures. Up and down arrows to change degree of 
 *  sketchiness.
 *  A set of very small circles are drawn which should not be visible in the sketchy view
 *  but present as points in the non-sketchy view. 
 *  @author Jo Wood, giCentre, City University London.
 *  @version 2.0, 2nd April, 2016.
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

public class CircleTest extends PApplet 
{
	// ------------------------------ Starter method ------------------------------- 

	/** Creates a simple application to test handy line drawing.
	 *  @param args Command line arguments (ignored). 
	 */
	public static void main(String[] args)
	{   
		PApplet.main(new String[] {"org.gicentre.tests.CircleTest"});
	}

	// ----------------------------- Object variables ------------------------------

	private HandyRenderer h;			// Does the sketchy rendering.
	private ZoomPan zoomer;				// For zooming and panning.
	private float angle;				// Hachure angle.
	private boolean isHandy;			// Toggles handy rendering on and off.
	private float roughness;			// Degree of sketchiness.
	
	// ---------------------------- Processing methods -----------------------------

	/** Initial window settings prior to setup().
	 */
	public void settings()
	{   
		size(800,800);
		
		// Should work with all Processing 3 renderers.
		// size(800,800, P2D);
		// size(800,800, P3D);
		// size(800,800, FX2D);
		
		pixelDensity(displayDensity());		// Use platform's maximum display density.
	}
	
	/** Sets up the sketch.
	 */
	public void setup()
	{   
		zoomer = new ZoomPan(this);
		angle = -45;
		roughness = 1;
		isHandy = true;
		h = new HandyRenderer(this);
		h.setHachureAngle(angle);
		h.setHachurePerturbationAngle(5);
		h.setIsHandy(isHandy);
		h.setRoughness(roughness);
	}
	
	
	/** Draws some sketchy circles.
	 */
	public void draw()
	{
		background(255);
		zoomer.transform();
		stroke(80);
		strokeWeight(1f);
		noFill();
		
		h.setSeed(1234);
		randomSeed(1245);
		int numCircles = 40;
		
		for (int i=0; i<numCircles; i++)
		{
			fill(random(100,200),random(60,200), random(100,200));
			float diameter = random(50,200);
			h.ellipse(random(40,width-40),random(40,height-40),diameter,random(100,200));
		}
		
		// Test very small circles (should be invisible).
		for (int i=0; i<numCircles; i++)
		{
			h.ellipse(random(40,width-40),random(40,height-40),0,0.1f);
		}
		
		noLoop();
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
			loop();
		}
		else if (key == ' ')
		{
			loop();
		}
		
		if (key == PConstants.CODED)
		{
			if (keyCode == PConstants.LEFT)
			{
				angle--;
				h.setHachureAngle(angle);
				loop();
			}
			else if (keyCode == PConstants.RIGHT)
			{
				angle++;
				h.setHachureAngle(angle);
				loop();
			}
			else if (keyCode == PConstants.UP)
			{
				roughness *= 1.1;
				h.setRoughness(roughness);
				loop();
			}
			else if (keyCode == PConstants.DOWN)
			{
				roughness *= 0.9;
				h.setRoughness(roughness);
				loop();
			}
		}
	}
	
	/** Redraws when mouse is dragged to allow zooming and panning.
	 */
	@Override
	public void mouseDragged()
	{
		loop();
	}
}
