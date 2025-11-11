package com.mondsciomegn.salagiochi.videogame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

import com.mondsciomegn.salagiochi.db.Category;
import com.mondsciomegn.salagiochi.db.DataBaseConnection;
import com.mondsciomegn.salagiochi.db.VideoGames;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class Roulette extends VideoGames{

	Scanner scanner = new Scanner(System.in);
	Random random = new Random();

    private List<Integer> numeri = new ArrayList<>();
    private Button[][] buttons = new Button[9][9];
    private char[][] playGrid = new char[9][9];
    private int index = 0;

    private GridPane grid = new GridPane();
    private boolean gameOver = false;

    private Stage primaryStage = new Stage();
    
	public Roulette(String name, Category category) {
		super(name, category);
	}
	
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
    	
    	primaryStage.setTitle("Roulette");
    	grid.setHgap(5);
    	grid.setVgap(5);
    	grid.setStyle("-fx-background-color: black; -fx-padding: 10;");

		numeri.clear();
    	int index = 0;

    	for (int n = 1; n <= 40; n++) {
    	    numeri.add(n);
    	    numeri.add(n);
    	}

    	// Label centrale per messaggi
    	Label messageLabel = new Label("Benvenuto nella Roulette!");
    	messageLabel.setMinSize(120, 120);
    	messageLabel.setPrefSize(120, 120);
    	messageLabel.setAlignment(Pos.CENTER);

    	for (int i = 0; i < 9; i++) {
    	    for (int j = 0; j < 9; j++) {

    	        if (i == 4 && j == 4) {
    	            grid.add(messageLabel, j, i);
    	            continue;
    	        }

    	        Button btn = new Button();
    	        btn.setMinSize(70, 70);

    	        if (index < numeri.size()) {
    	            int numero = numeri.get(index);
    	            btn.setText(String.valueOf(numero));

    	            long occorrenze = numeri.subList(0, index).stream()
    	                    .filter(n -> n == numero)
    	                    .count();
    	            String color = (occorrenze == 0) ? "#ff0000" : "#000000"; // rosso o nero

    	            btn.setStyle(
    	                "-fx-font-size: 20px; " +
    	                "-fx-font-weight: bold; " +
    	                "-fx-text-fill: white; " +
    	                "-fx-background-color: " + color + ";"
    	            );

    	            final int num = numero;
    	            btn.setOnAction(e -> {
    	                messageLabel.setText("Hai selezionato il numero " + num);
    	            });

    	            index++;

    	        } else {
    	            btn.setDisable(true);
    	            btn.setStyle("-fx-background-color: #444444;");
    	        }

    	        grid.add(btn, j, i);
    	    }
    	}

    	Scene scene = new Scene(grid, 720, 720);
    	primaryStage.setScene(scene);
    	primaryStage.show();
		
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