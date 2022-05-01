package corporate;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import client.Customer;
import client.Customer.Cart;
import exceptions.InvalidResponseType;

public class TestCustomerCart {
	ShopperSystem system;
	Manager scarbMan1;
	Store scarbStore;
	Store yorkStore;
	
	@Before
	public void setUp() throws InvalidResponseType {
		system = ShopperSystem.getInstance();
		scarbMan1 = (Manager) system.getUserByName("scarb1");
		scarbStore = scarbMan1.getManagedStore();
		yorkStore = system.getStoreByExactAddress("York");
	}
	
	// 4.4.3 Req 1 already tested in TestCustomerStoreLookup

	// test 4.4.3 Req 2
	@Test
	public void testAddRemoveItems() {
		Customer frank = (Customer) system.getUserByName("frank");
		Item beans = system.getItemByName("Beans");
		// beans are not in cart
		assertFalse(frank.isItemInCart(beans));
		assertEquals(0,frank.quantityInCart(beans));
		
		// add one beans to cart
		frank.addItemToCart(beans);
		assertTrue(frank.isItemInCart(beans));
		assertEquals(1,frank.quantityInCart(beans));
		
		// add 3 more beans to cart
		frank.addItemToCart(beans, 3);
		assertEquals(4,frank.quantityInCart(beans));
		
		// change beans quantity to 20
		frank.changeItemQuantityInCart(beans, 20);
		assertEquals(20,frank.quantityInCart(beans));
		
		// remove beans from cart
		frank.removeItemFromCart(beans);
		assertFalse(frank.isItemInCart(beans));
		assertEquals(0,frank.quantityInCart(beans));
		
		// test another way to remove from cart
		frank.addItemToCart(beans);
		frank.changeItemQuantityInCart(beans, 0); // equivalent to removing
		assertFalse(frank.isItemInCart(beans));
		assertEquals(0,frank.quantityInCart(beans));
	}
	
	// tests 4.4.3 Req 3.1 "customers log in and out"
	@Test
	public void testLoggedOut() throws InvalidResponseType {
		Customer testCust = (Customer) system.loginUser("test", "password").getUser();
		Cart original = testCust.getCart();
		Customer testCust2 = (Customer) system.loginUser("test", "password").getUser(); // mimics logging in again
		Cart newCart = testCust2.getCart();
		Set<Item> oldCartItems = original.getItems();
		for (Item i : oldCartItems) {
			assertTrue(testCust2.isItemInCart(i));
			assertEquals(original.getItemQuantity(i), newCart.getItemQuantity(i));
		}
	}
	
	// tests 4.4.3 Req 3.1 "customers are inactive for a period of time"
	@Test
	public void testInactiveUpdated() throws InvalidResponseType {
		Customer test = (Customer) system.getUserByName("thicc");
		Cart original = test.getCart();
		
		// manager updates the quantity available
		Item beans = system.getItemByName("Beans");
		scarbMan1.updateItemStock(beans, 3);
		
		Cart newCart = test.getCart(); // this is equivalent to refreshing the cart after inactivity
		Set<Item> oldCartItems = original.getItems();
		for (Item i : oldCartItems) {
			assertTrue(test.isItemInCart(i)); // which items are inside cart didn't change, just the quantity of one (which I check after)
			if (i != beans)
				assertEquals(original.getItemQuantity(i),newCart.getItemQuantity(i));
		}
		assertNotEquals(original.getItemQuantity(beans),newCart.getItemQuantity(beans));
		assertEquals(3,newCart.getItemQuantity(beans));
	}

	// tests 4.4.3 Req 3.2 "customers change the location of a store"
	@Test
	public void testChangeStore() throws InvalidResponseType {
		Customer superthicc = (Customer) system.getUserByName("superthicc");
		Store oldStore = superthicc.getPrefStore();
		Cart original = superthicc.getCart();
		superthicc.setPrefStore(yorkStore);
		Cart newCart = superthicc.getCart();
		Set<Item> oldCartItems = original.getItems();
		for (Item i : oldCartItems) {
			assertTrue(superthicc.isItemInCart(i)); // york does have every item as well
			assertTrue(original.getItemQuantity(i) > newCart.getItemQuantity(i)); // york has less inventory for each item than hk, so this needs to be true for every item
		}
		superthicc.setPrefStore(oldStore);
		Cart newerCart = superthicc.getCart();
		for (Item i : oldCartItems) {
			assertTrue(superthicc.isItemInCart(i)); // york does have every item as well
			assertEquals(newCart.getItemQuantity(i),newerCart.getItemQuantity(i)); // item quantity won't change
		}
	}
}
