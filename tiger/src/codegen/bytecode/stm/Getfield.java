package codegen.bytecode.stm;

import codegen.bytecode.Visitor;

public class Getfield extends T
{
	public String id;
	public String classid;
	public codegen.bytecode.type.T type;
	  public Getfield(String classid,String id,codegen.bytecode.type.T type)
	  {
		  this.classid = classid;
		  this.id = id;
		  
		  this.type = type;
	  }

	  @Override
	  public void accept(Visitor v)
	  {
	    v.visit(this);
	  }
}
