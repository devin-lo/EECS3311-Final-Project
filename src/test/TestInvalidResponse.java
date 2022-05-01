package test;

import static org.junit.Assert.*;

import org.junit.Test;

import corporate.DeleteResponse;
import corporate.Response;
import exceptions.InvalidResponseType;

public class TestInvalidResponse {

	// tests to increase coverage of DeleteResponse and Response
	
	@Test
	public void testDelete() {
		try {
	    	DeleteResponse del = new DeleteResponse(4);
	    	fail("Unexpected: Exception not thrown");
	    } catch (InvalidResponseType e) {
	    	assertEquals("4 is not a valid response type", e.getMessage());
	    }
	}

	@Test
	public void testLogin() {
		try {
	    	Response login = new Response(5);
	    	fail("Unexpected: Exception not thrown");
	    } catch (InvalidResponseType e) {
	    	assertEquals("5 is not a valid response type", e.getMessage());
	    }
	}
}
