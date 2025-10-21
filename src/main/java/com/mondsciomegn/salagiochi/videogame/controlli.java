package com.mondsciomegn.salagiochi.videogame;


public class controlli {
		
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
