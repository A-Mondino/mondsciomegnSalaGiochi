package com.mondsciomegn.salagiochi.videogame.roulette;

import java.util.Map;

public class RowRule implements GameRules {
    private final int divisor;
    private final int expectedRemainder;
    private final int multiplier;

    public RowRule(int divisor, int expectedRemainder, int multiplier) {
        this.divisor = divisor;
        this.expectedRemainder = expectedRemainder;
        this.multiplier = multiplier;
    }

    @Override
    public boolean isValid(int number, Map<Integer, String> colorMap) {
        if (number == 0) 
        	return false;
        
        return number % divisor == expectedRemainder;
    }

    @Override
    public int getMultiplier() {
        return multiplier;
    }
}
