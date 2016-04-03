package org.gicentre.tests;

import org.gicentre.handy.HandyRenderer;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

//*****************************************************************************************
/** Simple sketch to show a hand-drawn bar graph. 'H' to toggle sketchy rendering. Keys 1-3
 *  to change line thickness. 'S' to toggle secondary shading.
 *  @author Jo Wood, giCentre, City University London.
 *  @version 2.0, 31st March, 2016.
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
	private PFont sketchyTitleFont, sketchyBodyFont, normalTitleFont, normalBodyFont;
	private float sWeight;
	
	private boolean isHandy, useSecondary;
	
	private float[] data = new float[] {57,75,60,49,18,36,34,14,-40,17,-26,3,-15,-30,-50,-31,-86,-42,-64,-70,-67,-126,-66,0,-94,-221};
		
	// ---------------------------- Processing methods -----------------------------
	
	/** Initial window settings prior to setup().
	 */
	public void settings()
	{   
		size(440,800);
		
		// Should work with all Processing 3 renderers.
		// size(440, P2D);
		// size(440,800, P3D);
		// size(440,800, FX2D);
		
		pixelDensity(displayDensity());		// Use platform's maximum display density.
	}

	/** Sets up the sketch.
	 */
	@Override
	public void setup()
	{   
		sketchyTitleFont = loadFont("HumorSans-32.vlw");
		sketchyBodyFont  = loadFont("HumorSans-18.vlw");
		normalTitleFont  = createFont("sans-serif",32);
		normalBodyFont   = createFont("sans-serif",18);

		isHandy = true;
		sWeight = 1;
		useSecondary = false;
		
		h = new HandyRenderer(this);
		h.setIsHandy(isHandy);
		h.setSecondaryColour(color(0,30));
		h.setUseSecondaryColour(useSecondary);	
		h.setHachureAngle(-37);
		h.setHachurePerturbationAngle(7);
		h.setFillGap(2);
	}
	
	
	/** Draws the sketchy bars.
	 */
	@Override
	public void draw()
	{
		background(255);
		stroke(20);
		strokeWeight(sWeight);
		textAlign(PConstants.LEFT, PConstants.TOP);
		
		fill(80);
		textFont(isHandy? sketchyTitleFont: normalTitleFont);
		textSize(isHandy?32:28);
		text("What's in a name?",10,15);
						
		noFill();
		textFont(isHandy? sketchyBodyFont: normalBodyFont);
		textSize(isHandy?18:16);
		fill(40);
		textLeading(isHandy?18:16);
		text("Numbers of extra votes received as a bonus or deprived from a candidate depending on the first letter of their surname.",10,50,200,380);
					
		
		// Draw bars
		stroke(80);
		float barWidth = (height-50)/26f;
		float cx = width*.7f;
		textAlign(PConstants.CENTER,PConstants.CENTER);
		
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
				
		noLoop();
	}
	
	/** Responds to key presses to alter appearance of sketch shapes.
	 */
	@Override
	public void keyPressed()
	{
		if ((key =='h') || (key == 'H'))
		{
			isHandy = !isHandy;
			h.setIsHandy(isHandy);
			loop();
		}
		else if (key == '1')
		{
			sWeight = 1f;
			loop();
		}
		else if (key == '2')
		{
			sWeight = 1.5f;
			loop();
		}
		else if (key == '3')
		{
			sWeight = 2.5f;
			loop();
		}
		else if ((key == 's') || (key == 'S'))
		{
			useSecondary = !useSecondary;
			h.setUseSecondaryColour(useSecondary);
			loop();
		}
	}
}
