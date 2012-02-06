import org.gicentre.handy.*;    // For Handy rendering.

// Displays a simple rectangle in a hand-drawn style.
// Version 1.0, 24th January, 2012
// Author Jo Wood

HandyRenderer h;      // This does all the hard work of rendering.

// Set up the sketch and renderer.
void setup()
{
  size(400,250); 
  smooth();
  h = new HandyRenderer(this);    // Creates the renderer.
}

// Draw a rectangle in a sketchy style
void draw()
{
  background(255,251,204);
  h.rect(width/4,height/4,width/2,height/2);
  
  noLoop();  // No need to redraw.
}
