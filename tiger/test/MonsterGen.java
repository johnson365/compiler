public class MonsterGen
{
  public static void print(String s)
  {
    System.out.println (s);
  }
  
  public static void usage()
  {
    print ("Monster generator.\nUsage: java MonsterGen <num>");
    System.exit(1);
  }
  
  public static void main(String[] args)
  {
    if (args.length<1)
      usage();
    
    int num = 0;
    try{
      num = Integer.parseInt(args[0]);
    }
    catch (Exception e){
      System.out.println("Expects an integer");
      usage();
    }