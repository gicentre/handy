package org.gicentre.handy;

// *****************************************************************************************
/** Defines the minimum functionality for a renderer to draw graphic primitives. 
 *  @author Jo Wood, giCentre, City University London.
 *  @version 1.0, 15th January, 2012.
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

public interface Drawable 
{
	/** Should draw a 2D point at the given coordinates. 
	 *  @param x x coordinate of the point.
	 *  @param y y coordinate of the point.
	 */
	public abstract void point(float x, float y);
	
	/** Should draw a 2D line between the given coordinate pairs. 
	 *  @param x1 x coordinate of the start of the line.
	 *  @param y1 y coordinate of the start of the line.
	 *  @param x2 x coordinate of the end of the line.
	 *  @param y2 y coordinate of the end of the line.
	 */
	public abstract void line(float x1, float y1, float x2, float y2);
	
	/** Should draw a rectangle using the given location and dimensions. By default the x,y coordinates
	 *  will be the top left of the rectangle, but the meanings of these parameters should be able to 
	 *  be changed with Processing's rectMode() command.
	 *  @param x x coordinate of the rectangle position
	 *  @param y y coordinate of the rectangle position.
	 *  @param w Width of the rectangle (but see modifications possible with rectMode())
	 *  @param h Height of the rectangle (but see modifications possible with rectMode())
	 */
	public abstract void rect(float x, float y, float w, float h);
	
	/** Should draw an ellipse using the given location and dimensions. By default the x,y coordinates
	 *  will be centre of the ellipse, but the meanings of these parameters should be able to be 
	 *  changed with Processing's ellipseMode() command.
	 *  @param x x coordinate of the ellipse's position
	 *  @param y y coordinate of the ellipse's position.
	 *  @param w Width of the ellipse (but see modifications possible with ellipseMode())
	 *  @param h Height of the ellipse (but see modifications possible with ellipseMode())
	 */
	public abstract void ellipse(float x, float y, float w, float h);
	
	/** Should draw a triangle through the three pairs of coordinates.
	 *  @param x1 x coordinate of the first triangle vertex.
	 *  @param y1 y coordinate of the first triangle vertex.
	 *  @param x2 x coordinate of the second triangle vertex.
	 *  @param y2 y coordinate of the second triangle vertex.
	 *  @param x3 x coordinate of the third triangle vertex.
	 *  @param y3 y coordinate of the third triangle vertex.
	 */
	public abstract void triangle(float x1, float y1, float x2, float y2, float x3, float y3);
	
	/** Should draw a complex line that links the given coordinates. 
	 *  @param xCoords x coordinates of the line.
	 *  @param yCoords y coordinates of the line.
	 */
	public abstract void polyLine(float[] xCoords, float[] yCoords);
	
	/** Should draw a closed polygon shape based on the given arrays of vertices.
	 *  @param xCoords x coordinates of the shape.
	 *  @param yCoords y coordinates of the shape.
	 */
	public abstract void shape(float[] xCoords, float[] yCoords);
}
