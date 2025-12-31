package com.mondsciomegn.salagiochi.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.mondsciomegn.salagiochi.videogame.Tris;


public class DataBaseContainer {
	
	private DataBaseContainer() {}

	 public static List<User> getAllUsers() {
	        List<User> users = new ArrayList<>();

	        String query = "SELECT * FROM utente ORDER BY score DESC, nickname ASC";

	        try (Connection conn = DataBaseConnection.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(query)) {

	            while (rs.next()) {
	                User u = new User(
	                        rs.getString("nickname"),
	                        rs.getString("nome"),
	                        rs.getString("psww"),
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

	        String query = "SELECT * FROM videogioco ORDER BY score DESC";

	        try (Connection conn = DataBaseConnection.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(query)) {
	        	
	            while (rs.next()) {
	                games.add(new VideoGames(rs.getString("nome"), new Category(rs.getString("categoria"))));
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return games;
	  }

	public static List<ActivityLog> getAllActivity() {
		List<ActivityLog> activity = new ArrayList<>();
		String query = "SELECT * FROM activityLog ORDER BY data_partita DESC";

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
        	
            while (rs.next()) {
            	activity.add(
            			new ActivityLog(
            					new User(rs.getString("nickname")),
            					new VideoGames(rs.getString("videogioco"), null),
            					rs.getDate("data_partita"),
            					rs.getInt("score")
            					));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
		return activity;
	}

	 
}
