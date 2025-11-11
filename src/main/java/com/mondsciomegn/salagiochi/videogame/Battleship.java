package com.mondsciomegn.salagiochi.videogame;



import java.io.StreamCorruptedException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

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
import javafx.stage.Stage;


public class Battleship extends VideoGames {
	
	public Battleship(String name, Category category) {
		super(name, category);
		setScore(1000);
	}
	public Battleship(String name, Category category,int score) {
		super(name, category);
		setScore(score);
	}
	
	
	
	private final int N = 8;
	private final int BOAT_TYPE = 4;
	
	private Button[][] playerGrid = new Button[N][N];		// Griglia del Giocatore
	private Character[][] tmpPlayer = new Character[N][N];
    private Button[][] computerGrid = new Button[N][N];		// Matrice di supporto
    private Character[][] tmpComputer = new Character[N][N];

    private GridPane player = new GridPane();				// Servono per la visualizzazione grafica
    private GridPane computer = new GridPane();
    
    			
  
    
    private Integer[] currentShip = new Integer[2];	
    // currentShip[0] tiene la grandezza della Barca selezionata, currentShip[1] tiene quanti pezzi mancano per completarne l'inserimento
    private int [] totalPlayerShips = new int[BOAT_TYPE];					// 4 sono i tipi di barche, da grandezza 2, 3, 4, 5
    private int [] totalComputerShips = new int [BOAT_TYPE];
    
    private Map<Character, Integer> computerShipSizes = new HashMap<>();		// Le due mappe le uso per tenere traccia di quale nave è stata colpita
    private Map<Character, Integer> computerShipHits = new HashMap<>();
    private final char[] computerShipIDs = {'A', 'B', 'C', 'D', 'E'}; 			// 5 navi max --> A = nave da 2; B = nave da 3; C = nave da 3; D = nave da 4; E = Nave da 5

    
    private int firstRow = -N;
    private int firstCol = -N;
    
    private Stage primaryStage = new Stage();
    private Stage boatStage = new Stage();					// Finestra da cui scegliere le barche 
	
    private final int MAXH = 700;
    private final int MAXW = 700;
    private final int DIV = 13;

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
                        stmt.executeUpdate(); 		// Prova a inserire l'utente

                    } catch (SQLException ex) { 
                    	ex.printStackTrace();
                    }
                   
              } catch (SQLException e1) {
            	  e1.printStackTrace();
              }
        }
    	
    	
        primaryStage.setTitle("Battaglia Navale");

        for (int i = 0; i < N; i++) {		// Inizializzo i due campi da gioco
            for (int j = 0; j < N; j++) {		
               playerGrid[i][j] = new Button("");
               playerGrid[i][j].setMinSize(MAXH/DIV, MAXW/DIV);
               playerGrid[i][j].setStyle("-fx-font-size: 20px;");
               player.add(playerGrid[i][j], j, i);
               tmpPlayer[i][j] = 'k';
                
               computerGrid[i][j] = new Button("");
               computerGrid[i][j].setMinSize(MAXH/DIV, MAXW/DIV); 
               computerGrid[i][j].setStyle("-fx-font-size: 20px;");
               computer.add(computerGrid[i][j], j, i);
               
               tmpPlayer[i][j] = '-';
               tmpComputer[i][j] = '-';
            }
        }
        for (int i = 0; i < N; i++) 		// Disattivo i bottoni del computer per impedire all'utente che prema li prima del dovuto
		    for (int j = 0; j < N; j++) 
		        computerGrid[i][j].setDisable(true);
        
        
        
        Label p = new Label("PLAYER");
        p.setAlignment(Pos.CENTER);
        p.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label c = new Label("COMPUTER");
        c.setAlignment(Pos.CENTER);
        c.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        
        VBox playerBox = new VBox(10, p, player);
        playerBox.setAlignment(Pos.CENTER);
        playerBox.setMaxWidth(MAXW/2);
        playerBox.setMaxHeight(MAXH/2);
        VBox computerBox = new VBox(10, c, computer);
        computerBox.setAlignment(Pos.CENTER);
        computerBox.setMaxWidth(MAXW/2);
        computerBox.setMaxHeight(MAXH/2);
        
        
     
        HBox root = new HBox(20, playerBox, computerBox);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setMaxWidth(500);
        root.setMaxHeight(500);
        

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        setNumberOfBoats();					// Inizializzo qui la quantità di barche per ogni tipo		        
	}
	
	
	private void setNumberOfBoats() {	
		totalPlayerShips[0] = 1;			// Barche da 2 
		totalPlayerShips[1] = 2;			// Da 3
		totalPlayerShips[2] = 1;			// Da 4 
		totalPlayerShips[3] = 1;			// Da 5
		
		totalComputerShips[0] = 1;			// Barche da 2
		totalComputerShips[1] = 2;			// Da 3
		totalComputerShips[2] = 1;			// Da 4 
		totalComputerShips[3] = 1;			// Da 5
		
		putAllBoatdown();
	}
	
	
	
	private void putAllBoatdown() {
		boatStage.setTitle("Navi della flotta");
		
		VBox boatBox = new VBox(10);
		boatBox.setAlignment(Pos.CENTER);
		boatBox.setPadding(new Insets(10));
		
		
		Button[] boats = new Button[BOAT_TYPE];			// Quattro bottoni, uno per ogni tipo di nave
		Label[] boatsLabel = new Label[BOAT_TYPE];
		
		for(int i=0; i <BOAT_TYPE; i++) {
			HBox row = new HBox(10);
			row.setAlignment(Pos.CENTER);
			row.setPrefWidth(220);
			
			boats[i] = new Button("Nave da " + (i+2));
			boats[i].setMaxWidth(Double.MAX_VALUE);
			boats[i].setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
			HBox.setHgrow(boats[i], Priority.ALWAYS);
			
			boatsLabel[i] = new Label("x" + totalPlayerShips[i]);
			boatsLabel[i].setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
			boatsLabel[i].setMaxWidth(25);
			
			row.getChildren().addAll(boats[i],boatsLabel[i]);
			boatBox.getChildren().add(row);
		}
		
		
		for(int i = 0; i < BOAT_TYPE; i++) {		// Rimango in attesa dei click dell'utente
			final int boatLength = i + 2;
			
			boats[i].setOnAction(e -> {
				currentShip[0] = boatLength;	// Qui metto la grandezza della barca
				currentShip[1] = boatLength;	// E qui quanto mi manca per metterla completamente sulla griglia
				boatStage.setOnHidden(event -> primaryStage.getScene().getRoot().setDisable(false));
				totalPlayerShips[currentShip[0]-2]--;						// La tolgo dall'elenco totale
				boatsLabel[currentShip[0]-2].setText("x" + totalPlayerShips[currentShip[0]-2]);		// Aggiorno l'etichetta
				if(totalPlayerShips[currentShip[0]-2] == 0)					// Se il contatore delle barche di quella grandezza è a zero
					if(!boats[boatLength-2].isDisable())
						boats[boatLength-2].setDisable(true);			// disabilito il bottone se non era già disabilitato
				
				boatStage.close();
				colorCells();
			});
		}
		
		
		for (int i = 0; i < N; i++) {		// Resto in ascolto sull N x N caselle della griglia
            for (int j = 0; j < N; j++) {
            	
            	final int row = i;
                final int col = j;

                playerGrid[i][j].setOnAction(e -> {  
	                playerBoatDown(row, col);
	             });
                computerGrid[i][j].setOnAction(e1 -> {  
                	playerAttack(row, col);
                });
            }
        }
		
		
		
		Scene s = new Scene(boatBox, 300, 300);
		boatStage.setScene(s);
		boatStage.setOnCloseRequest(e -> {		// Questo impedisce la chiusura della finestra delle barche
			e.consume();
		});

	    boatStage.setX(primaryStage.getX() - s.getWidth() - 10);	// Questo serve per aprire la finestra a sx di quella principale
	    boatStage.setY(primaryStage.getY());

	    primaryStage.getScene().getRoot().setDisable(true);
	    boatStage.show();
	
	}
	
	
	private boolean playerBoatDown(int row, int col) {
		if(currentShip[0] != 0) {											// Se ho una barca da inserire
			if(!canPlaceShip(row,col)) 									// Controllo che io abbia spazio per metterla
				return false;			
			
			if (currentShip[0] == currentShip[1]) {  						// Primo pezzo della barca
				firstRow = row;												// Mi salvo la posizione di partenza della barca
	            firstCol = col;
	            
	            playerGrid[row][col].setText(currentShip[0].toString());	// Inserisco la mossa
	            resetBackGrid();											// Aggiorno la matrice di controllo
	            currentShip[1]--;											// E segno che l'ho messa giu			
	            if (currentShip[0] > 1) 									// Se la nave è più lunga di 1, evidenzio dove posso continuare l'inserimento
	                highlightValidDirections(row, col);   
			}else{														// Non è il primo pezzo
				if(tmpPlayer[row][col] == 'k')
					if(isNearToCell(row, col, firstRow, firstCol)) {	// Controllo che io stia inserendo la barca vicino alla posizione di partenza
							playerGrid[row][col].setText(currentShip[0].toString());	// Inserisco la mossa
							resetBackGrid();			// Aggionro la matrice
							currentShip[1]--;			// E segno che l'ho messa giu
							completeShip(row,col);		// Completo l'inserimento calcolando la direzione
					}
					else 
						return false;
				else 				
					return false;
				
			}
			
			if(currentShip[1] == 0) {									// Poi quando ho messo giù tutta la barca 
				firstRow = -N;											// Resetto le coordinate della prima casella della barca
		        firstCol = -N;
		        resetBackGround();										// Resetto tutti i colori e caselle settate invalide
		        resetBackGrid();
		        if(!checkForAllBoatsDown()) {							// Controllo se non ho messo giù tutte le barche
					primaryStage.getScene().getRoot().setDisable(true);	// Tolgo di nuovo la possibilità di premere sulla griglia
					boatStage.show();									// Riapro il pannello delle barche 
				}
				else {
					currentShip[0] = 0;									// Segno che non ci sono più barche da posizionare
					setPlayerGrid();									// Sistemo la griglia di supporto
					placeComputerShips();								// Faccio posizionare le barche al pc
					enterBattlePhase();									// Inizia la fase di gioco
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
			        alert.setTitle("FUOCO!!!!");
			        alert.setHeaderText("È ARRIVATO IL MOMENTO DI SPARARE AL NEMICO!!!");
			        alert.setContentText("premi in una qualunque cella del nemico!\nATTENZIONE: Se mancherai una nave nemica, concederai la mossa la computer!");
			        alert.showAndWait();
				}
				return true;
			}			
		}	
					
		return true;
	}
	
	
	private void playerAttack(int row, int col) {
		if(tmpComputer[row][col] != '-') {			// Se colpisco qualcosa nella girglia del computer
			char id = tmpComputer[row][col];		// Memorizzo l'id di cosa ho colpito
	        computerGrid[row][col].setText("X");	// Segno che l'ho compito
			computerShipHits.put(id, computerShipHits.get(id) + 1);	// Aggiungo il colpo nella mappa che mantiene, per ogni ID, il numero di volte in cui ho colpito quel tipo di barca 
									
	        if (computerShipHits.get(id).equals(computerShipSizes.get(id))) 	// Se la nave è affondata (i colpi subiti dalla nave == dimensione)
	            drawnShip(id); 													// La coloro tutta di rosso
	        else  																// Altrimenti l'ho colpita ma non affondata 
	            computerGrid[row][col].setStyle("-fx-background-color: rgba(255,165,0,0.3); -fx-font-size: 20px;"); // Allora coloro la casella di arancio
	        
			
			if(checkPlayerWin()) {				// Poi controllo se ho vinto
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
		        alert.setTitle("RISULTATO");
		        alert.setHeaderText("Hai vinto!!!");
		        alert.setContentText(null);
		        alert.showAndWait();
		        primaryStage.close();
				addPoints(getNickname());		// Calcolo i punti
			}
		}
		else {			// Non ho colpito niente, è il turno del computer a sparare
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle("MANCATO!!!!");
	        alert.setHeaderText("Hai trovato un buco nell'acqua xD");
	        alert.setContentText("ora il computer ti sparerà");
	        alert.showAndWait();
			computerGrid[row][col].setStyle("-fx-background-color: rgba(0,0,255,0.3); -fx-font-size: 20px;");	// Coloro la casella di blue
			exitBattlePhase();
			computerAttack();
		}
			
	}
	
	
	private void drawnShip(char index) {
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(tmpComputer[i][j] != null && tmpComputer[i][j]  == index)
					computerGrid[i][j].setStyle("-fx-background-color: rgba(255,0,0,0.3); -fx-font-size: 20px;");
			}
		}
		
	}
	
	
	private void computerAttack() {
		Random rand = new Random();
		while(true) {
		    int row = rand.nextInt(N);  // numero casuale tra 0 e N-1
		    int col = rand.nextInt(N);  // numero casuale tra 0 e N-1
		    if(tmpPlayer[row][col] != 'r' && (tmpPlayer[row][col] != '-' && tmpPlayer[row][col] != 'm')) {		// Se ha beccato una cella con qualcosa dentro, che non era già stata rivelata o mancata
		    	playerGrid[row][col].setStyle("-fx-background-color: rgba(255,0,0,0.3); -fx-font-size: 20px;");	// Coloro la cella di rossa visto che è un colpo andato a segno
		    	tmpPlayer[row][col] = 'r';  // Significa che ho preso una barca, allora la rivelo	    		
		    	Alert alert = new Alert(Alert.AlertType.INFORMATION);	// Informa l'utente del cambio di turno
		        alert.setTitle("COLPITO!!!!");
		        alert.setHeaderText("Il computer ti ha colpito!!!");
		        alert.setContentText(null);
		        alert.showAndWait();
				
		    }
		    else if(tmpPlayer[row][col] == '-') {		// Altrimenti se ha beccato una casella vuota
				tmpPlayer[row][col] = 'm';				// Segna che ha mancato quella casella li, così da un pescarla più casualmente
		    	playerGrid[row][col].setStyle("-fx-background-color: rgba(0,0,255,0.3); -fx-font-size: 20px;");	// Colora di blue
		    	enterBattlePhase();
				return;
		    }
		    
		    if(checkComputerWin()) {			// Stessa logica del player
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
		        alert.setTitle("RISULTATO");
		        alert.setHeaderText("Hai perso!!!");
		        alert.setContentText("Il computer ha vinto");
		        alert.showAndWait();
		        primaryStage.close();
				addPoints("_COMPUTER_");
				return;
		    }
		}
	}
	
	
	
	private boolean checkComputerWin() {	// Se tutte le barche sono state rivelate, il giocatore ha perso
		for (int i = 0; i < N; i++) 		
            for (int j = 0; j < N; j++) 
            	if(tmpPlayer[i][j] != '-')			// Ogni volta che ho una barca nella griglia di supporto
            		if(!playerGrid[i][j].getText().isEmpty() && tmpPlayer[i][j] != 'r')	// Se anche solo una barca nella playerGrid non è rivelata
            			return false;									// Non ha vinto
		return true;
	}
	
	
	private boolean checkPlayerWin() {		// Se tutte le barche sono state rivelate, il pc ha perso
		for (int i = 0; i < N; i++) 		
            for (int j = 0; j < N; j++) 
            	if(tmpComputer[i][j] != '-')			// Ogni volta che ho una barca nella griglia
            		if(computerGrid[i][j].getText().isEmpty())			// Se anche solo una non è stata rivelata
            			return false;									// Non ho vinto
		return true;
	}
	
	
	
	private void colorCells() {
		for (int i = 0; i < N; i++) 		
            for (int j = 0; j < N; j++) 
            	if(!playerGrid[i][j].getText().isEmpty()) 
            		playerGrid[i][j].setStyle("-fx-background-color: rgba(255,0,0,0.3); -fx-font-size: 20px;");
	}
	
	
	private void setPlayerGrid() {
		for (int i = 0; i < N; i++) 
		    for (int j = 0; j < N; j++) {
		    	if(!playerGrid[i][j].getText().isEmpty())
		    		tmpPlayer[i][j] = playerGrid[i][j].getText().charAt(0);
		    	else
		    		tmpPlayer[i][j] = '-';
		    }
		        
		
	}
	
	
	private void enterBattlePhase() { // Disabilito i pulsanti di playerGrid e riabilito quelli del pc in modo da poter essere premuti
		for (int i = 0; i < N; i++) 
		    for (int j = 0; j < N; j++) 
		        playerGrid[i][j].setDisable(true);		
		for (int i = 0; i < N; i++) 
		    for (int j = 0; j < N; j++) 
		        computerGrid[i][j].setDisable(false);
	}
	
	private void exitBattlePhase() { // Faccio il contrario
		for (int i = 0; i < N; i++) 
		    for (int j = 0; j < N; j++) 
		        playerGrid[i][j].setDisable(false);		
		for (int i = 0; i < N; i++) 
		    for (int j = 0; j < N; j++) 
		        computerGrid[i][j].setDisable(true);
	}
	
	
	
	private void placeComputerShips() {
		Random rand = new Random();
	    int idIndex = 0; 								// Per scorrere gli ID univoci

	    for (int i = 0; i < N; i++)						// Inizializzo la matrice di supporto per evitare valori null
	        for (int j = 0; j < N; j++)
	            tmpComputer[i][j] = '-';

	    for (int shipIndex = 0; shipIndex < BOAT_TYPE; shipIndex++) {		
	        int shipSize = shipIndex + 2; 				// Le navi vanno da 2 a 5
	        int shipsToPlace = totalComputerShips[shipIndex];		// Mi salvo quale nave è da mettere giù

	        for (int s = 0; s < shipsToPlace; s++) {			// Per tutta la lunghezza
	            boolean placed = false;
	            char shipID = computerShipIDs[idIndex++]; 		// Assegna ID univoco

	            
	            computerShipSizes.put(shipID, shipSize);		// Registra la dimensione della nave
	            computerShipHits.put(shipID, 0);				// E i colpi che ha subito finora (ovvero 0)

	            while (!placed) {				
	                int row = rand.nextInt(N);
	                int col = rand.nextInt(N);
	                boolean horizontal = rand.nextBoolean();

	                if (canPlaceComputerShip(row, col, shipSize, horizontal)) {
	                    for (int i = 0; i < shipSize; i++) {
	                        int r = row + (horizontal ? 0 : i);
	                        int c = col + (horizontal ? i : 0);
	                        tmpComputer[r][c] = shipID;			 // Salvo l'ID invece del numero perchè con più navi della stessa lunghezza è un casino se no riconoscerle
	                    }
	                    placed = true;
	                }
	            }
	        }
	    }

	    
	    for (int i = 0; i < N; i++)					// Rimuovi visivamente le navi dal campo del computer
	        for (int j = 0; j < N; j++)
	            computerGrid[i][j].setText("");
	    
	    
	    System.out.println("=== Mappa Computer ===");
	    for (int i = 0; i < N; i++) {
	        for (int j = 0; j < N; j++)
	            System.out.print(tmpComputer[i][j] + " ");
	        System.out.println();
	    }
	    
	}
	


	private boolean canPlaceComputerShip(int row, int col, int size, boolean horizontal) {
	    if (horizontal) {
	        if (col + size > N) // Uscirebbe dai bordi
	        	return false; 
	        
	        for (int i = 0; i < size; i++) 
	            if (tmpComputer[row][col + i] != null && tmpComputer[row][col + i] != '-') 	// C'è già una nave in questa cella
	                return false;     
	    } else {
	        if (row + size > N) // Stessa logica ma per la verticalità
	        	return false; 
	        
	        for (int i = 0; i < size; i++) 
	            if (tmpComputer[row + i][col] != null && tmpComputer[row + i][col] != '-') 
	                return false;    
	    }
	    return true;
	}


	
	private void resetBackGrid() {		// x la matrice di supporto
		for(int i=0; i < N; i++) 
			for(int j = 0; j < N; j++) 
				if(playerGrid[i][j].getText().isBlank()) 
					tmpPlayer[i][j] = 'k';
				else
					tmpPlayer[i][j] = 'x';
	}
	
	
	private boolean isNearToCell(int row, int col, int cellRow, int cellCol) {
	    
	    int deltaRow = Math.abs(row - cellRow);	// Calcola la differenza assoluta tra le righe
	    
	    int deltaCol = Math.abs(col - cellCol);	// Calcola la differenza assoluta tra le colonne
	    
	    // Controlla se le celle sono adiacenti ORTOGONALMENTE
	    // (deltaRow + deltaCol == 1)
	    
	    // Esempio:
	    // (3,3) e (3,4) -> |0| + |1| = 1 (Adiacenti OK)
	    // (3,3) e (4,4) -> |1| + |1| = 2 (Diagonale NO)
	    // (3,3) e (3,3) -> |0| + |0| = 0 (Stessa cella NO)
	    if(deltaRow + deltaCol == 1)
	    	return true;
	    return false;
	}
	
	
	
	private boolean canPlaceShip(int row, int col) {
		
	    int shipLen = currentShip[1];			// Controllo se la lunghezza che manca della barca ci sta
	    
	    if (!playerGrid[row][col].getText().isEmpty()) 	
	    	return false;
	        
	    if (shipLen == 1) 						// Le barche di lunghezza 1 possono essere sempre messe se la cella è libera
	        return true;
	    
	    boolean canPlace = false;

	    // --- Destra (col + i) ---
	    // C'è spazio fino alla fine della griglia (ultima cella: col + shipLen - 1)
	    if (col + shipLen <= N) { 
	        boolean isClear = true;
	        for (int i = 1; i < shipLen; i++) {
	            if (!playerGrid[row][col + i].getText().isEmpty()) {
	                isClear = false;
	                break;
	            }
	        }
	        if (isClear) return true;
	    }
	    
	    // --- Sinistra (col - i) ---
	    // C'è spazio fino all'inizio della griglia (ultima cella: col - shipLen + 1)
	    if (!canPlace && col - shipLen + 1 >= 0) {
	        boolean isClear = true;
	        for (int i = 1; i < shipLen; i++) {
	            if (!playerGrid[row][col - i].getText().isEmpty()) {
	                isClear = false;
	                break;
	            }
	        }
	        if (isClear) return true;
	    }

	    // --- Giù (row + i) ---
	    // C'è spazio fino alla fine della griglia (ultima cella: row + shipLen - 1)
	    if (!canPlace && row + shipLen <= N) {
	        boolean isClear = true;
	        for (int i = 1; i < shipLen; i++) {
	            if (!playerGrid[row + i][col].getText().isEmpty()) {
	                isClear = false;
	                break;
	            }
	        }
	        if (isClear) return true;
	    }
	    
	    // --- Su (row - i) ---
	    // C'è spazio fino all'inizio della griglia (ultima cella: row - shipLen + 1)
	    if (!canPlace && row - shipLen + 1 >= 0) {
	        boolean isClear = true;
	        for (int i = 1; i < shipLen; i++) {
	            if (!playerGrid[row - i][col].getText().isEmpty()) {
	                isClear = false;
	                break;
	            }
	        }
	        if (isClear) return true;
	    }

	    return canPlace;
	}
	
	
	
	private void highlightValidDirections(int row, int col) {
	  
	    int shipLen = currentShip[0];		
	    boolean canUp = true, canDown = true, canLeft = true, canRight = true;

	    // SU
	    for (int i = 1; i < shipLen; i++) {
	        int r = row - i;
	        if (r < 0 || !playerGrid[r][col].getText().isEmpty()) {			// Appena esco dalla matrice oppure trovo un ostacolo
	            canUp = false;												// Mi segno che non la posso mettere in su
	            for (int j = row - 1; j > r; j--) 							// Poi metto a 'x' tutte le posizioni dall'ostacolo fino all'inizio
	                tmpPlayer[j][col] = 'x';
	            break;
	        }
	    }

	    // GIÙ -> stessa logica
	    for (int i = 1; i < shipLen; i++) {
	        int r = row + i;
	        if (r >= N || !playerGrid[r][col].getText().isEmpty()) {
	            canDown = false;
	            for (int j = row + 1; j < r; j++) 
	            	tmpPlayer[j][col] = 'x';
	            break;
	        }
	    }

	    // SINISTRA
	    for (int i = 1; i < shipLen; i++) {
	        int c = col - i;
	        if (c < 0 || !playerGrid[row][c].getText().isEmpty()) {
	            canLeft = false;
	            for (int j = col - 1; j > c; j--) 
	            	tmpPlayer[row][j] = 'x';
	            break;
	        }
	    }

	    // DESTRA
	    for (int i = 1; i < shipLen; i++) {
	        int c = col + i;
	        if (c >= N || !playerGrid[row][c].getText().isEmpty()) {
	            canRight = false;
	            for (int j = col + 1; j < c; j++) 
	            	tmpPlayer[row][j] = 'x';
	            break;
	        }
	    }

	   
	    colorDirection(row, col, -1, 0, shipLen, canUp);		// Infine coloro le direzioni per una visualizzazione grafica
	    colorDirection(row, col, 1, 0, shipLen, canDown);
	    colorDirection(row, col, 0, -1, shipLen, canLeft);
	    colorDirection(row, col, 0, 1, shipLen, canRight);
	}

	
	private void colorDirection(int row, int col, int dr, int dc, int len, boolean valid) {
	    String color = valid ? "rgba(0,255,0,0.3)" : "rgba(255,0,0,0.3)";			// In base al flag di validità coloro di verde altrimenti rosso
	    
	    for (int i = 1; i < len; i++) {
	        int r = row + dr * i;
	        int c = col + dc * i;
	        if (r < 0 || r >= N || c < 0 || c >= N) 			// Appena esco dala griglia mi fermo 
	        	break;

	        playerGrid[r][c].setStyle(
	            String.format("-fx-background-color: %s; -fx-font-size: 20px;", color)
	        );
	    }
	    playerGrid[row][col].setStyle("-fx-background-color: rgba(0,255,0,0.3); -fx-font-size: 20px;");	// questa è per la casella appena selezionata
	}

	private void completeShip(int row, int col) {
		for(int k = 0; k < currentShip[1]; k++) {		// poi completa l'inserimento
			if(row - firstRow == 1)
				playerGrid[row + k + 1][col].setText(currentShip[0].toString());
		    if(row - firstRow == -1)
		    	playerGrid[row - k - 1][col].setText(currentShip[0].toString());
		    if(col - firstCol == 1)
		    	playerGrid[row][col + k + 1].setText(currentShip[0].toString());
		    if(col - firstCol == -1)
		    	playerGrid[row][col - k - 1].setText(currentShip[0].toString());
		    
		}
		resetBackGrid();
		currentShip[1] = 0;
		return;
	}
	
	private boolean checkForAllBoatsDown() {		
		for(int i = 0; i < 4; i++)
			if(totalPlayerShips[i] != 0)			// Se anche solo una non è finita ritorno falso
				return false;
			
		return true;
	}
	
	
	private void resetBackGround() {		// x il colore
		for(int i=0; i < N; i++) 
			for(int j = 0; j < N; j++) 
				if(playerGrid[i][j].getText().isEmpty()) 
					playerGrid[i][j].setStyle("-fx-font-size: 20px;");
				else 
					playerGrid[i][j].setStyle("-fx-background-color: rgba(0, 255, 0, 0.5); -fx-font-size: 20px;");	
	}
		
	
}

