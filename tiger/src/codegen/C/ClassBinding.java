package codegen.C;

public class ClassBinding
{
  public String extendss; // null for non-existing extends
  public boolean visited; // whether or not this class has been visited
  public java.util.LinkedList<Tuple> fields; // all fields    
  public java.util.ArrayList<Ftuple> methods; // all methods  

  public ClassBinding(String extendss)
  {
    this.extendss = extendss;
    this.visited = false;
    this.fields = new java.util.LinkedList<Tuple>();
    this.methods = new java.util.ArrayList<Ftuple>();
  }

  // put a single field
  public void put(String c, codegen.C.type.T type, String var)
  { 
    this.fields.add(new Tuple(c, type, var));
  }

  public void putm(String c, codegen.C.type.T ret,
			java.util.LinkedList<codegen.C.dec.T> args, String mthd) 
	{
		Ftuple t = new Ftuple(c, ret, args, mthd);
		this.methods.add(t);
		return;
	}

  public void put(Tuple t)
  {
    this.fields.add(t);
  }

  public void update(java.util.LinkedList<Tuple> fs)
  {
    this.fields = fs;
  }

  public void update(java.util.ArrayList<Ftuple> ms)
  {
    this.methods = ms;
  }



  @Override
  public String toString()
  {
    System.out.print("extends: ");
    if (this.extendss != null)
      System.out.println(this.extendss);
    else
      System.out.println("<>");
    System.out.println("\nfields:\n  ");
    System.out.println(fields.toString());
    System.out.println("\nmethods:\n  ");
    System.out.println(methods.toString());

    return "";
  }

}
