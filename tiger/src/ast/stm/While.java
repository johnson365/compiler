package ast.stm;

public class While extends T
{
  public ast.exp.T condition;
  public T body;
  
  public int whileconditionlineNum;

  public While(ast.exp.T condition, T body)
  {
    this.condition = condition;
    this.body = body;
    
    this.whileconditionlineNum = 0;
  }

  public While(ast.exp.T condition, T body,int whileconditionlineNum)
  {
    this.condition = condition;
    this.body = body;
    
    this.whileconditionlineNum = whileconditionlineNum;
  }
  
  
  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
  }
}
