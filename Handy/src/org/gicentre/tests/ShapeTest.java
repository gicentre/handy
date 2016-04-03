package org.gicentre.tests;

import org.gicentre.handy.HandyRenderer;
import org.gicentre.utils.move.ZoomPan;

import processing.core.PApplet;
import processing.core.PConstants;

//*****************************************************************************************
/** Simple sketch to test positioning and styling of handy shapes. The 'A' key toggles 
 *  alternating hachures. The arrow keys control hachure angle. Zooming and panning with 
 *  mouse dragging and 'R' to reset zoom.
 *  @author Jo Wood, giCentre, City University London.
 *  @version 2.0, 3rd April 2016.
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

public class ShapeTest extends PApplet 
{
	// ------------------------------ Starter method ------------------------------- 

	/** Creates a simple application to test handy line drawing.
	 *  @param args Command line arguments (ignored). 
	 */
	public static void main(String[] args)
	{   
		PApplet.main(new String[] {"org.gicentre.tests.ShapeTest"});
	}

	// ----------------------------- Object variables ------------------------------

	private HandyRenderer h;
	private ZoomPan zoomer;
	private boolean isAlternating;
	
	private float angle;
	private float BORDER = 20;		// Border between adjacent shapes
	
		
	// ---------------------------- Processing methods -----------------------------

	/** Initial window settings prior to setup().
	 */
	@Override
	public void settings()
	{   
		size(1400,600);
		
		// Should work with all Processing 3 renderers.
		// size(1400,600, P2D);
		// size(1400,600, P3D);
		// size(1400,600, FX2D);
		
		pixelDensity(displayDensity());		// Use platform's maximum display density.
	}
	
	/** Sets up the sketch.
	 */
	@Override
	public void setup()
	{   
		angle = -37;
		isAlternating = false;
		zoomer = new ZoomPan(this);
		h = new HandyRenderer(this);
		h.setHachureAngle(angle);
	}
	
	
	/** Draws a sequence of objects with different sketchy parameters
	 */
	@Override
	public void draw()
	{
		background(227,215,197);
		
		zoomer.transform();
		
		int numShapes = 14;
		int numTypes = 6;
		float maxWidth  = (width-(numShapes+1)*BORDER)/numShapes;
		float maxHeight = (height-(numTypes+1)*BORDER)/numTypes;
		float y = BORDER;
		
		// Rectangles
		for (int i=0; i<numShapes; i++)
		{
			setStyle(i);
			float x=BORDER + i*(maxWidth+BORDER);
			if (i==0)
			{
				rect(x,y,maxWidth,maxHeight);
			}
			else
			{
				h.rect(x, y, maxWidth, maxHeight);
			}
		}
		
		y+= (maxHeight+BORDER);
		
		// Ellipses
		for (int i=0; i<numShapes; i++)
		{
			setStyle(i);
			float x=BORDER + i*(maxWidth+BORDER);
			if (i==0)
			{
				ellipse(x+maxWidth/2,y+maxHeight/2,maxWidth,maxHeight);
			}
			else
			{
				h.ellipse(x+maxWidth/2,y+maxHeight/2, maxWidth, maxHeight);
			}
		}
		
		y+= (maxHeight+BORDER);
		
		// Triangles
		for (int i=0; i<numShapes; i++)
		{
			setStyle(i);
			float x=BORDER + i*(maxWidth+BORDER);
			if (i==0)
			{
				triangle(x+maxWidth*0.765f,y,x+maxWidth,y+maxHeight*.876f,x,y+maxHeight);
			}
			else
			{
				h.triangle(x+maxWidth*0.765f,y,x+maxWidth,y+maxHeight*.876f,x,y+maxHeight);
			}
		}
		
		y+= (maxHeight+BORDER);
		
		// Cross shapes
		for (int i=0; i<numShapes; i++)
		{
			setStyle(i);
			float x=BORDER + i*(maxWidth+BORDER);
			float cx = x+maxWidth/2;
			float cy = y+maxHeight/2;
			float armLength = min(maxWidth,maxHeight)/2;
			float halfWidth = armLength/3;
			if (i==0)
			{
				beginShape();
				   vertex(cx-halfWidth, cy-halfWidth);
				   vertex(cx-halfWidth, cy-armLength);
				   vertex(cx+halfWidth, cy-armLength);
				   
				   vertex(cx+halfWidth, cy-halfWidth);
				   vertex(cx+armLength, cy-halfWidth);
				   vertex(cx+armLength, cy+halfWidth);
				   
				   vertex(cx+halfWidth, cy+halfWidth);
				   vertex(cx+halfWidth, cy+armLength);
				   vertex(cx-halfWidth, cy+armLength);
				   
				   vertex(cx-halfWidth, cy+halfWidth);
				   vertex(cx-armLength, cy+halfWidth);
				   vertex(cx-armLength, cy-halfWidth);
				 endShape(PConstants.CLOSE);
			}
			else
			{
				float[] xCoords = new float[] {cx-halfWidth, cx-halfWidth, cx+halfWidth, cx+halfWidth, cx+armLength, cx+armLength,
											   cx+halfWidth, cx+halfWidth, cx-halfWidth, cx-halfWidth, cx-armLength, cx-armLength};
				float[] yCoords = new float[] {cy-halfWidth, cy-armLength, cy-armLength, cy-halfWidth, cy-halfWidth, cy+halfWidth,
											   cy+halfWidth, cy+armLength, cy+armLength, cy+halfWidth, cy+halfWidth, cy-halfWidth};
				h.shape(xCoords,yCoords);
			}
		}
		
		y+= (maxHeight+BORDER);
		
		// U shapes
		for (int i=0; i<numShapes; i++)
		{
			setStyle(i);
			float x=BORDER + i*(maxWidth+BORDER);
			float armWidth = maxWidth/4;
			if (i==0)
			{
				beginShape();
				   vertex(x+maxWidth-armWidth,y);
				   vertex(x+maxWidth         ,y);
				   vertex(x+maxWidth         ,y+maxHeight);
				   vertex(x                  ,y+maxHeight);
				   vertex(x 				 ,y);
				   vertex(x+armWidth         ,y);
				   vertex(x+armWidth         ,y+maxHeight-armWidth);
				   vertex(x+maxWidth-armWidth,y+maxHeight-armWidth);
				 endShape(PConstants.CLOSE);
			}
			else
			{
				float[] xCoords = new float[] {x+maxWidth-armWidth,x+maxWidth,x+maxWidth,x,x,x+armWidth,x+armWidth,x+maxWidth-armWidth};
				float[] yCoords = new float[] {y,y,y+maxHeight,y+maxHeight,y,y,y+maxHeight-armWidth,y+maxHeight-armWidth};
				h.shape(xCoords,yCoords);
			}
		}
		
		y+= (maxHeight+BORDER);
		
		// Curved shapes
		for (int i=0; i<numShapes; i++)
		{
			setStyle(i);
			float x=BORDER + i*(maxWidth+BORDER);
			if (i==0)
			{				
				beginShape();
				curveVertex(x+maxWidth, y+ maxHeight*0.7f);
				curveVertex(x+maxWidth, y+ maxHeight*0.7f);
				curveVertex(x+maxWidth*0.9f, y+ maxHeight*0.2f);
				curveVertex(x+maxWidth*0.03f, y+ maxHeight*0.15f);
				curveVertex(x+maxWidth*.5f, y+maxHeight*0.8f);
				curveVertex(x+maxWidth*.5f, y+maxHeight*0.8f);
				vertex(x+maxWidth*0.7f, y+maxHeight);
				vertex(x+maxWidth, y+maxHeight*0.7f);
				endShape();
			}
			else
			{
				h.beginShape();
				h.curveVertex(x+maxWidth, y+ maxHeight*0.7f);
				h.curveVertex(x+maxWidth, y+ maxHeight*0.7f);
				h.curveVertex(x+maxWidth*0.9f, y+ maxHeight*0.2f);
				h.curveVertex(x+maxWidth*0.03f, y+ maxHeight*0.15f);
				h.curveVertex(x+maxWidth*.5f, y+maxHeight*0.8f);
				h.curveVertex(x+maxWidth*.5f, y+maxHeight*0.8f);
				h.vertex(x+maxWidth*0.7f, y+maxHeight);
				h.vertex(x+maxWidth, y+maxHeight*0.7f);
				h.endShape();
			}
		}		
		noLoop();
	}
		
	@Override
	public void keyPressed()
	{
		if ((key == 'a') || (key == 'A'))
		{
			isAlternating = !isAlternating;
			loop();
		}
		else if ((key == 'r') || (key == 'R'))
		{
			zoomer.reset();
			loop();
		}
		
		if (key == PConstants.CODED)
		{
			if (keyCode == PConstants.LEFT)
			{
				angle--;
				loop();
			}
			else if (keyCode == PConstants.RIGHT)
			{
				angle++;
				loop();
			}
		}
	}
	
	@Override
	public void mouseDragged()
	{
		loop();
	}
	
	/** Sets up a particular rendering style for testing.
	 *  @param i Style index.
	 */
	private void setStyle(int i)
	{
		h.resetStyles();
		h.setHachureAngle(angle);
		h.setIsAlternating(isAlternating);
		switch (i)
		{
			case 0:
			case 1:
				stroke(80);
				strokeWeight(1);
				fill(162,187,243,150);
				h.setIsHandy(false);
				break;
				
			case 2:
				stroke(0,0,120);
				strokeWeight(3);
				noFill();
				h.setIsHandy(false);
				break;
				
			case 3:
				stroke(0,0,120);
				strokeWeight(3);
				noFill();
				h.setIsHandy(true);
				break;
				
			case 4:
				stroke(0,0,120);
				strokeWeight(0.5f);
				fill(120,0,0);
				h.setIsHandy(true);
				h.setSecondaryColour(color(0,255,0));
				break;
				
			case 5:
				//stroke(0,0,120);
				noStroke();
				strokeWeight(0.5f);
				fill(120,0,0);
				h.setIsHandy(true);
				h.setUseSecondaryColour(true);
				h.setSecondaryColour(color(255));
				break;
				
			case 6:
				stroke(0,0,120);
				strokeWeight(0.5f);
				fill(120,0,0);
				h.setIsHandy(true);
				h.setUseSecondaryColour(true);
				h.setSecondaryColour(color(0,40));				
				break;
				
			case 7:
				stroke(0);
				strokeWeight(4);
				fill(162,187,243,150);
				h.setIsHandy(true);
				h.setUseSecondaryColour(true);
				h.setBackgroundColour(color(0,0));
				h.setSecondaryColour(color(120,140,180));
				break;
				
			case 8:
				stroke(0);
				strokeWeight(0.3f);
				fill(120,140,180);
				h.setIsHandy(true);
				h.setUseSecondaryColour(true);
				h.setSecondaryColour(color(255,100));
				break;
				
			case 9:
				stroke(0);
				strokeWeight(0.3f);
				fill(120,140,180);
				h.setIsHandy(true);
				h.setUseSecondaryColour(true);
				h.setBackgroundColour(color(255,10));
				h.setSecondaryColour(color(255,100));
				break;
				
			case 10:
				stroke(0);
				strokeWeight(4);
				fill(162,187,243,150);
				h.setIsHandy(true);
				h.setFillWeight(4);
				h.setUseSecondaryColour(true);
				h.setSecondaryColour(color(120,140,180));
				break;
				
			case 11:
				stroke(0);
				strokeWeight(4);
				fill(162,187,243,150);
				h.setIsHandy(true);
				h.setFillWeight(4);
				h.setFillGap(6);
				h.setUseSecondaryColour(true);
				h.setSecondaryColour(color(120,140,180));
				break;
				
			case 12:
				stroke(0);
				strokeWeight(4);
				fill(162,187,243,150);
				h.setIsHandy(true);
				h.setFillGap(0);
				break;
			
			case 13:
				stroke(0,120);
				strokeWeight(2f);
				fill(162,187,243,150);
				h.setIsHandy(true);
				h.setUseSecondaryColour(true);
				h.setSecondaryColour(color(120,140,190,50));
				h.setFillWeight(0.5f);
				h.setFillGap(0.5f);
				break;
				
			default:
				// Do nothing.
		}
	}
}