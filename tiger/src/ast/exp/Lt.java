package ast.exp;

public class Lt extends T
{
  public T left;
  public T right;

  public int ltlineNum;
  
  public Lt(T left, T right)
  {
    this.left = left;
    this.right = right;
    
    this.ltlineNum = 0;//防止出现莫名其妙的错误
  }
  
  public Lt(T left, T right,int ltlineNum)
  {
    this.left = left;
    this.right = right;
    
    this.ltlineNum = ltlineNum;
  }

  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
    return;
  }
}
