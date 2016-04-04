package org.gicentre.handy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

// *****************************************************************************************
/** The renderer that draws graphic primitives in a sketchy style. The style of sketchiness
 *  can be configured in this class. Based on an original idea by <a 
 *  href="http://www.local-guru.net/blog/2010/4/23/simulation-of-hand-drawn-lines-in-processing" 
 *  target="_blank">Nikolaus Gradwohl</a>
 *  @author Jo Wood, giCentre, City University London based on an idea by Nikolaus Gradwohl.
 *  @version 2.0, 1st April, 2016.
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

public class HandyRenderer
{
	// -------------------------------- Object Variables ---------------------------------  

	private PApplet parent;						// Parent class invoking the renderer.
	private PGraphics graphics;					// Graphics context in which this class is to render.
	private Random rand;						// Random number generator for random but repeatable offsets.
	private float cosAngle,sinAngle,tanAngle;	// Lookups for quick calculations.
	private List<float[]> vertices;				// Temporary store of shape or polyline vertices.
	private HashSet<Integer>curveIndices;		// Pointer to vertices that refer to curves
	private int shapeMode;						// Type of setting for shape drawing.
	private boolean is3DShape;					// Indicates if shape defined with vertices is 2d or 3d.

	private enum Plane2d {XY, XZ, YZ}			// Used to identify plane onto which textures may be mapped for 3d faces.

	// Configuration settings
	private boolean isHandy;					// Determines if normal or hand-drawn appearance is used.
	private int fillColour, strokeColour;		// Main fill and stroke colours. 
	private int bgColour, secondaryColour;		// Background colour and secondary fill colour	
	private boolean overrideFillColour;			// Determines whether the fill colour is based on parent's fill colour or the setting in this class.
	private boolean overrideStrokeColour;		// Determines whether the stroke colour is based on parent's stroke colour or the setting in this class.
	private boolean useSecondary;				// Determines whether secondary colour is to be used.
	private boolean isAlternating;				// Determines whether hachuring alternates in direction in continuous stroke.
	private float hachureAngle;					// Angle of diagonal hachuring.
	private float anglePerturbation;			// Random perturbation in hachure angle per object drawn.
	private float fillWeight, fillGap;			// Hachure filling characteristics.
	private float strokeWeight;					// Stroke weight for lines.
	private float roughness;					// Scaling for random perturbations.
	private float bowing;						// Scaling of the 'bowing' of lines at their midpoint.

	private int numEllipseSteps;
	private float ellipseInc;					// Incremental steps along an ellipse.

	private static final float MIN_ROUGHNESS = 0.1f;	// Roughess less than this value will be consisidered 0.


	// ----------------------------------- Constructor -----------------------------------

	/** Creates a new HandyRender capable of using standard Processing drawing commands
	 *  to render features in a sketchy hand-drawn style.
	 *  @param parent Parent sketch that will be drawn to.
	 */
	public HandyRenderer(PApplet parent)
	{
		this.parent = parent;
		this.graphics = parent.g;

		numEllipseSteps = 9;
		ellipseInc = PConstants.TWO_PI/numEllipseSteps;
		vertices = new ArrayList<float[]>();
		curveIndices = new HashSet<Integer>();
		is3DShape = false;

		// Set initial configuration options.
		setIsHandy(true);
		resetStyles();		
	}

	// ------------------------------------- Methods ------------------------------------- 

	/** Sets the graphics context into which all output is directed. This method allows
	 *  output to be redirected to print output, offscreen buffers etc.
	 *  @param graphics New graphics context in which to render.
	 */
	public void setGraphics(PGraphics graphics)
	{
		this.graphics = graphics;
	}

	/** Copies the settings from one graphics context to another. This can be useful when creating an offscreen
	 *  buffer that needs to have the same appearance settings as the current context.
	 *  @param gSrc Source graphics context.
	 *  @param gDst Destination graphics context.
	 */
	public static void copyGraphics(PGraphics gSrc, PGraphics gDst)
	{
		gDst.backgroundColor = gSrc.backgroundColor;
		gDst.bezierDetail    = gSrc.bezierDetail;
		gDst.colorMode       = gSrc.colorMode;
		gDst.curveDetail     = gSrc.curveDetail;
		gDst.curveTightness  = gSrc.curveTightness;
		gDst.ellipseMode     = gSrc.ellipseMode;
		gDst.fill            = gSrc.fill;
		gDst.fillColor       = gSrc.fillColor;
		gDst.imageMode       = gSrc.imageMode;
		gDst.pixelDensity    = gSrc.pixelDensity;
		gDst.rectMode        = gSrc.rectMode;
		gDst.shapeMode       = gSrc.shapeMode;
		gDst.smooth          = gSrc.smooth;
		gDst.stroke          = gSrc.stroke;
		gDst.strokeCap       = gSrc.strokeCap;
		gDst.strokeJoin      = gSrc.strokeJoin;
		gDst.strokeColor     = gSrc.strokeColor;
		gDst.strokeWeight    = gSrc.strokeWeight;	
		gDst.textAlign       = gSrc.textAlign;
		gDst.textAlignY      = gSrc.textAlignY;
		gDst.textFont        = gSrc.textFont;
		gDst.textLeading     = gSrc.textLeading;
	}

	/** Sets the seed used for random offsets when drawing. This should be called if repeated calls
	 *  to the same sketchy drawing methods should result in exactly the same rendering. If not called
	 *  a vibrating appearance can be given to the rendering.
	 *  @param seed Random number seed. This can be any whole number, generating the same random variation
	 *                                  in rendering on each redraw.
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
	
	/** Reports whether the renderer is currently set to draw in a sketchy style or not.
	 * @return True if drawing in a sketchy style or false if not.
	 */
	public boolean isHandy()
	{ 
		return isHandy;
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

	/** Sets the fill colour for closed shapes. Note this will only have an effect if
	 *  <code>setOverrideFillColour()</code> is true. 
	 *  @param colour Fill colour to use.
	 */
	public void setFillColour(int colour)
	{
		this.fillColour = colour;
	}

	/** Determines whether or not to override the fill colour that would otherwise be determined by
	 *  the sketch's <code>fillColor</code> setting. If overridden, the colour is instead chosen from the 
	 *  value last provided to <code>setFillColour()</code>.
	 *  @param override If true the interior colour of features is determined by <code>setFillColour</code>,
	 *                  if not, it is determined by the parent sketch's fill colour setting.
	 */
	public void setOverrideFillColour(boolean override)
	{
		this.overrideFillColour = override;
	}

	/** Sets the stroke colour for rendering features. Note this will only have an effect if
	 *  <code>setOverrideStrokeColour()</code> is true. 
	 *  @param colour Stroke colour to use.
	 */
	public void setStrokeColour(int colour)
	{
		this.strokeColour = colour;
	}

	/** Determines whether or not to override the stroke colour that would otherwise be determined by
	 *  the sketch's <code>strokeColor</code> setting. If overridden, the colour is instead chosen from the 
	 *  value last provided to <code>setStrokeColour()</code>.
	 *  @param override If true the stroke colour of features is determined by <code>setStrokeColour</code>,
	 *                  if not, it is determined by the parent sketch's stroke colour setting.
	 */
	public void setOverrideStrokeColour(boolean override)
	{
		this.overrideStrokeColour = override;
	}

	/** Determines whether or not a secondary colour is used for filling lines.
	 *  @param useSecondary If true a secondary colour is used.
	 */
	public void setUseSecondaryColour(boolean useSecondary)
	{
		this.useSecondary = useSecondary;
	}

	/** Sets the secondary colour for line filling. Note this will only have an effect if
	 *  <code>setUseSecondaryColour()</code> is true.
	 *  @param colour Colour to tint line filling.
	 */
	public void setSecondaryColour(int colour)
	{
		this.secondaryColour = colour;
	}

	/** Determines the thickness of fill lines. If zero or negative, the thickness is
	 *  proportional to the sketch's current strokeWeight.
	 *  @param weight Fill weight in pixel units. If zero or negative, fill weight is based on the sketch's strokeWeight setting. 
	 */
	public void setFillWeight(float weight)
	{
		this.fillWeight = weight;
	}

	/** Determines the thickness of outer lines. If zero or negative, the thickness is
	 *  proportional to the sketch's current strokeWeight.
	 *  @param weight Stroke weight in pixel units. If zero or negative, stroke weight is based on the sketch's strokeWeight setting. 
	 */
	public void setStrokeWeight(float weight)
	{
		this.strokeWeight = weight;
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

	/** Sets the general roughness of the sketch. 1 is a typically neat sketchiness, 0 is very precise, 5 
	 *  is very sketchy. Values are capped at 10.
	 *  @param roughness The sketchiness of the rendering. The larger the number the more sketchy the rendering.
	 */
	public void setRoughness(float roughness)
	{
		// Cap roughness between 0 and 10.
		this.roughness = Math.max(0,Math.min(roughness, 10));
	}

	/** Sets the amount of 'bowing' of lines (contols the degree to which a straigh line appears as an 'I' or 'C'). Applies to
	 *  all straight lines such as rectangle boundaries, hachuring and the segments of a polygon boundary. A value of 0 means 
	 *  there is no systematic displacement away from the straight line path between the two endpoints in a line. A value of 1
	 *  gives a small random bowing, 10 an extremely bowed appearance. Note that bowing is independent of other random variations
	 *  in line geometry.Values are capped at 10.
	 *  @param bowing The degree of bowing in the rendering if straight lines. The larger the number the more 'loopy' lines appear.
	 */
	public void setBowing(float bowing)
	{
		// Cap roughness between 0 and 10.
		this.bowing = Math.max(0,Math.min(bowing, 10));
	}

	/** Resets the sketchy styles to default values.
	 */
	public void resetStyles()
	{
		isAlternating = false;
		anglePerturbation = 0;
		roughness = 1;
		bowing = 1;
		rand = new Random(12345);
		setStrokeColour(graphics.strokeColor);
		setFillColour(graphics.fillColor);
		setBackgroundColour(graphics.color(255));
		setSecondaryColour(graphics.color(255));
		setUseSecondaryColour(false);
		setFillWeight(-1);
		setStrokeWeight(-1);
		setFillGap(-1);
		setHachureAngle(-41);
		setHachurePerturbationAngle(0);
		setOverrideFillColour(false);
		setOverrideStrokeColour(false);
	}

	// -------------------------------------- Drawing methods --------------------------------------

	/** Draws 2D point at the given location. Currently this draws the point in the same style as the
	 *  default Processing renderer.
	 *  @param x x coordinate of the point.
	 *  @param y y coordinate of the point.
	 */
	public void point(float x, float y)
	{
		graphics.point(x, y);
	}

	/** Draws 3D point at the given location. Currently this draws the point in the same style as the
	 *  default Processing renderer.
	 *  @param x x coordinate of the point.
	 *  @param y y coordinate of the point.
	 *  @param z z coordinate of the point.
	 */
	public void point(float x, float y, float z)
	{
		graphics.point(x, y, z);
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

		graphics.pushStyle();

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

		if ((rx == 0) && (ry == 0))
		{
			// Never draw circles of radius 0.
			return;
		}

		if ((rx < roughness/4) || (ry < roughness/4))
		{
			// Don't draw anything with a radius less than a quarter of the roughness value
			return;
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

			// Only fill interior if the fill colour is distinct from the background.
			if (bgColour != (overrideFillColour?fillColour:oFill))
			{
				if (fillGap == 0)
				{
					// Fill with solid colour
					if (overrideFillColour)
					{
						graphics.fill(fillColour);
					}
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
					if (overrideFillColour)
					{
						graphics.stroke(fillColour);
					}
					else
					{
						graphics.stroke(oFill);
					}

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
							line(prevP2[0],prevP2[1],p1[0],p1[1],2);	
						}
						line(p1[0],p1[1],p2[0],p2[1],2);

						prevP2 = p2;
					}

					// Perturb hachure angle if requested.
					if (anglePerturbation > 0)
					{
						setHachureAngle(originalAngle);
					}
				}
			}
		}

		// Set stroke colour and weight.
		if ((oIsStroke) || (strokeWeight > 0))
		{
			if (overrideStrokeColour)
			{
				graphics.stroke(strokeColour);
			}
			else
			{
				graphics.stroke(oStroke);	
			}
			if (strokeWeight > 0)
			{
				graphics.strokeWeight(strokeWeight);
			}
			else
			{
				graphics.strokeWeight(oWeight);
			}
		}
		else
		{
			graphics.noStroke();
		}

		// Draw outline if requested
		if ((oIsStroke) || (overrideStrokeColour))
		{
			graphics.noFill();
			if (roughness < MIN_ROUGHNESS)
			{
				graphics.ellipse(cx,cy,2*rx,2*ry);
				graphics.ellipse(cx,cy,2*rx,2*ry);
			}
			else
			{
				buildEllipse(cx,cy,rx,ry,1,ellipseInc*getOffset(0.1f,getOffset(0.4f, 1f)));
				buildEllipse(cx,cy,rx,ry,1.5f,0);
			}
		}

		// Restore original style settings.
		graphics.popStyle();
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

		graphics.pushStyle();
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

			// Only fill interior if the fill colour is distinct from the background.
			if (bgColour != (overrideFillColour?fillColour:oFill))
			{
				if (fillGap == 0)
				{
					// Fill with solid colour
					if (overrideFillColour)
					{
						graphics.fill(fillColour);
					}
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
					if (overrideFillColour)
					{
						graphics.stroke(fillColour);
					}
					else
					{
						graphics.stroke(oFill);
					}

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
						line(prevCoords[0],prevCoords[1],prevCoords[2],prevCoords[3],2);

						while ((coords=i.getNextLine()) != null)
						{
							if (isAlternating)
							{
								line(prevCoords[2],prevCoords[3],coords[0],coords[1],2);
							}
							line(coords[0],coords[1],coords[2],coords[3],2);
							prevCoords = coords;
						}
					}

					// Restore original hachure angle if requested.
					if (anglePerturbation > 0)
					{
						setHachureAngle(originalAngle);
					}
				}
			}

			// Set stroke colour and weight.
			if ((oIsStroke) || (strokeWeight > 0))
			{
				if (overrideStrokeColour)
				{
					graphics.stroke(strokeColour);
				}
				else
				{
					graphics.stroke(oStroke);	
				}

				if (strokeWeight > 0)
				{
					graphics.strokeWeight(strokeWeight);
				}
				else
				{
					graphics.strokeWeight(oWeight);
				}
			}
			else
			{
				graphics.noStroke();
			}

			graphics.fill(oFill);
		}

		// Draw boundary of the rectangle.
		if ((oIsStroke) || (overrideStrokeColour))
		{
			line(left,top, right, top,2);
			line(right,top,right,bottom,2);
			line(right,bottom,left,bottom,2);
			line(left,bottom,left,top,2);
		}

		// Restore original style settings.
		graphics.popStyle();
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
		graphics.pushStyle();

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

			// Only fill interior if the fill colour is distinct from the background.
			if (bgColour != (overrideFillColour?fillColour:oFill))
			{
				if (fillGap == 0)
				{
					// Fill with solid colour
					if (overrideFillColour)
					{
						graphics.fill(fillColour);
					}
					graphics.noStroke();
					graphics.triangle(x1,y1,x2,y2,x3,y3);
					graphics.noFill();
				}
				else
				{
					// We will be using strokes to fill, so change stroke to fill colour.
					if (overrideFillColour)
					{
						graphics.stroke(fillColour);
					}
					else
					{
						graphics.stroke(oFill);
					}

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
									line(prevCoords[0],prevCoords[1],triCoords[0],triCoords[1],2);
								}
								prevCoords = new float[] {triCoords[2],triCoords[3]};
							}
							line(triCoords[0],triCoords[1],triCoords[2],triCoords[3],2);
						}
					}

					// Restore original hachure angle if requested.
					if (anglePerturbation > 0)
					{
						setHachureAngle(originalAngle);
					}
				}
			}

			// Restore original fill settings.
			graphics.fill(oFill);
		}

		// Draw boundary of the triangle.
		if ((oIsStroke) || (overrideStrokeColour))
		{
			if (overrideStrokeColour)
			{
				graphics.stroke(strokeColour);
			}
			else
			{
				graphics.stroke(oStroke);	
			}

			if (strokeWeight > 0)
			{
				graphics.strokeWeight(strokeWeight);
			}
			else
			{
				graphics.strokeWeight(oWeight);
			}
			line(x1,y1, x2, y2,2);
			line(x2,y2, x3,y3,2);
			line(x3,y3, x1,y1,2);
		}

		// Restore original stroke settings.
		graphics.popStyle();
	}

	/** Draws a quadrilateral shape. Similar to a rectangle but angles not constrained to 90 degrees.
	 *  Coordinates can proceed in either a clockwise or anti-clockwise direction.
	 *  @param x1 x coordinate of the first quadrilateral vertex.
	 *  @param y1 y coordinate of the first quadrilateral vertex.
	 *  @param x2 x coordinate of the second quadrilateral vertex.
	 *  @param y2 y coordinate of the second quadrilateral vertex.
	 *  @param x3 x coordinate of the third quadrilateral vertex.
	 *  @param y3 y coordinate of the third quadrilateral vertex.
	 *  @param x4 x coordinate of the fourth quadrilateral vertex.
	 *  @param y4 y coordinate of the fourth quadrilateral vertex.
	 */
	public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4)
	{
		shape(new float[] {x1,x2,x3,x4}, new float[] {y1,y2,y3,y4}, true);
	}

	/** Draws an arc along the outer edge of an ellipse defined by the x,y, w and h parameters.
	 *  This version allows the maximum random offset of the arc to be set explicitly.
	 *  @param x x coordinate of the ellipse's position around which this arc is defined.
	 *  @param y y coordinate of the ellipse's position around which this arc is defined
	 *  @param w Width of the ellipse around which this arc is defined (but see modifications possible with ellipseMode())
	 *  @param h Height of the ellipse around which this arc is defined (but see modifications possible with ellipseMode())
	 *  @param start Angle to start the arc in radians.
	 *  @param stop Angle to stop the arc in radians.
	 */
	public void arc(float x, float y, float w, float h, float start, float stop)
	{
		if (isHandy == false)
		{
			graphics.arc(x,y,w,h,start,stop);
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

		if ((rx == 0) && (ry == 0))
		{
			// Never draw circles of radius 0.
			return;
		}

		if ((rx < roughness/4) || (ry < roughness/4))
		{
			// Don't draw anything with a radius less than a quarter of the roughness value
			return;
		}	

		// Add small proportionate perturbation to dimensions of ellipse
		rx += getOffset(-rx*0.01f, rx*0.01f);
		ry += getOffset(-ry*0.01f, ry*0.01f);

		// Ensure start and stop angles are positive and sensible.
		float strt = start;
		float stp = stop;

		while (strt < 0)
		{
			strt += PConstants.TWO_PI;
			stp += PConstants.TWO_PI;
		}

		if (stp - strt > PConstants.TWO_PI) 
		{
			strt = 0;
			stp = PConstants.TWO_PI;
		}

		float arcInc = Math.min(ellipseInc/2,(stp-strt)/2);

		// Create a curved polygon to represent the sector.
		boolean oIsStroke = graphics.stroke;
		boolean oIsFill  = graphics.fill;
		int oStroke = graphics.strokeColor;
		int oFill   = graphics.fillColor;

		graphics.noStroke();

		beginShape();
		curveVertex(cx+rx*(float)Math.cos(strt), cy+ry*(float)Math.sin(strt));

		for (float theta=strt; theta<=stp; theta+=arcInc)
		{
			curveVertex(cx+rx*(float)Math.cos(theta), cy+ry*(float)Math.sin(theta));
		}

		// Last control point should be duplicate the last point of the arc.	
		curveVertex(cx+rx*(float)Math.cos(stp), cy+ry*(float)Math.sin(stp));
		curveVertex(cx+rx*(float)Math.cos(stp), cy+ry*(float)Math.sin(stp));
		vertex(cx+rx*(float)Math.cos(stp), cy+ry*(float)Math.sin(stp));

		vertex(cx,cy);
		endShape();


		// Draw outside edge of arc if we have a stroke.
		if (oIsStroke)
		{
			graphics.stroke(oStroke);
			graphics.noFill();

			beginShape();
			curveVertex(cx+rx*(float)Math.cos(strt), cy+ry*(float)Math.sin(strt));

			for (float theta=strt; theta<=stp; theta+=arcInc)
			{
				curveVertex(cx+rx*(float)Math.cos(theta), cy+ry*(float)Math.sin(theta));
			}

			// Last control point should be duplicate the last point of the arc.	
			curveVertex(cx+rx*(float)Math.cos(stp), cy+ry*(float)Math.sin(stp));
			curveVertex(cx+rx*(float)Math.cos(stp), cy+ry*(float)Math.sin(stp));

			endShape();
		}

		// Restore original stroke and fill settings.
		graphics.strokeColor = oStroke;
		graphics.stroke = oIsStroke;
		graphics.fillColor = oFill;
		graphics.fill = oIsFill;
	}

	/** Starts a new shape of type <code>POLYGON</code>. This must be paired with a call to 
	 *  <code>endShape()</code> or one of its variants.
	 */
	public void beginShape()
	{
		beginShape(PConstants.POLYGON);
	}

	/** Starts a new shape of the type specified in the mode parameter. This must be paired
	 *  with a call to <code>endShape()</code> or one of its variants.
	 *	@param mode either POINTS, LINES, TRIANGLES, TRIANGLE_FAN, TRIANGLE_STRIP, QUADS, QUAD_STRIP	 
	 */	
	public void beginShape(int mode)
	{
		if (isHandy == false)
		{
			graphics.beginShape(mode);
		}
		else
		{
			this.shapeMode=mode;
			vertices.clear();
			curveIndices.clear();
			is3DShape = false;
		}
	}

	/** Adds a 2d vertex to a shape that was started with a call to <code>beginShape()</code> 
	 *  or one of its variants.
	 *  @param x x coordinate of vertex to add.
	 *  @param y y coordinate of vertex to add.
	 */
	public void vertex(float x, float y)
	{
		if (isHandy == false)
		{
			graphics.vertex(x,y);
		}
		else
		{
			vertices.add(new float[] {x,y});
		}
	}

	/** Adds a 3d vertex to a shape that was started with a call to <code>beginShape()</code> 
	 *  or one of its variants.
	 *  @param x x coordinate of vertex to add.
	 *  @param y y coordinate of vertex to add.
	 *  @param z z coordinate of vertex to add.
	 */
	public void vertex(float x, float y, float z)
	{
		is3DShape = true;
		if (isHandy == false)
		{
			graphics.vertex(x,y,z);
		}
		else
		{
			vertices.add(new float[] {x,y,z});
		}
	}

	/** Adds a 2d vertex to a shape or line that has curved edges. That shape should have been
	 *  started with a call to <code>beginShape()</code> without any parameter.
	 *  @param x x coordinate of vertex to add.
	 *  @param y y coordinate of vertex to add.
	 */
	public void curveVertex(float x, float y)
	{
		if (isHandy == false)
		{
			graphics.curveVertex(x,y);
		}
		else
		{
			// Log this position in the vertex list as being a curve
			curveIndices.add(new Integer(vertices.size()));

			// Store the vertex geometry.
			vertices.add(new float[] {x,y});			
		}
	}

	/** Adds a 3d vertex to a shape or line that has curved edges. That shape should have been
	 *  started with a call to <code>beginShape()</code> without any parameter.
	 *  @param x x coordinate of vertex to add.
	 *  @param y y coordinate of vertex to add.
	 *  @param z z coordinate of vertex to add.
	 */
	public void curveVertex(float x, float y, float z)
	{
		is3DShape = true;
		if (isHandy == false)
		{
			graphics.curveVertex(x,y,z);
		}
		else
		{
			// Log this position in the vertex list as being a curve
			curveIndices.add(new Integer(vertices.size()));

			// Store the vertex geometry.
			vertices.add(new float[] {x,y,z});			
		}
	}

	/** Ends a shape definition. This should have been paired with a call to <code>beginShape()</code>
	 *  or one of its variants. Note that this version will not close the shape if the last vertex does 
	 *  not match the first one.
	 */
	public void endShape()
	{
		if (isHandy == false)
		{
			graphics.endShape();
		}
		else
		{
			if (is3DShape)
			{
				drawShape3d(false);
				is3DShape = false;
			}
			else
			{
				drawShape2d(false);
			}

			vertices.clear();
			curveIndices.clear();
		}
	}

	/** Ends a shape definition. This should have been paired with a call to <code>beginShape()</code> 
	 *  or one of its variants. If the mode parameter <code>CLOSE</code> the shape will be closed.
	 *  @param mode Type of shape closure.
	 */
	public void endShape(int mode) 
	{
		if (isHandy == false)
		{
			graphics.endShape(mode);
		}
		else
		{
			if (is3DShape)
			{
				drawShape3d(mode==PConstants.CLOSE);
				is3DShape = false;
			}
			else
			{
				drawShape2d(mode==PConstants.CLOSE);
			}
		}
		vertices.clear();
		curveIndices.clear();
	}

	/** Draws 3D cube with the given unit dimension.
	 *  @param bSize Size of each dimension of the cube.
	 */
	public void box(float bSize)
	{
		box(bSize, bSize, bSize);
	}

	/** Draws 3D box with the given dimensions.
	 *  @param bWidth Width of the box.
	 *  @param bHeight Height of the box.
	 *  @param bDepth Depth of the box.
	 */
	public void box(float bWidth, float bHeight, float bDepth)
	{
		if (isHandy == false)
		{
			graphics.box(bWidth,bHeight,bDepth);
		}
		else
		{			
			// Create a box without any strokes first.
			float bW = bWidth/2f;
			float bH = bHeight/2f;
			float bD = bDepth/2f;
			graphics.pushStyle();
			boolean isStrokeOverridden = overrideStrokeColour;
			setOverrideStrokeColour(false);
			graphics.noStroke();

			beginShape(PConstants.QUADS);
			  vertex(-bW,  bH,  bD);
			  vertex( bW,  bH,  bD);
			  vertex( bW, -bH,  bD);
			  vertex(-bW, -bH,  bD);

			  vertex( bW,  bH,  bD);
			  vertex( bW,  bH, -bD);
			  vertex( bW, -bH, -bD);
			  vertex( bW, -bH,  bD);
			  
			  vertex( bW,  bH, -bD);
			  vertex(-bW,  bH, -bD);
			  vertex(-bW, -bH, -bD);
			  vertex( bW, -bH, -bD);
			  
			  vertex(-bW,  bH, -bD);
			  vertex(-bW,  bH,  bD);
			  vertex(-bW, -bH,  bD);
			  vertex(-bW, -bH, -bD);

			  vertex(-bW,  bH, -bD);
			  vertex( bW,  bH, -bD);
			  vertex( bW,  bH,  bD);
			  vertex(-bW,  bH,  bD);
			  
			  vertex(-bW, -bH, -bD);
			  vertex( bW, -bH, -bD);
			  vertex( bW, -bH,  bD);
			  vertex(-bW, -bH,  bD);
			 endShape();
			 
			 graphics.popStyle();
			 setOverrideStrokeColour(isStrokeOverridden);
			 
			 // Finally draw lines along each of the box edges.
			 line(-bW,  bH,  bD, bW,  bH,  bD);
			 line( bW,  bH,  bD, bW, -bH,  bD);
			 line( bW, -bH,  bD,-bW, -bH,  bD);
			 line(-bW, -bH,  bD,-bW,  bH,  bD);

			 line( bW,  bH,  bD, bW,  bH, -bD);
			 line( bW,  bH, -bD, bW, -bH, -bD);
			 line( bW, -bH, -bD, bW, -bH,  bD);

			 line( bW,  bH, -bD,-bW,  bH, -bD);
			 line(-bW,  bH, -bD,-bW, -bH, -bD);
			 line(-bW, -bH, -bD, bW, -bH, -bD);

			 line(-bW,  bH, -bD,-bW,  bH,  bD);
			 line(-bW, -bH,  bD,-bW, -bH, -bD);
		}
	}

	/** Draws a closed 2d polygon based on the given arrays of vertices.
	 *  @param xCoords x coordinates of the shape.
	 *  @param yCoords y coordinates of the shape.
	 */
	public void shape(float[] xCoords, float[] yCoords)
	{
		shape(xCoords,yCoords,true);
	}

	/** Draws a closed 3d polygon based on the given arrays of vertices.
	 *  @param xCoords x coordinates of the shape.
	 *  @param yCoords y coordinates of the shape.
	 *  @param zCoords z coordinates of the shape.
	 */
	public void shape(float[] xCoords, float[] yCoords, float[] zCoords)
	{
		shape(xCoords,yCoords,zCoords,true);
	}

	/** Draws a 2d polygon based on the given arrays of vertices. This version can 
	 *  draw either open or closed shapes.
	 *  @param xCoords x coordinates of the shape.
	 *  @param yCoords y coordinates of the shape.
	 *  @param closeShape Boundary of shape will be closed if true.
	 */
	public void shape(float[] xCoords, float[] yCoords, boolean closeShape)
	{
		if ((xCoords == null) || (yCoords == null) || (xCoords.length ==0) || (yCoords.length == 0))
		{
			System.err.println("No coordinates provided to shape().");
			return;
		}			

		if (isHandy == false)
		{
			graphics.beginShape();
			for (int i=0; i<xCoords.length; i++)
			{
				graphics.vertex(xCoords[i],yCoords[i]);
			}
			if (closeShape)
			{
				graphics.endShape(PConstants.CLOSE);
			}
			else
			{
				graphics.endShape();
			}
			return;
		}

		graphics.pushStyle();

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

			// Only fill interior if the fill colour is distinct from the background.
			if (bgColour != (overrideFillColour?fillColour:oFill))
			{
				if (fillGap == 0)
				{
					// Fill with solid colour
					if (overrideFillColour)
					{
						graphics.fill(fillColour);
					}
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
					if (overrideFillColour)
					{
						graphics.stroke(fillColour);
					}
					else
					{
						graphics.stroke(oFill);
					}

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
								line(p1[0],p1[1],p2[0],p2[1],2);

								//							if (isAlternating)
								//							{
								//								if (prevCoords.size() == lines.size())
								//								{
								//									line(prevCoords.get(i/2)[0],prevCoords.get(i/2)[1],p1[0],p1[1],2);
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
			}

			// Restore original fill and stroke weight settings.
			graphics.fill(oFill);
			graphics.strokeWeight(oWeight);
		}

		// Draw boundary of the shape.
		if ((oIsStroke) || (overrideStrokeColour))
		{
			if (overrideStrokeColour)
			{
				graphics.stroke(strokeColour);
			}
			else
			{
				graphics.stroke(oStroke);	
			}

			if (strokeWeight > 0)
			{
				graphics.strokeWeight(strokeWeight);
			}
			else
			{
				graphics.strokeWeight(oWeight);
			}

			for (int i=0; i<xCoords.length-1; i++)
			{
				line(xCoords[i],yCoords[i],xCoords[i+1],yCoords[i+1],2);
			}
			if (closeShape)
			{
				line(xCoords[xCoords.length-1],yCoords[xCoords.length-1],xCoords[0],yCoords[0],2);
			}
		}

		// Restore styles.
		graphics.popStyle();
	}

	/** Draws a 3d polygon based on the given arrays of vertices. This version can 
	 *  draw either open or closed shapes.
	 *  @param xCoords x coordinates of the shape.
	 *  @param yCoords y coordinates of the shape.
	 *  @param zCoords z coordinates of the shape.
	 *  @param closeShape Boundary of shape will be closed if true.
	 */
	public void shape(float[] xCoords, float[] yCoords, float[] zCoords, boolean closeShape)
	{
		if ((xCoords == null) || (yCoords == null) || (zCoords == null) || (xCoords.length ==0) || (yCoords.length == 0) || (zCoords.length == 0))
		{
			System.err.println("No coordinates provided to shape().");
			return;
		}			

		if (isHandy == false)
		{
			graphics.beginShape();
			for (int i=0; i<xCoords.length; i++)
			{
				graphics.vertex(xCoords[i],yCoords[i],zCoords[i]);
			}
			if (closeShape)
			{
				graphics.endShape(PConstants.CLOSE);
			}
			else
			{
				graphics.endShape();
			}
			return;
		}

		graphics.pushStyle();


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
					graphics.vertex(xCoords[i],yCoords[i],zCoords[i]);
				}
				graphics.endShape(PConstants.CLOSE);				
				graphics.noFill();
			}

			// Only fill interior if the fill colour is distinct from the background.
			if (bgColour != (overrideFillColour?fillColour:oFill))
			{
				if (fillGap == 0)
				{
					// Fill with solid colour
					if (overrideFillColour)
					{
						graphics.fill(fillColour);
					}
					graphics.noStroke();
					graphics.beginShape();
					for (int i=0; i<xCoords.length; i++)
					{
						graphics.vertex(xCoords[i],yCoords[i],zCoords[i]);
					}
					graphics.endShape(PConstants.CLOSE);
					graphics.noFill();
				}
				else
				{				
					// We will be using strokes to fill, so change stroke to fill colour.
					if (overrideFillColour)
					{
						graphics.stroke(fillColour);
					}
					else
					{
						graphics.stroke(oFill);
					}

					if (fillWeight <=0)
					{
						graphics.strokeWeight(oWeight/2f);
					}
					else
					{
						graphics.strokeWeight(fillWeight);
					}

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

					// Do the drawing.
					drawHachuredFace(xCoords, yCoords, zCoords, gap);

					// Restore hachure angle if requested.
					if (anglePerturbation > 0)
					{
						setHachureAngle(originalAngle);
					}
				}
			}

			// Restore original fill and stroke weight settings.
			graphics.fill(oFill);
			graphics.strokeWeight(oWeight);

		}

		// Draw boundary of the shape.
		if ((oIsStroke) || (overrideStrokeColour))
		{
			if (overrideStrokeColour)
			{
				graphics.stroke(strokeColour);
			}
			else
			{
				graphics.stroke(oStroke);	
			}

			if (strokeWeight > 0)
			{
				graphics.strokeWeight(strokeWeight);
			}
			else
			{
				graphics.strokeWeight(oWeight);
			}

			for (int i=0; i<xCoords.length-1; i++)
			{
				line(xCoords[i],yCoords[i],zCoords[i],xCoords[i+1],yCoords[i+1],zCoords[i+1],2);
			}
			if (closeShape)
			{
				line(xCoords[xCoords.length-1],yCoords[xCoords.length-1],zCoords[xCoords.length-1],xCoords[0],yCoords[0],zCoords[0],2);
			}
		}

		// Restore styles.
		graphics.popStyle();
	}

	/** Draws a complex line that links the given coordinates. 
	 *  @param xCoords x coordinates of the line.
	 *  @param yCoords y coordinates of the line.
	 */
	public void polyLine(float[] xCoords, float[] yCoords)
	{
		if ((xCoords == null) || (yCoords == null) || (xCoords.length ==0) || (yCoords.length == 0))
		{
			System.err.println("No coordinates provided to polyLine().");
			return;
		}

		if ((graphics.stroke) || (overrideStrokeColour))
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

			graphics.pushStyle();
			int oStroke = graphics.strokeColor;

			if (overrideStrokeColour)
			{
				graphics.stroke(strokeColour);
			}
			else
			{
				graphics.stroke(oStroke);	
			}
			if (strokeWeight > 0)
			{
				graphics.strokeWeight(strokeWeight);
			}

			for (int i=0; i<xCoords.length-1; i++)
			{
				line(xCoords[i],yCoords[i],xCoords[i+1],yCoords[i+1],2);
			}

			// Restore style settings.
			graphics.popStyle();
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
		if ((graphics.stroke) || (overrideStrokeColour))
		{
			if (isHandy == false)
			{			
				graphics.line(x1,y1,x2,y2);
				return;
			}

			graphics.pushStyle();
			if (overrideStrokeColour)
			{
				graphics.stroke(strokeColour);
			}

			if (strokeWeight > 0)
			{
				graphics.strokeWeight(strokeWeight);
			}

			line(x1,y1,x2,y2,2);

			// Restore original stroke settings.
			graphics.popStyle();
		}
	}

	/** Draws a 3D line between the given coordinate triplets. 
	 *  @param x1 x coordinate of the start of the line.
	 *  @param y1 y coordinate of the start of the line.
	 *  @param z1 z coordinate of the start of the line.
	 *  @param x2 x coordinate of the end of the line.
	 *  @param y2 y coordinate of the end of the line.
	 *  @param z2 z coordinate of the end of the line.
	 */
	public void line(float x1, float y1, float z1, float x2, float y2, float z2)
	{	
		if ((graphics.stroke) || (overrideStrokeColour))
		{
			if (isHandy == false)
			{
				graphics.line(x1,y1,z1,x2,y2,z2);
				return;
			}

			graphics.pushStyle();
			if (overrideStrokeColour)
			{
				graphics.stroke(strokeColour);
			}

			if (strokeWeight > 0)
			{
				graphics.strokeWeight(strokeWeight);
			}

			line(x1,y1,z1,x2,y2,z2,2);

			// Restore original stroke settings.
			graphics.popStyle();
		}
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
		if (graphics.stroke)
		{
			if (isHandy == false)
			{
				graphics.line(x1,y1,x2,y2);
				return;
			}
			graphics.pushStyle();

			// Ensure random perturbation is no more than 10% of line length.
			float lenSq = (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2);
			float offset = maxOffset;

			if (maxOffset*maxOffset*100 > lenSq)
			{
				offset = (float)Math.sqrt(lenSq)/10;
			}

			float halfOffset = offset/2;
			float divergePoint = 0.2f + rand.nextFloat()*0.2f;


			if (useSecondary)
			{
				graphics.fill(secondaryColour);
			}
			else
			{
				graphics.noFill();
			}

			// This is the midpoint displacement value to give slightly bowed lines.
			float midDispX = bowing*maxOffset*(y2-y1)/200;
			float midDispY = bowing*maxOffset*(x1-x2)/200;

			midDispX = getOffset(-midDispX,midDispX);
			midDispY = getOffset(-midDispY,midDispY);

			graphics.beginShape();
			graphics.vertex(     x1 + getOffset(-offset,offset), y1 +getOffset(-offset,offset));
			graphics.curveVertex(x1 + getOffset(-offset,offset), y1 +getOffset(-offset,offset));
			graphics.curveVertex(midDispX+x1+(x2 -x1)*divergePoint + getOffset(-offset,offset), midDispY+y1 + (y2-y1)*divergePoint +getOffset(-offset,offset));
			graphics.curveVertex(midDispX+x1+2*(x2-x1)*divergePoint + getOffset(-offset,offset), midDispY+y1+ 2*(y2-y1)*divergePoint +getOffset(-offset,offset)); 
			graphics.curveVertex(x2 + getOffset(-offset,offset), y2 +getOffset(-offset,offset));
			graphics.vertex(     x2 + getOffset(-offset,offset), y2 +getOffset(-offset,offset));
			graphics.endShape();  
			
			graphics.beginShape();
			graphics.vertex(     x1 + getOffset(-halfOffset,halfOffset), y1 +getOffset(-halfOffset,halfOffset));
			graphics.curveVertex(x1 + getOffset(-halfOffset,halfOffset), y1 +getOffset(-halfOffset,halfOffset));
			graphics.curveVertex(midDispX+x1+(x2 -x1)*divergePoint + getOffset(-halfOffset,halfOffset), midDispY+y1 + (y2-y1)*divergePoint +getOffset(-halfOffset,halfOffset));
			graphics.curveVertex(midDispX+x1+2*(x2-x1)*divergePoint + getOffset(-halfOffset,halfOffset), midDispY+y1+ 2*(y2-y1)*divergePoint +getOffset(-halfOffset,halfOffset)); 
			graphics.curveVertex(x2 + getOffset(-halfOffset,halfOffset), y2 +getOffset(-halfOffset,halfOffset));
			graphics.vertex(     x2 + getOffset(-halfOffset,halfOffset), y2 +getOffset(-halfOffset,halfOffset));
			graphics.endShape();

			graphics.popStyle();
		}
	}


	/** Draws a 3D line between the given coordinate triplet. This version allows the random offset of the 
	 *  two end points to be set explicitly.
	 *  @param x1 x coordinate of the start of the line.
	 *  @param y1 y coordinate of the start of the line.
	 *  @param z1 z coordinate of the start of the line.
	 *  @param x2 x coordinate of the end of the line.
	 *  @param y2 y coordinate of the end of the line.
	 *  @param z2 z coordinate of the end of the line.
	 *  @param maxOffset Maximum random offset in pixel coordinates.
	 */
	private void line(float x1, float y1, float z1, float x2, float y2, float z2, float maxOffset)
	{		
		if (graphics.stroke)
		{
			if (isHandy == false)
			{
				graphics.line(x1,y1,z2,x2,y2,z2);
				return;
			}

			PVector v1 = new PVector(x2-x1,y2-y1,z2-z1);
			PVector vn = new PVector(x2-x1,y2-y1,z2-z1);
			vn.normalize();


			// Ensure random perturbation is no more than 10% of line length.
			float lenSq = v1.x*v1.x + v1.y*v1.y + v1.z*v1.z;
			float offset = maxOffset;

			if (maxOffset*maxOffset*100 > lenSq)
			{
				offset = (float)Math.sqrt(lenSq)/10;
			}

			float halfOffset = offset/2;
			float divergePoint = 0.2f + rand.nextFloat()*0.2f;

			graphics.pushStyle();

			if (useSecondary)
			{
				graphics.fill(secondaryColour);
			}
			else
			{
				graphics.noFill();
			}

			// This is the midpoint displacement value to give slightly bowed lines.
			PVector v2 = new PVector(1,1,1);
			PVector vCross = vn.cross(v2);
			float v1Len = v1.mag();

			float midDispX = v1Len*vCross.x/200;
			float midDispY = v1Len*vCross.y/200;
			float midDispZ = v1Len*vCross.z/200;

			midDispX = getOffset(-midDispX,midDispX);
			midDispY = getOffset(-midDispY,midDispY);
			midDispZ = getOffset(-midDispZ,midDispZ);

			graphics.beginShape();
			graphics.vertex(x1 + getOffset(-offset,offset),      y1 +getOffset(-offset,offset), z1+getOffset(-offset,offset));
			graphics.curveVertex(x1 + getOffset(-offset,offset), y1 +getOffset(-offset,offset), z1+getOffset(-offset,offset));
			graphics.curveVertex(midDispX+x1 + v1.x*divergePoint + getOffset(-offset,offset),  midDispY+y1 + v1.y*divergePoint +getOffset(-offset,offset),   midDispZ+z1 + v1.z*divergePoint +getOffset(-offset,offset));
			graphics.curveVertex(midDispX+x1 +2*v1.x*divergePoint + getOffset(-offset,offset), midDispY+y1+ 2*v1.y*divergePoint +getOffset(-offset,offset),  midDispZ+z1+ 2*v1.z*divergePoint +getOffset(-offset,offset)); 
			graphics.curveVertex(x2 + getOffset(-offset,offset), y2 +getOffset(-offset,offset), z2 +getOffset(-offset,offset));
			graphics.vertex(x2 + getOffset(-offset,offset), y2 +getOffset(-offset,offset), z2 +getOffset(-offset,offset));
			graphics.endShape();  

			graphics.beginShape();
			graphics.vertex(x1 + getOffset(-halfOffset,halfOffset),      y1 +getOffset(-halfOffset,halfOffset), z1 +getOffset(-halfOffset,halfOffset));
			graphics.curveVertex(x1 + getOffset(-halfOffset,halfOffset), y1 +getOffset(-halfOffset,halfOffset), z1 +getOffset(-halfOffset,halfOffset));
			graphics.curveVertex(midDispX+x1+v1.x*divergePoint + getOffset(-halfOffset,halfOffset),   midDispY+y1 + v1.y*divergePoint +getOffset(-halfOffset,halfOffset),   midDispZ+z1 + v1.z*divergePoint +getOffset(-halfOffset,halfOffset));
			graphics.curveVertex(midDispX+x1+2*v1.x*divergePoint + getOffset(-halfOffset,halfOffset), midDispY+y1+ 2*v1.y*divergePoint +getOffset(-halfOffset,halfOffset),  midDispZ+z1+ 2*v1.z*divergePoint +getOffset(-halfOffset,halfOffset)); 
			graphics.curveVertex(x2 + getOffset(-halfOffset,halfOffset), y2 +getOffset(-halfOffset,halfOffset), z2 +getOffset(-halfOffset,halfOffset));
			graphics.vertex(x2 + getOffset(-halfOffset,halfOffset), y2 +getOffset(-halfOffset,halfOffset), z2 +getOffset(-halfOffset,halfOffset));
			graphics.endShape();

			graphics.popStyle();
		}
	}

	/** Draws a 2D shape after it has been finished with <code>endShape()</code>.
	 *  @param closeShape True if the shape is to be closed.
	 */
	private void drawShape2d(boolean closeShape)
	{
		// Shapes with at least one curve vertex are a special case.
		if (curveIndices.size() > 0)
		{
			curvedShape();
			return;
		}

		float[] xs=new float[vertices.size()];
		float[] ys=new float[vertices.size()];

		int i=0;
		for (float[] coords : vertices)
		{
			xs[i]=coords[0];
			ys[i]=coords[1];
			i++;
		}

		if (this.shapeMode==PConstants.POLYGON)
		{
			shape(xs,ys,closeShape);
		}
		else if (this.shapeMode==PConstants.LINES)
		{
			for (i=0;i<xs.length-1;i+=2)
			{
				line(xs[i],ys[i],xs[i+1],ys[i+1]);
			}
		}
		else if (this.shapeMode==PConstants.POINTS)
		{
			for (i=0;i<xs.length;i++)
			{
				point(xs[i],ys[i]);
			}
		}
		else if (this.shapeMode==PConstants.TRIANGLES)
		{
			for (i=0;i<xs.length-2;i+=3)
			{
				triangle(xs[i],ys[i],xs[i+1],ys[i+1],xs[i+2],ys[i+2]);
			}
		}
		else if (this.shapeMode==PConstants.TRIANGLE_STRIP)
		{
			for (i=0;i<xs.length-2;i++)
			{
				triangle(xs[i],ys[i],xs[i+1],ys[i+1],xs[i+2],ys[i+2]);
			}
		}
		else if (this.shapeMode==PConstants.TRIANGLE_FAN)
		{
			for (i=1;i<xs.length-1;i++)
			{
				triangle(xs[0],ys[0],xs[i],ys[i],xs[i+1],ys[i+1]);
			}
		}
		else if (this.shapeMode==PConstants.QUADS)
		{
			for (i=0;i<xs.length-3;i+=4)
			{
				float[] quadXs=new float[]{xs[i],xs[i+1],xs[i+2],xs[i+3]};
				float[] quadYs=new float[]{ys[i],ys[i+1],ys[i+2],ys[i+3]};
				shape(quadXs,quadYs);
			}
		}
		else if (this.shapeMode==PConstants.QUAD_STRIP)
		{
			for (i=0;i<xs.length-3;i+=2)
			{
				float[] quadXs=new float[]{xs[i],xs[i+1],xs[i+3],xs[i+2]};
				float[] quadYs=new float[]{ys[i],ys[i+1],ys[i+3],ys[i+2]};
				shape(quadXs,quadYs);
			}
		}
	}


	/** Draws a 3D shape after it has been finished with <code>endShape()</code>.
	 *  @param closeShape True if the shape is to be closed.
	 */
	private void drawShape3d(boolean closeShape)
	{
		// Shapes with at least one curve vertex are a special case.
		if (curveIndices.size() > 0)
		{
			curvedShape();
			return;
		}

		float[] xs=new float[vertices.size()];
		float[] ys=new float[vertices.size()];
		float[] zs=new float[vertices.size()];

		int i=0;
		for (float[] coords : vertices)
		{
			xs[i]=coords[0];
			ys[i]=coords[1];
			zs[i]=coords[2];
			i++;
		}

		if (this.shapeMode==PConstants.POLYGON)
		{
			shape(xs,ys,zs,closeShape);
		}
		else if (this.shapeMode==PConstants.LINES)
		{
			for (i=0;i<xs.length-1;i+=2)
			{
				line(xs[i],ys[i],zs[i],xs[i+1],ys[i+1],zs[i+1]);
			}
		}
		else if (this.shapeMode==PConstants.POINTS)
		{
			for (i=0;i<xs.length;i++)
			{
				point(xs[i],ys[i],zs[i]);
			}
		}
		else if (this.shapeMode==PConstants.TRIANGLES)
		{
			for (i=0;i<xs.length-2;i+=3)
			{
				float[] triXs = new float[] {xs[i],xs[i+1],xs[i+2]};
				float[] triYs = new float[] {ys[i],ys[i+1],ys[i+2]};
				float[] triZs = new float[] {zs[i],zs[i+1],zs[i+2]};
				shape(triXs,triYs,triZs);
			}
		}
		else if (this.shapeMode==PConstants.TRIANGLE_STRIP)
		{
			for (i=0;i<xs.length-2;i++)
			{
				float[] triXs = new float[] {xs[i],xs[i+1],xs[i+2]};
				float[] triYs = new float[] {ys[i],ys[i+1],ys[i+2]};
				float[] triZs = new float[] {zs[i],zs[i+1],zs[i+2]};
				shape(triXs,triYs,triZs);
			}
		}
		else if (this.shapeMode==PConstants.TRIANGLE_FAN)
		{
			for (i=1;i<xs.length-1;i++)
			{
				float[] triXs = new float[] {xs[0],xs[i],xs[i+1]};
				float[] triYs = new float[] {ys[0],ys[i],ys[i+1]};
				float[] triZs = new float[] {zs[0],zs[i],zs[i+1]};
				shape(triXs,triYs,triZs);
			}
		}
		else if (this.shapeMode==PConstants.QUADS)
		{
			for (i=0;i<xs.length-3;i+=4)
			{
				float[] quadXs=new float[]{xs[i],xs[i+1],xs[i+2],xs[i+3]};
				float[] quadYs=new float[]{ys[i],ys[i+1],ys[i+2],ys[i+3]};
				float[] quadZs=new float[]{zs[i],zs[i+1],zs[i+2],zs[i+3]};
				shape(quadXs,quadYs,quadZs);
			}
		}
		else if (this.shapeMode==PConstants.QUAD_STRIP)
		{
			for (i=0;i<xs.length-3;i+=2)
			{
				float[] quadXs=new float[]{xs[i],xs[i+1],xs[i+3],xs[i+2]};
				float[] quadYs=new float[]{ys[i],ys[i+1],ys[i+3],ys[i+2]};
				float[] quadZs=new float[]{zs[i],zs[i+1],zs[i+3],zs[i+2]};
				shape(quadXs,quadYs,quadZs);
			}
		}
	}

	/** Fills the face implied by the given 3d geometry with a hachured texture.
	 *  @param xCoords x Coordinates of the face to fill.
	 *  @param yCoords y Coordinates of the face to fill.
	 *  @param zCoords z Coordinates of the face to fill.
	 *  @param gap Gap between hachures.
	 */
	private void drawHachuredFace(float[] xCoords, float[] yCoords, float[] zCoords, float gap)
	{
		// Bounding rectangle of the shape. For the 3d case, we use a fudge that attempts to find the 
		// axis plane with most variation. This will work well for sides of a cuboid for example where each
		// face is 2 dimensional and parallel to two axes. If a face varies in 3 dimensions, results may be distorted.
		float minX = xCoords[0];
		float maxX = xCoords[0];
		float minY = yCoords[0];
		float maxY = yCoords[0];
		float minZ = zCoords[0];
		float maxZ = zCoords[0];

		for (int i=1; i<xCoords.length; i++)
		{
			minX = Math.min(minX, xCoords[i]);
			maxX = Math.max(maxX, xCoords[i]);
			minY = Math.min(minY, yCoords[i]);
			maxY = Math.max(maxY, yCoords[i]);
			minZ = Math.min(minZ, zCoords[i]);
			maxZ = Math.max(maxZ, zCoords[i]);
		}

		float xRange = maxX-minX;
		float yRange = maxY-minY;
		float zRange = maxZ-minZ;
		
		
		// Check that we have at least two dimensions that need to have a surface
		if ( ((xRange < 2) && (yRange < 2)) ||
			 ((xRange < 2) && (zRange < 2)) ||
			 ((yRange < 2) && (zRange < 2)))
		{
			return;
		}

		float left = minX;
		float right = maxX;
		float top = maxY;
		float bottom = minY;
		Plane2d projectedPlane = Plane2d.XY;

		if ((yRange < zRange) && (yRange < xRange))
		{
			top = maxZ;
			bottom = minZ;
			projectedPlane = Plane2d.XZ;
		}
		else if ((xRange < zRange) && (xRange < yRange))
		{
			top = maxZ;
			bottom = minZ;
			left = minY;
			right = maxY;
			projectedPlane = Plane2d.YZ;
		}

		// Create hachured image and map it as a texture onto the shape.
		HachureIterator hi = new HachureIterator(0, top-bottom, 0, right-left, gap, sinAngle, cosAngle, tanAngle);

		float[] coords;
		float[] prevCoords = hi.getNextLine();
		PGraphics origGraphics = graphics;

		PGraphics textureImg = parent.createGraphics((int)(right-left), (int)(top-bottom),PConstants.P3D);	
				

		textureImg.beginDraw();				
		copyGraphics(graphics,textureImg);
		textureImg.smooth();			// Needed because 3D renderers may not allow smoothing.
		setGraphics(textureImg);
		graphics.fill(graphics.strokeColor);

		if (prevCoords != null)
		{
			line(prevCoords[0],prevCoords[1],prevCoords[2],prevCoords[3],2);		

			while ((coords=hi.getNextLine()) != null)
			{
				if (isAlternating)
				{
					line(prevCoords[2],prevCoords[3],coords[0],coords[1],2);
				}
				line(coords[0],coords[1],coords[2],coords[3],2);
				prevCoords = coords;
			}
		}

		textureImg.endDraw();		
		setGraphics(origGraphics);

		graphics.noFill();
		graphics.noStroke();
		graphics.beginShape();
		graphics.texture(textureImg);

		if (projectedPlane == Plane2d.XY)
		{
			for (int i=0; i<xCoords.length; i++)
			{
				float u = PApplet.map(xCoords[i],left,right,0,right-left);
				float v = PApplet.map(yCoords[i],bottom,top,0,top-bottom);
				graphics.vertex(xCoords[i],yCoords[i],zCoords[i],u,v);
			}
		}
		else if (projectedPlane == Plane2d.XZ)
		{
			for (int i=0; i<xCoords.length; i++)
			{
				float u = PApplet.map(xCoords[i],left,right,right-left,0);
				float v = PApplet.map(zCoords[i],bottom,top,0,top-bottom);
				graphics.vertex(xCoords[i],yCoords[i],zCoords[i],u,v);
			}
		}
		else if (projectedPlane == Plane2d.YZ)
		{
			for (int i=0; i<xCoords.length; i++)
			{
				float u = PApplet.map(yCoords[i],left,right,right-left,0);
				float v = PApplet.map(zCoords[i],bottom,top,0,top-bottom);
				graphics.vertex(xCoords[i],yCoords[i],zCoords[i],u,v);
			}
		}
		graphics.endShape(PConstants.CLOSE);
	}


	/** Draws a shape that includes curved edges.
	 */
	private void curvedShape()
	{
		float[] v0,v1=null,v2=null,v3=null;						// Last four vertices
		float[] v0Prime,v1Prime=null,v2Prime=null,v3Prime=null;	// Minor variation in curve.

		graphics.pushStyle();

		if (graphics.fill)
		{
			// Build a straight line approximation of the shape.
			// This is necessary to calculate the interior shape reasonably quickly.
			List<float[]> coords = new ArrayList<float[]>();

			v0 = vertices.get(0);
			v0[0] += getOffset(-2, 2);
			v0[1] += getOffset(-2, 2);

			v0Prime = vertices.get(0);
			v0Prime[0] += getOffset(-2, 2);
			v0Prime[1] += getOffset(-2, 2);

			for (int i=0; i<vertices.size(); i++)
			{
				boolean isCurveVertex = curveIndices.contains(new Integer(i));

				// Advance vertices along by 1.
				v3 = v2;
				v2 = v1;
				v1 = v0;

				v0 = new float[2];
				v0[0] = vertices.get(i)[0]+getOffset(-2, 2);
				v0[1] = vertices.get(i)[1]+getOffset(-2, 2);

				if (isCurveVertex == false)
				{
					// Store normal coordinate.
					coords.add(vertices.get(i));
				}
				else
				{
					if (i >=3)
					{
						// Add enough vertices to approximate curve with a straight line.
						float dist = distSq(v2[0], v2[1], v1[0], v1[1]);
						float step = (25 + 300*roughness)/dist;

						for (float t=0; t<1; t+= step)
						{
							float x = graphics.curvePoint(v3[0], v2[0], v1[0], v0[0], t);  
							float y = graphics.curvePoint(v3[1], v2[1], v1[1], v0[1], t);
							coords.add(new float[] {x,y});
						}
					}
				}
			}

			// Convert coordinates into array and send to shape to fill.
			float[] xs=new float[coords.size()];
			float[] ys=new float[coords.size()];
			int i=0;
			for (float[] vertex : coords)
			{
				xs[i]=vertex[0];
				ys[i]=vertex[1];
				i++;
			}

			// Temporarily disable stroke settings while we draw the interior.
			boolean isOStroke = graphics.stroke;
			boolean oOverrideStroke = overrideStrokeColour;
			int oStroke = graphics.strokeColor;

			graphics.noStroke();
			overrideStrokeColour = false;

			shape(xs, ys);

			graphics.stroke = isOStroke;
			overrideStrokeColour = oOverrideStroke;
			if (overrideStrokeColour)
			{
				graphics.stroke(strokeColour);
			}
			else if (graphics.stroke)
			{
				graphics.stroke(oStroke);	
			}
		}

		// Draw the outlines as curved lines.
		if ((graphics.stroke) || (overrideStrokeColour))
		{
			boolean oOverrideFill = overrideFillColour;

			graphics.noFill();
			overrideFillColour = false;
			if (strokeWeight > 0)
			{
				graphics.strokeWeight(strokeWeight);
			}

			v0 = vertices.get(0);
			v0[0] += getOffset(-2, 2);
			v0[1] += getOffset(-2, 2);

			v0Prime = vertices.get(0);
			v0Prime[0] += getOffset(-2, 2);
			v0Prime[1] += getOffset(-2, 2);

			for (int i=0; i<vertices.size(); i++)
			{
				boolean isCurveVertex = curveIndices.contains(new Integer(i));

				// Advance vertices along by 1.
				v3 = v2;
				v2 = v1;
				v1 = v0;

				v3Prime = v2Prime;
				v2Prime = v1Prime;
				v1Prime = v0Prime;

				v0 = new float[2];
				v0[0] = vertices.get(i)[0]+getOffset(-2, 2);
				v0[1] = vertices.get(i)[1]+getOffset(-2, 2);

				v0Prime = new float[2];
				v0Prime[0] = vertices.get(i)[0]+getOffset(-2, 2);
				v0Prime[1] = vertices.get(i)[1]+getOffset(-2, 2);

				if (isCurveVertex == false)
				{
					// Draw any straight line segments.
					if (i > 0)
					{
						line(v1[0],v1[1],v0[0],v0[1]);
					}
				}
				else
				{
					if (i >=3)
					{
						// We have enough to generate a curve.
						graphics.curve(v3[0], v3[1], v2[0], v2[1], v1[0], v1[1], v0[0], v0[1]);
						graphics.curve(v3Prime[0], v3Prime[1], v2Prime[0], v2Prime[1], v1Prime[0], v1Prime[1], v0Prime[0], v0Prime[1]);
					}
				}
			}

			overrideFillColour = oOverrideFill;
		}

		// Restore styles.
		graphics.popStyle();
	}


	/** Generates a random offset scaled around the given range. Note that the offset can exceed
	 *  the given maximum or minimum depending on the sketchiness of the renderer settings.
	 *  @param minVal Approximate minimum value around which the offset is generated.
	 *  @param maxVal Approximate maximum value around which the offset is generated.
	 */
	private float getOffset(float minVal, float maxVal)
	{
		return roughness*(rand.nextFloat()*(maxVal-minVal)+minVal);
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
	private static ArrayList<float[]> getIntersectingLines(float[] lineCoords, float[]xCoords, float[]yCoords)
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
	private static float distSq(float x1, float y1, float x2, float y2)
	{
		return (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2);
	}
}
