package com.mondsciomegn.salagiochi.videogame;


public class controlli {
	// Controlli per tris
		// Controllo se la posizione scritta dall'utente è accettabile
		public static boolean controllo(int riga, int colonna, char[][] areaGioco) {
			
			if(riga>2 || riga<0 || colonna>2 || colonna<0) {
				return false;
			}else {
				if (areaGioco[riga][colonna] != ' ') {
		        return false;
		        }else {
				return true;
		        }
			}
		}
		
		// Controllo la vittoria
		public static boolean controlloTris(char[][] areaGioco) {
			for(int i=0; i<2; i++) {
				
				// Orizzontale
				if(areaGioco[i][0] == 'X' && areaGioco[i][1] == 'X' && areaGioco[i][2] == 'X') {
					System.out.println("HAI VINTO!");
					return true;
				}
				
				if(areaGioco[i][0] == 'O' && areaGioco[i][1] == 'O' && areaGioco[i][2] == 'O') {
						System.out.println("HAI PERSO!");
						return true;
					}
				}
			
			
			for(int j=0; j<2; j++) {
				
				// Vericale
				if(areaGioco[0][j] == 'X' && areaGioco[1][j] == 'X' && areaGioco[2][j] == 'X') {
					System.out.println("HAI VINTO!");
					return true;
				}

				if(areaGioco[0][j] == 'O' && areaGioco[1][j] == 'O' && areaGioco[2][j] == 'O') {
					System.out.println("HAI PERSO!");
					return true;
					}
				}
			
			// Obliquo
			if((areaGioco[0][0] == 'X' && areaGioco[1][1] == 'X' && areaGioco[2][2] == 'X')||(areaGioco[0][2] == 'X' && areaGioco[1][1] == 'X' && areaGioco[2][0] == 'X')){
				System.out.println("HAI VINTO!");
				return true;
			}
			
			if((areaGioco[0][0] == 'O' && areaGioco[1][1] == 'O' && areaGioco[2][2] == 'O')||(areaGioco[0][2] == 'O' && areaGioco[1][1] == 'O' && areaGioco[2][0] == 'O')) {
				System.out.println("HAI PERSO!");
				return true;
				}
				
			return false;
				
			}
		
		// Controlli indovina numero
		public static boolean controlloNumeroCasuale(int numero, int numeroCasuale) {
			if(numero<numeroCasuale) {
				System.out.println("Il numero che hai inserito è più PICCOLO del numero estratto");
				return true;
			}else {
				
				if(numero>numeroCasuale) {
					System.out.println("Il numero che hai inserito è più GRANDE del numero estratto");
					return true;
				}else {
					if(numero==numeroCasuale) {
						System.out.println("HAI VINTO!");
						return false;
					}
				}
			}
			return true;
		}
		
		// Controlli dadi
		public static void controlloDadiMaggiore(int somma, int sommaC) {
			if(somma<sommaC) {
				System.out.println("HAI PERSO!");
			}else {
				if(somma>sommaC) {
					System.out.println("HAI VINTO!");
				}else {
					if(somma==sommaC) {
						System.out.println("PAREGGIO!");
					}
				}
			}
		}
		
		// Controlli Carta Forbice Sasso
		public static int controlloScelta(int scelta) {
			int num=0;
			if(scelta<1 || scelta>3) {
				System.out.println("SCELTA NON CORRETTA!!");
			}else {
				num++;
			}
			
			return num;
		}
		
		
		public static void controlloVittoria(int scelta, int sceltaC) {
			if((scelta == 1 && sceltaC == 1) || (scelta == 2 && sceltaC == 2) || (scelta == 3 && sceltaC == 3))  {
				System.out.println("PAREGGIO! ");
			}else {
				if((scelta == 1 && sceltaC == 2) || (scelta == 2 && sceltaC == 3) || (scelta == 3 && sceltaC == 1)) {
					System.out.println("HAI PERSO! ");
				}else {
					if((scelta == 1 && sceltaC == 3) || (scelta == 2 && sceltaC == 1) || (scelta == 3 && sceltaC == 2)) {
						System.out.println("HAI VINTO! ");
					}
				}
			}
		}

}
