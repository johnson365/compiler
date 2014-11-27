package codegen.C;

public class Tuple

{
  public String classs; // name of the class
  public codegen.C.type.T type; // type of the field
  public String id; // name of the field or method

  public Tuple(String classs, codegen.C.type.T type, String id)
  {
    this.classs = classs;
    this.type = type;
    this.id = id;
  }

  @Override

  public boolean equals(Object t)
  {
    if (t == null)
      return false;

    if (!(t instanceof Tuple))
      return false;

    return this.id.equals(((Tuple) t).id);
  }

}
