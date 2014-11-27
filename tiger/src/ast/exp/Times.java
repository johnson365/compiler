package ast.exp;

public class Times extends T
{
  public T left;
  public T right;
  
  
  public int timeslineNum;

  public Times(T left, T right)
  {
    this.left = left;
    this.right = right;
    
    this.timeslineNum = 0;//防止出现莫名其妙的错误
  }

  public Times(T left, T right,int timeslineNum)
  {
    this.left = left;
    this.right = right;
    
    this.timeslineNum = timeslineNum;
  }
  
  
  
  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
    return;
  }
}
