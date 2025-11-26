package com.mondsciomegn.salagiochi.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;


public abstract class VideoGames {
	private final int id = 0;
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
	
	
	public abstract void play(String nickName);
	
	
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
//	    double timerHeight = root.getHeight();

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
    	if(nickname.isEmpty()) {												// Gioco come anonimo
	    	String sql  = "UPDATE utente SET score = ? WHERE nickname = ?";
	    	try (Connection conn = DataBaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

	            	stmt.setInt(1, getScore());    								 // Assegna il punteggio
	            	if(nickname.isEmpty())
	            		stmt.setString(2,"_ANONIMO_");
                    stmt.executeUpdate(); 										// Prova a fare l'update
                   
              } catch (SQLException e1) {
            	  e1.printStackTrace();
              }
    	}
    	else {																	// Il NickName è valido
    		String sql  = "UPDATE utente SET score = score + ? WHERE nickname = ?";
	    	try (Connection conn = DataBaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

	            	stmt.setInt(1, getScore());    								// Assegna il punteggio
	            	stmt.setString(2,nickname);
                    stmt.executeUpdate(); 										// Prova a fare l'update
                   
              } catch (SQLException e1) {
            	  e1.printStackTrace();
              }
    	}
		
	}
	

    protected void showMessage(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Risultato");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
	
	
	public int getId() {
		return id;
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
