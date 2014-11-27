package parser;

import java.util.Map;

import ast.exp.ArraySelect;
import ast.stm.Assign;
import ast.stm.Block;
import ast.stm.T;
import ast.type.IntArray;
import lexer.Lexer;
import lexer.Token;
import lexer.Token.Kind;




public class Parser
{
  Lexer lexer;
  Token current;
  Token precurrent;
  
  Token unreadtoken;
  int unreadtokentag;
  
  String errorMessage;

  public Parser(String fname, java.io.PushbackReader pushbackreader)
  {
    lexer = new Lexer(fname, pushbackreader);
    
    precurrent = current;
    
    unreadtoken = current;//init
    unreadtokentag = 1;//init
    
 
    
    
    current = lexer.nextToken();

    	while(current.kind == Kind.TOKEN_COMMENTLINE || current.kind == Kind.TOKEN_COMMENTBLOCK)
    	{
    		precurrent = current;//记录上一个current
    		current = lexer.nextToken();
    	}
  }
  
  // /////////////////////////////////////////////
  // utility methods to connect the lexer
  // and the parser.

  private void advance()
  {
	
	if(current.kind != Kind.TOKEN_EOF && unreadtokentag == 1 )
    {
		precurrent = current;//记录上一个current
		current = lexer.nextToken();//
	   	while(current.kind == Kind.TOKEN_COMMENTLINE || current.kind == Kind.TOKEN_COMMENTBLOCK)
    	{
    		precurrent = current;//记录上一个current
    		current = lexer.nextToken();
    	}
    }
	else
	{
		if(current.kind == Kind.TOKEN_EOF)
			;
		else
		{
			precurrent = current;
			current = unreadtoken;
			unreadtokentag = 1;
			return;
		}
	}

    	
  }

  private void eatToken(Kind kind)
  {
    if (kind == current.kind)
      advance();
    else {
      System.out.println("Syntax error: parsing aborting..."+"at LineNumber:"+ current.lineNum);
      System.out.println("Expects: " + kind.toString());
      System.out.println("But got: " + current.kind.toString()+"\n");
      System.exit(1);
    }
  }

  private void error(String errorMessage)
  {
    System.out.println("\nSyntax error: parsing aborting..."+"at LineNumber:"+ precurrent.lineNum+"\nReason:"+errorMessage);
    System.out.println("FileName:"+lexer.Getfname());  
    System.out.println("FirstErrorTokenKind:"+precurrent.kind);
    System.out.println("LineNumber:"+ precurrent.lineNum);
    System.out.println("Lexeme:"+precurrent.lexeme+"\n");
    System.exit(1);

    return;
  }

  // ////////////////////////////////////////////////////////////
  // below are method for parsing.

  // A bunch of parsing methods to parse expressions. The messy
  // parts are to deal with precedence and associativity.

  // ExpList -> Exp ExpRest*
  // ->
  // ExpRest -> , Exp

private java.util.LinkedList<ast.exp.T> parseExpList()   
  {
	  
	  
	  java.util.LinkedList<ast.exp.T> explists = new java.util.LinkedList<ast.exp.T>();
	  ast.exp.T exp = null;
	  

    exp = parseExp();

    explists.add(exp);
    while (current.kind == Kind.TOKEN_COMMER) {
      advance();
      exp = parseExp();
      explists.add(exp);
    }
    
    return explists;
  }

  // AtomExp -> (exp)
  // -> INTEGER_LITERAL
  // -> true
  // -> false
  // -> this
  // -> id
  // -> new int [exp]     ->i = new int[i];
  // -> new id ()         ->c =new boject();



  private ast.exp.T parseAtomExp() 
  {
    switch (current.kind) {
    case TOKEN_LPAREN:
      advance();
      if(current.kind == Kind.TOKEN_RPAREN)
    	  error("there must be any expression in the ()");
      ast.exp.T parenexp = null;
      parenexp = parseExp();
      eatToken(Kind.TOKEN_RPAREN);
      return new ast.exp.Paren(parenexp);
    case TOKEN_NUM:
      advance();
      int num = Integer.parseInt(precurrent.lexeme);     
      return new ast.exp.Num(num);
    case TOKEN_TRUE:
      advance();
      return new ast.exp.True();     
    case TOKEN_FALSE: //我擦，少个FALSE，无语
        advance();
        return new ast.exp.False();
    case TOKEN_THIS:
      advance();
      return new ast.exp.This();
    case TOKEN_ID:
      advance();
      String id = precurrent.lexeme;//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
      return new ast.exp.Id(id,precurrent.lineNum);
    case TOKEN_NEW: 
    {
      advance();
      ast.exp.T exp = null;
      
      if(current.kind == Kind.TOKEN_INT)// like this-> id = new int[exp]
      { 
        advance();
        eatToken(Kind.TOKEN_LBRACK);
        if(current.kind == Kind.TOKEN_RBRACK)
        {
        	error("Variable must provide either dimension expressions or an array initializer");
        }   
        int newintarraylineNum = current.lineNum;
        exp = parseExp();
        eatToken(Kind.TOKEN_RBRACK);
        return new ast.exp.NewIntArray(exp,newintarraylineNum);
      }
      else if(current.kind == Kind.TOKEN_ID)// like this-> new object()
      { 
    	advance();
        String str = precurrent.lexeme;
        eatToken(Kind.TOKEN_LPAREN);
        eatToken(Kind.TOKEN_RPAREN);
        return new ast.exp.NewObject(str);
 
      }
    }
    default:
      error("Can not tackle this Expression...");
      return null;
    }
  }

  // NotExp -> AtomExp
  // -> AtomExp .id (expList)       //我操，这里才是 函数调用->Call
  // -> AtomExp [exp]               //我操，这里才是 数组-> aa[]->ArraySelect
  // -> AtomExp .length             //我操，这里才是 exp.length->Length
  private ast.exp.T parseNotExp()
  {
	ast.exp.T atomexp = null;
	int atomExplineNum = current.lineNum;
	 
	atomexp = parseAtomExp();	

	
    while (current.kind == Kind.TOKEN_DOT || current.kind == Kind.TOKEN_LBRACK) {
      if (current.kind == Kind.TOKEN_DOT) //length
      {
        advance();
        if (current.kind == Kind.TOKEN_LENGTH) // -> AtomExp .length
        {    	
            advance();
          return new ast.exp.Length(atomexp);
        }
 
       
        int argsNumCalllineNum = 0;
        
        eatToken(Kind.TOKEN_ID); 
        
        if(current.kind != Kind.TOKEN_LPAREN) 
        {
        	return new ast.exp.Id(precurrent.lexeme,precurrent.lineNum);
        }
        
        
        String id = precurrent.lexeme;      
        java.util.LinkedList<ast.exp.T> explists = new java.util.LinkedList<ast.exp.T>();
        
        eatToken(Kind.TOKEN_LPAREN);
        argsNumCalllineNum = precurrent.lineNum;//
        
        if(current.kind != Kind.TOKEN_RPAREN)
        explists = parseExpList();
        eatToken(Kind.TOKEN_RPAREN);

        return new ast.exp.Call(atomexp, id, explists,atomExplineNum,argsNumCalllineNum);
      } 
      else   
      {
    	ast.exp.T indexexp = null;
        advance();
        int templineNum = current.lineNum;
        indexexp = parseExp();
        eatToken(Kind.TOKEN_RBRACK);
        
        return new ast.exp.ArraySelect(atomexp, indexexp,templineNum);
        
      }
    }
    return atomexp;
  }

  // TimesExp -> ! TimesExp
  // -> NotExp
  private ast.exp.T parseTimesExp()
  {
	  ast.exp.T notexp = null;
    if(current.kind == Kind.TOKEN_NOT) {
      advance();
      int notlineNum = current.lineNum;
      notexp = parseNotExp();
      return new ast.exp.Not(notexp,notlineNum);
    }
    
    
    return parseNotExp();
  }

  // AddSubExp -> TimesExp * TimesExp
  // -> TimesExp
  private ast.exp.T parseAddSubExp()
  {
	  
	  ast.exp.T leftexp = null;
	  ast.exp.T rightexp = null;
	  leftexp = parseTimesExp();
	  
	  int timeslineNum = 0;
	  
    if(current.kind == Kind.TOKEN_TIMES) 
    {
    	timeslineNum = current.lineNum;
     	  advance(); 
     	 rightexp = parseTimesExp();
     	  return new ast.exp.Times(leftexp, rightexp,timeslineNum);
    
    }
    return leftexp;
  }

  // LtExp -> AddSubExp + AddSubExp
  // -> AddSubExp - AddSubExp
  // -> AddSubExp
  private ast.exp.T parseLtExp()
  {
	  ast.exp.T leftexp = null;
	  ast.exp.T rightexp = null;
	  
	 int leftexplineNum = 0;
	 int addrightexplineNum = 0;
	 int subrightexplineNum = 0;
	 
	 leftexplineNum = current.lineNum;
	leftexp = parseAddSubExp();
	
    if(current.kind == Kind.TOKEN_ADD || current.kind == Kind.TOKEN_SUB) 
    {
        if(current.kind == Kind.TOKEN_ADD)    	
    	{        	
    	  advance(); 
    	  addrightexplineNum = current.lineNum;
    	  rightexp = parseAddSubExp();
    	  return new ast.exp.Add(leftexp, rightexp,leftexplineNum,addrightexplineNum);
    	}
        else if(current.kind == Kind.TOKEN_SUB) 
        {         
      	  advance(); 
          subrightexplineNum = current.lineNum;
      	  rightexp = parseAddSubExp();
      	  return new ast.exp.Sub(leftexp, rightexp,leftexplineNum,subrightexplineNum);       
        }      
    }
    return leftexp;
   
  }

  // AndExp -> LtExp < LtExp
  // -> LtExp
  private ast.exp.T parseAndExp() 
  {
	  ast.exp.T leftexp = null;
	  ast.exp.T rightexp = null;
	  
	  int ltlineNum = 0;
	  leftexp = parseLtExp();
	    
    //支持 < <= > >= == !=
    if(current.kind == Kind.TOKEN_LT) {   	
      ltlineNum = current.lineNum;
      advance();
      rightexp = parseLtExp();    
      return new ast.exp.Lt(leftexp,rightexp,ltlineNum);
    }

    return leftexp;

  }

  // Exp -> AndExp && AndExp
  // -> AndExp
  private ast.exp.T parseExp() 
  {
	  ast.exp.T leftexp = null;
	  ast.exp.T rightexp = null;
	  int andleftlineNum = 0;
	  int andrightlineNum = 0;
	  
	  andleftlineNum = current.lineNum;
	  leftexp = parseAndExp();
    if(current.kind == Kind.TOKEN_AND) { 
      advance();
      andrightlineNum = current.lineNum;
      rightexp = parseAndExp();
      return new ast.exp.And(leftexp,rightexp,andleftlineNum,andrightlineNum);
    }
   
    return leftexp;   
  }

  // Statement -> { Statement* }
  // -> if ( Exp ) Statement else Statement
  // -> while ( Exp ) Statement
  // -> System.out.println ( Exp ) ;
  // -> id = Exp ;                         ->Assign
  // -> id [ Exp ]= Exp ;                  ->AssignArray

  private ast.stm.T parseStatement()
  {
	  
    // Lab1. Exercise 4: Fill in the missing code
    // to parse a statement.
   // new util.Todo();
	  if(current.kind == Kind.TOKEN_LBRACE)
	  {
		  java.util.LinkedList<ast.stm.T> blockstms = new java.util.LinkedList<ast.stm.T>();
		  
		  eatToken(Kind.TOKEN_LBRACE);// {
		  blockstms = parseStatements();
		  eatToken(Kind.TOKEN_RBRACE);// } //别忘记了，害我找半天
		  
		  return new ast.stm.Block(blockstms);
	  }		  
	  else if(current.kind == Kind.TOKEN_IF) //if
	  {
		  ast.exp.T ifcondition = null;
		  T ifthenn = null;
		  T ifelsee = null;
		  
		  int ifconditionlineNum = 0;
		  
		  eatToken(Kind.TOKEN_IF);
		  eatToken(Kind.TOKEN_LPAREN);
		  
		  ifconditionlineNum = current.lineNum;
		  
		  ifcondition = parseExp();//解析表达式
		  eatToken(Kind.TOKEN_RPAREN);
		  ifthenn = parseStatement();//解析语句
		  if(current.kind == Kind.TOKEN_ELSE)
		  {
			  eatToken(Kind.TOKEN_ELSE);    
			  ifelsee = parseStatement();//解析语句
          }
		  return new ast.stm.If(ifcondition,ifthenn,ifelsee,ifconditionlineNum);
	  }
	  else if(current.kind == Kind.TOKEN_WHILE)//while
	  {
		  ast.exp.T whilecondition = null;
		  T whilebody = null;
		  
		  int whileconditionlineNum = 0;
		  
		  eatToken(Kind.TOKEN_WHILE);
		  eatToken(Kind.TOKEN_LPAREN);
		  
		  whileconditionlineNum = current.lineNum;//注意，指向表达式的第一个token
		  
		  whilecondition = parseExp();//解析表达式
		  eatToken(Kind.TOKEN_RPAREN);
		  whilebody = parseStatement();//解析语句
		  return new ast.stm.While(whilecondition,whilebody,whileconditionlineNum);
	  }
	  else if(current.kind == Kind.TOKEN_SYSTEM)//print
	  {
		  ast.exp.T printexp = null;
		  
		  int printlineNum = 0;
		  
		  eatToken(Kind.TOKEN_SYSTEM);   //system
		  eatToken(Kind.TOKEN_DOT);      //.   
		  eatToken(Kind.TOKEN_OUT);      //out
		  eatToken(Kind.TOKEN_DOT);      //.
		  eatToken(Kind.TOKEN_PRINTLN);  //println
		  eatToken(Kind.TOKEN_LPAREN);   //(
		  if(current.kind != Kind.TOKEN_RPAREN)
		  {
			  printlineNum = current.lineNum;
			  printexp = parseExp();//解析表达式
		  }
		  else
			  printlineNum = precurrent.lineNum;
		 
		 		  
		  eatToken(Kind.TOKEN_RPAREN);   //)	  		  
		  eatToken(Kind.TOKEN_SEMI);     //; 注意一定要多读一个分号
		  
		  return new ast.stm.Print(printexp,printlineNum);
	  }
	  else if(current.kind == Kind.TOKEN_ID ||current.kind == Kind.TOKEN_THIS)
	  {
		  if(current.kind == Kind.TOKEN_THIS)//
		  {
			  eatToken(Kind.TOKEN_THIS);
			  eatToken(Kind.TOKEN_DOT);
		  }
	   String id = null;
	   eatToken(Kind.TOKEN_ID);
	   id = precurrent.lexeme;
	   
	   if(current.kind == Kind.TOKEN_ASSIGN)//Assign -> id = exp;
	  {
		   
		   int assignlineNum = current.lineNum;// AssignlineNum in AST
		   
		      ast.exp.T assignexp = null;

			  eatToken(Kind.TOKEN_ASSIGN);
			  assignexp = parseExp();//解析表达式			  
			  eatToken(Kind.TOKEN_SEMI);     //; 注意一定要多读一个分号
			  
			  return new ast.stm.Assign(id, assignexp,assignlineNum);
	  }
	  else if(current.kind == Kind.TOKEN_LBRACK)//AssignArray -> id[index] = exp;
	  {
		  ast.exp.T indexexp = null;
		  ast.exp.T exp = null;
		  
		  int assignArrayIdlineNum = precurrent.lineNum;
		  int assignArrayIdIndexlineNum = 0;
		  int assignArrayExplineNum = 0;
		  
			  eatToken(Kind.TOKEN_LBRACK);
			  
			  assignArrayIdIndexlineNum = current.lineNum;
			  indexexp = parseExp();//解析表达式
			  eatToken(Kind.TOKEN_RBRACK);
			  eatToken(Kind.TOKEN_ASSIGN); 
			  
			  assignArrayExplineNum = current.lineNum;//取表达式的第一个token的lineNum
			  
			  exp = parseExp();//解析表达式			  
			  eatToken(Kind.TOKEN_SEMI);     //; 注意一定要多读一个分号
			  
			  return new ast.stm.AssignArray(id,indexexp,exp,assignArrayIdlineNum,assignArrayIdIndexlineNum,assignArrayExplineNum);
	  }
	  else
		  error("Can not tackle this Statement...");//can not tackle
	
	   
	  }
  else
	  error("Can not tackle this Statement...");//can not tackle
	  
	  return null;
	  
  }

  // Statements -> Statement Statements
  // ->
  private java.util.LinkedList<ast.stm.T> parseStatements()
  {
	  java.util.LinkedList<ast.stm.T> stms = new java.util.LinkedList<ast.stm.T>();
	  ast.stm.T stm = null;
    while (current.kind == Kind.TOKEN_LBRACE || current.kind == Kind.TOKEN_IF
        || current.kind == Kind.TOKEN_WHILE
        || current.kind == Kind.TOKEN_SYSTEM || current.kind == Kind.TOKEN_ID ||current.kind == Kind.TOKEN_THIS) {
    	stm = parseStatement();
    	stms.add(stm);
    }
    return stms;
  }

  


  // Type -> int []
  //      -> boolean
  //      -> int
  //      -> id
  private ast.type.T parseType() 
  {
    // Lab1. Exercise 4: Fill in the missing code
    // to parse a type.
   // new util.Todo();
	  if(current.kind == Kind.TOKEN_INT) //int a,b,c;
	  {
		  advance();
		  if(current.kind == Kind.TOKEN_LBRACK)
			{
			  eatToken(Kind.TOKEN_LBRACK);	
			  eatToken(Kind.TOKEN_RBRACK);			   
			  return new ast.type.IntArray();
			}
		  else
		      return new ast.type.Int();
	  }
	  else if(current.kind == Kind.TOKEN_BOOLEAN)
	  {
		  advance();
		  return new ast.type.Boolean();
	  }
	  else if(current.kind == Kind.TOKEN_ID)
	  {
		  advance();
		  return new ast.type.Class(precurrent.lexeme);
	  }
	  else 	 
	  {
		  error("Can not tackle this type...");  
	      return null;
	  }
	  
  }

  // VarDecl -> Type id ;
  private ast.dec.Dec parseVarDecl() 
  {
    // to parse the "Type" nonterminal in this method, instead of writing
    // a fresh one.
	  	
	  ast.type.T dectype = null;
	  String decid = null;
	  
	  int declineNum = 0;
	  
	  dectype = parseType();
      
    if(current.kind == Kind.TOKEN_ID)
    {
    	declineNum = current.lineNum;

    eatToken(Kind.TOKEN_ID);     
    decid = precurrent.lexeme;
    

    
    if(current.kind == Kind.TOKEN_SEMI)
    	eatToken(Kind.TOKEN_SEMI);
    else if(current.kind == Kind.TOKEN_ASSIGN)
    	error("Can not init this variable here...");
    else
    	error("Define int/boolean/user-defined type variables must be ended by semicolon...");
    }
    else
    {  	
    	unreadtokentag = 0;
    	unreadtoken = current;
        current = precurrent;  	  
        return null;
    }
    return new ast.dec.Dec(dectype, decid,declineNum);
  }

  // VarDecls -> VarDecl VarDecls
  // ->
  private java.util.LinkedList<ast.dec.T> parseVarDecls()
  {
	  java.util.LinkedList<ast.dec.T> vardecs = new java.util.LinkedList<ast.dec.T>();
	  ast.dec.Dec dec = null;

	   while (current.kind == Kind.TOKEN_INT || current.kind == Kind.TOKEN_BOOLEAN  
        || current.kind == Kind.TOKEN_ID && unreadtokentag == 1) 
	   {
		   dec = parseVarDecl();
		   if(dec != null)
		   vardecs.add(dec);
       }
	   
    return vardecs;
  }

 
  // FormalList -> Type id FormalRest*
  // ->
  // FormalRest -> , Type id
  private java.util.LinkedList<ast.dec.T> parseFormalList()   //probably same like this -> int a,boolean b,int c ->形参表 parameter
  {
	  java.util.LinkedList<ast.dec.T> formallists = new java.util.LinkedList<ast.dec.T>();
	  ast.dec.T formaldec = null;
	  ast.type.T dectype = null;
	  String decid = null;
	  
	  int declineNum = 0;
	  
    if (current.kind == Kind.TOKEN_INT || current.kind == Kind.TOKEN_BOOLEAN
        || current.kind == Kind.TOKEN_ID) {
      dectype = parseType();
      eatToken(Kind.TOKEN_ID);
      
      declineNum = precurrent.lineNum;
      
      decid = precurrent.lexeme;
      formaldec = new ast.dec.Dec(dectype, decid,declineNum);
      formallists.add(formaldec);
      while (current.kind == Kind.TOKEN_COMMER) {
        advance();
        dectype = parseType();
        eatToken(Kind.TOKEN_ID);
        
        declineNum = precurrent.lineNum;
        
        decid = precurrent.lexeme;
        formaldec = new ast.dec.Dec(dectype, decid,declineNum);
        formallists.add(formaldec);      
      }
    }
    return formallists;
  }

  // Method -> public Type id ( FormalList )   FormalList->形参表 parameter
  // { VarDecl* Statement* return Exp ;}
  private ast.method.T parseMethod()
  {
    // Lab1. Exercise 4: Fill in the missing code
    // to parse a method.
   // new util.Todo();
	  ast.type.T retType = null;
	  String methodid = null;
	  java.util.LinkedList<ast.dec.T> formals = new java.util.LinkedList<ast.dec.T>();
	  java.util.LinkedList<ast.dec.T> locals = new java.util.LinkedList<ast.dec.T>();
	  java.util.LinkedList<ast.stm.T> stms = new java.util.LinkedList<ast.stm.T>();
	  ast.exp.T retExp = null;
	  
	  int retExplineNum = 0;
	  
	  
	  
	  eatToken(Kind.TOKEN_PUBLIC);  //public
	  retType = parseType();        //Type	  
	  eatToken(Kind.TOKEN_ID);      //ID
	  methodid = precurrent.lexeme;
	  eatToken(Kind.TOKEN_LPAREN);  //(
	  if(current.kind != Kind.TOKEN_RPAREN)
	  {
		  formals = parseFormalList();            //解析形参表FormalList
	  }		  
	  eatToken(Kind.TOKEN_RPAREN);  //)
	  eatToken(Kind.TOKEN_LBRACE);  //{
	  
	  
	  //注意：method方法里面，必须先 int a...   后语句  if while.....
	  locals = parseVarDecls(); 
	  stms = parseStatements();
	 // parseStatementsAndVarDecls();
	  
	  eatToken(Kind.TOKEN_RETURN);  //return
	  
	  retExplineNum = current.lineNum;//指向，表达式，的第一个token
	  
	  retExp = parseExp();//解析表达式
	  eatToken(Kind.TOKEN_SEMI);    //;
	  eatToken(Kind.TOKEN_RBRACE);  //}
    return new ast.method.Method(retType,methodid,formals,locals,stms,retExp,retExplineNum);
  }

  // MethodDecls -> MethodDecl MethodDecls
  // ->
  private java.util.LinkedList<ast.method.T> parseMethodDecls()
  {
	  java.util.LinkedList<ast.method.T> methods = new java.util.LinkedList<ast.method.T>();
	  ast.method.T method = null;
    while (current.kind == Kind.TOKEN_PUBLIC) {
      method = parseMethod();
      methods.add(method);
    }
    return methods;
  }

  // ClassDecl -> class id { VarDecl* MethodDecl* }
  // -> class id extends id { VarDecl* MethodDecl* }
  private ast.classs.T parseClassDecl()
  {
    eatToken(Kind.TOKEN_CLASS);
    eatToken(Kind.TOKEN_ID);
    String classname = precurrent.lexeme;
    String extendsname = null;
    int extendslineNum = 0;
    if (current.kind == Kind.TOKEN_EXTENDS) {
      extendslineNum = current.lineNum;
      eatToken(Kind.TOKEN_EXTENDS);
      eatToken(Kind.TOKEN_ID);
      extendsname = precurrent.lexeme;
    }
    eatToken(Kind.TOKEN_LBRACE);  
   
    java.util.LinkedList<ast.dec.T> classdec = parseVarDecls();
    java.util.LinkedList<ast.method.T> method = parseMethodDecls();
    
    eatToken(Kind.TOKEN_RBRACE);
    return new ast.classs.Class(classname, extendsname, classdec, method,extendslineNum);
  }

  // ClassDecls -> ClassDecl ClassDecls
  // ->
  private java.util.LinkedList<ast.classs.T> parseClassDecls()
  {
	  
	  java.util.LinkedList<ast.classs.T> classes = new java.util.LinkedList<ast.classs.T>();
	  ast.classs.T oneclass = null;
    while (current.kind == Kind.TOKEN_CLASS) {
      oneclass = parseClassDecl();
      classes.add(oneclass);
    }
    	
    return classes;
  }

  // MainClass -> class id
  // {
  // public static void main ( String [] id )
  // {
  // Statement
  // }
  // }
  private ast.mainClass.MainClass parseMainClass()
  {
	  
	   
    // Lab1. Exercise 4: Fill in the missing code
    // to parse a main class as described by the
    // grammar above.
  //  new util.Todo();
	    eatToken(Kind.TOKEN_CLASS);   //class
	    eatToken(Kind.TOKEN_ID);      //ID
	    String mainclassid = precurrent.lexeme;	    
	    eatToken(Kind.TOKEN_LBRACE);  //{	    
	    eatToken(Kind.TOKEN_PUBLIC);  //public
	    eatToken(Kind.TOKEN_STATIC);  //static
	    eatToken(Kind.TOKEN_VOID);    //void
	    eatToken(Kind.TOKEN_MAIN);    //main
	    eatToken(Kind.TOKEN_LPAREN);  //(
	    eatToken(Kind.TOKEN_STRING);  //string
	    eatToken(Kind.TOKEN_LBRACK);  //[
	    eatToken(Kind.TOKEN_RBRACK);  //]
	    eatToken(Kind.TOKEN_ID);      //args
	    String mainclassarg = precurrent.lexeme;
	    eatToken(Kind.TOKEN_RPAREN);  //)
	    eatToken(Kind.TOKEN_LBRACE);  //{

	    java.util.LinkedList<ast.dec.T> maindec = parseVarDecls();
	    java.util.LinkedList<ast.stm.T> mainstm = parseStatements();
	    //parseStatementsAndVarDecls();
      
	    eatToken(Kind.TOKEN_RBRACE);  //}
	    eatToken(Kind.TOKEN_RBRACE);  //} 注意有2个
	    return new ast.mainClass.MainClass(mainclassid,mainclassarg,maindec,mainstm);
	    
  }

  // Program -> MainClass ClassDecl*
  private ast.program.T parseProgram()
  {

	  ast.program.T prog = null;
	  ast.mainClass.MainClass mainclass = null;
	  java.util.LinkedList<ast.classs.T> classes = null;
	  
	  mainclass = parseMainClass();
	  classes = parseClassDecls();

      eatToken(Kind.TOKEN_EOF);
    
      prog = new ast.program.Program(mainclass,classes);
      
      return prog;
  }

  public ast.program.T parse()
  {     
	  ast.program.T prog = null;
      prog = parseProgram();
    return prog;
  }
}

//copyright by KIRA林泽南