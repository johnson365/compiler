package ast.exp;

public class ArraySelect extends T
{
  public T array;
  public T index;
  
  public int indexlineNum;

  public ArraySelect(T array, T index)
  {
    this.array = array;
    this.index = index;
    
    this.indexlineNum = 0;//防止出现莫名其妙的错误
  }
  
  public ArraySelect(T array, T index,int indexlineNum)
  {
    this.array = array;
    this.index = index;
    
    this.indexlineNum = indexlineNum;
  }


  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
    return;
  }
}
