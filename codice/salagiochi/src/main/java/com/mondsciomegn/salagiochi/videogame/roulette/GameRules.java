package com.mondsciomegn.salagiochi.videogame.roulette;

import java.util.Map;

public interface GameRules {
    boolean isValid(int number, Map<Integer, String> colorMap);
    int getMultiplier();
}