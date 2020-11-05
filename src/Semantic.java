import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class Semantic {
	// ------------------------------------------------------------------
	// Attributs
	// ------------------------------------------------------------------

	// l'accumulateur, c'est en quelques sortes un temporaire
	protected ArrayList<Integer> acc = new ArrayList<>();

	// la table des symboles, une HachMap avec l'identificateur comme clé
	// ceci permet d'avoir des accès instantanés O(1). 
	protected HashMap<String, Variable> syms = new HashMap<>();

	// verbose sert à savoir si le prochain message à afficher est une erreur ou pas
	protected boolean verbose = true;

	// loaded permet de savoir si l'accumulateur contient déjà des valeurs d'une variable
	protected boolean loaded = false;


	// ------------------------------------------------------------------
	// Méthodes
	// ------------------------------------------------------------------

	// afficherTableau sans paramètres pour afficher le contenu de l'accumulateur
	// et donc un tableau ou bien une case précise d'un tableau si on fournit un index
	public void afficherTableau(Integer index){
		if(this.verbose){
			if(index == null) 	System.out.println(String.format("    - : %-8s %s", "Tableau", this.acc));
			else {
				if(index >= this.acc.size()){
					System.err.println("    Erreur sémantique -> accès à un indice hors du tableau");
				} else {
					System.out.println(String.format("    - : %-8s %s", "Entier", this.acc));
				}
			}
		}
		// on remet toujours verbose a true et on clear l'acc
		this.verbose = true;
		this.acc.clear();
	}

	// afficherResultatExpression permet d'afficher en output le résultat d'une expression arithmétique
	// ou d'une variable depuis la TS
	// mais si loaded est à true alors on va afficher le tableau chargé dans l'accumulateur
	public void afficherResultatExpression(int entier){
		// si l'expression est un tableau ...
		if(this.acc.size() > 0 && this.loaded)	{
			this.afficherTableau(null);
			this.loaded = false;
			return;
		}
		if(this.verbose)	System.out.println(String.format("    - : %-8s %d", "Entier", entier));
		// pareil on réinitialise les valeurs
		this.verbose = true;
		this.acc.clear();
	}

	// affiche une déclaration à partir de l'identificateur
	public void afficherDeclaration(String ident){
		if(this.verbose)	System.out.println(this.syms.get(ident).toString());
		this.verbose = true;
		this.acc.clear();	
	}

	// cette fonction fait l'opération d'addition et de multiplication
	// il aurait été possible de le faire directement à partir de la grammaire mais il est nécéssaire
	// de garder une trace sur la dérnière opération faite et pouvoir l'afficher lors d'une erreur
	// sémantique. Dans ce cas là, si l'accumulateur contient déja un tableau et on essai
	// d'executer une opération arithmétique alors il y'a une erreur de typage entre entier et tableau
	public int calculate(int a, int b, String op){
		if(this.acc.size() > 0 && this.loaded)	{
			this.verbose = false;
			System.err.println("    Erreur sémantique -> types incompatible sur l'opérateur " + op);
			this.acc.clear();
			return 0;
		}
		switch(op){
			case "+":
				return a + b;
			case "*":
				return a * b;
			default:
				return 0;
		}
	}

	// fonction permettant de créer de nouvelles variables et de les ajouter à this.syms
	// si on a plus d'1 valeur dans l'accumulateur alors c'est un tableau sinon c'est un entier
	// ceci permet de renvoyer une erreur si les types sont differents
	// si tout est OK, on créer une Variable de type Entier ou Tableau
	public String declare(String ident) {
		if((ident.charAt(0) == '@' && this.acc().size() == 1)
			|| (ident.charAt(0) != '@' && this.acc().size() > 1)){
			System.err.println("    Erreur sémantique -> types incompatible");
			this.verbose = false;
			this.acc().clear();
			return null;
		}
		Variable newSym;
		if(this.acc().size() == 1)	newSym = new Entier(ident, this.acc().get(0));
		else 						newSym = new Tableau(ident, this.acc());
		this.syms.put(ident, newSym);
		this.acc().clear();
		return ident;
	}

	// lookUp avec l'identificateur seulement comme paramètre permet d'afficher le contenu d'un entier
	public int lookUp(String ident){
		if(!this.exists(ident))	return 0;
		return ((Entier) this.syms.get(ident)).getValeur();
	}

	// lookUp avec ident et index permet d'afficher la valeur d'une case depuis le tableau ident
	// si index >= à size alors l'indice est hors du tableau
	public int lookUp(String ident, int index){
		if(!this.exists(ident))	return 0;
		ArrayList<Integer> arr = ((Tableau) this.syms.get(ident)).getValeurs();
		if(index >= arr.size()){
			this.verbose = false;
			System.err.println("    Erreur sémantique -> accès à un indice hors du tableau");
			return 0;
		}
		return arr.get(index);
	}


	// NOTE : loadInAcc est l'equivalent de la fonction lookUp mais pour les tableau
	// car ici on va charger tout un tableau dans l'accumulateur et pas seulement un entier
	// vérifie aussi grâce à la méthode exists si la variable à été déclarée
	public void loadInAcc(String ident){
		if(!this.exists(ident))	return;
		this.loaded = true;
		this.acc = new ArrayList<>(((Tableau)this.syms.get(ident)).getValeurs());
	}

	// fonction qui renvoi une erreur sémantique si une variable n'est pas déclarée
	public boolean exists(String ident){
		if(!this.syms.containsKey(ident)){
			this.verbose = false;
			System.err.println("    Erreur sémantique -> identificateur inconnu : " + ident);
			return false;
		}
		return true;
	}

	// permet simplement l'affichage de toutes les variables déclarées par une simple itération dans la 
	// table des symboles
	public void print(){
		System.out.println("    Les valeurs des variables sont :");
		for(Iterator<Map.Entry<String, Variable>> it = this.syms.entrySet().iterator(); it.hasNext(); ) {
			System.out.println(it.next().getValue().toString());
		}
	}

	// getter de l'accumulateur
	public ArrayList<Integer> acc(){
		return this.acc;
	}
}