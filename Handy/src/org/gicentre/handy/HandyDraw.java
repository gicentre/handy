package org.gicentre.handy;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PGraphics2D;

//  ****************************************************************************************
/** Wrapper around a HandyRenderer that allows Handy drawing to be turned on or off inside
 *  a sketch without having to change any of the sketch's drawing commands. To use, put the
 *  drawing code between calls to <code>startHandy()</code> and <code>stopHandy()</code> in
 *  a sketch.
 *  @author Aidan Slingsby and Jo Wood, giCentre, City University London.
 *  @version 1.0, 28th January, 2012.
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

public class HandyDraw extends PGraphics2D{

	// -------------------------------- Object Variables ---------------------------------  
	
	private HandyRenderer handyRenderer;
	private PApplet sketch;
	private PGraphics prevG; //The sketch's g when startHandy() is called 
	
	private boolean useSuper=false;
	
	// ----------------------------------- Constructor -----------------------------------
	
	public HandyDraw(PApplet sketch){
		this.sketch=sketch;
		this.handyRenderer=new HandyRenderer(sketch);
		this.handyRenderer.setGraphics(this);	
		this.setSize(sketch.width, sketch.height);
	}
	
	// ------------------------------------- Methods ------------------------------------- 
	
	/** Turns on handy rendering. This should be paired with a call to <code>stopHandy()</code>
	 *  once sketchy rendering is complete.
	 */
	public void startHandy(){
		prevG=this.sketch.g;
		this.sketch.g=this;
		beginDraw();
		this.style(prevG.getStyle());			// Set the style of this to that of the sketch
		if (prevG.smooth) {
			this.smooth();
		}
		else{
			this.noSmooth();
		}
		if (sketch.width!=this.width || sketch.height!=this.height) {
			this.setSize(sketch.width, sketch.height);
		}

		background(255,0);  						// Erase previous drawing to screen buffer and set to transparent
	}
	

	/** Turns off handy rendering. This should be paired with a call to <code>startHandy()</code>
	 *  before any drawing that needs to be in a sketchy style.
	 */
	public void stopHandy(){
		endDraw();
		this.prevG.style(this.getStyle());	// Set the style of the sketch to that of this
		this.sketch.g=prevG;
		
		this.sketch.pushStyle();
		this.sketch.noTint(); 					// Temporarily remove tint while we draw handy screen buffer.
		this.sketch.image(this.get(),0,0);
		this.sketch.popStyle();
	}
	
	// ----------------------- Overridden Processing Draw Methods ------------------------- 
	
	/** Draws a 2D line between the given coordinate pairs. 
	 *  @param x1 x coordinate of the start of the line.
	 *  @param y1 y coordinate of the start of the line.
	 *  @param x2 x coordinate of the end of the line.
	 *  @param y2 y coordinate of the end of the line.
	 */
	@Override
	public void line(float x1, float y1, float x2, float y2){
		if (useSuper){
			super.line(x1, y1, x2, y2);
		}
		else{
			useSuper=true;
			handyRenderer.line(x1, y1, x2, y2);
			useSuper=false;
		}
	}
	
	/** Draws an ellipse using the given location and dimensions. By default the x,y coordinates
	 *  will be centre of the ellipse, but the meanings of these parameters can be changed with
	 *  Processing's <code>ellipseMode()</code> command.
	 *  @param x x coordinate of the ellipse's position
	 *  @param y y coordinate of the ellipse's position.
	 *  @param w Width of the ellipse (but see modifications possible with ellipseMode())
	 *  @param h Height of the ellipse (but see modifications possible with ellipseMode())
	 */
	@Override
	public void ellipse(float x, float y,float w, float h){
		if (useSuper){
			super.ellipse(x,y,w,h);
		}
		else{
			useSuper=true;
			handyRenderer.ellipse(x, y, w, h);
			useSuper=false;
		}
	}

	/** Draws a rectangle using the given location and dimensions. By default the x,y coordinates
	 *  will be the top left of the rectangle, but the meanings of these parameters can be 
	 *  changed with Processing's <code>rectMode()</code> command.
	 *  @param x x coordinate of the rectangle position
	 *  @param y y coordinate of the rectangle position.
	 *  @param w Width of the rectangle (but see modifications possible with rectMode())
	 *  @param h Height of the rectangle (but see modifications possible with rectMode())
	 */
	@Override
	public void rect(float x, float y,float w, float h){
		if (useSuper){
			super.rect(x,y,w,h);
		}
		else{
			useSuper=true;
			handyRenderer.rect(x, y, w, h);
			useSuper=false;
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
	public void triangle(float x1, float y1,float x2, float y2,float x3, float y3){
		if (useSuper){
			super.triangle(x1,y1,x2,y2,x3,y3);
		}
		else{
			useSuper=true;
			handyRenderer.triangle(x1,y1,x2,y2,x3,y3);
			useSuper=false;
		}
	}

	/** Starts a new shape of type <code>POLYGON</code>. This must be paired with a call to 
	 * <code>endShape()</code> or one of its variants.
	 */
	@Override	
	public void beginShape(){
		this.beginShape(POLYGON);
	}

	/** Starts a new shape of the type specified in the mode parameter. This must be paired
	 * with a call to <code>endShape()</code> or one of its variants.
	 *	@param mode either POINTS, LINES, TRIANGLES, TRIANGLE_FAN, TRIANGLE_STRIP, QUADS, QUAD_STRIP	 
	 */
	@Override	
	public void beginShape(int mode){
		if (useSuper){
			super.beginShape(mode);
		}
		else{
			useSuper=true;
			handyRenderer.beginShape(mode);
			useSuper=false;
		}
	}

	
	@Override
	/** Adds a vertex to a shape that was started with a call to <code>beginShape()</code> 
	 *  or one of its variants.
	 *  @param x x coordinate of vertex to add.
	 *  @param y y coordinate of vertex to add.
	 */
	public void vertex(float x, float y){
		if (useSuper){
			super.vertex(x,y);
		}
		else{
			useSuper=true;
			handyRenderer.vertex(x,y);
			useSuper=false;
		}
	}
	
	
	/** Ends a shape definition. This should have been paired with a call to 
	 *  <code>beginShape()</code> or one of its variants. Note that this version
	 *  will not close the shape if the last vertex does not match the first one.
	 */
	@Override
	public void endShape(){
		if (useSuper){
			super.endShape();
		}
		else{
			useSuper=true;
			handyRenderer.endShape();
			useSuper=false;
		}
	}
		
	
	/** Ends a shape definition. This should have been paired with a call to 
	 *  <code>beginShape()</code> or one of its variants. If the mode parameter
	 *  is <code>CLOSE</code> the shape will be closed.
	 */
	@Override
	public void endShape(int mode) 
	{
		if (useSuper){
			super.endShape(mode);
		}
		else{
			useSuper=true;
			handyRenderer.endShape(mode);
			useSuper=false;
		}
	}
}
