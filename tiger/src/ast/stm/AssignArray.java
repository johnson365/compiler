package ast.stm;

public class AssignArray extends T //id[indexexp] = exp;
{
  public String id;
  public ast.exp.T index;
  public ast.exp.T exp;
  
  public int assignArrayIdlineNum;
  public int assignArrayIdIndexlineNum;
  public int assignArrayExplineNum;
  

  public AssignArray(String id, ast.exp.T index, ast.exp.T exp)
  {
    this.id = id;
    this.index = index;
    this.exp = exp;
    
    this.assignArrayIdlineNum = 0;
    this.assignArrayIdIndexlineNum = 0;
    this.assignArrayExplineNum = 0;
  }
  
  public AssignArray(String id, ast.exp.T index, ast.exp.T exp,
		  int assignArrayIdlineNum,
		  int assignArrayIdIndexlineNum,
		  int assignArrayExplineNum)
  {
    this.id = id;
    this.index = index;
    this.exp = exp;
     
    this.assignArrayIdlineNum = assignArrayIdlineNum;
    this.assignArrayIdIndexlineNum = assignArrayIdIndexlineNum;
    this.assignArrayExplineNum = assignArrayExplineNum;
   
  }
  
  

  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
  }
}
