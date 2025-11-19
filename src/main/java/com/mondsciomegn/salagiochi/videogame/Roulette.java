package com.mondsciomegn.salagiochi.videogame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

import com.mondsciomegn.salagiochi.db.Category;
import com.mondsciomegn.salagiochi.db.DataBaseConnection;
import com.mondsciomegn.salagiochi.db.VideoGames;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Roulette extends VideoGames{

	private Random random = new Random();						// Per estrazione vincente

    private List<Integer> numbers = new ArrayList<>();
    private GridPane grid = new GridPane();						// Per la visualizzazione grafica
    private Stage primaryStage = new Stage();
    
    
	public Roulette(String name, Category category) {
		super(name, category);
		setScore(600);
	}
	
	
	public void play(String nickname) {
		setNickname(nickname);
		Dialog<ButtonType> dialog = new Dialog<>();
    	dialog.setTitle("Dettagli Gioco");
    	dialog.setHeaderText("Istruzioni:");
    	dialog.setContentText(
    	        "Punto su uno dei bottoni sulla mappa di gioco"
    	);
    	
    	ButtonType play = new ButtonType("Gioca", ButtonBar.ButtonData.OK_DONE);
    	ButtonType cancel = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);

    	dialog.getDialogPane().getButtonTypes().setAll(play, cancel);

    	Optional<ButtonType> result = dialog.showAndWait();
    	if (!result.isPresent() || result.get() == cancel) {
    	    return;
    	}

    	startGame();
	}
	
	
	private void startGame() {
    	if(getNickname().isEmpty()) {					// Significa che qualcuno sta giocando in anonimo
            String sql = "INSERT INTO utente (nickname, nome, psww, score)" +
	  				  "SELECT '_ANONIMO_', 'Anonimo', '' , 0 " +
	  				  "WHERE NOT EXISTS (SELECT 1 FROM utente WHERE nickname = '_ANONIMO_');"; 
            
            try (Connection conn = DataBaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {
                    try {
                        stmt.executeUpdate(); 			// Prova a inserire l'utente

                    } catch (SQLException ex) { 
                    	ex.printStackTrace();
                    }
                   
              } catch (SQLException e1) {
            	  e1.printStackTrace();
              }
        }
    	
    	primaryStage.setTitle("Roulette Orizzontale");
    	grid.getChildren().clear();
    	grid.setHgap(5);
    	grid.setVgap(5);
    	grid.setStyle("-fx-background-color: darkgreen; -fx-padding: 20;");

    	Label messageLabel = new Label("Benvenuto nella Roulette!");

    	// Mappa colori ufficiali roulette
    	Map<Integer, String> colors = new HashMap<>();
    	int[] redNumbers = {1,3,5,7,9,12,14,16,18,19,21,23,25,27,30,32,34,36};
    	int[] blackNumbers = {2,4,6,8,10,11,13,15,17,20,22,24,26,28,29,31,33,35};

    	for (int r : redNumbers) colors.put(r, "red");
    	for (int b : blackNumbers) colors.put(b, "black");

    	// Creazione di tre righe orizzontali contenenti i nuemri di gioco
    	int[][] layout = {
    	    {1, 4, 7, 10, 13, 16, 19, 22, 25, 28, 31, 34},
    	    {2, 5, 8, 11, 14, 17, 20, 23, 26, 29, 32, 35},
    	    {3, 6, 9, 12, 15, 18, 21, 24, 27, 30, 33, 36}
    	};

    	for (int row = 0; row < 3; row++) {
    	    for (int col = 0; col < layout[row].length; col++) {

    	        int num = layout[row][col];
    	        String color = colors.get(num);

    	        Button btn = new Button(String.valueOf(num));
    	        btn.setMinSize(70, 70);
    	        btn.setStyle(
    	            "-fx-font-size: 20px;" +
    	            "-fx-font-weight: bold;" +
    	            "-fx-text-fill: white;" +
    	            "-fx-background-color:" + color + ";"
    	        );

    	        /*btn.setOnAction(e -> {

    	            String colourUser = color.equals("red") ? "rosso" : "nero";

    	            // COMPUTER
    	            int extractComputer = new Random().nextInt(38); // 0-37 (37 = 00)
    	            int numComputer = extractComputer == 37 ? 100 : extractComputer;
    	            String colourComputer = "verde";
    	            if (extractComputer >= 1 && extractComputer <= 36) {
    	                colourComputer = colors.get(extractComputer).equals("red") ? "rosso" : "nero";
    	            }

    	            // VINCENTE
    	            int extractWinner = new Random().nextInt(38);
    	            int numWinner = extractWinner == 37 ? 100 : extractWinner;
    	            String colourWinner = "verde";
    	            if (extractWinner >= 1 && extractWinner <= 36) {
    	                colourWinner = colors.get(extractWinner).equals("red") ? "rosso" : "nero";
    	            }

    	            messageLabel.setText(
    	                "Hai scelto: " + num + " (" + colourUser + ")\n\n" +
    	                "Computer: " + (numComputer == 100 ? "00" : numComputer) + " (" + colourComputer + ")\n\n" +
    	                "Numero vincente: " + (numWinner == 100 ? "00" : numWinner) + " (" + colourWinner + ")\n\n" +
    	                "Clicca per continuare"
    	            );

    	            checkWin(
    	                num,
    	                numComputer,
    	                numWinner,
    	                colourUser, colourComputer, colourWinner
    	            );
    	        }); */

    	        grid.add(btn, col, row + 1);
    	    }

    	    // Aggiunta colonna con bottoni bianchi che puntano all'intera riga
    	    Button empty = new Button("Punta sull'intera riga");
    	    empty.setMinSize(70, 70);
    	    empty.setStyle("-fx-background-color: white; -fx-border-color: white;");
    	    grid.add(empty, 12, row + 1); // colonna 12 perché 0–11 sono numeri
    	}

    	String[] choiceColomns = {
    		"Punta sui primi 12 numeri",
    		"Punta sui numeri da 13 a 24",
    		"Punta sui numeri da 25 a 36",
    	};
    	
    	// Aggiunta riga con 3 bottni larghi 4 colonne
    	for (int i = 0; i < 3; i++) {
    	    Button extra = new Button(choiceColomns[i]);
    	    extra.setMinSize(70 * 4 + 18, 70); 											// 4 colonne + spazio
    	    extra.setStyle("-fx-background-color: white; -fx-border-color: black;");
    	    grid.add(extra, i * 4, 4, 4, 1);
    	}
    	
    	// Array contenenti caratteristiche dei 6 bottoni
    	String[] choiceExtra = {
    		    "Punta 1-18",
    		    "Pari",
    		    "",
    		    "",
    		    "Dispari",
    		    "Punta 19-36"
    		};

    		String[] colorsExtra = {
    		    "white",
    		    "white",
    		    "red",
    		    "black",
    		    "white",
    		    "white"
    		};
    		
    		// Aggiunta riga con bottini larghi 2 colonne
    		for (int i = 0; i < 6; i++) {
    		    Button b = new Button(choiceExtra[i]);
        	    b.setMinSize(70 * 2 + 7, 70); 												// 2 colonne + spazio
    		    b.setStyle(
    		        "-fx-font-size: 16px;" +
    		        "-fx-font-weight: bold;" +
    		        "-fx-background-color: " + colorsExtra[i] + ";" +
    		        "-fx-border-color: black;"
    		    );
    		    
    		    grid.add(b, i*2, 5, 2, 1); 
    		}

    	// Messaggi sotto
    	grid.add(messageLabel, 0, 6, 12, 1);
    	
    	// Nuova finestra per i gettoni
    	Stage coinStage = new Stage();
    	coinStage.setTitle("Gettoni");

    	VBox coinBox = new VBox(10);

    	// Label e TextField per inserire soldi
    	Label coinLabel = new Label("Gettoni:");
    	
    	coinBox.getChildren().addAll(coinLabel);

    	Scene coinScene = new Scene(coinBox, 300, 500);
    	coinStage.setScene(coinScene);

    	// Posizioniamo la seconda finestra a destra della prima
    	coinStage.setX(primaryStage.getX() + 1200 + 10); 			// larghezza principale + spazio
    	coinStage.setY(primaryStage.getY());
    	coinStage.show();

    	Scene scene = new Scene(grid, 1100, 470);
    	primaryStage.setScene(scene);
    	primaryStage.show();

	}
	
	
	private void checkWin(int number, int numComputer, int numWinner, String colour, String colourComputer, String colourWinner) {
		if (number == numWinner && colour.equals(colourWinner)) {
			showMessage("HAI VINTO!");
            addPoints(getNickname());

	    } else if (numComputer == numWinner && colourComputer.equals(colourWinner)) {
			showMessage("HAI PERSO!");
            addPoints("_COMPUTER_");

	    } else {
			showMessage("PAREGGIO!");
	    }
		
		if(colour.equals(colourWinner)) {
			addPointsExtra(getNickname());
		} 
		
		if(colourComputer.equals(colourWinner)) {
			addPointsExtra("_COMPUTER_");
		}
	}
	
	
	private void addPointsExtra(String nickname) {
    	if(nickname.isEmpty()) {												// Gioco come anonimo
	    	String sql  = "UPDATE utente SET score = score + 100 WHERE nickname = ?";
	    	try (Connection conn = DataBaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

	            	if(nickname.isEmpty())
	            		stmt.setString(1,"_ANONIMO_");
                    	stmt.executeUpdate(); 									// Prova a fare l'update
                   
              } catch (SQLException e1) {
            	  e1.printStackTrace();
              }
    	}
    	else {																	// Il NickName è valido
    		String sql  = "UPDATE utente SET score = score + 100 WHERE nickname = ?";
	    	try (Connection conn = DataBaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

	            	stmt.setString(1,nickname);
                    stmt.executeUpdate(); 										// Prova a fare l'update
                   
              } catch (SQLException e1) {
            	  e1.printStackTrace();
              }
    	}
		
	}
		
}
