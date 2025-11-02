package com.mondsciomegn.salagiochi.videogame;

import java.util.Random;
import java.util.Scanner;

import com.mondsciomegn.salagiochi.db.Category;
import com.mondsciomegn.salagiochi.db.VideoGames;


public class cartaForbiceSasso extends VideoGames {
	
	public cartaForbiceSasso(String name, Category category, int score) {
		super(name, category);
	}

	public void play(String nickName) {
		Scanner scanner = new Scanner(System.in);
		Random ran = new Random();
		
		int sceltaC, scelta, num;
		
		do {
			System.out.println("INSERISCI IL NUMERO DELL'OGGETTO CHE VUOI ESSERE: ");
			System.out.println("1 - CARTA ");
			System.out.println("2 - FORBICE ");
			System.out.println("3 - SASSO ");
			scelta = scanner.nextInt();
			
			num = controlloScelta(scelta);
			
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
		controlloVittoria(scelta, sceltaC);
	}
	
	private static int controlloScelta(int scelta) {
		int num=0;
		if(scelta<1 || scelta>3) {
			System.out.println("SCELTA NON CORRETTA!!");
		}else {
			num++;
		}
		
		return num;
	}
	
	
	private static void controlloVittoria(int scelta, int sceltaC) {
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

