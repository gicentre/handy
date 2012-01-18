package org.gicentre.handy;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PGraphics2D;

/**Acts as a Wrapper around HandyRenderer to make Handy drawing work like
 * other Processing drawing functions.
 * 
 * To use, put the drawing code between calls to startHandy() and stopHandy(). 
 * 
 * 
 * @author sbbb717
 *
 */
public class HandyDraw extends PGraphics2D{

	HandyRenderer handyRenderer;
	PApplet sketch;
	PGraphics sketchG;//keep track of the Sketch's original canvas
	
	public HandyDraw(PApplet sketch){
		this.sketch=sketch;
		this.sketchG=sketch.g;
		this.handyRenderer=new HandyRenderer(sketch);
		this.handyRenderer.setGraphics(this);
		this.setSize(sketch.width, sketch.height);
	}
	
	public void startHandy(){
		this.sketch.g=this;
		beginDraw();
		this.style(sketchG.getStyle()); //set the style of this to that of the sketch
		if (sketch.g.smooth)
			this.smooth();
		if (sketch.width!=this.width || sketch.height!=this.height)
			this.setSize(sketch.width, sketch.height);
		background(0,0);  //Erase previous drawing and set to transparent
	}
	
	public void stopHandy(){
		endDraw();
		this.sketchG.style(this.getStyle());//set the style of the sketch to that of this
		this.sketch.g=sketchG;
		
		this.sketch.pushStyle();
		this.sketch.noTint(); //temporarily remove tint
		this.sketch.image(this.get(),0,0);
		this.sketch.popStyle();
	}
	
	public void line(float x1, float y1, float x2, float y2){
		handyRenderer.line(x1, y1, x2, y2);
	}
	
	//this method crashes
//	public void ellipse(float x, float y,float w, float h){
//		handyRenderer.ellipse(x, y, w, h);
//	}

	//...but this one doesn't!
	public void rect(float x, float y,float w, float h){
		handyRenderer.ellipse(x, y, w, h);
	}

}
