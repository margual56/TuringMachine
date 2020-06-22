class SyntaxError extends Exception{
  String str = "";
  
  public SyntaxError(){
  }
  
  public SyntaxError(String text){
    str = text;
  }
  
  @Override
  String toString(){
    return "Syntax error: " + str; 
  }
}
