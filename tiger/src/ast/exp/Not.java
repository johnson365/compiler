package ast.exp;

public class Not extends T
{
  public T exp;
  
  public int notlineNum;


  public Not(T exp,int notlineNum)
  {
    this.exp = exp;
    
    this.notlineNum = notlineNum;
  }
  
  
  public Not(T exp)
  {
    this.exp = exp;
    
    this.notlineNum = 0;
  }

  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
    return;
  }
}
