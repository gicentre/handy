package org.gicentre.handy.test;
import org.gicentre.handy.HandyDraw;

import processing.core.PApplet;

/** Test class for HandyDraw
 * 
 * @author sbbb717
 *
 */
public class HandyDrawTest extends PApplet {

	HandyDraw handyCanvas;
	
	public void setup(){
		handyCanvas=new HandyDraw(this);
	}
	
	public void draw(){
		background(255);
		handyCanvas.startHandy();
		line(10, 10, 200, 200);
		fill(200,100,100);
		ellipse(100,100,20,20);
		rect(150,150,30,30);
		handyCanvas.stopHandy();
		

	}
	
}
