package org.gicentre.tests;

import org.gicentre.handy.HandyRecorder;
import org.gicentre.handy.HandyRenderer;
import org.gicentre.utils.move.*;				// For zooming.

import processing.core.PApplet;
import processing.core.PConstants;

//*****************************************************************************************
/** Simple sketch to test handy curved shape drawing. Should draw two identical shapes.
 *  'H' to toggle sketchy rendering, left and right arrows to change curve tightness.
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

public class CurvedLinesTest extends PApplet 
{
	// ------------------------------ Starter method ------------------------------- 

	/** Creates a simple application to test handy line drawing.
	 *  @param args Command line arguments (ignored). 
	 */
	public static void main(String[] args)
	{   
		PApplet.main(new String[] {"org.gicentre.tests.CurvedLinesTest"});
	}

	// ----------------------------- Object variables ------------------------------

	private HandyRenderer h;			// Does the sketchy rendering.
	private boolean isHandy;			// Toggles handy rendering on and off.
	private float tightness;			// Curve tightness.
	private ZoomPan zoomer;				// For zooming and panning.
	private HandyRecorder handyRec;
		
	// ---------------------------- Processing methods -----------------------------

	/** Initial window settings prior to setup().
	 */
	@Override
	public void settings()
	{   
		size(300,300);
		
		// Should work with all Processing 3 renderers.
		// size(800,800, P2D);
		// size(800,800, P3D);
		// size(800,800, FX2D);
		
		pixelDensity(displayDensity());		// Use platform's maximum display density.
	}
	
	/** Sets up the sketch.
	 */
	@Override
	public void setup()
	{   
		zoomer = new ZoomPan(this);
		tightness = 0;
		curveTightness(tightness);
		
		isHandy = true;
		h = new HandyRenderer(this);
		h.setIsHandy(isHandy);
		handyRec = new HandyRecorder(h);
	}
		
	/** Draws a sketchy curve.
	 */
	@Override
	public void draw()
	{
		background(255);
		zoomer.transform();
		
		stroke(80,30,20);
		fill(80,30,20,100);
		curveTightness(tightness);
		
		float cx = 10;
		float cy = height/2;
		
		h.beginShape();
		 h.curveVertex(cx+84, cy+ 91);
		 h.curveVertex(cx+84, cy+ 91);
		 h.curveVertex(cx+68, cy+ 19);
		 h.curveVertex(cx+21, cy+ 17);
		 h.curveVertex(cx+32, cy+100);
		 h.curveVertex(cx+32, cy+100);
		 h.vertex(cx+84, cy+91);
		h.endShape();
		
		// Second shape using the handy recorder. Should appear identical to first shape but to the right.
		cx  = width/2;
		beginRecord(handyRec);
		 beginShape();
		  curveVertex(cx+84, cy+ 91);
		  curveVertex(cx+84, cy+ 91);
		  curveVertex(cx+68, cy+ 19);
		  curveVertex(cx+21, cy+ 17);
		  curveVertex(cx+32, cy+100);
		  curveVertex(cx+32, cy+100);
		  vertex(cx+84, cy+91);
		 endShape();
		endRecord();
		
		noLoop();
	}
		
	/** Changes the curve tightness in response to the left and right arrow keys.
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
				tightness -= 0.05f;
				loop();
			}
			else if (keyCode == PConstants.RIGHT)
			{
				tightness += 0.05f;
				loop();
			}
		}
	}
	
	@Override
	/** Ensure sketch is redrawn when the mouse is dragged for zooming/panning.
	 */
	public void mouseDragged()
	{
		loop();
	}
}
