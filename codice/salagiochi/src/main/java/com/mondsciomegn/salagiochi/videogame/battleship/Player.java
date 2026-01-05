package com.mondsciomegn.salagiochi.videogame.battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.control.Alert;

public class Player {
	
    private final Field field;
    private final List<Ship> fleet;
    private final int[] totalShips;
    private final boolean isComputer;
	private Random rand = new Random();
	private Player opponent = null;

    public Player(int gridSize, int[] totalShips, boolean isComputer) {
		this.totalShips = totalShips;
		this.isComputer = isComputer;
		
        this.field = new Field(gridSize);
        this.fleet = new ArrayList<>();
    }
    
    public void setUpShips() {
        char shipID = 'A';

        for (int i = 0; i < totalShips.length; i++) 
            for (int j = 0; j < totalShips[i]; j++) {
                fleet.add(new Ship(shipID, 2 + i));
                shipID++;
            }
    }     

	public boolean checkForAllBoatDown() {
		for (Ship ship : fleet) 
			if(!ship.isPlaced())
				return false;
		return true;
	}

	public void enterBattlePhase() {
		for (int i = 1; i < field.getSize(); i++)
	        for (int j = 1; j < field.getSize(); j++)
	        	if(field.getGrid().getButtonGrid()[i][j].isDisable())
	        		field.getGrid().getButtonGrid()[i][j].setDisable(false);
	        	else
	        		field.getGrid().getButtonGrid()[i][j].setDisable(true);
	}

	public void putAllBoatDown() {
	    	   
	    for (int i = 0; i < field.getSize(); i++)						// Inizializzo la matrice di supporto per evitare valori null
	        for (int j = 0; j < field.getSize(); j++)
	            field.getSupportGrid()[i][j] = '-';
	  
	    
	    for (Ship ship : fleet) {		         
            while (!ship.isPlaced()) {
                int row = rand.nextInt(field.getSize() - 1) + 1;  		// 1..8
                int col = rand.nextInt(field.getSize() - 1) + 1; 			// 1..8
                boolean horizontal = rand.nextBoolean();

                if (canPlaceComputerShip(row, col, ship.getSize(), horizontal)) {
                    for (int i = 0; i < ship.getSize(); i++) {
                        int r = row + (horizontal ? 0 : i);
                        int c = col + (horizontal ? i : 0);
                        field.getSupportGrid()[r][c] = ship.getId();
                    }
                    ship.setIsPlaced(true);
                }
            }
	        
	    }

	    
	    for (int i = 1; i < field.getSize(); i++)								// Nascondo la visualizzazione all'utente
	        for (int j = 1; j < field.getSize(); j++)
	            field.getGrid().getButtonGrid()[i][j].setText("");
	}
	
	/**
	 * Gestisce la logica di posizionamento delle navi del computer 
	 * 
	 * @param row numero, casuale, della riga dove il computer vuole posizionare la nave
	 * @param col numero, casuale, della colonna dove il computer vuole posizionare la nave
	 * @param size lunghezza della nave
	 * @param horizontal 	true se il computer vuole posizionare la nave in orizzontale
	 * 						false se la vuole posizionare in verticale
	 * 
	 * @return 	true se la nave è stata posizionata correttamente
	 * 			false se la nave non può essere posizionata nella posizione richiesta
	 */
	private boolean canPlaceComputerShip(int row, int col, int size, boolean horizontal) {
	    if (horizontal) {
	        if (col + size > field.getSize()) // Uscirebbe dai bordi
	        	return false; 
	        
	        for (int i = 0; i < size; i++) 
	            if (field.getSupportGrid()[row][col + i] != null && field.getSupportGrid()[row][col + i] != '-') 	// C'è già una nave in questa cella
	                return false;     
	    } else {
	        if (row + size > field.getSize()) // Stessa logica ma per la verticalità
	        	return false; 
	        
	        for (int i = 0; i < size; i++) 
	            if (field.getSupportGrid()[row + i][col] != null && field.getSupportGrid()[row + i][col] != '-') 
	                return false;    
	    }
	    return true;
	}

	public boolean attack(int row, int col, boolean tutorialFlag, Player opponent) {
		this.opponent = opponent;
	    if (isComputer) {
	        return computerAttack(tutorialFlag);
	    } else {
	        return playerAttack(row, col, tutorialFlag);
	    }
	}

	
	private boolean computerAttack(boolean tutorialFlag) {
	    int row, col;

	    while (true) {
	    	row = rand.nextInt(field.getSize() - 1) + 1;
	        col = rand.nextInt(field.getSize() - 1) + 1;

	        char cell = opponent.getField().getSupportGrid()[row][col];

	        if (cell == 'm' || cell == 'r')		// Il computer non fa mosse stupide 
	            continue;
	        
	        if(cell == '-') {
	        	opponent.getField().getGrid().getButtonGrid()[row][col].setStyle("-fx-background-color: rgba(0,0,255,0.3); -fx-font-size: 20px;"); // Coloro la casella di blue 
	        	opponent.getField().getSupportGrid()[row][col] = 'm';
	        	return false;
	        }
	        else {	
		        opponent.getField().getSupportGrid()[row][col] = 'r';
			    opponent.getField().getGrid().getButtonGrid()[row][col].setStyle("-fx-background-color: rgba(255,0,0,0.3); -fx-font-size: 20px;");
	
			    if (tutorialFlag)
			        showMessage("COLPITO!!!!", "Il computer ti ha colpito!!!", null);
			    return true;
	        }
	    }
	}

	private boolean playerAttack(int row, int col, boolean tutorialFlag) {
	    char cell = opponent.getField().getSupportGrid()[row][col];

	    if (cell != '-') {
	        if (!opponent.getField().getGrid().getButtonGrid()[row][col].getText().equals("X")) {
	        	char id = opponent.getField().getSupportGrid()[row][col]; // Memorizzo l'id di cosa ho colpito 
	        	opponent.getField().getGrid().getButtonGrid()[row][col].setText("X"); // Segno che l'ho compito 
	        	opponent.getShipById(id).addHit(); 
	        	if (opponent.getShipById(id).isSunk()) // Se la nave è affondata (i colpi subiti dalla nave == dimensione) 
	        		sunkenShips(id, opponent); // La coloro tutta di rosso 
	        	else // Altrimenti l'ho colpita ma non affondata 
	        		opponent.getField().getGrid().getButtonGrid()[row][col].setStyle("-fx-background-color: rgba(255,165,0,0.3); -fx-font-size: 20px;"); // Allora coloro la casella di arancio
	        }
	        return true;
	    } else {
	    	if(tutorialFlag) 
	    		showMessage("MANCATO!!!!", "Hai trovato un buco nell'acqua xD", "ora il computer ti sparerà"); 
	    	opponent.getField().getGrid().getButtonGrid()[row][col].setStyle("-fx-background-color: rgba(0,0,255,0.3); -fx-font-size: 20px;"); // Coloro la casella di blue 
	    }
	    return false;
	}



	private void sunkenShips(char id, Player opponent) {
		for (int i = 1; i < field.getSize(); i++) 
	        for (int j = 1; j < field.getSize(); j++) 
	            if (opponent.getField().getSupportGrid()[i][j] == id)
	            	opponent.getField().getGrid().getButtonGrid()[i][j].setStyle("-fx-background-color: rgba(255,0,0,0.3); -fx-font-size: 20px;");
	}

    // Getters
    public Field getField() { return field; }
    public List<Ship> getFleet() { return fleet; }
    public boolean isComputer() { return isComputer; }
    public int getTotalNumberOfShips() {return fleet.size();}
    
    public int getNumberOfShips(int length) {
        int count = 0;
        for (Ship ship : fleet) 
            if (ship.getSize() == length) 
                count++;
        
        return count;
    }
    
    public Ship getShip(int length) {
    	for (Ship ship : fleet) 
            if (ship.getSize() == length && !ship.isPlaced())
            		return ship;
        return null;        
    }
    
    public Ship getShipById(char id) {
        for (Ship ship : fleet) {
            if (ship.getId() == id) {
                return ship;
            }
        }
        return null; // oppure Optional
    }

    

    public int[] getTotalShips() {
    	return this.totalShips;
    }

	
	public void showMessage(String title, String header, String content) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
	}
	
}