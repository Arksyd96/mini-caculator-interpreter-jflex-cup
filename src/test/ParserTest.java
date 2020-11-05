import static org.junit.Assert.*;
import java.util.*;
import java.io.*;

import org.junit.Test;

public class ParserTest{

	// ------------------------------------------------------------
	// Mocked data
	// ------------------------------------------------------------

	String erreurLexicales =
		"5 - 2\n" +
		"5 / 2\n" +
		"a := 5\n" +
		"10ab = 5\n" +
		"a = 5;\n" +
		"b = 5; ;\n" +
		"c ::= 5\n"
		;	

	String expressionsCorrectes = 
		"5\n" + 
		"2 + 2\n" +
		"5 * 2\n" +
		"2 + 2 * 5\n" +
		"2 * 5 + 2\n" +
		"{1, 2, 3}\n" +
		"{1}\n" +
		"{1, 2}^{3}\n" +
		"{1, 2}^{3, 4}^{5, 6, 7}\n" +
		"{1, 2}[0]\n" +
		"{0, 1, 2, 3}[3]\n" +
		"{0, 1, 2, 3}[1 * 2 + 1]\n"
		;

	String expressionsErronnees = 
		"+\n" +
		"5 +\n" +
		"5 * +\n" +
		"2 * 5 + * 7\n" +
		"2 5 +\n" +
		"5 + {4, 5}\n" +
		"5 * {4, 5}\n" +
		"{4, 5} + {6}\n" +
		"{1, 2, 3, 4}[5]\n"
		;

	String declarationsCorrectes =
		"a = 5\n" + 
		"b = 10\n" +
		"c = a + b\n" +
		"d = a + b * 2\n" +
		"@a = {1, 2}\n" +
		"@b = {1, 2}^{3, 4}\n" +
		"e = @a[0]\n" +
		"f = 0\n" + 
		"g = @b[@a[f + 1] + 1]\n" +
		"PRINT\n"
		;

	String declarationsErronnees =
		"a = {1, 2}\n" +
		"@a = 5\n" +
		"@a = {1, 2, 3}\n" +
		"@b = @a[0]\n" +
		"a = 5 + b\n" +
		"a\n" +
		"@b\n" +
		"@a[4]\n" 
	;


	// ------------------------------------------------------------
	// Les tests
	// ------------------------------------------------------------


	@Test
	public void erreurLexicales() throws Exception {
		System.out.print(
			"\n//-------------------------------------------------\n" +
			"// Test des erreurs lexicales \n" +
			"// (quelques unes seulement car il est impossible de tout tester)\n" +
			"//-------------------------------------------------\n" +
			"NOTE : La valeur de error_sync_size() est reglée à 1,\n" +
			"En d'autres termes, l'analyseur va consommer un caractère seulement lors d'une erreur\n" +
			"et continuer l'analyse comme si de rien n'était. ceci aura l'effet d'engendrer aussi des\n" +
			"erreurs syntaxiques, car le programme ne vas pas s'arrêter mais continuer à analyser.\n\n" +
			"Input :\n" +
			this.erreurLexicales
		);
		parser p = new parser(erreurLexicales);
		p.parse();
		assertFalse(p.isSyntaxCorrect());
		assertFalse(p.lexer.isLexicCorrect());

		System.out.println("Lexique correcte ? => " + p.lexer.isLexicCorrect() + " | expected : false");
		System.out.println("Syntaxe correcte ? => " + p.isSyntaxCorrect() + " | expected : false");
		System.out.println();
	}

	@Test
	public void expressionsSyntaxiquementCorrectes() throws Exception{
		System.out.print(
			"\n//-------------------------------------------------\n" +
			"// Test des expressions syntaxiquement correctes\n" +
			"//-------------------------------------------------\n" +
			"Input :\n" +
			this.expressionsCorrectes
		);
		parser p = new parser(expressionsCorrectes);
		p.parse();
		assertTrue(p.isSyntaxCorrect());
		assertTrue(p.lexer.isLexicCorrect());
		
		System.out.println("Lexique correcte ? => " + p.lexer.isLexicCorrect() + " | expected : true");
		System.out.println("Syntaxe correcte ? => " + p.isSyntaxCorrect() + " | expected : true");
		System.out.println();
	}

	@Test
	public void expressionsSyntaxiquementErronnees() throws Exception{
		System.out.print(
			"\n//-------------------------------------------------\n" +
			"// Test des expressions syntaxiquement érronnées\n" +
			"//-------------------------------------------------\n" +
			"Input :\n" +
			this.expressionsErronnees
		);
		parser p = new parser(expressionsErronnees);
		p.parse();
		assertFalse(p.isSyntaxCorrect());
		assertTrue(p.lexer.isLexicCorrect());

		System.out.println("Lexique correcte ? => " + p.lexer.isLexicCorrect() + " | expected : true");
		System.out.println("Syntaxe correcte ? => " + p.isSyntaxCorrect() + " | expected : false");
		System.out.println();
	}

	@Test
	public void declarationsCorrectes() throws Exception{
		System.out.print(
			"\n//-------------------------------------------------\n" +
			"// Test des déclarations correctes de variables\n" +
			"//-------------------------------------------------\n" +
			"Input :\n" +
			this.declarationsCorrectes
		);
		parser p = new parser(declarationsCorrectes);
		p.parse();
		assertTrue(p.isSyntaxCorrect());
		assertTrue(p.lexer.isLexicCorrect());
		assertEquals(p.sem.syms.size(), 9);
		assertEquals(((Tableau) p.sem.syms.get("@b")).getValeurs().size(), 4);
		assertEquals(((Entier) p.sem.syms.get("d")).getValeur(), 25);

		System.out.println("Lexique correcte ? => " + p.lexer.isLexicCorrect() + " | expected : true");
		System.out.println("Syntaxe correcte ? => " + p.isSyntaxCorrect() + " | expected : true");	
		System.out.println("La table des symboles contient : " + p.sem.syms.size() + " | expected : 9");
		System.out.println("La valeur de @b dans la table des symboles : "
			+ ((Tableau) p.sem.syms.get("@b")).getValeurs() + " | expected : [1, 2, 3, 4]");
		System.out.println("La valeur de d dans la table des symboles : "
			+ ((Entier) p.sem.syms.get("d")).getValeur() + " | expected : 25");
		System.out.println();
	}

	@Test
	public void declarationsErronnees() throws Exception {
		System.out.print(
			"\n//-------------------------------------------------\n" +
			"// Test des déclarations sémantiquement ou syntaxiquement erronnées\n" +
			"//-------------------------------------------------\n" +
			"Input :\n" +
			this.declarationsErronnees
		);
		parser p = new parser(declarationsErronnees);
		p.parse();
		assertTrue(p.isSyntaxCorrect());
		assertTrue(p.lexer.isLexicCorrect());

		System.out.println("Lexique correcte ? => " + p.lexer.isLexicCorrect() + " | expected : true");
		System.out.println("Syntaxe correcte ? => " + p.isSyntaxCorrect() + " | expected : true");	
		System.out.println();
	}

}