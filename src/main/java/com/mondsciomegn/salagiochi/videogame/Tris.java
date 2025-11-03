package com.mondsciomegn.salagiochi.videogame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;



import com.mondsciomegn.salagiochi.db.Category;
import com.mondsciomegn.salagiochi.db.DataBaseConnection;
import com.mondsciomegn.salagiochi.db.User;
import com.mondsciomegn.salagiochi.db.VideoGames;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;



public class Tris extends VideoGames{
		
		public Tris(String name, Category category) {
			super(name, category);
		}
		public Tris(String name, Category category,int score) {
			super(name, category);
			setScore(score);
		}

	 	private Button[][] buttons = new Button[3][3];		// Griglia del tris
	    private char[][] playGrid = new char[3][3];			// Matrice di supporto
	    
	    private GridPane grid = new GridPane();				// Per la visualizzazione grafica
	    
	    private boolean gameOver = false;
	    private Random random = new Random();
	    
	    private Stage primaryStage = new Stage();
	    
	    
	    @Override
	    public void play(String nickName) {
	    	Dialog<ButtonType> dialog = new Dialog<>();
	    	dialog.setTitle("Dettagli Gioco");
	    	dialog.setHeaderText("Istruzioni:");
	    	dialog.setContentText(
	    	        "Inserisci tre simboli uguali in orizzontale, obliquo o verticale " +
	                "prima dell'avversario. Il primo giocatore che riesce a creare una di queste " +
	                "combinazioni vince la partita!\n\n" +
	                "Se invece tutte le caselle si riempiono senza che nessuno abbia allineato " +
	                "i tre simboli, il gioco termina in pareggio."
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
	    	
	    	
	        primaryStage.setTitle("Gioco Tris");


	        for (int i = 0; i < 3; i++) {		// Inizializzo la matrice di bottoni 
	            for (int j = 0; j < 3; j++) {
	                playGrid[i][j] = ' ';		
	                buttons[i][j] = new Button("");
	                buttons[i][j].setMinSize(100, 100);
	                buttons[i][j].setStyle("-fx-font-size: 36px;");
	                grid.add( buttons[i][j], j, i);
	            }
	        }
	        
	        for (int i = 0; i < 3; i++) {		// per le 3 x 3 caselle della griglia
	            for (int j = 0; j < 3; j++) {
	            	
	            	final int row = i;
	                final int col = j;

	            	 buttons[i][j].setOnAction(e -> {
		                    if (!gameOver && buttons[row][col].getText().isEmpty()) {
		                    	if(!playerMove(row, col, nickname))	// questo if e il ritorno della funzione... 
		                    		computerMove();			// Dopo che ho fatto la mossa io la deve fare anche il computer
		                    }
		             });
	            }
	        }

	        Scene scene = new Scene(grid, 300, 300);
	        primaryStage.setScene(scene);
	        primaryStage.show();
	    }

	    private Boolean playerMove(int row, int col, String nickname) {	
	    	//...serve solo per evitare un errore che si verificava perchè la mossa del computer veniva fatta lo stesso dopo il primaryStage.hide();
	        playGrid[row][col] = 'X';			// Setto la matrice di supporto con il segno del giocatore
	        buttons[row][col].setText("X");		// Setto anche il bottone

	        if (gameCheck('X')) {			// Poi controllo se ho vinto
	            gameOver = true;
	            showMessage("Hai vinto!");
	            primaryStage.hide();
	            addPoints(nickname);		// E assegno i punti
	            return true;
	        }
	        
	        return false;
	    }

	    private void addPoints(String nickname) {
	    	if(nickname.isEmpty()) {				// Gioco come anonimo
		    	String sql  = "UPDATE utente SET score = ? WHERE nickname = ?";
		    	try (Connection conn = DataBaseConnection.getConnection();
	                    PreparedStatement stmt = conn.prepareStatement(sql)) {
	
		            	stmt.setInt(1, getScore());     // Assegna il punteggio
		            	if(nickname.isEmpty())
		            		stmt.setString(2,"_ANONIMO_");
	                    stmt.executeUpdate(); 			// Prova a fare l'update
	
	                   
	              } catch (SQLException e1) {
	            	  e1.printStackTrace();
	              }
	    	}
	    	else {									// Ho un nickname valido
	    		String sql  = "UPDATE utente SET score = score + ? WHERE nickname = ?";
		    	try (Connection conn = DataBaseConnection.getConnection();
	                    PreparedStatement stmt = conn.prepareStatement(sql)) {
	
		            	stmt.setInt(1, getScore());     // Assegna il punteggio
		            	stmt.setString(2,nickname);
	                    stmt.executeUpdate(); 			// Prova a fare l'update
	                   
	              } catch (SQLException e1) {
	            	  e1.printStackTrace();
	              }
	    	}
			
		}

		private void computerMove() {	
	        int[] move = move();

	        if (move == null) {		// Se il mio array di mosse è vuoto significa che il computer non puo più fare nulla ed è un pareggio
	            gameOver = true;
	            showMessage("Pareggio!");
	            primaryStage.hide();
	            
	        } else {						// Altrimenti ho trovato una mossa da fare 
		        int row = move[0];
		        int col = move[1];
		        playGrid[row][col] = 'O';
		        buttons[row][col].setText("O");
	        }
	        
	        if (gameCheck('O')) {		// Poi controllo se il computer ha vinto
	            gameOver = true;
	            showMessage("Hai perso!");
	            primaryStage.hide();
	            addPoints("_COMPUTER_");
	        }
	        
	    }

	    private int[] move() {			// AGGIUNGERE MATRICE PRIORITA PER MOSSA NOT DUMB
	        List<int[]> unused = new ArrayList<>();
	        for (int i = 0; i < 3; i++) {
	            for (int j = 0; j < 3; j++) {
	                if (playGrid[i][j] == ' ') {
	                    unused.add(new int[]{i, j});
	                }
	            }
	        }
	        if (unused.isEmpty()) 
	        	return null;

	        return unused.get(random.nextInt(unused.size()));
	    }

	    private boolean gameCheck(char symbol) {
	        for (int i = 0; i < 3; i++) {
	            if (playGrid[i][0] == symbol && playGrid[i][1] == symbol && playGrid[i][2] == symbol)
	                return true;
	        }
	        
	        for (int j = 0; j < 3; j++) {
	            if (playGrid[0][j] == symbol && playGrid[1][j] == symbol && playGrid[2][j] == symbol)
	                return true;
	        }
	        
	        if (playGrid[0][0] == symbol && playGrid[1][1] == symbol && playGrid[2][2] == symbol)
	            return true;
	        
	        if (playGrid[0][2] == symbol && playGrid[1][1] == symbol && playGrid[2][0] == symbol)
	            return true;
	        
	        return false;
	    }

	    // Risulatato partita
	    private void showMessage(String messaggio) {
	        Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle("Risultato");
	        alert.setHeaderText(null);
	        alert.setContentText(messaggio);
	        alert.showAndWait();
	    }
	    
	    // Pop-up istruzioni di gioco 
	    private void showPopUp(String messaggio) {
	        Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle("Dettagli Gioco");
	        alert.setHeaderText("Istruzioni:");
	        alert.setContentText(messaggio);
	        alert.showAndWait();
	    }
	}
	
