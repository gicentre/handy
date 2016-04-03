package org.gicentre.tests;

import java.util.ArrayList;

import org.gicentre.handy.HandyPresets;
import org.gicentre.handy.HandyRenderer;
import org.gicentre.handy.Simplifier;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

// *****************************************************************************************
/** Simple mouse-controlled painting application to test line and polygon drawing and writing
 *  to an offscreen buffer. Drag mouse to draw lines; shift-drag to draw polygons.
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


public class OffscreenBufferTest extends PApplet 
{
	// ------------------------------ Starter method ------------------------------- 

	/** Creates the paint program as an application.
	 *  @param args Command line arguments (ignored). 
	 */
	public static void main(String[] args)
	{   
		PApplet.main(new String[] {"org.gicentre.tests.OffscreenBufferTest"});
	}

	// ----------------------------- Object variables ------------------------------

	private HandyRenderer h;
	private Mark currentMark;
	private float roughness;

	private PGraphics pg;				// Offscreen buffer.
	
	// ---------------------------- Processing methods -----------------------------

	/** Initial window settings prior to setup().
	 */
	@Override
	public void settings()
	{   
		size(1200,800);
		
		// Should work with all Processing 3 renderers.
		// size(1200,800, P2D);
		// size(1200,800, P3D);
		// size(1200,800, FX2D);
		
		// TODO: PROCESSING BUG IN OFFSCREEN BUFFER IN RETINA MODE PREVENTS THIS WORKING AT PIXEL DENSITY 2
		//pixelDensity(displayDensity());		// Use platform's maximum display density.
	}
	
	/** Sets up the sketch.
	 */
	@Override
	public void setup()
	{
		// Create the offscreen buffer into which accumulated drawing will placed.
		pg = createGraphics(width,height);		
		pg.beginDraw();
		pg.background(255);
		pg.endDraw();
		
		h = HandyPresets.createPencil(this);
		currentMark = new Mark(h);
		roughness = 1;
		h.setRoughness(roughness);
	}

	// ------------------------ Processing draw -------------------------

	/** Draws the user-generated lines and polygons.
	 */
	@Override
	public void draw()
	{
		background(255);
		
		h.setSeed(12345);		// Stops jittering on redraw.
		
		// Draw accumulated image.
		image (pg,0,0);

		// Draw currently active shape on top of image.
		currentMark.draw();

		noLoop();
	}

	/** Adds the current pointer location to the current shape when mouse is pressed.
	 */
	@Override
	public void mousePressed()
	{
		currentMark.add(mouseX,mouseY); 
		loop(); 
	}

	/** Adds the current pointer location to the current shape when mouse is dragged.
	 */
	@Override
	public void mouseDragged()
	{
		currentMark.add(mouseX,mouseY); 
		loop(); 
	}

	/** Stores the current shape when the mouse is released.
	 */
	@Override
	public void mouseReleased()
	{		
		// Add the active shape to the image buffer
		h.setGraphics(pg);
		pg.beginDraw();
		currentMark.draw();
		pg.endDraw();
		h.setGraphics(this.g);
		
		// Reset new shape.
		currentMark = new Mark(h);
		loop();
	}

	/** Responds to key pressed by allowing appearance of objects to be changed and line/polygon drawing to
	 *  be controlled with the shift key.
	 */
	@Override
	public void keyPressed()
	{		
		if (key==CODED)
		{
			if (keyCode == SHIFT)
			{  
				currentMark.setIsPolygon(true);
				loop();
			}

			else if (keyCode == LEFT)
			{
				roughness *=0.9f;
				h.setRoughness(roughness);
				println("Roughness down to "+roughness);
				loop();
			}
			else if (keyCode == RIGHT)
			{
				roughness *=1.1f;
				h.setRoughness(roughness);

				println("Roughness up to "+roughness);
				loop();
			}
		}
	}

	/** Completes the current polygon (if it is being drawn) on mouse release.
	 */
	@Override
	public void keyReleased()
	{
		if (key==CODED)
		{
			if (keyCode == SHIFT)
			{  
				currentMark.setIsPolygon(false);
				loop();
			}
		}
	}


	// ----------------------------------- Nested classes -----------------------------------------

	// Represents a single graphical mark such as a line or polygon.
	private class Mark
	{
		private ArrayList<PVector> coords;
		private float[] xCoords,yCoords;
		private int fillColour;
		private boolean isPolygon;
		private HandyRenderer handy;


		public Mark(HandyRenderer h)
		{
			this.handy = h;
			coords = new ArrayList<PVector>();
			fillColour = color(80,30,30);
			isPolygon = false;
		}


		void add(float x, float y)
		{
			coords.add(new PVector(x,y));

			Simplifier.simplify(coords,1);
			xCoords = Simplifier.getSimplifiedX();
			yCoords = Simplifier.getSimplifiedY();
		}

		void setIsPolygon(boolean isPolygon)
		{
			this.isPolygon = isPolygon;
		}


		void draw()
		{
			if (xCoords == null)
			{
				return;
			}

			if (isPolygon)
			{
				fill(fillColour);
				handy.shape(xCoords,yCoords);
			}
			else
			{
				handy.polyLine(xCoords,yCoords);
			}
		}
	}
}
