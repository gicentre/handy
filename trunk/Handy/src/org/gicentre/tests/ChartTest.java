package org.gicentre.tests;

import org.gicentre.handy.HandyRenderer;
import org.gicentre.utils.colour.ColourTable;
import org.gicentre.utils.gui.DrawableFactory;
import org.gicentre.utils.move.ZoomPan;
import org.gicentre.utils.stat.BarChart;
import org.gicentre.utils.stat.XYChart;

import processing.core.PApplet;
import processing.core.PConstants;

//  ****************************************************************************************
/** Tests chart drawing in a simple Processing sketch. 
 *  @author Jo Wood, giCentre, City University London.
 *  @version 3.2, 1st August, 2011. 
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

@SuppressWarnings("serial")
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

    private BarChart chart1;
    private XYChart chart2;
    
    private boolean showXAxis, showYAxis;
    private boolean transpose;
    private boolean useLog;
    
    private int barGap=2;
    private int barPad = 0;
    
    private ZoomPan zoomer;
    
    // ---------------------------- Processing methods -----------------------------

    /** Sets up the chart and fonts.
     */
    public void setup()
    {   
        size(640,350);
        smooth(); 
        textFont(createFont("Helvetica",10));
        textSize(10);
        zoomer = new ZoomPan(this);
        
        showXAxis = true;
        showYAxis = true;
        transpose = true;
        useLog    = false;
        
        HandyRenderer renderer = new HandyRenderer(this);
        renderer.setFillGap(0.5f);
                
        //float[] barData = new float[] {0.358f,0.370f,0.342f,0.348f,0.334f,0.349f,0.317f,0.331f,0.313f,0.300f,0.303f,0.312f};
        //float[] barData = new float[] {0.26f, 0.27f, 0.28f, 0.29f, 0.30f, 0.31f, 0.32f, 0.33f, 0.34f, 0.35f, 0.36f, 0.37f};
        float[] barData = new float[] {0.21235f, 0.5f, 1,     2,     4,     8,     16,    32,    64,    128,   256,   512,   1024, 2048};
        //float[] barData = new float[] {1,     10,     100,     1000,     10000,    100000,    1000000,    10000000};
        
        chart1 = new BarChart(this);
        chart1.setRenderer(DrawableFactory.createHandyRenderer(renderer));
        chart1.setData(barData);
        //chart1.setMinValue(0.2f);
        //chart1.setMaxValue(10000f);
        chart1.showValueAxis(showYAxis);
        chart1.setValueFormat("###,###.####");
        chart1.showCategoryAxis(showXAxis);
        chart1.transposeAxes(transpose);
        //chart1.setBarColour(color(240,50,50));
        chart1.setBarColour(barData,ColourTable.getPresetColourTable(ColourTable.GREENS, -1000, 1000));
        chart1.setBarLabels(new String[] {"Item 1","Item 2","Item 3","Item 4","Item 5","Item 6","Item 7","Item 8","Item 9","Item 10","Item 11","Item 12","Item 13", "Item 14"});
        chart1.setBarGap(barGap);
        chart1.setShowEdge(true);
 
        

        chart2 = new XYChart(this);
        chart2.setShowEdge(true);
        chart2.setRenderer(DrawableFactory.createHandyRenderer(renderer));
        
        float[] xData = new float[]{2,4,6,8,12,14,15,16,23};
        float[] yData = new float[]{6.0f,7.2f,5.8f,4.3f,2.1f,3.5f,6.8f,6.2f,5.8f};
        float[] sizeData = new float[]{1,10,4,20,13,6,2,8,6};
        chart2.setData(xData, yData);
        chart2.showXAxis(showXAxis);
        chart2.showYAxis(showYAxis);
        chart2.setMinX(0);
        chart2.setMinY(0);
        chart2.setXFormat("###,###.###");
        chart2.setPointSize(10);
        chart2.setYFormat("0.0");

        chart2.setPointColour(xData, ColourTable.getPresetColourTable(ColourTable.BLUES,0,23));
        chart2.setPointSize(sizeData, 16);
        chart2.setLineWidth(1);
        chart2.setXAxisLabel("This is the x-axis");
        chart2.setYAxisLabel("This is the y-axis");
    }

    /** Draws some charts.
     */
    public void draw()
    {   
        background(255);
        zoomer.transform();
       
        strokeWeight(1);
        
        stroke(250,100,100);
        rect(1,1,width*.5f-2,height-2);
        stroke(0,120);
        chart1.draw(1,1,width*.5f-2,height-2);
        
        stroke(100,100,250);
        rect(width*.5f+1,1,width*.5f-2,height-2);
        stroke(0,80);
        chart2.draw(width*.5f+1,1,width*.5f-2,height-2);
        
        noLoop();
    }
    
    public void keyPressed()
    {
        if (key == 'l')
        {
            useLog = !useLog;
            chart1.setLogValues(useLog);
            chart2.setLogX(useLog);
            chart2.setLogY(useLog);
            loop();
        }
        if (key == 't')
        {
            transpose = !transpose;
            chart1.transposeAxes(transpose);
            loop();
        }
        if (key == 'x')
        {
            showXAxis = !showXAxis;
            chart1.showCategoryAxis(showXAxis);
            chart2.showXAxis(showXAxis);
            loop();
        }
        else if (key == 'y')
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
    
    @Override
    public void mouseDragged()
    {
    	loop();
    }
}