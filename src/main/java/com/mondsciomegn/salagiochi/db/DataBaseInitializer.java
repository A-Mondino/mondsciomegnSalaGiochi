package com.mondsciomegn.salagiochi.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseInitializer {
    public static void initialize() {
        String user = 
                "CREATE TABLE IF NOT EXISTS utente (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "nickname VARCHAR(30)," +
                "nome VARCHAR(30), " +
                "cognome VARCHAR(30), " +
                "score INT" +
                " );" ;
        
        String videogames = 
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
        
        
        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(user);
            stmt.execute(videogames);
            stmt.execute(category);
            stmt.execute(checkpoint);
            stmt.execute(activityLog);
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
