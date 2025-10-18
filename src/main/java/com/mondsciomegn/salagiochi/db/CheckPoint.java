package com.mondsciomegn.salagiochi.db;

import java.util.Objects;

public class CheckPoint {
	private final int id;							// name of category
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
	
	public boolean equals(Object o) {			// confronto oggetti CheckPoint
    	if (this == o)
    		return true;
    	if (!(o instanceOf CheckPoint)) 
    		return false;
    	CheckPoint cp = (CheckPoint) o;
    	return id == cp.id;
    }
    
    public int hashCode() {						
    	return Objects.hash(id);
    }
}