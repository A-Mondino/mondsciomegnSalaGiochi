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

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(user);
            
           // System.out.println("Tabelle pronte!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
