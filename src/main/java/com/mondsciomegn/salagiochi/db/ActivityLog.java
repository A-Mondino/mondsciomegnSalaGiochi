package com.mondsciomegn.salagiochi.db;

import java.sql.Date;


public class ActivityLog {
	private User user;
	private VideoGames videogame;
	private Date dateGame;
	private int score;
	
	
	public ActivityLog(User user, VideoGames videogame, Date dateGame, int score) {
		this.user = user;
		this.videogame = videogame;
		this.dateGame = dateGame;
		this.score = score;
	}
	
	
	public User getUser() {
		return user;
	}
	
	
	public void setUser(User user) {
		this.user = user;
	}
	
	
	public VideoGames getVideogame() {
		return videogame;
	}
	
	
	public void setVideogame(VideoGames videogame) {
		this.videogame = videogame;
	}
	
	
	public Date getDateGame() {
		return dateGame;
	}
	
	
	public void setDateGame(Date dateGame) {
		this.dateGame = dateGame;
	}
	
	
	public int getScore() {
		return score;
	}
	
	
	public void setScore(int score) {
		this.score = score;
	}
	
	
}
	