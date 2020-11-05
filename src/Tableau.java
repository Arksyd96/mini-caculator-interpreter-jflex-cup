import java.util.ArrayList;

public class Tableau extends Variable {
		private ArrayList<Integer> valeurs;

		public Tableau(String ident, int... valeurs) {
			super(ident);
			this.valeurs = new ArrayList<>();
			for(int i : valeurs)	this.valeurs.add(i);
		}
	
		public Tableau(String ident, ArrayList<Integer> arr) {
			super(ident);
			this.valeurs = new ArrayList<>(arr);
		}

		public ArrayList<Integer> getValeurs() {
			return this.valeurs;
		}

		public void setValeurs(ArrayList<Integer> valeurs) {
			this.valeurs = valeurs;
		}
		
		public String toString() {
			return String.format("    %-5s = %-8s %s", 
				this.getIndentificator(), this.getClass().getName(), this.valeurs);
		}
		
	}