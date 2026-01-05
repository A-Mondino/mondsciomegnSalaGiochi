package com.mondsciomegn.salagiochi.videogame.battleship;

public class Field {
    private final int size;
    private final GuiGrid grid;
    private final Character[][] supportGrid;
    
    private static final char EMPTY = '-';
    private static final char VALID = 'k';
    private static final char INVALID = 'x';
    private static final char HEADER = '?';
    private static final char CORNER = '/';
    
    // Definiamo le 4 direzioni: {direction x row, direction x col} 
    private static final int[][] DIRECTIONS = {
			    {-1, 0}, // UP
			    {1, 0},  // DOWN
			    {0, -1}, // LEFT
			    {0, 1}   // RIGHT
    };
    
    
    public Field(int size) {
        this.size = size;
        this.grid = new GuiGrid(size);
        this.supportGrid = new Character[size][size];
        grid.setUpGrid();
        setUpSupportMatrix();
        
    }
    
    public boolean canPlaceShip(int row, int col, int shipLen) {
        if (supportGrid[row][col] != EMPTY) return false;
        if (shipLen == 1) return true;

        for (int[] d : DIRECTIONS) 
            if (isDirectionValid(row, col, d[0], d[1], shipLen)) 
                return true;
            
        return false;
    }

    
    private boolean isDirectionValid(int row, int col, int dr, int dc, int len) {
        int rEnd = row + dr * (len - 1);		// Se la (row + len-1) (perchè il primo pezzo già messo) nella direzione...
        int cEnd = col + dc * (len - 1);		// Stessa cosa x le colonne

        if (!isInsideGrid(rEnd, cEnd)) 			//...non è all'interno della griglia
        	return false;	//non è una direzione valida

        for (int i = 1; i < len; i++) 			// non è valida nemmeno se, proseguendo lungo la direzione, trovo una cella con dentro qualcosa
            if (supportGrid[row + dr * i][col + dc * i] != EMPTY) 
                return false;
            
        
        return true;
    }

    
    public void highlightValidDirections(int row, int col, int distance) {  

	    for (int[] d : DIRECTIONS) {		 // Verifichiamo se la direzione è valida
	        boolean valid = checkAndMarkPath(row, col, d[0], d[1], distance);
	        colorDirection(row, col, d[0], d[1], distance, valid);
	    }
	  
	    // Coloriamo la cella di partenza
	    grid.getButtonGrid()[row][col].setStyle("-fx-background-color: rgba(0,255,0,0.3); -fx-font-size: 20px;");
	}
    
    
    private boolean checkAndMarkPath(int row, int col, int dr, int dc, int len) {
        boolean valid = isDirectionValid(row, col, dr, dc, len);
        char marker = valid ? VALID : INVALID;

        for (int i = 1; i < len; i++) {
            int r = row + dr * i;
            int c = col + dc * i;
            if (isInsideGrid(r,c) && supportGrid[r][c] == EMPTY) 
                supportGrid[r][c] = marker;
        }
        return valid;
    }



    public boolean placeShip(int startRow, int startCol, int row, int col, Ship ship) {
        if (supportGrid[row][col] != VALID) return false;

        for (int[] d : DIRECTIONS) {
            int dr = d[0];
            int dc = d[1];

            int r = startRow;
            int c = startCol;

            for (int i = 1; i < ship.getSize(); i++) {
                r += dr;		// Seguo la direzione sulle righe
                c += dc;		// E sulle colonne

                if (r == row && c == col) {		// Se trovo quelle dove ho premuto significa che ho trovato la direzione 
                    for (int j = 1; j < ship.getSize(); j++) {	// Allora completo l'inserimento partendo dall'inizio
                    	startRow += dr;
                    	startCol += dc;
                        putPieceDown(startRow, startCol, ship);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    
    

	public void putPieceDown(int row, int col, Ship currentShip) {
		grid.getButtonGrid()[row][col].setText(String.valueOf(currentShip.getSize()));
		grid.getButtonGrid()[row][col].setStyle("-fx-background-color: lightgreen; -fx-font-size: 20px;");
		supportGrid[row][col] = currentShip.getId();
	}
	  
    
    
    
    private void setUpSupportMatrix() {
    	 for (int i = 0; i < size; i++) 
             for (int j = 0; j < size; j++) 
                 if (i == 0 && j == 0) 
                     supportGrid[i][j] = CORNER;
                 else if (i == 0 || j == 0) 
                     supportGrid[i][j] = HEADER;
                 else 
                     supportGrid[i][j] = EMPTY;
    }

    

	public Character[][] getSupportGrid() {
		return supportGrid;
	}

	/**
	 * Colora lo sfondo delle celle di verde se in quella direzione è possibile piazzare la barca altrimenti se non è possibili piazzarla (es: non c'è abbastanza spazio)
	 * colora lo sfondo delle celle in quella direzione di rosse. Il numero di celle colorate dipende dalla lunghezza della nave.
	 * 
	 * @param row numero della riga della cella dove si vuole partire per posizionare la nave
	 * @param col numero della colonna della cella dove si vuole partire per posizionare la nave
	 * @param dr incremento riga per direzione
	 * @param dc incremento colonna per direzione
	 * @param len lunghezza della nave
	 * @param valid true se la direzione in cui si vuole posizionare la nave è valida 
	 * 				false altrimenti
	 */
	private void colorDirection(int row, int col, int dr, int dc, int len, boolean valid) {
	    String color = valid ? "rgba(0,255,0,0.3)" : "rgba(255,0,0,0.3)";			// In base al flag di validità coloro di verde altrimenti rosso
	    
	    for (int i = 1; i < len; i++) {
	        int r = row + dr * i;
	        int c = col + dc * i;
	        if (r < 0 || r >= size || c < 0 || c >= size) 			// Appena esco dala griglia mi fermo 
	        	break;

	        grid.getButtonGrid()[r][c].setStyle(
	            String.format("-fx-background-color: %s; -fx-font-size: 20px;", color)
	        );
	    }
	    grid.getButtonGrid()[row][col].setStyle("-fx-background-color: rgba(0,255,0,0.3); -fx-font-size: 20px;");	// questa è per la casella appena selezionata
	}



	public void resetGrid() {
	    for (int i = 0; i < size; i++) 
	        for (int j = 0; j < size; j++) {
	            
	            if (supportGrid[i][j] == INVALID || supportGrid[i][j] == VALID) 
	                supportGrid[i][j] = EMPTY;
	            
	            if(supportGrid[i][j] != EMPTY && supportGrid[i][j] != HEADER && supportGrid[i][j] != CORNER)
	            	grid.getButtonGrid()[i][j].setStyle("-fx-background-color: rgba(0,255,0,0.3); -fx-font-size: 20px;");
	            else
	            	grid.getButtonGrid()[i][j].setStyle(" -fx-font-size: 20px;");
	        }
	    
	}

	public GuiGrid getGrid() {
		return grid;
	}
	
	private boolean isInsideGrid(int r, int c) {
	    return (r >= 1 && r < size && c >= 1 && c < size);
	}
	
	public int getSize() {
		return size;
	}
		
}