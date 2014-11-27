package ast.stm;

public class If extends T
{
  public ast.exp.T condition;
  public T thenn;
  public T elsee;
  
  
  public int ifconditionlineNum;
  

  public If(ast.exp.T condition,T thenn,T elsee)
  {
    this.condition = condition;
    this.thenn = thenn;
    this.elsee = elsee;
    
    this.ifconditionlineNum = 0;
  }


  public If(ast.exp.T condition,T thenn,T elsee,int ifconditionlineNum)
  {
    this.condition = condition;
    this.thenn = thenn;
    this.elsee = elsee;
    
    this.ifconditionlineNum = ifconditionlineNum;
  }
  
  
  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
  }
}
