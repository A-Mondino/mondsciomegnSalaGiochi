package com.mondsciomegn.salagiochi.db;


public class Category {
	private final String nameC;							// name of category
	private String description;
	
	public Category(String nameC, String description) {
        if (nameC == null) throw new IllegalArgumentException("Nome categoria non valido");
        this.nameC = nameC;
		this.description = description;
	}
	
	public String getNameC() {
		return nameC;
	}
	
	public String getDescription() {
		return description;
	}
	
	
}