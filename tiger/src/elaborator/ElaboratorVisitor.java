package elaborator;

import java.util.Hashtable;


import ast.type.Int;


          




public class ElaboratorVisitor implements ast.Visitor
{
  public ClassTable classTable; // symbol table for class
  public MethodTable methodTable; // symbol table for each method
  public String currentClass; // the class name being elaborated
  
  public String currentMethod;
  
  public ast.type.T type; // type of the expression being elaborated

  public ElaboratorVisitor()
  {
    this.classTable = new ClassTable();
    this.methodTable = new MethodTable();
    this.currentClass = null;
    this.type = null;
  }

  private void error(String errorMessage,int lineNum)
  {
    System.out.println("Sematics Error: at lineNumber "+lineNum);
    System.out.println(errorMessage+"\n");
    
  }

  // /////////////////////////////////////////////////////
  // expressions
  //自己加的
  @Override
  public void visit(ast.exp.Paren e)
  {
	   e.exp.accept(this);
	   return;
  }
  
  @Override//correct
  public void visit(ast.exp.Add e)
  {                              
	    e.left.accept(this);
	    if (!this.type.toString().equals("@int"))
	    {
	    	error("operator '+' left expression must be int type",e.addleftexplineNum);
	    	
	    }
	    ast.type.T leftty = this.type;
	    e.right.accept(this);
		if (!this.type.toString().equals("@int"))
		{
			error("operator '+' right expression must be int type" ,e.addrightexplineNum);
		
		}
	
	    this.type = new ast.type.Int();
	    return;
  }

  @Override//correct
  public void visit(ast.exp.And e)
  {
	  
	  
	    e.left.accept(this);
	    if (!this.type.toString().equals("@boolean"))//leftexp必须解析之后为 boolean
	    {
	    	error("operator '&&' left expression must be boolean type",e.andleftlineNum);
	    	return;
	    }
	    ast.type.T ty = this.type;
	    e.right.accept(this);
	    if (!this.type.toString().equals("@boolean"))//rightexp必须解析之后为 boolean
	    {
	    	error("operator '&&' right expression must be boolean type",e.andrightlineNum);
	    	return;
	    }
	  //  if (!this.type.toString().equals(ty.toString()))
	  //    error();
	    this.type = new ast.type.Boolean();
	    return;
  }

  @Override//correct
  public void visit(ast.exp.ArraySelect e)// arrayexp[indexexp]
  {
	  e.array.accept(this);

	  
	  e.index.accept(this);
	    if (!this.type.toString().equals("@int"))//indexexp必须解析之后为 boolean
	    {
	    	error("Array index expression must be int type",e.indexlineNum);
	    	return;
	    }
	    
	    this.type = new ast.type.Int();
	    return;

  }

  @Override//correct
  public void visit(ast.exp.Call e)
  {
    ast.type.T leftty;
    ast.type.Class ty = null;

    e.exp.accept(this);
    leftty = this.type;
    if (leftty instanceof ast.type.Class) {
      ty = (ast.type.Class) leftty;
      e.type = ty.id;
    } 
    else
       error("function invoker must be class type",e.atomExplineNum);
 
    
    //形参!!!!!!!!!!!
    MethodType mty = this.classTable.getm(ty.id, e.id);//ty.id -> classbinding -> e.id -> methodtype
    
    java.util.LinkedList<ast.type.T> argsty = new java.util.LinkedList<ast.type.T>();//实参
    for (ast.exp.T a : e.args) {
      a.accept(this);
      argsty.addLast(this.type);
    }
    if (mty.argsType.size() != argsty.size())//
    {
    	error("actual parameter's number must be equal to formal parameter in function call",e.argsNumCalllineNum);
    	return;
    }

    for (int i = 0; i < argsty.size(); i++) {
      ast.dec.Dec dec = (ast.dec.Dec) mty.argsType.get(i);
      if (!dec.type.toString().equals(argsty.get(i).toString())) { 

			if (argsty.get(i) instanceof ast.type.Class && this.classTable.get(argsty.get(i).toString()).extendss != null) // 实参类型是类类型，则处理多态
			{
			
				 ClassBinding cb;
				 int error = 1;
			    cb = this.classTable.get(argsty.get(i).toString());
				while (cb != null && cb.extendss != null) 
				{
					if (cb.extendss.toString().equals(dec.type.toString()))
					{
	
						if(control.Control.codegen == control.Control.Codegen_Kind_t.Bytecode)
						{
							argsty.set(i, dec.type);
						}
						
						error = 0;
						break;
					}
					else			
					{						
						cb = this.classTable.get(cb.extendss);
					}
				}
				
				if(error == 1)
				{
			        error("polymorphic usage error",e.argsNumCalllineNum);
				    return;
				}
				
				
			}
			else
				{
				    error("actual parameter's type is diffrent from formal parameter '"+ dec.id+ "' declarated "+ dec.type+ " type in function call ", e.argsNumCalllineNum);
				    return;
				}
			
		}
    }
    
    
    this.type = mty.retType;
    
    
    e.at = argsty; 
    e.rt = this.type;
    return;
  }

  @Override//correct
  public void visit(ast.exp.False e)
  {
	  this.type = new ast.type.Boolean();
	  return;
  }

  @Override//correct
  public void visit(ast.exp.Id e)//correct
  {
    // first look up the id in method table
    ast.type.T type = this.methodTable.get(this.currentClass,this.currentMethod,e.id);
    // if search failed, then s.id must be a class field.
    if (type == null) {
      type = this.classTable.get(this.currentClass, e.id);
      // mark this id as a field id, this fact will be
      // useful in later phase.
      e.isField = true;
    }
    if (type == null)
    	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    {
    	error("variable '"+e.id+"' can not be use without declaration",e.pair.get(e.id));
    	return;
    }
    this.type = type;
    // record this type on this node for future use.
    e.type = type;//!!!!!!!!!!!!!!!!!!!!!!!!!!!

    return;
  }

  @Override//correct
  public void visit(ast.exp.Length e)//int a; 
                                    
  {
	 e.array.accept(this);

	 this.type = new ast.type.Int();
	 return;
	 
  }

  @Override//correct
  public void visit(ast.exp.Lt e)//correct
  {
    e.left.accept(this);
    ast.type.T ty = this.type;
    e.right.accept(this);
    if (!this.type.toString().equals(ty.toString()))
    	{
    	    error("operator < left expression and right expreesion must be represented as same type",e.ltlineNum);
    	    return;
    	}
    this.type = new ast.type.Boolean();
    return;
  }

  @Override//correct
  public void visit(ast.exp.NewIntArray e)
  {
	   e.exp.accept(this);
	   if (!this.type.toString().equals("@int"))//index必须解析之后为 int
	   {
		   error("new IntArray index must be int type",e.newintarraylineNum);
		   return;
	   }
	   
	   this.type = new ast.type.IntArray();
	    return;
  }

  @Override//correct
  public void visit(ast.exp.NewObject e)//correct
  {
    this.type = new ast.type.Class(e.id);//注意，上面的call，解析完前面，this.type会被置为class
    return;
  }

  @Override//correct
  public void visit(ast.exp.Not e)//not应该和lt一样，最后是boolean类型
  {
	  e.exp.accept(this);
	
	  if(!this.type.toString().equals("@boolean"))
	  {
		  error("operator '!' expression must be boolean type",e.notlineNum);
		//  return;
	  }

	  
	  this.type = new ast.type.Boolean();
	  return;
  }

  @Override//correct
  public void visit(ast.exp.Num e)
  {
    this.type = new ast.type.Int();
    return;
  }//correct

  @Override//correct
  public void visit(ast.exp.Sub e)//correct
  {                               // leftexp - rightexp
    e.left.accept(this);
    if (!this.type.toString().equals("@int"))//leftexp必须解析之后为 int
    {
    	error("operator '-' left expression must be int type",e.subleftexplineNum);
    	//return;
    }
    ast.type.T leftty = this.type;// 返回左边的exp类型
    e.right.accept(this);
    if (!this.type.toString().equals("@int"))//rightexp必须解析之后为 int
    {
    	error("operator '-' right expression must be int type",e.subrightexplineNum);
    	//return;
    }
    //if (!this.type.toString().equals(leftty.toString()))//判断左边和右边的的类型相等与否
    //  error();
    this.type = new ast.type.Int();//表示当前操作sub，完成之后，生成一个操作数类型为 int
    return;
  }

  @Override//correct
  public void visit(ast.exp.This e)//
  
  {
    this.type = new ast.type.Class(this.currentClass);
    return;
  }

  @Override//correct
  public void visit(ast.exp.Times e)
  {
    e.left.accept(this);
    if (!this.type.toString().equals("@int"))//leftexp必须解析之后为 int
    {
    	error("operator '*' left expression must be int type",e.timeslineNum);
    	//return;
    }
    ast.type.T leftty = this.type;
    e.right.accept(this);    
    if (!this.type.toString().equals("@int"))//rightexp必须解析之后为 int
    {
    	error("operator '*' right expression must be int type",e.timeslineNum);
    	//return;
    }
  //  if (!this.type.toString().equals(leftty.toString()))
  //    error();
    this.type = new ast.type.Int();
    return;
  }

  @Override//correct
  public void visit(ast.exp.True e)
  {
	  this.type = new ast.type.Boolean();
	  return;
  }

  // statements
  @Override//correct
  public void visit(ast.stm.Assign s)//已经修改 id = exp;
  {
    // first look up the id in method table
    ast.type.T type = this.methodTable.get(this.currentClass,this.currentMethod,s.id);
    // if search failed, then s.id must
    if (type == null)
      type = this.classTable.get(this.currentClass, s.id);
    if (type == null)
    {
    	error("variable '"+s.id+"' can not be use without declaration",s.assignlineNum);
    	return;
    }
    s.exp.accept(this);//
   
   if(!this.type.toString().equals(type.toString()))
   	{

		if (this.type instanceof ast.type.Class && this.classTable.get(this.type.toString()).extendss != null) 
		{
			
			 ClassBinding cb;
			 int error = 1;
		    cb = this.classTable.get(this.type.toString());
			while (cb != null && cb.extendss != null) 
			{
				if (cb.extendss.toString().equals(type.toString()))
				{
					
					error = 0;
					break;
				}
				else			
				{						
					cb = this.classTable.get(cb.extendss);
				}
			}
			
			if(error == 1)
			{
		        error("polymorphic usage error",s.assignlineNum);
			    return;
			}
			
			
		}
		else
			{
			    error("operator '=' left expression and right expreesion must be the same type",s.assignlineNum);
			    return;
			}
			
			

   	}
   
    s.type = type;
    
    return;
  }

  @Override//correct
  public void visit(ast.stm.AssignArray s)//   A[indexexp] = exp;
  {
	  ast.type.T type = this.methodTable.get(this.currentClass,this.currentMethod,s.id);
	  if (type == null)
	      type = this.classTable.get(this.currentClass, s.id);
	    if (type == null)
	    {
	    	error("Array '"+s.id+"[]' can not be use without declaration",s.assignArrayIdlineNum);
	    	//return;
	    }
	  s.index.accept(this);
	  if (!this.type.toString().equals("@int"))//indexexp必须解析之后为 int
	  {
		  error("Array "+s.id+"[] index expression must be int type",s.assignArrayIdIndexlineNum);
		  //return;
	  }
	  s.exp.accept(this);
	  if (!this.type.toString().equals("@int"))//exp必须解析后为 int
	  {
		  error("operator '=' right expression must be int type",s.assignArrayExplineNum);
		 // return;
	  }
	  return;
  }

  @Override//correct
  public void visit(ast.stm.Block s)
  {
	    for (ast.stm.T ss : s.stms)
	        ss.accept(this);
	    return;
  }

  @Override//correct
  public void visit(ast.stm.If s)
  {
    s.condition.accept(this);
    if (!this.type.toString().equals("@boolean"))
    	{
    	   error("if condition must be represented as boolean",s.ifconditionlineNum);
    	  // return;
    	}
    s.thenn.accept(this);
    s.elsee.accept(this);
    return;
  }

  @Override//correct
  public void visit(ast.stm.Print s)
  {
	 
	  if(s.exp!=null)
    s.exp.accept(this);
    if (!this.type.toString().equals("@int"))
    {
    	error("print statement can only print int type",s.printlineNum);
    	//return;
    }
    return;
  }

  @Override
  public void visit(ast.stm.While s)
  {
	 s.condition.accept(this);
	 if (!this.type.toString().equals("@boolean"))
	 {
		 error("while condition must be represented as boolean",s.whileconditionlineNum);
		// return;
	 }
	 
	 s.body.accept(this);
     return;
	 
  }

  // type
  @Override
  public void visit(ast.type.Boolean t)
  {
	    this.type = new ast.type.Boolean();
	    return;
  }

  @Override
  public void visit(ast.type.Class t)//may be error
  {
	  this.type = new ast.type.Class(this.currentClass);
	    return;
  }

  @Override
  public void visit(ast.type.Int t)
  {
    //System.out.println("aaaa");
	    this.type = new ast.type.Int();
	    return;
  }

  @Override
  public void visit(ast.type.IntArray t)
  {
	    this.type = new ast.type.IntArray();
	    return;
  }

  // dec
  @Override
  public void visit(ast.dec.Dec d)
  {


  }

  // method
  @Override
  public void visit(ast.method.Method m)//correct
  {
	  this.currentMethod = m.id;

	  this.methodTable.put(currentClass, currentMethod,new Hashtable<String,Hashtable<String,elaborator.MethodTable.MethodTypeuse>>());

    this.methodTable.put(this.currentClass,this.currentMethod,m.formals, m.locals);

    


    for (ast.stm.T s : m.stms)
      s.accept(this);
    
    m.retExp.accept(this);
	 if (!this.type.toString().equals(m.retType.toString()))
	 {
		 error("return expression's type is different from method declearation return type",m.retExplineNum);
		 return;
	 }
    
    return;
  }

  // class
  @Override
  public void visit(ast.classs.Class c)//correct
  {
    this.currentClass = c.id;
    if(c.extendss != null)
    {
    	if(this.classTable.get(c.extendss) == null)
    		error("Base class '"+c.extendss+"' can not use without declearation",c.extendslineNum);
    }
    
    
    for (ast.method.T m : c.methods) {
    	
      m.accept(this);
    }
    return;
  }

  // main class
  @Override
  public void visit(ast.mainClass.MainClass c)
  {
    
    
	   for (ast.stm.T s : c.stms)
		  s.accept(this);

	  

    return;
  }

  // ////////////////////////////////////////////////////////
  // step 1: build class table
  // class table for Main class
  private void buildMainClass(ast.mainClass.MainClass mainclass)
  {
    this.classTable.put(mainclass.id, new ClassBinding(null));
      
  }

  // class table for normal classes
  private void buildClass(ast.classs.Class c)//correct
  {
    this.classTable.put(c.id, new ClassBinding(c.extendss));
    
    for (ast.dec.T dec : c.decs) {
      ast.dec.Dec d = (ast.dec.Dec) dec;

      
      this.classTable.put(c.id, d.id, d.type,d.pair.get(d.id));
    }
    for (ast.method.T method : c.methods) {
      ast.method.Method m = (ast.method.Method) method;
      this.classTable.put(c.id, m.id, new MethodType(m.retType, m.formals));
    }
  }

  // step 1: end
  // ///////////////////////////////////////////////////
 
  // program
  @Override
  public void visit(ast.program.Program p)//correct
  {
    // ////////////////////////////////////////////////
    // step 1: build a symbol table for class (the class table)
    // a class table is a mapping from class names to class bindings
    // classTable: className -> ClassBinding{extends, fields, methods}

    buildMainClass((ast.mainClass.MainClass) p.mainClass);
    for (ast.classs.T c : p.classes) {
      buildClass((ast.classs.Class) c);
    }



    
    p.mainClass.accept(this);
    for (ast.classs.T c : p.classes) {
      c.accept(this);
    }
    // we can double check that the class table is OK!
    if (control.Control.elabClassTable) {//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
      this.classTable.dump();
    }
    
    if (control.Control.elabMethodTable)//!!!!!
        this.methodTable.dump();
   
    
  
    this.classTable.warning();//输出警告信息
    System.out.println();//换行
    this.methodTable.warning();//输出警告信息
    
    //copyright by KIRA林泽南
  }
}

//copyright by KIRA林泽南
