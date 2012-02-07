package org.gicentre.tests.handy;

import org.gicentre.handy.HandyRenderer;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PGraphicsJava2D;
import processing.core.PImage;
import processing.core.PMatrix;
import processing.core.PMatrix2D;
import processing.core.PMatrix3D;
import processing.core.PShape;
import processing.core.PStyle;

//  ****************************************************************************************
/** Wrapper around a HandyRenderer that allows Handy drawing to be turned on or off inside
 *  a sketch without having to change any of the sketch's drawing commands. To use, put the
 *  drawing code between calls to <code>startHandy()</code> and <code>stopHandy()</code> in
 *  a sketch.
 *  
 *  Alternative implementation
 *  
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

public class HandyDraw2 extends PGraphics{

	// -------------------------------- Object Variables ---------------------------------  
	
	private HandyRenderer handyRenderer;
	private PGraphics canvas;//canvas it is drawing on (usually Papplet.g)
	private boolean active; //is active
	
	// ----------------------------------- Constructor -----------------------------------
	
	public HandyDraw2(PApplet sketch){
		this.parent=sketch;
		this.handyRenderer=new HandyRenderer(sketch);
	}
	
	// ------------------------------------- Methods ------------------------------------- 
	
	/** Turns on handy rendering. This should be paired with a call to <code>stopHandy()</code>
	 *  once sketchy rendering is complete.
	 */
	public void startHandy(){
		if (active)
			System.err.println("Handy already started - did you stopHandy()?");
		else{
			canvas=this.parent.g; //get whatever the sketch is drawing onto
			handyRenderer.setGraphics(canvas); //set the handy renderer to this
			this.parent.g=this; //set the sketch to "this" so we can intercept all drawing commands
			active=true;
		}
	}
	

	/** Turns off handy rendering. This should be paired with a call to <code>startHandy()</code>
	 *  before any drawing that needs to be in a sketchy style.
	 */
	public void stopHandy(){
		if (active){
			this.parent.g=canvas;
			handyRenderer.setGraphics(parent.g);//reset to sketch's g
			active=false;
		}
		else{
			System.err.println("Handy already stopped - did you startHandy()?");
		}
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
		handyRenderer.line(x1, y1, x2, y2);
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
		handyRenderer.ellipse(x, y, w, h);
	}

	
	/** Draws a quadrilateral shape. Similar to a rectangle but angles not constrained to 90 degrees.
	 *  Coordinates can proceed in either a clockwise or anti-clockwise direction.
	 *  @param x1 x coordinate of the first quadrilateral vertex.
	 *  @param y1 y coordinate of the first quadrilateral vertex.
	 *  @param x2 x coordinate of the second quadrilateral vertex.
	 *  @param y2 y coordinate of the second quadrilateral vertex.
	 *  @param x3 x coordinate of the third quadrilateral vertex.
	 *  @param y3 y coordinate of the third quadrilateral vertex.
	 *  @param x4 x coordinate of the fourth quadrilateral vertex.
	 *  @param y4 y coordinate of the fourth quadrilateral vertex.
	 */
	@Override
	public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4)
	{
		handyRenderer.quad(x1, y1, x2, y2, x3, y3, x4, y4);
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
		handyRenderer.rect(x, y, w, h);
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
		handyRenderer.triangle(x1,y1,x2,y2,x3,y3);
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
		handyRenderer.beginShape(mode);
	}

	
	@Override
	/** Adds a vertex to a shape that was started with a call to <code>beginShape()</code> 
	 *  or one of its variants.
	 *  @param x x coordinate of vertex to add.
	 *  @param y y coordinate of vertex to add.
	 */
	public void vertex(float x, float y){
		handyRenderer.vertex(x,y);
	}
	
	
	/** Ends a shape definition. This should have been paired with a call to 
	 *  <code>beginShape()</code> or one of its variants. Note that this version
	 *  will not close the shape if the last vertex does not match the first one.
	 */
	@Override
	public void endShape(){
		handyRenderer.endShape();
	}
		
	
	/** Ends a shape definition. This should have been paired with a call to 
	 *  <code>beginShape()</code> or one of its variants. If the mode parameter
	 *  is <code>CLOSE</code> the shape will be closed.
	 */
	@Override
	public void endShape(int mode) 
	{
		handyRenderer.endShape(mode);
	}

	@Override
	public void ambient(float x, float y, float z) {
		canvas.ambient(x, y, z);
	}

	@Override
	public void ambient(float gray) {
		canvas.ambient(gray);
	}

	@Override
	public void ambient(int rgb) {
		canvas.ambient(rgb);
	}

	@Override
	public void ambientLight(float red, float green, float blue, float x,
			float y, float z) {
		canvas.ambientLight(red, green, blue, x, y, z);
	}

	@Override
	public void ambientLight(float red, float green, float blue) {
		canvas.ambientLight(red, green, blue);
	}

	@Override
	public void applyMatrix(float n00, float n01, float n02, float n03,
			float n10, float n11, float n12, float n13, float n20, float n21,
			float n22, float n23, float n30, float n31, float n32, float n33) {
		canvas.applyMatrix(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22, n23,
				n30, n31, n32, n33);
	}

	@Override
	public void applyMatrix(float n00, float n01, float n02, float n10,
			float n11, float n12) {
		canvas.applyMatrix(n00, n01, n02, n10, n11, n12);
	}

	@Override
	public void applyMatrix(PMatrix source) {
		canvas.applyMatrix(source);
	}

	@Override
	public void applyMatrix(PMatrix2D source) {
		canvas.applyMatrix(source);
	}

	@Override
	public void applyMatrix(PMatrix3D source) {
		canvas.applyMatrix(source);
	}

	@Override
	public void arc(float a, float b, float c, float d, float start, float stop) {
		handyRenderer.arc(a, b, c, d, start, stop);
	}

	@Override
	public void autoNormal(boolean auto) {
		canvas.autoNormal(auto);
	}

	@Override
	public void background(float x, float y, float z, float a) {
		canvas.background(x, y, z, a);
	}

	@Override
	public void background(float x, float y, float z) {
		canvas.background(x, y, z);
	}

	@Override
	public void background(float gray, float alpha) {
		canvas.background(gray, alpha);
	}

	@Override
	public void background(float gray) {
		canvas.background(gray);
	}

	@Override
	public void background(int rgb, float alpha) {
		canvas.background(rgb, alpha);
	}

	@Override
	public void background(int rgb) {
		canvas.background(rgb);
	}

	@Override
	public void background(PImage image) {
		canvas.background(image);
	}

	@Override
	public void beginCamera() {
		canvas.beginCamera();
	}

	@Override
	public void beginDraw() {
		canvas.beginDraw();
	}

	@Override
	public void beginRaw(PGraphics rawGraphics) {
		canvas.beginRaw(rawGraphics);
	}

	@Override
	public PShape beginRecord() {
		return canvas.beginRecord();
	}

	@Override
	public void beginText() {
		canvas.beginText();
	}

	@Override
	public void bezier(float x1, float y1, float z1, float x2, float y2,
			float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
		canvas.bezier(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
	}

	@Override
	public void bezier(float x1, float y1, float x2, float y2, float x3,
			float y3, float x4, float y4) {
		canvas.bezier(x1, y1, x2, y2, x3, y3, x4, y4);
	}

	@Override
	public void bezierDetail(int detail) {
		canvas.bezierDetail(detail);
	}

	@Override
	public float bezierPoint(float a, float b, float c, float d, float t) {
		return canvas.bezierPoint(a, b, c, d, t);
	}

	@Override
	public float bezierTangent(float a, float b, float c, float d, float t) {
		return canvas.bezierTangent(a, b, c, d, t);
	}

	@Override
	public void bezierVertex(float x2, float y2, float z2, float x3, float y3,
			float z3, float x4, float y4, float z4) {
		canvas.bezierVertex(x2, y2, z2, x3, y3, z3, x4, y4, z4);
	}

	@Override
	public void bezierVertex(float x2, float y2, float x3, float y3, float x4,
			float y4) {
		canvas.bezierVertex(x2, y2, x3, y3, x4, y4);
	}

	@Override
	public void box(float w, float h, float d) {
		canvas.box(w, h, d);
	}

	@Override
	public void box(float size) {
		canvas.box(size);
	}

	@Override
	public void breakShape() {
		handyRenderer.breakShape();
	}

	@Override
	public void camera() {
		canvas.camera();
	}

	@Override
	public void camera(float eyeX, float eyeY, float eyeZ, float centerX,
			float centerY, float centerZ, float upX, float upY, float upZ) {
		canvas.camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
	}

	@Override
	public boolean canDraw() {
		return canvas.canDraw();
	}

	@Override
	public void colorMode(int mode, float maxX, float maxY, float maxZ,
			float maxA) {
		canvas.colorMode(mode, maxX, maxY, maxZ, maxA);
	}

	@Override
	public void colorMode(int mode, float maxX, float maxY, float maxZ) {
		canvas.colorMode(mode, maxX, maxY, maxZ);
	}

	@Override
	public void colorMode(int mode, float max) {
		canvas.colorMode(mode, max);
	}

	@Override
	public void colorMode(int mode) {
		canvas.colorMode(mode);
	}

	@Override
	public void curve(float x1, float y1, float z1, float x2, float y2,
			float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
		canvas.curve(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
	}

	@Override
	public void curve(float x1, float y1, float x2, float y2, float x3,
			float y3, float x4, float y4) {
		canvas.curve(x1, y1, x2, y2, x3, y3, x4, y4);
	}

	@Override
	public void curveDetail(int detail) {
		canvas.curveDetail(detail);
	}

	@Override
	public float curvePoint(float a, float b, float c, float d, float t) {
		return canvas.curvePoint(a, b, c, d, t);
	}

	@Override
	public float curveTangent(float a, float b, float c, float d, float t) {
		return canvas.curveTangent(a, b, c, d, t);
	}

	@Override
	public void curveTightness(float tightness) {
		canvas.curveTightness(tightness);
	}

	@Override
	public void curveVertex(float x, float y, float z) {
		canvas.curveVertex(x, y, z);
	}

	@Override
	public void curveVertex(float x, float y) {
		handyRenderer.curveVertex(x, y);
	}


	@Override
	public void directionalLight(float red, float green, float blue, float nx,
			float ny, float nz) {
		canvas.directionalLight(red, green, blue, nx, ny, nz);
	}

	@Override
	public boolean displayable() {
		return canvas.displayable();
	}

	@Override
	public void dispose() {
		canvas.dispose();
	}

	@Override
	public void edge(boolean edge) {
		canvas.edge(edge);
	}

	@Override
	public void ellipseMode(int mode) {
		canvas.ellipseMode(mode);
	}

	@Override
	public void emissive(float x, float y, float z) {
		canvas.emissive(x, y, z);
	}

	@Override
	public void emissive(float gray) {
		canvas.emissive(gray);
	}

	@Override
	public void emissive(int rgb) {
		canvas.emissive(rgb);
	}

	@Override
	public void endCamera() {
		canvas.endCamera();
	}

	@Override
	public void endDraw() {
		canvas.endDraw();
	}

	@Override
	public void endRaw() {
		canvas.endRaw();
	}

	@Override
	public void endRecord() {
		canvas.endRecord();
	}

	@Override
	public void endText() {
		canvas.endText();
	}

	@Override
	public void fill(float x, float y, float z, float a) {
		canvas.fill(x, y, z, a);
	}

	@Override
	public void fill(float x, float y, float z) {
		canvas.fill(x, y, z);
	}

	@Override
	public void fill(float gray, float alpha) {
		canvas.fill(gray, alpha);
	}

	@Override
	public void fill(float gray) {
		canvas.fill(gray);
	}

	@Override
	public void fill(int rgb, float alpha) {
		canvas.fill(rgb, alpha);
	}

	@Override
	public void fill(int rgb) {
		canvas.fill(rgb);
	}

	@Override
	public void flush() {
		canvas.flush();
	}

	@Override
	public void frustum(float left, float right, float bottom, float top,
			float near, float far) {
		canvas.frustum(left, right, bottom, top, near, far);
	}

	@Override
	public PMatrix getMatrix() {
		return canvas.getMatrix();
	}

	@Override
	public PMatrix2D getMatrix(PMatrix2D target) {
		return canvas.getMatrix(target);
	}

	@Override
	public PMatrix3D getMatrix(PMatrix3D target) {
		return canvas.getMatrix(target);
	}

	@Override
	public PStyle getStyle() {
		return canvas.getStyle();
	}

	@Override
	public PStyle getStyle(PStyle s) {
		return canvas.getStyle(s);
	}

	@Override
	public void hint(int which) {
		canvas.hint(which);
	}

	@Override
	public boolean hintEnabled(int which) {
		return canvas.hintEnabled(which);
	}

	@Override
	public void image(PImage image, float a, float b, float c, float d, int u1,
			int v1, int u2, int v2) {
		canvas.image(image, a, b, c, d, u1, v1, u2, v2);
	}

	@Override
	public void image(PImage image, float x, float y, float c, float d) {
		canvas.image(image, x, y, c, d);
	}

	@Override
	public void image(PImage image, float x, float y) {
		canvas.image(image, x, y);
	}

	@Override
	public void imageMode(int mode) {
		canvas.imageMode(mode);
	}

	@Override
	public boolean is2D() {
		return canvas.is2D();
	}

	@Override
	public boolean is3D() {
		return canvas.is3D();
	}

	@Override
	public boolean isRecording() {
		return canvas.isRecording();
	}

	@Override
	public int lerpColor(int c1, int c2, float amt) {
		return canvas.lerpColor(c1, c2, amt);
	}

	@Override
	public void lightFalloff(float constant, float linear, float quadratic) {
		canvas.lightFalloff(constant, linear, quadratic);
	}

	@Override
	public void lightSpecular(float x, float y, float z) {
		canvas.lightSpecular(x, y, z);
	}

	@Override
	public void lights() {
		canvas.lights();
	}

	@Override
	public void line(float x1, float y1, float z1, float x2, float y2, float z2) {
		canvas.line(x1, y1, z1, x2, y2, z2);
	}

	@Override
	public void matrixMode(int mode) {
		canvas.matrixMode(mode);
	}

	@Override
	public void mergeShapes(boolean val) {
		canvas.mergeShapes(val);
	}

	@Override
	public float modelX(float x, float y, float z) {
		return canvas.modelX(x, y, z);
	}

	@Override
	public float modelY(float x, float y, float z) {
		return canvas.modelY(x, y, z);
	}

	@Override
	public float modelZ(float x, float y, float z) {
		return canvas.modelZ(x, y, z);
	}

	@Override
	public void noFill() {
		canvas.noFill();
	}

	@Override
	public void noLights() {
		canvas.noLights();
	}

	@Override
	public void noSmooth() {
		canvas.noSmooth();
	}

	@Override
	public void noStroke() {
		canvas.noStroke();
	}

	@Override
	public void noTexture() {
		canvas.noTexture();
	}

	@Override
	public void noTint() {
		canvas.noTint();
	}

	@Override
	public void normal(float nx, float ny, float nz) {
		canvas.normal(nx, ny, nz);
	}

	@Override
	public void ortho() {
		canvas.ortho();
	}

	@Override
	public void ortho(float left, float right, float bottom, float top,
			float near, float far) {
		canvas.ortho(left, right, bottom, top, near, far);
	}

	@Override
	public void ortho(float left, float right, float bottom, float top) {
		canvas.ortho(left, right, bottom, top);
	}

	@Override
	public void perspective() {
		canvas.perspective();
	}

	@Override
	public void perspective(float fovy, float aspect, float zNear, float zFar) {
		canvas.perspective(fovy, aspect, zNear, zFar);
	}

	@Override
	public void point(float x, float y, float z) {
		canvas.point(x, y, z);
	}

	@Override
	public void point(float x, float y) {
		canvas.point(x, y);
	}

	@Override
	public void pointLight(float red, float green, float blue, float x,
			float y, float z) {
		canvas.pointLight(red, green, blue, x, y, z);
	}

	@Override
	public void popMatrix() {
		canvas.popMatrix();
	}

	@Override
	public void popStyle() {
		canvas.popStyle();
	}

	@Override
	public void printCamera() {
		canvas.printCamera();
	}

	@Override
	public void printMatrix() {
		canvas.printMatrix();
	}

	@Override
	public void printProjection() {
		canvas.printProjection();
	}

	@Override
	public void pushMatrix() {
		canvas.pushMatrix();
	}

	@Override
	public void pushStyle() {
		canvas.pushStyle();
	}

	@Override
	public void quadVertex(float cx, float cy, float cz, float x3, float y3,
			float z3) {
		canvas.quadVertex(cx, cy, cz, x3, y3, z3);
	}

	@Override
	public void quadVertex(float cx, float cy, float x3, float y3) {
		canvas.quadVertex(cx, cy, x3, y3);
	}

	@Override
	public void rect(float a, float b, float c, float d, float tl, float tr,
			float bl, float br) {
		canvas.rect(a, b, c, d, tl, tr, bl, br);
	}

	@Override
	public void rect(float a, float b, float c, float d, float hr, float vr) {
		canvas.rect(a, b, c, d, hr, vr);
	}

	@Override
	public void rectMode(int mode) {
		canvas.rectMode(mode);
	}

	@Override
	public void resetMatrix() {
		canvas.resetMatrix();
	}

	@Override
	public void rotate(float angle, float vx, float vy, float vz) {
		canvas.rotate(angle, vx, vy, vz);
	}

	@Override
	public void rotate(float angle) {
		canvas.rotate(angle);
	}

	@Override
	public void rotateX(float angle) {
		canvas.rotateX(angle);
	}

	@Override
	public void rotateY(float angle) {
		canvas.rotateY(angle);
	}

	@Override
	public void rotateZ(float angle) {
		canvas.rotateZ(angle);
	}

	@Override
	public void scale(float x, float y, float z) {
		canvas.scale(x, y, z);
	}

	@Override
	public void scale(float sx, float sy) {
		canvas.scale(sx, sy);
	}

	@Override
	public void scale(float s) {
		canvas.scale(s);
	}

	@Override
	public void screenBlend(int mode) {
		canvas.screenBlend(mode);
	}

	@Override
	public float screenX(float x, float y, float z) {
		return canvas.screenX(x, y, z);
	}

	@Override
	public float screenX(float x, float y) {
		return canvas.screenX(x, y);
	}

	@Override
	public float screenY(float x, float y, float z) {
		return canvas.screenY(x, y, z);
	}

	@Override
	public float screenY(float x, float y) {
		return canvas.screenY(x, y);
	}

	@Override
	public float screenZ(float x, float y, float z) {
		return canvas.screenZ(x, y, z);
	}

	@Override
	public void setMatrix(PMatrix source) {
		canvas.setMatrix(source);
	}

	@Override
	public void setMatrix(PMatrix2D source) {
		canvas.setMatrix(source);
	}

	@Override
	public void setMatrix(PMatrix3D source) {
		canvas.setMatrix(source);
	}

	@Override
	public void setParent(PApplet parent) {
		canvas.setParent(parent);
	}

	@Override
	public void setPath(String path) {
		canvas.setPath(path);
	}

	@Override
	public void setPrimary(boolean primary) {
		canvas.setPrimary(primary);
	}

	@Override
	public void setSize(int w, int h) {
		canvas.setSize(w, h);
	}

	@Override
	public void shape(PShape shape, float x, float y, float c, float d) {
		canvas.shape(shape, x, y, c, d);
	}

	@Override
	public void shape(PShape shape, float x, float y) {
		canvas.shape(shape, x, y);
	}

	@Override
	public void shape(PShape shape) {
		canvas.shape(shape);
	}

	@Override
	public void shapeMode(int mode) {
		canvas.shapeMode(mode);
	}

	@Override
	public void shapeName(String name) {
		canvas.shapeName(name);
	}

	@Override
	public void shearX(float angle) {
		canvas.shearX(angle);
	}

	@Override
	public void shearY(float angle) {
		canvas.shearY(angle);
	}

	@Override
	public void shininess(float shine) {
		canvas.shininess(shine);
	}

	@Override
	public void smooth() {
		canvas.smooth();
	}

	@Override
	public void specular(float x, float y, float z) {
		canvas.specular(x, y, z);
	}

	@Override
	public void specular(float gray) {
		canvas.specular(gray);
	}

	@Override
	public void specular(int rgb) {
		canvas.specular(rgb);
	}

	@Override
	public void sphere(float r) {
		canvas.sphere(r);
	}

	@Override
	public void sphereDetail(int ures, int vres) {
		canvas.sphereDetail(ures, vres);
	}

	@Override
	public void sphereDetail(int res) {
		canvas.sphereDetail(res);
	}

	@Override
	public void spotLight(float red, float green, float blue, float x, float y,
			float z, float nx, float ny, float nz, float angle,
			float concentration) {
		canvas.spotLight(red, green, blue, x, y, z, nx, ny, nz, angle, concentration);
	}

	@Override
	public void stroke(float x, float y, float z, float a) {
		canvas.stroke(x, y, z, a);
	}

	@Override
	public void stroke(float x, float y, float z) {
		canvas.stroke(x, y, z);
	}

	@Override
	public void stroke(float gray, float alpha) {
		canvas.stroke(gray, alpha);
	}

	@Override
	public void stroke(float gray) {
		canvas.stroke(gray);
	}

	@Override
	public void stroke(int rgb, float alpha) {
		canvas.stroke(rgb, alpha);
	}

	@Override
	public void stroke(int rgb) {
		canvas.stroke(rgb);
	}

	@Override
	public void strokeCap(int cap) {
		canvas.strokeCap(cap);
	}

	@Override
	public void strokeJoin(int join) {
		canvas.strokeJoin(join);
	}

	@Override
	public void strokeWeight(float weight) {
		canvas.strokeWeight(weight);
	}

	@Override
	public void style(PStyle s) {
		canvas.style(s);
	}

	@Override
	public void text(char c, float x, float y, float z) {
		canvas.text(c, x, y, z);
	}

	@Override
	public void text(char c, float x, float y) {
		canvas.text(c, x, y);
	}

	@Override
	public void text(char c) {
		canvas.text(c);
	}

	@Override
	public void text(char[] chars, int start, int stop, float x, float y,
			float z) {
		canvas.text(chars, start, stop, x, y, z);
	}

	@Override
	public void text(char[] chars, int start, int stop, float x, float y) {
		canvas.text(chars, start, stop, x, y);
	}

	@Override
	public void text(float num, float x, float y, float z) {
		canvas.text(num, x, y, z);
	}

	@Override
	public void text(float num, float x, float y) {
		canvas.text(num, x, y);
	}

	@Override
	public void text(int num, float x, float y, float z) {
		canvas.text(num, x, y, z);
	}

	@Override
	public void text(int num, float x, float y) {
		canvas.text(num, x, y);
	}

	@Override
	public void text(String s, float x1, float y1, float x2, float y2, float z) {
		canvas.text(s, x1, y1, x2, y2, z);
	}

	@Override
	public void text(String str, float x1, float y1, float x2, float y2) {
		canvas.text(str, x1, y1, x2, y2);
	}

	@Override
	public void text(String str, float x, float y, float z) {
		canvas.text(str, x, y, z);
	}

	@Override
	public void text(String str, float x, float y) {
		canvas.text(str, x, y);
	}

	@Override
	public void text(String str) {
		canvas.text(str);
	}

	@Override
	public void textAlign(int alignX, int alignY) {
		canvas.textAlign(alignX, alignY);
	}

	@Override
	public void textAlign(int align) {
		canvas.textAlign(align);
	}

	@Override
	public float textAscent() {
		return canvas.textAscent();
	}


	@Override
	public float textDescent() {
		return canvas.textDescent();
	}

	@Override
	public void textFont(PFont which, float size) {
		canvas.textFont(which, size);
	}

	@Override
	public void textFont(PFont which) {
		canvas.textFont(which);
	}

	@Override
	public void textLeading(float leading) {
		canvas.textLeading(leading);
	}

	@Override
	public void textMode(int mode) {
		canvas.textMode(mode);
	}

	@Override
	public void textSize(float size) {
		canvas.textSize(size);
	}

	@Override
	public float textWidth(char c) {
		return canvas.textWidth(c);
	}

	@Override
	public float textWidth(char[] chars, int start, int length) {
		return canvas.textWidth(chars, start, length);
	}

	@Override
	public float textWidth(String str) {
		return canvas.textWidth(str);
	}

	@Override
	public void texture(PImage... images) {
		canvas.texture(images);
	}

	@Override
	public void texture(PImage image) {
		canvas.texture(image);
	}

	@Override
	public void textureBlend(int mode) {
		canvas.textureBlend(mode);
	}

	@Override
	public void textureMode(int mode) {
		canvas.textureMode(mode);
	}

	@Override
	public void tint(float x, float y, float z, float a) {
		canvas.tint(x, y, z, a);
	}

	@Override
	public void tint(float x, float y, float z) {
		canvas.tint(x, y, z);
	}

	@Override
	public void tint(float gray, float alpha) {
		canvas.tint(gray, alpha);
	}

	@Override
	public void tint(float gray) {
		canvas.tint(gray);
	}

	@Override
	public void tint(int rgb, float alpha) {
		canvas.tint(rgb, alpha);
	}

	@Override
	public void tint(int rgb) {
		canvas.tint(rgb);
	}

	@Override
	public void translate(float tx, float ty, float tz) {
		canvas.translate(tx, ty, tz);
	}

	@Override
	public void translate(float tx, float ty) {
		canvas.translate(tx, ty);
	}

	@Override
	public void vertex(float x, float y, float z, float u, float v) {
		canvas.vertex(x, y, z, u, v);
	}

	@Override
	public void vertex(float x, float y, float u, float v) {
		canvas.vertex(x, y, u, v);
	}

	@Override
	public void vertex(float x, float y, float z) {
		canvas.vertex(x, y, z);
	}

	@Override
	public void vertex(float... values) {
		canvas.vertex(values);
	}

	@Override
	public void vertexFields(float[] v) {
		canvas.vertexFields(v);
	}

}
