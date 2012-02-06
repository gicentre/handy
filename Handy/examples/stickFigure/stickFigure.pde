import org.gicentre.handy.*;        // For hand-drawn rendering.

// Creates a simple stick figure using sketchy graphics
// Version 1.0, 6th December, 2011.
// Author Jo Wood, giCentre, City University London.

// --------------------- Sketch-wide variables ----------------------

HandyRenderer h;      // This does the sketchy drawing.

ArrayList<PVector> joints;
int movingJoint;      // Index of the joint currently being moved by mouse.

// ------------------------ Initialisation --------------------------

void setup()
{
  size(600,400);
  smooth();
  strokeWeight(3);
  noFill();
  h = new HandyRenderer(this);    // Initialise the handy renderer.

  movingJoint = -1;
  
  joints = new ArrayList<PVector>();
  joints.add(new PVector(width/2+10,height/2-20));   // head
  joints.add(new PVector(width/2,height/2));        // Neck
  joints.add(new PVector(width/2,height/2+50));     // Pelvis
  joints.add(new PVector(width/2+2,height/2+90));   // Left knee
  joints.add(new PVector(width/2+20,height/2+90));  // Right knee
  joints.add(new PVector(width/2-20,height/2+130)); // Left ankle
  joints.add(new PVector(width/2+20,height/2+130)); // Right ankle
  joints.add(new PVector(width/2-10,height/2+130)); // Left toe
  joints.add(new PVector(width/2+30,height/2+130)); // Right toe
  joints.add(new PVector(width/2-20,height/2+35));  // Left elbow
  joints.add(new PVector(width/2+10,height/2+40));  // Right elbow
  joints.add(new PVector(width/2-15,height/2+70));  // Left wrist
  joints.add(new PVector(width/2+40,height/2+70));  // Right wrist
  joints.add(new PVector(width/2-12,height/2+70));  // Left finger  
  joints.add(new PVector(width/2+42,height/2+70));  // Right finger
}

// ------------------------ Processing draw -------------------------

void draw()
{
  background(255);
  
  h.setSeed(1234);      // Set this if you don't wish to see minor varations on each redraw.
  
  h.rect(30,30,width-60,height-60);
     
  float tilt = atan2(joints.get(1).x-joints.get(0).x,joints.get(1).y-joints.get(0).y);
  pushMatrix();
  translate(joints.get(1).x,joints.get(1).y);
  rotate(-tilt);
  h.ellipse(0,-25,40,50);                         // Head
  popMatrix();
  
  
  
  h.line(joints.get(1).x,joints.get(1).y,joints.get(2).x,joints.get(2).y);     // Body
  h.line(joints.get(2).x,joints.get(2).y,joints.get(3).x,joints.get(3).y);     // Left femur
  h.line(joints.get(2).x,joints.get(2).y,joints.get(4).x,joints.get(4).y);     // Right femur
  h.line(joints.get(3).x,joints.get(3).y,joints.get(5).x,joints.get(5).y);     // Left shin
  h.line(joints.get(4).x,joints.get(4).y,joints.get(6).x,joints.get(6).y);     // Right shin
  h.line(joints.get(5).x,joints.get(5).y,joints.get(7).x,joints.get(7).y);     // Left foot
  h.line(joints.get(6).x,joints.get(6).y,joints.get(8).x,joints.get(8).y);     // Right foot
  h.line(joints.get(1).x,joints.get(1).y,joints.get(9).x,joints.get(9).y);     // Left upper arm
  h.line(joints.get(1).x,joints.get(1).y,joints.get(10).x,joints.get(10).y);   // Right upper arm
  h.line(joints.get(9).x,joints.get(9).y,joints.get(11).x,joints.get(11).y);   // Left lower arm
  h.line(joints.get(10).x,joints.get(10).y,joints.get(12).x,joints.get(12).y); // Right lower arm
  h.line(joints.get(11).x,joints.get(11).y,joints.get(13).x,joints.get(13).y); // Left hand
  h.line(joints.get(12).x,joints.get(12).y,joints.get(14).x,joints.get(14).y); // Right hand
  
  //ellipse(joints.get(0).x,joints.get(0).y,4,4);
}


void mousePressed()
{
  // Find nearest joint to mouse position.
  float minDist = MAX_FLOAT;
  for (int i=0; i<joints.size(); i++)
  {
    float currentDist = dist(mouseX,mouseY,joints.get(i).x,joints.get(i).y);
    if (currentDist < minDist)
    {
      minDist = currentDist;
      movingJoint = i;
    }
  } 
}

void mouseDragged()
{
  if (movingJoint >=0)
  {
    joints.get(movingJoint).x = mouseX;
    joints.get(movingJoint).y = mouseY;
  }
}
