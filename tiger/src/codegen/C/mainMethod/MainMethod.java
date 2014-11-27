package codegen.C.mainMethod;

import codegen.C.Visitor;

public class MainMethod extends T
{
  public java.util.LinkedList<codegen.C.dec.T> locals;
  public java.util.LinkedList<codegen.C.stm.T> stms;//已改。。。。。。。。。。

  public MainMethod(java.util.LinkedList<codegen.C.dec.T> locals,
		  java.util.LinkedList<codegen.C.stm.T> stms)
  {
    this.locals = locals;
    this.stms = stms;
  }

  @Override
  public void accept(Visitor v)
  {
    v.visit(this);
  }

}
