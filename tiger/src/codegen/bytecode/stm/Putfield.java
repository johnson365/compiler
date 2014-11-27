package codegen.bytecode.stm;

import codegen.bytecode.Visitor;

public class Putfield extends T
{
	public String id;
	public String classid;
	public codegen.bytecode.type.T type;
	  public Putfield(String classid,String id,codegen.bytecode.type.T type)
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
