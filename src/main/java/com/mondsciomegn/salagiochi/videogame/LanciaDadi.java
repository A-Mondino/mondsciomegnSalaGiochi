package com.mondsciomegn.salagiochi.videogame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Random;

import com.mondsciomegn.salagiochi.db.Category;
import com.mondsciomegn.salagiochi.db.DataBaseConnection;
import com.mondsciomegn.salagiochi.db.VideoGames;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class LanciaDadi extends VideoGames{
	
    private boolean gameOver = false;
    private Stage primaryStage = new Stage();
	
    
	public LanciaDadi(String name, Category category) {
		super(name, category);
	}
	
	
	public void play(String nickName) { 
		Dialog<ButtonType> dialog = new Dialog<>();
    	dialog.setTitle("Dettagli Gioco");
    	dialog.setHeaderText("Istruzioni:");
    	dialog.setContentText(
    	        "Lancia i due dadi, chi ottiene la somma maggiore dei dati lanciati vince!\n" +
                "Se entrambi i giocatori ottengono la stessa somma dal lancio dei dadi " +
                "il gioco finisce in pareggio."
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
		if(nickname.isEmpty()) {														// Significa che qualcuno sta giocando in anonimo
            String sql = "INSERT INTO utente (nickname, nome, psww, score)" +
	  				  "SELECT '_ANONIMO_', 'Anonimo', '' , 0 " +
	  				  "WHERE NOT EXISTS (SELECT 1 FROM utente WHERE nickname = '_ANONIMO_');"; 
            
            try (Connection conn = DataBaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {
                    try {
                        stmt.executeUpdate(); 											// Prova a inserire l'utente
                    } catch (SQLException ex) { 
                    	ex.printStackTrace();
                    }
                   
              } catch (SQLException e1) {
            	  e1.printStackTrace();
              }
        }
		
		primaryStage.setTitle("Gioco dei Dadi");

        Label title = new Label("Lancio dei Dadi");
        Label playerLabel = new Label("Giocatore:");
        Label computerLabel = new Label("Computer:");
        Label message = new Label("-");
        Label messaggio1 = new Label("-");

        Button lanciaButton = new Button("Lancia i Dadi");

        // Evento OnClick
        lanciaButton.setOnAction(e -> {
            if (!gameOver) {
                int sommaGiocatore = lancia();
                message.setText("La somma dei dadi che hai lanciato è: " +sommaGiocatore);

                int sommaComputer = lancia();
                messaggio1.setText("La somma dei dadi lanciati dall'avversario è: " +sommaComputer);

                controlloDadiMaggiore(sommaGiocatore, sommaComputer, nickname);
                gameOver = true;
            }
        });
        
        VBox centro = new VBox(10);
        centro.setStyle("-fx-alignment: center;");
        centro.getChildren().addAll(title, playerLabel, message,
        	    computerLabel, messaggio1);

        BorderPane layout = new BorderPane();
        layout.setCenter(centro);
        layout.setBottom(lanciaButton);
        BorderPane.setAlignment(lanciaButton, javafx.geometry.Pos.CENTER);
        layout.setStyle("-fx-padding: 20;");

        Scene scene = new Scene(layout, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
		
	}
	
	
	private static int lancia() {
		Random ran = new Random();
		int somma = 0, dado1, dado2;
		
		dado1 = ran.nextInt(6) + 1;
		dado2 = ran.nextInt(6) + 1;
		somma = dado1 + dado2;
		
		return somma;
	}
	
	
	private void controlloDadiMaggiore(int somma, int sommaC, String nickName) {
		if(somma<sommaC) {
			showMessage("HAI PERSO!");
            addPoints(nickName);
		}else {
			if(somma>sommaC) {
				showMessage("HAI VINTO!");
	            addPoints(nickName);
			}else {
				if(somma==sommaC) {
					showMessage("PAREGGIO!");
		            addPoints(nickName);
				}
			}
		}
	}
	
	
	
	  private void showMessage(String messaggio) {
	        Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle("Risultato");
	        alert.setHeaderText(null);
	        alert.setContentText(messaggio);
	        alert.showAndWait();
	    }
	

}
