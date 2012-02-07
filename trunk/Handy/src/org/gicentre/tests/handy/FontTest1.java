package org.gicentre.tests.handy;

import processing.core.PApplet;

/** Experiments with sketchy text
 * 
 * Approach here is to define the strokes (curved and straight line segments) that
 * make up alphanumeric characters letters, and draw them with handy.
 * 
 * At the time of writing, HandyRender.arc() not implemented yet, but I think the
 * approach looks promising (not not implemented this way, of course!)
 * 
 * @author Aidan Slingsby
 *
 */
public class FontTest1 extends PApplet{

	HandyDraw2 handyDraw;
	boolean handy=true;
	public void setup(){
		smooth();
		handyDraw=new HandyDraw2(this);
	}
	
	public void draw(){
		
		if (handy)
			handyDraw.startHandy();
		
		background(255);
		
		scale(0.6f);
		//give us an 'H'
		translate(30,30);
		line(0,0,0,30);
		line(0,15,20,15);
		line(20,0,20,30);

		//give us an 'e'
		translate(30,0);
		noFill();
		line(0,20,20,20);
		arc(10,20,20,20,PI/3f,PI*2);
		
		//give us an 'l'
		translate(30,0);
		line(0,0,0,30);

		//give us an 'l'
		translate(10,0);
		line(0,0,0,30);
		
		//give us on 'o'
		ellipse(20,20,20,20);

		
		if (handy)
			handyDraw.stopHandy();
		
		noLoop();
	}
	
	public void mouseMoved(){
		loop();
	}
	
	public void keyPressed(){
		if (key=='h')
			handy=!handy;
		loop();
	}
	
	
	
}
