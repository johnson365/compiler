package ast;

import ast.dec.Dec;
import ast.exp.NewIntArray;

public class Fac
{
  // Lab2, exercise 2: read the following code and make
  // sure you understand how the sample program "test/Fac.java" is represented.

  // /////////////////////////////////////////////////////
  // To represent the "Fac.java" program in memory manually
  // this is for demonstration purpose only, and
  // no one would want to do this in reality (boring and error-prone).
/*
class Factorial { 
	public static void main(String[] a) {
        System.out.println(new Fac().ComputeFac(10));
    }
}
class Fac {
    public int ComputeFac(int num) {
        int num_aux;
        if (num < 1)
            num_aux = 1;
        else
            num_aux = num * (this.ComputeFac(num-1));
        return num_aux;
    }
}
*/
	
  // // main class: "Factorial"
  static ast.mainClass.MainClass factorial = new ast.mainClass.MainClass(
      "Factorial", "a", 
      new util.Flist<ast.dec.T>().addAll(),
      new util.Flist<ast.stm.T>().addAll(
      new ast.stm.Print(new ast.exp.Call(
          new ast.exp.NewObject("Fac"), "ComputeFac",
          new util.Flist<ast.exp.T>().addAll(new ast.exp.Num(10))))
                                            )
                                           
      );

  // // class "Fac"
  static ast.classs.Class fac = new ast.classs.Class("Fac", null,
      new util.Flist<ast.dec.T>().addAll(),
      new util.Flist<ast.method.T>().addAll(new ast.method.Method(
          new ast.type.Int(), "ComputeFac", new util.Flist<ast.dec.T>()
              .addAll(new ast.dec.Dec(new ast.type.Int(), "num")),
          new util.Flist<ast.dec.T>().addAll(new ast.dec.Dec(
              new ast.type.Int(), "num_aux")), new util.Flist<ast.stm.T>()
              .addAll(new ast.stm.If(new ast.exp.Lt(new ast.exp.Id("num"),
                  new ast.exp.Num(1)), new ast.stm.Assign("num_aux",
                  new ast.exp.Num(1)), new ast.stm.Assign("num_aux",
                  new ast.exp.Times(new ast.exp.Id("num"), new ast.exp.Call(
                      new ast.exp.This(), "ComputeFac",
                      new util.Flist<ast.exp.T>().addAll(new ast.exp.Sub(
                          new ast.exp.Id("num"), new ast.exp.Num(1)))))))),
          new ast.exp.Id("num_aux"))));
  
  // program
  //static对象，所以Tiger里面还可以调用之
  public static ast.program.Program prog = new ast.program.Program(factorial,
      new util.Flist<ast.classs.T>().addAll(fac));

  // Lab2, exercise 2: you should write some code to
  // represent the program "test/Sum.java".
  // Your code here:
/* 
  class Sum { 
		public static void main(String[] a) {
	        System.out.println(new Doit().doit(101));
	    }
	}

	class Doit {
	    public int doit(int n) {
	        int sum;
	        int i;
	        
	        i = 0;
	        sum = 0;
	        while (i<n)
	        	sum = sum + i;
	        return sum;
	    }
	}
	*/

/*
  // // main class: "Sum"
  static ast.mainClass.MainClass sum = new ast.mainClass.MainClass(
      "Sum", "a", new ast.stm.Print(new ast.exp.Call(
          new ast.exp.NewObject("Doit"), "doit",
          new util.Flist<ast.exp.T>().addAll(new ast.exp.Num(101)))));
  
  // // class "Doit"
  static ast.classs.Class doit = new ast.classs.Class("Doit", null,
      new util.Flist<ast.dec.T>().addAll(),
      new util.Flist<ast.method.T>().addAll(
    		  new ast.method.Method(new ast.type.Int(), 
    				  "doit", 
    				  new util.Flist<ast.dec.T>().addAll(new ast.dec.Dec(new ast.type.Int(), "n")),
          new util.Flist<ast.dec.T>().addAll(
        		  new ast.dec.Dec(new ast.type.Int(), "sum"),
        		  new ast.dec.Dec(new ast.type.Int(), "i")), 
        		  new util.Flist<ast.stm.T>().addAll(
        				  new ast.stm.Assign("i", new ast.exp.Num(0)),
        				  new ast.stm.Assign("sum", new ast.exp.Num(0))
              ,new ast.stm.While(new ast.exp.Lt(new ast.exp.Id("i"),new ast.exp.Id("n")),new ast.stm.Assign("sum",new ast.exp.Add(new ast.exp.Id("sum"),new ast.exp.Id("i"))))),
          new ast.exp.Id("sum"))));
  
  
  // program
  //static对象，所以Tiger里面还可以调用之
  public static ast.program.Program progsum = new ast.program.Program(sum,
      new util.Flist<ast.classs.T>().addAll(doit));
  
  */
  
 
//Test_All_PrettyPrintVisiter_print->correct already
/*
class Sum
{
  public static void main (String [] a)
  {

    System.out.println (new Doit().doit(101,33,86));
  }
}

class Doit
{
  int[] aa;

  public int doit(int n,boolean b,boolean c)
  {
    base sum;
    int i;

    {
      c = 33;
      ca = 4;
    }

    i = 0;
    sum = 0;
    i = new int[78];
    j = A[3];
    A[7] = 7 + 9;
    num = number.length;

    while (i && n)
      sum = sum * i;
    return sum;
  }
}

*/
 static ast.mainClass.MainClass sum = new ast.mainClass.MainClass(
      "Sum", "a",
      new util.Flist<ast.dec.T>().addAll(),
      new util.Flist<ast.stm.T>().addAll(
      new ast.stm.Print(new ast.exp.Call(
          new ast.exp.NewObject("Doit"), "doit",
          new util.Flist<ast.exp.T>().addAll(new ast.exp.Num(101),new ast.exp.Num(33),new ast.exp.Num(86))))
                                        )
    		  );
  
  // // class "Doit"
  static ast.classs.Class doit = new ast.classs.Class("Doit", null,
      new util.Flist<ast.dec.T>().addAll(
    		  new ast.dec.Dec(new ast.type.IntArray(),"aa")
    		  ),
      new util.Flist<ast.method.T>().addAll(
    		  new ast.method.Method(new ast.type.Int(), 
    				  "doit", 
    				  new util.Flist<ast.dec.T>().addAll(new ast.dec.Dec(new ast.type.Int(), "n"),new ast.dec.Dec(new ast.type.Boolean(),"b"),new ast.dec.Dec(new ast.type.Boolean(),"c")),
          new util.Flist<ast.dec.T>().addAll(
        		  new ast.dec.Dec(new ast.type.Class("base"), "sum"),
        		  new ast.dec.Dec(new ast.type.Int(), "i")), 
        		  new util.Flist<ast.stm.T>().addAll(new ast.stm.Block(new util.Flist<ast.stm.T>().addAll(new ast.stm.Assign("c",new ast.exp.Num(33)),new ast.stm.Assign("ca", new ast.exp.Num(4)))),
        				  new ast.stm.Assign("i", new ast.exp.Num(0)),
        				  new ast.stm.Assign("sum", new ast.exp.Num(0)),
        				  new ast.stm.Assign("i",new ast.exp.NewIntArray(new ast.exp.Num(78))),
        				  new ast.stm.Assign("j",new ast.exp.ArraySelect(new ast.exp.Id("A"),new ast.exp.Num(3))),
        				  new ast.stm.AssignArray("A",new ast.exp.Num(7),new ast.exp.Add(new ast.exp.Num(7),new ast.exp.Num(9))),
        				  new ast.stm.Assign("num",new ast.exp.Length(new ast.exp.Id("number"))),
              new ast.stm.While(new ast.exp.And(new ast.exp.Id("i"),new ast.exp.Id("n")),new ast.stm.Assign("sum",new ast.exp.Times(new ast.exp.Id("sum"),new ast.exp.Id("i"))))),
          new ast.exp.Id("sum"))));
  
  
  // program
  //static对象，所以Tiger里面还可以调用之
  public static ast.program.Program progsum = new ast.program.Program(sum,
      new util.Flist<ast.classs.T>().addAll(doit));
  
  
 
  
  
}
