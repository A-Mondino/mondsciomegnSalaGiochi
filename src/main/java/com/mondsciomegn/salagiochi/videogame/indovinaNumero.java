package com.mondsciomegn.salagiochi.videogame;

import java.util.Random;
import java.util.Scanner;

import com.mondsciomegn.salagiochi.db.Category;
import com.mondsciomegn.salagiochi.db.VideoGames;


public class indovinaNumero extends VideoGames{
	
	public indovinaNumero(String name, Category category, int score) {
		super(name, category);
	}

	Scanner scanner = new Scanner(System.in);
	Random random = new Random();
	
	public void play(String nickName) {
		
		int numeroCasuale, numero, cont=0;
		boolean uscita;
				
		numeroCasuale = random.nextInt(100);
		System.out.println(numeroCasuale);

		
		do {
			System.out.println("Indovina il numero selezionato: ");
			numero = scanner.nextInt();
			
			cont++;
			
			uscita = controlloNumeroCasuale(numero, numeroCasuale);
		
		}while(uscita != false && cont < 5);	
			
		if(cont == 5) {
			System.out.println("HAI PERSO!");
		}
	}
	
	private static boolean controlloNumeroCasuale(int numero, int numeroCasuale) {
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
	

}
