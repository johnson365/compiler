package ast;
import java.util.Iterator;




public class PrettyPrintVisitor implements Visitor
{
  private int indentLevel;

  public PrettyPrintVisitor()
  {
    this.indentLevel = 4;
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
    System.out.println(s);
  }

  private void say(String s)
  {
    System.out.print(s);
  }

  // /////////////////////////////////////////////////////
  // expressions
  @Override
  public void visit(ast.exp.Paren e)
  {
	  this.say("(");
	  e.exp.accept(this);
	  this.say(")");
    return;
  }
  
  @Override
  public void visit(ast.exp.Add e)//correct
  {
    // Lab2, exercise4: filling in missing code.
    // Similar for other methods with empty bodies.
    // Your code here:
	    e.left.accept(this);
	    this.say(" + ");
	    e.right.accept(this);
	    return;
  }

  @Override
  public void visit(ast.exp.And e)//correct
  {
	  e.left.accept(this);
	  this.say(" && ");
	  e.right.accept(this);
	  return;
  }

  @Override
  public void visit(ast.exp.ArraySelect e)//correct->probably like this-> j = A[3];
  {
	  e.array.accept(this);
	  this.say("[");
	  e.index.accept(this);
	  this.say("]");
	  
  }

  @Override
  public void visit(ast.exp.Call e)//correct
  {
    e.exp.accept(this);
    this.say("." + e.id + "("); 
    

 

        if(!e.args.isEmpty())
    	{

            (e.args.getFirst()).accept(this);        	
    	    Iterator it= e.args.iterator();
            if(it.hasNext())//从第二个元素开始哦
    	    it.next();
            while(it.hasNext())
            {
    	     this.say(",");
    	     ((ast.exp.T)it.next()).accept(this);        
            }
    	}
    /*  
    for (ast.exp.T x : e.args) {   
      x.accept(this);  
      this.say(", ");   
    }
    */
    this.say(")");
    return;
  }

  @Override
  public void visit(ast.exp.False e)//correct
  {
	  this.say("false");
	  return;
  }

  @Override
  public void visit(ast.exp.Id e)//correct
  {
    this.say(e.id);
    return;
  }

  @Override
  public void visit(ast.exp.Length e)//correct
                                     //->find this instance from ../test/BinarySearch.java 
                                     //->int[] number ;
                                     //->right = number.length ;
  {
	  e.array.accept(this);
	  this.say(".length");
  }

  @Override
  public void visit(ast.exp.Lt e)//correct
  {
    e.left.accept(this);
    this.say(" < ");
    e.right.accept(this);
    return;
  }

  @Override
  public void visit(ast.exp.NewIntArray e)//correct->probably like this-> p = new int[89];
  {
	  this.say("new " + "int" + "[");
	  if(e.exp != null)
	  e.exp.accept(this);
	  this.say("]");
	  return;
  }

  @Override
  public void visit(ast.exp.NewObject e)//correct
  {
    this.say("new " + e.id + "()");
    return;
  }

  @Override
  public void visit(ast.exp.Not e)//correct
  {
	  this.say("!");
	  e.exp.accept(this);
	  return;
  }

  @Override
  public void visit(ast.exp.Num e)//correct
  {
    System.out.print(e.num);
    return;
  }

  @Override
  public void visit(ast.exp.Sub e)//correct
  {
    e.left.accept(this);
    this.say(" - ");
    e.right.accept(this);
    return;
  }

  @Override
  public void visit(ast.exp.This e)//correct
  {
    this.say("this");
    return;
  }

  @Override
  public void visit(ast.exp.Times e)//correct
  {
    e.left.accept(this);
    this.say(" * ");
    e.right.accept(this);
    return;
  }

  @Override
  public void visit(ast.exp.True e)//correct
  {
	  this.say("true");
	  return;
  }

  // statements
  @Override
  public void visit(ast.stm.Assign s)//correct
  {
    this.printSpaces();
    this.say(s.id + " = ");
    s.exp.accept(this);
    this.sayln(";");
    return;
  }

  @Override
  public void visit(ast.stm.AssignArray s)//correct-> probably like this -> ID[indexexp] = exp;-> A[5] = 5+9;
  {
	  this.printSpaces();
	  this.say(s.id);
	  this.say("[");
	  s.index.accept(this);
	  this.say("] = ");
	  s.exp.accept(this);
	  this.sayln(";");
	  return;
  }

  @Override
  public void visit(ast.stm.Block s)//correct
  {
	  /*
	  //test_correct-> like this
	    {
	        c = 33;
	        ca = 4;
	    }
      */
	    this.printSpaces();	    
	    this.sayln("{");
	    this.indent();
	    for (ast.stm.T stm : s.stms)
	        stm.accept(this);
	    this.unIndent();
	    this.printSpaces();
	    this.sayln("}");
	    this.sayln("");
  }

  @Override
  public void visit(ast.stm.If s)//correct
  {
	this.sayln("");//if语句前换个行先
    this.printSpaces();
    this.say("if (");
    s.condition.accept(this);
    this.sayln(")");
    this.indent();
    s.thenn.accept(this);
    this.unIndent();
    this.printSpaces();
    this.sayln("else");
    this.indent();
    s.elsee.accept(this);
    this.unIndent();
    return;
  }

  @Override
  public void visit(ast.stm.Print s)//correct
  {
    this.printSpaces();
    this.say("System.out.println (");
    s.exp.accept(this);
    this.sayln(");");
    return;
  }

  @Override
  public void visit(ast.stm.While s)//correct
  {
	    this.sayln("");
	    this.printSpaces();
	    this.say("while (");
	    s.condition.accept(this);
	    this.sayln(")");
	    this.indent();
	    s.body.accept(this);
	    this.unIndent();
	        
  }
  // type
  @Override
  public void visit(ast.type.Boolean t)//correct
  {
	  this.say("boolean");
	  return;
  }

  @Override
  public void visit(ast.type.Class t)//correct
  {
	  this.say(t.id);
	  return;
  }

  @Override
  public void visit(ast.type.Int t)//correct
  {
    this.say("int");
    return;
  }

  @Override
  public void visit(ast.type.IntArray t)//correct
  {
	    this.say("int[]");
	    return;
  }

  // dec
  @Override
  public void visit(ast.dec.Dec d)//correct
  {
	  d.type.accept(this);
	  this.say(" "+d.id);
  }

  // method
  @Override
  public void visit(ast.method.Method m)//correct
  {
    this.sayln("");//if语句前换个行先
    this.say("  public ");
    m.retType.accept(this);
    this.say(" " + m.id + "(");
    
    
    
    if(!m.formals.isEmpty())
    {
        ((ast.dec.Dec)m.formals.getFirst()).accept(this);
   
    Iterator it= m.formals.iterator();
    if(it.hasNext())//从第二个元素开始哦
    	it.next();
    while(it.hasNext()){
    	 this.say(",");
    	 ((ast.dec.Dec)it.next()).accept(this);        
                       }
    }
    
    this.sayln(")");
    this.sayln("  {");

    
 
      for (ast.dec.T d : m.locals) 
      {
      ast.dec.Dec dec = (ast.dec.Dec) d;
      this.say("    ");
      dec.type.accept(this);
      this.say(" " + dec.id + ";\n");                          
      }
    

    this.sayln("");
    for (ast.stm.T s : m.stms)
      s.accept(this);
    this.say("    return ");
    m.retExp.accept(this);
    this.sayln(";");
    this.sayln("  }");
    return;
  }

  // class
  @Override
  public void visit(ast.classs.Class c)//correct
  {
    this.say("class " + c.id);
    if (c.extendss != null)
      this.sayln(" extends " + c.extendss);
    else
      this.sayln("");

    this.sayln("{");

    for (ast.dec.T d : c.decs) {
      ast.dec.Dec dec = (ast.dec.Dec) d;
      this.say("  ");
      dec.type.accept(this);
      this.say(" ");
      this.sayln(dec.id + ";");
    }
    for (ast.method.T mthd : c.methods)
      mthd.accept(this);
    this.sayln("}");
    return;
  }

  // main class
  @Override
  public void visit(ast.mainClass.MainClass c)//correct
  {
    this.sayln("class " + c.id);
    this.sayln("{");
    this.sayln("  public static void main (String [] " + c.arg + ")");
    this.sayln("  {");


    for (ast.dec.T d : c.locals) {
        ast.dec.Dec dec = (ast.dec.Dec) d;
        this.say("    ");
        dec.type.accept(this);
        this.say(" " + dec.id + ";\n");
      }
      this.sayln("");
      for (ast.stm.T s : c.stms)
        s.accept(this);
   
    
    this.sayln("  }");
    this.sayln("}");
    return;
  }

  // program
  @Override
  public void visit(ast.program.Program p)//correct
  {
	 
    p.mainClass.accept(this);
    this.sayln("");
    for (ast.classs.T classs : p.classes) {
      classs.accept(this);
    }
    System.out.println("\n\n");
  }
}
