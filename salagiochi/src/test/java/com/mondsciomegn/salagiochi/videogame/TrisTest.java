package com.mondsciomegn.salagiochi.videogame;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.mondsciomegn.salagiochi.db.Category;

class TrisTest {

	Tris t = new Tris("Tris", new Category("Arcade"));
	
	@Test
	void scoreTest() {   //Test case: score del punteggio corretto
		assertEquals(300, t.getScore());
	}

}
