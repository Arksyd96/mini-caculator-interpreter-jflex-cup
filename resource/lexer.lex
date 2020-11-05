import java_cup.runtime.Symbol;

%%
%class Lexer
%unicode
%cup
%line
%column

// ------------------------------------------------
// Configuration du parser
// ------------------------------------------------

%{
	// ce boolean me permet de savoir, à la fin d'une analyse, 
    // si le programme est léxicalement correcte ou pas
    // utile pour les tests unitaires.
	protected boolean lexic_correct = true;

	// getter pour l'attribut lexic_correct
	protected boolean isLexicCorrect(){
		return this.lexic_correct;
	}

	// permet d'avoir le token courant au format String à partir du parser
	// le paramètre <Symbol cur_token> de la méthode syntax_error du parser
	// permet seulement d'avoir l'identifiant du token, et donc il faut
	// acceder à la classe Sym pour récupérer sa valeur littérale.
	protected String getCurToken(){
		return yytext();
	}

	// renvoie une erreur léxicale et remet this.lexic_correct à false
	protected void emit_error(){
		this.lexic_correct = false;
		System.err.println("    Erreur lexicale à " + yyline + ":" + yycolumn + " => " + yytext());
	}
%}

integer = [0-9]+
ident 	= [a-zA-Z_][a-zA-Z0-9_]*
t_ident = @{ident}

%%
"PRINT"			{ return new Symbol(sym.PRINT); }
"{"				{ return new Symbol(sym.OACC); }
"}"				{ return new Symbol(sym.CACC); }
","				{ return new Symbol(sym.COMMA); }
"["				{ return new Symbol(sym.OBRA); }
"]"				{ return new Symbol(sym.CBRA); }
"+"				{ return new Symbol(sym.PLUS); }
"*"				{ return new Symbol(sym.MULT); }
"("				{ return new Symbol(sym.OPAR); }
")" 			{ return new Symbol(sym.CPAR); }
"^"				{ return new Symbol(sym.CAT); }
"="				{ return new Symbol(sym.AFFECT); }
{ integer }		{ return new Symbol(sym.integer, 	new Integer(yytext())); }
{ ident }		{ return new Symbol(sym.ident, 		new String(yytext())); }
{ t_ident }		{ return new Symbol(sym.t_ident,	new String(yytext())); }

[\n]			{ return new Symbol(sym.NEWL); }
[ \r\t]			{ /*ne rien faire*/ }
.				{ emit_error(); }