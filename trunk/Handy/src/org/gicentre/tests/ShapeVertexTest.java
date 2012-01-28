package org.gicentre.tests;

import org.gicentre.handy.HandyRenderer;
import org.gicentre.utils.move.ZoomPan;

import processing.core.PApplet;

//*****************************************************************************************
/** Simple sketch to test beginShape(), endShape() and vertex handy rendering.
 *  @author Jo Wood, giCentre, City University London.
 *  @version 1.0, 28th January 2012.
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
public class ShapeVertexTest extends PApplet 
{
	// ------------------------------ Starter method ------------------------------- 

	/** Creates a simple application to test handy line drawing.
	 *  @param args Command line arguments (ignored). 
	 */
	public static void main(String[] args)
	{   
		PApplet.main(new String[] {"org.gicentre.tests.ShapeVertexTest"});
	}

	// ----------------------------- Object variables ------------------------------

	private HandyRenderer h;
	private ZoomPan zoomer;
	
	// ---------------------------- Processing methods -----------------------------

	/** Sets up the sketch.
	 */
	public void setup()
	{   
		size(400,250);
		smooth();
		fill(168,212,176);
		stroke(120);
		strokeWeight(3);

		zoomer = new ZoomPan(this);
		h = new HandyRenderer(this);
		h.setFillGap(2);
	}

	/** Draws a sketchy cross at the mouse position.
	 */
	public void draw()
	{
		background(255);  
		zoomer.transform();
		drawCross(width/2,height/2,70);
		
		noLoop();
	}
	
	@Override
	/** Redraws screen when mouse is dragged, to allow zooming and panning.
	 */
	public void mouseDragged()
	{
		loop();
	}
	// ------------------------------ Private methods ------------------------------

	/** Draws a cross at the given x,y position.
	 *  @param x x coordinate of the cross centre.
	 *  @param y y coordinate of the cross centre.
	 *  @param armLength Length of one arm of the cross in pixels.
	 */
	private void drawCross(float x, float y, float armLength)
	{
		float halfWidth = armLength/3;

		h.beginShape();
		h.vertex(x-halfWidth, y-halfWidth);
		h.vertex(x-halfWidth, y-armLength);
		h.vertex(x+halfWidth, y-armLength);

		h.vertex(x+halfWidth, y-halfWidth);
		h.vertex(x+armLength, y-halfWidth);
		h.vertex(x+armLength, y+halfWidth);

		h.vertex(x+halfWidth, y+halfWidth);
		h.vertex(x+halfWidth, y+armLength);
		h.vertex(x-halfWidth, y+armLength);

		h.vertex(x-halfWidth, y+halfWidth);
		h.vertex(x-armLength, y+halfWidth);
		h.vertex(x-armLength, y-halfWidth);

		h.endShape(CLOSE);
	}

}
