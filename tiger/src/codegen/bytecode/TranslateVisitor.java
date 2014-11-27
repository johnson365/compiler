package codegen.bytecode;

import java.util.Hashtable;
import java.util.LinkedList;

import ast.dec.Dec;

import util.Label;




public class TranslateVisitor implements ast.Visitor
{
  private String classId;
  private int index;
  private Hashtable<String, Integer> indexTable;
  private codegen.bytecode.type.T type; 
  private codegen.bytecode.dec.T dec;
  private LinkedList<codegen.bytecode.stm.T> stms;
  private codegen.bytecode.method.T method;
  private codegen.bytecode.classs.T classs;
  private codegen.bytecode.mainClass.T mainClass;
  public  codegen.bytecode.program.T program;
  
  
  
  private java.util.LinkedList<String> fieldsneedthis;
  

  private boolean fieldnotinIndexTable;
  
  
  private String thisid;
  private codegen.bytecode.type.T thistype; 
  
  
  public java.util.LinkedList<String> classesNamej;
  
  public TranslateVisitor()
  {
    this.classId = null;
    this.indexTable = null;
    this.type = null;
    this.dec = null;
    this.stms = new java.util.LinkedList<codegen.bytecode.stm.T>();
    this.method = null;
    this.classs = null;
    this.mainClass = null;
    this.program = null;
    
    this.thisid = null;
    this.thistype = null;
    
    this.fieldnotinIndexTable = false;
    this.fieldsneedthis = new java.util.LinkedList<String>();
    this.classesNamej = new java.util.LinkedList<String>();

 
  }
//correct
  private void emit(codegen.bytecode.stm.T s)
  {
    this.stms.add(s);
  }

  // /////////////////////////////////////////////////////
  // expressions
  
  //自己加的
  @Override
  public void visit(ast.exp.Paren e)
  {
	//////////////////////////////////自己加的
	 e.exp.accept(this);
  }
  
  
  
  @Override//correct
  public void visit(ast.exp.Add e)
  {

	    e.left.accept(this);
	    e.right.accept(this);
	    emit(new codegen.bytecode.stm.Iadd());
  }


  @Override//correct
  public void visit(ast.exp.And e)
  {
	  Label l0 = new Label(),l1 = new Label(),l2 = new Label();
	  
	  e.left.accept(this);
	  
	  //此时栈顶是 0 or 1
	  emit(new codegen.bytecode.stm.Ifne(l0));
	  emit(new codegen.bytecode.stm.Ldc(0));  
	  emit(new codegen.bytecode.stm.Goto(l2));
	  
	  emit(new codegen.bytecode.stm.Label(l0));
	  e.right.accept(this);
	  emit(new codegen.bytecode.stm.Ifne(l1)); 
	  emit(new codegen.bytecode.stm.Ldc(0)); 
	  emit(new codegen.bytecode.stm.Goto(l2));
	  
	  emit(new codegen.bytecode.stm.Label(l1));
	  emit(new codegen.bytecode.stm.Ldc(1));
	  
	  
	  emit(new codegen.bytecode.stm.Label(l2));
	  
  }

  

  
  @Override//correct
  public void visit(ast.exp.ArraySelect e)
  {

	    
	  e.array.accept(this);
	  e.index.accept(this);
	  emit(new codegen.bytecode.stm.Iaload());
      

  }

  @Override//correct
  public void visit(ast.exp.Call e)
  {

	  
    e.exp.accept(this);
    for (ast.exp.T x : e.args) {
      x.accept(this);
    }
    e.rt.accept(this);
    codegen.bytecode.type.T rt = this.type;
    java.util.LinkedList<codegen.bytecode.type.T> at = new java.util.LinkedList<codegen.bytecode.type.T>();
    for (ast.type.T t : e.at) {
      t.accept(this);
      at.add(this.type);
    }
    emit(new codegen.bytecode.stm.Invokevirtual(e.id, e.type, at, rt));
    return;
  }

  @Override//correct
  public void visit(ast.exp.False e)
  {

	  
	  emit(new codegen.bytecode.stm.Ldc(0));
  }

  @Override
  public void visit(ast.exp.Id e)
  {
	  if(this.fieldsneedthis.contains(e.id))
	  {
		  emit(new codegen.bytecode.stm.Aload(0));
		  
		  e.type.accept(this);
		  emit(new codegen.bytecode.stm.Getfield(this.classId,e.id,this.type));	  
		  this.thisid = e.id;
		  this.thistype = this.type;
		  
		  return;
	  }
	
	  
    int index = this.indexTable.get(e.id);
    ast.type.T type = e.type;
    
    if (type.getNum() > 0)// a reference
      emit(new codegen.bytecode.stm.Aload(index));
    else
      emit(new codegen.bytecode.stm.Iload(index));
    // but what about this is a field?
    return;
  }


  @Override//correct
  public void visit(ast.exp.Length e)
  {
	  e.array.accept(this);
	  emit(new codegen.bytecode.stm.IntArrayLength());
  }
  
  
  @Override//correct
  public void visit(ast.exp.Lt e)
  {
    Label tl = new Label(), fl = new Label(), el = new Label();
    e.left.accept(this);
    e.right.accept(this);
    emit(new codegen.bytecode.stm.Ificmplt(tl));
    emit(new codegen.bytecode.stm.Label(fl));
    emit(new codegen.bytecode.stm.Ldc(0));
    emit(new codegen.bytecode.stm.Goto(el));
    emit(new codegen.bytecode.stm.Label(tl));
    emit(new codegen.bytecode.stm.Ldc(1));
    emit(new codegen.bytecode.stm.Goto(el));
    emit(new codegen.bytecode.stm.Label(el));
    return;
  }

  
  @Override//correct
  public void visit(ast.exp.NewIntArray e)
  {

	  e.exp.accept(this);
	  emit(new codegen.bytecode.stm.NewIntArray());
	  
  }

  @Override//correct
  public void visit(ast.exp.NewObject e)
  {
    emit(new codegen.bytecode.stm.NewObject(e.id));
    return;
  }

  @Override//correct
  public void visit(ast.exp.Not e)
  {
	  e.exp.accept(this);
	  emit(new codegen.bytecode.stm.Ldc(1));
	  emit(new codegen.bytecode.stm.Ixor());
	  
  }

  @Override//correct
  public void visit(ast.exp.Num e)
  {
    emit(new codegen.bytecode.stm.Ldc(e.num));
    return;
  }

  @Override//correct
  public void visit(ast.exp.Sub e)
  {
    e.left.accept(this);
    e.right.accept(this);
    emit(new codegen.bytecode.stm.Isub());
    return;
  }

  


  @Override//correct
  public void visit(ast.exp.This e)
  {
	
    emit(new codegen.bytecode.stm.Aload(0));
    return;
  }

  @Override//correct
  public void visit(ast.exp.Times e)
  {
    e.left.accept(this);
    e.right.accept(this);
    emit(new codegen.bytecode.stm.Imul());
    return;
  }

  @Override//correct
  public void visit(ast.exp.True e)
  {
	  emit(new codegen.bytecode.stm.Ldc(1));
  }

  // statements
  @Override//correct
  public void visit(ast.stm.Assign s)
  {	  
	  if(this.fieldsneedthis.contains(s.id))   
	  {
		  emit(new codegen.bytecode.stm.Aload(0));
		  s.exp.accept(this);
		  
		  s.type.accept(this);
		  emit(new codegen.bytecode.stm.Putfield(this.classId,s.id,this.type));
		  return;
	  }

	s.exp.accept(this);
    int index = this.indexTable.get(s.id);
    ast.type.T type = s.type;
    
    if (type.getNum() > 0)
      emit(new codegen.bytecode.stm.Astore(index));
    else
      emit(new codegen.bytecode.stm.Istore(index));

    return;
  }

  @Override//correct
  public void visit(ast.stm.AssignArray s)
  {
	  
	  
	  if(this.fieldsneedthis.contains(s.id))   
	  {
		  emit(new codegen.bytecode.stm.Aload(0));
		  emit(new codegen.bytecode.stm.Getfield(this.classId, s.id, new codegen.bytecode.type.IntArray()));
		  s.index.accept(this);
		  s.exp.accept(this);
		  	  
		  emit(new codegen.bytecode.stm.Iastore());
		  return;
	  }
	  
	  
	    int index = this.indexTable.get(s.id);
	    
		  if(this.fieldsneedthis.contains(s.id))
		  {
			  emit(new codegen.bytecode.stm.Aload(0));
		  }
		
	    emit(new codegen.bytecode.stm.Aload(index));
	    

	    s.index.accept(this);   
	    
	
	    s.exp.accept(this);
	    
	    emit(new codegen.bytecode.stm.Iastore());
	  
  }

  @Override//correct
  public void visit(ast.stm.Block s)
  {
	    for (ast.stm.T b : s.stms) {
	      b.accept(this);
	    }
  }

  
  @Override//correct
  public void visit(ast.stm.If s)
  {
    Label tl = new Label(), fl = new Label(), el = new Label();
    s.condition.accept(this);
    emit(new codegen.bytecode.stm.Ifne(tl));
    emit(new codegen.bytecode.stm.Label(fl));
    
    s.elsee.accept(this);
    emit(new codegen.bytecode.stm.Goto(el));
    emit(new codegen.bytecode.stm.Label(tl));
    
    
    s.thenn.accept(this);
    emit(new codegen.bytecode.stm.Goto(el));
    emit(new codegen.bytecode.stm.Label(el));
    
    return;
  }

  @Override//correct
  public void visit(ast.stm.Print s)
  {
	  if(s.exp!=null)
    s.exp.accept(this);
    emit(new codegen.bytecode.stm.Print());
    return;
  }

  @Override//correct
  public void visit(ast.stm.While s)
  {
	  Label l0 = new Label(),l1 = new Label(), l2 = new Label(), l3 = new Label();
	  
	  emit(new codegen.bytecode.stm.Label(l0));
	  s.condition.accept(this);
	  emit(new codegen.bytecode.stm.Ifne(l1));
	  emit(new codegen.bytecode.stm.Label(l2));
	  emit(new codegen.bytecode.stm.Goto(l3));
	  emit(new codegen.bytecode.stm.Label(l1));
	  s.body.accept(this);
	  emit(new codegen.bytecode.stm.Goto(l0));
	  emit(new codegen.bytecode.stm.Label(l3));
	  
  }

  // type
  @Override//correct
  public void visit(ast.type.Boolean t)
  {
	  this.type = new codegen.bytecode.type.Int();
  }

  @Override//correct
  public void visit(ast.type.Class t)
  {
	  this.type = new codegen.bytecode.type.Class(t.id);
  }

  @Override//correct
  public void visit(ast.type.Int t)
  {
    this.type = new codegen.bytecode.type.Int();
  }

  @Override//correct
  public void visit(ast.type.IntArray t)
  {
	  this.type = new codegen.bytecode.type.IntArray();
  }

  // dec
  @Override//correct
  public void visit(ast.dec.Dec d)
  {
    d.type.accept(this);
    this.dec = new codegen.bytecode.dec.Dec(this.type, d.id);
    
    
    if(this.fieldnotinIndexTable == false)
           this.indexTable.put(d.id, index++);
    else
    {
    	this.fieldnotinIndexTable = false;
    }
    return;
  }

  // method
  @Override//correct
  public void visit(ast.method.Method m)
  {
    // record, in a hash table, each var's index
    // this index will be used in the load store operation
    this.index = 1;
    this.indexTable = new java.util.Hashtable<String, Integer>();

    m.retType.accept(this);
    codegen.bytecode.type.T newRetType = this.type;
    
    java.util.LinkedList<codegen.bytecode.dec.T> newFormals = new java.util.LinkedList<codegen.bytecode.dec.T>();
    for (ast.dec.T d : m.formals) {
      d.accept(this);
      newFormals.add(this.dec);
      
      
    }
    
    
    java.util.LinkedList<codegen.bytecode.dec.T> locals = new java.util.LinkedList<codegen.bytecode.dec.T>();
    for (ast.dec.T d : m.locals) {
      d.accept(this);
      locals.add(this.dec);
    }
    
    this.stms = new java.util.LinkedList<codegen.bytecode.stm.T>();
    for (ast.stm.T s : m.stms) {
      s.accept(this);
    }

    // return statement is specially treated
    m.retExp.accept(this);

    if (m.retType.getNum() > 0)
      emit(new codegen.bytecode.stm.Areturn());
    else
      emit(new codegen.bytecode.stm.Ireturn());

    this.method = new codegen.bytecode.method.Method(newRetType, m.id,
        this.classId, newFormals, locals, this.stms, 0, this.index);
                                          

    return;
  }

  // class
  @Override//correct
  public void visit(ast.classs.Class c)
  {

	  this.classesNamej.add(c.id);
	  
	
	   for (ast.dec.T dec : c.decs) {
		   ast.dec.Dec d = (ast.dec.Dec)dec;
               this.fieldsneedthis.add(d.id);
		    }
	  
	  
    this.classId = c.id;
    
   
    
    java.util.LinkedList<codegen.bytecode.dec.T> newDecs = new java.util.LinkedList<codegen.bytecode.dec.T>();

    for (ast.dec.T dec : c.decs) {
    	this.fieldnotinIndexTable = true;
      dec.accept(this);
      newDecs.add(this.dec);
    }
    java.util.LinkedList<codegen.bytecode.method.T> newMethods = new java.util.LinkedList<codegen.bytecode.method.T>();
    for (ast.method.T m : c.methods) {
      m.accept(this);
      newMethods.add(this.method);
    }
    this.classs = new codegen.bytecode.classs.Class(c.id, c.extendss, newDecs,
        newMethods);
    return;
  }

  // main class
  @Override//correct
  public void visit(ast.mainClass.MainClass c)
  {

	  this.classesNamej.add(c.id);
	  
  
		this.stms = new java.util.LinkedList<codegen.bytecode.stm.T>();
		for (ast.stm.T s : c.stms) {
			s.accept(this);
		}
    this.mainClass = new codegen.bytecode.mainClass.MainClass(c.id, c.arg,
        this.stms);
  
    this.stms = new java.util.LinkedList<codegen.bytecode.stm.T>();
    return;
  }

  // program
  @Override//correct
  public void visit(ast.program.Program p)
  {
    // do translations
    p.mainClass.accept(this);
    

    java.util.LinkedList<codegen.bytecode.classs.T> newClasses = new java.util.LinkedList<codegen.bytecode.classs.T>();
    for (ast.classs.T classs : p.classes) {
      classs.accept(this);
      newClasses.add(this.classs);
    }
    this.program = new codegen.bytecode.program.Program(this.mainClass,
        newClasses);
    

    
    return;
  }
}
