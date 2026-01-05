package com.mondsciomegn.salagiochi.videogame.battleship;

import java.util.Optional;
import com.mondsciomegn.salagiochi.db.Category;
import com.mondsciomegn.salagiochi.db.VideoGames;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Battleship extends VideoGames {
    
    private static final int N = 9;
    private static final int BOAT_TYPE = 4;
    private int[] totalShips = {1, 2, 1, 1}; // Navi da 2, 3, 4, 5
    private Player player = new Player(N, totalShips, false);
    private Player computer = new Player(N, totalShips, true);
  
    private Ship currentShip = null;
    
    private Stage primaryStage = new Stage();
    private Stage boatStage = new Stage();
    
    // Stato del gioco
    private boolean tutorialFlag = false;
    
    private int firstRow = -1;
    private int firstCol = -1;

    public Battleship(String name, Category category) {
        super(name, category);
        setScore(1000);
    }

    /**
	 * Avvia la procedura di inizio del gioco facendo visualizzare le istruzioni.
	 * Se il giocatore decide di giocare avvia la partita altrimenti ritorna alla finestra VideoGames
	 * 
	 * @param nickname nickname del giocatore.
	 */
	@Override
	public void play(String nickname) {
		setNickname(nickname);
    	Dialog<ButtonType> dialog = new Dialog<>();
    	
    	dialog.setTitle("Dettagli Gioco");
    	dialog.setHeaderText("Istruzioni:");
    	dialog.setContentText(
    	        "Fai esplodere tutte le navi dell'avversario"
    	);
    	
    	ButtonType play = new ButtonType("Gioca", ButtonBar.ButtonData.OK_DONE);
    	ButtonType cancel = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);
    	ButtonType tutorial = new ButtonType("Tutorial", ButtonBar.ButtonData.HELP_2);
    	
    	dialog.getDialogPane().getButtonTypes().setAll(tutorial, play, cancel);

    	Optional<ButtonType> result = dialog.showAndWait();
    	if (!result.isPresent() || result.get() == cancel) {
    	    return;
    	}
    	else 
    		if(result.get() == tutorial)
    			this.tutorialFlag = true;

    	startGame();
    }

	/**
	 * Avvio una nuova partita di battaglia navale, sono presenti le funzioni per la gestione dell'interfaccia grafica.
	 * Inizializzazione e creazione della griglia di gioco, preprazione di tutti gli elementi necessari prima del  
	 * posizionamento delle barca  
	*/
    private void startGame() {
    	 primaryStage.setTitle("Battaglia Navale");
    	 for (int i = 0; i < N; i++) 							// Disattivo i bottoni del computer per impedire all'utente che prema li prima del dovuto
         	for (int j = 0; j < N; j++) 
 		        computer.getField().getGrid().disableButton();
    	 
    	 Label p = new Label("PLAYER");
         p.setAlignment(Pos.CENTER);
         p.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
         Label c = new Label("COMPUTER");
         c.setAlignment(Pos.CENTER);
         c.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
         
         
         VBox playerBox = new VBox(10, p, player.getField().getGrid().getGridGUI());
         playerBox.setAlignment(Pos.CENTER);
         playerBox.setMaxWidth(350);
         playerBox.setMaxHeight(350);
         VBox computerBox = new VBox(10, c, computer.getField().getGrid().getGridGUI());
         computerBox.setAlignment(Pos.CENTER);
         computerBox.setMaxWidth(350);
         computerBox.setMaxHeight(350);
         
         
      
         HBox root = new HBox(20, playerBox, computerBox);
         root.setPadding(new Insets(20));
         root.setAlignment(Pos.CENTER);
         root.setMaxWidth(500);
         root.setMaxHeight(500);
         

         Scene scene = new Scene(root);
         primaryStage.setScene(scene);
         primaryStage.initModality(Modality.APPLICATION_MODAL);
         startTimer(primaryStage);
         primaryStage.show();
         primaryStage.setOnCloseRequest(event -> {
             stopTimer();
         });
         
         
         setNumberOfBoats();					// Inizializzo qui la quantità di barche per ogni tipo		        
         computer.putAllBoatDown();
         putAllBoatdown();
    }
    
    private void putAllBoatdown() {
		boatStage.setTitle("Navi della flotta");
		
		VBox boatBox = new VBox(10);
		Button[] boats = new Button[BOAT_TYPE];			// Quattro bottoni, uno per ogni tipo di nave
		Label[] boatsLabel = new Label[BOAT_TYPE];
		
		
		boatBox.setAlignment(Pos.CENTER);
		boatBox.setPadding(new Insets(10));
		setUpBoatButtons(boatBox, boats, boatsLabel);
		
		for(int i = 0; i < BOAT_TYPE; i++) {		// Rimango in attesa dei click dell'utente
			final int boatLength = i + 2;

			boats[i].setOnAction(e -> {
				
				currentShip = player.getShip(boatLength);
				
				boatStage.setOnHidden(event -> primaryStage.getScene().getRoot().setDisable(false));
				player.getTotalShips()[currentShip.getSize()-2]--;					// La tolgo dall'elenco totale
				boatsLabel[currentShip.getSize()-2].setText("x" + player.getTotalShips()[currentShip.getSize()-2]);		// Aggiorno l'etichetta
				if(player.getTotalShips()[currentShip.getSize()-2] == 0 && !boats[boatLength-2].isDisable())
						boats[boatLength-2].setDisable(true);			// disabilito il bottone se non era già disabilitato
				
				boatStage.close();
				
			});
		}
		
		
		for (int i = 1; i < N; i++) {       		
		    for (int j = 1; j < N; j++) {

		        final int row = i;
		        final int col = j;

		        player.getField().getGrid().getButtonGrid()[i][j].setOnAction(e -> {
		            putPieceDown(row, col);
		        });

		       computer.getField().getGrid().getButtonGrid()[i][j].setOnAction(e1 -> {
		            attack(row, col);
		       });
		    }
		}
		
		
		boatBox.setStyle("-fx-background-radius: 20px; -fx-padding: 20px;");
		Scene s = new Scene(boatBox, 250, 250);
		boatStage.setScene(s);
		boatStage.initStyle(StageStyle.TRANSPARENT);

	    boatStage.setX(primaryStage.getX() - s.getWidth() );	// Questo serve per aprire la finestra a sx di quella principale
	    boatStage.setY(primaryStage.getY());

	    primaryStage.getScene().getRoot().setDisable(true);
	    boatStage.initModality(Modality.APPLICATION_MODAL);
	    boatStage.showAndWait();
	
	}
    
    
    private void attack(int row, int col) {
		if(!player.attack(row, col, tutorialFlag, computer)) {	// Se l'attacco del giocatore contro il pc non va a buon fine
			while(computer.attack(row, col, tutorialFlag, player)) 	// Tocca al PC attaccare, finche fa una mossa giusta
				if(checkWin(player)) {					// Controlla se ha vinto
					showMessage("Il computer ha vinto");
			        primaryStage.close();
			        stopTimer();
					addPoints("_COMPUTER_");
					registerGame(getNickname(), 0);
					return;
				}
		}
		else 
		if(checkWin(computer)) {							// se invece l'attacco va a buon fine controllo se ho vinto
			showMessage("Hai vinto!!!");
	        primaryStage.close();
	        stopTimer();
			addPoints(getNickname());		// Calcolo i punti
			registerGame(getNickname(), getScore());
		}
		
	}
    
    private boolean checkWin(Player opponent) {
		if(opponent.isComputer()) {
			for(Ship s : opponent.getFleet())
				if(!s.isSunk())
					return false;
		}
		else 
			for (int i = 1; i < N; i++)
		        for (int j = 1; j < N; j++)
		            if (!opponent.getField().getGrid().getButtonGrid()[i][j].getText().isEmpty() && opponent.getField().getSupportGrid()[i][j] != 'r')
		                    return false;
		   
		 return true;
	}

	private void setUpBoatButtons(VBox boatBox, Button[] boats, Label[] boatsLabel) {
		    	
    	for(int i=0; i < BOAT_TYPE; i++) {
			HBox row = new HBox(10);
			row.setAlignment(Pos.CENTER);
			row.setPrefWidth(220);
			
			boats[i] = new Button("Nave da " + (i+2));
			boats[i].setMaxWidth(Double.MAX_VALUE);
			boats[i].setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
			HBox.setHgrow(boats[i], Priority.ALWAYS);
			
			boatsLabel[i] = new Label("x" + player.getNumberOfShips(i + 2));
			boatsLabel[i].setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
			boatsLabel[i].setMaxWidth(25);
			
			row.getChildren().addAll(boats[i],boatsLabel[i]);
			boatBox.getChildren().add(row);
		}
	}

	private void putPieceDown(int row, int col) {
        if (currentShip == null) 
        	return;

        // Se è il primo click per questa nave
        if (firstRow == -1) {
            if (player.getField().canPlaceShip(row, col, currentShip.getSize())) {
                firstRow = row;
                firstCol = col;
                
                // Coloriamo la cella selezionata per dare feedback
                player.getField().putPieceDown(row,col,currentShip);
                player.getField().highlightValidDirections(row, col, currentShip.getSize());
            }
        } else { 										// È il secondo click: determiniamo la direzione
            if(player.getField().placeShip(firstRow, firstCol, row, col, currentShip)) {	// Se il piazzamento è andato a buon fine
            	currentShip.setIsPlaced(true);
            	currentShip = null;
            	
            	firstRow = firstCol = -1;
	            player.getField().resetGrid();
	            
	            
	            if(player.checkForAllBoatDown()) {
	            	player.enterBattlePhase();
	            	computer.enterBattlePhase();
	            }
	            else
	            	boatStage.showAndWait();
            } 
        }
    }

    

	private void setNumberOfBoats() {	
		player.setUpShips();
		computer.setUpShips();
	}


 
}