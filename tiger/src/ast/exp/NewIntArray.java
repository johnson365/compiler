package ast.exp;

public class NewIntArray extends T
{
  public T exp;
  
  public int newintarraylineNum;

  
  public NewIntArray(T exp,int newintarraylineNum)
  {
    this.exp = exp;
    
    this.newintarraylineNum = newintarraylineNum;
  }
  
  
  
  public NewIntArray(T exp)
  {
    this.exp = exp;
    
    this.newintarraylineNum = 0;
  }

  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
    return;
  }
}
