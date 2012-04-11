package org.gicentre.handy;

// *****************************************************************************************
/** Stores version information about the Handy sketchy drawing package.
 *  @author Jo Wood, giCentre, City University London.
 *  @version 1.1, 11th April, 2012.
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

public class Version 
{
	private static final float  VERSION = 1.1f;
	private static final String VERSION_TEXT = "Handy sketchy drawing package V1.1, 11th April, 2012";

	/** Reports the current version of the handy package.
	 *  @return Text describing the current version of this package.
	 */
	public static String getText()
	{
		return VERSION_TEXT;
	}

	/** Reports the numeric version of the handy package.
	 *  @return Number representing the current version of this package.
	 */
	public static float getVersion()
	{
		return VERSION;
	}
}