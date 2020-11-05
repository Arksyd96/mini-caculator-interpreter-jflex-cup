public class Entier extends Variable {
		private int valeur;

		public Entier(String ident) {
			super(ident);
			// TODO Auto-generated constructor stub
		}
		public Entier(String ident, int valeur) {
			super(ident);
			this.valeur = valeur;
		}
		public int getValeur() {
			return valeur;
		}
		public void setValeur(int valeur) {
			this.valeur = valeur;
		}
		
		public String toString() {
			return String.format("    %-5s = %-8s %d", 
				this.getIndentificator(), this.getClass().getName(), this.valeur);
		}
	}
	