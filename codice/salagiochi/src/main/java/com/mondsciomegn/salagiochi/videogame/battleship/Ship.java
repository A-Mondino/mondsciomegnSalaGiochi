package com.mondsciomegn.salagiochi.videogame.battleship;

public class Ship {
    private final char id;
    private final int size;
    private boolean isPlaced;
    private int hits;

    public Ship(char id, int size) {
        this.id = id;
        this.size = size;
        this.hits = 0;
        this.isPlaced = false;
    }

    public void addHit() {
        if (hits < size) 
        	hits++;
    }

    public boolean isSunk() {
        return hits >= size;
    }
    
    public boolean isPlaced() {
    	return this.isPlaced;
    }
    
    public void setIsPlaced(boolean isPlaced) {
    	this.isPlaced = isPlaced;
    }
    
    public char getId() { return id; }
    public int getSize() { return size; }
    
}