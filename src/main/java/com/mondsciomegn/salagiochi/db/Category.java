package com.mondsciomegn.salagiochi.db;

import java.util.Objects;

public class Category {
	private final String name;							// name of category
	private String description;
	
	public Category(String name, String description) {
        if (name == null) throw new IllegalArgumentException("Nome categoria non valido");
        this.name = name;
		this.description = description;
	}
	
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean equals(Object o) {			// confronto oggetti Category
    	if (this == o)
    		return true;
    	if (!(o instanceOf Category)) 
    		return false;
    	Category c = (Category) o;
    	return name.equals(.name);
    }
    
    public int hashCode() {						
    	return name.hashCode();
    }
	
	
}