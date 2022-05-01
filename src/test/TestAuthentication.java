package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import base.User;
import corporate.ShopperSystem;
import exceptions.InvalidResponseType;
import corporate.Response;

// tests Req 4.1
public class TestAuthentication {
	
	ShopperSystem system;
	
	// 4.1/4.2/4.3.1/4.3.2/4.3.3/4.4.1/4.4.2/4.4.3/4.4.4/4.5 are the 10 requirements
	@Before
	public void setUp() {
		system = ShopperSystem.getInstance();
	}
	
	// tests 4.1 Req 1 and 4.1 Req 2
	@Test
	public void testRegister() throws InvalidResponseType {
		String username = "abc";
		String password = "passwordD";
		assertTrue(system.createCustomerAccount(username, password));
		Response tryLogin = system.loginUser(username, password);
		assertEquals(Response.SUCCESS,tryLogin.getResponse());
		assertEquals(username,tryLogin.getUser().getUsername());
	}
	
	// tests 4.1 Req 3
	@Test
	public void testLoginFail() throws InvalidResponseType {
		Response noUser = system.loginUser("whatever", "password");
		assertEquals(Response.NOUSER,noUser.getResponse());
		assertEquals("No user with that username exists.",noUser.getComment());
		Response wrongPW = system.loginUser("abd", "password");
		assertEquals(Response.WRONGPASS,wrongPW.getResponse());
		assertEquals("Wrong password.",wrongPW.getComment());
	}
}
