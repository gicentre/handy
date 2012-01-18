package org.gicentre.tests;

import java.util.ArrayList;

import org.gicentre.handy.HandyRenderer;
import org.gicentre.handy.Simplifier;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

// *****************************************************************************************
/** Simple mouse-controlled painting application to test line and polygon drawing.
 *  @author Jo Wood, giCentre, City University London.
 *  @version 1.0, 4th January, 2012.
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
public class Painter extends PApplet 
{
	// ------------------------------ Starter method ------------------------------- 

	/** Creates the paint program as an application.
	 *  @param args Command line arguments (ignored). 
	 */
	public static void main(String[] args)
	{   
		PApplet.main(new String[] {"org.gicentre.tests.Painter"});
	}

	// ----------------------------- Object variables ------------------------------

	private HandyRenderer h;
	private ArrayList<Mark> marks;
	private Mark currentMark;


	// ---------------------------- Processing methods -----------------------------

	public void setup()
	{
		size(1200,800);
		smooth();
		marks = new ArrayList<Mark>();
		currentMark = new Mark();
		h = new HandyRenderer(this);
	}

	// ------------------------ Processing draw -------------------------

	public void draw()
	{
		background(255);

		for (Mark mark : marks)
		{
			mark.draw();  
		}

		currentMark.draw();

		noLoop();
	}


	public void mousePressed()
	{
		currentMark.add(mouseX,mouseY); 
		loop(); 
	}


	public void mouseDragged()
	{
		currentMark.add(mouseX,mouseY); 
		loop(); 
	}

	public void mouseReleased()
	{
		marks.add(currentMark);
		currentMark = new Mark();
		loop();
	}

	public void keyPressed()
	{
		if (key==CODED)
		{
			if (keyCode == SHIFT)
			{  
				currentMark.setIsPolygon(true);
				loop();
			}
		}
	}

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
		ArrayList<PVector> coords;
		float[] xCoords,yCoords;
		int strokeColour, fillColour;
		boolean isPolygon;

		public Mark()
		{
			coords = new ArrayList<PVector>();
			strokeColour = color(0);
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
				h.shape(xCoords,yCoords);
			}
			else
			{
				h.polyLine(xCoords,yCoords);
			}
		}
	}
}
