package com.mondsciomegn.salagiochi.videogame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

import com.mondsciomegn.salagiochi.db.Category;
import com.mondsciomegn.salagiochi.db.DataBaseConnection;
import com.mondsciomegn.salagiochi.db.VideoGames;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;


public class DiceGuesser extends VideoGames{
	
	public DiceGuesser(String name, Category category) {
		super(name, category);
	}

	
	Scanner scanner = new Scanner(System.in);
	Random random = new Random();
	
	
	public void play(String nickName) {
		Dialog<ButtonType> dialog = new Dialog<>();
    	dialog.setTitle("Dettagli Gioco");
    	dialog.setHeaderText("Istruzioni:");
    	dialog.setContentText(
    	        "Indovina il numero estratto in 5 tentativi per vincere il gioco."
    	);
    	
    	ButtonType play = new ButtonType("Gioca", ButtonBar.ButtonData.OK_DONE);
    	ButtonType cancel = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);

    	dialog.getDialogPane().getButtonTypes().setAll(play, cancel);

    	Optional<ButtonType> result = dialog.showAndWait();
    	if (!result.isPresent() || result.get() == cancel) {
    	    return;
    	}

    	startGame(nickName);
	}
	
	
	private void startGame(String nickname) {
		
    	if(nickname.isEmpty()) {					// Significa che qualcuno sta giocando in anonimo
            String sql = "INSERT INTO utente (nickname, nome, psww, score)" +
	  				  "SELECT '_ANONIMO_', 'Anonimo', '' , 0 " +
	  				  "WHERE NOT EXISTS (SELECT 1 FROM utente WHERE nickname = '_ANONIMO_');"; 
            
            try (Connection conn = DataBaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {
                    try {
                        stmt.executeUpdate(); 		// Prova a inserire l'utente

                    } catch (SQLException ex) { 
                    	ex.printStackTrace();
                    }
                   
              } catch (SQLException e1) {
            	  e1.printStackTrace();
              }
        }
		
	}
	
	private boolean controlloNumeroCasuale(int numero, int numeroCasuale) {
		if(numero<numeroCasuale) {
			showMessage("Il numero che hai inserito è più PICCOLO del numero estratto");
			return true;
		}else {
			
			if(numero>numeroCasuale) {
				showMessage("Il numero che hai inserito è più GRANDE del numero estratto");
				return true;
			}else {
				if(numero==numeroCasuale) {
					showMessage("HAI VINTO!");
					return false;
				}
			}
		}
		return true;
	}
	
	private void showMessage(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Risultato");
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
	

}
