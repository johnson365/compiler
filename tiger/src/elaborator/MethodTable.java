package elaborator;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import util.Todo;

public class MethodTable
{

	private Hashtable<String,Hashtable<String,Hashtable<String,MethodTypeuse>>> table;
  
	
	  class MethodTypeuse{
		 public MethodTypeuse(ast.type.T type)
		 {
			 this.type = type;
			 this.use = 0;
			 this.lineNum = 0;
			 
		 }
		 public MethodTypeuse(ast.type.T type,int lineNum)
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
	
	
	
	
  public MethodTable()
  {
    this.table = new Hashtable<String,Hashtable<String,Hashtable<String,MethodTypeuse>>>();
  }
  
  // Duplication is not allowed
  public void put(String currentClass,String currentMethod,Hashtable<String,Hashtable<String,MethodTypeuse>> map)
  {

 
    if (this.table.get(currentClass) != null) {
     
    	;
    }
    else
    this.table.put(currentClass, map);
    

 
    this.table.get(currentClass).put(currentMethod, new Hashtable<String,MethodTypeuse>());
  }
  
  

  // Duplication is not allowed
  public void put(String currentClass,String currentMethod,java.util.LinkedList<ast.dec.T> formals,
      java.util.LinkedList<ast.dec.T> locals)
  {


			for (ast.dec.T dec : formals) {
				ast.dec.Dec decc = (ast.dec.Dec) dec;
				if (this.table.get(decc.id) != null) {
					System.out.println("duplicated parameter: " + decc.id);
					System.exit(1);
				}
				// this.table.put(decc.id, decc.type);
				this.table.get(currentClass).get(currentMethod).put(decc.id,new MethodTypeuse(decc.type,decc.pair.get(decc.id)));
			}
		//}

		for (ast.dec.T dec : locals) {
			ast.dec.Dec decc = (ast.dec.Dec) dec;
			if (this.table.get(decc.id) != null) {
				System.out.println("duplicated variable: " + decc.id);
				System.exit(1);
			}
			// this.table.put(decc.id, decc.type);
			this.table.get(currentClass).get(currentMethod).put(decc.id,new MethodTypeuse(decc.type,decc.pair.get(decc.id)));
		}

  }

  // return null for non-existing keys
  public ast.type.T get(String currentClass,String currentMethod,String id) 
  {

	  
	  Hashtable<String,MethodTypeuse> map1 = this.table.get(currentClass).get(currentMethod);
	  
	  ast.type.T type = null;
			 if(!map1.isEmpty())
 {
			if (map1.get(id) != null) 
			{ 
				type = map1.get(id).type;
				
				map1.get(id).use = 1;
			}
		}

	  return type;
  }

  public void dump()
  {
    //new Todo();
	  System.out.println("methodTable:");
	
	  System.out.println(this.table);
	  System.out.println();
	  
	  
  }

	public void warning()// Hashtable<String,Hashtable<String,Hashtable<String,MethodTypeuse>>>
	{
		Iterator<String> iter = this.table.keySet().iterator();
		 while(iter.hasNext())
		 {
			 String s1 = (String)iter.next();
			   Iterator<String> it1 = this.table.get(s1).keySet().iterator();
			   while(it1.hasNext())
			   {
				   String s2 = (String)it1.next();
				   Iterator<String> it2 = this.table.get(s1).get(s2).keySet().iterator();
				   while(it2.hasNext())
				   {
					   String s3 = (String)it2.next();
					   if(this.table.get(s1).get(s2).get(s3).use == 0)
						   System.out.println("warning: variable '"+s3+"' has never be used..."+"at lineNumber: "+this.table.get(s1).get(s2).get(s3).lineNum);
					   
				   }

					  
				   
				   
			   }
			 
		 
		 }
		
		
	}
  
  
  
  @Override
  public String toString()
  {
    return this.table.toString();
  }
}
