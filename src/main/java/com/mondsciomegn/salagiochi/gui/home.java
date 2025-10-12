package com.mondsciomegn.salagiochi.gui;



import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;


public class home extends Application{
	private int currentRoom = 0; 
    private Label roomLabel;

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sala Giochi");

        // Layout principale
        BorderPane root = new BorderPane();

        // Etichetta centrale
        roomLabel = new Label("Stanza Centrale");
        roomLabel.setStyle("-fx-font-size: 30px;");
        StackPane centerPane = new StackPane(roomLabel);
        centerPane.setAlignment(Pos.CENTER);
        root.setCenter(centerPane);

        // Freccia sinistra
        Button leftArrow = new Button("◄");
        leftArrow.setStyle("-fx-font-size: 40px; -fx-background-color: transparent;");
        leftArrow.setOnAction(e -> switchRoom(-1)); // Evento click
        StackPane leftPane = new StackPane(leftArrow);
        leftPane.setAlignment(Pos.CENTER_LEFT);
        root.setLeft(leftPane);

        // Freccia destra
        Button rightArrow = new Button("►");
        rightArrow.setStyle("-fx-font-size: 40px; -fx-background-color: transparent;");
        rightArrow.setOnAction(e -> switchRoom(1)); // Evento click
        StackPane rightPane = new StackPane(rightArrow);
        rightPane.setAlignment(Pos.CENTER_RIGHT);
        root.setRight(rightPane);

        // Scena
        Scene scene = new Scene(root, 1200, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Metodo per cambiare stanza
    private void switchRoom(int direction) {
        currentRoom += direction;

        // Limita le stanze a 3 (sinistra, centrale, destra)
        if (currentRoom < -1) currentRoom = 1;
        if (currentRoom > 1) currentRoom = -1;

        switch (currentRoom) {
            case -1:
                roomLabel.setText("Stanza Sinistra");
                break;
            case 0:
                roomLabel.setText("Stanza Centrale");
                break;
            case 1:
                roomLabel.setText("Stanza Destra");
                break;
        }
    }

    public static void main(String[] args) {
        launch(args); 							// Avvia l'applicazione JavaFX
    }
}


