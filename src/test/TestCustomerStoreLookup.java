package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import client.Customer;
import corporate.Item;
import corporate.ShopperSystem;
import corporate.Store;
import exceptions.InvalidResponseType;

public class TestCustomerStoreLookup {
	ShopperSystem system;
	Customer customer;
	Store northYork;
	
	@Before
	public void setUp() throws InvalidResponseType {
		system = ShopperSystem.getInstance();
		customer = (Customer) system.loginUser("abcdefghi", "password").getUser();
		northYork = system.getStoreByExactAddress("North York");
	}

	// tests 4.4.1 Req 1
	@Test
	public void testSearchLocations() {
		List<Store> custSearch = customer.searchLocations("york", 50);
		assertTrue(custSearch.contains(northYork));
		assertEquals(1, custSearch.indexOf(northYork)); // because store named "York" should come before it!
	}

	// tests 4.4.1 Req 2 and 4.4.3 Req 1
	@Test
	public void testSaveUnsaveLocations() {
		assertNull(customer.getPrefStore());
		assertNull(customer.getCart());
		Store northYork = system.getStoreByExactAddress("North York");
		assertEquals("Set the preferred store.\n",customer.setPrefStore(northYork));
		assertEquals(northYork,customer.getPrefStore());
		assertEquals(0,customer.getCart().getItems().size()); // i.e. there is a cart
		assertEquals("Deleted preferred store from settings. (This also deletes your existing cart)",customer.setPrefStore(null));
		assertNull(customer.getPrefStore());
		assertNull(customer.getCart());
		
		// test null cart - for coverage
		Item beans = system.getItemByName("Beans");
		assertEquals(Customer.CARTERR,customer.addItemToCart(beans));
		assertEquals(Customer.CARTERR,customer.addItemToCart(beans, 3));
		assertEquals(Customer.CARTERR,customer.changeItemQuantityInCart(beans, 3));
		assertEquals(Customer.CARTERR,customer.removeItemFromCart(beans));
		assertEquals(Customer.CARTERR,customer.updateCartAvail());
		assertNull(customer.getBestOrder(3));
		assertFalse(customer.isItemInCart(beans));
		assertEquals(0,customer.quantityInCart(beans));
	}
	
	// tests 4.4.1 Req 3
	@Test
	public void testChangeLocation() {
		Store scarb = system.getStoreByExactAddress("Scarborough");
		customer.setPrefStore(northYork);
		assertNotEquals(scarb,customer.getPrefStore());
		customer.setPrefStore(scarb);
		assertEquals(scarb, customer.getPrefStore());
		assertNotEquals(northYork, customer.getPrefStore());
	}
}
