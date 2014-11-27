package codegen.bytecode.stm;

import codegen.bytecode.Visitor;

public class NewObject extends T
{
  public String c;

  public NewObject(String c)
  {
    this.c = c;
  }

  @Override
  public void accept(Visitor v)
  {
    v.visit(this);
  }
}
