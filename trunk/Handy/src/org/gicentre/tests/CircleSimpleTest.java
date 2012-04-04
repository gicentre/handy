package org.gicentre.tests;

import org.gicentre.handy.HandyRenderer;

import processing.core.PApplet;
import processing.pdf.*;


// *****************************************************************************************
/** Simple sketch to draw a single circle in a handy style that can be saved as a PDF image.
 *  @author Jo Wood, giCentre, City University London.
 *  @version 1.0, 26th March, 2012
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
public class CircleSimpleTest extends PApplet 
{
	// ------------------------------ Starter method ------------------------------- 

	/** Creates a simple application to test handy line drawing.
	 *  @param args Command line arguments (ignored). 
	 */
	public static void main(String[] args)
	{   
		PApplet.main(new String[] {"org.gicentre.tests.CircleSimpleTest"});
	}

	// ----------------------------- Object variables ------------------------------

	private HandyRenderer h;
	private boolean savePDF;

	// ---------------------------- Processing methods -----------------------------

	/** Sets up the sketch.
	 */
	public void setup()
	{   
		//size(500,500, PDF, "circle.pdf");
		size(500,500);
		smooth();
		savePDF = false;

		h = new HandyRenderer(this);
		h.setRoughness(3);
		strokeWeight(4);
	}


	/** Draws some sketchy lines.
	 */
	public void draw()
	{
		if (savePDF)
		{
			beginRecord(PDF, "frame-####.pdf");
		}
		
		background(255);

		h.ellipse(width/2,height/2,width/2,height/2);
		
		if (savePDF)
		{
		    endRecord();
			savePDF = false;
		}

		//exit();
		noLoop();
	}

	@Override
	public void keyPressed()
	{

		if (key == ' ')
		{
			loop();
		}
		else if (key == 'p')
		{
			savePDF = true;
			loop();
		}
	}
}
