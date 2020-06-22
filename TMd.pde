class TMd extends TM {
  private float tapeCounter = 0;
  private float tmpHead;
  private boolean changing = false;

  public TMd(String code) throws SyntaxError {
    super(code);
    
    tmpHead = head;
  }

  public TMd(String code, String initialState) throws SyntaxError {
    super(code, initialState);
    
    tmpHead = head;
  }

  @Override
    protected void move(String m) {
    if (m.equals("R")) {
      this.head++;

      if (this.tape.length-this.head-1 <= 1) {
        char newtape[] = new char[this.tape.length+2];

        for (int i = 0; i<this.tape.length; i++) {
          newtape[i] = tape[i];
        }

        newtape[this.tape.length + 0] = '0';
        newtape[this.tape.length + 1] = '0';

        this.tape = newtape;
      }

      changing = true;
    } else if (m.equals("L")) {
      this.head--;

      if (this.head <= 1) {
        char newtape[] = new char[this.tape.length+2];
        newtape[0] = '0';
        newtape[1] = '0';

        for (int i = 0; i<this.tape.length; i++) {
          newtape[i+2] = tape[i];
        }

        this.tape = newtape;

        this.head += 2;
      }

      changing = true;
    }
  }

  public void showTapeSummary(float x, float y, float wid, float hei) {
    float margin = min(wid, hei)*4/50;

    fill(20);
    stroke(0);
    rect(x+margin/2, y+margin, wid-margin+1, hei-margin);

    fill(30, 150);
    stroke(0);
    rect(x, y, wid, hei);

    stroke(0);
    line(x, y, x+margin/2, y+margin);
    line(x+wid, y, x+wid-margin/2+1, y+margin);

    int columns = 11;
    int rows = tape.length/columns;

    float cs = min((wid-margin*2)/columns, (hei-margin)/rows);

    float x1 = x + margin, y1 = y + margin*3/2;

    stroke(255);
    for (int i = 0, column = 1; i<tape.length; i++, column++) {  
      int value = getTape(i)-'0';
      
      if (value == 0) {
        fill(0);
      } else {
        fill(255, 0, 0);
      }

      rect(x1, y1, cs, cs);

      x1 += cs;

      if (column == columns) {
        column = 0;
        y1 += cs;
        x1 = x + margin;
        
        while(y1 > y+hei || y1+cs>y+hei)
          cs-=0.5;
      }
    }
  }

  public void show(float x0, float y, float wid, float hei, int headspace) throws RuntimeError{
    float cs = min(wid/(headspace*2), hei);
    
    textAlign(CENTER, CENTER);
    textSize(cs*0.9);
    
    float x, offset = lerp(tmpHead, this.head, tapeCounter);
    float middle_x = x0+wid/2;
    
    stroke(255);
    for(int i = min(0, this.head-headspace); i<max(this.head+headspace, this.tape.length+1); i++){
      x = middle_x + cs*(i-offset);
      
      if(x<x0 || x>=x0+wid)
        continue;
        
      fill(0);
      rect(x, y, cs, cs);
      fill(255);
      text(getTape(i), x+cs/2, y+cs*0.4);
    }
    
    if (tapeCounter == 1) {
      tapeCounter = 0;
      tmpHead = this.head;
      changing = false;
    }else if(changing){
      tapeCounter += 0.1;
    }
  }
}
