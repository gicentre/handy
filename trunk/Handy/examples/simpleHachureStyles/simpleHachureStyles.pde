import org.gicentre.handy.*;

HandyRenderer h;

void setup()
{
  size(300,200);
  smooth();
  h = new HandyRenderer(this);
  fill(206,76,52);
  h.setHachurePerturbationAngle(15);
}

void draw()
{
  background(234,215,182);
  h.setRoughness(1);

  h.setFillGap(0.5);
  h.setFillWeight(0.1);
  h.rect(50,30,80,50);

  h.setFillGap(3);
  h.setFillWeight(2);
  h.rect(170,30,80,50);

  h.setFillGap(5);
  h.setIsAlternating(true);
  h.rect(50,120,80,50);

  h.setRoughness(3); 
  h.setFillWeight(1);
  h.setIsAlternating(false);
  h.rect(170,120,80,50);
}
