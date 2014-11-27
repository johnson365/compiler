package ast.exp;




public class And extends T
{
  public T left;
  public T right;

  public int andleftlineNum;
  public int andrightlineNum;

  public And(T left,T right)
  {
     this.left = left;
     this.right = right;
     
     this.andleftlineNum = 0;
     this.andrightlineNum = 0;
  }
  public And(T left,T right,int andleftlineNum,int andrightlineNum)
  {
     this.left = left;
     this.right = right;
     
     this.andleftlineNum = andleftlineNum;
     this.andrightlineNum = andrightlineNum;
  }

  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
    return;
  }
}
