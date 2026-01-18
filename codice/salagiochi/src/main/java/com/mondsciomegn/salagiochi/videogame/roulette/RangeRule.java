package com.mondsciomegn.salagiochi.videogame.roulette;

import java.util.Map;

public class RangeRule implements GameRules {
	 private final int min;
	 private final int max;
	 private final int multiplier;
	
	 public RangeRule(int min, int max, int multiplier) {
	     this.min = min;
	     this.max = max;
	     this.multiplier = multiplier;
	 }
	
	 @Override
	 public boolean isValid(int number, Map<Integer, String> colorMap) {
	     return number >= min && number <= max;
	 }
	
	 @Override
	 public int getMultiplier() {
	     return multiplier;
	 }
}
