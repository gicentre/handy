package org.gicentre.handy;

import java.util.ArrayList;
import processing.core.PVector;

//*****************************************************************************************
/** Performs Douglas-Peucker simplification on linear coordinate collections.
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

public class Simplifier 
{
	private static ArrayList<Float> xCoords,yCoords; 	// Used for storing simplified coordinates.
	private static float[] xOriginal,yOriginal;			// Original coordinates.
	private static float[] xSimp,ySimp;					// Simplified coordinates.
	private static float tolerance;
	

	/** Creates a simplified version of the given collection of coordinates. Uses Douglas-Peucker simplification
	 *  using the given tolerance value. The greater the tolerance, the greater the simplification. 
	 *  @param origCoords Coordinates to be simplified.
	 *  @param tol Douglas-Peucker tolerance (in spatial units).
	 */
	public static void simplify(ArrayList<PVector>origCoords, float tol)
	{       
		Simplifier.tolerance = tol;
		int numCoords = origCoords.size();
		xOriginal = new float[numCoords];
		yOriginal = new float[numCoords];
		for (int i=0; i<origCoords.size(); i++)
		{
			PVector p = origCoords.get(i);
			xOriginal[i] = p.x;
			yOriginal[i] = p.y;
		}

		xCoords = new ArrayList<Float>();
		yCoords = new ArrayList<Float>();

		douglasPeucker(0,numCoords-1);
		
		xSimp = new float[xCoords.size()];
		ySimp = new float[yCoords.size()];
		
		for (int i=0; i<xCoords.size(); i++)
		{
			xSimp[i] = xCoords.get(i).floatValue();
			ySimp[i] = yCoords.get(i).floatValue();
		}
	}
		
	/** Provides the simplified x coordinates. This should only be called after simplify().
	 *  @return x coordinates of simplified line.
	 */
	public static float[] getSimplifiedX()
	{
		return xSimp;
	}
	
	/** Provides the simplified y coordinates. This should only be called after simplify().
	 *  @return y coordinates of simplified line.
	 */
	public static float[] getSimplifiedY()
	{
		return ySimp;
	}
	
	

	/** Recursive algorithm that simplifies the geometry contained in object variables
	 *  x[] and y[]. Stores results in object variables xCoords and yCoords.
	 *  @param start Index of first point in line to examine.
	 *  @param end Index of last point in line to examine.
	 */
	private static void douglasPeucker(int start, int end)
	{        
		if (end-start < 2)  // Adjacent points
		{
			if ((xCoords.size() == 0) ||
					((xCoords.get(xCoords.size()-1)).floatValue() != xOriginal[start]) ||
					((yCoords.get(yCoords.size()-1)).floatValue() != yOriginal[start]))
			{
				xCoords.add(new Float(xOriginal[start]));
				yCoords.add(new Float(yOriginal[start]));
			}

			if ((xCoords.get(xCoords.size()-1).floatValue() != xOriginal[end]) ||
					((yCoords.get(yCoords.size()-1).floatValue() != yOriginal[end])))
			{
				xCoords.add(new Float(xOriginal[end]));
				yCoords.add(new Float(yOriginal[end]));
			}

			return;
		}

		Segment seg = new Segment(xOriginal[start],yOriginal[start],xOriginal[end],yOriginal[end]);
		float maxDist = 0;
		int furthestNode = 0;
		for (int i=start+1; i<end; i++)
		{
			float dist = seg.calcDistance(xOriginal[i],yOriginal[i]);
			//System.out.println("Dist from "+seg+" is "+dist);

			if (dist > maxDist)
			{
				maxDist = dist;
				furthestNode = i;
			}
		}

		if (maxDist > tolerance)
		{
			douglasPeucker(start,furthestNode);
			douglasPeucker(furthestNode,end);
		}
		else
		{       
			Float lastXCoord = null;
			Float lastYCoord = null;
			
			if (xCoords.size() > 0)
			{
				lastXCoord = xCoords.get(xCoords.size()-1);
				lastYCoord = yCoords.get(xCoords.size()-1);
			}
			
			if ((xCoords.size() == 0) ||
				(((lastXCoord != null) && (lastYCoord != null)) &&
				 ((lastXCoord.floatValue() != xOriginal[start]) ||
				 (lastYCoord.floatValue() != yOriginal[start]))))
			{
				xCoords.add(new Float(xOriginal[start]));
				yCoords.add(new Float(yOriginal[start]));
			}

			lastXCoord = xCoords.get(xCoords.size()-1);
			lastYCoord = yCoords.get(xCoords.size()-1);
			
			if (((lastXCoord != null) && (lastYCoord != null)) &&
				((lastXCoord.floatValue() != xOriginal[end]) ||
				(lastYCoord.floatValue() != yOriginal[end])))
			{
				xCoords.add(new Float(xOriginal[end]));
				yCoords.add(new Float(yOriginal[end]));
			}
		}
	}
}
