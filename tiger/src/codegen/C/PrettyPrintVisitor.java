package codegen.C;

import java.util.Iterator;

import control.Control;


public class PrettyPrintVisitor implements Visitor
{
  private int indentLevel;
  private java.io.BufferedWriter writer;
  
  private java.util.LinkedList<codegen.C.Tuple> thisTuples;
  

  private boolean decfixIntArray;
  private boolean newfixIntArray;
  private codegen.C.exp.T expfixIntArray;
  
  
  private codegen.C.type.T thistype;
  private String thisClassId;
  public java.util.HashMap<String,java.util.LinkedList<Character>> thisgcfield;
  

  public PrettyPrintVisitor()
  {
    this.indentLevel = 2;
  
    this.decfixIntArray = false;
    this.newfixIntArray = false;
    
    thisTuples = new java.util.LinkedList<codegen.C.Tuple>();
    
    thisClassId = null;
    thisgcfield = new java.util.HashMap<String,java.util.LinkedList<Character>>();
    
    
  }

  private void indent()
  {
    this.indentLevel += 2;
  }

  private void unIndent()
  {
    this.indentLevel -= 2;
  }

  private void printSpaces()
  {
    int i = this.indentLevel;
    while (i-- != 0)
      this.say(" ");
  }

  private void sayln(String s)
  {
    say(s);
    try {
      this.writer.write("\n");
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private void say(String s)
  {
    try {
      this.writer.write(s);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  // /////////////////////////////////////////////////////
  // expressions
  
  //自己加的
  @Override
  public void visit(codegen.C.exp.Paren e)
  {
	//////////////////////////////////自己加的
	  this.say("(");
	  e.exp.accept(this);
	  this.say(")");	 
  }
  
  
  @Override
  public void visit(codegen.C.exp.Add e)
  {
	    e.left.accept(this);
	    this.say(" + ");
	    e.right.accept(this);
  }

  @Override
  public void visit(codegen.C.exp.And e)
  {
	    e.left.accept(this);
	    this.say(" && ");
	    e.right.accept(this);
  }

  @Override
  public void visit(codegen.C.exp.ArraySelect e)// arrayexp[indexexp]
  {
	  e.array.accept(this);
	  this.say("[");
	  e.index.accept(this);
	  this.say("]");
  }

  @Override//correct
  public void visit(codegen.C.exp.Call e)
  {
    this.say("(" + e.assign + "=");
    e.exp.accept(this);
    this.say(", ");
    this.say(e.assign + "->vptr->" + e.id + "(" + e.assign);
    
    int size = e.args.size();  
    if (size == 0) {   
      this.say("))");
      return;
    }
    for (codegen.C.exp.T x : e.args) {
      this.say(", ");
      x.accept(this);
    }
    this.say("))");
    return;
  }

  @Override//correct
  public void visit(codegen.C.exp.Id e)
  {

	    Iterator it = this.thisTuples.iterator();
	    while (it.hasNext()) 
	    {
	    	codegen.C.Tuple tempTuple = (codegen.C.Tuple)it.next();
	    	if(tempTuple.id.equals(e.id))
	    		{
	    		    this.say("this->");
	    		    break;
	    		}
	    }
	  this.say(e.id);

	  
  }

  @Override
  public void visit(codegen.C.exp.Length e)
  {
	  e.array.accept(this);
	  this.say("_size");
  }

  @Override//correct
  public void visit(codegen.C.exp.Lt e)
  {
    e.left.accept(this);
    this.say(" < ");
    e.right.accept(this);
    return;
  }

  @Override
  public void visit(codegen.C.exp.NewIntArray e)
  {
	  this.say("(int*)malloc(sizeof(int)*");
	  e.exp.accept(this);
	  this.say(")");  
	  
	  
	  this.newfixIntArray = true;
	  this.expfixIntArray = e.exp;

  }

  @Override//correct
  public void visit(codegen.C.exp.NewObject e)
  {
	  this.say("((struct " + e.id + "*)Tiger_new(&" + e.id
			        + "_vtable_object, sizeof(struct " + e.id + ")))");
    return;
  }

  @Override
  public void visit(codegen.C.exp.Not e)
  {
	  this.say("!");
	  e.exp.accept(this);
  }

  @Override//correct
  public void visit(codegen.C.exp.Num e)
  {
    this.say(Integer.toString(e.num));
    return;
  }

  @Override//correct
  public void visit(codegen.C.exp.Sub e)
  {
    e.left.accept(this);
    this.say(" - ");
    e.right.accept(this);
    return;
  }

  @Override//correct
  public void visit(codegen.C.exp.This e)
  {
    this.say("this");
  }

  @Override//correct
  public void visit(codegen.C.exp.Times e)
  {
    e.left.accept(this);
    this.say(" * ");
    e.right.accept(this);
    return;
  }

  // statements
  @Override//correct
  public void visit(codegen.C.stm.Assign s)
  {
	  this.printSpaces();
	    
	    boolean success =false;
		
		    Iterator it = this.thisTuples.iterator();
		    while (it.hasNext()) 
		    {
		    	codegen.C.Tuple tempTuple = (codegen.C.Tuple)it.next();
		    	if(tempTuple.id.equals(s.id))
		    		{
		    		    this.say("this->");
		    		    success = true;
		    		    break;
		    		}
		    }
	    
	    this.say(s.id + " = ");
	    s.exp.accept(this);
	    this.sayln(";");
	    if(this.newfixIntArray == true)
	    {
	    	if(success)
	    	{
	    		this.say("  this->");
	    	}
	    	else
	    		this.say("  ");
	    	this.say(s.id+"_size = ");
	    	this.expfixIntArray.accept(this);
	    	this.sayln(";");
	    	
	    	this.newfixIntArray = false;
	    }
	    
	    
	    return;
  }

  @Override
  public void visit(codegen.C.stm.AssignArray s)
  {
	  this.printSpaces();
	    Iterator it = this.thisTuples.iterator();
	    while (it.hasNext()) 
	    {
	    	codegen.C.Tuple tempTuple = (codegen.C.Tuple)it.next();
	    	if(tempTuple.id.equals(s.id))
	    		{
	    		    this.say("this->");
	    		    break;
	    		}
	    }
	  
	  
	  this.say(s.id);
	  this.say("[");
	  s.index.accept(this);
	  this.say("] = ");
	  s.exp.accept(this);
	  this.sayln(";");
  }

  @Override
  public void visit(codegen.C.stm.Block s)
  {
	    this.printSpaces();	    
	    this.sayln("{");
	    this.indent();
	    for (codegen.C.stm.T stm : s.stms)
	        stm.accept(this);
	    this.unIndent();
	    this.printSpaces();
	    this.sayln("}");
	    this.sayln("");
  }

  @Override//correct
  public void visit(codegen.C.stm.If s)
  {
	this.sayln("");//if语句前换个行先
    this.printSpaces();
    this.say("if(");
    s.condition.accept(this);
    this.sayln(")");
    this.indent();
    s.thenn.accept(this);
    this.unIndent();
    this.sayln("");
    this.printSpaces();
    this.sayln("else");
    this.indent();
    s.elsee.accept(this);
    this.sayln("");
    this.unIndent();
    return;
  }

  @Override//correct
  public void visit(codegen.C.stm.Print s)
  {
    this.printSpaces();
    this.say("System_out_println (");
    s.exp.accept(this);
    this.sayln(");");
    return;
  }

  @Override
  public void visit(codegen.C.stm.While s)
  {
	    this.sayln("");
	    this.printSpaces();
	    this.say("while(");
	    s.condition.accept(this);
	    this.sayln(")");
	    this.indent();
	    s.body.accept(this);
	    this.unIndent();
  }

  // type
  @Override//correct
  public void visit(codegen.C.type.Class t)
  {
    this.say("struct " + t.id + "*");

    this.thistype = new codegen.C.type.Class(t.id);
  }

  @Override//correct
  public void visit(codegen.C.type.Int t)
  {
    this.say("int");
    
    this.thistype = new codegen.C.type.Int();
  }

  @Override
  public void visit(codegen.C.type.IntArray t)
  {
	  
	  this.say("int*");
      this.decfixIntArray = true;
     
      this.thistype = new codegen.C.type.IntArray();
  }

  // dec
  @Override
  public void visit(codegen.C.dec.Dec d)
  {
	  d.type.accept(this);
	  this.say(" "+d.id);
  }

  // method
  @Override//correct
  public void visit(codegen.C.method.Method m)
  {

	  this.sayln("struct "+m.classId+"_"+m.id+"_gc_frame");
	  this.sayln("{");
	  this.sayln("  void* prev;");
	  this.sayln("  int* fields_this_address;");
	  this.sayln("  char* arguments_gc_map;");
	  this.sayln("  char* locals_gc_map;");
	  
	  int tempindex = 0;
	  for (codegen.C.dec.T d : m.locals) 
      {
			if (m.gcstack.get(tempindex) == '1') 
			{
				codegen.C.dec.Dec dec = (codegen.C.dec.Dec) d;
				this.say("  ");
				dec.type.accept(this);
				this.say(" " + dec.id);
				this.sayln(";");
	      }
			tempindex++;
	  }
	  this.sayln("};");

	  this.sayln("char* "+m.classId+"_"+m.id+"_arguments_gc_map;");
	  this.sayln("char* "+m.classId+"_"+m.id+"_locals_gc_map;");
	  
	  m.retType.accept(this);
	    this.say(" " + m.classId + "_" + m.id + "(");
	    

	    
	    java.util.LinkedList<Character> arguments_gc_map = new java.util.LinkedList<Character>();
	    java.util.LinkedList<Character> locals_gc_map = new java.util.LinkedList<Character>();
	
	    int size = m.formals.size();
	    for (codegen.C.dec.T d : m.formals) {
	      codegen.C.dec.Dec dec = (codegen.C.dec.Dec) d;
	      size--;
	      dec.accept(this);
          if(this.thistype.toString().equals("@int") || this.thistype.toString().equals("@int[]"))
        	  arguments_gc_map.add('0');
	    	  
          else
        	  arguments_gc_map.add('1');
	      
	      if (size > 0)
	        this.say(", ");
	    }
	    this.sayln(")");
	    this.sayln("{");

	   
	    this.sayln("  struct "+m.classId+"_"+m.id+"_gc_frame frame;\n");
	    
	    
	    
	    for (codegen.C.dec.T d : m.locals) {
	      codegen.C.dec.Dec dec = (codegen.C.dec.Dec) d;
	      this.say("  ");   
	      dec.type.accept(this);
          if(this.thistype.toString().equals("@int") || this.thistype.toString().equals("@int[]"))
        	  
	    	  ;
          else
        	  locals_gc_map.add('1');
	      
	      
	      
	      this.say(" " + dec.id);  

		  this.sayln(";");

			  if(this.decfixIntArray == true)
			  {
				  this.sayln("  int "+dec.id+"_size;");
				  this.decfixIntArray = false;
			  }
	    }

	    this.sayln("");	    
	    this.sayln("  frame.prev = prev;");
	    this.sayln("  prev = &frame;");
	    
	    this.sayln("  frame.arguments_gc_map = "+m.classId+"_"+m.id+"_arguments_gc_map;");
	    this.sayln("  frame.fields_this_address = this;");
	    this.sayln("  frame.locals_gc_map = "+m.classId+"_"+m.id+"_locals_gc_map;");	
	    
	        
	    
	    this.sayln("");
	    for (codegen.C.stm.T s : m.stms)
	      s.accept(this);
	    
	    
	    
	    
	  
	    int tempindex2 = 0;
		  for (codegen.C.dec.T d : m.locals) 
		  {
			if (m.gcstack.get(tempindex2) == '1') 
			{
				codegen.C.dec.Dec dec = (codegen.C.dec.Dec) d;
				this.sayln("  " + "frame." + dec.id + " = " + dec.id + ";");
			}
			tempindex2++;
		  }
	  
	    
	    
	    
	    
	    
	    
	   
	    
	    this.say("  return ");
	    m.retExp.accept(this);
	    this.sayln(";");
	    this.sayln("}");
	       
	    

	  
	    
	    this.say("char* "+m.classId+"_"+m.id+"_arguments_gc_map = \"");
	    for(char x:arguments_gc_map)
	    	this.say(String.valueOf(x));
	    this.sayln("\";");
	    
	    this.say("char* "+m.classId+"_"+m.id+"locals_gc_map = \"");
	    for(char x:locals_gc_map)
	    	this.say(String.valueOf(x));
	    this.sayln("\";\n");
	    
	    
	    
	    return;
  }

  @Override//correct
  public void visit(codegen.C.mainMethod.MainMethod m)
  {
    this.sayln("int Tiger_main(int argc,char** argv)");
    this.sayln("{");
    for (codegen.C.dec.T dec : m.locals) {
      this.say("  ");
      codegen.C.dec.Dec d = (codegen.C.dec.Dec) dec;
      d.accept(this);//用dec来解析
  
      this.sayln(";");
    }

    for (codegen.C.stm.T s : m.stms)
        s.accept(this);
      this.sayln("  return 0;");
    this.sayln("}\n");

    return;
  }

  @Override
  public void visit(codegen.C.vtable.Vtable v)
  {
    this.sayln("struct " + v.id + "_vtable");
    this.sayln("{");

    this.sayln("  char* "+v.id+"_field_gc_map;");


    
    for (codegen.C.Ftuple t : v.ms) {
      this.say("  ");
      t.ret.accept(this);
      this.say(" (*" + t.id+")(");
 
      if(!t.args.isEmpty())
      {
    	  t.args.getFirst().accept(this);
    	  Iterator it= t.args.iterator();
    	  if(it.hasNext())//从第二个元素开始哦
    	   	    it.next();
          while(it.hasNext())
          {
        	  this.say(",");    
           	  ((codegen.C.dec.T)it.next()).accept(this);        	 
          }
      }
    
      this.sayln(");");
    }
    this.sayln("};\n");
    
    this.sayln("// declaration vtable object");
    this.sayln("struct "+v.id+"_vtable "+v.id+"_vtable_object;");
    this.sayln("");
    
    return;
  }

  private void outputVtable(codegen.C.vtable.Vtable v)
  {
    this.say("struct " + v.id + "_vtable " + v.id + "_vtable_object = ");
    this.sayln("");
    this.sayln("{");
    this.say("  \"");
    
    java.util.LinkedList<Character> cc = new java.util.LinkedList<Character>();
    cc =  this.thisgcfield.get(v.id);
    for(char x : cc)
    {
    	if(x == '0')
    		this.say("0");
    	else
    		this.say("1");
    }
    this.sayln("\",");

    
    
    
    this.say("  ");
    if(v.ms.size() != 0) 
    {
    	this.say(v.ms.get(0).classs + "_" + v.ms.get(0).id);  
			for (int i = 1; i < v.ms.size(); i++) 
			{
				this.sayln(",");
				this.say("  ");
				this.say(v.ms.get(i).classs + "_" + v.ms.get(i).id);
			}
    }
    this.sayln("");   
    this.sayln("};\n");
     
  }


  
  // class
  @Override
  public void visit(codegen.C.classs.Class c)
  {
	  
	
	  this.thisClassId = c.id;
	  for(codegen.C.Tuple t:c.decs)
	  thisTuples.add(t);
	  
	  
    this.sayln("struct " + c.id);
    this.sayln("{");
    this.sayln("  struct " + c.id + "_vtable* vptr;");
 

    this.sayln("  int isObjOrArray;");
    this.sayln("  unsigned length;");
    this.sayln("  void* forwarding;");
    
    
    java.util.LinkedList<Character> cc = new java.util.LinkedList<Character>();
    for (codegen.C.Tuple t : c.decs) {
      this.say("  ");//因为struct是贴着边写的，不会嵌套，所以直接空2格就可以了
      t.type.accept(this); 
      
      //lab4     
      	if(this.thistype.toString().equals("@int") || this.thistype.toString().equals("@int[]"))	
      		cc.add('0');  	
      	else
      		cc.add('1');   	
      
         
      this.say(" ");//int a 之间 空一格
      this.say(t.id);
	  this.sayln(";");
	  if(this.decfixIntArray == true)
	  {
		  this.sayln("  int "+t.id+"_size;");
		  this.decfixIntArray = false;
		  
		  cc.add('0');
		  
	  }
    }
    this.thisgcfield.put(c.id, cc);
    
    this.sayln("};");
    return;
  }

  // program
  @Override
  public void visit(codegen.C.program.Program p)
  {
    // we'd like to output to a file, rather than the "stdout".
    try {
      String outputName = null;
      if (Control.outputName != null)
        outputName = Control.outputName;
      else if (Control.fileName != null)
        outputName = Control.fileName + ".c";
      else
        outputName = "a.c";

      this.writer = new java.io.BufferedWriter(new java.io.OutputStreamWriter(
          new java.io.FileOutputStream(outputName)));
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }

    this.sayln("// This is automatically generated by the Tiger compiler.");
    this.sayln("// Do NOT modify!\n");

    this.sayln("//output .h");
    this.sayln("#include<stdio.h>");
    this.sayln("#include<stdlib.h>");
    this.sayln("");
    
    this.sayln("\n\n//a global pointer");
    this.sayln("void* prev = NULL;\n\n");

    
    this.sayln("// structures");
    for (codegen.C.classs.T c : p.classes) {
      c.accept(this);
    }

    this.sayln("// vtables structures");
    for (codegen.C.vtable.T v : p.vtables) {
      v.accept(this);
    }
    this.sayln("");
    
    


    this.sayln("// methods");
    for (codegen.C.method.T m : p.methods) {
      m.accept(this);
    }
    this.sayln("");

    
    this.sayln("// vtables");
    for (codegen.C.vtable.T v : p.vtables) {
      outputVtable((codegen.C.vtable.Vtable) v);
    }
    this.sayln("");
    



    this.sayln("// main method");
    p.mainMethod.accept(this);
    this.sayln("");

    this.say("\n\n");
    
    try {
      this.writer.close();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }

  }

}
