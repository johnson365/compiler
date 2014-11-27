package ast.exp;

public class Add extends T
{
  public T left;
  public T right;
  
  public int addleftexplineNum;
  public int addrightexplineNum;
  
  

  public Add(T left, T right)
  {
    this.left = left;
    this.right = right;
    
    
    this.addleftexplineNum = 0;
    this.addrightexplineNum = 0;
  }
  
  public Add(T left, T right,int addleftexplineNum,int addrightexplineNum)
  {
    this.left = left;
    this.right = right;
    
    this.addleftexplineNum = addleftexplineNum;
    this.addrightexplineNum = addrightexplineNum;

  }

  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
    return;
  }
}
