package org.gicentre.handy;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PGraphics2D;
import processing.core.PVector;

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
	
	boolean isDrawingShape=false;
	List<PVector> vertices=new ArrayList<PVector>();
	
	boolean useSuper=false;
	
	public HandyDraw(PApplet sketch){
		this.sketch=sketch;
		this.sketchG=sketch.g;
		this.handyRenderer=new HandyRenderer(sketch);
		this.handyRenderer.setGraphics((PGraphics2D)this);
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
		
		//clear any incomplete shapes
		isDrawingShape=false;
		vertices.clear();
	}
	
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

	
//	public void triangle(float x1, float y1,float x2, float y2,float x3, float y3){
//		//a bit horrible, but will call the superclass' method if called by HandyRenderer
//		//to prevent infinite recursion
//		for(StackTraceElement stackTraceElement:Thread.currentThread().getStackTrace()){
//			if (stackTraceElement.getClassName().equals(HandyRenderer.class.getName())){
//				super.triangle(x1,y1,x2,y2,x3,y3);
//				return;
//			}
//		}
//		//otherwise, call HandyRenderer's method
//		handyRenderer.triangle(x1,y1,x2,y2,x3,y3);
//	}
	
	public void beginShape(){
		if (useSuper){
			super.beginShape();
		}
		else{
			useSuper=true;
			isDrawingShape=true;
			useSuper=false;
		}
	}
	
	public void vertex(float x, float y){
		if (useSuper){
			super.vertex(x,y);
		}
		else{
			useSuper=true;
			vertices.add(new PVector(x,y));
			useSuper=false;
		}
	}

	public void endShape(){
		if (useSuper){
			super.endShape();
		}
		else{
			useSuper=true;
			float[] xs=new float[vertices.size()];
			float[] ys=new float[vertices.size()];
			int i=0;
			for (PVector pVector:vertices){
				xs[i]=pVector.x;
				ys[i]=pVector.y;
				i++;
			}
			handyRenderer.shape(xs,ys);
			isDrawingShape=false;
			vertices.clear();
			useSuper=false;
		}
	}

}
