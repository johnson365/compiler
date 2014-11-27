package ast.exp;

import java.util.Map;

public class Id extends T
{
  public String id; // name of the id
  public ast.type.T type; // type of the id
  public boolean isField; // whether or not this is a class field
  
  public Map<String,Integer> pair;
  
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11
  public Id(String id,int lineNum)
  {
    this.id = id;
    this.type = null;
    this.isField = false;
    
    
    this.pair = new java.util.HashMap<String, Integer>();
    this.pair.put(id,lineNum);
    
  }
  
  
 
 
  public Id(String id)
  {
    this.id = id;
    this.type = null;
    this.isField = false;
  }
  
  
  public Id(String id, ast.type.T type, boolean isField)
  {
    this.id = id;
    this.type = type;
    this.isField = isField;
  }

  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
    return;
  }
}
