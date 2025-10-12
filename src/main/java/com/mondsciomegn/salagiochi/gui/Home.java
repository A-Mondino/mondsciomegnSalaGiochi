package com.mondsciomegn.salagiochi.gui;


import com.mondsciomegn.salagiochi.db.DataBaseInitializer;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;


public class Home extends Application{
	private int currentRoom = 0; 		// Per capire in quale stanza siamo
    private Room room = new Room();	
    private BorderPane root = new BorderPane();	// Finestra 

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sala Giochi");

        // Etichette centrali
        Label roomLabel = new Label("Benvenuto in sala giochi!!");
        Label label = new Label("Se è la tua prima volta, guardati un pò intorno!!");
		
        VBox box = new VBox(5);	// 15 è lo spazio tra le due label
        box.setStyle("-fx-font-size: 30px;");
        box.getChildren().addAll(roomLabel, label);	// Aggiungo le due etichette ad una Vertical Box, che impila verticalmente i parametri
        box.setAlignment(Pos.TOP_CENTER);
        root.setTop(box);

        // Freccia sinistra
        Button leftArrow = new Button("◄");
        leftArrow.setStyle("-fx-font-size: 40px; -fx-background-color: transparent;");
        leftArrow.setOnAction(e -> switchRoom(-1)); // Evento OnClick
        StackPane leftPane = new StackPane(leftArrow);
        leftPane.setAlignment(Pos.CENTER_LEFT);
        root.setLeft(leftPane);

        // Freccia destra
        Button rightArrow = new Button("►");
        rightArrow.setStyle("-fx-font-size: 40px; -fx-background-color: transparent;");
        rightArrow.setOnAction(e -> switchRoom(1)); // Evento OnClick
        StackPane rightPane = new StackPane(rightArrow);
        rightPane.setAlignment(Pos.CENTER_RIGHT);
        root.setRight(rightPane);

        // Scena
        Scene scene = new Scene(root, 1200, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    
    private void switchRoom(int direction) { // Metodo per cambiare stanza
        currentRoom += direction;

        // Limita le stanze a 3 (sinistra, centrale, destra)
        if (currentRoom < -1) currentRoom = 1;	// Se sono a SX e voglio andare a SX resetto la current room a 1 (stanza a DX)
        if (currentRoom > 1) currentRoom = -1;	// Se sono a DX e voglio andare a DX resetto la current room a -1 (stanza a SX)
        
        room.showRoom(root, currentRoom);
        
    }

    public static void main(String[] args) {
    	DataBaseInitializer.initialize();		// Preparo il DataBase

        launch(args); 							// Avvia l'applicazione JavaFX
    }
}


