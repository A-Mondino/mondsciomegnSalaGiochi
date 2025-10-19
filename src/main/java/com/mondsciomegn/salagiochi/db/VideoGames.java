package com.mondsciomegn.salagiochi.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public abstract class VideoGames {
	private final int id = 0;
	private String name;
	private Category category;
	private int score = 0;
	private List<CheckPoint> checkpoints = new ArrayList<>();
	private float time = 0;
	
	public VideoGames(String name, Category category, int score) {
		this.name = name;
		this.category = category;
		this.score = score;
	}
	
	public abstract void play();
	
	private void startTimer() {
		// da finire
	}
	
	public int getId() {
		return id;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

	public List<CheckPoint> getCheckpoints() {
		return checkpoints;
	}
	
	public void setCheckPoint(List<CheckPoint> checkpoints) {
		this.checkpoints = checkpoints;
	}
	
	
}
