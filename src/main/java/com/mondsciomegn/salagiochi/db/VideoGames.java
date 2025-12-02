package com.mondsciomegn.salagiochi.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import java.sql.Timestamp;
import java.util.Date;


public class VideoGames {
	private String name;
	private Category category;
	private int score = 0;
	private int time = 0;
	private String nickname = null;
	private Stage timerStage = new Stage();
	
	public VideoGames(String name, Category category) {
		this.name = name;
		this.category = category;
		
		String sql  = "SELECT score FROM videogioco WHERE nome = ?";
    	
		try (Connection conn = DataBaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            	stmt.setString(1, name);    
            	try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        this.score = rs.getInt("score");
                    } else 
                        this.score = 0;   
                }
               
          } catch (SQLException e1) {
        	  e1.printStackTrace();
          }
	}
	
	
	public void play(String nickName) {}
	
	
	protected void startTimer(Stage mainStage) {
		time = 0;
	    Label label = new Label("Secondi: " + time);
	    label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
	    label.setMinSize(120, 30);

	    Timeline timeline = new Timeline(				// Creo un oggetto timeline che ogni secondo aggiorna l'etichetta
	        new KeyFrame(Duration.seconds(1), event -> {
	            time++;
	            label.setText("Secondi: " + time);
	        })
	    );
	    timeline.setCycleCount(Timeline.INDEFINITE);	// Lo metto in ripetizione all'infinito
	    timeline.play();								// E lo faccio partire

	    VBox root = new VBox(label);					// Un po di formattazione...
	    root.setStyle(
	        "-fx-background-color: #9C27B0;" +
	        "-fx-background-radius: 20px;" +
	        "-fx-padding: 20px;"
	    );
	    root.setAlignment(Pos.CENTER);
	    timerStage.initStyle(StageStyle.TRANSPARENT);	//Rendo lo sfondo dello stage trasparente 
	    
	    Scene scene = new Scene(root);
	    scene.setFill(null);
	    timerStage.setScene(scene);						// Ci aggiungo il contenitore del timer
	    timerStage.setResizable(false);					// Così il timer non è modificabile

	    												// Poi posiziono il tutto in alto a destra dello schermo
	    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();	// x il calcolo della dim dello schermo
	    timerStage.show(); 
	    
	    root.applyCss();								// Queste due forzano l'applicazione degli stili CSS
	    root.layout();									// E dei layout della root 

	    double timerWidth = root.getWidth();			// Senza quelle due funzioni questi due calcoli vengono sbagliati

	    double timerX = screenBounds.getMaxX() - timerWidth - 20; // 10px margine dal bordo
	    double timerY = screenBounds.getMinY() + 20;              // 10px dal top

	    timerStage.setX(timerX);
	    timerStage.setY(timerY);
	}

	
	protected int stopTimer() {
		timerStage.close();
		return time;
	}
	
	
	public void addtime(int x) {
		this.time += x;
	}
	
	
	protected void addPoints(String nickname) {
		boolean isAnonimous = (nickname == null) || nickname.isEmpty();		// Controllo se sta giocando in anonimo
		
		String finalNickname = isAnonimous ? "_ANONIMO_" : nickname;		
		String sql  = isAnonimous 											// In base all'anonimo setto il nickname della query
				? "UPDATE utente SET score = ? WHERE nickname = ?"
				: "UPDATE utente SET score = score + ? WHERE nickname = ?"; 

	    try (Connection conn = DataBaseConnection.getConnection();
	                PreparedStatement stmt = conn.prepareStatement(sql)) {
        	stmt.setInt(1, getScore());    								 	// Assegna il punteggio
    		stmt.setString(2,finalNickname);
            stmt.executeUpdate(); 											// Prova a fare l'update
	    } catch (SQLException e) {
        	e.printStackTrace();
        }		
	}
	
	
	protected void registerGame(String nickname, int gameScore) {
		// Controllo se il nickname è nullo o vuoto
	    String finalNickname = (nickname == null || nickname.isEmpty()) ? "_ANONIMO_" : nickname;

	    String sql = "INSERT INTO activityLog (nickname, videogioco, data_partita, score) VALUES (?, ?, ?, ?)";
	    
	    try (Connection conn = DataBaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, finalNickname);                  // nickname del giocatore
	        stmt.setString(2, this.name);                      // nome del videogioco
	        stmt.setTimestamp(3, new Timestamp(new Date().getTime()));  // data corrente
	        stmt.setInt(4, gameScore);                        // punteggio del videogioco

	        stmt.executeUpdate();                               // eseguo l'inserimento
	        //System.out.println("Gioco registrato correttamente!");

	    } catch (SQLException e) {
	        e.printStackTrace();
	        showMessage("Errore durante la registrazione della partita!");
	    }
	}
	

    protected void showMessage(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Risultato");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
	
	
	public int getScore() {
		return score;
	}
	
	
	public void setScore(int score) {
		this.score = score;
	}
	
	
	public String getName() {
		return name;
	}

	
	public void setName(String name) {
		this.name = name;
	}

	
	public Category getCategory() {
		return category;
	}

	
	public void setCategory(Category category) {
		this.category = category;
	}

	
	public String getNickname() {
		return nickname;
	}

	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
}
