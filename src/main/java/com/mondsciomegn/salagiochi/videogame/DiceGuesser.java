package com.mondsciomegn.salagiochi.videogame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Random;

import com.mondsciomegn.salagiochi.db.Category;
import com.mondsciomegn.salagiochi.db.DataBaseConnection;
import com.mondsciomegn.salagiochi.db.VideoGames;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class DiceGuesser extends VideoGames{
	

    private Stage primaryStage = new Stage();
    
    private ImageView diceUser1 = new ImageView();
    private ImageView diceUser2 = new ImageView();
    private ImageView diceComp1 = new ImageView();
    private ImageView diceComp2 = new ImageView();
	
    
	public DiceGuesser(String name, Category category) {
		super(name, category);
		setScore(50);
	}
	
	
	public void play(String nickname) { 
		setNickname(nickname);
		Dialog<ButtonType> dialog = new Dialog<>();
    	dialog.setTitle("Dettagli Gioco");
    	dialog.setHeaderText("Istruzioni:");
    	dialog.setContentText(
    	        "Lancia i due dadi, chi ottiene la somma maggiore dei dati lanciati vince!\n" +
                "Se entrambi i giocatori ottengono la stessa somma dal lancio dei dadi " +
                "il gioco finisce in pareggio."
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
		if(getNickname().isEmpty()) {														// Significa che qualcuno sta giocando in anonimo
            String sql = "INSERT INTO utente (nickname, nome, psww, score)" +
	  				  "SELECT '_ANONIMO_', 'Anonimo', '' , 0 " +
	  				  "WHERE NOT EXISTS (SELECT 1 FROM utente WHERE nickname = '_ANONIMO_');"; 
            
            try (Connection conn = DataBaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {
                    try {
                        stmt.executeUpdate(); 												// Prova a inserire l'utente
                    } catch (SQLException ex) { 
                    	ex.printStackTrace();
                    }
                   
              } catch (SQLException e1) {
            	  e1.printStackTrace();
              }
        }
		
		primaryStage.setTitle("Gioco dei Dadi");

        Label title = new Label("Lancio dei Dadi");
        
        Label message = new Label("-");
        Label message1 = new Label("-");

        HBox playerBox = new HBox(10, diceUser1, diceUser2); 								// dadi giocatore
        playerBox.setAlignment(Pos.CENTER);

        VBox playerSection = new VBox(5, new Label("Giocatore:"), playerBox, message);
        playerSection.setAlignment(Pos.CENTER);

        HBox compBox = new HBox(10, diceComp1, diceComp2); 									// dadi computer
        compBox.setAlignment(Pos.CENTER);

        VBox compSection = new VBox(5, new Label("Computer:"), compBox, message1);
        compSection.setAlignment(Pos.CENTER);


        Button throwButton = new Button("Lancia i Dadi");

        throwButton.setOnAction(e -> {
                int dice1 = throwDice();
                diceUser1.setImage(viewImage(dice1));

                int dice2 = throwDice();
                diceUser2.setImage(viewImage(dice2));

                int sumPlayer = dice1 + dice2;

                message.setText("La somma dei dadi che hai lanciato è: " + sumPlayer);

                int dice3 = throwDice();
                diceComp1.setImage(viewImage(dice3));

                int dice4 = throwDice();
                diceComp2.setImage(viewImage(dice4));

                int sumComputer = dice3 + dice4;
                message1.setText("La somma dei dadi lanciati dall'avversario è: " + sumComputer);

                checkHigherDice(sumPlayer, sumComputer);
        });
        
        VBox center = new VBox(20, title, playerSection, compSection);
        center.setAlignment(Pos.CENTER);

        BorderPane layout = new BorderPane();
        layout.setCenter(center);
        layout.setBottom(throwButton);
        BorderPane.setAlignment(throwButton, javafx.geometry.Pos.CENTER);
        layout.setStyle("-fx-padding: 20;");

        Scene scene = new Scene(layout, 350, 450);
        primaryStage.setScene(scene);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        startTimer(primaryStage);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            stopTimer();
        });
		
	}
	
	
	private static int throwDice() {
		Random ran = new Random();			//Lancio casuale del dado 
		int dice;
		
		dice = ran.nextInt(6) + 1;
		
		return dice;
	}
	
	
	private void checkHigherDice(int sum, int sumC) {
		if(sum < sumC) {
			showMessage("HAI PERSO!");
            addPoints("_COMPUTER_");
            registerGame(getNickname(), 0);
		}else {
			if(sum > sumC) {
				showMessage("HAI VINTO!");
	            addPoints(getNickname());
	            registerGame(getNickname(), 1);
			}else {
					showMessage("PAREGGIO!");
					registerGame(getNickname(), 0);
				}
		}
	}
	
	private Image viewImage(int dice) {
	    // Percorso per il caricamento delle immagini
	    String imgPath = getClass()
	            .getResource("/com/mondsciomegn/salagiochi/gui/") 					// Percordo esatto per arrivare alla cartella gui 
	            .toString()															// Converte percorso in lista
	            .replace("gui/", "gui/img/");  										// Cambiamo percorso della stringa per puntare alla cartella "img"

	    String fullPath = imgPath + dice + ".jpg";
	    return new Image(fullPath, 80, 80, true, true);
	}



}
