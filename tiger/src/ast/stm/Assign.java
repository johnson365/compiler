package ast.stm;

public class Assign extends T// id = exp;
{
  public String id;
  public ast.exp.T exp;
  public ast.type.T type; // type of the id

  
  public int assignlineNum;

  public Assign(String id, ast.exp.T exp)
  {
    this.id = id;
    this.exp = exp;
    this.type = null;
    
    this.assignlineNum = 0;
  }

  public Assign(String id, ast.exp.T exp,int assignlineNum)
  {
    this.id = id;
    this.exp = exp;
    this.type = null;
    
    this.assignlineNum = assignlineNum;
  }
  
  
  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
  }
}
