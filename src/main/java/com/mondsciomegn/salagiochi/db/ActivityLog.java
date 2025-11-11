package com.mondsciomegn.salagiochi.db;

import java.time.LocalDateTime;


public class ActivityLog {
	private User user;
	private VideoGames videogame;
	private LocalDateTime dateGame;
	private int score;
	
	
	public ActivityLog(User user, VideoGames videogame, LocalDateTime dateGame, int score) {
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
	
	
	public LocalDateTime getDateGame() {
		return dateGame;
	}
	
	
	public void setDateGame(LocalDateTime dateGame) {
		this.dateGame = dateGame;
	}
	
	
	public int getScore() {
		return score;
	}
	
	
	public void setScore(int score) {
		this.score = score;
	}
	
	
}
	