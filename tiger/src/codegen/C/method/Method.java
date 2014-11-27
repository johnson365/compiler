package codegen.C.method;

import codegen.C.Visitor;

public class Method extends T
{
  public codegen.C.type.T retType;
  public String classId;
  public String id;
  public java.util.LinkedList<codegen.C.dec.T> formals;
  public java.util.LinkedList<codegen.C.dec.T> locals;
  public java.util.LinkedList<codegen.C.stm.T> stms;
  public codegen.C.exp.T retExp;
  
  
  //lab4
  public java.util.LinkedList<Character> gcstack;


  public Method(codegen.C.type.T retType, String classId, String id,
      java.util.LinkedList<codegen.C.dec.T> formals,
      java.util.LinkedList<codegen.C.dec.T> locals,
      java.util.LinkedList<codegen.C.stm.T> stms, codegen.C.exp.T retExp)
  {
    this.retType = retType;
    this.classId = classId;
    this.id = id;
    this.formals = formals;
    this.locals = locals;
    this.stms = stms;
    this.retExp = retExp;
    
    //在这里遍历一遍locals，然后建立新的数据结构，最后在那边生成
    
    gcstack = new java.util.LinkedList<Character>();
    for(codegen.C.dec.T x : locals)
    {
    	codegen.C.dec.Dec d = (codegen.C.dec.Dec)x;
    	if(d.type.toString().equals("@int")||d.type.toString().equals("@int[]"))
    		gcstack.add('0');
    	else
    		gcstack.add('1');
    }
       
    
  }

  @Override
  public void accept(Visitor v)
  {
    v.visit(this);
  }

}
