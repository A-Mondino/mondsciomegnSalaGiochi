package com.mondsciomegn.salagiochi.gui;

import com.mondsciomegn.salagiochi.*;
import com.mondsciomegn.salagiochi.db.DataBaseContainer;
import com.mondsciomegn.salagiochi.db.User;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class Room {
	private Label roomLabel = new Label();
	Label label = new Label("");						// Uso una label vuota solo per mantenere le freccie laterali alla stessa altezza
	
	public void showRoom(BorderPane root, int room) {
		
		
        VBox box = new VBox(roomLabel,label);
        box.setStyle("-fx-font-size: 30px;");
        box.setAlignment(Pos.TOP_CENTER);
        root.setTop(box);
        root.setCenter(null);
        
		switch (room) {
	        case -1:
	        	roomLabel.setText("Tabellone");
	        	
	        	roomL(root);
	            break;
	        case 0:
	        	
	        	roomLabel.setText("Sala Giochi");
	        	roomM(root);												// Main Room
	            break;
	        case 1:
	        	
	        	roomLabel.setText("VideoGames");
	        	roomR(root);
	            break;
		} 
	}
	
	private void roomL(BorderPane root) {
		TableView<User> userTable = new TableView<>();

		addUserColums(userTable);											// Aggiungo le varie colonne
	    userTable.getItems().addAll(DataBaseContainer.getAllUsers());		// Carica i dati 
	  
	   
	    
	    //DA MODIFICARE IN CASO CI SIANO PIU TABELLE ---->>
	    userTable.setMaxWidth(600); 
	    StackPane tableContainer = new StackPane(userTable);				// Solo per rendere la tabella un po più carina
	    int numRecords = userTable.getItems().size();
	    int downPadding = 500 - numRecords;
	    if(downPadding < 50) downPadding = 50;
	    	
	    tableContainer.setPadding(new Insets(
	    	    0, 				// sopra
	    	    20, 			// destra
	    	    downPadding, 	// sotto
	    	    20  			// sinistra
	    	));
	   tableContainer.setAlignment(Pos.TOP_CENTER);
	    root.setCenter(tableContainer);	
	  // <<---- DA MODIFICARE IN CASO CI SIANO PIU TABELLE 
	}
	
	
	private void addUserColums(TableView<User> table) {
		
	    TableColumn<User, Integer> colId = new TableColumn<>("ID");
	    colId.setCellValueFactory(data ->
	        new SimpleIntegerProperty(data.getValue().getId()).asObject()
	    );

	    TableColumn<User, String> colNick = new TableColumn<>("NickName");
	    colNick.setCellValueFactory(data ->
	        new SimpleStringProperty(data.getValue().getNickname())
	    );

	    TableColumn<User, String> colName = new TableColumn<>("Nome");
	    colName.setCellValueFactory(data ->
	        new SimpleStringProperty(data.getValue().getName())
	    );
	    
	    TableColumn<User, String> colSurname = new TableColumn<>("Cognome");
	    colSurname.setCellValueFactory(data ->
	    	new SimpleStringProperty(data.getValue().getSurname())
	    );

	    TableColumn<User, Integer> colScore = new TableColumn<>("Score");
	    colScore.setCellValueFactory(data ->
	        new SimpleIntegerProperty(data.getValue().getScore()).asObject()
	    );

	    table.getColumns().addAll(colId, colNick, colName, colSurname, colScore);
	}

	
	private void roomM(BorderPane root) {
        
    }
	
	private void roomR(BorderPane root) {
	
	}
	
}
