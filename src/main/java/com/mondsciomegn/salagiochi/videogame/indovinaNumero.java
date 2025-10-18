package com.mondsciomegn.salagiochi.videogame;

import java.util.Random;
import java.util.Scanner;

import com.mondsciomegn.salagiochi.db.Category;
import com.mondsciomegn.salagiochi.db.VideoGames;

public class indovinaNumero extends VideoGames{
	
	public indovinaNumero(String name, Category category, int score) {
		super(name, category, score);
		
	}

	Scanner scanner = new Scanner(System.in);
	Random random = new Random();
	
	public void play() {
		
		int numeroCasuale, numero, cont=0;
		boolean uscita;
		
		controlli controlli = new controlli();
		
		numeroCasuale = random.nextInt(100);
		System.out.println(numeroCasuale);

		
		do {
			System.out.println("Indovina il numero selezionato: ");
			numero = scanner.nextInt();
			
			cont++;
			
			uscita = controlli.controlloNumeroCasuale(numero, numeroCasuale);
		
		}while(uscita != false && cont<5);	
			
		if(cont==5) {
			System.out.println("HAI PERSO!");
		}
	}
	

}
