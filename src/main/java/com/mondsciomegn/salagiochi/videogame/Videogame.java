package com.mondsciomegn.salagiochi.videogame;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public abstract class Videogame {
	private final int id;
	private String nameV;
	private Category category;
	private final List<CheckPoint> checkpoints = new ArrayList<>();
	
	public Videogame(int id, String nameV, Category category) {
		this.id = id;
		this.nameV = nameV;
		this.category = category;
	}
	
	
	public void gioca() {
	}
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		int scelta;
		
		System.out.println("INSERISCI IL NUMERO DEL GIOCO A CUI VUOI GIOCARE GIOCARE: ");
		System.out.println("1 - TRIS ");
		System.out.println("2 - CARTA, FORBICE E SASSO ");
		System.out.println("3 - INDOVINA IL NUMERO ");
		System.out.println("4 - LANCIO DEI DADI ");
		scelta = scanner.nextInt();
	
		switch(scelta) {
	
			case 1:
				tris Gioco1 = new tris();
				Gioco1.gioca();
				break;
			
			case 2:
				cartaForbiceSasso Gioco2 = new cartaForbiceSasso();
				Gioco2.gioca();
				break;
				
			case 3:
				indovinaNumero Gioco3 = new indovinaNumero();
				Gioco3.gioca();
				break;
				
			case 4:
				dadi Gioco4 = new dadi();
				Gioco4.gioca();
				break;
				
			default:
				System.out.println("ESCI DAL GIOCO ");
		}
		
	}

}
