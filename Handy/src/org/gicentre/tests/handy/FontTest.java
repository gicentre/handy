package org.gicentre.tests.handy;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.geom.PathIterator;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphicsJava2D;

/**Experimenting with sketchy text. Using Java's built-in functions to extract
 * the shape of text, and then draw with the sketchy renderer
 * 
 * Would probably work best if the font is quite thin and it's filled in solid
 * colour, but there are some problems with rendering polygons with islands
 * with handy renderer 
 * 
 * @author Aidan Slingsby
 *
 */
public class FontTest extends PApplet{

	HandyDraw2 handyDraw;
	FontRenderContext fontRenderContext;
	PFont f;
	
	public void setup(){
		size(800,800);
		smooth();
		
		handyDraw=new HandyDraw2(this);
		
		f=createFont("Helvetica", 20);
		
		//Get a FontRenderContext
		Graphics2D g2d=null;
		if (g instanceof PGraphicsJava2D) {
			g2d=((PGraphicsJava2D)g).g2;
			fontRenderContext=g2d.getFontRenderContext();
		}
		else {
			System.err.println("Doesn't work with this renderer");
		}
	}
	
	public void draw(){
		background(255);

		fill(255,0,0);
		handyDraw.startHandy();
		drawText("Hello world", 10, 300, f);
		handyDraw.stopHandy();
	}
	
	
	private void drawText(String text, float x, float y, PFont f){
		
		Shape shape=f.getFont().createGlyphVector(fontRenderContext, text).getOutline(x, y);
		stroke(0);
		fill(0);
		beginShape(POLYGON);
		PathIterator pi = shape.getPathIterator(null,5);//5 pixels
		float[] firstPointSeg=null;
		while(!pi.isDone()){
			float coords[]=new float[6];
			int type=pi.currentSegment(coords);
			if (type==PathIterator.SEG_MOVETO){
				breakShape();
				firstPointSeg=coords;
				vertex(coords[0],coords[1]);
			}
			else if (type==PathIterator.SEG_CLOSE){
				vertex(firstPointSeg[0],firstPointSeg[1]);
			}
			else {
				vertex(coords[0],coords[1]);
			}

			pi.next();
		}
		endShape(CLOSE);
		
	}
}
