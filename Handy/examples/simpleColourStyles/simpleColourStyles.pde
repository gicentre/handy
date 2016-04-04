import org.gicentre.handy.*;

// Displays 4 sktechy rectangles with different fill styles.
// Version 2.0, 4th April, 2016
// Author Jo Wood

HandyRenderer h;

void setup()
{
  size(300,200);
  h = new HandyRenderer(this);
  h.setOverrideFillColour(true);
  h.setOverrideStrokeColour(true);
}

void draw()
{
  background(247,230,197);

  h.setBackgroundColour(color(255));
  h.setFillColour(color(206,76,52));
  h.setStrokeColour(color(0));
  h.rect(50,30,80,50);
  
  h.setBackgroundColour(color(255,130));
  h.rect(170,30,80,50);
  
  h.setBackgroundColour(color(0,0)); 
  h.setStrokeColour(color(206,76,52)); 
  h.rect(50,120,80,50);
  
  h.setBackgroundColour(color(206,76,52)); 
  h.setFillColour(color(19,39,28));
  h.setStrokeColour(color(200,70,48));
  h.rect(170,120,80,50);  
  
  noLoop();     // No need to redraw.
}