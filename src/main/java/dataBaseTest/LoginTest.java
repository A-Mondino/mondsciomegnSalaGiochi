package dataBaseTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.mondsciomegn.salagiochi.db.User;

class LoginTest {
	
	private User u = new User("CiaoSonoAndre", "Andrea", "xxx");
	
	@Test
	void correctPasswordLogin() {		// Test case: Login Riuscito
		assertEquals(u.getPassword(), "xxx");
	}
	
	@Test
	void incorrectPasswordLogin() {		// Test case: Password non corretta
		assertEquals(u.getPassword(), "yyy");
	}
	
	@Test
	void ambiguosLogin() {		// Test case: Password non inserita
		assertEquals(u.getPassword(), "");
	}
	
	@Test
	void incorrectLogin() {		// Test case: coppia nickname & password non corretti
		String[] nickAndPsw = new String [2];
		nickAndPsw[0] = "CiaoSonoAngelo";
		nickAndPsw[1] = "xxx";
		
		assertEquals(u.getNickname(), nickAndPsw[0]);
		assertEquals(u.getPassword(), nickAndPsw[1]);
	}
	
	@Test
	void correctLogin() {		// Test case: coppia nickname & password corretti
		String[] nickAndPsw = new String [2];
		nickAndPsw[0] = "CiaoSonoAndre";
		nickAndPsw[1] = "xxx";
		
		assertEquals(u.getNickname(), nickAndPsw[0]);
		assertEquals(u.getPassword(), nickAndPsw[1]);
	}
}
