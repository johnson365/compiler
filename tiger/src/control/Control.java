package control;

public class Control
{
  // source file
  public static String fileName = null;
  
  
  
  //注意，在elabrator里面，如果对于生成 java bytecode，则需要argsty.set(i, dec.type);
                          //对于生成 C，则不需要哪句话，所以要判断下。操

  
  

  // compiler testing and debugging
  public static boolean testlexer = false;
  public static boolean testFac = false;

  // lexer and parser
  public static boolean lex = false;

  // ast
  public static boolean dumpAst = false;

  // elaborator
  public static boolean elabClassTable = false;
  public static boolean elabMethodTable = false;

  // code generator
  public static String outputName = null;

  public enum Codegen_Kind_t {
    Bytecode, C, Dalvik, X86
  }

  public static Codegen_Kind_t codegen = Codegen_Kind_t.C;

}
