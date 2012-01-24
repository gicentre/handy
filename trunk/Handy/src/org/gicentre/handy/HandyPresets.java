package org.gicentre.handy;

import processing.core.PApplet;

// *****************************************************************************************
/** Set of static classes for creating preset handy styles, such as pencil sketch, ink and
 *  watercolour, 'Sharpie' style etc.
 *  @author Jo Wood, giCentre, City University London.
 *  @version 1.0, 23rd January, 2012.
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

public class HandyPresets
{
	/** Prevents this class from being instantiated.
	 */
	private HandyPresets()
	{
		// Do not allow instantiation of this class since it contains only static methods.
	}
		
	/** Creates a renderer that draws in a pencil sketch style.
	 *  @param parent PArent sketch that will do the drawing.
	 *  @return Renderer that draws in a pencil sketch style.
	 */
	public static HandyRenderer createPencil(PApplet parent)
	{
		HandyRenderer handy = new HandyRenderer(parent);
		
		handy.setOverrideStrokeColour(true);
		handy.setStrokeColour(parent.color(120,180));
		handy.setOverrideFillColour(true);
		handy.setFillColour(parent.color(128,220));
		handy.setFillWeight(0.3f);
		handy.setFillGap(0.8f);
		handy.setUseSecondaryColour(true);
		handy.setBackgroundColour(parent.color(255,50));
		handy.setSecondaryColour(parent.color(255,100));		
		handy.setHachurePerturbationAngle(5);
		return handy;
	}
	
	/** Creates a renderer that draws in a coloured pencil sketch style.
	 *  @param parent PArent sketch that will do the drawing.
	 *  @return Renderer that draws in a coloured pencil sketch style.
	 */
	public static HandyRenderer createColouredPencil(PApplet parent)
	{
		HandyRenderer handy = new HandyRenderer(parent);
		handy.setFillWeight(1.5f);
		handy.setFillGap(1f);
		handy.setStrokeColour(parent.color(255,0));
		handy.setOverrideStrokeColour(true);
		//handy.setBackgroundColour(parent.color(25,50));
		handy.setHachurePerturbationAngle(5);
		return handy;
	}
	
	/** Creates a renderer that draws in a watercolour and ink style.
	 *  @param parent PArent sketch that will do the drawing.
	 *  @return Renderer that draws in a pencil sketch style.
	 */
	public static HandyRenderer createWaterAndInk(PApplet parent)
	{
		HandyRenderer handy = new HandyRenderer(parent);
		handy.setOverrideStrokeColour(true);
		handy.setStrokeColour(parent.color(0));
		handy.setOverrideFillColour(false);
		handy.setFillGap(0);
		handy.setRoughness(3);
		return handy;
	}
	
	/** Creates a renderer that draws in a felt-tip marker ('Sharpie') style.
	 *  @param parent PArent sketch that will do the drawing.
	 *  @return Renderer that draws in a marker style.
	 */
	public static HandyRenderer createMarker(PApplet parent)
	{
		HandyRenderer handy = new HandyRenderer(parent);
		handy.setOverrideStrokeColour(true);
		handy.setStrokeColour(parent.color(0,160));		
		handy.setFillWeight(5);
		handy.setStrokeWeight(3);
		handy.setFillGap(7);
		handy.setHachurePerturbationAngle(5);
		handy.setRoughness(1.5f);
		return handy;
	}
}