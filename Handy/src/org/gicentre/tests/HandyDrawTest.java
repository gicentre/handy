package org.gicentre.tests;
import org.gicentre.handy.HandyDraw;

import processing.core.PApplet;

//****************************************************************************************
/** Test class for HandyDraw.
 *  @author Aidan Slingsby, giCentre, City University London.
 *  @version 1.0, 19th January, 2012.
 */ 
//  ****************************************************************************************

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

@SuppressWarnings("serial")
public class HandyDrawTest extends PApplet {

	HandyDraw handyDraw;
	boolean useHandy=true;

	public void setup(){
		handyDraw=new HandyDraw(this);
		smooth();
		size(240,240);
	}

	public void draw(){
		background(255);
		
		fill(200,100,100); //styles are honoured by handyDraw even if set outside start/stopHandy

		if (useHandy){
			handyDraw.startHandy();
		}
		
		line(10, 10, 200, 200);
		ellipse(100,100,20,20);
		rect(150,150,30,30);
		triangle(40, 20, 20, 40, 60, 30);
		beginShape();
		vertex(180,30);
		vertex(200,30);
		vertex(200,10);
		vertex(210,10);
		vertex(210,30);
		vertex(230,30);
		vertex(230,40);
		vertex(210,40);
		vertex(210,60);
		vertex(200,60);
		vertex(200,40);
		vertex(180,40);
		endShape();
		
		ellipse(mouseX,mouseY,40,40);
		
		if (useHandy) {
			handyDraw.stopHandy();
		}
	}

	public void keyPressed(){
		//toggle use handy
		if (key=='h')
			useHandy=!useHandy;
	}

}
