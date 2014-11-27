package elaborator;


import java.util.Iterator;

import java.util.Set;

import util.Todo;

public class ClassTable
{
  // map each class name (a string), to the class bindings.
  private java.util.Hashtable<String, ClassBinding> table;

  public ClassTable()
  {
    this.table = new java.util.Hashtable<String, ClassBinding>();
  }

  // Duplication is not allowed
  public void put(String c, ClassBinding cb)
  {
    if (this.table.get(c) != null) {
      System.out.println("duplicated class: " + c);
      System.exit(1);
    }
    this.table.put(c, cb);
  }

  // put a field into this table
  // Duplication is not allowed
  public void put(String c, String id, ast.type.T type,int lineNum)
  {                                     
    ClassBinding cb = this.table.get(c);
    cb.put(id, type,lineNum);
    return;
  }

  // put a method into this table
  // Duplication is not allowed.
  // Also note that MiniJava does NOT allow overloading.
  public void put(String c, String id, MethodType type)
  {
    ClassBinding cb = this.table.get(c);
    cb.put(id, type);
    return;
  }

  // return null for non-existing class
  public ClassBinding get(String className)  
  {
    return this.table.get(className);
  }

  // get type of some field
  // return null for non-existing field.
  public ast.type.T get(String className, String xid)
	{
		ClassBinding cb = this.table.get(className);
		ast.type.T type = null;
		if (cb.fields.get(xid) != null) {
			type = cb.fields.get(xid).type;
			cb.fields.get(xid).use = 1;
			
		}
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
		while (type == null) { // search all parent classes until found or fail
			if (cb.extendss == null)
				return type;

			if (this.table.get(cb.extendss) != null) 
			{
				cb = this.table.get(cb.extendss);

				if (cb.fields.get(xid) != null)
				{ 
					type = cb.fields.get(xid).type;
					
					cb.fields.get(xid).use = 1;
				}
			}
			else
			{
				return null;
			}

		}
		return type;
	}

	// get type of some method
  // return null for non-existing method
  public MethodType getm(String className, String mid)//classname->classbinding->methods->    get(methodid)->methodtype
  {
    ClassBinding cb = this.table.get(className);
    MethodType type = cb.methods.get(mid);
    while (type == null) { // search all parent classes until found or fail
      if (cb.extendss == null)
        return type;

      cb = this.table.get(cb.extendss);
      type = cb.methods.get(mid);
    }
    return type;
  }

	public void dump()
	{
		// new Todo();

		System.out.println("classTable:");
		if (!this.table.isEmpty()) {
			
			Set<String> classset = this.table.keySet();
			for (Iterator<String> iter = classset.iterator(); iter.hasNext();) {
			
				String tempiter = iter.next();

				System.out.println(tempiter);

				System.out.println(this.table.get(tempiter));
			}

		}

	}
	
	public void warning()
	{
	
		Iterator<String> iter = this.table.keySet().iterator();
		   while(iter.hasNext())
		   {
			   String s = (String) iter.next();
			   Iterator<String> it1 = this.table.get(s).fields.keySet().iterator(); 
			   while(it1.hasNext())
			   {
				   String s1 = (String)it1.next();
				   if(this.table.get(s).fields.get(s1).use == 0)
					   System.out.println("warning: variable ‘"+s1+"‘ has never be used..."+"at lineNumber: "+this.table.get(s).fields.get(s1).lineNum);
			   
			   }
	        }
	
	}
	
	

  @Override
  public String toString()
  {
    return this.table.toString();
  }
}
