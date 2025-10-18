package com.mondsciomegn.salagiochi.db;


public class Category {
	private final String name;							// name of category
	private String description;
	
	public Category(String name, String description) {
        if (name == null) throw new IllegalArgumentException("Nome categoria non valido");
        this.name = name;
		this.description = description;
	}
	
	
	public String getDescription() {
		return description;
	}
	public String toString() {
		return name;
	}
	
	
}