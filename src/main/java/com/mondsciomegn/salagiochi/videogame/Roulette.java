package com.mondsciomegn.salagiochi.videogame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

import com.mondsciomegn.salagiochi.db.Category;
import com.mondsciomegn.salagiochi.db.DataBaseConnection;
import com.mondsciomegn.salagiochi.db.VideoGames;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class Roulette extends VideoGames{

	Scanner scanner = new Scanner(System.in);
	Random random = new Random();						// Per estrazione vincente

    private List<Integer> numeri = new ArrayList<>();
    
    private GridPane grid = new GridPane();				// Per la visualizzazione grafica
    
    private Stage primaryStage = new Stage();
    
	public Roulette(String name, Category category) {
		super(name, category);
		setScore(600);
	}
	
	public void play(String nickname) {
		setNickname(nickname);
		Dialog<ButtonType> dialog = new Dialog<>();
    	dialog.setTitle("Dettagli Gioco");
    	dialog.setHeaderText("Istruzioni:");
    	dialog.setContentText(
    	        "Seleziona uno dei numeri nel tabellone (considerando il colore), l'avversario farà la stessa cosa.\n" +
    	        "La roulette gira e si ferma su un determinato numero di un determinato colore.\n" +
    	        "Se il numero estratto combacia con quello che hai scelto, HAI VINTO!\n" +
    	        "Se, invece, combacia con quello dell'avversario, HAI PERSO!\n" +
    	        "Se non combacia con nessuno dei due numeri decisi dai giocatori il gioco termina in PAREGGIO. "
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
    	
    	primaryStage.setTitle("Roulette");
    	grid.getChildren().clear(); 			// pulisci griglia se si rigioca
    	grid.setHgap(5);
    	grid.setVgap(5);
    	grid.setStyle("-fx-background-color: black; -fx-padding: 10;");

		numeri.clear();
    	int index = 0;

    	// Per inserire lo stesso numero due volte nell'area di gioco 
    	for (int n = 1; n <= 40; n++) {
    	    numeri.add(n);
    	    numeri.add(n);
    	}

    	// Label centrale per messaggi di output
    	Label messageLabel = new Label("Benvenuto nella Roulette!");
    	messageLabel.setWrapText(true);
    	messageLabel.setMinSize(220, 220);
    	messageLabel.setAlignment(Pos.CENTER);
    	messageLabel.setStyle("-fx-background-color: white; ");
    	
        grid.add(messageLabel, 3, 3, 3, 3); 	// posizione 3,3 con larghezza/altezza 3 celle


    	for (int i = 0; i < 9; i++) {
    	    for (int j = 0; j < 9; j++) {

    	    	// Salta le celle centrali (3-5)
                if (i >= 3 && i <= 5 && j >= 3 && j <= 5) continue;

    	        Button btn = new Button();
    	        btn.setMinSize(70, 70);

    	        if (index < numeri.size()) {
    	            int numero = numeri.get(index);
    	            btn.setText(String.valueOf(numero));

    	            long occorrenze = numeri.subList(0, index).stream()
    	                    .filter(n -> n == numero)
    	                    .count();
    	            String color = (occorrenze == 0) ? "red" : "black"; // rosso o nero

    	            btn.setStyle(
    	                "-fx-font-size: 20px; " +
    	                "-fx-font-weight: bold; " +
    	                "-fx-text-fill: white; " +
    	                "-fx-background-color: " + color + ";"
    	            );

    	            btn.setOnAction(e -> {
    	                //Scelta giocatore
    	                int num = numero;
    	                String colore = color.equals("red") ? "rosso" : "nero";

    	                //Scelta avversario
    	                int numComputer = random.nextInt(40) + 1;
    	                String coloreComputer = (numComputer % 2 == 0) ? "nero" : "rosso";

    	                //Estrazione numero vincente
    	                int numVincente = random.nextInt(40) + 1;
    	                String coloreVincente = (numVincente % 2 == 0) ? "nero" : "rosso";
    	               
    	                //Output di gioco
    	                messageLabel.setText("Hai selezionato il numero " + num + ", " +colore+ "\n\n" +
    	                "L'avversario ha selezionato il numero " + numComputer + ", " +coloreComputer+ "\n\n\n" +
    	                "L'estrazione vincente è " + numVincente + ", " +coloreVincente+ "\n\n\n" +
    	                "(Seleziona un'altra casella per iniziare un'altra partita)");
    	                
    	                controlloVincita(num,numComputer,numVincente,colore,coloreComputer,coloreVincente);
    	            });
    	            index++;
    	        } 
    	        grid.add(btn, j, i);
    	    }
    	}
    	Scene scene = new Scene(grid, 720, 720);
    	primaryStage.setScene(scene);
    	primaryStage.showAndWait();
		
	}
	
	private void controlloVincita(int num, int numComputer, int numVincente, String colore, String coloreComputer, String coloreVincente) {
		if (num == numVincente && colore.equals(coloreVincente)) {
			showMessage("HAI VINTO!");
            addPoints(getNickname());

	    } else if (numComputer == numVincente && coloreComputer.equals(coloreVincente)) {
			showMessage("HAI PERSO!");
            addPoints("_COMPUTER_");

	    } else {
			showMessage("PAREGGIO!");
	    }
	}

}