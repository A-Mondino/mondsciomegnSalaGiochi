package com.mondsciomegn.salagiochi.videogame;



import java.awt.Color;
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
    private Button[][] computerGrid = new Button[N][N];		// Matrice di supporto
    

    private GridPane player = new GridPane();				// Servono per la visualizzazione grafica
    private GridPane computer = new GridPane();
    
    
    private boolean gameOver = false;				
  
    
    private Integer[] currentShip = new Integer[2];			
    // currentShip[0] tiene la grandezza della Barca selezionata, currentShip[1] tiene quanti pezzi mancano per completarne l'inserimento
    
    
    private Random random = new Random();
    
    private Stage primaryStage = new Stage();
    private Stage boatStage = new Stage();					// Finestra da cui scegliere le barche 
	
    private final int MAXH = 700;
    private final int MAXW = 700;
    private final int DIV = 13;

	@Override
	public void play(String nickname) {
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

    	startGame(nickname);

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
    	
    	
        primaryStage.setTitle("Battaglia Navale");

        for (int i = 0; i < N; i++) {		// Inizializzo i due campi da gioco
            for (int j = 0; j < N; j++) {		
               playerGrid[i][j] = new Button("");
               playerGrid[i][j].setMinSize(MAXH/DIV, MAXW/DIV);
               playerGrid[i][j].setStyle("-fx-font-size: 20px;");
               player.add(playerGrid[i][j], j, i);
                
               computerGrid[i][j] = new Button("");
               computerGrid[i][j].setMinSize(MAXH/DIV, MAXW/DIV); 
               computerGrid[i][j].setStyle("-fx-font-size: 20px;");
               computer.add(computerGrid[i][j], j, i);
            }
        }
        
       
        
        
        for (int i = 0; i < N; i++) {		// per le 3 x 3 caselle della griglia
            for (int j = 0; j < N; j++) {
            	
            	final int row = i;
                final int col = j;

                playerGrid[i][j].setOnAction(e -> {
	                    if (!gameOver && playerGrid[row][col].getText().isEmpty()) {
	                    	if(!playerMove(row, col, nickname))	// questo if e il ritorno della funzione... 
	                    		computerMove();			// Dopo che ho fatto la mossa io la deve fare anche il computer
	                    }
	             });
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
        putAllBoatdown();
	}
	
	
	
	
	private void putAllBoatdown() {
		
		boatStage.setTitle("Navi della flotta");
		
		VBox boatBox = new VBox(10);
		boatBox.setAlignment(Pos.CENTER);
		boatBox.setPadding(new Insets(10));
		
		
		Button[] boats = new Button[4];
		for(int i=0; i <4; i++) {
			boats[i] = new Button("Nave da " + (i+1));
			boats[i].setMaxWidth(Double.MAX_VALUE);
			boats[i].setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
		}
		
		boatBox.getChildren().addAll(boats);
		for(int i = 0; i < 4; i++) {
			final int boatLength = i + 1;
			boats[i].setOnAction(e -> {
				currentShip[0] = boatLength;	// Qui metto la grandezza della barca
				currentShip[1] = boatLength;	// E qui quanto mi manca per metterla completamente sulla griglia
				boatStage.close();
			});
		}
		
		Scene s = new Scene(boatBox, 300, 300);
		boatStage.setScene(s);
		boatStage.setOnCloseRequest(e -> {		// Questo impedisce la chiusura della finestra delle barche
			e.consume();
		});

	    boatStage.setX(primaryStage.getX() - s.getWidth() - 10);	// Questo serve per aprire la finestra a sx di quella principale
	    boatStage.setY(primaryStage.getY());

	    boatStage.show();
	
	}
	
	private boolean playerMove(int row, int col, String nickname) {
		if(currentShip[0] != 0) {										// Solo per controllare che ci sia una barca da inserire
			playerGrid[row][col].setText(currentShip[0].toString());	// Inserisco la mossa
			currentShip[1]--;											// E segno che l'ho messa giu
			if(currentShip[1] == 0) {		// Quando ho messo giù tutta la barca 
				boatStage.show();			// Riapro il pannello delle barche (DA AGGIUNGERE: togliere un counter dalle barche di quella length, ovvero currentShip[0])	
				resetBackGround();			// Resetto il Background 
				return true;
			}else {
				if(colorNearCells(row,col) == false) {		// Se il colore non va a buon fine fai l'undo delle operazioni sopra
					currentShip[1]++;
					playerGrid[row][col].setText("");
				}
			}			 
		}
				
		return true;		// Ritornerò false quando non dovrà fare la mossa il computer (ovvero se vinco/colpisco)
	}
	
	private void resetBackGround() {
		for(int i=0; i < N; i++) 
			for(int j = 0; j < N; j++) 
				if(playerGrid[i][j].getText().isEmpty()) 
					playerGrid[i][j].setStyle("-fx-font-size: 20px;");
				else
					playerGrid[i][j].setStyle("-fx-background-color: rgba(144, 238, 144, 0.5); -fx-font-size: 20px;");
				
	}
	
	
	private boolean colorNearCells(int row, int col) {	
	
	   int[] tmp =  checkOutOfBorders(row,col);		// Solo controlli che la cella che evidenzierò non sfori dalla matrice
	   if(checkObstacle(row, col) == false)			// Qui invece controllo se la barca la posso effettivamente mettere o se manca spazio
		   return false;							// Se check torna falso non posso mettere giu la barca
	   
	   if(playerGrid[row][col + 1 - tmp[2]].getText().isEmpty())
		   playerGrid[row][col + 1 - tmp[2]].setStyle("-fx-background-color: rgba(0,255,0,0.5); -fx-border-color: green; -fx-border-width: 3; -fx-font-size: 20px; ");
	   else
		   playerGrid[row][col + 1 - tmp[2]].setStyle("-fx-background-color: rgba(255,0,0,0.5); -fx-border-width: 3; -fx-font-size: 20px; ");
	   
	   if(playerGrid[row][col - 1 + tmp[3]].getText().isEmpty())
		   playerGrid[row][col - 1 + tmp[3]].setStyle("-fx-background-color: rgba(0,255,0,0.5); -fx-border-color: green; -fx-border-width: 3; -fx-font-size: 20px; ");
	   else
		   playerGrid[row][col - 1 + tmp[3]].setStyle("-fx-background-color: rgba(255,0,0,0.5); -fx-border-width: 3; -fx-font-size: 20px; ");
	   
	   if(playerGrid[row + 1 - tmp[0]][col].getText().isEmpty())
		   playerGrid[row + 1 - tmp[0]][col].setStyle("-fx-background-color: rgba(0,255,0,0.5); -fx-border-color: green; -fx-border-width: 3; -fx-font-size: 20px; ");
	   else
		   playerGrid[row + 1 - tmp[0]][col].setStyle("-fx-background-color: rgba(255,0,0,0.5); -fx-border-width: 3; -fx-font-size: 20px; ");
	   
	   if(playerGrid[row - 1 + tmp[1]][col].getText().isEmpty())
		   playerGrid[row - 1 + tmp[1]][col].setStyle("-fx-background-color: rgba(0,255,0,0.5); -fx-border-color: green; -fx-border-width: 3; -fx-font-size: 20px; ");
	   else
		   playerGrid[row - 1 + tmp[1]][col].setStyle("-fx-background-color: rgba(255,0,0,0.5); -fx-border-width: 3; -fx-font-size: 20px; ");
	    
	   return true;
	}
	
	
	private boolean checkObstacle(int row, int col) {	
	    boolean canUp = true, canDown = true, canLeft = true, canRight = true;

	    for (int i = 1; i < currentShip[0]; i++) {		// Controllo se posso metterla a DX
	        int newCol = col + i;
	        if (newCol >= N || !playerGrid[row][newCol].getText().isEmpty()) {
	            canRight = false;
	            break;
	        }
	    }

	    for (int i = 1; i < currentShip[0]; i++) {		// Controllo se posso metterla a SX
	        int newCol = col - i;
	        if (newCol < 0 || !playerGrid[row][newCol].getText().isEmpty()) {
	            canLeft = false;
	            break;
	        }
	    }

	    for (int i = 1; i < currentShip[0]; i++) {		// Controllo se posso metterla GIU
	        int newRow = row + i;
	        if (newRow >= N || !playerGrid[newRow][col].getText().isEmpty()) {
	            canDown = false;
	            break;
	        }
	    }

	    for (int i = 1; i < currentShip[0]; i++) {		// Controllo se posso metterla SU
	        int newRow = row - i;	
	        if (newRow < 0 || !playerGrid[newRow][col].getText().isEmpty()) {
	            canUp = false;
	            break;
	        }
	    }

	    return (canUp || canDown || canLeft || canRight);		// Se ritorno almeno un true la barca la posso mettere giù
	}
	
	private int[] checkOutOfBorders(int row, int col) {
		int[] tmp =  new int[4];  	// Array di integer per segnare da quale punto sforerebbe di uno il controllo
		for(int i = 0; i < 4; i++)
			tmp[i] = 0;
		
		if(row + 1 == N) 
			tmp[0]++;
		if(row - 1 < 0)
			tmp[1]++;
		if(col + 1 == N)
			tmp[2]++;
		if(col - 1 < 0)
			tmp[3]++;
				
		return tmp;
		
	}
	
	
	private void computerMove() {
				
	}
		
}

