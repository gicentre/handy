package org.gicentre.tests;

import org.gicentre.handy.HandyPresets;
import org.gicentre.handy.HandyRenderer;
import org.gicentre.utils.move.ZoomPan;

import processing.core.PApplet;
import processing.core.PConstants;

//*****************************************************************************************
/** Simple sketch to show handy shape drawing in a pencil sketch style.
 *  @author Jo Wood, giCentre, City University London.
 *  @version 1.0, 2nd December, 2011.
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
public class RandomShapeExample extends PApplet 
{
	// ------------------------------ Starter method ------------------------------- 

	/** Creates a simple application to test handy line drawing.
	 *  @param args Command line arguments (ignored). 
	 */
	public static void main(String[] args)
	{   
		PApplet.main(new String[] {"org.gicentre.tests.RandomShapeExample"});
	}

	// ----------------------------- Object variables ------------------------------

	private HandyRenderer pencil,marker,water,cPencil, border;	
	private ZoomPan zoomer;
	private float angle;
	private boolean isHandy;
		
	// ---------------------------- Processing methods -----------------------------

	/** Sets up the sketch.
	 */
	public void setup()
	{   
		size(1200,800);
		smooth();
		zoomer = new ZoomPan(this);
		angle = -42;
		isHandy = true;	
		
		pencil  = HandyPresets.createPencil(this);		
		water   = HandyPresets.createWaterAndInk(this);
		marker  = HandyPresets.createMarker(this);
		cPencil = HandyPresets.createColouredPencil(this);
		border = new HandyRenderer(this);
		
		pencil.setHachureAngle(angle);
		water.setHachureAngle(angle);
		marker.setHachureAngle(angle);
		cPencil.setHachureAngle(angle);
		
		textFont(createFont("YWFTHLLVTKA-Normal",36));
	}
	
	
	/** Draws the same shapes in four different sketchy styles.
	 */
	public void draw()
	{
		background(255);
		zoomer.transform();
		pencil.setSeed(1234);
		water.setSeed(1234);
		marker.setSeed(1234);
		cPencil.setSeed(1234);
		
		stroke(0);
		strokeWeight(1);
		textAlign(RIGHT,BOTTOM);
		
				
		randomSeed(10);
		noFill();
		border.rect(10,10,width/2-20,height/2-20);
		drawShapes(pencil,0,0,width/2,height/2);
		fill(60);
		text("Pencil",width/2-20,height/2-15);
		
		randomSeed(10);
		noFill();
		border.rect(width/2+10,10,width/2-20,height/2-20);
		drawShapes(water,width/2,0,width/2,height/2);
		fill(60);
		text("Ink and watercolour",width-20,height/2-15);
		
		randomSeed(10);
		noFill();
		border.rect(10,height/2+10,width/2-20,height/2-20);
		drawShapes(marker,0,height/2,width/2,height/2);
		fill(60);
		text("Marker pen",width/2-20,height-15);
		
		randomSeed(10);
		noFill();
		border.rect(width/2+10,height/2+10,width/2-20,height/2-20);
		drawShapes(cPencil,width/2,height/2,width/2,height/2);
		fill(60);
		text("Coloured pencil",width-20,height-15);
		
		noLoop();
	}
		
	/** Allow handy rendering to be toggled on or off and hachure angle to be changed with key presses.
	 */
	@Override
	public void keyPressed()
	{
		if (key =='h')
		{
			isHandy = !isHandy;
			pencil.setIsHandy(isHandy);
			water.setIsHandy(isHandy);
			marker.setIsHandy(isHandy);
			cPencil.setIsHandy(isHandy);
			loop();
		}
		else if (key == 'r')
		{
			zoomer.reset();
			loop();
		}
		
		if (key == PConstants.CODED)
		{
			if (keyCode == PConstants.LEFT)
			{
				angle--;
				pencil.setHachureAngle(angle);
				water.setHachureAngle(angle);
				marker.setHachureAngle(angle);
				cPencil.setHachureAngle(angle);
				
				loop();
			}
			else if (keyCode == PConstants.RIGHT)
			{
				angle++;
				pencil.setHachureAngle(angle);
				water.setHachureAngle(angle);
				marker.setHachureAngle(angle);
				cPencil.setHachureAngle(angle);
				loop();
			}
		}
	}
	
	/** Ensures screen is updated whenever a zooming/panning mouse is dragged.
	 */
	@Override
	public void mouseDragged()
	{
		loop();
	}
	
	/** Draws a range of shapes at random positions on the screen.
	 *  @param handy Renderer to do the drawing.
	 *  @param x Left hand edge of drawing area
	 *  @param y Top of drawing area
	 *  @param w Width of drawing area.
	 *  @param h Height of drawing area.
	 */
	private void drawShapes(HandyRenderer handy, float x, float y, float w, float h)
	{
		float minSize = w/10;
		float maxSize = Math.min(w,h)/4;
			
		for (int i=0; i<30; i++)
		{
			int colour = color(random(100,200),random(60,200), random(100,200),120);
			fill(colour);
			float shapeChoice = random(0,1);
			if (shapeChoice < 0.33)
			{
				handy.rect(x+random(minSize,w-maxSize),y+random(minSize,h-maxSize),random(minSize,maxSize), random(minSize,maxSize));
			}
			else if (shapeChoice <0.66)
			{
				float x1 = x+random(minSize,w-maxSize);
				float y1 = y+random(minSize,h-maxSize);
				float x2 = x1+random(50,maxSize);
				float y2 = y1+random(-10,10);
				float x3 = (x1+x2)/2;
				float y3 = y1+random(-minSize,-maxSize);
				handy.triangle(x1,y1,x2,y2,x3,y3);	
			}
			else
			{
				handy.ellipse(x+random(minSize,w-maxSize), y+random(minSize,h-maxSize),random(minSize,maxSize), random(minSize,maxSize));	
			}
			
		}
		

	}
}
