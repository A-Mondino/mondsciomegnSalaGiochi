package com.mondsciomegn.salagiochi.gui;


import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.mondsciomegn.salagiochi.db.Category;
import com.mondsciomegn.salagiochi.db.DataBaseConnection;
import com.mondsciomegn.salagiochi.db.DataBaseContainer;
import com.mondsciomegn.salagiochi.db.DataBaseInitializer;
import com.mondsciomegn.salagiochi.db.User;
import com.mondsciomegn.salagiochi.db.VideoGames;
import com.mondsciomegn.salagiochi.videogame.*;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.beans.property.StringProperty;


public class Room extends Application{
	private int currentRoom = 0; 
	private Boolean firstTime = true;
    private StringProperty currentNickName = new SimpleStringProperty("");		// Memorizza il NickName dopo il login
	
	private Stage primaryStage = null;
	private BorderPane root = new BorderPane();				// Finestra 
	private Label roomLabel = new Label();
	private final int roomH = 1000;
	private final int roomW = 650;
	Scene scene = new Scene(root, roomH, roomW);
	
	
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
        if (currentRoom < -1) 
        	currentRoom = 1;				// Se sono a SX e voglio andare a SX resetto la current room a 1 (stanza a DX)
        
        if (currentRoom > 1) 
        	currentRoom = -1;				// Se sono a DX e voglio andare a DX resetto la current room a -1 (stanza a SX)
       
        showRoom(currentRoom); 
    }

	
	private void showRoom(int room) {
       root.setCenter(null);
       roomLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
       
       switch (room) {
	        case -1:	    
	        	roomLabel.setText("Tabellone");
	        	roomL();
	            break;
	            
	        case 0:
	        	roomLabel.setText("Benvenuto in sala giochi!!");
	        	roomM();												// Main Room
	            break;
	            
	        case 1:	  
	        	roomLabel.setText("VideoGames");
	        	roomR();
	            break;
		} 
	}

		
	private void roomL() {
		root.getTop().setVisible(false);

        StackPane centerPane = new StackPane(boxTitle());
        centerPane.setAlignment(Pos.BOTTOM_CENTER);          
        
		TableView<User> userTable = new TableView<>();
	    TableView<VideoGames> gameTable = new TableView<>();
	    
	    double tableWidth = 500;							// Solo per uniformità di grandezza delle tabelle che hanno num di colonne diverse
	   
	    addUserColumns(userTable); 										// Aggiunta delle colonne
	    userTable.getColumns().get(2).setVisible(false);				// Nascondo la password
	    addGameColumns(gameTable);
	   
	    
	    userTable.getItems().addAll(DataBaseContainer.getAllUsers());	// Aggiunta dei dati
	    gameTable.getItems().addAll(DataBaseContainer.getAllGames());
	    
	   
	    userTable.setPrefWidth(tableWidth);
	    gameTable.setPrefWidth(tableWidth);
	    StackPane userPane = new StackPane(userTable);					// Creazione StackPane per ogni tabella
	    StackPane gamePane = new StackPane(gameTable);
	    userPane.setAlignment(Pos.CENTER);
	    gamePane.setAlignment(Pos.CENTER);

	    
	    GridPane grid = new GridPane();									// Creazione della griglia 2x2
	    grid.setPadding(new Insets(20));
	    grid.setHgap(20); 												// Spazio orizzontale tra le tabelle
	    grid.setVgap(20); 												// Spazio verticale tra le tabelle

	    // Posiziona le tabelle nella prima riga
	    grid.add(userTable, 0, 0);										// colonna 0, riga 0
	    grid.add(gameTable, 1, 0); 										// colonna 1, riga 0

	    
	    BorderPane.setAlignment(grid, Pos.CENTER);						// Centra la griglia nella stanza
	    
	    
	    // Aggiunta dell'etichetta ad una box solo per avere tutte le etichette di ogni stanza allineate
	    VBox centerContent = new VBox(30, grid, centerPane);
	    centerContent.setAlignment(Pos.CENTER);
	    root.setCenter(centerContent);
	}
		
	
	private void roomM() {
		primaryStage.setTitle("Sala Giochi");							// Titolo 
        String imagePath = getClass().getResource("./img/room.jpg").toExternalForm();
        BackgroundImage bgImage = new BackgroundImage(
                new Image(imagePath),
                BackgroundRepeat.NO_REPEAT,								// Serve per evitare la ripetizione dell'immagine in orizzontale
                BackgroundRepeat.NO_REPEAT,								// o in verticale
                BackgroundPosition.CENTER,
                new BackgroundSize(
                		1.0,
                		1.0,
                        true,											// Larghezza in percentuale = true
                        true, 											// Altezza in percentuale = true
                        false, 											// Adatta alla scena = false
                        false)											// Ritagliare l'immagine = false
        );
        
        root.setBackground(new Background(bgImage));					// Solo per l'immagine sfondo
        
        
        ToolBar toolbar = new ToolBar();								// ToolBar per effettuare il login nel sistema 
        toolbar.setPadding(new Insets(15));
        BorderPane.setMargin(toolbar, new Insets(10, 10, 0, 10)); 		// Margine superiore, destro e sinistro di 10px
        toolbar.setStyle("-fx-background-color: rgba(255,255,255,0.8); -fx-background-radius: 10;");

        String tmp = getClass().getResource("./img/tmp.png").toExternalForm();	
      
        ImageView imageView = new ImageView(new Image(tmp));			// Creazione di un ImageView per poter mostrare la foto
        imageView.setPickOnBounds(true); 								// Il click è percepito su tutta l'immagine al posto di un solo pixel
        imageView.setCursor(Cursor.HAND);								// Solo una piccola chicca :D
        imageView.setFitWidth(40);   
        imageView.setFitHeight(40);
        imageView.setPreserveRatio(true);
        
        Region spacer = new Region();			// Questo spacer in realtà non serve a niente, ma non esiste altro modo per mettere l'elemento nella toolBar tutto a sinistra...
        HBox.setHgrow(spacer, Priority.ALWAYS); // ...quindi uso uno spacer per "pushare" l'elemento a sinistra

        Label nickName = new Label("");									// Label del NickName
        nickName.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        nickName.textProperty().bind(currentNickName);
                
        toolbar.getItems().addAll(nickName, spacer, imageView);
        root.setTop(toolbar);											// Aggiungo alla root...
        root.getTop().setVisible(true);									// ...e la mostro; è importante perchè nelle altre stanze nascondo la ToolBar

        imageView.setOnMouseClicked(event -> {  						// Evento on-click
        	Stage popupStage = new Stage();								// Creazione di un nuovo Stage (finestra figlia)
        	
        	if(currentNickName.get().equals("")) {		// Se il NickName è vuoto significa che non ho fatto il login, quindi mostro i due form (registrazione e login)
                popupStage.setTitle("Register / Login");
                
                GridPane formGrid = new GridPane();						// Creazione di una griglia per mettere tutto in modo ordinato
                formGrid.setPadding(new Insets(20));
                formGrid.setHgap(50);
                formGrid.setVgap(50);
                formGrid.setAlignment(Pos.CENTER);
                
                VBox formLoginBox =  new VBox();		
                formLoginBox = loginBox(popupStage);			// Carico il form
                VBox formRegisterBox = new VBox();
                formRegisterBox = registerBox();				// Carico il form
                	
                formGrid.add(formRegisterBox, 0, 0); 					// cella (0,0)
                formGrid.add(formLoginBox, 1, 0);    					// cella (0,1)

                VBox centerContent = new VBox(10, formGrid);	
                centerContent.setAlignment(Pos.CENTER);
                
                Scene popupScene = new Scene(centerContent, roomH/2, roomW/2);	    // Scena del popup
                popupStage.setScene(popupScene);

        	}
        	else {														//	L'utente è loggato, devo mostrare un popup di "logout" o "cancella account"
        		popupStage.setTitle("Account");
        		
    	        Label title = new Label("Ciao " + currentNickName.get() + "!");
    	        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

    	        Button logOut = new Button("LogOut");
    	        logOut.setCursor(Cursor.HAND);
    	        logOut.setStyle("-fx-background-color: #ff9800; -fx-text-fill: white; -fx-font-weight: bold;");
    	        
    	        logOut.setOnAction(e -> {											// Evento OnClick
    	        	Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
    	            confirmAlert.setTitle("Logout");
    	            
    	            confirmAlert.setHeaderText("Sei sicuro di voler effettuare il logout?");
    	            confirmAlert.setContentText("Conferma per uscire.");

    	            Optional<ButtonType> result = confirmAlert.showAndWait();		// Mostra la finestra e aspetta la risposta
    	            if (result.isPresent() && result.get() == ButtonType.OK) {		// L'utente conferma il logout
    	            	currentNickName.set("");      								// Resetta il NickName
    	            	popupStage.close();
    	            } else {
    	                					// L'utente ha premuto Cancel o chiuso la finestra, non fare nulla
    	            }
    	        }); 						

    	        
    	        Button deleteAccount = new Button("Elimina Account");
    	        deleteAccount.setCursor(Cursor.HAND);
    	        deleteAccount.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-weight: bold;");
    	        
    	        deleteAccount.setOnAction(e -> {
    	            Alert confirmDelete = new Alert(Alert.AlertType.WARNING);
    	            confirmDelete.setTitle("Elimina Account");
    	           
    	            confirmDelete.setHeaderText("Sei sicuro di voler eliminare il tuo account?");
    	            confirmDelete.setContentText("Questa azione è IRREVERSIBILE!");
    	            
    	            confirmDelete.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
    	            Optional<ButtonType> result = confirmDelete.showAndWait();
    	            if (result.isPresent() && result.get() == ButtonType.OK) {		// L'utente conferma il logout
    	            	try (Connection conn = DataBaseConnection.getConnection();
   	                         PreparedStatement stmt = conn.prepareStatement("DELETE FROM utente WHERE nickname = ?")) {
   	                        stmt.setString(1, currentNickName.get());
   	                        stmt.executeUpdate();
   	                        currentNickName.set("");
   	                        popupStage.close();
   	                    } catch (SQLException ex) {
   	                        ex.printStackTrace();
   	                        Alert err = new Alert(Alert.AlertType.ERROR, "Errore durante l'eliminazione dell'account.");
   	                        err.showAndWait();
   	                    }
    	            } else {
    	                					// L'utente ha premuto Cancel o chiuso la finestra, non fare nulla
    	            }
    	        });

    	        VBox box = new VBox(15, title, logOut, deleteAccount);
    	        box.setAlignment(Pos.CENTER);
    	        box.setPadding(new Insets(20));

    	        Scene popupScene = new Scene(box, 300, 200);
    	        popupStage.setScene(popupScene);
        	}
        	
        	
            popupStage.initOwner(((Node) event.getSource()).getScene().getWindow());	// Imposto la finestra come "figlia" della principale
            popupStage.initModality(Modality.WINDOW_MODAL); 							// Blocca la finestra principale finché non il popup non viene chiuso
            popupStage.show();															// Mostra la finestra
            
        });
        

       
        // Giusto un po' di formattazzione grafica ---->

        VBox box = new VBox(10);   							// Box per il titolo principale
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.BOTTOM_CENTER);  
        box.setStyle("-fx-background-color: rgba(255,255,255,0.8); -fx-background-radius: 10;");
        
        
        GridPane grid = new GridPane();						// Griglia per formattazione, da valutare se metterci qualcosa dentro
        grid.setPadding(new Insets(20));
        grid.setHgap(50);
        grid.setVgap(50);
        grid.setAlignment(Pos.TOP_RIGHT);
        
        int extraPadding = 0;								// Solo per formattazione grafica
        
        if(!firstTime) {									// Se non è la prima volta che visualizzi la stanza centrale
    	    roomLabel.setText("SalaGiochi");
            box.setMaxWidth(300);		
            box.setMaxHeight(100);
    	    box.getChildren().add(roomLabel);
    	    extraPadding = 70;
   
        }else {												// Altrimenti è sicuramente la prima volta che visualizzi la stanza
        	Label label = new Label("Se è la tua prima volta, guardati un po' intorno!!");
        	label.setStyle("-fx-font-size: 20px;");
        	box.setMaxWidth(550);
            box.setMaxHeight(100);
        	box.getChildren().addAll(roomLabel, label);		// Aggiunta delle due etichette ad una Box, che ordina verticalmente i parametri
        	firstTime = false;									
        	
        }
        StackPane centerPane = new StackPane(box);			// Aggiunta della box dopo che è stata formattata in base a firstTime
        centerPane.setAlignment(Pos.BOTTOM_CENTER);				
        VBox centerContent = new VBox((400 + extraPadding), grid, centerPane);	// Questo serve solo per mettere in modo ordinato gli elementi (l'extra padding è solo per formattazione grafica)
        centerContent.setAlignment(Pos.CENTER);
        root.setCenter(centerContent);
        
  
        Button leftArrow = new Button("◄");					// Freccia sinistra
        leftArrow.setStyle("-fx-font-size: 40px; -fx-background-color: transparent;");
        leftArrow.setCursor(Cursor.HAND);
        leftArrow.setOnAction(e -> switchRoom(-1)); 		// Evento OnClick
        
        AnchorPane leftAnchor = new AnchorPane();
        StackPane leftPane = new StackPane(leftArrow);
        AnchorPane.setTopAnchor(leftPane,(double) (roomW/2.5));		// 20px dal bordo superiore
        AnchorPane.setLeftAnchor(leftPane, 20.0);  					// 20px dal bordo sinistro
        
        leftPane.setMaxHeight(50);
        leftPane.setAlignment(Pos.CENTER_LEFT);
        leftPane.setStyle("-fx-background-color: rgba(255,255,255,0.8); -fx-background-radius: 10;");
        leftAnchor.getChildren().add(leftPane);
        root.setLeft(leftAnchor);
        

        Button rightArrow = new Button("►");				// Freccia destra
        rightArrow.setStyle("-fx-font-size: 40px; -fx-background-color: transparent;");
        rightArrow.setCursor(Cursor.HAND);
        rightArrow.setOnAction(e -> switchRoom(1)); 		// Evento OnClick
        
        AnchorPane rightAnchor = new AnchorPane();
        StackPane rightPane = new StackPane(rightArrow);
        AnchorPane.setTopAnchor(rightPane,(double) (roomW/2.5));	// 20px dal bordo superiore
        AnchorPane.setRightAnchor(rightPane, 20.0);  				// 20px dal bordo sinistro
        
        rightPane.setMaxHeight(50);        
        rightPane.setAlignment(Pos.CENTER_RIGHT);
        rightPane.setStyle("-fx-background-color: rgba(255,255,255,0.8); -fx-background-radius: 10;");
        rightAnchor.getChildren().add(rightPane);			// L'anchor serve solo per centrare le frecce verticalmente
        root.setRight(rightAnchor);


        primaryStage.setScene(scene);						 // Scena
        primaryStage.show();
    }
	
	
	private void roomR() {
		root.getTop().setVisible(false);        

        StackPane centerPane = new StackPane(boxTitle());
        centerPane.setAlignment(Pos.BOTTOM_CENTER);  

	    GridPane grid = new GridPane();
	    grid.setHgap(5);
	    grid.setVgap(5);
	    grid.setAlignment(Pos.CENTER);

	    ColumnConstraints col = new ColumnConstraints();
	    col.setPercentWidth(50);
	    grid.getColumnConstraints().addAll(col, col);

	    RowConstraints row = new RowConstraints();
	    row.setPercentHeight(50);
	    grid.getRowConstraints().addAll(row, row);

	    // Creazione dei 4 riquadri cliccabili per i rispettivi giochi
	    grid.add(createImagePane("./img/tris.jpg", () -> {
	        new Tris("Tris", new Category("Arcade"))
	        .play(currentNickName.get());  
	    }), 0, 0);

	    grid.add(createImagePane("./img/BN1.jpg", () -> {
	        new Battleship("BattleShip", new Category("Strategia"))
	        .play(currentNickName.get());
	    }), 1, 0);

	    grid.add(createImagePane("./img/roulette.jpg", () -> {
	        new Roulette("Roulette", new Category("Fortuna"))
	        .play(currentNickName.get());
	    }), 0, 1);

	    grid.add(createImagePane("./img/dadi.jpg", () -> {
	        new DiceGuesser("DiceGuesser", new Category("Fortuna"))
	        .play(currentNickName.get());
	    }), 1, 1);

	    
	    VBox centerContent = new VBox(20, grid, centerPane);
	    centerContent.setAlignment(Pos.CENTER);
	    root.setCenter(centerContent);
	    
	}
	
	
	private VBox registerBox() {		
		VBox registerBox = new VBox(10);
        registerBox.setAlignment(Pos.CENTER);
        registerBox.setPadding(new Insets(20));
        registerBox.setStyle("-fx-background-color: rgba(255,255,255,0.85); -fx-background-radius: 10;");
        
        Label regTitle = new Label("Registrati");
        regTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
       
        TextField regNickname = new TextField();
        regNickname.setPromptText("Nickname");
        TextField regUser = new TextField();
        regUser.setPromptText("Nome");
        PasswordField regPass = new PasswordField();
        regPass.setPromptText("Password");
        PasswordField regPassConf = new PasswordField();
        regPassConf.setPromptText("Conferma Password");
        Button regButton = new Button("Registrati");
        regButton.setCursor(Cursor.HAND);
        regButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 8;");
        
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, null, ButtonType.OK);
        Alert errorAlert = new Alert(Alert.AlertType.ERROR, null, ButtonType.OK);
        Alert warningAlert = new Alert(Alert.AlertType.WARNING, null, ButtonType.OK);
        
        regButton.setOnAction(e -> {
            if (regUser.getText().isEmpty() || regNickname.getText().isEmpty() || regPass.getText().isEmpty() || regPassConf.getText().isEmpty()) {
            	warningAlert.setTitle("Credenziali mancanti"); 
            	warningAlert.setHeaderText("Compila tutti i campi!");
            	warningAlert.showAndWait();
            } else if (!regPass.getText().equals(regPassConf.getText())) {
                errorAlert.setTitle("Credenziali errate"); 
            	errorAlert.setHeaderText("Le password non coincidono!");
                errorAlert.showAndWait();
                regPass.clear();
                regPassConf.clear();
            } else {
            	try {
                    User newUser = new User(regNickname.getText(), regUser.getText(),  regPass.getText());                    
                    
                    String sql = "INSERT INTO utente(nickname, nome, psww , score) VALUES (?, ?, ?, ?)";
                    
                    try (Connection conn = DataBaseConnection.getConnection();
                            PreparedStatement stmt = conn.prepareStatement(sql)) {

	                    	stmt.setString(1, newUser.getNickname());
	                        stmt.setString(2, newUser.getName());
	                        stmt.setString(3, newUser.getPassword());
	                        stmt.setInt(4,0);
	                        
	                        
	                        try {
	                            stmt.executeUpdate(); 									// prova a inserire l'utente
	                            infoAlert.setTitle("Registrazione riuscita");
	                            infoAlert.setHeaderText("Benvenuto " + regUser.getText() + "!\nRicordati di fare il Login");
	                            infoAlert.showAndWait();
	                            
	                            regUser.clear();
	                            regNickname.clear();
	                            regPass.clear();
	                            regPassConf.clear();
	
	                        } catch (SQLException ex) {
	                            // Controlla se è violazione di UNIQUE o PRIMARY KEY
	                            if (ex.getErrorCode() == 23505 || ex.getMessage().contains("Unique") || ex.getMessage().contains("PRIMARY KEY")) {
	                                errorAlert.setTitle("Errore registrazione");
	                                errorAlert.setHeaderText("Nickname già esistente!");
	                                errorAlert.showAndWait();
	
	                                regUser.clear();
	                                regNickname.clear();
	                                regPass.clear();
	                                regPassConf.clear();
	                                
	                            } else {
	                                ex.printStackTrace();
	                                errorAlert.setTitle("Errore DB");
	                                errorAlert.setHeaderText("Impossibile registrare l'utente!");
	                                errorAlert.showAndWait();
	                            }
	                        }
                           
                      } catch (SQLException e1) {
                    	  e1.printStackTrace();
                      }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    errorAlert.setTitle("Errore DB");
                    errorAlert.setHeaderText("Impossibile registrare l'utente!");
                    errorAlert.showAndWait();
                    regUser.clear();
                    regPass.clear();
                    regPassConf.clear();
                }
                
            }
        });
        
        registerBox.getChildren().addAll(regTitle, regUser, regNickname, regPass, regPassConf, regButton);

		return registerBox;
	}

	
	private VBox loginBox(Stage popupStage) {		
		VBox loginBox = new VBox(10);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(20));
        loginBox.setStyle("-fx-background-color: rgba(255,255,255,0.85); -fx-background-radius: 10;");
        
        Label logTitle = new Label("Accedi");
        logTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        TextField logUser = new TextField();
        logUser.setPromptText("Nickname");
        PasswordField logPass = new PasswordField();
        logPass.setPromptText("Password");
        Button logButton = new Button("Login");
        logButton.setCursor(Cursor.HAND);
        logButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-background-radius: 8;");
        
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, null, ButtonType.OK);
        Alert errorAlert = new Alert(Alert.AlertType.ERROR, null, ButtonType.OK);
        Alert warningAlert = new Alert(Alert.AlertType.WARNING, null, ButtonType.OK);
        
        logButton.setOnAction(e -> {
            if (logUser.getText().isEmpty() || logPass.getText().isEmpty()) {
            	warningAlert.setTitle("Credenziali mancanti"); 
            	warningAlert.setHeaderText("Inserisci username e password!");
                warningAlert.showAndWait();
            } else {
            	try {
                                         
                     String sql = "SELECT nickname FROM utente WHERE nickname = ? AND psww = ?";
                     
                     try (Connection conn = DataBaseConnection.getConnection();
	                         PreparedStatement stmt = conn.prepareStatement(sql)) {
	                	 	 stmt.setString(1, logUser.getText());						// Sostituisce il primo ? con il NickName del form
	                	 	 stmt.setString(2, logPass.getText());
	                    	 ResultSet rs = stmt.executeQuery(); 						// Esegue la query e salva il risultato
	                    	 if (rs.next()) {											// NickName trovato
	                    		 currentNickName.set(logUser.getText());
	                    		
	                             infoAlert.setTitle("Login riuscito");
	                             infoAlert.setHeaderText("Bentornato " + logUser.getText() + "!");
	                             infoAlert.showAndWait();
	                             logUser.clear();
	                             logPass.clear();
	                             popupStage.close();
	                         } else { 													// Nessun utente trovato o password errata (sono due casi distinti)
	                        	 String sql1 = "SELECT nickname FROM utente WHERE nickname = ? ";
	                        	 
	                        	 try (PreparedStatement stmt1 = conn.prepareStatement(sql1)) {
	                        		 stmt1.setString(1, logUser.getText());
	                        		 ResultSet rs1 = stmt1.executeQuery(); 				// Cerco solo il NickName
	                        		 if (rs1.next()) {									// Se trovo un risultatp significa che aveva sbagliato password prima
	                        			 errorAlert.setTitle("Password Errata");
	                                     errorAlert.setHeaderText("Le password non corrispondono!");
	                                     errorAlert.showAndWait();
	                                     logPass.clear();
	                        		 }
	                        		 else {												// Se no l'utente non esiste
	                        			 errorAlert.setTitle("Utente non trovato");
	                                     errorAlert.setHeaderText("Nickname non esistente!");
	                                     errorAlert.showAndWait();
	                                     logUser.clear();
	                                     logPass.clear();
	                        		 }
	                        	 }catch (SQLException e1) {
			                       	  e1.printStackTrace();
			                     }
	                         }    
                      } catch (SQLException e2) {
                    	  e2.printStackTrace();
                      }

                 } catch (Exception ex) {
                     ex.printStackTrace();
                     errorAlert.setTitle("Errore DB");
                     errorAlert.setHeaderText("Errore durante la connessione al database.");
                     errorAlert.showAndWait();
                     logUser.clear();
                	 logPass.clear();
                 }
              }
        });
        loginBox.getChildren().addAll(logTitle, logUser, logPass, logButton);
        return loginBox;
	}

	
	private VBox boxTitle() {	
		VBox box = new VBox(10);   					// Box per contenere la roomLabel
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.BOTTOM_CENTER);  
        box.setMaxWidth(300);
        box.setMaxHeight(100);
        box.setStyle("-fx-background-color: rgba(255,255,255,0.8); -fx-background-radius: 10;");
        box.getChildren().add(roomLabel);
		return box;
	}

	
	private StackPane createImagePane(String imagePath, Runnable onClick) {			// per i 4 quadranti delle immagini dei giochi
	    Image img = new Image(getClass().getResourceAsStream(imagePath));
	    ImageView imgView = new ImageView(img);
	    imgView.setCursor(Cursor.HAND);
	    imgView.setFitWidth(200);
	    imgView.setFitHeight(200);
	    imgView.setPreserveRatio(true);
	    imgView.setOnMouseClicked(e -> onClick.run());

	    StackPane pane = new StackPane(imgView);
	    pane.setStyle("-fx-border-color: black; -fx-padding: 10;");
	    return pane;
	}
	
	
	@SuppressWarnings("unchecked")
	private void addGameColumns(TableView<VideoGames> gameTable) {
		TableColumn<VideoGames, String> name = new TableColumn<>("Gioco");
		name.setCellValueFactory(data ->
        new SimpleStringProperty(data.getValue().getName()));
		
		TableColumn<VideoGames, Integer> score = new TableColumn<>("Punteggio");
		score.setCellValueFactory(data ->
		new SimpleIntegerProperty(data.getValue().getScore()).asObject());
		
		TableColumn<VideoGames, Category> category = new TableColumn<>("Categoria");
		category.setCellValueFactory(data ->
		new SimpleObjectProperty<>(data.getValue().getCategory()));
		
		TableColumn<VideoGames, String> desc = new TableColumn<>("Descrizione");
	    desc.setCellValueFactory(data ->
	    new SimpleStringProperty(data.getValue().getCategory().getDescription()));    
	    
		gameTable.getColumns().addAll(name,category, desc, score);
	}	
	
	
	@SuppressWarnings("unchecked")
	private void addUserColumns(TableView<User> table) {

	    TableColumn<User, String> colNick = new TableColumn<>("NickName");
	    colNick.setCellValueFactory(data ->
	        new SimpleStringProperty(data.getValue().getNickname())
	    );

	    TableColumn<User, String> colName = new TableColumn<>("Nome");
	    colName.setCellValueFactory(data ->
	        new SimpleStringProperty(data.getValue().getName())
	    );
	    
	    TableColumn<User, String> colPassword = new TableColumn<>("psww");
	    colPassword.setCellValueFactory(data ->
	    	new SimpleStringProperty(data.getValue().getPassword())
	    );

	    TableColumn<User, Integer> colScore = new TableColumn<>("Score");
	    colScore.setCellValueFactory(data ->
	        new SimpleIntegerProperty(data.getValue().getScore()).asObject()
	    );
	    
	    table.getColumns().addAll(colNick, colName, colPassword, colScore);
	}
	

}
