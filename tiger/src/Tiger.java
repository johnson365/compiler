
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackReader;

import lexer.Lexer;
import lexer.Token;
import lexer.Token.Kind;

import control.CommandLine;
import control.Control;

import parser.Parser;




public class Tiger
{

  
  
  public static void main(String[] args)
  {
	  long startMili=System.currentTimeMillis();//计时开始
	  
	  
	PushbackReader pushbackreader;
    Parser parser;
    
    
    // /////////////////////////////////////////////////////////
    // normal compilation phases.
    ast.program.T theAst = null;

   
    // ///////////////////////////////////////////////////////
    // handle command line arguments
    CommandLine cmd = new CommandLine();
    String fname = cmd.scan(args);
    if (fname == null) {
      cmd.usage();
      return;
    }
    //Control.fileName = fname;// lab4!!!!!!!!!!!!!!!!11
    
    
    
    // ///////////////////////////////////////////////////// this is just for test lexer
    // it would be helpful to be able to test the lexer
    // independently.
    if (control.Control.testlexer) {
      System.out.println("Testing the lexer. All tokens:");
      try {
    	pushbackreader = new PushbackReader(new FileReader("../test/"+fname));
        Lexer lexer = new Lexer(fname, pushbackreader);
        Token token = lexer.nextToken();  
        while (token.kind!=Kind.TOKEN_EOF){
          System.out.println(token.toString());
          token = lexer.nextToken();
        }
      System.out.println(token.toString());// print TOKEN_EOF
        
        pushbackreader.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
      System.exit(1);
    }//copyright by KIRA林泽南

    
   // if (control.Control.lex)
   // {
    	//System.out.println("Testing the lexer and the parser:\n");
	    try 
	    {
	    	pushbackreader = new PushbackReader(new FileReader("../test/"+fname));
	    	parser = new Parser(fname, pushbackreader);	 
	    	
	    	theAst = parser.parse();
	    	System.out.println("\nLexer and Parser well down,no fault...\n"); 	
	        pushbackreader.close();
	         
	  
	    }
        catch (Exception e) 
        {
	      e.printStackTrace();
	      System.exit(1);
        }
    //}
    
    // /////////////////////////////////////////////////////
    // to test the pretty printer on the "test/Fac.java" program
    if (control.Control.testFac) {
      System.out.println("Testing the Tiger compiler on Fac.java starting:");
      ast.PrettyPrintVisitor pp = new ast.PrettyPrintVisitor();
      ast.Fac.prog.accept(pp);

      // elaborate the given program, this step is necessary
      // for that it will annotate the AST with some
      // informations used by later phase.
      //elaborator.ElaboratorVisitor elab = new elaborator.ElaboratorVisitor();
      //ast.Fac.prog.accept(elab);

      // Compile this program to C.
      System.out.println("Translate the program to C");
      codegen.C.TranslateVisitor trans2C = new codegen.C.TranslateVisitor();
      // pass this visitor to the "Fac.java" program.
      ast.Fac.prog.accept(trans2C);
      codegen.C.program.T cast = trans2C.program;
      codegen.C.PrettyPrintVisitor ppc = new codegen.C.PrettyPrintVisitor();
      cast.accept(ppc);

      System.out.println("Testing the Tiger compiler on Fac.java finished.");
      System.exit(1);
    }
    

    
    
    // pretty printing the AST, if necessary
    if (control.Control.dumpAst) {
      ast.PrettyPrintVisitor pp = new ast.PrettyPrintVisitor();
      theAst.accept(pp);
      
      System.exit(1);
    }
    
     //elaborate the AST, report all possible errors.
    elaborator.ElaboratorVisitor elab = new elaborator.ElaboratorVisitor();
    theAst.accept(elab);
    
  
    
    // code generation
    switch (control.Control.codegen) {
    case Bytecode:
      codegen.bytecode.TranslateVisitor trans = new codegen.bytecode.TranslateVisitor();
      theAst.accept(trans);
      codegen.bytecode.program.T bytecodeAst = trans.program;
      codegen.bytecode.PrettyPrintVisitor ppbc = new codegen.bytecode.PrettyPrintVisitor();
      bytecodeAst.accept(ppbc);
      
      
      java.util.LinkedList<String> classesNamej = trans.classesNamej;
      
			for (String x : classesNamej) 
			{
				try {
					System.out.println("\ntry:java -jar ../jasmin.jar "+x+".j\n");

					String[] tempcmd = new String[] { "java", "-jar","../jasmin.jar", x+".j"};
					Process ps = Runtime.getRuntime().exec(tempcmd);

					java.io.BufferedReader br = new java.io.BufferedReader(
							new java.io.InputStreamReader(ps.getInputStream()));
					StringBuffer sb = new StringBuffer();
					String line;
					while ((line = br.readLine()) != null) {
						sb.append(line).append("\n");
					}
					String result = sb.toString();

					System.out.println(result);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		String firstclassName = classesNamej.getFirst();
			
  	 try {
  		System.out.println("try:java "+firstclassName+"\n");
  		
  		String[] tempcmd = new String[]{"java",firstclassName};
  		Process ps = Runtime.getRuntime().exec(tempcmd);

  		java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(ps.getInputStream()));
  		StringBuffer sb = new StringBuffer();
  		String line;
  		while ((line = br.readLine()) != null) {
  		sb.append(line).append("\n");
  		}
  		String result = sb.toString();

  		System.out.println(result);
  		} catch (Exception e) {
  		e.printStackTrace();
  		}
      
  	 
  	 
  	 
      break;
    case C:
      codegen.C.TranslateVisitor transC = new codegen.C.TranslateVisitor();
      theAst.accept(transC);
      codegen.C.program.T cAst = transC.program;//此时transC.program里面已经存放了 C的AST
      codegen.C.PrettyPrintVisitor ppc = new codegen.C.PrettyPrintVisitor();
      cAst.accept(ppc);
      
 	  System.out.println("a.c has been write into the file\n");

 	  
 	  
 	  //java Tiger ../test/TreeVisitor.java -codegen C
 	  //gcc a.c ../runtime/runtime.c
 	 try {
 		System.out.println("try:gcc a.c ../runtime/runtime.c\n");
 		
 		String[] tempcmd = new String[]{"gcc","a.c","../runtime/runtime.c"};
 		Process ps = Runtime.getRuntime().exec(tempcmd);

 		java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(ps.getInputStream()));
 		StringBuffer sb = new StringBuffer();
 		String line;
 		while ((line = br.readLine()) != null) {
 		sb.append(line).append("\n");
 		}
 		String result = sb.toString();

 		System.out.println(result);
 		} catch (Exception e) {
 		e.printStackTrace();
 		}
 	 
 	 try {
 		System.out.println("try:./a.out\n");
 		
 		String[] tempcmd = new String[]{"./a.out"};
 		Process ps = Runtime.getRuntime().exec(tempcmd);

 		java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(ps.getInputStream()));
 		StringBuffer sb = new StringBuffer();
 		String line;
 		while ((line = br.readLine()) != null) {
 		sb.append(line).append("\n");
 		}
 		String result = sb.toString();

 		System.out.println(result);
 		} catch (Exception e) {
 		e.printStackTrace();
 		}
 	//copyright by KIRA林泽南
      break;
    case Dalvik:
        codegen.dalvik.TranslateVisitor transDalvik = new codegen.dalvik.TranslateVisitor();
        theAst.accept(transDalvik);
        codegen.dalvik.program.T dalvikAst = transDalvik.program;
        codegen.dalvik.PrettyPrintVisitor ppDalvik = new codegen.dalvik.PrettyPrintVisitor();
        dalvikAst.accept(ppDalvik);
        break;
    case X86:
      // similar
      break;
    default:
      break;
    }
    
    // Lab3, exercise 6: add some glue code to
    // call gcc to compile the generated C or x86
    // file, or call java to run the bytecode file.
    // Your code:
    long endMili=System.currentTimeMillis();//计时结束
    
    System.out.println("cost time："+(endMili-startMili)+"ms");
    
    
    return;
  
  }
}
//copyright by KIRA林泽南

