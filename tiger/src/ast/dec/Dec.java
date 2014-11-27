package ast.dec;

import java.util.Map;

import ast.Visitor;

public class Dec extends T
{
  public ast.type.T type;
  public String id;
  
  
  public Map<String,Integer> pair;//!!!!!!!!!!!
  
  public Dec(ast.type.T type, String id,int declineNum)
  {
    this.type = type;
    this.id = id;
    
    this.pair = new java.util.HashMap<String, Integer>();
    this.pair.put(id,declineNum);
  }
  
  

  public Dec(ast.type.T type, String id)
  {
    this.type = type;
    this.id = id;
  }

  @Override
  public void accept(Visitor v)
  {
    v.visit(this);
  }
}
