package com.mondsciomegn.salagiochi.db;


public class User {
    private final int id = 0;
    private String nickname;
    private String name;
    private String surname;
    private int score;

    
    public User(String nickname, String name, String surname, int score) {
        if (nickname == null) throw new IllegalArgumentException("Nickame non valido");
        this.nickname = nickname;
        this.name = name;
        this.surname = surname;
        this.score = 0;
    }
    //

    public int getId() { 
    	return id;
    }
    
    public String getNickname() {
    	return nickname; 
    }
    
    public String getName() { 
    	return name;
    }
    
    public String getSurname() {
    	return surname;
    }
    
    public int getScore() { 
    	return score; 
    }
    
}
