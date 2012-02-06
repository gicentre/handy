import org.gicentre.handy.*;

HandyRenderer h1,h2,h3,h4;

void setup()
{
  size(610,200);
  smooth();
  noLoop();
  h1 = HandyPresets.createPencil(this);
  h2 = HandyPresets.createColouredPencil(this);
  h3 = HandyPresets.createWaterAndInk(this);
  h4 = HandyPresets.createMarker(this);
}

void draw()
{
  background(234,215,182);
  for (int i=0; i<5; i++)
  {
    fill(206+random(-30,30),76+random(-30,30),52+random(-30,30),160);
    h1.rect(random(10,200),random(10,50),80,50);
    h2.rect(random(310,520),random(10,50),80,50);
    h3.rect(random(10,200),random(100,140),80,50);
    h4.rect(random(310,520),random(100,140),80,50);  
  }
}


