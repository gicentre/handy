package org.gicentre.handy;

//*****************************************************************************************
/** Provides a set of line coordinates that progress across a rectangular area at a given
 *  angle.
 *  @author Jo Wood, giCentre, City University London.
 *  @version 1.0, 3rd January, 2012.
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
class HachureIterator 
{
	// -------------------------------- Object Variables ---------------------------------
	
	private float sinAngle,tanAngle;
	private float top,bottom,left, right;
	private float gap;
	private float pos;
	private float deltaX, hGap;
	private Segment sLeft,sRight;
	
	// ---------------------------------- Constructor ------------------------------------
	
	/** Creates an iterator that will provide a sequence of lines that fill the rectangular region provided.
	 *  @param top y-coordinate of top of rectangle.
	 *  @param bottom y-coordinate of bottom of rectangle.
	 *  @param left x-coordinate of left of rectangle.
	 *  @param right x-coordinate of right of rectangle.
	 *  @param gap Gap in pixel units between adjacent lines.
	 *  @param sinAngle Sine of the angle of the lines.
	 *  @param cosAngle Cosine of the angle of the lines.
	 *  @param tanAngle Tangent of the angle of the lines.
	 */
	HachureIterator(float top, float bottom, float left, float right, float gap, float sinAngle, float cosAngle, float tanAngle)
	{
		this.top      = top;
		this.bottom   = bottom;
		this.left     = left;
		this.right    = right;
		this.gap      = gap;
		this.sinAngle = sinAngle;
		this.tanAngle = tanAngle;
		
		if (Math.abs(sinAngle) < 0.0001)
		{
			// Special case 1: Vertical lines
			pos = left+gap;
		}
		else if (Math.abs(sinAngle) > 0.9999)
		{
			// Special case 2: Horizontal lines
			pos = top+gap;
		}
		else
		{
			deltaX = (bottom-top)*Math.abs(tanAngle);
			pos = left-Math.abs(deltaX);
			hGap   = Math.abs(gap /cosAngle);
			sLeft = new Segment(left,bottom,left,top);
			sRight = new Segment(right,bottom,right,top);
		}		
	}
	
	/** Reports the next line that fits within the rectangle.
	 *  @return Coordinates of the line (x1,y1,x2,y2) or null if no more lines to find.
	 */
	float[] getNextLine()
	{
		if (Math.abs(sinAngle) < 0.0001)
		{
			// Special case 1: Vertical hachuring
			if (pos < right)
			{
				float[] line = new float[] {pos,top,pos,bottom};
				pos += gap;
				return line;
			}
		}
		else if (Math.abs(sinAngle) > 0.9999)
		{
			// Special case 2: Horizontal hachuring
			if (pos<bottom)
			{

				float[] line = new float[] {left,pos,right,pos};
				pos += gap;
				return line;
			}
		}
		else
		{
			float xLower = pos-deltaX/2;
			float xUpper = pos+deltaX/2;
			float yLower = bottom;
			float yUpper = top;
		
			if (pos < right+deltaX)
			{
				while (((xLower < left) && (xUpper < left)) ||
						((xLower > right) && (xUpper > right)))
				{
					pos += hGap;
					xLower = pos-deltaX/2;
					xUpper = pos+deltaX/2;
					
					if (pos > right+deltaX)
					{
						return null;
					}
				}
				
				Segment s = new Segment(xLower,yLower,xUpper,yUpper);
			
				if (s.compare(sLeft) == Segment.Relation.INTERSECTS)
				{
					xLower = s.getIntersectionX();
					yLower = s.getIntersectionY();
				}
				if (s.compare(sRight) == Segment.Relation.INTERSECTS)
				{
					xUpper = s.getIntersectionX();
					yUpper = s.getIntersectionY();
				}
				if (tanAngle > 0)
				{
					xLower = right-(xLower-left);
					xUpper = right-(xUpper-left);
				}
				float[] line = new float[] {xLower,yLower,xUpper,yUpper};
				pos += hGap;
				return line;
			}
		}
		
		// If we get to this point, we must have finished all hachures
		return null;
	}
}
