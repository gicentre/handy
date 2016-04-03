package org.gicentre.tests;

import org.gicentre.handy.HandyRenderer;

import processing.core.PApplet;

// *****************************************************************************************
/** Simple sketch to draw a single circle in a handy style that can be saved as a PDF image
 *  by pressing the 'P' key or SVG file with the 'S' key. Spacebar re-renders with a different
 *  random perturbation.
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

public class PDFAndSVGTest extends PApplet 
{
	// ------------------------------ Starter method ------------------------------- 

	/** Creates a simple application to test handy line drawing.
	 *  @param args Command line arguments (ignored). 
	 */
	public static void main(String[] args)
	{   
		PApplet.main(new String[] {"org.gicentre.tests.PDFAndSVGTest"});
	}

	// ----------------------------- Object variables ------------------------------

	private HandyRenderer h;
	private enum Output {SCREEN, PDF_FILE, SVG_FILE}
	private Output outType;

	// ---------------------------- Processing methods -----------------------------
	
	/** Initial window settings prior to setup().
	 */
	@Override
	public void settings()
	{   
		size(500,500);
		
		// Should work with all Processing 3 renderers.
		//size(500,500, P2D);
		// size(500,500, P3D);
		// size(500,500, FX2D);
		
		pixelDensity(displayDensity());		// Use platform's maximum display density.
	}

	/** Sets up the sketch.
	 */
	@Override
	public void setup()
	{   
		outType = Output.SCREEN;
		h = new HandyRenderer(this);
		h.setRoughness(2);
	}

	/** Draws a sketchy circle.
	 */
	@Override
	public void draw()
	{
		if (outType == Output.SVG_FILE)
		{
			beginRecord(SVG, "sketchyCircle.svg");
			h.setGraphics(recorder);
		}
		else if (outType == Output.PDF_FILE)
		{
			beginRecord(PDF, "sketchyCircle.pdf");
			h.setGraphics(recorder);
		}
				
		background(255);
		strokeWeight(4);
		stroke(0);
		
		h.ellipse(width/2,height/2,width/2,height/2);
		
		if (outType != Output.SCREEN)
		{
		    endRecord();
			outType = Output.SCREEN;
			h.setGraphics(this.g);
			loop();
		}
		else
		{
			noLoop();
		}
	}

	/** Responds to a key press.
	 */
	@Override
	public void keyPressed()
	{
		if (key == ' ')
		{
			loop();
		}
		else if ((key == 'p') || (key == 'P'))
		{
			outType = Output.PDF_FILE;
			loop();
		}
		else if ((key == 's') || (key == 'S'))
		{
			outType = Output.SVG_FILE;
			loop();
		}
	}
}
