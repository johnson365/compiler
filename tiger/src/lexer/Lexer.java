package lexer;

import java.io.InputStream;

import util.Todo;

import lexer.Token.Kind;

import java.io.FileNotFoundException;
import java.io.FileReader;  
import java.io.IOException;  
import java.io.PushbackReader;

public class Lexer
{
  String fname; // the input file name to be compiled
  //InputStream fstream; // input stream for the above file
  
  PushbackReader pushbackreader;
  
  int line;

  public java.util.Map<String,Kind> TokenKeyMap;
  
  
  public Lexer(String fname, PushbackReader pushbackreader)
  {
    this.fname = fname;
    this.pushbackreader = pushbackreader;
    
    line = 1;
    
    TokenKeyMap = new java.util.HashMap<String,Kind>();
       
    TokenKeyMap.put("boolean", Kind.TOKEN_BOOLEAN); 
    TokenKeyMap.put("class",Kind.TOKEN_CLASS);
    TokenKeyMap.put("else",Kind.TOKEN_ELSE);
    TokenKeyMap.put("extends",Kind.TOKEN_EXTENDS);
    TokenKeyMap.put("false", Kind.TOKEN_FALSE);
    TokenKeyMap.put("if", Kind.TOKEN_IF);
    TokenKeyMap.put("int", Kind.TOKEN_INT);
    TokenKeyMap.put("length", Kind.TOKEN_LENGTH);
    TokenKeyMap.put("main", Kind.TOKEN_MAIN);		
    TokenKeyMap.put("new", Kind.TOKEN_NEW);
    TokenKeyMap.put("out", Kind.TOKEN_OUT);
    TokenKeyMap.put("println", Kind.TOKEN_PRINTLN);
    TokenKeyMap.put("public", Kind.TOKEN_PUBLIC);
    TokenKeyMap.put("private", Kind.TOKEN_PRIVATE);
    TokenKeyMap.put("protected", Kind.TOKEN_PROTECTED);
    TokenKeyMap.put("return", Kind.TOKEN_RETURN);
    TokenKeyMap.put("static", Kind.TOKEN_STATIC);
    TokenKeyMap.put("String", Kind.TOKEN_STRING);
    TokenKeyMap.put("System", Kind.TOKEN_SYSTEM);
    TokenKeyMap.put("this", Kind.TOKEN_THIS);
    TokenKeyMap.put("true", Kind.TOKEN_TRUE);
    TokenKeyMap.put("void", Kind.TOKEN_VOID);
    TokenKeyMap.put("while", Kind.TOKEN_WHILE);
   
  }
  
  
  //为了 Parser，返回fname的值
  public String Getfname(){return fname;}
  

  // When called, return the next token (refer to the code "Token.java")
  // from the input stream.
  // Return TOKEN_EOF when reaching the end of the input stream.
  private Token nextTokenInternal() throws IOException 
  {
	  
    int c = pushbackreader.read();
    //if put int into numstr,it will be ASCII,so put char in numstr is well
    if (c == -1)
      // The value for "lineNum" is now "null",
      // you should modify this to an appropriate
      // line number for the "EOF" token.
      return new Token(Kind.TOKEN_EOF, line);

    // skip all kinds of "blanks"
    while (c == ' ' || c == '\t' || c == '\n')
    {	
    	if(c == '\n')
    		line++;
    	c = pushbackreader.read();
    }
    if (c == -1)
      return new Token(Kind.TOKEN_EOF, line);
    
 
    if (Character.isLetter(c) || c == '_') 
    {
		StringBuffer idstr = new StringBuffer();
		do 
		{
			idstr.append((char)c);
			c = pushbackreader.read();
			
		} while (Character.isLetterOrDigit(c) || c == '_');
		pushbackreader.unread(c);

		String tempstr = idstr.toString(); 
		
	    if(TokenKeyMap.containsKey(tempstr))
	    	 return new Token(TokenKeyMap.get(tempstr), line,tempstr);
	    else
		     return new Token(Kind.TOKEN_ID,line,tempstr);
	}
	
	if (Character.isDigit(c)) 
	{
		StringBuffer numstr = new StringBuffer();
		do 
		{
			numstr.append((char)c);  
			c = pushbackreader.read();			
		}while (Character.isDigit(c));		
		pushbackreader.unread(c);
		String tempstr = numstr.toString();
		return new Token(Kind.TOKEN_NUM, line,tempstr);			
	}

    switch (c) 
    {
    case '+':
      return new Token(Kind.TOKEN_ADD, line);
    case '-':
      return new Token(Kind.TOKEN_SUB, line);
    case '*':
      return new Token(Kind.TOKEN_TIMES, line);
    case '/':
    {
    	
    	c = pushbackreader.read();
    	if(c == '/')
    	{   		
    	    do
    		{
    			c = pushbackreader.read();  			
    		}
    		while(c != '\n' && c != -1);
    	    if(c == '\n')
    	    {
    	    	pushbackreader.unread(c);
    	    	return new Token(Kind.TOKEN_COMMENTLINE, line);   	     	
    	    }
    	    else
    	    	return new Token(Kind.TOKEN_EOF, line);  		
    	}
    	else if(c == '*')
    	{
    		int templine = line; 
    		while(true)
    		{
     	        do
        	    {
     	        	c = pushbackreader.read();  
     	        	if(c == '\n')
     	        		{
     	        		    line++;
     	        		}
        	    }
        	    while(c != '*'); 
     	        c = pushbackreader.read(); 
     	        if(c == '/')
     	    	 return new Token(Kind.TOKEN_COMMENTBLOCK, templine);  	
    		}
    	}
    	else
    	{
    		pushbackreader.unread(c);     		
    		return new Token(Kind.TOKEN_DIVISION, line);
    	}
    	
    }
    case '.':
      return new Token(Kind.TOKEN_DOT, line);
    case ',':
      return new Token(Kind.TOKEN_COMMER, line);
    case ';':
      return new Token(Kind.TOKEN_SEMI, line);
    case '(':
      return new Token(Kind.TOKEN_LPAREN, line);
    case ')':
      return new Token(Kind.TOKEN_RPAREN, line); 
    case '[':
      return new Token(Kind.TOKEN_LBRACK, line);
    case ']':
      return new Token(Kind.TOKEN_RBRACK, line);
    case '{':
      return new Token(Kind.TOKEN_LBRACE, line);
    case '}':
      return new Token(Kind.TOKEN_RBRACE, line);
      
      
    case '&':
    {
    	c = pushbackreader.read();

	    if (c == '&') 
			return new Token(Kind.TOKEN_AND, line);
		else 
		 {
			util.Error.bug();
			//return null;
		 }
	}
    case '|':
    {	
    	c = pushbackreader.read();
    	if (c == '|') 
			return new Token(Kind.TOKEN_OR, line);
		else 
		{
	 		util.Error.bug();
	 		//return null;
	    }
    }
    case '=':
    {
    	c = pushbackreader.read();
    	if(c == '=')
    		return new Token(Kind.TOKEN_EQUAL, line);
    	else
    	{
    		pushbackreader.unread(c);
    		return new Token(Kind.TOKEN_ASSIGN, line);
    		
    	}	
    }

	case '!':
	{
		c = pushbackreader.read();
    	if(c == '=')
    		return new Token(Kind.TOKEN_NOTEQUAL, line);
    	else
    	{
    		pushbackreader.unread(c);
    		return new Token(Kind.TOKEN_NOT, line);   		
    	}
    }
	
	case '<':
	{
		c = pushbackreader.read();
    	if(c == '=')
    		return new Token(Kind.TOKEN_LTE, line);
    	else
    	{
    		pushbackreader.unread(c);
    		return new Token(Kind.TOKEN_LT, line);   		
    	}
	}
	
	case '>':
	{	
		c = pushbackreader.read();
    	if(c == '=')
    		return new Token(Kind.TOKEN_GTE, line);
    	else
    	{
    		pushbackreader.unread(c);
    		return new Token(Kind.TOKEN_GT, line);   		
    	}
	}
	
	
	
    default: 
    	return new Token(Kind.TOKEN_EOF, line); 
      // Lab 1, exercise 2: supply missing code to
      // lex other kinds of tokens.
      // Hint: think carefully about the basic
      // data structure and algorithms. The code
      // is not that much and may be less than 50 lines. If you
      // find you are writing a lot of code, you
      // are on the wrong way.
      //new Todo();
     // return null;
    }
  }

  public Token nextToken()
  {
    Token t = null;

    try 
    {
      t = this.nextTokenInternal();
    } catch (Exception e) 
    {
      e.printStackTrace();
      System.exit(1);
    }
    if (control.Control.lex)  	
    {
    	;
    	//System.out.println(t.toString());
    }
    return t;
  }
}
//copyright by 林泽南