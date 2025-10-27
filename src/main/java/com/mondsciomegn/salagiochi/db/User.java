package com.mondsciomegn.salagiochi.db;


public class User {
    private String nickname;
    private String name;
    private String password;
    private int score = 0;

    
    public User(String nickname, String name, String password, int score) {
        if (nickname == null) throw new IllegalArgumentException("Nickame non valido");
        this.nickname = nickname;
        this.name = name;
        this.password = password;
        this.score = score;
    }
    
    public User(String nickname, String name, int score) {
        this.nickname = nickname;
        this.name = name;
        this.score = 0;
    }
    
    public User(String nickname, String name, String password) {
        this.nickname = nickname;
        this.name = name;
        this.password = password;
        this.score = 0;
    }
    
    public String getNickname() {
    	return nickname; 
    }
    
    public void setNickname(String nickname) {
    	this.nickname = nickname; 
    }
    
    public String getName() { 
    	return name;
    }
    
    public void setName(String name) { 
    	this.name = name;
    }
    
    public String getPassword() {
    	return password;
    }
    
    public void setPassword(String password) { 
    	this.password = password;
    }
    
    public int getScore() { 
    	return score; 
    }
    
    public void setScore(int score) {
    	if(score < 0) throw new IllegalArgumentException("Lo score non può essere negativo!");
    		this.score = score; 
    }
    
    public void addScore(int Points) {
    	if(Points < 0) throw new IllegalArgumentException("Lo score inserito non è valido");
    	this.score += Points;
    }
    
    
}
