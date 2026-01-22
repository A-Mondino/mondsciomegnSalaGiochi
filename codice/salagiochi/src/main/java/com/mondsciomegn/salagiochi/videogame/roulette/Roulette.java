package com.mondsciomegn.salagiochi.videogame.roulette;

import java.util.Optional;
import java.util.Random;

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

public class Roulette extends VideoGames {

    // Dipendenze
    private final RouletteRules rules;
    private final RouletteDB rouletteDBConnection;
    private final Random random;

    // Stato UI e Gioco
    private int playerScore = 0;
    private Button selectedButton = null;
    private GridPane grid = new GridPane();
    private Stage primaryStage = new Stage();
    
    private int gameTokens = 0;
    private boolean selectionLocked = false;
    
    // Costanti Grafiche
    private static final int BTN_SIZE = 70;
    
    /**
     * Costruttore della classe Roulette,
	 * Inizializza il punteggio a 600.
	 * 
     * @param name nome del gioco
     * @param category categoria a cui è associato il gioco 
     */

    public Roulette(String name, Category category) {
        super(name, category);
        this.rules = new RouletteRules();
        this.rouletteDBConnection = new RouletteDB();
        this.random = new Random();
        setScore(600); 
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
        dialog.setContentText("Punta su uno dei bottoni sulla mappa di gioco e buona fortuna!");
        
        ButtonType playType = new ButtonType("Gioca", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelType = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().setAll(playType, cancelType);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == playType) {
            startGame();
        }
    }

    
    /**
	 * Avvio del gioco, gestisce sia l'interfaccia grafica (griglia di gioco) che la logica effettiva del gioco.
	 * Genera i pulsanti numerici della roulette, con i rispettivi colori, e crea anche i pulsanti per le azioni 
	 * "speciali" (es: seleziona un'intera riga). 
	 * Viene richiamato il metodo per la conversione dei punti in gettoni per poter effettuare una puntata e 
	 * iniziare la partita.
	 */	
    private void startGame() {
    	primaryStage.setTitle("Roulette");
    	startTimer(primaryStage); 
    	playerScore = rouletteDBConnection.getScoreFromDB(getNickname());
        
        
        setupGrid();
        Scene scene = new Scene(grid, 1100, 470);
        primaryStage.setScene(scene);
        primaryStage.show();

        
        // Gestione chiusura finestra
        primaryStage.setOnCloseRequest(e -> {
            if (gameTokens > 0) {		// Se aveva ancora gettoni ricalcolo i punti
                updatePoints(0); 		// E glieli restituisco (il moltiplicatore è zero perchè non devo aggiungere punti extra della vincita
                gameTokens = 0;
            }
            stopTimer();
        });

        openConverterDialog();
    }

    /**
     * Costruisce l'interfaccia grafica della griglia.
     */
    private void setupGrid() {
        grid.getChildren().clear();
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setStyle("-fx-background-color: darkgreen; -fx-padding: 20;");

        // 1. Numeri (Griglia 3 righe)
        int[][] layout = {
            {1, 4, 7, 10, 13, 16, 19, 22, 25, 28, 31, 34},
            {2, 5, 8, 11, 14, 17, 20, 23, 26, 29, 32, 35},
            {3, 6, 9, 12, 15, 18, 21, 24, 27, 30, 33, 36}
        };

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < layout[row].length; col++) {
                int num = layout[row][col];
                String color = rules.getColorForNumber(num);
                
                Button btn = createGameButton(String.valueOf(num), color, BTN_SIZE, BTN_SIZE);
                grid.add(btn, col, row + 1);
            }
        }

        // 2. Puntate Righe
        String[] choiceRow = {"Punta sull'intera riga 1", "Punta sull'intera riga 2", "Punta sull'intera riga 3"};
        for (int i = 0; i < 3; i++) {
            Button btn = createGameButton(choiceRow[i], "white", BTN_SIZE, BTN_SIZE);
            btn.setStyle(btn.getStyle() + "-fx-text-fill: black; -fx-font-size: 12px;"); // Override stile testo
            grid.add(btn, 12, i + 1);
        }

        // 3. Puntate Dozzine (bottoni larghi 4)
        String[] choiceColumns = {"Punta sui primi 12 numeri", "Punta sui numeri da 13 a 24", "Punta sui numeri da 25 a 36"};
        for (int i = 0; i < 3; i++) {
            Button btn = createGameButton(choiceColumns[i], "white", BTN_SIZE * 4 + 18, BTN_SIZE);
            btn.setStyle("-fx-background-color: white; -fx-border-color: black;");
            grid.add(btn, i * 4, 4, 4, 1);
        }

        // 4. Puntate Speciali (bottoni larghi 2)
        String[] choiceExtra = {"Punta 1-18", "Pari", "Rosso", "Nero", "Dispari", "Punta 19-36"};
        String[] colorsExtra = {"white", "white", "red", "black", "white", "white"};
        
        for (int i = 0; i < 6; i++) {
            Button btn = createGameButton(choiceExtra[i], colorsExtra[i], BTN_SIZE * 2 + 7, BTN_SIZE);
            if(colorsExtra[i].equals("white")) {
                 btn.setStyle(btn.getStyle() + "-fx-text-fill: black;");
            }
            grid.add(btn, i * 2, 5, 2, 1);
        }
    }
    
    /**
     * Metodo helper per creare bottoni standardizzati
     */
    private Button createGameButton(String text, String color, double width, double height) {
        Button btn = new Button(text);
        btn.setMinSize(width, height);
        btn.setStyle(
            "-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white; " +
            "-fx-background-color: " + color + "; -fx-border-color: white;"
        );
        
        btn.setOnAction(e -> handleButtonClick(btn));
        return btn;
    }

    
    private void handleButtonClick(Button btn) {
    	String currentChoice;
        currentChoice = selectButton(btn);
        if (currentChoice != null) {
            int nWinner = random.nextInt(36) + 1;
            checkWinner(nWinner, currentChoice);
        }
    }

    
    private String selectButton(Button cell) {
        if (gameTokens <= 0) {
            showMessage("Non hai gettoni disponibili! Converti altri punti.");
            return null;
        }
        if (selectionLocked) return null;		// Per gestire eccezioni

        selectedButton = cell;
        cell.setStyle(cell.getStyle() + "-fx-border-color: yellow; -fx-border-width: 3px;");
        selectionLocked = true;
        
        return cell.getText().trim();
    }

    private void checkWinner(int extractedNum, String decision) {
        
        int multiplier = rules.checkWin(extractedNum, decision);	// Uso la classe Logic per calcolare il moltiplicatore della vincita

        if (multiplier > 0 ) {
            showMessage("Numero estratto: " + extractedNum + "\nHAI VINTO!");
           
            updatePoints(multiplier);  // Aggiorno punti: moltiplicatore * 300 + devo ridare la puntata
            registerGame(getNickname(), (multiplier + gameTokens) * 300);		// Registro la partita
        } else { 	// Non ho vinto nulla, devo registrare però la partedita di punti
            showMessage("Numero estratto: " + extractedNum + "\nHAI PERSO!");
            registerGame(getNickname(), (multiplier * gameTokens) * 300);		// Registro la perdita dei punti
        }
        
                
        gameTokens = 0; 			// Reset stato
        selectionLocked = false;
        if (selectedButton != null) {
            String baseStyle = selectedButton.getStyle().replace("-fx-border-color: yellow; -fx-border-width: 3px;", "");
            selectedButton.setStyle(baseStyle);
            selectedButton = null;
        }

        // Riapro il converter
        openConverterDialog();
    }

    private void openConverterDialog() {
        Stage tokenStage = new Stage();
        tokenStage.setTitle("Converti punti in gettoni");
        
        VBox root = new VBox(15);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #A5D6A7; -fx-background-radius: 20px; -fx-padding: 20px;");
        
        playerScore = rouletteDBConnection.getScoreFromDB(getNickname());
        
        Label pointsLabel = new Label("Punti disponibili: " + playerScore);
        Label tokensLabel = new Label("Gettoni disponibili: " + gameTokens);
        
        // Stili Label
        String lblStyle = "-fx-font-size: 18px; -fx-font-weight: bold;";
        pointsLabel.setStyle(lblStyle);
        tokensLabel.setStyle(lblStyle);

        Button convertBtn = new Button("Converti 300 punti = 1 gettone");
        Button betBtn = new Button("Punta i gettoni");
        Button exitBtn = new Button("Esci");
        
        String btnStyle = "-fx-font-size: 16px; -fx-font-weight: bold;";
        convertBtn.setStyle(btnStyle);
        betBtn.setStyle(btnStyle);
        exitBtn.setStyle(btnStyle);
        
        convertBtn.setMaxWidth(Double.MAX_VALUE);
        betBtn.setDisable(gameTokens <= 0);

        convertBtn.setOnAction(e -> {
            if (playerScore >= 300) {
                // Modifica punti: -1 gettone (che vale 300 punti)
            	
                rouletteDBConnection.updateScore(getNickname(), playerScore - 300);
                playerScore = rouletteDBConnection.getScoreFromDB(getNickname());
                gameTokens++;
                
                pointsLabel.setText("Punti disponibili: " + playerScore);
                tokensLabel.setText("Gettoni convertiti: " + gameTokens);
                betBtn.setDisable(false);
            } else {
            	if (playerScore < 300) {
	            	Alert alert = new Alert(Alert.AlertType.INFORMATION, "Non hai abbastanza punti per convertire altri gettoni!");
	                alert.showAndWait();
	                // Se non ha gettoni, non ne ha mai convertiti quindi deve prima procurarsene
	                if (gameTokens == 0) {
		                tokenStage.close();
		                primaryStage.close();
		                stopTimer();
	                }
            	}
            }
        });

        betBtn.setOnAction(e -> {
            tokenStage.close();
            primaryStage.getScene().getRoot().setDisable(false);
        });
        
        exitBtn.setOnAction(e -> {
            if (gameTokens > 0) {
                updatePoints(0);
                gameTokens = 0;
            }
            tokenStage.close();
            primaryStage.close();
            stopTimer();
        });
        
        HBox buttonBox = new HBox(exitBtn, betBtn);
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        root.getChildren().addAll(pointsLabel, tokensLabel, convertBtn, buttonBox);

        Scene scene = new Scene(root, 300, 230);
        scene.setFill(null);
        tokenStage.initStyle(StageStyle.TRANSPARENT);
        tokenStage.setScene(scene);

        primaryStage.getScene().getRoot().setDisable(true);
        tokenStage.initModality(Modality.APPLICATION_MODAL);
        tokenStage.showAndWait();
    }

    /**
     * Aggiorna i punti nel DB basandosi sui gettoni e sul moltiplicatore di vincita.
     */
    private void updatePoints(int multiplier) {
        int pointsToAdd = 300 * (gameTokens + multiplier);

        rouletteDBConnection.updateScore(getNickname(), (playerScore + pointsToAdd));
    }
}