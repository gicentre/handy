package org.gicentre.tests;

import org.gicentre.handy.HandyRenderer;
import org.gicentre.utils.FrameTimer;

import processing.core.PApplet;
import processing.core.PConstants;

//*****************************************************************************************
/** Simple sketch to test handy 3d cone building. 'H' toggles sketchy rendering, up and 
 *  down arrows control roughness.
 *  @author Jo Wood, giCentre, City University London.
 *  @version 2.0, 2nd April, 2016
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

public class ConeTest extends PApplet 
{
	// ------------------------------ Starter method ------------------------------- 

	/** Creates a simple application to test handy shape drawing.
	 *  @param args Command line arguments (ignored). 
	 */
	public static void main(String[] args)
	{   
		PApplet.main(new String[] {"org.gicentre.tests.ConeTest"});
	}

	// ----------------------------- Object variables ------------------------------

	private HandyRenderer h;			// Does the sketchy rendering.
	private boolean isHandy;			// Toggles handy rendering on and off.
	private float roughness;			// Degree of sketchiness.
	private FrameTimer timer;			// For rendering speed reporting.

	// ---------------------------- Processing methods -----------------------------

	/** Initial window settings prior to setup().
	 */
	@Override
	public void settings()
	{   
		size(800,800, P3D);					// Requires P3D for 3d rendering. 
		pixelDensity(displayDensity());		// Use platform's maximum display density.
	}
	
	/** Sets up the sketch.
	 */
	public void setup()
	{   
		roughness = 2;
		h = new HandyRenderer(this);
		h.setRoughness(roughness);
		h.setFillGap(2);
		strokeWeight(3);
		fill(205,185,162);
		h.setBackgroundColour(color(205,185,162));

		timer = new FrameTimer();
	}

	/** Draws a sketchy cone.
	 */
	public void draw()
	{
		background(235,215,182);
		h.setSeed(1956);
		timer.displayFrameRate();

		lights();
		translate(width / 2, height / 2);
		rotateY(map(mouseX, 0, width, 0, 2*PI));
		rotateZ(map(mouseY, 0, height, 0, -2*PI));
		stroke(0);
		translate(0, -40, 0);
		drawCylinder(10, 180, 200, 16);
	}

	/** Responds to key presses to control appearance of the cone.
	 */
	@Override
	public void keyPressed()
	{
		if ((key =='h') || (key == 'H'))
		{
			isHandy = !isHandy;
			h.setIsHandy(isHandy);
		}

		if (key == PConstants.CODED)
		{
			if (keyCode == PConstants.UP)
			{
				roughness *= 1.1;
				h.setRoughness(roughness);
			}
			else if (keyCode == PConstants.DOWN)
			{
				roughness *= 0.9;
				h.setRoughness(roughness);
			}
		}
	}

	// ---------------------------- Private methods --------------------------------

	/** Draws a cone, cylinder or pyramid. See http://www.processing.org/learning/3d/vertices.html
	 */
	void drawCylinder(float topRadius, float bottomRadius, float tall, int sides) 
	{
		float angle = 0;
		float angleIncrement = TWO_PI / sides;
		h.beginShape(QUAD_STRIP);
		for (int i = 0; i < sides + 1; ++i)
		{
			h.vertex(topRadius*cos(angle), 0, topRadius*sin(angle));
			h.vertex(bottomRadius*cos(angle), tall, bottomRadius*sin(angle));
			angle += angleIncrement;
		}
		h.endShape();

		// If it is not a cone, draw the circular top cap
		if (topRadius != 0) 
		{
			angle = 0;
			h.beginShape(TRIANGLE_FAN);

			// Center point
			h.vertex(0, 0, 0);
			for (int i = 0; i < sides + 1; i++)
			{
				h.vertex(topRadius * cos(angle), 0, topRadius * sin(angle));
				angle += angleIncrement;
			}
			h.endShape();
		}

		// If it is not a cone, draw the circular bottom cap
		if (bottomRadius != 0)
		{
			angle = 0;
			h.beginShape(TRIANGLE_FAN);

			// Center point
			h.vertex(0, tall, 0);
			for (int i = 0; i < sides + 1; i++)
			{
				h.vertex(bottomRadius * cos(angle), tall, bottomRadius * sin(angle));
				angle += angleIncrement;
			}
			h.endShape();
		}
	}
}


