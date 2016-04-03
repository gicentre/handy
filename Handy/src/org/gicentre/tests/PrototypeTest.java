package org.gicentre.tests;

import org.gicentre.handy.HandyRenderer;
import org.gicentre.utils.colour.ColourTable;

import processing.core.PApplet;
import processing.core.PFont;


//*****************************************************************************************
/** Sketch to illustrate the use of the HandyRenderer to build a digital prototype.
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

public class PrototypeTest extends PApplet 
{
	// ------------------------------ Starter method ------------------------------- 

	/** Creates a simple application to test handy line drawing.
	 *  @param args Command line arguments (ignored). 
	 */
	public static void main(String[] args)
	{   
		PApplet.main(new String[] {"org.gicentre.tests.PrototypeTest"});
	}

	// ----------------------------- Object variables ------------------------------

	private HandyRenderer h;
	private PFont largeFont, mediumFont, smallFont;
	
	private ColourTable mapColours;
		
	// ---------------------------- Processing methods -----------------------------
	
	/** Initial window settings prior to setup().
	 */
	@Override
	public void settings()
	{   
		size(930,630);
		
		// Should work with all Processing 3 renderers.
		// size(930,630, P2D);
		// size(930,630, P3D);
		// size(930,630, FX2D);
		
		pixelDensity(displayDensity());		// Use platform's maximum display density.
	}
	
	/** Sets up the sketch.
	 */
	@Override
	public void setup()
	{   		
		largeFont  = loadFont("HumorSans-32.vlw");
		mediumFont = loadFont("HumorSans-18.vlw");
		smallFont  = mediumFont;
		mapColours = ColourTable.getPresetColourTable(ColourTable.PU_OR,0,1);
		
		h = new HandyRenderer(this);
	}
	
	/** Draws the prototypes.
	 */
	@Override
	public void draw()
	{
		background(245,235,220);
		
		stroke(0,40);
		fill(255);
		h.setFillGap(0);
		h.setRoughness(0.5f);
		h.rect(10,10,width-20,height-20);
		
		// Left hand Likert area
		h.rect(20,20, 200, height-40);
		
		// Middle map  area
		h.rect(240, 20, 480, height-40);
				
		// Right hand demographics area
		h.rect(740,20,170,height-40);
		
		// Likert graphs
		drawLikert("Local area",        30,  30, 180, 105);
		drawLikert("Public services",   30, 145, 180, 105);
		drawLikert("Public involvment", 30, 260, 180, 105);
		drawLikert("The community",     30, 375, 180, 105);
		drawLikert("Access to services",30, 490, 180, 105);
		
		// Treemap / map area
		h.setRoughness(1);
		textFont(mediumFont);
		h.setFillGap(6);
		stroke(0,30);
		textAlign(CENTER,CENTER);
		fill(mapColours.findColour(0.8f));
		h.rect(250, 30, 140,160);
		fill(0,140);
		text("North West\nLeicestershire",320,95);
		
		fill(mapColours.findColour(0.6f));
		h.rect(400, 30, 210,160);
		fill(0,140);
		text("Charnwood",505,95);
		
		fill(mapColours.findColour(0.7f));
		h.rect(620, 30, 90,160);
		fill(0,140);
		text("Melton",665,95);
		
		fill(mapColours.findColour(0.3f));
		h.rect(250, 200, 120,160);
		fill(0,140);
		text("Hinckley &\nBosworth",310,280);
		
		fill(mapColours.findColour(0.6f));
		h.rect(380, 200, 120,160);
		fill(0,140);
		text("Blaby",440,280);
		
		fill(mapColours.findColour(0.4f));
		h.rect(510, 200, 70,160);
		fill(0,140);
		text("Oadby &\nWigston",545,280);
		
		fill(mapColours.findColour(0.8f));
		h.rect(590, 200, 120,160);
		fill(0,140);
		text("Harborough",650,280);
		
		
		// Map legend and titles
		h.setRoughness(0.5f);
		h.setFillGap(2);
		stroke(0,40);
		for (int i=0; i<20; i++)
		{
			float x = map(i,0,20, 250, 710);
			float y = 400;
			float wdth = 460/20;
			float hght = 20;
			fill(mapColours.findColour(i/20f));
			h.rect(x,y,wdth,hght);
		}
		fill(0,200);
		textSize(16);
		textFont(mediumFont);
		textAlign(LEFT);
		text("Negative response",250,440);
		textAlign(RIGHT);
		text("Positive response",710,440);
		
		textSize(32);
		textFont(largeFont);
		textSize(32);
		textAlign(CENTER);
		fill(0,150);
		text("Question title here",(710+250)/2,530);
		
		textSize(16);
		textFont(mediumFont);
		fill(0,200);
		text("Advanced menu options and status displayed here",(710+250)/2,605);
		
		
		// Demographics bars
		float yPos = drawDemographic("Gender", 3, 745, 30, 160);
		yPos = drawDemographic("Age", 9, 745, yPos+10, 160);
		yPos = drawDemographic("Health", 5, 745, yPos+10, 160);
		yPos = drawDemographic("Occupancy", 5, 745, yPos+10, 160);
		yPos = drawDemographic("Disability", 3, 745, yPos+10, 160);
		yPos = drawDemographic("Ethnicity", 3, 745, yPos+10, 160);
		yPos = drawDemographic("Years resident", 5, 745, yPos+10, 160);
		
		h.setFillGap(1);
		fill(0,140);
		textAlign(RIGHT,CENTER);
		text("Everyone",745+34,height-38);
		
		fill(172,117,116);
		float barLength = (160-40);
		h.rect(745+40,height-40,barLength,9);
		
		noLoop();
	}
		
	/** Redraw on any key press.
	 */
	@Override
	public void keyPressed()
	{
		loop();
	}
	
	/** Draws one of the horizontal bar charts representing a demographic variable.
	 *  @param title Title of chart
	 *  @param numBars Number of bars to draw.
	 *  @param x Left hand edge of bar area
	 *  @param y Top edge of bar area.
	 *  @param wdth Maximum width of bar area.
	 *  @return The y position of the newly created bar area.
	 */
	private float drawDemographic(String title, int numBars, float x, float y, float wdth)
	{
		float barWidth = 9;
		float yPos = y;
		
		fill(0,140);
		textSize(18);
		textFont(mediumFont);
		stroke(0,50);
		textAlign(LEFT,CENTER);
		text(title,x+40,yPos);
		yPos += 12;
		
	
		textFont(smallFont);
		textSize(12);
		for (int i=0; i<numBars; i++)
		{
			fill(0,140);
			textAlign(RIGHT,CENTER);
			text("Group",x+34,yPos+2);
			
			fill(182,147,146);
			float barLength = random((wdth-40)*0.1f,(wdth-40));
			h.rect(x+40,yPos,barLength,barWidth);
			yPos += (barWidth+3);
		}
		return yPos;
	}
	
	/** Draws a Likert response graph.
	 * @param title Title of graph
	 * @param x Left hand edge of graph.
	 * @param y Top edge of graph.
	 * @param wdth Width of graph area.
	 * @param hght Height of graph area.
	 */
	private void drawLikert(String title, float x, float y, float wdth, float hght)
	{
		fill(0,140);
		textFont(mediumFont);
		stroke(0,50);
		textAlign(LEFT,TOP);
		text(title,x,y);
		h.setFillGap(3);
		
		for (int i=0; i<5; i++)
		{
			float fall= 1.2f - 0.3f*abs(i-2);
			float xPos = map(i,0,5,x,x+wdth);
			float barHeight = random(5,(hght-40)*fall);
			fill(mapColours.findColour(map(i,0,4,0.1f,0.9f)));
			h.rect(xPos,y+hght-10,25,-barHeight);
		}
		
		float cx = x + wdth*random(0.4f,0.6f);
		float len = random(wdth*0.12f,wdth*.25f);
		stroke(80,0,0,100);
		strokeWeight(2);
		h.line(cx-len, y+hght-5, cx+len, y+hght-5);
		noFill();
		h.ellipse(cx, y+hght-5, 12, 12);
		
		strokeWeight(1);
	}
	
}