package org.gicentre.tests;

import org.gicentre.handy.HandyRenderer;
import org.gicentre.utils.colour.ColourTable;
import org.gicentre.utils.gui.DrawableFactory;
import org.gicentre.utils.stat.BarChart;
import org.gicentre.utils.stat.XYChart;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

//  ****************************************************************************************
/** Tests chart drawing in a simple Processing sketch.  'H' to toggle sketchy rendering. 
 *  Arrows to change bar gap. 'X' and 'Y' for toggling axes; 'T' for transposing axes.
 *  'L' for toggling log scaling.
 *  @author Jo Wood, giCentre, City University London.
 *  @version 2.0, 2nd April, 2016. 
 */ 
// *****************************************************************************************

/* This file is part of giCentre utilities library. gicentre.utils is free software: you can 
 * redistribute it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * gicentre.utils is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this
 * source code (see COPYING.LESSER included with this source code). If not, see 
 * http://www.gnu.org/licenses/.
 */

public class ChartTest extends PApplet
{
	// ------------------------------ Starter method ------------------------------- 

	/** Creates a simple application to test the chart drawing utilities.
	 *  @param args Command line arguments (ignored). 
	 */
	public static void main(String[] args)
	{   
		PApplet.main(new String[] {"org.gicentre.tests.ChartTest"});
	}

	// ----------------------------- Object variables ------------------------------

	private HandyRenderer h;		// Does the sketchy rendering.
	private boolean isHandy;		// Toggles handy rendering on and off.
	
	private PFont sketchyFont,normalFont;
	
	private BarChart chart1;
	private XYChart chart2;

	private boolean showXAxis, showYAxis;
	private boolean transpose;
	private boolean useLog;

	private int barGap = 2;
	private int barPad = 0;

	// ---------------------------- Processing methods -----------------------------

	/** Initial window settings prior to setup().
	 */
	public void settings()
	{   
		size(1200,500);

		// Should work with all Processing 3 renderers.
		//size(1200, P2D);
		//size(1200,500, P3D);
		//size(1200,500, FX2D);

		pixelDensity(displayDensity());		// Use platform's maximum display density.
	}

	/** Sets up the chart and fonts.
	 */
	public void setup()
	{   
		sketchyFont = loadFont("HumorSans-18.vlw");
		normalFont = createFont("sansSerif",14);
	
		textFont(isHandy ? sketchyFont:normalFont);
		
		isHandy = true;
		showXAxis = true;
		showYAxis = true;
		transpose = false;
		useLog    = false;

		// Handy appearance.
		h = new HandyRenderer(this);
		h.setFillGap(1.5f);

		// Data to show in charts.
		float[] barData = new float[] {0.21235f, 0.5f, 1,     2,     4,     8,     16,    32,    64,    128,   256,   512,   1024, 2048};
		float[] xData = new float[]{2,4,6,8,12,14,15,16,23};
		float[] yData = new float[]{6.0f,7.2f,5.8f,4.3f,2.1f,3.5f,6.8f,6.2f,5.8f};
		float[] sizeData = new float[]{1,10,4,20,13,6,2,8,6};

		// Bar chart.
		chart1 = new BarChart(this);
		chart1.setRenderer(DrawableFactory.createHandyRenderer(h));
		chart1.setData(barData);
		chart1.showValueAxis(showYAxis);
		chart1.setValueFormat("###,###.####");
		chart1.showCategoryAxis(showXAxis);
		chart1.transposeAxes(transpose);
		chart1.setBarColour(barData,ColourTable.getPresetColourTable(ColourTable.GREENS, -1000, 1000));
		chart1.setBarLabels(new String[] {"Item 1","Item 2","Item 3","Item 4","Item 5","Item 6","Item 7","Item 8","Item 9","Item 10","Item 11","Item 12","Item 13", "Item 14"});
		chart1.setBarGap(barGap);

		// Line chart.
		chart2 = new XYChart(this);
		chart2.setShowEdge(true);
		chart2.setRenderer(DrawableFactory.createHandyRenderer(h)); 
		chart2.setData(xData, yData);
		chart2.setMinY(0);
		chart2.setMaxY(10);
		chart2.showXAxis(showXAxis);
		chart2.showYAxis(showYAxis);
		chart2.setXFormat("###,###.###");
		chart2.setPointSize(sizeData, 32);
		chart2.setLineWidth(5f);
		chart2.setXAxisLabel("This is the x-axis");
		chart2.setYAxisLabel("This is the y-axis");
		chart2.setAxisColour(color(255));
		chart2.setAxisValuesColour(color(128,100,100));
		chart2.setAxisLabelColour(color(255,200,200));
		chart2.setPointColour(xData, ColourTable.getPresetColourTable(ColourTable.REDS,0,23));
		chart2.setLineColour(color(200,100,100));
	}

	/** Draws two charts.
	 */
	public void draw()
	{   
		background(255);
		textFont(isHandy ? sketchyFont:normalFont);
		
        strokeWeight(1);
        textSize(10);
        chart1.draw(1,1,width*.5f-2,height-2);
        
        fill(0);
        textSize(18);
        rect(width*.5f,0,width*.5f,height);
        chart2.draw(width*.5f+1,1,width*.5f-2,height-2);

		noLoop();
	}

	/** Responds to key presses to control appearance of the charts.
	 */
	@Override
	public void keyPressed()
	{
		if ((key =='h') || (key == 'H'))
		{
			isHandy = !isHandy;
			h.setIsHandy(isHandy);
			loop();
		}
		else if ((key == 'l') || (key == 'L'))
		{
			useLog = !useLog;
			chart1.setLogValues(useLog);
			chart2.setLogX(useLog);
			chart2.setLogY(useLog);
			loop();
		}
		if ((key == 't') || (key == 'T'))
		{
			transpose = !transpose;
			chart1.transposeAxes(transpose);
			loop();
		}
		if ((key == 'x') || (key == 'X'))
		{
			showXAxis = !showXAxis;
			chart1.showCategoryAxis(showXAxis);
			chart2.showXAxis(showXAxis);
			loop();
		}
		else if ((key == 'y') || (key == 'Y'))
		{
			showYAxis = !showYAxis;
			chart1.showValueAxis(showYAxis);
			chart2.showYAxis(showYAxis);
			loop();
		}

		if (key == CODED)
		{
			if (keyCode == PConstants.LEFT)
			{
				if (barGap > 0)
				{
					barGap--;
					chart1.setBarGap(barGap);
					loop();
				}
			}
			else if (keyCode == PConstants.RIGHT)
			{
				if (barGap < width)
				{
					barGap++;
					chart1.setBarGap(barGap);
					loop();
				}
			}
			else if (keyCode == PConstants.DOWN)
			{
				if (barPad > 0)
				{
					barPad--;
					chart1.setBarPadding(barPad);
					loop();
				}
			}
			else if (keyCode == PConstants.UP)
			{
				if (barPad < width)
				{
					barPad++;
					chart1.setBarPadding(barPad);
					loop();
				}
			}
		}
	}
}