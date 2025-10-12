package com.mondsciomegn.salagiochi.db;


public class Utente {
    private int id;
    private String nickname;
    private String nome;
    private int score;

    public Utente(int id, String nickname, String nome, int score) {
        this.id = id;
        this.nickname = nickname;
        this.nome = nome;
        this.score = score;
    }

    public int getId() { 
    	return id;
    }
    public String getNickname() {
    	return nickname; 
    }
    public String getNome() { 
    	return nome;
    }
    public int getScore() { 
    	return score; 
    }
}
