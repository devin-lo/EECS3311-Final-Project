package test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import base.User;
import corporate.DeleteResponse;
import corporate.Response;
import corporate.ShopperSystem;
import exceptions.InvalidResponseType;

public class TestDeleteAccount {
	ShopperSystem system;
	User deleteMe;
	String oldUsername = "deleteMe";
	String oldPW = "password";

	@Before
	public void setUp() throws InvalidResponseType {
		system = ShopperSystem.getInstance();
		deleteMe = system.loginUser(oldUsername, oldPW).getUser();
	}

	// tests 4.2 Req 4 - can ONLY do after all the other classes otherwise I'll error
	@Test
	public void testDelAcc() throws InvalidResponseType {
		assertEquals(DeleteResponse.WRONGPASS,deleteMe.deleteUser("blah").getResponse());
		assertEquals(DeleteResponse.SUCCESS,deleteMe.deleteUser(oldPW).getResponse());
		assertEquals(Response.NOUSER,system.loginUser(oldUsername, oldPW).getResponse());
		assertEquals(DeleteResponse.WRONGUSER,deleteMe.deleteUser(oldPW).getResponse()); // wronguser response bc deleteMe been deleted already
		User admin = system.loginUser("admin", "password").getUser();
		DeleteResponse tryAdmin = admin.deleteUser("password");
		assertEquals(DeleteResponse.ONEADMINLEFT,tryAdmin.getResponse());
		assertEquals("You can't delete your account because there is only one registered administrator in the system.",tryAdmin.getComment());
	}
}
