package com.mondsciomegn.salagiochi.videogame;

public class videoGames {
	private String nome;
	private String istruzione;
	private String inizio;
	private int punteggio;
	
	public videoGames(String nome, String istruzine, String inzio, int punteggio) {
		this.nome=nome;
		this.istruzione=istruzione;
		this.inizio=inizio;
		this.punteggio=punteggio;
	}
	
	 public String getNome() { 
	    	return nome;
	    }
	 
	 public String getIstruzione() { 
	    	return istruzione;
	    }
	 
	 public String getInizio() { 
	    	return inizio;
	    }

	 public int getPunteggio() { 
	    	return punteggio;
	    }

}
