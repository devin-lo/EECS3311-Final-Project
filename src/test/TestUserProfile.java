package test;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import base.User;
import corporate.DeleteResponse;
import corporate.Response;
import corporate.Store;
import corporate.ShopperSystem;
import exceptions.InvalidResponseType;

// tests 4.2
public class TestUserProfile {
	ShopperSystem system;
	User abd;
	String oldUsername = "abd";
	String oldPW = "abd";
	
	// 4.1/4.2/4.3.1/4.3.2/4.3.3/4.4.1/4.4.2/4.4.3/4.4.4/4.5 are the 10 requirements
	@Before
	public void setUp() throws InvalidResponseType {
		system = ShopperSystem.getInstance();
		abd = system.loginUser(oldUsername, oldPW).getUser();
	}
	
	// tests 4.2 Req 1 part 1
	@Test
	public void testChangeUsername() {
		String existingOtherUsername = "me";
		String newUsername = "abcABC";
		assertFalse(abd.changeUsername(existingOtherUsername, oldPW));
		assertFalse(abd.changeUsername(newUsername, "blah"));
		assertTrue(abd.changeUsername(newUsername, oldPW));
		assertNotEquals(oldUsername, abd.getUsername());
		assertEquals(newUsername, abd.getUsername());
		assertTrue(abd.changeUsername(oldUsername, oldPW)); // gotta change the username back before next test
		assertNotEquals(newUsername, abd.getUsername());
		assertEquals(oldUsername, abd.getUsername());
	}

	// tests 4.2 Req 1 part 2
	@Test
	public void testChangePW() throws InvalidResponseType {
		String newPW = "password";
		assertFalse(abd.changePassword(newPW, newPW));
		assertTrue(abd.changePassword(oldPW, newPW));
		assertEquals(Response.WRONGPASS,system.loginUser(oldUsername, oldPW).getResponse());
		User isIt = system.loginUser(oldUsername, newPW).getUser();
		assertEquals(abd,isIt);
		assertTrue(abd.changePassword(newPW, oldPW)); // change it back before next test
	}
	
	// tests 4.2 Req 2
	@Test
	public void testTwoFactor() {
		String testPhone1 = "416-123-4567";
		String testPhone2 = "123-456-7890";
		assertFalse(abd.hasTwoFactor());
		assertFalse(abd.setTwoFactor(testPhone1, testPhone2));
		assertTrue(abd.setTwoFactor(oldPW, testPhone1));
		assertTrue(abd.hasTwoFactor());
		assertEquals(testPhone1,abd.getTwoFactor());
		assertTrue(abd.setTwoFactor(oldPW, testPhone2));
		assertTrue(abd.hasTwoFactor());
		assertNotEquals(testPhone1,abd.getTwoFactor());
		assertEquals(testPhone2,abd.getTwoFactor());
		assertFalse(abd.clearTwoFactor(testPhone2));
		assertTrue(abd.clearTwoFactor(oldPW));
		assertFalse(abd.hasTwoFactor());
	}
	
	// tests 4.2 Req 3
	@Test
	public void testStorePref() {
		Store torStore = system.getStoreByExactAddress("Toronto");
		Store yorkStore = system.getStoreByExactAddress("York");
		assertNull(abd.getPrefStore());
		assertNotNull(abd.setPrefStore(torStore));
		assertNotNull(abd.getPrefStore());
		assertEquals(torStore,abd.getPrefStore());
		assertNotNull(abd.setPrefStore(yorkStore));
		assertNotNull(abd.getPrefStore());
		assertNotEquals(torStore,abd.getPrefStore());
		assertNotNull(abd.setPrefStore(null));
		assertNull(abd.getPrefStore());
	}
	
	// test 4.2 Req 4 part 1
	@Test
	public void testUserProfile1() throws InvalidResponseType {
		User who = system.loginUser("who", "password").getUser();
		assertNull(who.getAccountInfo("what"));
		Map<String, String> userInfo = who.getAccountInfo("password");
		assertNotNull(userInfo);
		assertEquals("who",userInfo.get("Username"));
		assertEquals(who.getTwoFactor(),userInfo.get("Two-Factor Phone"));
		assertEquals(system.getStoreByExactAddress("York").getAddress(),userInfo.get("Preferred Store"));
	}
	
	// test 4.2 Req 4 part 1
	@Test
	public void testUserProfile2() throws InvalidResponseType {
		Map<String, String> userInfo = abd.getAccountInfo(oldPW);
		assertNotNull(userInfo);
		assertEquals(oldUsername, userInfo.get("Username"));
		assertEquals("N/A", userInfo.get("Two-Factor Phone"));
		assertEquals("None", userInfo.get("Preferred Store"));
	}
	
	// test 4.2 Req 4 part 1
	// increases manager coverage %
	@Test
	public void testUserProfile3() throws InvalidResponseType {
		User mscott = system.loginUser("mscott", "password").getUser();
		Map<String, String> userInfo = mscott.getAccountInfo("password");
		assertNotNull(userInfo);
		assertEquals("mscott", userInfo.get("Username"));
		assertEquals("N/A", userInfo.get("Two-Factor Phone"));
		assertEquals("N/A", userInfo.get("Managed Store"));
	}
	
	// test 4.2 Req 4 part 1
	// increases manager coverage %
	@Test
	public void testUserProfile4() throws InvalidResponseType {
		User york = system.loginUser("york", "password").getUser();
		Map<String, String> userInfo = york.getAccountInfo("password");
		assertNotNull(userInfo);
		assertEquals("york", userInfo.get("Username"));
		assertEquals("N/A", userInfo.get("Two-Factor Phone"));
		assertEquals("York", userInfo.get("Managed Store"));
	}
}
