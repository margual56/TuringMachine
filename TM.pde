import java.io.*; //<>// //<>//

class TM {
  protected HashMap instructions;
  protected String finalStates[];
  protected int head = 0;
  protected String state = "";
  protected char tape[];
  protected boolean undefined = false;

  public TM(String code) throws SyntaxError {
    instructions = new HashMap();

    String lines[] = loadStrings(code);
    String mycode = "";

    for (String line : lines) {
      if (line.indexOf("//")!=-1) {
        line = line.substring(0, line.indexOf("//"));
      }

      line = line.replace(" ", "").replace("\n", "");

      mycode += line;
    }

    String commands[] = mycode.split(";");

    if (commands[0].charAt(0)!='{') {
      throw new SyntaxError("No initial state definition!\n\"{00...<initial_state>...00}\" needed!");
    }

    if (commands[1].indexOf("F={")==-1)
      throw new SyntaxError("No final state definition!\n\"F={<final_state(s)>}\" needed!");

    String state, reads;
    for (int i = 2; i<commands.length; i++) {
      String line = commands[i];

      line = line.replace("(", "").replace(")", "");

      String s[] = line.split(",");

      if (s.length!=5)
        throw new SyntaxError("Error in command " + i + ", 5 parameters expected. " + s.length + " parameters found instead.");

      state = s[0];
      reads = s[1];

      instructions.put(state+reads, s);
    }

    setInitialState(commands[0]);
    setFinalStates(commands[1]);
  }

  public TM(String code, String initialState) throws SyntaxError {
    instructions = new HashMap();

    String lines[] = loadStrings(code);
    String mycode = "";

    for (String line : lines) {
      if (line.indexOf("//")!=-1) {
        line = line.substring(0, line.indexOf("//"));
      }

      line = line.replace(" ", "").replace("\n", "");

      mycode += line;
    }

    String commands[] = mycode.split(";");
    int startingLine = 1;

    if (commands[0].charAt(0)!='{') {
      startingLine = 0;
    }

    if (commands[startingLine+1].indexOf("F={")==-1)
      throw new SyntaxError("No final state definition!\n\"F={<final_state(s)>}\" needed!");

    String state, reads;
    for (int i = startingLine+2; i<commands.length; i++) {
      String line = commands[i];

      line = line.replace("(", "").replace(")", "");

      String s[] = line.split(",");

      if (s.length!=5)
        throw new SyntaxError("Error in command " + i + ", 5 parameters expected. " + s.length + " parameters found instead.");

      state = s[0];
      reads = s[1];

      instructions.put(state+reads, s);
    }

    setInitialState(initialState);
    setFinalStates(commands[1]);
  }

  private void setInitialState(String state) {
    state = state.replace("{", "").replace("}", "");

    int i = 0;
    while (!Character.isLetter(state.charAt(i))) {
      i++;
    }

    String initialPosition = (state.charAt(i) + "") + ("" + state.charAt(i+1));
    state = state.replace(initialPosition, "");
    this.state = initialPosition;
    head = i;

    tape = new char[state.length()];

    for (i = 0; i<tape.length; i++) {
      tape[i] = state.charAt(i);
    }
  }

  private void setFinalStates(String state) {
    state = state.replace("#defineF={", "").replace("}", "");

    finalStates = state.split(",");
  }

  public int update() throws RuntimeError{
    String[] current;
    try{
      current = getInstruction(this.state, ""+getTape(this.head));
    }catch(RuntimeError error){
      throw error;
    }
    
    this.tape[this.head] = current[2].charAt(0);

    move(current[3]);

    this.state = current[4];  
    
    try{
      current = getInstruction(this.state, ""+getTape(this.head));
    }catch(RuntimeError error){
      throw error;
    }
    
    if (current[3].equals("H")) {
      if (isFinal(this.state)) {
        return 0;
      } else {
        undefined = true;
        return -1;
      }
    } else {
      return 1;
    }
  }

  private boolean isFinal(String s) {
    for (String st : finalStates)
      if (st.equals(s))
        return true;

    return false;
  }

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
    }
  }

  private String[] getInstruction(String s, String v) throws RuntimeError {
    String[] toReturn = (String[])instructions.get(s+v);
    
    if(toReturn == null)
      throw new RuntimeError("The state " + s + " with the value " + v + " on the tape is not defined");
      
    return toReturn;
  }

  public String getTape() {
    String t = "{";

    for (int i = 0; i<tape.length; i++) {
      if (i == head) {
        t += state + tape[i];
      } else {
        t += tape[i] + "";
      }
    }

    return t + "};";
  }

  protected char getTape(int index) {
    if (index<0 || index>=tape.length)
      return '0';

    return tape[index];
  }

  @Override
    public String toString() {
    String out = "";

    for (int i = 0; i<tape.length-1; i++) {
      out += tape[i] + " ";
    }

    out += tape[tape.length-1] + "\n";

    for (int i = 0; i<head*2; i++)
      out += " ";

    out += "^\n";

    String s[];
    
    try{
      s = getInstruction(state, ""+getTape(head));
    }catch(RuntimeError error){
      return out;
    }

    out += "(";
    for (int i = 0; i<s.length-1; i++)
      out += s[i] + ", ";
    out += s[s.length-1] + ")\n";

    return out;
  }
  
  public String output(){
    if(undefined)
      return ((char)193) + "";
    
    int sum = 0;
    
    for(int i = 0; i<tape.length; i++){
      sum += tape[i]-'0';
    }
    
    return Integer.toString(sum);
  }

  public String printTape() {
    String out = "";

    for (int i = 0; i<tape.length-1; i++) {
      out += tape[i] + " ";
    }

    out += tape[tape.length-1] + "\n";

    for (int i = 0; i<head*3; i++)
      out += " ";

    out += "^\n";

    String s[];
    
    try{
      s = getInstruction(state, ""+tape[head]);
    }catch(RuntimeError error){
      return out;
    }

    out += "(";
    for (int i = 0; i<s.length-1; i++)
      out += s[i] + ", ";
    out += s[s.length-1] + ")\n";

    return out;
  }
}
