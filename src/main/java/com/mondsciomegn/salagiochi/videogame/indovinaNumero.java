package com.mondsciomegn.salagiochi.videogame;

import java.util.Random;
import java.util.Scanner;

public class indovinaNumero extends videoGame{
	Scanner scanner = new Scanner(System.in);
	Random random = new Random();
	
	public void gioca() {
		
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
