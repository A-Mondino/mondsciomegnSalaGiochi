package com.mondsciomegn.salagiochi.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class DataBaseInitializer {
    public static void initialize() {
    	
        String user = 
                "CREATE TABLE IF NOT EXISTS utente (" +
                "nickname VARCHAR(30) PRIMARY KEY," +
                "nome VARCHAR(30), " +
                "psww VARCHAR(30), " +
                "score INT" +
                " );" ;
        
        String videogame = 
                "CREATE TABLE IF NOT EXISTS videogioco (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "nome VARCHAR(30)," +
                "categoria VARCHAR(30), " +
                "score INT" +
                " );" ;
        
        String category = 
                "CREATE TABLE IF NOT EXISTS category (" +
                "nome VARCHAR(30) PRIMARY KEY," +
                "descrizione VARCHAR(50) " +
                " );" ;
        
        String checkpoint =
        		"CREATE TABLE IF NOT EXISTS checkpoint (" +
        		"id INT AUTO_INCREMENT PRIMARY KEY," +
                "punti INT " +
                " );" ;
        
        String activityLog =
        		"CREATE TABLE IF NOT EXISTS activityLog (" +
        		"id INT AUTO_INCREMENT PRIMARY KEY," +
                "data_partita TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
        		"punteggio INT" +
                " );" ;
        
        
        String createCategorys =  "INSERT INTO category (nome, descrizione) " +
        						  "SELECT 'Arcade', 'Giochi vecchio stile :D'" +
        						  "WHERE NOT EXISTS (SELECT 1 FROM category WHERE nome = 'Arcade')" +
        						  "UNION ALL " +
        						  "SELECT 'Strategia', 'Giochi di tattica'" + 
        						  "WHERE NOT EXISTS (SELECT 1 FROM category WHERE nome = 'Strategia');";
        						  
        
        String createVideogames = "INSERT INTO videogioco (nome, categoria, score)" +
        						  "SELECT 'Tris', 'Arcade', 100 " +
        						  "WHERE NOT EXISTS (SELECT 1 FROM videogioco WHERE nome = 'Tris')" + 
        						  "UNION ALL " + 
        						  "SELECT 'Battaglia Navale', 'Strategia', 1000 " +
        						  "WHERE NOT EXISTS (SELECT 1 FROM videogioco WHERE nome = 'Battaglia Navale');";
       
        String createUsers = 	  "INSERT INTO utente (nickname, nome, psww, score)" +
				  				  "SELECT '_COMPUTER_', 'computer', 'computer123' , 0 " +
				  				  "WHERE NOT EXISTS (SELECT 1 FROM utente WHERE nickname = '_COMPUTER_');"; 
				  				  
        
        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
        	
            stmt.execute(user);
            stmt.execute(videogame);
            stmt.execute(category);
            stmt.execute(checkpoint);
            stmt.execute(activityLog);
            stmt.executeUpdate(createUsers);
            stmt.executeUpdate(createCategorys);
            stmt.executeUpdate(createVideogames);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
