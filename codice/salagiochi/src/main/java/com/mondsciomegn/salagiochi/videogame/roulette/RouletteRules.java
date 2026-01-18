package com.mondsciomegn.salagiochi.videogame.roulette;

import java.util.HashMap;
import java.util.Map;

public class RouletteRules {

    private final Map<Integer, String> colors = new HashMap<>();
    // Qui usiamo la lista (Mappa) di un "altro tipo" come chiedevi: l'interfaccia GameRule
    private final Map<String, GameRules> rulesMap = new HashMap<>();

    public RouletteRules() {
        initializeColors();
        initializeRules();
    }

    private void initializeColors() {
        int[] redNumbers = {1,3,5,7,9,12,14,16,18,19,21,23,25,27,30,32,34,36};
        int[] blackNumbers = {2,4,6,8,10,11,13,15,17,20,22,24,26,28,29,31,33,35};
        for (int r : redNumbers) colors.put(r, "red");
        for (int b : blackNumbers) colors.put(b, "black");
    }

    private void initializeRules() {
        // --- Regole Righe (Resto della divisione per 3) ---
        // Riga 1: 1, 4, 7... (diviso 3 danno resto 1)
        rulesMap.put("Punta sull'intera riga 1", new RemainderRule(3, 1, 3));	// Triplica la puntata
        // Riga 2: 2, 5, 8... (diviso 3 danno resto 2)
        rulesMap.put("Punta sull'intera riga 2", new RemainderRule(3, 2, 3));
        // Riga 3: 3, 6, 9... (divisibili per 3, resto 0)
        rulesMap.put("Punta sull'intera riga 3", new RemainderRule(3, 0, 3));

        // --- Regole Dozzine (Intervalli) ---
        rulesMap.put("Punta sui primi 12 numeri", new RangeRule(1, 12, 3));		// Triplica la puntata	
        rulesMap.put("Punta sui numeri da 13 a 24", new RangeRule(13, 24, 3));
        rulesMap.put("Punta sui numeri da 25 a 36", new RangeRule(25, 36, 3));

        // --- Regole Semplici ---
        rulesMap.put("Punta 1-18", new RangeRule(1, 18, 2));					// Raddoppia la puntata
        rulesMap.put("Punta 19-36", new RangeRule(19, 36, 2));

        // Pari (diviso 2 resto 0) e Dispari (diviso 2 resto 1)
        rulesMap.put("Pari", new RemainderRule(2, 0, 2)); 						// Raddoppia la puntata
        rulesMap.put("Dispari", new RemainderRule(2, 1, 2));

        // --- Colori ---	
        rulesMap.put("Rosso", new ColorRule("red", 2));							// Raddoppia la puntata
        rulesMap.put("Nero", new ColorRule("black", 2));	
    }

    public String getColorForNumber(int number) {
        return colors.getOrDefault(number, "green");
    }


   
    public int checkWin(int extractedNum, String decision) {
        
        // 1. Caso speciale: l'utente ha scritto un numero esplicito
        if (isNumeric(decision)) {
            int choiceNum = Integer.parseInt(decision);
            return (extractedNum == choiceNum) ? 36 : -1;
        }

        // 2. Recuperiamo la regola dalla mappa
        GameRules rule = rulesMap.get(decision);

        // 3. Se la regola esiste e il numero estratto la soddisfa, ritorniamo il moltiplicatore
        if (rule != null && rule.isValid(extractedNum, colors)) {
            return rule.getMultiplier();
        }

        return -1;
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}