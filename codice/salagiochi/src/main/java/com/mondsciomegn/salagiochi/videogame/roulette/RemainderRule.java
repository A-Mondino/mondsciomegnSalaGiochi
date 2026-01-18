package com.mondsciomegn.salagiochi.videogame.roulette;

import java.util.Map;

public class RemainderRule implements GameRules {
    private final int divisor;
    private final int expectedRemainder;
    private final int multiplier;

    public RemainderRule(int divisor, int expectedRemainder, int multiplier) {
        this.divisor = divisor;
        this.expectedRemainder = expectedRemainder;
        this.multiplier = multiplier;
    }

    @Override
    public boolean isValid(int number, Map<Integer, String> colorMap) {
        // Gestione speciale: lo 0 di solito fa perdere queste puntate
        if (number == 0) return false;
        return number % divisor == expectedRemainder;
    }

    @Override
    public int getMultiplier() {
        return multiplier;
    }
}
