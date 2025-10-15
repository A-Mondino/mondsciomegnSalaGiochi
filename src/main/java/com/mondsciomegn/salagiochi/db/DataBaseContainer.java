package com.mondsciomegn.salagiochi.db;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseContainer {
	 public static List<User> getAllUsers() {
	        List<User> users = new ArrayList<>();

	        String query = "SELECT * FROM utente";

	        try (Connection conn = DataBaseConnection.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(query)) {

	            while (rs.next()) {
	                User u = new User(
	                        rs.getInt("id"),
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
}
