package com.mondsciomegn.salagiochi.videogame;

import java.util.Random;


public class dadi extends videoGame{
controlli controllo = new controlli();
	
	public void gioca() {
		int somma, sommaCasuale;
		
		System.out.println("LANCIO DEI DADI: ");
		System.out.println("Lancia due dati e il punteggio più alto vince la partita! \n\n");
		
		somma = lancia();
		System.out.println("La somma dei dati che hai lanciato è: "+somma+"\n");

		sommaCasuale = lancia();
		System.out.println("La somma dei dati lanciati dall'avversario è: "+sommaCasuale);

		
		controllo.controlloDadiMaggiore(somma, sommaCasuale);
		
	}
	
	public static int lancia() {
		Random ran = new Random();
		int somma=0, dado1, dado2;
		
		dado1 = ran.nextInt(6)+1;

		dado2 = ran.nextInt(6)+1;
		System.out.println("I numeri usciti dal lancio dei due dati sono: "+dado1+" e "+dado2);
		somma = dado1+dado2;
		
		return somma;
	}
	

}
