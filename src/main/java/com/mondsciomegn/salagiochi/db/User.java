package com.mondsciomegn.salagiochi.db;


public class User {
    private int id;
    private String nickname;
    private String name;
    private String surname;
    private int score;

    
    public User(int id, String nickname, String name, String surname, int score) {
        if (nickname == null) throw new IllegalArgumentException("Nickame non valido");
        this.id = id;
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
