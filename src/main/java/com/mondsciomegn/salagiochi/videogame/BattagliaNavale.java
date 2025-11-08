package com.mondsciomegn.salagiochi.videogame;



import java.io.StreamCorruptedException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Random;

import com.mondsciomegn.salagiochi.db.Category;
import com.mondsciomegn.salagiochi.db.DataBaseConnection;
import com.mondsciomegn.salagiochi.db.VideoGames;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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


public class BattagliaNavale extends VideoGames {
	
	public BattagliaNavale(String name, Category category) {
		super(name, category);
	}
	public BattagliaNavale(String name, Category category,int score) {
		super(name, category);
		setScore(score);
	}
	
	
	private final int N = 8;
	private Button[][] playerGrid = new Button[N][N];		// Griglia del Giocatore
	private Character[][] tmp = new Character[N][N];
    private Button[][] computerGrid = new Button[N][N];		// Matrice di supporto
    

    private GridPane player = new GridPane();				// Servono per la visualizzazione grafica
    private GridPane computer = new GridPane();
    
    
    private boolean gameOver = false;				
  
    
    private Integer[] currentShip = new Integer[2];	
    // currentShip[0] tiene la grandezza della Barca selezionata, currentShip[1] tiene quanti pezzi mancano per completarne l'inserimento
    private int [] totalPlayerShips = new int[4];					// 4 sono i tipi di barche, da grandezza 1, 2, 3, 4
    private int [] totalComputerShips = new int [4];
    
    private int firstRow = -N;
    private int firstCol = -N;
    private int lastRow = -N;
    private int lastCol = -N;


    
    
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
               tmp[i][j] = 'k';
                
               computerGrid[i][j] = new Button("");
               computerGrid[i][j].setMinSize(MAXH/DIV, MAXW/DIV); 
               computerGrid[i][j].setStyle("-fx-font-size: 20px;");
               computer.add(computerGrid[i][j], j, i);
            }
        }
        
        
        
        
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
	
	
	
	
	private void putAllBoatdown() {
		boatStage.setTitle("Navi della flotta");
		
		VBox boatBox = new VBox(10);
		boatBox.setAlignment(Pos.CENTER);
		boatBox.setPadding(new Insets(10));
		
		
		Button[] boats = new Button[4];
		Label[] boatsLabel = new Label[4];
		
		for(int i=0; i <4; i++) {
			HBox row = new HBox(10);
			row.setAlignment(Pos.CENTER);
			row.setPrefWidth(220);
			
			boats[i] = new Button("Nave da " + (i+2));
			boats[i].setMaxWidth(Double.MAX_VALUE);
			boats[i].setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
			HBox.setHgrow(boats[i], Priority.ALWAYS);
			
			boatsLabel[i] = new Label("x" + totalPlayerShips[i]);
			if(totalPlayerShips[i] == 0)
				boatsLabel[i].setText("");
			boatsLabel[i].setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
			boatsLabel[i].setMaxWidth(25);
			
			row.getChildren().addAll(boats[i],boatsLabel[i]);
			boatBox.getChildren().add(row);
		}
		
		
		for(int i = 0; i < 4; i++) {
			final int boatLength = i + 2;
			
			boats[i].setOnAction(e -> {
				currentShip[0] = boatLength;	// Qui metto la grandezza della barca
				currentShip[1] = boatLength;	// E qui quanto mi manca per metterla completamente sulla griglia
				boatStage.setOnHidden(event -> primaryStage.getScene().getRoot().setDisable(false));
				totalPlayerShips[currentShip[0]-2]--;						// La tolgo dall'elenco totale
				if(totalPlayerShips[currentShip[0]-2] == 0)					// Se il contatore delle barche di quella grandezza è a zero
					if(!boats[boatLength-2].isDisable())
						boats[boatLength-2].setDisable(true);			// disabilito il bottone se non era già disabilitato
				
				boatStage.close();
				colorCells();
			});
		}
		
		
		for (int i = 0; i < N; i++) {		// per le 3 x 3 caselle della griglia
            for (int j = 0; j < N; j++) {
            	
            	final int row = i;
                final int col = j;

                playerGrid[i][j].setOnAction(e -> {  
	                    	playerBoatDown(row, col);
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
	
	
	private void colorCells() {
		for (int i = 0; i < N; i++) {		// per le 3 x 3 caselle della griglia
            for (int j = 0; j < N; j++) {
            	if(!playerGrid[i][j].getText().isEmpty()) {
            		playerGrid[i][j].setStyle("-fx-background-color: rgba(255,0,0,0.3); -fx-font-size: 20px;");
            		
            	}
            }
        }
		
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
	
	
	private boolean playerBoatDown(int row, int col) {
		if(currentShip[0] != 0) {											// Se ho una barca da inserire
			if(!canPlaceShip(row,col)) {									// Controllo che io abbia spazio per metterla
				return false;
			}
			
			if (currentShip[0] == currentShip[1]) {  						// Primo pezzo della barca
				resetBackGround();
				firstRow = lastRow = row;												// Mi salvo la posizione di partenza della barca
	            firstCol = lastCol = col;
	           											// E l'ultima casella premuta
	            
	            playerGrid[row][col].setText(currentShip[0].toString());	// Inserisco la mossa
	            tmp[row][col] = 'x';
	            currentShip[1]--;											// E segno che l'ho messa giu			
	            if (currentShip[0] > 1) {									// Se la nave è più lunga di 1, evidenzio dove posso continuare l'inserimento
	                highlightValidDirections(row, col);
	            }
			}
			else{														// Non è il primo pezzo													
				if(isNearToCell(row, col, firstRow, firstCol)) {	// Controllo che io stia inserendo la barca vicino alla posizione di partenza
					if(playerGrid[row][col].getText().isEmpty() && tmp[row][col] == 'k') {// E che sia una casella valida
						playerGrid[row][col].setText(currentShip[0].toString());	// Inserisco la mossa
						currentShip[1]--;			// E segno che l'ho messa giu
						firstRow = row;				// E aggiorno la posizione dell'ultimo inserimento
						firstCol = col;
						//Ricolorare in base a quanto manca
					}else {								
						return false;	
					}
				}
				else
					return false;	
			}
			
			if(currentShip[1] == 0) {									// Poi se ho messo giù tutta la barca 
				firstRow = -N;											// Resetto le coordinate della prima casella della barca
		        firstCol = -N;
		        lastCol = -N;											// Resetto le coordinate della prima casella della barca
		        lastRow = -N;
		        resetBackGround();
		        
		        if(!checkForAllBoatsDown()) {							// Controllo se non ho messo giù tutte le barche
					primaryStage.getScene().getRoot().setDisable(true);	// Tolgo di nuovo la possibilità di premere sulla griglia
					
					boatStage.show();									// Riapro il pannello delle barche 
				}
				else {
					currentShip[0] = 0;									// Segno che non ci sono più barche da posizionare
					
				}
				return true;
			}			
		}	// CurrentShip[0] vale zero e quindi devo tornare false perchè il click non porta a nulla
					
			
		return false;
	}
	
	

	
	private boolean isNearToCell(int row, int col, int cellRow, int cellCol) {
	    // Calcola la differenza assoluta tra le righe
	    int deltaRow = Math.abs(row - cellRow);
	    // Calcola la differenza assoluta tra le colonne
	    int deltaCol = Math.abs(col - cellCol);
	    
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
		
	    int shipLen = currentShip[0];
	    
	    // 1. Il punto iniziale non deve essere occupato
	    if (!playerGrid[row][col].getText().isEmpty()) 
	        return false;
	    
	    // 2. Barca di lunghezza 1 è sempre posizionabile (se non occupata)
	    if (shipLen == 1) 
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
	    resetBackGround(); // pulisci eventuali colori precedenti

	    int shipLen = currentShip[0];
	    boolean canUp = true, canDown = true, canLeft = true, canRight = true;

	    // SU
	    for (int i = 1; i < shipLen; i++) {
	        int r = row - i;
	        if (r < 0 || !playerGrid[r][col].getText().isEmpty()) {
	            canUp = false;
	            break;
	        }
	    }

	    // GIÙ
	    for (int i = 1; i < shipLen; i++) {
	        int r = row + i;
	        if (r >= N || !playerGrid[r][col].getText().isEmpty()) {
	            canDown = false;
	            break;
	        }
	    }

	    // SINISTRA
	    for (int i = 1; i < shipLen; i++) {
	        int c = col - i;
	        if (c < 0 || !playerGrid[row][c].getText().isEmpty()) {
	            canLeft = false;
	            break;
	        }
	    }

	    // DESTRA
	    for (int i = 1; i < shipLen; i++) {
	        int c = col + i;
	        if (c >= N || !playerGrid[row][c].getText().isEmpty()) {
	            canRight = false;
	            break;
	        }
	    }

	   
	    colorDirection(row, col, -1, 0, shipLen, canUp);
	    colorDirection(row, col, 1, 0, shipLen, canDown);
	    colorDirection(row, col, 0, -1, shipLen, canLeft);
	    colorDirection(row, col, 0, 1, shipLen, canRight);
	}

	private void colorDirection(int row, int col, int dr, int dc, int len, boolean valid) {
		colorCells();
	    String color = valid ? "rgba(0,255,0,0.3)" : "rgba(255,0,0,0.3)";
	    Character prova = valid ? 'k' : 'x'; 
	    
	    for (int i = 1; i < len; i++) {
	        int r = row + dr * i;
	        int c = col + dc * i;
	        if (r < 0 || r >= N || c < 0 || c >= N) 
	        	break;

	        playerGrid[r][c].setStyle(
	            String.format("-fx-background-color: %s; -fx-font-size: 20px;", color)
	        );
	        tmp[r][c] = prova; 
	    }
	    playerGrid[row][col].setStyle("-fx-background-color: rgba(0,255,0,0.3); -fx-font-size: 20px;");
	}

	
	
	private boolean checkForAllBoatsDown() {
		for(int i = 0; i < 4; i++)
			if(totalPlayerShips[i] != 0)
				return false;
			
		return true;
	}
	
	
	private void resetBackGround() {
		for(int i=0; i < N; i++) 
			for(int j = 0; j < N; j++) 
				if(playerGrid[i][j].getText().isEmpty()) {
					playerGrid[i][j].setStyle("-fx-font-size: 20px;");
					tmp[i][j] = 'k';
				}
				else {
					playerGrid[i][j].setStyle("-fx-background-color: rgba(0, 255, 0, 0.5); -fx-font-size: 20px;");	
					tmp[i][j] = 'x';
				}
	}
		
	
	private void computerMove() {
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				
			}
		}
		
	}
		
}

