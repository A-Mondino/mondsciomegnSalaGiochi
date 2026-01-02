package com.mondsciomegn.salagiochi.videogame;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.mondsciomegn.salagiochi.db.Category;
import com.mondsciomegn.salagiochi.videogame.battleship.Battleship;

class BattleshipTest {

	Battleship b = new Battleship("BattleShip", new Category("Strategia"));
	
	@Test
	void scoreTest() {   //Test case: score del punteggio non corretto
		assertEquals(100, b.getScore());
	}


}
