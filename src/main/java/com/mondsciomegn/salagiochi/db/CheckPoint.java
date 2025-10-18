package com.mondsciomegn.salagiochi.db;


public class CheckPoint {
	private final int id;							// name of category
	private int points;
	
	public CheckPoint(int id, int points) {
        if (points < 0) throw new IllegalArgumentException("I punti non possono essere negativi");
        this.id = id;
		this.points = points;
	}
	
	public int detId() {
		return id;
	}
	
	public int getPoints() {
		return points;
	}
	
	
}