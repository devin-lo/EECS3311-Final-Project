package test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import client.Customer;
import corporate.Item;
import corporate.ShopperSystem;
import corporate.Store;
import exceptions.InvalidResponseType;

public class TestRecommendation {
	ShopperSystem system;
	Customer customer;
	Store york;
	
	@Before
	public void setUp() throws InvalidResponseType {
		system = ShopperSystem.getInstance();
		customer = (Customer) system.loginUser("who", "password").getUser();
		york = system.getStoreByExactAddress("York");
	}

	// tests 4.5 Reqs 1 and 2
	@Test
	public void testRecommendation() {
		List<Item> myRec = system.suggestedItems(customer); // 4.5 Req 1
		Item chair = system.getItemByName("Beanbag Chair");
		Item beanie = system.getItemByName("Beanie");
		
		// 4.5 Req 2.1 - my interpretation of this:
		// all items on sale should be on the suggested items list
		Set<Item> saleList = york.getSaleList();
		for (Item i : saleList)
			assertTrue(myRec.contains(i));
		
		// if customer has that item in cart, it shouldn't show up in suggested items.
		assertTrue(customer.isItemInCart(beanie));
		assertFalse(myRec.contains(beanie));
		
		/* 4.5 Req 2.2
		 * beanbag chair is an item that is not in the customer's list,
		 * and the only other customer at the store with this item doesn't have any items in common
		 * in their cart with user "who"
		 */
		assertFalse(customer.isItemInCart(chair));
		assertFalse(myRec.contains(chair));
		
		// 4.5 Req 2.2 - item not in customer's cart but IS in the cart of another customer who has a common item
		Item testItem = system.getItemByName("Test Item");
		assertFalse(customer.isItemInCart(testItem));
		assertTrue(myRec.contains(testItem));
		
		// 4.5 Req 2.3
		Item popular = system.getItemByName("Super Popular Thing");
		assertEquals(popular, myRec.get(0)); // because popular has an artifically high search freq, it should be the top item in list.
	}

}
