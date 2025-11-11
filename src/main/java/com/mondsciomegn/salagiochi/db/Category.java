package com.mondsciomegn.salagiochi.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Category {
	private String name;							
	private String description;
	
	
	public Category(String name, String description) {
        if (name == null) 
        	throw new IllegalArgumentException("Nome categoria non valido");
        this.name = name;
		this.description = description;
	}
	
	
	public Category(String name) {
        if (name == null) 
        	throw new IllegalArgumentException("Nome categoria non valido");
        this.name = name;
        
        String sql  = "SELECT descrizione FROM category WHERE nome = ?";
    	try (Connection conn = DataBaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            	stmt.setString(1, name);   							 // Assegna il NickName
            	try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        this.description = rs.getString("descrizione");
                    } else 
                        this.description = ""; 						 
                }
               
          } catch (SQLException e1) {
        	  e1.printStackTrace();
          }
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
	
	
	public String toString() {
		return name;
	}
	
	
}