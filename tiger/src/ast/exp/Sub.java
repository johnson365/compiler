package ast.exp;

public class Sub extends T
{
  public T left;
  public T right;
  
  public int subleftexplineNum;
  public int subrightexplineNum;
  

  public Sub(T left, T right)
  {
    this.left = left;
    this.right = right;
    
    
    this.subleftexplineNum = 0;//防止出现莫名其妙的错误
    this.subrightexplineNum = 0;
  }

  public Sub(T left, T right,int subleftexplineNum,int subrightexplineNum)
  {
    this.left = left;
    this.right = right;
    
    this.subleftexplineNum = subleftexplineNum;
    this.subrightexplineNum = subrightexplineNum;
  }
  
  
  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
    return;
  }
}
