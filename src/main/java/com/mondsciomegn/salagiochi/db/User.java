package com.mondsciomegn.salagiochi.db;

import java.util.Objects;

public class User {
    private final int id = 0;
    private String nickname;
    private String name;
    private String surname;
    private int score = 0;

    
    public User(String nickname, String name, String surname, int score) {
        if (nickname == null) throw new IllegalArgumentException("Nickame non valido");
        this.nickname = nickname;
        this.name = name;
        this.surname = surname;
        this.score = score;
    }
    //

    public int getId() { 
    	return id;
    }
    
    public void setId(int id) {
    	this.id = id;
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
    
    public String getSurname() {
    	return surname;
    }
    
    public void setSurname() { 
    	this.surname = surname;
    }
    
    public int getScore() { 
    	return score; 
    }
    
    public void setScore() {
    	if(score < 0) throw new IllegalArgumentException("Lo score non può essere negativo!");
    	this.score = score; 
    }
    
    public void addScore(int Points) {
    	if(Points < 0) throw new IllegalArgumentException("Lo score inserito non è valido");
    	this.score += Points;
    }
    
    public boolean equals(Object o) {			// confronto oggetti User
    	if (this == o)
    		return true;
    	if (!(o instanceOf User)) 
    		return false;
    	User user = (User) o;
    	return id == user.id;
    }
    
    public int hashCode() {						
    	return Objects.hash(id);
    }
    
}
