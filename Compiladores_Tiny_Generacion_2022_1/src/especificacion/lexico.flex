package ve.edu.unet;

import java_cup.runtime.*;
import java.io.Reader;
//import otros.*;

%%
/* Habilitar la compatibilidad con el interfaz CUP para el generador sintactico*/
%cup
/* Llamar Scanner a la clase que contiene el analizador Lexico */
%class Lexico

/*-- DECLARACIONES --*/
%{
	public Lexico(Reader r, SymbolFactory sf){
        this(r);
		this.sf=sf;
		lineanum=0;
		debug=true;
	}
	private SymbolFactory sf;
	private int lineanum;
	private boolean debug;


/******************************************************************
BORRAR SI NO SE NECESITA
	//TODO: Cambiar la SF por esto o ver que se hace
	//Crear un nuevo objeto java_cup.runtime.Symbol con informaci�n sobre el token actual sin valor
 	  private Symbol symbol(int type){
    		return new Symbol(type,yyline,yycolumn);
	  }
	//Crear un nuevo objeto java_cup.runtime.Symbol con informaci�n sobre el token actual con valor
	  private Symbol symbol(int type,Object value){
    		return new Symbol(type,yyline,yycolumn,value);
	  }
******************************************************************/
%}
%eofval{
    return sf.newSymbol("EOF",sym.EOF);
%eofval}

/* Acceso a la columna y fila actual de analisis CUP */
%line
%column



digito		= [0-9]
numero		= {digito}+
letra			= [a-zA-Z]
identificador	= {letra}+
nuevalinea		= \n | \n\r | \r\n
espacio		= [ \t]+
%%
"program"           { if(debug) System.out.println("token PROGRAMA");
            return sf.newSymbol("PROGRAMA",sym.PROGRAMA);
            }
"var"           { if(debug) System.out.println("token VAR");
            return sf.newSymbol("VAR",sym.VAR);
            }
"Integer"           { if(debug) System.out.println("token INT");
            return sf.newSymbol("INT",sym.INT);
            }
"Boolean"          { if(debug) System.out.println("token BOOL");
            return sf.newSymbol("BOOL",sym.BOOL);
            }
"if"            {	if(debug) System.out.println("token IF");
			return sf.newSymbol("IF",sym.IF);
			}
"then"          { if(debug) System.out.println("token THEN");
			return sf.newSymbol("THEN",sym.THEN);
			}
"else"          {	if(debug) System.out.println("token ELSE");
			return sf.newSymbol("ELSE",sym.ELSE);
			}
"end"           {	if(debug) System.out.println("token END");
			return sf.newSymbol("END",sym.END);
			}
"begin"           {	if(debug) System.out.println("token BEGIN");
			return sf.newSymbol("BEGIN",sym.BEGIN);
			}
"repeat"        {	if(debug) System.out.println("token REPEAT");
			return sf.newSymbol("REPEAT",sym.REPEAT);
			}
"until"         {	if(debug) System.out.println("token UNTIL");
			return sf.newSymbol("UNTIL",sym.UNTIL);
			}
"read"          {	if(debug) System.out.println("token READ");
			return sf.newSymbol("READ",sym.READ);
			}
"write"         {	if(debug) System.out.println("token WRITE");
			return sf.newSymbol("WRITE",sym.WRITE);
			}
"for"			{	if(debug)System.out.println("token FOR");
			return sf.newSymbol("FOR",sym.FOR);
			}
"to"			{	if(debug)System.out.println("token TO");
			return sf.newSymbol("TO",sym.TO);
			}
"do"        	{	if(debug) System.out.println("token DO");
			return sf.newSymbol("DO",sym.DO);
			}
"and"           { if(debug) System.out.println("token AND");
          return sf.newSymbol("AND",sym.AND);
          }
"or"            { if(debug) System.out.println("token OR");
          return sf.newSymbol("OR",sym.OR);
          }
"not"           { if(debug) System.out.println("token NOT");
          return sf.newSymbol("NOT",sym.NOT);
          }
"mod"           { if(debug) System.out.println("token MOD");
          return sf.newSymbol("MOD",sym.MOD);
          }
"array"           { if(debug) System.out.println("token ARRAY");
          return sf.newSymbol("ARRAY",sym.ARRAY);
          }
"of"           { if(debug) System.out.println("token OF");
          return sf.newSymbol("OF",sym.OF);
          }
":="            {	if(debug) System.out.println("token ASIGNACION");
			return sf.newSymbol("ASIGNACION",sym.ASIGNACION);
			}
"="             {	if(debug) System.out.println("token IGUAL");
			return sf.newSymbol("IGUAL",sym.IGUAL);
			}
"<"             {	if(debug) System.out.println("token MENOR_QUE");
			return sf.newSymbol("MENOR_QUE",sym.MENOR_QUE);
			}
">"             {	if(debug) System.out.println("token MAYOR_QUE");
			return sf.newSymbol("MAYOR_QUE",sym.MAYOR_QUE);
			}
"+"             {	if(debug) System.out.println("token PLUS");
			return sf.newSymbol("PLUS",sym.PLUS);
			}
"-"             {	if(debug) System.out.println("token MINUS");
			return sf.newSymbol("MINUS",sym.MINUS);
			}
"*"             {	if(debug) System.out.println("token TIMES");
			return sf.newSymbol("TIMES",sym.TIMES);
			}
"/"             {	if(debug) System.out.println("token OVER");
			return sf.newSymbol("OVER",sym.OVER);
			}
"("             {	if(debug) System.out.println("token LPAREN");
			return sf.newSymbol("LPAREN",sym.LPAREN);
			}
")"             {	if(debug) System.out.println("token RPAREN");
			return sf.newSymbol("RPAREN",sym.RPAREN);
			}
"["             {	if(debug) System.out.println("token LBRACKET");
			return sf.newSymbol("LBRACKET",sym.LBRACKET);
			}
"]"             {	if(debug) System.out.println("token RBRACKET");
			return sf.newSymbol("RBRACKET",sym.RBRACKET);
			}
";"             {	if(debug) System.out.println("token PUNTO_COMA");
			return sf.newSymbol("PUNTO_COMA",sym.PUNTO_COMA);
			}
":"             {	if(debug) System.out.println("token DOS_PUNTOS");
			return sf.newSymbol("DOS_PUNTOS",sym.DOS_PUNTOS);
			}
","             { if(debug) System.out.println("token COMMA");
          return sf.newSymbol("COMMA",sym.COMMA);
          }
"."             { if(debug) System.out.println("token POINT");
          return sf.newSymbol("POINT",sym.POINT);
          }

{numero}        {	if(debug) System.out.println("token NUM");
			return sf.newSymbol("NUM",sym.NUM,new String(yytext()));
			}
{identificador}	{	if(debug) System.out.println("token ID");
				return sf.newSymbol("ID",sym.ID,new String(yytext()));
			}
{nuevalinea}       {lineanum++;}
{espacio}    { /* saltos espacios en blanco*/}
"{"[^}]+"}"  { /* salto comentarios */ if(debug) System.out.println("token COMENTARIO"); }
.               {System.err.println("Caracter Ilegal encontrado en analisis lexico: " + yytext() + "\n");}