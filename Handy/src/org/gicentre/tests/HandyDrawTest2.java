package org.gicentre.tests;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import org.gicentre.tests.handy.HandyDraw2;
import org.gicentre.utils.move.ZoomPan;

import processing.core.PApplet;

//****************************************************************************************
/** Test class for HandyDraw.
 *  @author Aidan Slingsby, giCentre, City University London.
 *  @version 1.0, 31st January, 2012.
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
public class HandyDrawTest2 extends PApplet implements MouseWheelListener{

	HandyDraw2 handyDraw;
	boolean useHandy=true;
	
	ZoomPan zoomer;

	public void setup(){
		this.addMouseWheelListener(this);
		size(400,500);
		handyDraw=new HandyDraw2(this);
		smooth();
		zoomer = new ZoomPan(this);
	}

	public void draw(){
		
		background(255);
		zoomer.transform();
		

		fill(200,100,100); //styles are honoured by handyDraw even if set outside start/stopHandy
		stroke(0,0,200);
		if (useHandy){
			handyDraw.startHandy();
		}
		
		//background(255);
		
		
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
		endShape(CLOSE);
		
		
		//examples from http://processing.org/reference/beginShape_.html
		
		pushMatrix();
		
		translate(0,200);
		beginShape();
		vertex(30, 20);
		vertex(85, 20);
		vertex(85, 75);
		vertex(30, 75);
		endShape(CLOSE);

		translate(100,0);
		beginShape(POINTS);
		vertex(30, 20);
		vertex(85, 20);
		vertex(85, 75);
		vertex(30, 75);
		endShape();

		translate(100,0);
		beginShape(LINES);
		vertex(30, 20);
		vertex(85, 20);
		vertex(85, 75);
		vertex(30, 75);
		endShape();

		translate(100,0);
		noFill();
		beginShape();
		vertex(30, 20);
		vertex(85, 20);
		vertex(85, 75);
		vertex(30, 75);
		endShape();

		translate(-300,100);
		noFill();
		beginShape();
		vertex(30, 20);
		vertex(85, 20);
		vertex(85, 75);
		vertex(30, 75);
		endShape(CLOSE);

		translate(100,0);
		fill(200,100,100);
		beginShape(TRIANGLES);
		vertex(30, 75);
		vertex(40, 20);
		vertex(50, 75);
		vertex(60, 20);
		vertex(70, 75);
		vertex(80, 20);
		endShape();

		translate(100,0);
		beginShape(TRIANGLE_STRIP);
		vertex(30, 75);
		vertex(40, 20);
		vertex(50, 75);
		vertex(60, 20);
		vertex(70, 75);
		vertex(80, 20);
		vertex(90, 75);
		endShape();

		translate(100,0);
		beginShape(TRIANGLE_FAN);
		vertex(57.5f, 50);
		vertex(57.5f, 15); 
		vertex(92, 50); 
		vertex(57.5f, 85); 
		vertex(22, 50); 
		vertex(57.5f, 15); 
		endShape();

		translate(-300,100);
		beginShape(QUADS);
		vertex(30, 20);
		vertex(30, 75);
		vertex(50, 75);
		vertex(50, 20);
		vertex(65, 20);
		vertex(65, 75);
		vertex(85, 75);
		vertex(85, 20);
		endShape();

		translate(100,0);
		beginShape(QUAD_STRIP); 
		vertex(30, 20); 
		vertex(30, 75); 
		vertex(50, 20);
		vertex(50, 75);
		vertex(65, 20); 
		vertex(65, 75); 
		vertex(85, 20);
		vertex(85, 75); 
		endShape();

		translate(100,0);
		beginShape();
		vertex(20, 20);
		vertex(40, 20);
		vertex(40, 40);
		vertex(60, 40);
		vertex(60, 60);
		vertex(20, 60);
		endShape(CLOSE);

		popMatrix();
			
		ellipse(zoomer.getMouseCoord().x,zoomer.getMouseCoord().y,40,40);

		if (useHandy) {
			handyDraw.stopHandy();
		}
		noLoop();
	}

	public void mouseMoved(){
		loop();
	}
	
	public void mouseDragged(){
		loop();
	}
	
	public void keyPressed(){
		//toggle use handy
		if (key=='h')
			useHandy=!useHandy;
		if (key=='s')
			if (g.smooth)
				noSmooth();
			else
				smooth();
				
		loop();
	}

	/** Responds to a mouse wheel movement by ensuring the screen is updated
	 *  with new zoom level.
	 *  @param e Mouse wheel movement event.
	 */
	public void mouseWheelMoved(MouseWheelEvent e) {
		loop();
	}

}
