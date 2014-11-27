package elaborator;

import java.util.Hashtable;

public class ClassBinding
{
  public String extendss; // null for non-existing extends  !!!!!!!!!!!其父类的名字
  public Hashtable<String,Typeuse> fields;   
  public Hashtable<String, MethodType> methods;  

  
  class Typeuse{
	 public Typeuse(ast.type.T type)
	 {
		 this.type = type;
		 this.use = 0;
		 this.lineNum = 0;
		 
		 
	 }
	 public Typeuse(ast.type.T type,int lineNum)
	 {
		 this.type = type;
		 this.use = 0;
		 this.lineNum = lineNum;
		 
	 }
	 public ast.type.T type;
	 public int use;
	 public int lineNum;
	  @Override
	  public String toString()
	  {
		 
		  return type.toString();
	  }
  }
  
  public ClassBinding(String extendss)
  {
    this.extendss = extendss;
    this.fields = new Hashtable<String,Typeuse>();
    this.methods = new Hashtable<String, MethodType>();
  }

  public ClassBinding(String extendss,
      Hashtable<String,Typeuse> fields,
      Hashtable<String, MethodType> methods)
  {
    this.extendss = extendss;
    this.fields = fields;
    this.methods = methods;
  }

  public void put(String xid, ast.type.T type,int lineNum)//在classbinding中插入dec-> map<decname,dectype>
  {
    if (this.fields.get(xid) != null) {    // Duplication is not allowed
      System.out.println("duplicated class field: " + xid);
      System.exit(1);
    }

    this.fields.put(xid,new Typeuse(type,lineNum));
  }

  public void put(String mid, MethodType mt)//在classbinding中插入method-> map<methodname,methodtype>
  {
    if (this.methods.get(mid) != null) {  //Duplication is not allowed
      System.out.println("duplicated class method: " + mid);
      System.exit(1);
    }
    this.methods.put(mid, mt);
  }

  @Override
  public String toString()
  {
    System.out.print("extends: ");
    if (this.extendss != null)
      System.out.println(this.extendss);
    else
      System.out.println("<>");
    System.out.println("fields:  ");
    System.out.println(fields.toString());
    System.out.println("methods:  ");
    System.out.println(methods.toString());

    return "";
  }

}
