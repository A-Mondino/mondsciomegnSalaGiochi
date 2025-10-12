package com.mondsciomegn.salagiochi.db;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseContainer {
	 public static List<Utente> getAllUsers() {
	        List<Utente> utenti = new ArrayList<>();

	        String query = "SELECT id, nickname, nome, score FROM utente";

	        try (Connection conn = DataBaseConnection.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(query)) {

	            while (rs.next()) {
	                Utente u = new Utente(
	                        rs.getInt("id"),
	                        rs.getString("nickname"),
	                        rs.getString("nome"),
	                        rs.getInt("score")
	                );
	                utenti.add(u);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return utenti;
	    }
}
