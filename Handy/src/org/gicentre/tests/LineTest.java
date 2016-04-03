package org.gicentre.tests;

import org.gicentre.handy.HandyRenderer;
import org.gicentre.utils.move.*;				// For zooming.

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

// *****************************************************************************************
/** Simple sketch to test handy line drawing by drawing three parallel sketchy lines with 
 *  the central one on top of a non-sketchy line. H key toggles sketchy rendering on or off.
 *  Left and right arrow keys control the degree of sketchiness. Zoom and pan by dragging 
 *  the mouse, R to reset view.
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

public class LineTest extends PApplet 
{
	// ------------------------------ Starter method ------------------------------- 

	/** Creates a simple application to test handy line drawing.
	 *  @param args Command line arguments (ignored). 
	 */
	public static void main(String[] args)
	{   
		PApplet.main(new String[] {"org.gicentre.tests.LineTest"});
	}

	// ----------------------------- Object variables ------------------------------

	private HandyRenderer h;			// Does the sketchy rendering.
	private ZoomPan zoomer;				// For zooming and panning.
	private boolean isHandy;			// Toggles handy rendering on and off.
	private float roughness;			// Degree of sketchiness.
			
	// ---------------------------- Processing methods -----------------------------

	/** Initial window settings prior to setup().
	 */
	@Override
	public void settings()
	{   
		size(600,200);
		
		// Should work with all Processing 3 renderers.
		// size(600,200, P2D);
		// size(600,200, P3D);
		// size(600,200, FX2D);
		
		pixelDensity(displayDensity());		// Use platform's maximum display density.
	}
	
	/** Sets up the sketch.
	 */
	@Override
	public void setup()
	{   
		zoomer = new ZoomPan(this);
		
		isHandy = true;
		h = new HandyRenderer(this);
		h.setIsHandy(isHandy);
		roughness = 1;
		h.setRoughness(roughness);
	}
	
	
	/** Draws three parallel lines.
	 */
	@Override
	public void draw()
	{
		background(255);
		zoomer.transform();
			
		PVector p1 = new PVector(100,100);
		PVector p2 = new PVector(width-100,height-100);
		
		// Draw guides for middle line
		pushStyle();
		strokeWeight(0.3f);
		stroke(150,0,0);
		float radius = 4*roughness;
		
		ellipse(p1.x,p1.y,radius,radius);
		ellipse(p2.x,p2.y,radius,radius);
		line(p1.x,p1.y,p2.x,p2.y);
		
		popStyle();
		
		h.line(p1.x,p1.y-50,p2.x,p2.y-50);	
		h.line(p1.x,p1.y,p2.x,p2.y);
		h.line(p1.x,p1.y+50,p2.x,p2.y+50);

		noLoop();
	}
		
		
	/** Toggles sketchiness, resets zoomer and re-renders image according to the key pressed and
	 *  changes the roughness in response to the left and right arrow keys.
	 */
	@Override
	public void keyPressed()
	{
		if ((key =='h') || (key =='H'))
		{
			isHandy = !isHandy;
			h.setIsHandy(isHandy);
			loop();
		}
		else if ((key =='r') || (key =='R'))
		{
			zoomer.reset();
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
				roughness *= 0.9f;
				h.setRoughness(roughness);
				loop();
			}
			else if (keyCode == PConstants.RIGHT)
			{
				roughness *= 1.1f;
				h.setRoughness(roughness);
				loop();
			}
		}
	}
	
	/** Ensure sketch is redrawn when the mouse is dragged for zooming/panning.
	 */
	@Override
	public void mouseDragged()
	{
		loop();
	}
}
