package org.gicentre.tests;

import org.gicentre.handy.HandyRenderer;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;


//*****************************************************************************************
/** Simple sketch to show a hand-drawn bar graph.
 *  @author Jo Wood, giCentre, City University London.
 *  @version 1.0, 26th November, 2011.
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
public class GraphExample extends PApplet 
{
	// ------------------------------ Starter method ------------------------------- 

	/** Creates a simple application to test handy line drawing.
	 *  @param args Command line arguments (ignored). 
	 */
	public static void main(String[] args)
	{   
		PApplet.main(new String[] {"org.gicentre.tests.GraphExample"});
	}

	// ----------------------------- Object variables ------------------------------

	private HandyRenderer h;
	private PFont titleFont, bodyFont;
	private float sWeight;
	
	private PFont hLargeFont, hMediumFont, nLargeFont, nMediumFont;
	private boolean isHandy, useSecondary;
	
	private float[] data = new float[] {57,75,60,49,18,36,34,14,-40,17,-26,3,-15,-30,-50,-31,-86,-42,-64,-70,-67,-126,-66,0,-94,-221};
		
	// ---------------------------- Processing methods -----------------------------
	

	/** Sets up the sketch.
	 */
	public void setup()
	{   
		size(440,800);
		
		//size(1000,400);
		
		smooth();
				
		hLargeFont  = loadFont("AmarelinhaBold-36.vlw");
		hMediumFont = loadFont("Amarelinha-32.vlw");
		//nLargeFont  = createFont("SansSerif",28);
		//nMediumFont = createFont("SansSerif",22);
		nLargeFont = hLargeFont;
		nMediumFont = hMediumFont;
		
		titleFont = nLargeFont;
		bodyFont = nMediumFont;
		isHandy = true;
		sWeight = 1;
		useSecondary = false;
		
		h = new HandyRenderer(this);
		h.setIsHandy(isHandy);
		h.setSecondaryColour(color(0,10));
		h.setUseSecondaryColour(useSecondary);	
		h.setHachureAngle(-37);
		h.setHachurePerturbationAngle(7);
		h.setFillGap(2);
	}
	
	
	/** Draws some sketchy lines.
	 */
	public void draw()
	{
		background(255);
		stroke(80);
		strokeWeight(sWeight);
		textAlign(PConstants.LEFT, PConstants.TOP);
		
		//h.rect(10, 10, 240, 55);
		//h.rect(10, 10, 240, 55);
		fill(80);
		textFont(titleFont);
		textSize(isHandy?48:28);
		text("What's in a name?",20,10);
		
		
		//textAlign(PConstants.CENTER, PConstants.TOP);
		//text("What's in a name?",width/2,10);
		
		
		noFill();
		//h.rect(10, 70, 220, 180);
		textFont(bodyFont);
		textSize(isHandy?32:22);
		fill(40);
		textLeading(isHandy?32:26);
		text("Numbers of extra votes received as a bonus or deprived from a candidate depending on the first letter of their surname.",20,80,200,380);
		
		//textAlign(PConstants.LEFT, PConstants.TOP);
		//text("Numbers of extra votes received as a bonus or deprived from a candidate depending on the first letter of their surname.",60,210,450,180);

			
		textAlign(PConstants.CENTER,PConstants.CENTER);
		
		
	
		// Draw bars
		
		float barWidth = (height-50)/26f;
		float cx = width*.66f;
		for (int i=0; i<data.length; i++)
		{
			float barLength = data[i];
			fill(162,187,243);
			h.rect(cx,10+i*barWidth,barLength,barWidth-4);
			
			fill(100);
			if (barLength>0)
			{
				text((char)('A'+i),cx-10,10+(i+0.4f)*barWidth);
			}
			else
			{
				text((char)('A'+i),cx+10,10+(i+0.4f)*barWidth);
			}
		}
		
		
		/* Draw bars
		float cy = height*.31f;
		float barWidth = (width-70)/26f;
		textFont(titleFont);
		fill(40);
		for (int i=0; i<data.length; i++)
		{
			float barLength = data[i];
			fill(162,187,243);
			h.setHachureAngle(-37+random(-7,7));
			h.rect(50+i*barWidth,cy,barWidth-4,-barLength);
			
			fill(100);
			if (barLength>0)
			{
				text((char)('A'+i),50+(i+0.4f)*barWidth,cy+15);
			}
			else
			{
				text((char)('A'+i),50+(i+0.4f)*barWidth,cy-20);
			}
		}
		*/
		
		
		// Draw scale
		stroke(180);
		fill(80);
		h.line(cx-250, height-10, cx+100, height-10);
		
		textAlign(CENTER,BOTTOM);
		textSize(isHandy?20:14);
		for (int x=-250; x<=100; x+=50)
		{
			text(x,cx+x,height-15);
			h.line(cx+x, height-10, cx+x, height-15);
		}
		
		
		/* Draw scale
		stroke(180);
		fill(80);
		h.line(40,cy+250, 40,cy-100);
		
		textAlign(RIGHT,CENTER);
		textSize(isHandy?20:14);
		for (int y=-100; y<=250; y+=50)
		{
			text(-y,30,cy+y);
			h.line(35,cy+y,40, cy+y);
		}
		*/
		
		noLoop();
	}
	
	@Override
	public void keyPressed()
	{
		if (key =='h')
		{
			isHandy = !isHandy;
			h.setIsHandy(isHandy);
			if (isHandy)
			{
				titleFont = hLargeFont;
				bodyFont = hMediumFont;
			}
			else
			{
				titleFont = nLargeFont;
				bodyFont = nMediumFont;
			}
			loop();
		}
		else if (key == '1')
		{
			sWeight = 0.3f;
			loop();
		}
		else if (key == '2')
		{
			sWeight = 1;
			loop();
		}
		else if (key == '3')
		{
			sWeight = 3;
			loop();
		}
		else if (key == 's')
		{
			useSecondary = !useSecondary;
			h.setUseSecondaryColour(useSecondary);
			loop();
		}
	}
}
