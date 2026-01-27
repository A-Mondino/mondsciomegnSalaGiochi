package com.mondsciomegn.salagiochi.videogame;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;



import com.mondsciomegn.salagiochi.db.Category;

import com.mondsciomegn.salagiochi.db.VideoGames;


import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.ButtonBar;


public class Tris extends VideoGames{
		
		/**
		 * Costruttore della classe Tris,
		 * Inizializza il punteggio a 300.
		 * 
		 * @param name nome del gioco 
		 * @param category categoria a cui è associato il gioco 
		 */
		public Tris(String name, Category category) {
			super(name, category);
			setScore(300);
		}
		
		

	 	private Button[][] buttons = new Button[3][3];		// Griglia del tris
	    private char[][] playGrid = new char[3][3];			// Matrice di supporto
	    
	    private GridPane grid = new GridPane();				// Per la visualizzazione grafica
	    
	    private boolean gameOver = false;
	    private Random random = new Random();
	    
	    private Stage primaryStage = new Stage();
	    
	    
	    /**
	     * Avvia la procedura di inizio del gioco facendo visualizzare le istruzioni.
	     * Se il giocatore decide di giocare avvia la partita altrimenti ritorna alla finestra VideoGames
	     *	 
	     * @param nickname nickname del giocatore.
	     */
	    public void play(String nickName) {
	    	setNickname(nickName);
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

	    	startGame();

	    }

	    /**
	     * Avvio del gioco, gestisce sia l'interfaccia grafica che la logica effettiva del gioco.
	     * Il metodo inizializza una griglia 3x3 composta da pulsanti che rappresentano la griglia di gioco.
	     */
	    private void startGame() {
	    	
	        primaryStage.setTitle("Gioco Tris");

	        for (int i = 0; i < 3; i++) {								// Inizializzo la matrice di bottoni 
	            for (int j = 0; j < 3; j++) {
	                playGrid[i][j] = ' ';		
	                buttons[i][j] = new Button("");
	                buttons[i][j].setMinSize(100, 100);
	                buttons[i][j].setStyle("-fx-font-size: 36px;");
	                grid.add( buttons[i][j], j, i);
	            }
	        }
	        
	        for (int i = 0; i < 3; i++) {								// Griglia con caselle 3x3
	            for (int j = 0; j < 3; j++) {
	            	
	            	final int row = i;
	                final int col = j;

	            	 buttons[i][j].setOnAction(e -> {
		                    if (!gameOver && buttons[row][col].getText().isEmpty()) {
		                    	if(!playerMove(row, col))				// Questo if e il ritorno della funzione... 
		                    		computerMove();						// Mossa del computer
		                    }
		             });
	            }
	        }
	        
	        
	        Scene scene = new Scene(grid, 300, 300);
	        primaryStage.setScene(scene);
	        primaryStage.initModality(Modality.APPLICATION_MODAL);
	        startTimer(primaryStage);
	        primaryStage.show();
	        
	        
	        primaryStage.setOnCloseRequest(event -> {
	            stopTimer();
	        });
	    }

	    /**
	     * Giocata del giocatore (turno del giocatore)
	     * 
	     * @param row numero della riga dove posizionare la giocata
	     * @param col numero della colonna dove posizionare la giocata
	     * @return  true se la giocata è andata a buon fine e la partita termina con una vittoria
	     * 			false in caso di errore 
	     */
	    private Boolean playerMove(int row, int col) {	
	    	//...serve solo per evitare un errore che si verificava perchè la mossa del computer veniva fatta lo stesso dopo il primaryStage.hide();
	        playGrid[row][col] = 'X';						// Setto la matrice di supporto con il segno del giocatore
	        buttons[row][col].setText("X");					// Setto anche il bottone

	        if (gameCheck('X')) {							// Controllo se ho vinto
	            gameOver = true;
	            showMessage("Hai vinto!");
	            primaryStage.close();
	            registerGame(getNickname(), getScore());
	            stopTimer();
	            addPoints(getNickname());					// E assegno i punti
	            return true;
	        }
	        
	        return false;
	    }


	    /**
	     * Giocata del computer (turno del computer) 
	     */
		private void computerMove() {	
	        int[] move = move();

	        if (move == null) {						// Se il mio array di mosse è vuoto significa che il computer non puo più fare nulla ed è un pareggio
	            gameOver = true;
	            showMessage("Pareggio!");
	            primaryStage.close();
	            stopTimer();
	            registerGame(getNickname(), 0);
	        } else {								// Altrimenti ho trovato una mossa da fare 
		        int row = move[0];
		        int col = move[1];
		        playGrid[row][col] = 'O';
		        buttons[row][col].setText("O");
	        }
	        
	        if (gameCheck('O')) {					// Poi controllo se il computer ha vinto
	            gameOver = true;
	            showMessage("Hai perso!");
	            primaryStage.close();
	            stopTimer();
	            addPoints("_COMPUTER_");
	            registerGame(getNickname(), 0);
	        }
	        
	    }

		/**
		 * Seleziona una mossa valida per il computer scegliendo tra le celle ancora libere nella 
		 * griglia di gioco. Le celle ancora disponibili sono salvate in una lista. 
		 * @return  null se non sono più disponibili celle di gioco oppure
		 * 			restituisce una posizione libera scelta dal computer come giocata
		 */
	    private int[] move() {						// AGGIUNGERE MATRICE PRIORITA PER MOSSA NOT DUMB
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

	    /**
	     * Controllo del gioco per stabilire se si è verificata una vittoria 
	     * 
	     * @param symbol simbolo per verificare vittoria (X = simbolo giocatore / O = simbolo computer)
	     * @return  true in caso di vittoria, termina il gioco 
	     * 			false vittoria non verifica, continuo del gioco 
	     */
	    private boolean gameCheck(char symbol) {
	        for (int i = 0; i < 3; i++) 
	            if (playGrid[i][0] == symbol && playGrid[i][1] == symbol && playGrid[i][2] == symbol)
	                return true;
	        
	        for (int j = 0; j < 3; j++) 
	            if (playGrid[0][j] == symbol && playGrid[1][j] == symbol && playGrid[2][j] == symbol)
	                return true;
	        
	        if (playGrid[0][0] == symbol && playGrid[1][1] == symbol && playGrid[2][2] == symbol)
	            return true;
	        
	        if (playGrid[0][2] == symbol && playGrid[1][1] == symbol && playGrid[2][0] == symbol)
	            return true;
	        
	        return false;
	    }	    
	    
	}
	
