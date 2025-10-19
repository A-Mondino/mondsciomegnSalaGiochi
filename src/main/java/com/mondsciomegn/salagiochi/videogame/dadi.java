package com.mondsciomegn.salagiochi.videogame;

import java.util.Random;

import com.mondsciomegn.salagiochi.db.Category;
import com.mondsciomegn.salagiochi.db.VideoGames;


public class dadi extends VideoGames{
	
	public dadi(String name, Category category, int score) {
		super(name, category, score);
	}

	controlli controllo = new controlli();
	
	public void play() {
		int somma, sommaCasuale;
		
		System.out.println("LANCIO DEI DADI: ");
		System.out.println("Lancia due dati e il punteggio più alto vince la partita! \n\n");
		
		somma = lancia();
		System.out.println("La somma dei dati che hai lanciato è: "+somma+"\n");

		sommaCasuale = lancia();
		System.out.println("La somma dei dati lanciati dall'avversario è: "+sommaCasuale);

		
		controllo.controlloDadiMaggiore(somma, sommaCasuale);	
	}
	
	private static int lancia() {
		Random ran = new Random();
		int somma = 0, dado1, dado2;
		
		dado1 = ran.nextInt(6) + 1;

		dado2 = ran.nextInt(6) + 1;
		System.out.println("I numeri usciti dal lancio dei due dati sono: "+ dado1 + " e " + dado2);
		somma = dado1 + dado2;
		
		return somma;
	}
	

}
