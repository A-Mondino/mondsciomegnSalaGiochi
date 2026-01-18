package com.mondsciomegn.salagiochi.videogame.roulette;

import java.util.Map;

public class ColorRule implements GameRules {
	private final String targetColor;
	private final int multiplier;

	public ColorRule(String targetColor, int multiplier) {
	   this.targetColor = targetColor;
	   this.multiplier = multiplier;
	}
	
	@Override
	public boolean isValid(int number, Map<Integer, String> colorMap) {
	   if (number == 0) return false;
	   String color = colorMap.getOrDefault(number, "green");
	   return targetColor.equals(color);
	}
	
	@Override
	public int getMultiplier() {
	   return multiplier;
	}
}
