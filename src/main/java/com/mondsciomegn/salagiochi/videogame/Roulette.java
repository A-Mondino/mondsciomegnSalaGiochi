package com.mondsciomegn.salagiochi.videogame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Roulette extends VideoGames{

	private Random random = new Random();						// Per estrazione vincente
    private Stage boatStage = new Stage();						// Finestra per gettoni

    private Button selectedButton = null;
    private GridPane grid = new GridPane();						// Per la visualizzazione grafica
    private Stage primaryStage = new Stage();
    
    private int gameTokens = 0;   								// Gettoni che il giocatore converte
    private boolean puntataAbilitata = false;
    private boolean selectionLocked = false;
    private String scelta;
    private int numero;
    
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
    	
    	aggiornaPunteggioDaDB();
    	
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
    	        
    	        // Solo se le puntate sono abilitate
    	        btn.setOnAction(e -> {
    	        	scelta=selectButton(btn);
    	        	if (scelta != null) {
    	                numero = estraiNumeroVincente();
    	                verifica(numero, scelta);
    	            }    	        });

    	        grid.add(btn, col, row + 1);
    	    }
    	}

    	    String[] choiceRow = {
    	    	    "Punta sull'intera riga 1",
    	    	    "Punta sull'intera riga 2",
    	    	    "Punta sull'intera riga 3",
    	    	};

    	    	for (int i = 0; i < 3; i++) {
    	    	    Button empty = new Button(choiceRow[i]);
    	    	    empty.setMinSize(70, 70);
    	    	    empty.setStyle("-fx-background-color: white; -fx-border-color: white;");
    	    	    
    	    	    empty.setOnAction(e -> {
    	    	        scelta = selectButton(empty);
    	    	        if (scelta != null) {
    	    	            numero = estraiNumeroVincente();
    	    	            verifica(numero, scelta);
    	    	        }
    	    	    });

    	    	    grid.add(empty, 12, i + 1);
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
    	    
    	    extra.setOnAction(e -> {
    	    	scelta=selectButton(extra);
    	    	if (scelta != null) {
    	            numero = estraiNumeroVincente();
    	            verifica(numero, scelta);
    	        }
	        });
    	    
    	    grid.add(extra, i * 4, 4, 4, 1);
    	}
    	
    	// Array contenenti caratteristiche dei 6 bottoni
    	String[] choiceExtra = {
    		    "Punta 1-18",
    		    "Pari",
    		    "Rosso",
    		    "Nero",
    		    "Dispari",
    		    "Punta 19-36"
    		};

    	String [] colorsExtra = {
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
    		    
    		    b.setOnAction(e -> {
    		    	scelta=selectButton(b);
    		    	if (scelta != null) {
    		            numero = estraiNumeroVincente();
    		            verifica(numero, scelta);
    		        }
    	        });
    		    
    		    grid.add(b, i*2, 5, 2, 1); 
    		}
        
	    Button restart = new Button("Ripunta");
	    restart.setOnAction(e -> {
	    	selectionLocked = false;        
	        if (selectedButton != null) {
	            // Rimuove il bordo giallo dal bottone selezionato
	            selectedButton.setStyle(selectedButton.getStyle().replace("-fx-border-color: yellow; -fx-border-width: 3px;", ""));
	            selectedButton = null;
	        }
	    	Converter();

	        });
    	
    	Scene scene = new Scene(grid, 1100, 470);
       	primaryStage.setScene(scene);
       	primaryStage.show();
       	
        grid.add(messageLabel, 0, 0, 12, 1);
        	
    	Converter();

	    VBox resultBox = new VBox(10, restart);
	    resultBox.setPadding(new Insets(10));

	    // Aggiungi il VBox come ultima riga della griglia
	    grid.add(resultBox, 0, 6, 12, 1);	

	}
	
	private void Converter() {

	    Stage tokenStage = new Stage();
	    tokenStage.setTitle("Converti punti in gettoni");

	    VBox root = new VBox(15);
	    root.setPadding(new Insets(15));
	    root.setAlignment(Pos.CENTER);
	    
	    aggiornaPunteggioDaDB();
	    Label puntiLabel = new Label("Punti disponibili: " +getScore());
	    puntiLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

	    Label gettoniLabel = new Label("Gettoni convertiti: 0");
	    gettoniLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

	    Button convertiBtn = new Button("Converti 300 punti = 1 gettone");
	    convertiBtn.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    convertiBtn.setMaxWidth(Double.MAX_VALUE);

	    Button puntaBtn = new Button("Punta i gettoni");
	    puntaBtn.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    puntaBtn.setDisable(true);

	    final int[] gettoniConvertiti = {0};

	    convertiBtn.setOnAction(e -> {

	        if (getScore() >= 300) {												// Controllo che ci siano punti a sufficienza

	        	subPoints(getNickname());  											// Aggiorna punteggio
	            gettoniConvertiti[0]++;

	            puntiLabel.setText("Punti disponibili: " + getScore());
	            gettoniLabel.setText("Gettoni convertiti: " + gettoniConvertiti[0]);

	            puntaBtn.setDisable(false);
	        }else {
	        	Alert alert = new Alert(Alert.AlertType.INFORMATION);
	            alert.setTitle("Punti insufficienti");
	            alert.setHeaderText(null);
	            alert.setContentText("Non hai abbastanza punti per giocare!");
	            alert.showAndWait();

	            // Chiudi la finestra del gioco
	            primaryStage.close();
	            return;  // Esce dal metodo
	        }
	    });

	    puntaBtn.setOnAction(e -> {
	        gameTokens += gettoniConvertiti[0];   									// Gettoni usati nel gioco
	        puntataAbilitata = true; 												// Abilita le puntate
	        tokenStage.close();
	        primaryStage.getScene().getRoot().setDisable(false);
	    });

	    root.getChildren().addAll(puntiLabel, gettoniLabel, convertiBtn, puntaBtn);

	    Scene scene = new Scene(root, 300, 230);
	    tokenStage.setScene(scene);

	    tokenStage.setOnCloseRequest(e -> e.consume());

	    primaryStage.getScene().getRoot().setDisable(true);
	    tokenStage.initModality(Modality.APPLICATION_MODAL);
	    tokenStage.showAndWait();
	}
	
	
	private String selectButton(Button cell) {
	    if (gameTokens <= 0) {
	        showMessage("Non hai gettoni disponibili! Converti altri punti.");
	        return null;
	    }
	    
	    if (selectionLocked) {
	        // Se già selezionato un pulsante, blocca ulteriori selezioni
	        return null;
	    }

	    selectedButton = cell;
	    cell.setStyle(cell.getStyle() + "-fx-border-color: yellow; -fx-border-width: 3px;");
	    
	    // Blocca tutte le selezioni future
	    selectionLocked = true;
	    
	    // Riduci i gettoni
	    gameTokens--;
	    
	    String selectedNumber = cell.getText().trim();
	    return selectedNumber;
	}
	
	private int estraiNumeroVincente() {
	    return random.nextInt(36) + 1;
	}

	private void verifica(int num, String decisione) {
	    try {
	        int sceltaNum = Integer.parseInt(decisione);
	        if(num == sceltaNum) {
	            showMessage("Numero estratto: " + num + "\nHAI VINTO!");
	            addPoints(getNickname());
	            return;
	        }else {
	            showMessage("Numero estratto: " + num + "\nHAI PERSO!");
	        }
	    } catch (NumberFormatException ignored) {}
			
		switch(decisione) {
		
		case "Punta sull'intera riga 1":
		{
            showMessage("Hai selezionato: Punta sull'intera riga 1" );
            if(num == 1 || num == 4 || num == 7 || num == 10 || num == 13 || num == 16 || num == 19 || num == 22 || num == 25 || num == 28 || num == 31 || num == 34) {
				showMessage("Numero estratto: " + num + "\nHAI VINTO!");
    	        addPoints(getNickname());
            }else
	            showMessage("Numero estratto: " + num + "\nHAI PERSO!");

			break;
		}
		
		case "Punta sull'intera riga 2":
		{
            showMessage("Hai selezionato: Punta sull'intera riga 2" );
            if(num == 2 || num == 5 || num == 8 || num == 11 || num == 14 || num == 17 || num == 20 || num == 23 || num == 26 || num == 29 || num == 32 || num == 35) {
				showMessage("Numero estratto: " + num + "\nHAI VINTO!");
    	        addPoints(getNickname());
            }else
	            showMessage("Numero estratto: " + num + "\nHAI PERSO!");
            
			break;
		}
		
		case "Punta sull'intera riga 3":
		{
            showMessage("Hai selezionato: Punta sull'intera riga 3" );
            if(num == 3 || num == 6 || num == 9 || num == 12 || num == 15 || num == 18 || num == 21 || num == 24 || num == 27 || num == 30 || num == 33 || num == 36) {
				showMessage("Numero estratto: " + num + "\nHAI VINTO!");
    	        addPoints(getNickname());
            }else
	            showMessage("Numero estratto: " + num + "\nHAI PERSO!");

			break;
		}
	 	    
		case "Punta sui primi 12 numeri":
		{
            showMessage("Hai selezionato: Punta sui primi 12 numeri" );
            if(num > 0 && num < 13) {
				showMessage("Numero estratto: " + num + "\nHAI VINTO!");
    	        addPoints(getNickname());
            }else
	            showMessage("Numero estratto: " + num + "\nHAI PERSO!");

			break;
		}
		
		case "Punta sui numeri da 13 a 24":
		{
            showMessage("Hai selezionato: Punta sui numeri da 13 a 24" );
            if(num > 12 && num < 25) {
				showMessage("Numero estratto: " + num + "\nHAI VINTO!");
    	        addPoints(getNickname());
            }else
	            showMessage("Numero estratto: " + num + "\nHAI PERSO!");

			break;
		}
		
		case "Punta sui numeri da 25 a 36":
		{
            showMessage("Hai selezionato: Punta sui numeri da 25 a 36" );
            if(num > 24 && num < 37) {
				showMessage("Numero estratto: " + num + "\nHAI VINTO!");
    	        addPoints(getNickname());
            }else
	            showMessage("Numero estratto: " + num + "\nHAI PERSO!");

			break;
		}
		
		case "Punta 1-18":
		{
            showMessage("Hai selezionato: Punta 1-18" );
            if(num > 0 && num < 19) {
				showMessage("Numero estratto: " + num + "\nHAI VINTO!");
    	        addPoints(getNickname());
            }else
	            showMessage("Numero estratto: " + num + "\nHAI PERSO!");

			break;
		}
		
		case "Pari":
		{
            showMessage("Hai selezionato: Pari" );
            if(num % 2 == 0) {
				showMessage("Numero estratto: " + num + "\nHAI VINTO!");
    	        addPoints(getNickname());
            }else
	            showMessage("Numero estratto: " + num + "\nHAI PERSO!");

			break;
		}
		
		case "Dispari":
		{
            showMessage("Hai selezionato: dispari" );
            if(num % 2 != 0) {
				showMessage("Numero estratto: " + num + "\nHAI VINTO!");
    	        addPoints(getNickname());
            }else
	            showMessage("Numero estratto: " + num + "\nHAI PERSO!");

			break;
		}
		
		case "Punta 19-36":
		{
            showMessage("Hai selezionato: Punta 19-36" );
            if(num > 18 && num < 37) {
				showMessage("Numero estratto: " + num + "\nHAI VINTO!");
    	        addPoints(getNickname());
            }else
	            showMessage("Numero estratto: " + num + "\nHAI PERSO!");

			break;
		}
		
		case "Rosso":
		{
	        showMessage("Hai selezionato i numeri di colore: Rosso" );
	        if(num == 1 || num == 3 || num == 5 || num == 7 || num == 9 || num == 12 || num == 14 || num == 16 || num == 18 || num == 19 || num == 21 || num == 23 || num == 25 || num == 27 || num == 30 || num == 32 || num == 34 || num == 36) {
	        	showMessage("Numero estratto: " + num + "\nHAI VINTO!");
	    	    addPoints(getNickname());
	            }else
		            showMessage("Numero estratto: " + num + "\nHAI PERSO!");

			break;
		}

		case "Nero":
		{
	        showMessage("Hai selezionato i numeri di colore: Nero" );
	        if(num == 2 || num == 4 || num == 6 || num == 8 || num == 10 || num == 11 || num == 13 || num == 15 || num == 17 || num == 20 || num == 22 || num == 24 || num == 26 || num == 28 || num == 29 || num == 31 || num == 33 || num == 35) {
					showMessage("Numero estratto: " + num + "\nHAI VINTO!");
	    	        addPoints(getNickname());
	            }else
		            showMessage("Numero estratto: " + num + "\nHAI PERSO!");

			break;

		}

		}

	}
		
	private void aggiornaPunteggioDaDB() {
	    String nicknameDB = getNickname().isEmpty() ? "_ANONIMO_" : getNickname();
	    String sql = "SELECT score FROM utente WHERE nickname = ?";

	    try (Connection conn = DataBaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, nicknameDB);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            setScore(rs.getInt("score")); // aggiorna il punteggio in memoria
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	
	private void subPoints(String nickname) {
	    String nicknameDB = nickname.isEmpty() ? "_ANONIMO_" : nickname;
	    String sql = "UPDATE utente SET score = score - 300 WHERE nickname = ?";

	    try (Connection conn = DataBaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, nicknameDB);
	        stmt.executeUpdate();

	        // Aggiorna anche in memoria
	        setScore(getScore() - 300);

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

		
}