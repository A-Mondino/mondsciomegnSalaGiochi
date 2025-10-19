package com.mondsciomegn.salagiochi.db;


public class CheckPoint {
	private int id;							
	private int points;
	
	public CheckPoint(int id, int points) {
        if (points < 0) throw new IllegalArgumentException("I punti non possono essere negativi");
        this.id = id;
		this.points = points;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getPoints() {
		return points;
	}
	
	public void setPoints(int points) {
		this.points = points;
	}
	
	
}