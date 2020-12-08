import javax.swing.JFrame;
import javax.swing.JOptionPane;

//The default Turing program to execute
String program = "program.tm";

TMd turing;
String text;
boolean pause = false, finished;
int state = 1;

void setup() {  
  size(1200, 1000);

  finished = false;
  text = "";
  state = 1;

  try {
    turing= new TMd(program);
  }
  catch(Exception e) {
    JOptionPane.showMessageDialog(frame, "\"program.tm\" does not exist");
    print(e);
    exit();
    return;
  }

  text = turing.toString();

  frameRate(60);
}

void draw() {
  background(44);

  try {
    turing.showTapeSummary(50, 50, width-100, 300);
    //turing.show(50, 400, width-100, 300, 6);
  }
  catch(Exception error) {
    print(error);
  }

  textSize(60);
  textAlign(LEFT, TOP);
  fill(255);

  if (state == -1)
    text(String.format("Output: %s", ((char)193) + ""), 50, 350);
  else
    text(String.format("Output: %s", turing.output()), 50, 350);

  if (!pause && !finished && frameCount%40==0) {    
    if (state <= 0)
      finished = true;

    try {
      state = turing.update();
      text = turing.printTape();
    }
    catch(Exception error) {
      print(error);
      finished = true;
    }
  }

  fill(255);
  textSize(60);
  textAlign(LEFT, CENTER);
  text(text, 50, height*0.65);
}

void keyPressed() {
  if (finished)
    frameCount = -1;
  else
    pause = !pause;
}

int sign(float n) {
  if (n == 0)
    return 0;

  return round(abs(n)/n);
}
