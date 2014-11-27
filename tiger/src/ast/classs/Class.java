package ast.classs;

import ast.Visitor;

public class Class extends T
{
  public String id;
  public String extendss; // null for non-existing "extends"
  public java.util.LinkedList<ast.dec.T> decs;
  public java.util.LinkedList<ast.method.T> methods;
  
  public int extendslineNum;

  public Class(String id, String extendss,
	      java.util.LinkedList<ast.dec.T> decs,
	      java.util.LinkedList<ast.method.T> methods,int extendslineNum)
	  {
	    this.id = id;
	    this.extendss = extendss;
	    this.decs = decs;
	    this.methods = methods;
	    
	    this.extendslineNum = extendslineNum;
	  }
  
  
  
  public Class(String id, String extendss,
      java.util.LinkedList<ast.dec.T> decs,
      java.util.LinkedList<ast.method.T> methods)
  {
    this.id = id;
    this.extendss = extendss;
    this.decs = decs;
    this.methods = methods;
    
    this.extendslineNum = 0;
  }

  @Override
  public void accept(Visitor v)
  {
    v.visit(this);
  }
}
