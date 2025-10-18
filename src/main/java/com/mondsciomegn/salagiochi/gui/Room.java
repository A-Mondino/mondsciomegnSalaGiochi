package com.mondsciomegn.salagiochi.gui;

import com.mondsciomegn.salagiochi.*;
import com.mondsciomegn.salagiochi.db.Category;
import com.mondsciomegn.salagiochi.db.DataBaseContainer;
import com.mondsciomegn.salagiochi.db.User;
import com.mondsciomegn.salagiochi.db.VideoGames;
import com.mondsciomegn.salagiochi.videogame.*;

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
import javafx.scene.layout.GridPane;
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
	    TableView<VideoGames> gameTable = new TableView<>();
	   double tableWidth = 500;		// Solo per uniformità di grandezza delle tabelle che hanno num di colonne diverse

	   
	    addUserColums(userTable); // Aggiungo le colonne
	    addGameColums(gameTable);
	    
	    
	    userTable.getItems().addAll(DataBaseContainer.getAllUsers()); // Aggiungo i dati
	    gameTable.getItems().addAll(DataBaseContainer.getAllGames());
	    
	    
	    
	    userTable.setPrefWidth(tableWidth);
	    gameTable.setPrefWidth(tableWidth);
	    StackPane userPane = new StackPane(userTable);	// Metto tutto dentro ad un StackPane per centrare tutto
	    StackPane gamePane = new StackPane(gameTable);
	    userPane.setAlignment(Pos.CENTER);
	    gamePane.setAlignment(Pos.CENTER);
	    

	    
	    GridPane grid = new GridPane();	// Creo una griglia 2x2
	    grid.setPadding(new Insets(20));
	    grid.setHgap(20); // spazio orizzontale tra le tabelle
	    grid.setVgap(20); // spazio verticale tra le tabelle

	    // Posiziona le tabelle nella prima riga
	    grid.add(userTable, 0, 0); // colonna 0, riga 0
	    grid.add(gameTable, 1, 0); // colonna 1, riga 0


	    // Centrare la griglia nella stanza
	    BorderPane.setAlignment(grid, Pos.CENTER);
	    root.setCenter(grid);
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
		

		Button start = new Button("Inizia a giocare");		// Bottone di inizio
		root.setCenter(start);
		
		VBox layout = new VBox(20, start);
		root.setCenter(layout);
	}

	private void addGameColums(TableView<VideoGames> gameTable) {
		TableColumn<VideoGames, String> name = new TableColumn<>("Gioco: ");
		name.setCellValueFactory(data ->
        new SimpleStringProperty(data.getValue().getName()));

		
		TableColumn<VideoGames, Integer> score = new TableColumn<>("Punteggio: ");
		score.setCellValueFactory(data ->
		new SimpleIntegerProperty(data.getValue().getScore()).asObject());
		
	/*	TableColumn<VideoGames, Category> category = new TableColumn<>("Categoria: ");
		category.setCellValueFactory(data ->
        new Category(data.getValue().getCategory()));*/
		
		gameTable.getColumns().addAll(name, score);
		
	}
	
}
