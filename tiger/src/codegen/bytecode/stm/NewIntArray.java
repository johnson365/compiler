package codegen.bytecode.stm;

import codegen.bytecode.Visitor;

public class NewIntArray extends T{



	  public NewIntArray()
	  {
	   
	  }

	  @Override
	  public void accept(Visitor v)
	  {
	    v.visit(this);
	  }
}
