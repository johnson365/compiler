package ast.stm;

public class Print extends T
{
  public ast.exp.T exp;

  public int printlineNum;
  
  public Print(ast.exp.T exp)
  {
    this.exp = exp;
    
    this.printlineNum = 0;
  }
  
  public Print(ast.exp.T exp,int printlineNum)
  {
    this.exp = exp;
    
    this.printlineNum = printlineNum;
  }
  

  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
  }
}
