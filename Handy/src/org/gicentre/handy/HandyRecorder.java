package org.gicentre.handy;

import processing.core.PApplet;
import processing.core.PGraphics;

// *****************************************************************************************
/** A PGraphics class for rendering in a sketchy style. An object of this type can be passed
 *  to a sketch's <code>beginRecord(PGraphics)</code> method. 
*   @author Jo Wood, giCentre, City University London.
*   @version 2.0, 3rd April, 2016.
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

public class HandyRecorder extends PGraphics 
{
	// -------------------------------- Object Variables ---------------------------------  

	private HandyRenderer h;
	
	// --------------------------------- Constructors ------------------------------------  

	/** Creates a new sketchy graphics context associated with the given parent sketch. This 
	 *  version will create an internal handy renderer with default properties.
	 *  @param parentSketch Sketch with which this handy graphics object is to be associated.
	 */
	public HandyRecorder(PApplet parentSketch)
	{
		h = new HandyRenderer(parentSketch);
	}
	
	/** Creates a new sketchy graphics context associated with the given handy renderer. This
	 *  version allows the properties of the supplied handy renderer to be changed programmatically.
	 *  @param h Handy renderer to use when drawing to this graphics context.
	 */
	public HandyRecorder(HandyRenderer h)
	{
		this.h = h;
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
		if (h.isHandy())
		{
			h.point(x, y);
		}
		else
		{
			super.point(x, y);
		}
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
		if (h.isHandy())
		{
			h.point(x, y, z);
		}
		else
		{
			super.point(x, y, z);
		}
	}

	/** Draws an ellipse using the given location and dimensions. By default the x,y coordinates
	 *  will be centre of the ellipse, but the meanings of these parameters can be changed with
	 *  Processing's ellipseMode() command.
	 *  @param x x coordinate of the ellipse's position
	 *  @param y y coordinate of the ellipse's position.
	 *  @param eWidth Width of the ellipse (but see modifications possible with ellipseMode())
	 *  @param eHeight Height of the ellipse (but see modifications possible with ellipseMode())
	 */
	@Override
	public void ellipse(float x, float y, float eWidth, float eHeight)
	{
		if (h.isHandy())
		{
			h.ellipse(x, y, eWidth, eHeight);
		}
		else
		{
			super.ellipse(x, y, eWidth, eHeight);
		}
	}

	/** Draws a rectangle using the given location and dimensions. By default the x,y coordinates
	 *  will be the top left of the rectangle, but the meanings of these parameters can be 
	 *  changed with Processing's rectMode() command.
	 *  @param x x coordinate of the rectangle position
	 *  @param y y coordinate of the rectangle position.
	 *  @param rWidth Width of the rectangle (but see modifications possible with rectMode())
	 *  @param rHeight Height of the rectangle (but see modifications possible with rectMode())
	 */
	@Override
	public void rect(float x, float y, float rWidth, float rHeight)
	{
		if (h.isHandy())
		{
			h.rect(x, y, rWidth, rHeight);
		}
		else
		{
			super.rect(x, y, rWidth, rHeight);
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
	@Override
	public void triangle(float x1, float y1, float x2, float y2, float x3, float y3)
	{
		if (h.isHandy())
		{
			h.triangle(x1,y1,x2,y2,x3,y3);
		}
		else
		{
			super.triangle(x1, y1, x2, y2, x3, y3);
		}
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
		if (h.isHandy())
		{
			h.quad(x1,y1,x2,y2,x3,y3,x4,y4);
		}
		else
		{
			super.quad(x1, y1, x2, y2, x3, y3, x4, y4);
		}
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
		if (h.isHandy())
		{
			h.arc(x, y, aWidth, aHeight, start, stop);
		}
		else
		{
			super.arc(x, y, aWidth, aHeight, start, stop);
		}
	}

	/** Starts a new shape of type <code>POLYGON</code>. This must be paired with a call to 
	 *  <code>endShape()</code> or one of its variants.
	 */
	@Override
	public void beginShape()
	{
		if (h.isHandy())
		{
			h.beginShape();
		}
		else
		{
			super.beginShape();
		}
	}

	/** Starts a new shape of the type specified in the mode parameter. This must be paired
	 *  with a call to <code>endShape()</code> or one of its variants.
	 *	@param mode either POINTS, LINES, TRIANGLES, TRIANGLE_FAN, TRIANGLE_STRIP, QUADS, QUAD_STRIP	 
	 */	
	@Override
	public void beginShape(int mode)
	{
		if (h.isHandy())
		{
			h.beginShape(mode);
		}
		else
		{
			super.beginShape(mode);
		}
	}

	/** Adds a 2d vertex to a shape that was started with a call to <code>beginShape()</code> 
	 *  or one of its variants.
	 *  @param x x coordinate of vertex to add.
	 *  @param y y coordinate of vertex to add.
	 */
	@Override
	public void vertex(float x, float y)
	{
		if (h.isHandy())
		{
			h.vertex(x, y);
		}
		else
		{
			super.vertex(x, y);
		}
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
		if (h.isHandy())
		{
			h.vertex(x, y, z);
		}
		else
		{
			super.vertex(x, y, z);
		}
	}

	/** Adds a 2d vertex to a shape or line that has curved edges. That shape should have been
	 *  started with a call to <code>beginShape()</code> without any parameter.
	 *  @param x x coordinate of vertex to add.
	 *  @param y y coordinate of vertex to add.
	 */
	@Override
	public void curveVertex(float x, float y)
	{
		if (h.isHandy())
		{
			h.curveVertex(x,y);
		}
		else
		{
			super.curveVertex(x,y);
		}
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
		if (h.isHandy())
		{
			h.curveVertex(x,y,z);
		}
		else
		{
			super.curveVertex(x, y, z);
		}
	}

	/** Ends a shape definition. This should have been paired with a call to <code>beginShape()</code>
	 *  or one of its variants. Note that this version will not close the shape if the last vertex does 
	 *  not match the first one.
	 */
	@Override
	public void endShape()
	{
		if (h.isHandy())
		{
			h.endShape();
		}
		else
		{
			super.endShape();
		}
	}

	/** Ends a shape definition. This should have been paired with a call to <code>beginShape()</code> 
	 *  or one of its variants. If the mode parameter <code>CLOSE</code> the shape will be closed.
	 */
	@Override
	public void endShape(int mode) 
	{
		if (h.isHandy())
		{
			h.endShape(mode);
		}
		else
		{
			super.endShape(mode);
		}
	}
	
	/** Draws 3D cube with the given unit dimension.
	 *  @param bSize Size of each dimension of the cube.
	 */
	@Override
	public void box(float bSize)
	{
		if (h.isHandy())
		{
			h.box(bSize);
		}
		else
		{
			super.box(bSize);
		}
	}

	/** Draws 3D box with the given dimensions.
	 *  @param bWidth Width of the box.
	 *  @param bHeight Height of the box.
	 *  @param bDepth Depth of the box.
	 */
	@Override
	public void box(float bWidth, float bHeight, float bDepth)
	{
		if (h.isHandy())
		{
			h.box(bWidth, bHeight, bDepth);
		}
		else
		{
			super.box(bWidth, bHeight, bDepth);
		}
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
		if (h.isHandy())
		{
			h.line(x1,y1, x2,y2);
		}
		else
		{
			super.line(x1, y1, x2, y2);
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
	@Override
	public void line(float x1, float y1, float z1, float x2, float y2, float z2)
	{	
		if (h.isHandy())
		{
			h.line(x1, y1, z1, x2, y2, z2);
		}
		else
		{
			super.line(x1, y1, z1, x2, y2, z2);
		}
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
	
	/** Would rotate the coordinate system around the x-axis in 3d space but ignored here as this
	 *  will be handled by the parent sketch.
	 *  @param angle Angle by which to rotate around the x-axis (ignored).
	 */
	@Override
	public void rotateX(float angle)
	{	
		// Do nothing.
	}
	
	/** Would rotate the coordinate system around the y-axis in 3d space but ignored here as this
	 *  will be handled by the parent sketch.
	 *  @param angle Angle by which to rotate around the y-axis (ignored).
	 */
	@Override
	public void rotateY(float angle)
	{	
		// Do nothing.
	}
	
	/** Would rotate the coordinate system around the z-axis in 3d space but ignored here as this
	 *  will be handled by the parent sketch.
	 *  @param angle Angle by which to rotate around the z-axis (ignored).
	 */
	@Override
	public void rotateZ(float angle)
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
	
	/** Would print the current transform matrix but ignored here as this will be handled by the parent sketch.
	 */
	@Override
	public void printMatrix()
	{	
		// Do nothing.
	}
	
	
	/** Would start 3d camera position definition but ignored here as this will be handled by the parent sketch.
	 */
	@Override
	public void beginCamera()
	{	
		// Do nothing.
	}
	
	/** Would allow the default 3d camera position to be set but ignored here as this will be handled by the parent sketch.
	 */
	@Override
	public void camera()
	{	
		// Do nothing.
	}
	
	/** Would end 3d camera position definition but ignored here as this will be handled by the parent sketch.
	 */
	@Override
	public void endCamera()
	{	
		// Do nothing.
	}
	
	/** Would allow the view frustum (clipping object) to be set but ignored here as this will be handled by the parent sketch.
	 */
	@Override
	public void frustum(float left, float right, float bottom, float top, float near, float far)
	{	
		// Do nothing.
	}
	
	/** Would apply the default perspective settings but ignored here as this will be handled by the parent sketch.
	 */
	@Override
	public void perspective()
	{	
		// Do nothing.
	}
	
	/** Would allow perspective settings to be changed but ignored here as this will be handled by the parent sketch.
	 */
	@Override
	public void perspective(float fovy, float aspect, float zNear, float zFar)
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
	
	/** Would set the default 3d lighting for this graphics context but ignores it in this case as this will
	 *  be handled by the parent sketch.
	 */
	@Override
	public void lights()
	{	
		// Do nothing.
	}
	
	/** Would set a point light source for this graphics context but ignores it in this case as this will
	 *  be handled by the parent sketch.
	 */
	@Override
	public void pointLight(float v1, float v2, float v3, float x, float y, float z)
	{	
		// Do nothing.
	}
	
	/** Would set a ambient light source for this graphics context but ignores it in this case as this will
	 *  be handled by the parent sketch.
	 */
	@Override
	public void ambientLight(float v1, float v2, float v3)
	{	
		// Do nothing.
	}
	
	/** Would set a ambient light source for this graphics context but ignores it in this case as this will
	 *  be handled by the parent sketch.
	 */
	@Override
	public void ambientLight(float v1, float v2, float v3, float x, float y, float z)
	{	
		// Do nothing.
	}
	
	/** Would set a directional light source for this graphics context but ignores it in this case as this will
	 *  be handled by the parent sketch.
	 */
	@Override
	public void directionalLight(float v1, float v2, float v3, float nx, float ny, float nz)
	{	
		// Do nothing.
	}
	
	/** Would set a spotlight source for this graphics context but ignores it in this case as this will
	 *  be handled by the parent sketch.
	 */
	@Override
	public void spotLight(float v1, float v2, float v3, float x, float y, float z, float nx, float ny, float nz, float angle, float concentration)
	{	
		// Do nothing.
	}
	
	
	/** Would set a light falloff for point, spot and ambient light sources for this graphics context but ignores 
	 *  it in this case as this will be handled by the parent sketch.
	 */
	@Override
	public void lightFalloff(float constant, float linear, float quadratic)
	{	
		// Do nothing.
	}
	
	/** Would set a specular colour for light sources in this graphics context but ignores 
	 *  it in this case as this will be handled by the parent sketch.
	 */
	@Override
	public void lightSpecular(float v1, float v2, float v3)
	{	
		// Do nothing.
	}
	
}
