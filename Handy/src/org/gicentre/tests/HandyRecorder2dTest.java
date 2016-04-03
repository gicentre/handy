package org.gicentre.tests;

import org.gicentre.handy.HandyRecorder;
import org.gicentre.handy.HandyRenderer;

import processing.core.PApplet;
import processing.core.PConstants;

// *****************************************************************************************
/** Tests the use of the HandyGraphics context to allow sketchy graphics without explicitly
 *  calling methods in the HandyRenderer.
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

public class HandyRecorder2dTest extends PApplet 
{
	// ------------------------------ Starter method ------------------------------- 

	/** Creates a simple application to test handy line drawing.
	 *  @param args Command line arguments (ignored). 
	 */
	public static void main(String[] args)
	{   
		PApplet.main(new String[] {"org.gicentre.tests.HandyRecorder2dTest"});
	}

	// ----------------------------- Object variables ------------------------------

	private HandyRenderer h;			// Does the sketchy rendering.
	private HandyRecorder handyRec;		// Graphics context in which to render.
	private boolean isHandy;			// Toggles handy rendering on and off.
	private float roughness;			// Degree of sketchiness.

	// ---------------------------- Processing methods -----------------------------

	/** Initial window settings prior to setup().
	 */
	public void settings()
	{   
		//size(800,800);

		// Should work with all Processing 3 renderers.
		// size(800,800, P2D);
		size(800,800, P3D);
		// size(800,800, FX2D);

		pixelDensity(displayDensity());		// Use platform's maximum display density.
	}

	/** Sets up the sketch.
	 */
	public void setup()
	{   
		h = new HandyRenderer(this);
		h.setFillGap(2f);
		handyRec = new HandyRecorder(h);
		isHandy = true;
		roughness = 1;
	}

	/** Draws a range of graphics objects using a HandyGraphics object.
	 */
	@Override
	public void draw()
	{
		background(255);
		strokeWeight(2);
		stroke(0);
		
		h.setSeed(1224);
		h.setRoughness(roughness);
		
		// Anything drawn before turning on the handy recorder should appear as normal
		line(mouseX,mouseY-100,mouseX,mouseY+100);
		line(mouseX-100,mouseY,mouseX+100,mouseY);
		noFill();
		strokeWeight(0.5f);
		ellipse(mouseX,mouseY,200,200);
				
		// Turn on Handy rendering at the start of each draw cycle.
		if (isHandy)
		{
			beginRecord(handyRec);
		}
		pushMatrix();
		rotate(radians(-10));
		translate(0,100);
		scale(1.5f);
		fill(100,100);
		
		line(10, 10, 200, 200);
		ellipse(100,100,20,20);
		rect(150,150,30,30);
		triangle(40, 20, 20, 40, 60, 30);
		beginShape();
		vertex(180,30);
		vertex(200,30);
		vertex(200,10);
		vertex(210,10);
		vertex(210,30);
		vertex(230,30);
		vertex(230,40);
		vertex(210,40);
		vertex(210,60);
		vertex(200,60);
		vertex(200,40);
		vertex(180,40);
		endShape(CLOSE);
		
		// Some curved lines and shapes in a different style.
		pushStyle();
		
		noFill();
		strokeWeight(2);
		stroke(255, 102, 0);
		curve(405, 26, 405, 26, 273, 24, 273, 61);
		curve(405, 26, 273, 24, 273, 61, 415, 65); 
		curve(273, 24, 273, 61, 415, 65, 415, 65);
				
		fill(255, 102, 0);
		beginShape();
		curveVertex(384,  191);
		curveVertex(384,  191);
		curveVertex(368,  119);
		curveVertex(321,  117);
		curveVertex(332, 200);
		curveVertex(332, 200);
		vertex(384, 191);		
		endShape(CLOSE);
		
		popStyle();
		
		
		//examples from http://processing.org/reference/beginShape_.html
		
		pushMatrix();
		translate(0,200);
		beginShape();
		vertex(30, 20);
		vertex(85, 20);
		vertex(85, 75);
		vertex(30, 75);
		endShape(CLOSE);

		translate(100,0);
		beginShape(POINTS);
		vertex(30, 20);
		vertex(85, 20);
		vertex(85, 75);
		vertex(30, 75);
		endShape();

		translate(100,0);
		beginShape(LINES);
		vertex(30, 20);
		vertex(85, 20);
		vertex(85, 75);
		vertex(30, 75);
		endShape();

		translate(100,0);
		noFill();
		beginShape();
		vertex(30, 20);
		vertex(85, 20);
		vertex(85, 75);
		vertex(30, 75);
		endShape();

		translate(-300,100);
		noFill();
		beginShape();
		vertex(30, 20);
		vertex(85, 20);
		vertex(85, 75);
		vertex(30, 75);
		endShape(CLOSE);

		translate(100,0);
		fill(200,100,100);
		beginShape(TRIANGLES);
		vertex(30, 75);
		vertex(40, 20);
		vertex(50, 75);
		vertex(60, 20);
		vertex(70, 75);
		vertex(80, 20);
		endShape();

		translate(100,0);
		beginShape(TRIANGLE_STRIP);
		vertex(30, 75);
		vertex(40, 20);
		vertex(50, 75);
		vertex(60, 20);
		vertex(70, 75);
		vertex(80, 20);
		vertex(90, 75);
		endShape();

		translate(100,0);
		beginShape(TRIANGLE_FAN);
		vertex(57.5f, 50);
		vertex(57.5f, 15); 
		vertex(92, 50); 
		vertex(57.5f, 85); 
		vertex(22, 50); 
		vertex(57.5f, 15); 
		endShape();

		translate(-300,100);
		beginShape(QUADS);
		vertex(30, 20);
		vertex(30, 75);
		vertex(50, 75);
		vertex(50, 20);
		vertex(65, 20);
		vertex(65, 75);
		vertex(85, 75);
		vertex(85, 20);
		endShape();

		translate(100,0);
		beginShape(QUAD_STRIP); 
		vertex(30, 20); 
		vertex(30, 75); 
		vertex(50, 20);
		vertex(50, 75);
		vertex(65, 20); 
		vertex(65, 75); 
		vertex(85, 20);
		vertex(85, 75); 
		endShape();

		translate(100,0);
		beginShape();
		vertex(20, 20);
		vertex(40, 20);
		vertex(40, 40);
		vertex(60, 40);
		vertex(60, 60);
		vertex(20, 60);
		endShape(CLOSE);

		popMatrix();		// End of scaling and translation.
		
		popMatrix();		// End of rotation
		
		// Small sketchy ellipse after transformations follows mouse position.
		stroke(0);
		fill(200,50,50,100);
		ellipse(mouseX,mouseY,90,40);
		
		// Turn off handy rendering at the end of each draw cycle.
		if (isHandy)
		{
			endRecord();
		}
		
		// Any code following the turning off of the handy recorder should be unsketchy.
		fill(50,50,200,100);
		noStroke();
		ellipse(mouseX,mouseY,200,120);
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
		}
		
		if (key == PConstants.CODED)
		{
			if (keyCode == PConstants.UP)
			{
				roughness *= 1.1;
			}
			else if (keyCode == PConstants.DOWN)
			{
				roughness *= 0.9;
			}
		}
	}
}
