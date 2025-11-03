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
	
	private Button[][] playerGrid = new Button[8][8];		// Griglia del Giocatore
    private Button[][] computerGrid = new Button[8][8];		// Matrice di supporto
    

    private GridPane player = new GridPane();				// Servono per la visualizzazione grafica
    private GridPane computer = new GridPane();
    
    
    private boolean gameOver = false;				
    private boolean allBoatPuttedDown = false;				// per l'inizio del game
    private int currentShip = 1;
    
    
    private Random random = new Random();
    
    private Stage primaryStage = new Stage();
	
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

        for (int i = 0; i < 8; i++) {		// Inizializzo i due campi da gioco
            for (int j = 0; j < 8; j++) {		
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
        
        
        for (int i = 0; i < 8; i++) {		// per le 3 x 3 caselle della griglia
            for (int j = 0; j < 8; j++) {
            	
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
	}
	
	
	private boolean playerMove(int row, int col, String nickname) {
		
		if(!allBoatPuttedDown) {
			putAllBoatdown(row, col);
		}
		
		
		return true;
	}
	
	private void putAllBoatdown(int row, int col) {
		for(int i = 0; i < currentShip; i++) {
			playerGrid[row][col].setText(getNearship(row, col).toString());
			//currentShip--;
		}
		//currentShip++;
	}
	
	
	private Integer getNearship(int row, int col) {
		Integer move = currentShip;
		
		/*if(playerGrid[row][col].getText().equals("")) {
			move = 1; 
			
		}*/
		
		return move;
	}
	private void computerMove() {
				
	}
	
	
	
}

