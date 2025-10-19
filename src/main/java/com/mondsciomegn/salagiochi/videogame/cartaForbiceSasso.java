package com.mondsciomegn.salagiochi.videogame;

import java.util.Random;
import java.util.Scanner;

import com.mondsciomegn.salagiochi.db.Category;
import com.mondsciomegn.salagiochi.db.VideoGames;


public class cartaForbiceSasso extends VideoGames {
	
	public cartaForbiceSasso(String name, Category category, int score) {
		super(name, category, score);
	}

	public void play() {
		Scanner scanner = new Scanner(System.in);
		Random ran = new Random();
		controlli c = new controlli();
		
		int sceltaC, scelta, num;
		
		do {
			System.out.println("INSERISCI IL NUMERO DELL'OGGETTO CHE VUOI ESSERE: ");
			System.out.println("1 - CARTA ");
			System.out.println("2 - FORBICE ");
			System.out.println("3 - SASSO ");
			scelta = scanner.nextInt();
			
			num = controlli.controlloScelta(scelta);
			
		}while(num == 0);
		
		sceltaC = ran.nextInt(3) + 1;
		
		switch(sceltaC) {
			case 1:
				System.out.println("L'avversario ha scelto: CARTA");
				break;
				
			case 2:
				System.out.println("L'avversario ha scelto: FORBICE");
				break;
			
			case 3:
				System.out.println("L'avversario ha scelto: SASSO");
				break;
				
			default:
				break;
		}
		controlli.controlloVittoria(scelta, sceltaC);
	}
}

