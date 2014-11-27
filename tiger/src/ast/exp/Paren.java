package ast.exp;
//自己加的，类似于stm里面的 block

public class Paren extends T
{
  public T exp;
  public Paren(T exp)
  {
	  this.exp = exp;
  }

  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
    return;
  }
}
