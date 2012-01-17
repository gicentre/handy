package org.gicentre.handy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

// *****************************************************************************************
/** The renderer that draws graphic primitives in a sketchy style. The style of sketchiness
 *  can be configured in this class. Based on an original idea by <a 
 *  href="http://www.local-guru.net/blog/2010/4/23/simulation-of-hand-drawn-lines-in-processing" 
 *  target="_blank">Nikolaus Gradwohl</a>
 *  @author Jo Wood, giCentre, City University London based on an idea by Nikolaus Gradwohl.
 *  @version 1.0, 17th January, 2012.
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

public class HandyRenderer implements Drawable
{
	// -------------------------------- Object Variables ---------------------------------  

	private PApplet parent;						// Parent sketch issuing rendering commands.
	private PGraphics graphics;					// Graphics context in which this class is to render.
	private Random rand;						// Random number generator for random but repeatable offsets.
	private float cosAngle,sinAngle,tanAngle;	// Lookups for quick calculations.

	// Configuration settings
	private boolean isHandy;					// Determines if normal or hand-drawn appearance is used.
	private int bgColour, secondaryColour;		// Background colour and secondary fill colour	
	private boolean useSecondary;				// Determines if secondary colour is to be used.
	private boolean isAlternating;				// Determines if hachuring alternates in direction in continuous stroke.
	private float hachureAngle;					// Angle of diagonal hachuring.
	private float anglePerturbation;			// Random perturbation in hachure angle per object drawn.
	private float fillWeight, fillGap;			// Hachure filling characteristics.

	private int numEllipseSteps;
	private float ellipseInc;//, xInc, xIncAnti, yInc, yIncAnti;	// Incremental steps along an ellipse.

	// ----------------------------------- Constructor -----------------------------------

	public HandyRenderer(PApplet parent)
	{
		this.parent = parent;
		this.graphics = parent.g;

		numEllipseSteps = 9;
		ellipseInc = PConstants.TWO_PI/numEllipseSteps;
		isAlternating = false;
		anglePerturbation = 0;
		
		// Set initial configuration options.
		rand = new Random(123456);
		setIsHandy(true);
		resetStyles();		
	}

	// ------------------------------------- Methods ------------------------------------- 

	/** Sets the the graphics context into which all output is directed. This method allows
	 *  output to be redirected to print output, offscreen buffers etc.
	 *  @param graphics New graphics context in which to render.
	 */
	public void setGraphics(PGraphics graphics)
	{
		this.graphics = graphics;
	}

	/** Sets the seed used for random offsets when drawing. This should be called if repeated calls
	 *  to the same sketchy drawing methods should result in exactly the same rendering. If not called
	 *  a vibrating appearance can be given to the rendering.
	 *  @param seed Random number seed. This can be any whole number. A given number give the same random 
	 *              variation in rendering appearance every time.
	 */
	public void setSeed(long seed)
	{
		rand.setSeed(seed);
	}

	// ----------------------------------- Configuration methods -----------------------------------

	/** Determines whether or not the renderer applies a hand-drawn sketchy appearance.
	 *  If false, normal Processing drawing is used; if true a sketchy appearance using 
	 *  the current configuration style settings is used.
	 *  @param isHandy Sketchy appearance used if true, or normal Processing appearance if false.
	 */
	public void setIsHandy(boolean isHandy)
	{
		this.isHandy = isHandy;
	}

	/** Sets the angle for shading hachures.
	 *  @param degrees Angle of hachures in degrees where 0 is vertical, 45 is NE-SW and 90 is horizontal.
	 */
	public void setHachureAngle(float degrees)
	{
		hachureAngle = PApplet.radians(degrees%180);
		cosAngle = (float)Math.cos(hachureAngle);
		sinAngle = (float)Math.sin(hachureAngle);
		tanAngle = (float)Math.tan(hachureAngle);
	}
	
	/** Sets the maximum random perturbation in hachure angle per object. This allows a hachure angle to
	 *  vary between different shapes, but maintain approximate parallel hachure angle within a shape.
	 *  @param degrees Maximum hachure perturbation angle.
	 */
	public void setHachurePerturbationAngle(float degrees)
	{
		this.anglePerturbation = degrees;
	}
	
	/** Sets the background colour for closed shapes. 
	 *  @param colour Background colour.
	 */
	public void setBackgroundColour(int colour)
	{
		this.bgColour = colour;
	}

	/** Determines whether or not a secondary colour is used for filling lines.
	 *  @param useSecondary If true a secondary colour is used.
	 */
	public void setUseSecondaryColour(boolean useSecondary)
	{
		this.useSecondary = useSecondary;
	}

	/** Sets the secondary colour for line filling. 
	 *  @param colour Colour to tint line filling.
	 */
	public void setSecondaryColour(int colour)
	{
		this.secondaryColour = colour;
	}
	
	/** Determines the thickness of fill lines. If zero or negative, the thickness is
	 *  proportional to the sketch's current strokeWeight.
	 *  @param weight Fill weight in pixel units. If zero or negative, fill weight is based on strokeWeight setting. 
	 */
	public void setFillWeight(float weight)
	{
		this.fillWeight = weight;
	}
	
	/** Determines the gap between fill lines. If zero, standard solid fill is used. If negative,
	 *  the gap is proportional to the sketch's current strokeWeight.
	 *  @param gap Gap between fill lines in pixel units. If zero, solid fill used; if negative, gap based on strokeWeight setting.
	 */
	public void setFillGap(float gap)
	{
		this.fillGap = gap;
	}
	
	/** Determines whether or not an alternating fill stroke is used to shade shapes. If true, shading appears
	 *  as one long zig-zag stroke rather than many approximately parallel lines.
	 *  @param alternate Zig-zag filling used if true, parallel lines if not.
	 */
	public void setIsAlternating(boolean alternate)
	{
		this.isAlternating = alternate;
	}
	
	/** Resets the sketchy styles to default values.
	 */
	public void resetStyles()
	{
		setBackgroundColour(parent.color(255));
		setSecondaryColour(parent.color(255));
		setUseSecondaryColour(false);
		setFillWeight(-1);
		setFillGap(-1);
		setHachureAngle(-41);
		setHachurePerturbationAngle(0);
	}

	// -------------------------------------- Drawing methods --------------------------------------

	/** Draws point at the given location. Currently this draws the point in the same style as the
	 *  default Processing renderer.
	 *  @param x x coordinate of the point.
	 *  @param y y coordinate of the point.
	 */
	public void point(float x, float y)
	{
		graphics.point(x, y);
	}
	
	/** Draws an ellipse using the given location and dimensions. By default the x,y coordinates
	 *  will be centre of the ellipse, but the meanings of these parameters can be changed with
	 *  Processing's ellipseMode() command.
	 *  @param x x coordinate of the ellipse's position
	 *  @param y y coordinate of the ellipse's position.
	 *  @param w Width of the ellipse (but see modifications possible with ellipseMode())
	 *  @param h Height of the ellipse (but see modifications possible with ellipseMode())
	 */
	public void ellipse(float x, float y, float w, float h)
	{
		if (isHandy == false)
		{
			graphics.ellipse(x,y,w,h);
			return;
		}

		// Default is to use 'CENTER' mode for defining ellipse
		float cx = x;
		float cy = y;
		float rx = Math.abs(w/2);
		float ry = Math.abs(h/2);

		// Adjust bounds for other ellipse modes.
		if (graphics.ellipseMode == PConstants.CORNER)
		{
			float left   = Math.min(x,x+w);
			float top    = Math.min(y,y+h);
			float right  = Math.max(x,x+w);
			float bottom = Math.max(y,y+h);
			rx = (right-left)/2;
			ry = (bottom-top)/2;
			cx = left + rx;
			cy = top  + ry;

		}
		if (graphics.ellipseMode == PConstants.CORNERS)
		{
			float left   = Math.min(x,w);
			float top    = Math.min(y,h);
			float right  = Math.max(x,w);
			float bottom = Math.max(y,h);
			rx = (right-left)/2;
			ry = (bottom-top)/2;
			cx = left + rx;
			cy = top  + ry;
		}
		else if (graphics.ellipseMode == PConstants.RADIUS)
		{
			rx = Math.abs(w);
			ry = Math.abs(h);
		}

		// Add small proportionate perturbation to dimensions of ellipse
		rx += getOffset(-rx*0.05f, rx*0.05f);
		ry += getOffset(-ry*0.05f, ry*0.05f);

		// Store the original stroke and fill colours.
		int oStroke = graphics.strokeColor;
		int oFill   = graphics.fillColor;
		float oWeight = graphics.strokeWeight;
		boolean oIsStroke = graphics.stroke;
		boolean oIsFill = graphics.fill;
		float originalAngle = PApplet.degrees(hachureAngle);

		if (oIsFill)
		{
			// Erase interior of ellipse if not completely transparent
			if ((fillGap != 0) && (graphics.alpha(bgColour) > 0))
			{
				int oEllipseMode = graphics.ellipseMode;
				graphics.ellipseMode(PConstants.RADIUS);
				graphics.noStroke();
				graphics.fill(bgColour);
				graphics.ellipse(cx,cy,rx,ry);
				graphics.ellipseMode(oEllipseMode);
				graphics.noFill();
			}
			
			if (fillGap == 0)
			{
				// Fill with solid colour
				int oEllipseMode = graphics.ellipseMode;
				graphics.ellipseMode(PConstants.RADIUS);
				graphics.noStroke();
				graphics.ellipse(cx,cy,rx,ry);
				graphics.ellipseMode(oEllipseMode);
				graphics.noFill();
			}
			else
			{
				// We will be using strokes to fill, so change stroke to fill colour.
				graphics.stroke(oFill);
				
				// Perturb hachure angle if requested.
				if (anglePerturbation > 0)
				{
					setHachureAngle(originalAngle + (2*rand.nextFloat()-1)*anglePerturbation);
				}
				
				if (fillWeight <=0)
				{
					graphics.strokeWeight(oWeight/2f);
				}
				else
				{
					graphics.strokeWeight(fillWeight);
				}

				double aspectRatio = ry/rx;
				double hyp = (float)Math.sqrt(aspectRatio*tanAngle*aspectRatio*tanAngle+1);
				double sinAnglePrime = aspectRatio*tanAngle / hyp;
				double cosAnglePrime = 1 / hyp;
				
				float gap = fillGap;	// Gap between adjacent lines.
				if (gap < 0)
				{
					gap = oWeight*4;					
				}
				if (isAlternating)
				{
					// If zig-zag filling, increase gap to give approximately similar density.
					gap *= 1.41f;
				}
				double gapPrime = gap/((rx*ry/Math.sqrt((ry*cosAnglePrime)*(ry*cosAnglePrime) + (rx*sinAnglePrime)*(rx*sinAnglePrime)))/rx);
				double halfLen = (float)Math.sqrt((rx*rx) - (cx-rx+gapPrime)*(cx-rx+gapPrime));
				float[] prevP2 = affine(cx-rx+gapPrime,cy+halfLen,cx,cy,sinAnglePrime,cosAnglePrime,aspectRatio);
				
				for (double xPos=cx-rx+gapPrime; xPos<cx+rx; xPos+=gapPrime)
				{
					halfLen = (float)Math.sqrt((rx*rx) - (cx-xPos)*(cx-xPos));
					float[] p1 = affine(xPos,cy-halfLen,cx,cy,sinAnglePrime,cosAnglePrime,aspectRatio);
					float[] p2 = affine(xPos,cy+halfLen,cx,cy,sinAnglePrime,cosAnglePrime,aspectRatio);
					
					if (isAlternating)
					{
						line(prevP2[0],prevP2[1],p1[0],p1[1]);	
					}
					line(p1[0],p1[1],p2[0],p2[1]);
					
					prevP2 = p2;
				}
				
				// Perturb hachure angle if requested.
				if (anglePerturbation > 0)
				{
					setHachureAngle(originalAngle);
				}
			}
		}

		// Restore original stroke colour and weight.
		if (oIsStroke)
		{
			graphics.stroke(oStroke);
		}
		else
		{
			graphics.noStroke();
		}
		graphics.strokeWeight(oWeight);


		// Draw outline if requested
		if (graphics.stroke)
		{
			graphics.noFill();

			buildEllipse(cx,cy,rx,ry,1,ellipseInc*getOffset(0.1f,getOffset(0.4f, 1f)));
			buildEllipse(cx,cy,rx,ry,1.5f,0);

			if (oIsFill)
			{
				graphics.fill(oFill);
			}
		}
	}
	
	/** Draws a rectangle using the given location and dimensions. By default the x,y coordinates
	 *  will be the top left of the rectangle, but the meanings of these parameters can be 
	 *  changed with Processing's rectMode() command.
	 *  @param x x coordinate of the rectangle position
	 *  @param y y coordinate of the rectangle position.
	 *  @param w Width of the rectangle (but see modifications possible with rectMode())
	 *  @param h Height of the rectangle (but see modifications possible with rectMode())
	 */
	public void rect(float x, float y, float w, float h)
	{
		if (isHandy == false)
		{
			graphics.rect(x,y,w,h);
			return;
		}

		// Default is to use 'CORNER' mode for defining rectangle
		float left   = Math.min(x,x+w);
		float top    = Math.min(y,y+h);
		float right  = Math.max(x,x+w);
		float bottom = Math.max(y,y+h);

		// Adjust bounds for other rectangle modes.
		if (graphics.rectMode == PConstants.CORNERS)
		{
			left   = Math.min(x,w);
			top    = Math.min(y,h);
			right  = Math.max(x,w);
			bottom = Math.max(y,h);
		}
		else if (graphics.rectMode == PConstants.CENTER)
		{
			float halfWidth = w/2f;
			float halfHeight = h/2f;

			left   = Math.min(x-halfWidth,x+halfWidth);
			right  = Math.max(x-halfWidth,x+halfWidth);
			top    = Math.min(y-halfHeight,y+halfHeight);
			bottom = Math.max(y-halfHeight,y+halfHeight);
		}
		else if (graphics.rectMode == PConstants.RADIUS)
		{
			left   = Math.min(x-w,x+w);
			right  = Math.max(x-w,x+w);
			top    = Math.min(y-h,y+h);
			bottom = Math.max(y-h,y+h);
		}

		// Store the original stroke and fill colours.
		int oStroke = graphics.strokeColor;
		int oFill   = graphics.fillColor;
		float oWeight = graphics.strokeWeight;
		boolean oIsStroke = graphics.stroke;

		if (graphics.fill)
		{
			// Erase interior of rectangle if background colour is not completely transparent.
			if ((fillGap != 0) && (graphics.alpha(bgColour) > 0))
			{
				int oRectMode = graphics.rectMode;
				graphics.rectMode(PConstants.CORNERS);
				graphics.fill(bgColour);
				graphics.noStroke();
				graphics.rect(left,top,right,bottom);
				graphics.rectMode(oRectMode);
				graphics.noFill();
			}
			
			if (fillGap == 0)
			{
				// Fill with solid colour
				int oRectMode = graphics.rectMode;
				graphics.rectMode(PConstants.CORNERS);
				graphics.noStroke();
				graphics.rect(left,top,right,bottom);
				graphics.rectMode(oRectMode);
				graphics.noFill();
			}
			else
			{
				// We will be using strokes to fill, so change stroke to fill colour.
				graphics.stroke(oFill);
				
				// Perturb hachure angle if requested.
				float originalAngle = PApplet.degrees(hachureAngle);
				if (anglePerturbation > 0)
				{
					setHachureAngle(originalAngle + (2*rand.nextFloat()-1)*anglePerturbation);
				}
				
				if (fillWeight <=0)
				{
					graphics.strokeWeight(oWeight/2f);
				}
				else
				{
					graphics.strokeWeight(fillWeight);
				}
				
				float gap = fillGap;	// Gap between adjacent lines.
				if (gap < 0)
				{
					gap = oWeight*4;					
				}
				if (isAlternating)
				{
					// If zig-zag filling, increase gap to give approximately similar density.
					gap *= 1.41f;
				}
				
				HachureIterator i = new HachureIterator(top, bottom, left, right, gap, sinAngle, cosAngle, tanAngle);
				float[] coords;
				float[] prevCoords = i.getNextLine();
				
				if (prevCoords != null)
				{
					line(prevCoords[0],prevCoords[1],prevCoords[2],prevCoords[3]);
					
					while ((coords=i.getNextLine()) != null)
					{
						if (isAlternating)
						{
							line(prevCoords[2],prevCoords[3],coords[0],coords[1]);
						}
						line(coords[0],coords[1],coords[2],coords[3]);
						prevCoords = coords;
					}
				}
				
				// Restore original hachure angle if requested.
				if (anglePerturbation > 0)
				{
					setHachureAngle(originalAngle);
				}
			}

			// Restore original stroke colour and weight.
			if (oIsStroke)
			{
				graphics.stroke(oStroke);
			}
			else
			{
				graphics.noStroke();
			}
			graphics.strokeWeight(oWeight);
			graphics.fill(oFill);
		}

		// Draw boundary of the rectangle.
		if (graphics.stroke)
		{
			line(left,top, right, top);
			line(right,top,right,bottom);
			line(right,bottom,left,bottom);
			line(left,bottom,left,top);
		}
	}
	
	/** Draws a triangle through the three pairs of coordinates.
	 *  @param x1 x coordinate of the first triangle vertex.
	 *  @param y1 y coordinate of the first triangle vertex.
	 *  @param x2 x coordinate of the second triangle vertex.
	 *  @param y2 y coordinate of the second triangle vertex.
	 *  @param x3 x coordinate of the third triangle vertex.
	 *  @param y3 y coordinate of the third triangle vertex.
	 */
	public void triangle(float x1, float y1, float x2, float y2, float x3, float y3)
	{
		if (isHandy == false)
		{
			graphics.triangle(x1,y1,x2,y2,x3,y3);
			return;
		}

		// Bounding rectangle of the triangle.
		float left   = Math.min(x1,Math.min(x2, x3));
		float right  = Math.max(x1,Math.max(x2, x3));
		float top    = Math.min(y1,Math.min(y2, y3));
		float bottom = Math.max(y1,Math.max(y2, y3));

		// Store the original stroke and fill colours.
		int oStroke = graphics.strokeColor;
		int oFill   = graphics.fillColor;
		float oWeight = graphics.strokeWeight;
		boolean oIsStroke = graphics.stroke;

		if (graphics.fill)
		{
			// Erase interior of rectangle if background colour is not completely transparent.
			if ((fillGap != 0) && (graphics.alpha(bgColour) > 0))
			{
				graphics.fill(bgColour);
				graphics.noStroke();
				graphics.triangle(x1,y1,x2,y2,x3,y3);
				graphics.noFill();
			}
			
			if (fillGap == 0)
			{
				// Fill with solid colour
				graphics.noStroke();
				graphics.triangle(x1,y1,x2,y2,x3,y3);
				graphics.noFill();
			}
			else
			{
				// We will be using strokes to fill, so change stroke to fill colour.
				graphics.stroke(oFill);
				
				// Perturb hachure angle if requested.
				float originalAngle = PApplet.degrees(hachureAngle);
				if (anglePerturbation > 0)
				{
					setHachureAngle(originalAngle + (2*rand.nextFloat()-1)*anglePerturbation);
				}
				
				if (fillWeight <=0)
				{
					graphics.strokeWeight(oWeight/2f);
				}
				else
				{
					graphics.strokeWeight(fillWeight);
				}
				
				float gap = fillGap;	// Gap between adjacent lines.
				if (gap < 0)
				{
					gap = oWeight*4;					
				}
				
				if (isAlternating)
				{
					// If zig-zag filling, increase gap to give approximately similar density.
					gap *= 1.41f;
				}
				
				float[] prevCoords=null;
				
				HachureIterator i = new HachureIterator(top-1, bottom+1, left-1, right+1, gap, sinAngle, cosAngle, tanAngle);
				float[] rectCoords;
				while ((rectCoords=i.getNextLine()) != null)
				{
					// line within rectangle can only intersect triangle two times at most.
					float[] triCoords = new float[4];
					int nextPoint = 0;
					
					Segment s = new Segment(rectCoords[0],rectCoords[1],rectCoords[2],rectCoords[3]);
					if (s.compare(new Segment(x1,y1,x2,y2)) == Segment.Relation.INTERSECTS)
					{
						triCoords[nextPoint] = s.getIntersectionX();
						triCoords[nextPoint+1] = s.getIntersectionY();
						nextPoint+=2;
					}
					if (s.compare(new Segment(x2,y2,x3,y3)) == Segment.Relation.INTERSECTS)
					{
						triCoords[nextPoint] = s.getIntersectionX();
						triCoords[nextPoint+1] = s.getIntersectionY();
						nextPoint+=2;
					}
					if ((nextPoint <=2) && (s.compare(new Segment(x3,y3,x1,y1)) == Segment.Relation.INTERSECTS))
					{
						triCoords[nextPoint] = s.getIntersectionX();
						triCoords[nextPoint+1] = s.getIntersectionY();
						nextPoint+=2;
					}
										
					if (nextPoint == 4)
					{
						if (isAlternating) 
						{
							// Ensure coordinates are ordered consistently
							if (distSq(triCoords[0],triCoords[1],rectCoords[0],rectCoords[1]) > 
								distSq(triCoords[2],triCoords[3],rectCoords[0],rectCoords[1]))
							{
								float tempX = triCoords[2];
								float tempY = triCoords[3];
								triCoords[2] = triCoords[0];
								triCoords[3] = triCoords[1];
								triCoords[0] = tempX;
								triCoords[1] = tempY;
							}

							if (prevCoords != null)
							{
								line(prevCoords[0],prevCoords[1],triCoords[0],triCoords[1]);
							}
							prevCoords = new float[] {triCoords[2],triCoords[3]};
						}
						line(triCoords[0],triCoords[1],triCoords[2],triCoords[3]);
					}
				}
				
				// Restore original hachure angle if requested.
				if (anglePerturbation > 0)
				{
					setHachureAngle(originalAngle);
				}
			}

			// Restore original stroke colour and weight.
			if (oIsStroke)
			{
				graphics.stroke(oStroke);
			}
			else
			{
				graphics.noStroke();
			}
			graphics.strokeWeight(oWeight);
			graphics.fill(oFill);
		}

		// Draw boundary of the triangle.
		if (graphics.stroke)
		{
			line(x1,y1, x2, y2);
			line(x2,y2, x3,y3);
			line(x3,y3, x1,y1);
		}
	}
	
	/** Draws a closed polygon shape based on the given arrays of vertices.
	 *  @param xCoords x coordinates of the shape.
	 *  @param yCoords y coordinates of the shape.
	 */
	public void shape(float[] xCoords, float[] yCoords)
	{
		if (isHandy == false)
		{
			graphics.beginShape();
			for (int i=0; i<xCoords.length; i++)
			{
				graphics.vertex(xCoords[i],yCoords[i]);
			}
			graphics.endShape(PConstants.CLOSE);
			return;
		}

		// Bounding rectangle of the shape.
		float left   = xCoords[0];
		float right  = xCoords[0];
		float top    = yCoords[0];
		float bottom = yCoords[0];
		for (int i=1; i<xCoords.length; i++)
		{
			left   = Math.min(left, xCoords[i]);
			right  = Math.max(right, xCoords[i]);
			top    = Math.min(top, yCoords[i]);
			bottom = Math.max(bottom, yCoords[i]);
		}

		// Store the original stroke and fill colours.
		int oStroke = graphics.strokeColor;
		int oFill   = graphics.fillColor;
		float oWeight = graphics.strokeWeight;
		boolean oIsStroke = graphics.stroke;

		if (graphics.fill)
		{
			// Erase interior of shape if background colour is not completely transparent.
			if ((fillGap != 0) && (graphics.alpha(bgColour) > 0))
			{
				graphics.fill(bgColour);
				graphics.noStroke();
				graphics.beginShape();
				for (int i=0; i<xCoords.length; i++)
				{
					graphics.vertex(xCoords[i],yCoords[i]);
				}
				graphics.endShape(PConstants.CLOSE);				
				graphics.noFill();
			}
			
			if (fillGap == 0)
			{
				// Fill with solid colour
				graphics.noStroke();
				graphics.beginShape();
				for (int i=0; i<xCoords.length; i++)
				{
					graphics.vertex(xCoords[i],yCoords[i]);
				}
				graphics.endShape(PConstants.CLOSE);
				graphics.noFill();
			}
			else
			{
				// We will be using strokes to fill, so change stroke to fill colour.
				graphics.stroke(oFill);
				
				// Perturb hachure angle if requested.
				float originalAngle = PApplet.degrees(hachureAngle);
				if (anglePerturbation > 0)
				{
					setHachureAngle(originalAngle + (2*rand.nextFloat()-1)*anglePerturbation);
				}
				
				if (fillWeight <=0)
				{
					graphics.strokeWeight(oWeight/2f);
				}
				else
				{
					graphics.strokeWeight(fillWeight);
				}
				
				float gap = fillGap;	// Gap between adjacent lines.
				if (gap < 0)
				{
					gap = oWeight*4;					
				}
				
				// TODO: Implement alternating shading for arbitrary shapes.
//				if (isAlternating)
//				{
//					// If zig-zag filling, increase gap to give approximately similar density.
//					gap *= 1.41f;
//				}
				
//				ArrayList<float[]> prevCoords= new ArrayList<float[]>();

				// Iterate through each line that could intersect with the shape.
				HachureIterator it = new HachureIterator(top-1, bottom+1, left-1, right+1, gap, sinAngle, cosAngle, tanAngle);
				
				float[] rectCoords = null;
			
				while ((rectCoords=it.getNextLine()) != null)
				{
					ArrayList<float[]> lines = getIntersectingLines(rectCoords,xCoords,yCoords);
					
					for (int i=0; i<lines.size(); i+=2)
					{
						if (i < lines.size()-1)
						{
							float[] p1 = lines.get(i);
							float[] p2 = lines.get(i+1);
							line(p1[0],p1[1],p2[0],p2[1]);
							
//							if (isAlternating)
//							{
//								if (prevCoords.size() == lines.size())
//								{
//									line(prevCoords.get(i/2)[0],prevCoords.get(i/2)[1],p1[0],p1[1]);
//								}
//								prevCoords.add(new float[] {p2[0],p2[1]});
//							}
						}
					}
				}
				
				// Restore hachure angle if requested.
				if (anglePerturbation > 0)
				{
					setHachureAngle(originalAngle);
				}
			}

			// Restore original stroke colour and weight.
			if (oIsStroke)
			{
				graphics.stroke(oStroke);
			}
			else
			{
				graphics.noStroke();
			}
			graphics.strokeWeight(oWeight);
			graphics.fill(oFill);
		}

		// Draw boundary of the shape.
		if (graphics.stroke)
		{
			for (int i=0; i<xCoords.length-1; i++)
			{
				line(xCoords[i],yCoords[i],xCoords[i+1],yCoords[i+1]);
			}
			line(xCoords[xCoords.length-1],yCoords[xCoords.length-1],xCoords[0],yCoords[0]);
		}
	}
	
	
	/** Draws a complex line that links the given coordinates. 
	 *  @param xCoords x coordinates of the line.
	 *  @param yCoords y coordinates of the line.
	 */
	public void polyLine(float[] xCoords, float[] yCoords)
	{
		if (isHandy == false)
		{
			graphics.pushStyle();
			graphics.noFill();
			graphics.beginShape();
			for (int i=0; i<xCoords.length; i++)
			{
				graphics.vertex(xCoords[i],yCoords[i]);
			}
			graphics.endShape();
			graphics.popStyle();
			return;
		}
		
		if (graphics.stroke)
		{
			for (int i=0; i<xCoords.length-1; i++)
			{
				line(xCoords[i],yCoords[i],xCoords[i+1],yCoords[i+1]);
			}
		}
	}

	/** Draws a 2D line between the given coordinate pairs. 
	 *  @param x1 x coordinate of the start of the line.
	 *  @param y1 y coordinate of the start of the line.
	 *  @param x2 x coordinate of the end of the line.
	 *  @param y2 y coordinate of the end of the line.
	 */
	public void line(float x1, float y1, float x2, float y2)
	{		
		if (isHandy == false)
		{
			graphics.line(x1,y1,x2,y2);
			return;
		}

		line(x1,y1,x2,y2,2);
	}
	
	/** Converts an array list of numeric values into a floating point array.
	 *  Useful for methods that require primitive arrays of floats based on a dynamic collection.
	 *  @param list List of numbers to convert.
	 *  @return Array of floats representing the list.
	 */
	public static float[] toArray(List<Float> list)
	{
		float[] array = new float[list.size()];
		for (int i=0; i< list.size(); i++)
		{
			array[i] = list.get(i).floatValue();
		}
		return array;
	}
	
	// --------------------------------- Private methods --------------------------------- 
	
	/** Draws a 2D line between the given coordinate pairs. This version allows the random offset of the 
	 *  two end points to be set explicitly.
	 *  @param x1 x coordinate of the start of the line.
	 *  @param y1 y coordinate of the start of the line.
	 *  @param x2 x coordinate of the end of the line.
	 *  @param y2 y coordinate of the end of the line.
	 *  @param maxOffset Maximum random offset in pixel coordinates.
	 */
	private void line(float x1, float y1, float x2, float y2, float maxOffset)
	{
		// Ensure random perturbation is no more than 10% of line length.
		float lenSq = (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2);
		float offset = maxOffset;
		
		if (maxOffset*maxOffset*100 > lenSq)
		{
			offset = (float)Math.sqrt(lenSq)/10;
		}
		
		float halfOffset = offset/2;
		float divergePoint = 0.2f + rand.nextFloat()*0.2f;
		
		int oFill = graphics.fillColor;
		if (useSecondary)
		{
			graphics.fill(secondaryColour);
		}
		else
		{
			graphics.noFill();
		}
		
		graphics.beginShape();
		parent.vertex(x1 + getOffset(-offset,offset), y1 +getOffset(-offset,offset));
		parent.curveVertex(x1 + getOffset(-offset,offset), y1 +getOffset(-offset,offset));
		parent.curveVertex(x1+(x2 -x1)*divergePoint + getOffset(-offset,offset), y1 + (y2-y1)*divergePoint +getOffset(-offset,offset));
		parent.curveVertex(x1+2*(x2-x1)*divergePoint + getOffset(-offset,offset), y1+ 2*(y2-y1)*divergePoint +getOffset(-offset,offset)); 
		parent.curveVertex(x2 + getOffset(-offset,offset), y2 +getOffset(-offset,offset));
		parent.vertex(x2 + getOffset(-offset,offset), y2 +getOffset(-offset,offset));
		parent.endShape();  

		parent.beginShape();
		parent.vertex(x1 + getOffset(-halfOffset,halfOffset), y1 +getOffset(-halfOffset,halfOffset));
		parent.curveVertex(x1 + getOffset(-halfOffset,halfOffset), y1 +getOffset(-halfOffset,halfOffset));
		parent.curveVertex(x1+(x2 -x1)*divergePoint + getOffset(-halfOffset,halfOffset), y1 + (y2-y1)*divergePoint +getOffset(-halfOffset,halfOffset));
		parent.curveVertex(x1+2*(x2-x1)*divergePoint + getOffset(-halfOffset,halfOffset), y1+ 2*(y2-y1)*divergePoint +getOffset(-halfOffset,halfOffset)); 
		parent.curveVertex(x2 + getOffset(-halfOffset,halfOffset), y2 +getOffset(-halfOffset,halfOffset));
		parent.vertex(x2 + getOffset(-halfOffset,halfOffset), y2 +getOffset(-halfOffset,halfOffset));
		parent.endShape();

		graphics.fill(oFill);
	}
	

	/** Generates a random offset scaled around the given range. Note that the offset can exceed
	 *  the given maximum or minimum depending on the sketchiness of the renderer settings.
	 *  @param minVal Approximate minimum value around which the offset is generated.
	 *  @param maxVal Approximate maximum value around which the offset is generated.
	 */
	private float getOffset(float minVal, float maxVal)
	{
		return rand.nextFloat()*(maxVal-minVal)+minVal;
	}
	
	/** Adds the curved vertices to build an ellipse.
	 *  @param cx x coordinate of the centre of the ellipse.
	 *  @param cy y coordinate of the centre of the ellipse.
	 *  @param rx Radius in the x direction of the ellipse.
	 *  @param ry Radius in the y direction of the ellipse.
	 */
	private void buildEllipse(float cx, float cy, float rx, float ry, float offset, float overlap)
	{
		float radialOffset = getOffset(-0.5f,0.5f)-PConstants.HALF_PI;
			
		graphics.beginShape();

		// First control point should be penultimate point on ellipse.	
		graphics.curveVertex(getOffset(-offset,offset)+cx+0.9f*rx*(float)Math.cos(radialOffset-ellipseInc),
				getOffset(-offset,offset)+cy+0.9f*ry*(float)Math.sin(radialOffset-ellipseInc));

		for (float theta=radialOffset; theta<PConstants.TWO_PI+radialOffset-0.01; theta+=ellipseInc)
		{
			graphics.curveVertex(getOffset(-offset,offset)+cx+rx*(float)Math.cos(theta),
					getOffset(-offset,offset)+cy+ry*(float)Math.sin(theta));
		}
		
		graphics.curveVertex(getOffset(-offset,offset)+cx+rx*(float)Math.cos(radialOffset+PConstants.TWO_PI+overlap*0.5f),
				getOffset(-offset,offset)+cy+ry*(float)Math.sin(radialOffset+PConstants.TWO_PI+overlap*0.5f));
		
		graphics.curveVertex(getOffset(-offset,offset)+cx+0.98f*rx*(float)Math.cos(radialOffset+overlap),
				getOffset(-offset,offset)+cy+0.98f*ry*(float)Math.sin(radialOffset+overlap));
		
		graphics.curveVertex(getOffset(-offset,offset)+cx+0.9f*rx*(float)Math.cos(radialOffset+overlap*0.5),
				getOffset(-offset,offset)+cy+0.9f*ry*(float)Math.sin(radialOffset+overlap*0.5));

		graphics.endShape();
	}
	
	/** Applies a combined affine transformation that translates (cx,cy) to origin, rotates it, scales it
	 *  according to the given aspect ratio and then translates back to (cx,cy)
	 *  @param x x coordinate of the point to transform.
	 *  @param y y coordinate of the point to transform.
	 *  @param cx x coordinate of the centre point to translate to origin.
	 *  @param cy y coordinate of the centre point to translate to origin.
	 *  @param sinAnglePrime sine of modified angle that accounts for scaling.
	 *  @param cosAnglePrime cosine of modified angle that accoints for scaling.
	 *  @param R aspect ratio of ellipse (y/x).
	 *  @return Transformed coordinate pair.
	 */
	private static float[] affine(double x, double y, double cx, double cy, double sinAnglePrime, double cosAnglePrime,double R)
	{		
		double A = -cx*cosAnglePrime-cy*sinAnglePrime+cx;
		double B = R*(cx*sinAnglePrime - cy*cosAnglePrime)+cy;
		double C = cosAnglePrime;
		double D = sinAnglePrime;
		double E = -R*sinAnglePrime;
		double F = R*cosAnglePrime;
		return new float[] {(float)(A+ C*x + D*y), (float)(B + E*x + F*y)};
	}
	
	/** Provides a list of the coordinates of interior lines that represent the intersections
	 *  of a given line with a given shape boundary. 
	 * @param lineCoords The endpoints of the line to intersect.
	 * @param xCoords The x coordinates of the boundary of the shape to be intersected with the line.
	 * @param yCoords The y coordinates of the boundary of the shape to be intersected with the line.
	 * @return List of coordinates representing the intersecting lines.
	 */
	private ArrayList<float[]> getIntersectingLines(float[] lineCoords, float[]xCoords, float[]yCoords)
	{
		TreeMap<Float,float[]> intersections = new TreeMap<Float,float[]>();
		Segment s1 = new Segment(lineCoords[0],lineCoords[1],lineCoords[2],lineCoords[3]);

		// Final all points of intersection between line and shape boundary and ensure they are ordered from the start of the line.
		for (int i=0; i<xCoords.length; i++)
		{
			Segment s2 = new Segment(xCoords[i],yCoords[i],xCoords[(i+1)%xCoords.length],yCoords[(i+1)%xCoords.length]);

			if (s1.compare(s2) == Segment.Relation.INTERSECTS)
			{
				intersections.put(new Float(distSq(s1.getIntersectionX(), s1.getIntersectionY(), lineCoords[0],lineCoords[1])), 
						          new float[] {s1.getIntersectionX(),s1.getIntersectionY()});
			}
		}
			
		return new ArrayList<float[]>(intersections.values());
	}
	
	/** Calculates the squared distance between a given pair of points.
	 * @param x1 x coordinate of first point.
	 * @param y1 y coordinate of first point.
	 * @param x2 x coordinate of second point.
	 * @param y2 y coordinate of second point.
	 * @return Squared distance between points.
	 */
	private float distSq(float x1, float y1, float x2, float y2)
	{
		return (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2);
	}
}
