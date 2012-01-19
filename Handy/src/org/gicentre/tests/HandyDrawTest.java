package org.gicentre.tests;
import org.gicentre.handy.HandyDraw;

import processing.core.PApplet;

/** Test class for HandyDraw
 * 
 * @author sbbb717
 *
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

		if (useHandy)
			handyDraw.startHandy();
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
		if (useHandy)
			handyDraw.stopHandy();


	}

	public void keyPressed(){
		//toggle use handy
		if (key=='h')
			useHandy=!useHandy;
	}

}
