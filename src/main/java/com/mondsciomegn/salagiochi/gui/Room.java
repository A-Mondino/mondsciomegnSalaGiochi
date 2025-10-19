package com.mondsciomegn.salagiochi.gui;

import com.mondsciomegn.salagiochi.*;
import com.mondsciomegn.salagiochi.db.Category;
import com.mondsciomegn.salagiochi.db.DataBaseContainer;
import com.mondsciomegn.salagiochi.db.DataBaseInitializer;
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
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class Room extends Application{
	private int currentRoom = 0; 
	private Stage primaryStage = null;
	private BorderPane root = new BorderPane();				// Finestra 
	private Label roomLabel = new Label();
	Label label = new Label("");						// Uso una label vuota solo per mantenere le freccie laterali alla stessa altezza
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		showRoom(currentRoom);
	}
	 
    public static void main(String[] args) {
    	DataBaseInitializer.initialize();					// Preparo il DataBase

        launch(args); 										// Avvia l'applicazione JavaFX
    }
	
	private void switchRoom(int direction) { 				// Metodo per cambiare stanza
        currentRoom += direction;

        // Limita le stanze a 3 (sinistra, centrale, destra)
        if (currentRoom < -1) currentRoom = 1;				// Se sono a SX e voglio andare a SX resetto la current room a 1 (stanza a DX)
        if (currentRoom > 1) currentRoom = -1;				// Se sono a DX e voglio andare a DX resetto la current room a -1 (stanza a SX)
        showRoom(currentRoom);
        
    }

	private void showRoom(int room) {
			
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
		
	private void roomM(BorderPane root) {
		primaryStage.setTitle("Sala Giochi");
        String imagePath = getClass().getResource("./img/roomM1.jpg").toExternalForm();
        BackgroundImage bgImage = new BackgroundImage(
                new Image(imagePath),
                BackgroundRepeat.NO_REPEAT,		// Serve a non far ripetere l'immagine nè in orizz
                BackgroundRepeat.NO_REPEAT,		// Nè in verticale
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        //BackgroundSize.AUTO,
                        //BackgroundSize.AUTO,
                		1.0,
                		1.0,
                        true,	// Larghezza in percentuale = false
                        true, 	// Altezza in percentuale = false
                        false, 	// Adatta alla pagina (coprire l'intera area)
                        true)	// Ritagliare l'immagine = false
        );
        root.setBackground(new Background(bgImage));
        
        

        // Etichette 
        Label roomLabel = new Label("Benvenuto in sala giochi!!");
        Label label = new Label("Se è la tua prima volta, guardati un po' intorno!!");
        roomLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
        label.setStyle("-fx-font-size: 20px;");

        // Giusto un po di formattazzione grafica ---->

        VBox box = new VBox(10);   // Spazio tra etichette
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.BOTTOM_CENTER);  
        box.setMaxWidth(550);
        box.setMaxHeight(100);
        box.setStyle("-fx-background-color: rgba(255,255,255,0.8); -fx-background-radius: 10;");

        
        box.getChildren().addAll(roomLabel, label);			// Aggiungo le due etichette ad una Vertical Box, che impila verticalmente i parametri
        StackPane centerPane = new StackPane(box);
        centerPane.setAlignment(Pos.BOTTOM_CENTER);  
        root.setCenter(centerPane);

        
  
        // Freccia sinistra
        Button leftArrow = new Button("◄");
        leftArrow.setStyle("-fx-font-size: 40px; -fx-background-color: transparent;");
        leftArrow.setOnAction(e -> switchRoom(-1)); 		// Evento OnClick
        
        // Wrapper pane per renderlo visibile
        StackPane leftPane = new StackPane(leftArrow);
        leftPane.setPadding(new Insets(5)); // piccolo padding interno
        leftPane.setAlignment(Pos.CENTER_LEFT);
        leftPane.setStyle("-fx-background-color: rgba(0,0,0,0.3); -fx-background-radius: 5;");

        root.setLeft(leftPane);


        // Freccia destra
        Button rightArrow = new Button("►");
        rightArrow.setStyle("-fx-font-size: 40px; -fx-background-color: transparent;");
        rightArrow.setOnAction(e -> switchRoom(1)); 		// Evento OnClick
        
        StackPane rightPane = new StackPane(rightArrow);
        rightPane.setPadding(new Insets(5));
        rightPane.setAlignment(Pos.CENTER_RIGHT);
        rightPane.setStyle("-fx-background-color: rgba(0,0,0,0.3); -fx-background-radius: 10;");

        root.setRight(rightPane);

        
        
        // Scena
        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setScene(scene);
        primaryStage.show();
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


}
