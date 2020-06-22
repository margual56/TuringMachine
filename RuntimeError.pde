class RuntimeError extends Exception{
  String str = "";
  
  public RuntimeError(){
  }
  
  public RuntimeError(String text){
    str = text;
  }
  
  @Override
  String toString(){
    return "Runtime error: " + str; 
  }
}
