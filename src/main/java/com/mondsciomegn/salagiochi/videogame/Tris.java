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

	private Button[][] buttons = new Button[3][3];
	    private char[][] areaGioco = new char[3][3];
	    private boolean gameOver = false;
	    private Random random = new Random();

	    @Override
	    public void play() {
	    	Platform.runLater(() -> {
	    		mostraPopUp("Inserisci tre simboli uguali in orizzontale, obliquo o verticale" +
	            " prima dell'avversario. Il primo giocatore che riesce a creare una di queste" + 
	    		" combinazioni, vince la partita! Se invece tutte le caselle si riempiono senza "+
	            "che nessuno abbia allineato i tre simboli, il gioco termina in pareggio." );
	            Stage primaryStage = new Stage();
	            startGame(primaryStage);
	        });
	    }

	    private void startGame(Stage stage) {
	        stage.setTitle("Gioco Tris");

	        GridPane grid = new GridPane();

	        for (int i = 0; i < 3; i++) {
	            for (int j = 0; j < 3; j++) {
	                areaGioco[i][j] = ' ';
	                Button button = new Button("");
	                button.setMinSize(100, 100);
	                button.setStyle("-fx-font-size: 36px;");

	                final int riga = i;
	                final int colonna = j;

	                button.setOnAction(e -> {
	                    if (!gameOver && button.getText().isEmpty()) {
	                        playerMove(riga, colonna);
	                    }
	                });

	                buttons[i][j] = button;
	                grid.add(button, j, i);
	            }
	        }

	        Scene scene = new Scene(grid, 300, 300);
	        stage.setScene(scene);
	        stage.show();
	    }

	    private void playerMove(int riga, int colonna) {
	        areaGioco[riga][colonna] = 'X';
	        buttons[riga][colonna].setText("X");

	        if (controlloGioco('X')) {
	            gameOver = true;
	            mostraMessaggio("Hai vinto!");
	            return;
	        }

	        computerMove();

	        if (controlloGioco('O')) {
	            gameOver = true;
	            mostraMessaggio("Hai perso!");
	        }
	    }

	    private void computerMove() {
	        if (gameOver) return;

	        int[] mossa = mossaCasuale();

	        if (mossa == null) {
	            gameOver = true;
	            mostraMessaggio("Pareggio!");
	            return;
	        }

	        int riga = mossa[0];
	        int colonna = mossa[1];
	        areaGioco[riga][colonna] = 'O';
	        buttons[riga][colonna].setText("O");
	    }

	    private int[] mossaCasuale() {
	        List<int[]> libere = new ArrayList<>();
	        for (int i = 0; i < 3; i++) {
	            for (int j = 0; j < 3; j++) {
	                if (areaGioco[i][j] == ' ') {
	                    libere.add(new int[]{i, j});
	                }
	            }
	        }
	        if (libere.isEmpty()) return null;

	        return libere.get(random.nextInt(libere.size()));
	    }

	    private boolean controlloGioco(char simbolo) {
	        for (int i = 0; i < 3; i++) {
	            if (areaGioco[i][0] == simbolo && areaGioco[i][1] == simbolo && areaGioco[i][2] == simbolo)
	                return true;
	        }
	        for (int j = 0; j < 3; j++) {
	            if (areaGioco[0][j] == simbolo && areaGioco[1][j] == simbolo && areaGioco[2][j] == simbolo)
	                return true;
	        }
	        if (areaGioco[0][0] == simbolo && areaGioco[1][1] == simbolo && areaGioco[2][2] == simbolo)
	            return true;
	        if (areaGioco[0][2] == simbolo && areaGioco[1][1] == simbolo && areaGioco[2][0] == simbolo)
	            return true;
	        return false;
	    }

	    // Risulatato partita
	    private void mostraMessaggio(String messaggio) {
	        Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle("Risultato");
	        alert.setHeaderText(null);
	        alert.setContentText(messaggio);
	        alert.showAndWait();
	    }
	    
	    // Pop-up istruzioni di gioco 
	    private void mostraPopUp(String messaggio) {
	        Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle("Dettagli Gioco");
	        alert.setHeaderText("Istruzioni:");
	        alert.setContentText(messaggio);
	        alert.showAndWait();
	    }
	}
	
