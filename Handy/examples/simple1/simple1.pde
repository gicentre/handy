import org.gicentre.handy.*;    // For Handy rendering.

// Displays a simple rectangle in a hand-drawn style.
// Version 2.0, 4th April, 2016
// Author Jo Wood

HandyRenderer h;      // This does all the hard work of rendering.

// Set up the sketch and renderer.
void setup()
{
  size(400,250); 
  h = new HandyRenderer(this);    // Creates the renderer.
}

// Draw a rectangle in a sketchy style
void draw()
{
  background(247,230,197);
  h.rect(width/4,height/4,width/2,height/2);
  
  noLoop();  // No need to redraw.
}