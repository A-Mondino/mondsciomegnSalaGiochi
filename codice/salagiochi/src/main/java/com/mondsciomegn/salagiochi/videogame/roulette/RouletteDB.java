package com.mondsciomegn.salagiochi.videogame.roulette;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mondsciomegn.salagiochi.db.DataBaseConnection;

public class RouletteDB {

    public int getScoreFromDB(String nickname) {
        String nicknameDB = (nickname == null || nickname.isEmpty()) ? "_ANONIMO_" : nickname;
        String sql = "SELECT score FROM utente WHERE nickname = ?";
        
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nicknameDB);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("score");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0; // O un valore di default
    }

    public void updateScore(String nickname, int newTotalScore) {
        String nicknameDB = (nickname == null || nickname.isEmpty()) ? "_ANONIMO_" : nickname;
        String sql = "UPDATE utente SET score = ? WHERE nickname = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newTotalScore);
            stmt.setString(2, nicknameDB);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}