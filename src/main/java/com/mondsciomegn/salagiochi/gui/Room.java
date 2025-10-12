package com.mondsciomegn.salagiochi.gui;

import com.mondsciomegn.salagiochi.*;
import com.mondsciomegn.salagiochi.db.DataBaseContainer;
import com.mondsciomegn.salagiochi.db.Utente;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class Room {
	private Label roomLabel = new Label();
	Label label = new Label("");			// Uso una label vuota solo per mantenere le freccie laterali alla stessa altezza
	
	public void showRoom(BorderPane root, int room) {
		
		
        VBox box = new VBox(roomLabel,label);
        box.setStyle("-fx-font-size: 30px;");
        box.setAlignment(Pos.TOP_CENTER);
        root.setTop(box);
        root.setCenter(null);
        
		switch (room) {
	        case -1:
	        	roomLabel.setText("Tabellone");
	        	
	        	StanzaSX(root);
	            break;
	        case 0:
	        	
	        	roomLabel.setText("Sala Giochi");
	        	StanzaCENTRALE(root);
	            break;
	        case 1:
	        	
	        	roomLabel.setText("VideoGames");
	        	StanzaDX(root);
	            break;
		} 
	}
	
	private void StanzaSX(BorderPane root) {
		TableView<Utente> table = new TableView<>();

	    TableColumn<Utente, Integer> colId = new TableColumn<>("ID");
	    colId.setCellValueFactory(data -> 
	        new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject()
	    );

	    TableColumn<Utente, String> colNick = new TableColumn<>("NickName");
	    colNick.setCellValueFactory(data ->
	        new javafx.beans.property.SimpleStringProperty(data.getValue().getNickname())
	    );

	    TableColumn<Utente, String> colNome = new TableColumn<>("Nome");
	    colNome.setCellValueFactory(data ->
	        new javafx.beans.property.SimpleStringProperty(data.getValue().getNome())
	    );

	    TableColumn<Utente, Integer> colScore = new TableColumn<>("Score");
	    colScore.setCellValueFactory(data ->
	        new javafx.beans.property.SimpleIntegerProperty(data.getValue().getScore()).asObject()
	    );

	    table.getColumns().addAll(colId, colNick, colNome, colScore);

	    // Carica i dati (vuoto per ora)
	    table.getItems().addAll(DataBaseContainer.getAllUtenti());

	    // Inserisci nella scena
	    root.setCenter(table);
		
	}
	
	private void StanzaCENTRALE(BorderPane root) {
        
    }
	
	private void StanzaDX(BorderPane root) {
	
	}
	
}
