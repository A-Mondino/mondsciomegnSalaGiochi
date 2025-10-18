package com.mondsciomegn.salagiochi.db;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.mondsciomegn.salagiochi.videogame.Tris;

public class DataBaseContainer {
	
	 public static List<User> getAllUsers() {
	        List<User> users = new ArrayList<>();

	        String query = "SELECT * FROM utente";

	        try (Connection conn = DataBaseConnection.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(query)) {

	            while (rs.next()) {
	                User u = new User(
	                        rs.getString("nickname"),
	                        rs.getString("nome"),
	                        rs.getString("cognome"),
	                        rs.getInt("score")
	                );
	                users.add(u);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return users;
	    }
	 
	 public static List<VideoGames> getAllGames() {
		 
	        List<VideoGames> games = new ArrayList<>();

	        String query = "SELECT * FROM videogioco";

	        try (Connection conn = DataBaseConnection.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(query)) {

	            while (rs.next()) {
	                VideoGames v = new Tris(
	                        rs.getString("nome"),
	                        (new Category(rs.getString("categoria"), null)),
	                        rs.getInt("score")
	                );
	                games.add(v);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return games;
	  }

}
