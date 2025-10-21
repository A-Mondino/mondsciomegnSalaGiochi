package com.mondsciomegn.salagiochi.videogame;

import java.util.Random;
import java.util.Scanner;

import com.mondsciomegn.salagiochi.db.Category;
import com.mondsciomegn.salagiochi.db.VideoGames;


public class Tris extends VideoGames{
	public Tris(String name, Category category, int score) {
		super(name, category, score);
	}
	
	private static void stampaGriglia(char[][] areaGioco) {
	    System.out.println("\n  0   1   2"); 
	    for (int i = 0; i < 3; i++) {
	        System.out.print(i + " "); 
	        for (int j = 0; j < 3; j++) {
	            System.out.print(areaGioco[i][j]);
	            if (j < 2) System.out.print(" | ");
	        }
	        System.out.println();
	        if (i < 2) System.out.println("  -----------");
	    }
	    System.out.println();
	}
	
	private static int[] mossaCasuale(char[][] griglia) {
	    Random rand = new Random();
	    int riga, colonna;

	    do {
	        riga = rand.nextInt(3); 
	        colonna = rand.nextInt(3);
	    } while (griglia[riga][colonna] != ' ');

	    return new int[]{riga, colonna};
	}
	
	private static int tastiera(int riga) {
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Scrivere dove vuoi posizionare la figura: ");
		
		System.out.println("Scrivere il numero della riga dove posizionare la figura: ");
		riga = scanner.nextInt();

		return riga;
	}
	
	private static int tastiera(int riga, int colonna) {
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Scrivere dove vuoi posizionare la figura: ");
		
		System.out.println("Scrivere il numero della colonna dove posizionare la figura: ");
		colonna = scanner.nextInt();
		
		return colonna;
	}

	@Override
	public void play() {
		int riga = 0, colonna = 0, num;
		boolean ris, fine=false;
		final char[][] areaGioco = {
				{' ', ' ', ' '},
				{' ', ' ', ' '},
				{' ', ' ', ' '},
		};
		
		System.out.println("TRIS ");
		System.out.println("Inserisci il segno nella griglia di gioco, se fai tre segni vicini (orizzontalmente, verticalmente e in obliquo) vinci!");
		System.out.println("Inserisci il numero della riga (da 0 a 2) e della colonna (da 0 a 2) per indicare la posizione dove vuoi giocare... \n\n");


		do {
			do {
				num = 0;
				riga = tastiera(riga);
				colonna = tastiera(riga, colonna);
				
				ris = controllo(riga, colonna, areaGioco);
				
				if(ris == false) {
					System.out.println("POSIZIONE NON TROVATA");
					num++; 
				}
				
				}while(num != 0);
			
			//se il valore inserito dall'utente è corretto inserisce la X
			areaGioco[riga][colonna] = 'X';

			int mossa[] = mossaCasuale(areaGioco);
			int rigaC = mossa[0];
			int colonnaC = mossa[1];
			
			System.out.println("POSIZIONE COMPUTER " + rigaC + "," +colonnaC);
			
			//O segno computer
			areaGioco[rigaC][colonnaC] = 'O';
			
			stampaGriglia(areaGioco);
			fine = controlloTris(areaGioco);
			
		}while(fine != true);
	}
	
	private static boolean controllo(int riga, int colonna, char[][] areaGioco) {
		
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
	
	private static boolean controlloTris(char[][] areaGioco) {
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
	

}
