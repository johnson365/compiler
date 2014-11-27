package codegen.bytecode.stm;

import codegen.bytecode.Visitor;

public class IntArrayLength extends T{

	  public IntArrayLength()
	  {
	   
	  }

	  @Override
	  public void accept(Visitor v)
	  {
	    v.visit(this);
	  }
}
