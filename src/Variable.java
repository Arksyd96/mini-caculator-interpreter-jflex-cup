
// Il est plus facile de créer une classe pour chaque type primitif
// de cette manière on peut utiliser l'héritage à notre aventage
// et créer une HashMap de Variables en général sans se soucier du type de ces dérnières.
// même pour l'affichage c'est plus facile, il suffit de faire un toString() quelque soit le type
// on est sur que ça va bien afficher les données qu'on veut :D
public class Variable {
	private String indentificator;

	public Variable(String ident) {
		indentificator = ident;
	}
	
	public String getIndentificator() {
		return indentificator;
	}

	public void setIndentificator(String indentificator) {
		this.indentificator = indentificator;
	}

}
