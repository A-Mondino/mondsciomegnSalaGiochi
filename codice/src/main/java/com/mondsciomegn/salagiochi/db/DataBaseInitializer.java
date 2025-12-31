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
        
        
        String activityLog =
        		"CREATE TABLE IF NOT EXISTS activityLog (" +
        		"id INT AUTO_INCREMENT PRIMARY KEY," +
        		"nickname  VARCHAR(30)," +
        		"videogioco  VARCHAR(30)," +
                "data_partita TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
        		"score INT" +
                " );" ;
        
        
        String createCategorys =  "INSERT INTO category (nome, descrizione) " +
        						  "SELECT 'Arcade', 'Giochi vecchio stile :D'" +
        						  "WHERE NOT EXISTS (SELECT 1 FROM category WHERE nome = 'Arcade')" +
        						  "UNION ALL " +
        						  "SELECT 'Strategia', 'Giochi di tattica'" + 
        						  "WHERE NOT EXISTS (SELECT 1 FROM category WHERE nome = 'Strategia')" +
        						  "UNION ALL " +
        						  "SELECT 'Fortuna', 'Giochi di fortuna'" + 
        						  "WHERE NOT EXISTS (SELECT 1 FROM category WHERE nome = 'Fortuna');";
        						  
        
        String createVideogames = "INSERT INTO videogioco (nome, categoria, score)" +
        						  "SELECT 'Tris', 'Arcade', 300 " +
        						  "WHERE NOT EXISTS (SELECT 1 FROM videogioco WHERE nome = 'Tris')" + 
        						  "UNION ALL " + 
        						  "SELECT 'Battaglia Navale', 'Strategia', 1000 " +
        						  "WHERE NOT EXISTS (SELECT 1 FROM videogioco WHERE nome = 'Battaglia Navale')" + 
        						  "UNION ALL " + 
        						  "SELECT 'Roulette', 'Fortuna', 600 " +
        						  "WHERE NOT EXISTS (SELECT 1 FROM videogioco WHERE nome = 'Roulette')" + 
        						  "UNION ALL " + 
        						  "SELECT 'LanciaDadi', 'Fortuna', 50 " +
        						  "WHERE NOT EXISTS (SELECT 1 FROM videogioco WHERE nome = 'TrovaDadi');";
       
        String createComputerAcc = "INSERT INTO utente (nickname, nome, psww, score)" +
				  				   "SELECT '_COMPUTER_', 'computer', 'computer123' , 0 " +
				  				   "WHERE NOT EXISTS (SELECT 1 FROM utente WHERE nickname = '_COMPUTER_');"; 
        
        String createAnonimousAcc = "INSERT INTO utente (nickname, nome, psww, score)" +
				  					"SELECT '_ANONIMO_', 'anonimo', '' , 0 " +
				  					"WHERE NOT EXISTS (SELECT 1 FROM utente WHERE nickname = '_ANONIMO_');";
        
        
        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
        	
        	//stmt.execute("DROP TABLE videogioco");
        	stmt.execute(user);
            stmt.execute(videogame);
            stmt.execute(category);
            stmt.execute(activityLog);
            stmt.executeUpdate(createComputerAcc);
            stmt.executeUpdate(createAnonimousAcc);
            stmt.executeUpdate(createCategorys);
            stmt.executeUpdate(createVideogames);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
