package org.gicentre.tests;

import org.gicentre.handy.HandyRenderer;
import org.gicentre.utils.move.*;				// For zooming.

import processing.core.PApplet;
import processing.core.PConstants;

//*****************************************************************************************
/** Simple sketch to test handy curved line drawing.
 *  @author Jo Wood, giCentre, City University London.
 *  @version 1.0, 6th February, 2012
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

	private HandyRenderer h;
	private boolean isHandy;
	private float tightness;		// Curve tightness.
	private ZoomPan zoomer;
		
	// ---------------------------- Processing methods -----------------------------

	/** Sets up the sketch.
	 */
	public void setup()
	{   
		size(300,300);
		smooth();
		zoomer = new ZoomPan(this);
		tightness = 0;
		curveTightness(tightness);
		
		isHandy = true;
		h = new HandyRenderer(this);
		h.setIsHandy(isHandy);
	}
	
	
	/** Draws a sketchy curve.
	 */
	public void draw()
	{
		background(255);
		zoomer.transform();
		
		stroke(80,30,20);
		fill(80,30,20,100);
		float cx = width/2;
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
		
		noLoop();
	}
		
	@Override
	/** Changes the curve tightness in response to the left and right arrow keys.
	 */
	public void keyPressed()
	{
		if (key =='h')
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
				curveTightness(tightness);
				loop();
			}
			else if (keyCode == PConstants.RIGHT)
			{
				tightness += 0.05f;
				curveTightness(tightness);
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
