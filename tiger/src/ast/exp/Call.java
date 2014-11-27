package ast.exp;


public class Call extends T
{
  public T exp;
  public String id;
  public java.util.LinkedList<T> args;//实参!!!!!!!!!!!!!!!
  public String type; // type of first field "exp"


  //lab4
  // the following two fields records the argument types
  // and return type for the method being called, all these
  // types are declared types, but not actural types of the
  // arguments.
  // Both these two fields are filled in by the elaborator.
  public java.util.LinkedList<ast.type.T> at; // arg's type -
  public ast.type.T rt;//

  
  

  public int atomExplineNum;//
  public int argsNumCalllineNum;
  
  
  public Call(T exp, String id, java.util.LinkedList<T> args)
  {
    this.exp = exp;
    this.id = id;
    this.args = args;


    
    
    this.type = null;
    this.at = null;
    this.rt = null;

    
    this.atomExplineNum = 0;//防止出现莫名其妙的错误
    this.argsNumCalllineNum = 0;
  }

  public Call(T exp, String id, java.util.LinkedList<T> args,int atomExplineNum,int argsNumCalllineNum)
  {
    this.exp = exp;
    this.id = id;
    this.args = args;
    this.type = null;
    
    
    this.atomExplineNum = atomExplineNum;
    this.argsNumCalllineNum = argsNumCalllineNum;


  }

  
  
  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
    return;
  }
}
