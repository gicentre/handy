package org.gicentre.handy;

import processing.core.PApplet;
import processing.core.PGraphics;

// *****************************************************************************************
/** A PGraphics class for rendering in a sketchy style. An object of this type can be passed
 *  to a sketch's <code>beginRecord(PGraphics)</code> method. 
*   @author Jo Wood, giCentre, City University London based on an idea by Nikolaus Gradwohl.
*   @version 2.0, 1st April, 2016.
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

public class HandyGraphics extends PGraphics 
{
	// -------------------------------- Object Variables ---------------------------------  

	private HandyRenderer h;
	private PApplet parentSketch;
	
	// --------------------------------- Constructors ------------------------------------  

	/** Creates a new sketchy graphics context associated with the given parent sketch.
	 * @param parent Sketch with which this handy graphics object is to be associated.
	 */
	public HandyGraphics(PApplet parent)
	{
		this.parentSketch = parent;
		h = new HandyRenderer(parent);
	}
	
	/** Creates a new sketchy graphics context associated with the given parent sketch.
	 *  @param h Handy renderer to use when drawing to this graphics context.
	 *  @param parent Sketch with which this handy graphics object is to be associated.
	 */
	public HandyGraphics(HandyRenderer h, PApplet parent)
	{
		this.h = h;
		this.parentSketch = parent;
	}
	
	// ---------------------------- Overridden graphics methods -------------------------------------
	
	/** Draws 2D point at the given location. Currently this draws the point in the same style as the
	 *  default Processing renderer.
	 *  @param x x coordinate of the point.
	 *  @param y y coordinate of the point.
	 */
	@Override
	public void point(float x, float y)
	{
		h.point(x, y);
	}

	/** Draws 3D point at the given location. Currently this draws the point in the same style as the
	 *  default Processing renderer.
	 *  @param x x coordinate of the point.
	 *  @param y y coordinate of the point.
	 *  @param z z coordinate of the point.
	 */
	@Override
	public void point(float x, float y, float z)
	{
		h.point(x, y, z);
	}

	/** Draws an ellipse using the given location and dimensions. By default the x,y coordinates
	 *  will be centre of the ellipse, but the meanings of these parameters can be changed with
	 *  Processing's ellipseMode() command.
	 *  @param x x coordinate of the ellipse's position
	 *  @param y y coordinate of the ellipse's position.
	 *  @param eWidth Width of the ellipse (but see modifications possible with ellipseMode())
	 *  @param wHeight Height of the ellipse (but see modifications possible with ellipseMode())
	 */
	@Override
	public void ellipse(float x, float y, float eWidth, float eHeight)
	{
		h.ellipse(x, y, eWidth, eHeight);
	}

	/** Draws a rectangle using the given location and dimensions. By default the x,y coordinates
	 *  will be the top left of the rectangle, but the meanings of these parameters can be 
	 *  changed with Processing's rectMode() command.
	 *  @param x x coordinate of the rectangle position
	 *  @param y y coordinate of the rectangle position.
	 *  @param w Width of the rectangle (but see modifications possible with rectMode())
	 *  @param h Height of the rectangle (but see modifications possible with rectMode())
	 */
	@Override
	public void rect(float x, float y, float rWidth, float rHeight)
	{
		h.rect(x, y, rWidth, rHeight);
	}

	/** Draws a triangle through the three pairs of coordinates.
	 *  @param x1 x coordinate of the first triangle vertex.
	 *  @param y1 y coordinate of the first triangle vertex.
	 *  @param x2 x coordinate of the second triangle vertex.
	 *  @param y2 y coordinate of the second triangle vertex.
	 *  @param x3 x coordinate of the third triangle vertex.
	 *  @param y3 y coordinate of the third triangle vertex.
	 */
	@Override
	public void triangle(float x1, float y1, float x2, float y2, float x3, float y3)
	{
		h.triangle(x1,y1,x2,y2,x3,y3);
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
	@Override
	public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4)
	{
		h.quad(x1,y1,x2,y2,x3,y3,x4,y4);
	}

	/** Draws an arc along the outer edge of an ellipse defined by the x,y, w and h parameters.
	 *  This version allows the maximum random offset of the arc to be set explicitly.
	 *  @param x x coordinate of the ellipse's position around which this arc is defined.
	 *  @param y y coordinate of the ellipse's position around which this arc is defined
	 *  @param aWidth Width of the ellipse around which this arc is defined (but see modifications possible with ellipseMode())
	 *  @param aHeight Height of the ellipse around which this arc is defined (but see modifications possible with ellipseMode())
	 *  @param start Angle to start the arc in radians.
	 *  @param stop Angle to stop the arc in radians.
	 */
	@Override
	public void arc(float x, float y, float aWidth, float aHeight, float start, float stop)
	{
		h.arc(x, y, aWidth, aHeight, start, stop);
	}

	/** Starts a new shape of type <code>POLYGON</code>. This must be paired with a call to 
	 *  <code>endShape()</code> or one of its variants.
	 */
	@Override
	public void beginShape()
	{
		h.beginShape();
	}

	/** Starts a new shape of the type specified in the mode parameter. This must be paired
	 *  with a call to <code>endShape()</code> or one of its variants.
	 *	@param mode either POINTS, LINES, TRIANGLES, TRIANGLE_FAN, TRIANGLE_STRIP, QUADS, QUAD_STRIP	 
	 */	
	@Override
	public void beginShape(int mode)
	{
		h.beginShape(mode);
	}

	/** Adds a 2d vertex to a shape that was started with a call to <code>beginShape()</code> 
	 *  or one of its variants.
	 *  @param x x coordinate of vertex to add.
	 *  @param y y coordinate of vertex to add.
	 */
	@Override
	public void vertex(float x, float y)
	{
		h.vertex(x, y);
	}

	/** Adds a 3d vertex to a shape that was started with a call to <code>beginShape()</code> 
	 *  or one of its variants.
	 *  @param x x coordinate of vertex to add.
	 *  @param y y coordinate of vertex to add.
	 *  @param z z coordinate of vertex to add.
	 */
	@Override
	public void vertex(float x, float y, float z)
	{
		h.vertex(x, y, z);
	}

	/** Adds a 2d vertex to a shape or line that has curved edges. That shape should have been
	 *  started with a call to <code>beginShape()</code> without any parameter.
	 *  @param x x coordinate of vertex to add.
	 *  @param y y coordinate of vertex to add.
	 */
	@Override
	public void curveVertex(float x, float y)
	{
		h.curveVertex(x,y);
	}

	/** Adds a 3d vertex to a shape or line that has curved edges. That shape should have been
	 *  started with a call to <code>beginShape()</code> without any parameter.
	 *  @param x x coordinate of vertex to add.
	 *  @param y y coordinate of vertex to add.
	 *  @param z z coordinate of vertex to add.
	 */
	@Override
	public void curveVertex(float x, float y, float z)
	{
		h.curveVertex(x,y,z);
	}

	/** Ends a shape definition. This should have been paired with a call to <code>beginShape()</code>
	 *  or one of its variants. Note that this version will not close the shape if the last vertex does 
	 *  not match the first one.
	 */
	@Override
	public void endShape()
	{
		h.endShape();
	}

	/** Ends a shape definition. This should have been paired with a call to <code>beginShape()</code> 
	 *  or one of its variants. If the mode parameter <code>CLOSE</code> the shape will be closed.
	 */
	@Override
	public void endShape(int mode) 
	{
		h.endShape(mode);
	}

	/** Draws 3D cube with the given unit dimension.
	 *  @param bSize Size of each dimension of the cube.
	 */
	@Override
	public void box(float bSize)
	{
		h.box(bSize);
	}

	/** Draws 3D box with the given dimensions.
	 *  @param bW Width of the box.
	 *  @param bH Height of the box.
	 *  @param bD Depth of the box.
	 */
	@Override
	public void box(float bWidth, float bHeight, float bDepth)
	{
		h.box(bWidth, bHeight, bDepth);
	}

	/** Draws a 2D line between the given coordinate pairs. 
	 *  @param x1 x coordinate of the start of the line.
	 *  @param y1 y coordinate of the start of the line.
	 *  @param x2 x coordinate of the end of the line.
	 *  @param y2 y coordinate of the end of the line.
	 */
	@Override
	public void line(float x1, float y1, float x2, float y2)
	{	
		h.line(x1,y1, x2,y2);
	}

	/** Draws a 3D line between the given coordinate triplets. 
	 *  @param x1 x coordinate of the start of the line.
	 *  @param y1 y coordinate of the start of the line.
	 *  @param z1 z coordinate of the start of the line.
	 *  @param x2 x coordinate of the end of the line.
	 *  @param y2 y coordinate of the end of the line.
	 *  @param z2 z coordinate of the end of the line.
	 */
	@Override
	public void line(float x1, float y1, float z1, float x2, float y2, float z2)
	{	
		h.line(x1, y1, z1, x2, y2, z2);
	}

	// ---------------- Methods not directly available in PGraphics requiring the parent sketch instead ----------------
	
	// These are mostly ignored as the default PGraphics equivalent reports an error message that they
	// are not available in the renderer. A HandyGraphics object is normally created in a PApplet
	// that does implement these methods, so there we just override to prevent the incorrect error message. 
	
	/** Would translate the coordinate system by the given x and y values but ignored here as this will
	 *  be handled by the parent sketch.
	 * @param x x value to translate by.
	 * @param y y value to translate by.
	 */
	@Override
	public void translate(float x, float y)
	{	
		// Do nothing.
	}
	
	/** Would translate the coordinate system by the given x and y values but ignored here as this 
	 *  will be handled by the parent sketch.
	 *  @param x x value to translate by.
	 *  @param y y value to translate by.
	 *  @param z z value to translate by.
	 */
	@Override
	public void translate(float x, float y, float z)
	{	
		// Do nothing.
	}
	
	/** Would scale the coordinate system in all directions but ignored here as this will be handled
	 *  by the parent sketch.
	 *  @param s value to scale all axes by.
	 */
	@Override
	public void scale(float s)
	{	
		// Do nothing.
	}
	
	/** Would scale the coordinate system by the given x and y values but ignored here as this will be
	 *  handled by the parent sketch.
	 *  @param sx x value to scale by.
	 *  @param sy y value to scale by.
	 */
	@Override
	public void scale(float sx, float sy)
	{	
		// Do nothing.
	}
	
	/** Would scale the coordinate system by the given x, y and z values but ignored here as this will be
	 *  handled by the parent sketch.
	 *  @param sx x value to scale by.
	 *  @param sy y value to scale by.
	 *  @param sz z value to scale by.
	 */
	@Override
	public void scale(float sx, float sy, float sz)
	{	
		// Do nothing.
	}
	
	/** Would rotate the coordinate system by the given angle but ignored here as this will be
	 *  handled by the parent sketch.
	 *  @param angle Angle by which to rotate the coordinate system (ignored).
	 */
	@Override
	public void rotate(float angle)
	{	
		// Do nothing.
	}
	
	/** Would rotate the coordinate system by the given angles but ignored here as this will be
	 *  handled by the parent sketch.
	 *  @param angle  Angle of rotation  (ignored).
	 *  @param x x component of vector around which to rotate (ignored)
	 *  @param y y component of vector around which to rotate (ignored)
	 *  @param z z component of vector around which to rotate (ignored)
	 */
	@Override
	public void rotate(float angle, float x, float y, float z)
	{	
		// Do nothing.
	}
	
	/** Would store a copy of the current transform matrix on the stack but ignored here as this
	 *  will be handled by the parent sketch. 
	 */
	@Override
	public void pushMatrix()
	{	
		// Do nothing.
	}
	
	/** Would retrieve a copy of the current transform matrix from the stack but ignored here as this
	 *  will be handled by the parent sketch. 
	 */
	@Override
	public void popMatrix()
	{	
		// Do nothing.
	}
	
	/** Would reset the current transform matrix to its default transform but ignored here as this
	 *  will be handled by the parent sketch.
	 */
	@Override
	public void resetMatrix()
	{	
		// Do nothing.
	}
	
	/** Would set the blend mode for this graphics context but ignores it in this case as this will
	 *  be handled by the parent sketch.
	 */
	@Override
	public void blendMode(int mode)
	{	
		// Do nothing.
	}
}
