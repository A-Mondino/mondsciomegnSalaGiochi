package com.mondsciomegn.salagiochi.videogame.battleship;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class GuiGrid {
	private final int size;
	private Button[][] buttonGrid;
    private GridPane gridPane;
   
    private static final int MAXH = 700;
    private static final int MAXW = 700;
    private static final int DIV = 13;
    
    public GuiGrid(int size) {
    	this.size = size;
    	this.buttonGrid = new Button[size][size];
    	this.gridPane = new GridPane();
    }
    
    public void setUpGrid() {
    	for (int i = 0; i < size; i++)        
            for (int j = 0; j < size; j++) {

            	buttonGrid[i][j] = new Button();
            	buttonGrid[i][j].setMinSize(MAXH / DIV, MAXW / DIV);
            	buttonGrid[i][j].setStyle("-fx-font-size: 20px;");
            	gridPane.add(buttonGrid[i][j], j, i);
                

                if (i == 0 && j == 0) {							 // Cella (0,0): vuota e disattivata
                	buttonGrid[i][j].setText("");
                	buttonGrid[i][j].setDisable(true);
                }else 
                	if (i == 0 && j > 0) {						 // Prima riga (intestazioni colonne): A–H
	                    char colLabel = (char) ('A' + (j - 1));  // j=1 -> A, j=8 -> H
	                    buttonGrid[i][j].setText(String.valueOf(colLabel));
	                    buttonGrid[i][j].setDisable(true);
                	}
                	else if (j == 0 && i > 0) {					 // Prima colonna (intestazioni righe): 1–8
		                    String rowLabel = String.valueOf(i); // i=1 -> "1", i=8 -> "8"
		                    buttonGrid[i][j].setText(rowLabel);
		                    buttonGrid[i][j].setDisable(true);
                		}else 								  // Celle interne (1..8, 1..8): giocabili, no testo iniziale
                			buttonGrid[i][j].setText(""); 
            }
        
   }
    
    public void disableButton() {
    	for (int i = 0; i < size; i++) 							// Disattivo i bottoni del computer per impedire all'utente che prema li prima del dovuto
         	for (int j = 0; j < size; j++) 
         		buttonGrid[i][j].setDisable(true);
    }
    
    
	public Button[][] getButtonGrid() {
		return buttonGrid;
	}
	

	public GridPane getGridGUI() {
		return gridPane;
	}
}
