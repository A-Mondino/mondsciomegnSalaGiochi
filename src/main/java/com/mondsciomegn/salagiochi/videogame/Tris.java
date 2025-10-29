package com.mondsciomegn.salagiochi.videogame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;

import com.mondsciomegn.salagiochi.db.Category;
import com.mondsciomegn.salagiochi.db.VideoGames;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Tris extends VideoGames{
		
		public Tris(String name, Category category, int score) {
			super(name, category, score);
		}

	 	private Button[][] buttons = new Button[3][3];		// Griglia del tris
	    private char[][] playGrid = new char[3][3];		// Matrice di supporto
	    
	    private boolean gameOver = false;
	    private Random random = new Random();
	    
	    private Stage primaryStage = new Stage();
	    
	    
	    @Override
	    public void play(String nickName) {
	    	Platform.runLater(() -> {
	    		showPopUp("Inserisci tre simboli uguali in orizzontale, obliquo o verticale" +
	            " prima dell'avversario. Il primo giocatore che riesce a creare una di queste" + 
	    		" combinazioni, vince la partita! Se invece tutte le caselle si riempiono senza "+
	            "che nessuno abbia allineato i tre simboli, il gioco termina in pareggio." );
	            
	            startGame(primaryStage, nickName);
	        });
	    }

	    private void startGame(Stage stage, String nickName) {
	    		
	    	if(nickName.isEmpty()) {		// Significa che qualcuno sta giocando come guest
	    		// Devo creare un nuovo utente guest1, guest2 etc etc
	    	}
	    	
	    	
	        stage.setTitle("Gioco Tris");

	        GridPane grid = new GridPane();

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
		                        playerMove(row, col);
		                        computerMove();			// Dopo che ho fatto la mossa io la deve fare anche il computer
		                    }
		                });
	            }
	        }

	        Scene scene = new Scene(grid, 300, 300);
	        stage.setScene(scene);
	        stage.show();
	    }

	    private void playerMove(int row, int col) {
	        playGrid[row][col] = 'X';			// Setto la matrice di supporto con il segno del giocatore
	        buttons[row][col].setText("X");		// Setto anche il bottone

	        if (gameCheck('X')) {			// poi controllo se ho vinto
	            gameOver = true;
	            showMessage("Hai vinto!");
	            primaryStage.hide();
	        }
	    }

	    private void computerMove() {	
	        int[] move = move();

	        if (move == null) {		// Se il mio array di mosse è vuoto significa che il computer non puo più fare nulla ed è un pareggio
	            gameOver = true;
	            showMessage("Pareggio!");
	            primaryStage.hide();
	        }else {						// Altrimenti ho trovato una mossa da fare 
		        int row = move[0];
		        int col = move[1];
		        playGrid[row][col] = 'O';
		        buttons[row][col].setText("O");
	        }
	        
	        if (gameCheck('O')) {		// Poi controllo se il computer ha vinto
	            gameOver = true;
	            showMessage("Hai perso!");
	            primaryStage.hide();
	        }
	        
	    }

	    private int[] move() {			// GGIUNGERE MATRICE PRIORITA PER MOSSA NOT DUMB
	        List<int[]> unused = new ArrayList<>();
	        for (int i = 0; i < 3; i++) {
	            for (int j = 0; j < 3; j++) {
	                if (playGrid[i][j] == ' ') {
	                    unused.add(new int[]{i, j});
	                }
	            }
	        }
	        if (unused.isEmpty()) return null;

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
	
