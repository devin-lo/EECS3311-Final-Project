package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import client.Customer;
import client.Customer.Cart;
import corporate.Item;
import corporate.ShopperSystem;
import corporate.Store;
import exceptions.InvalidResponseType;

public class TestBestOrder {
	ShopperSystem system;
	Customer customer;
	Store store;
	public static final double EPSILON = 0.00001;
	
	@Before
	public void setUp() throws InvalidResponseType {
		system = ShopperSystem.getInstance();
		customer = (Customer) system.loginUser("ordered", "password").getUser();
		store = customer.getPrefStore();
	}

	// tests 4.4.4
	@Test
	public void testBestOrder() {
		Map<Item, Double> bestOrder = customer.getBestOrder(10);
		List<Item> itemOrder = new ArrayList<Item>();
		List<Double> timeOrder = new ArrayList<Double>();
		for (Map.Entry<Item, Double> entry : bestOrder.entrySet()) {
			itemOrder.add(entry.getKey());
			timeOrder.add(entry.getValue());
		}
		
		// 4.4.4 - Req 1.2
		Cart cart = customer.getCart();
		for (Item i : cart.getItems())
			assertTrue(itemOrder.contains(i));
		
		// grocery, clothes, furniture
		Item beans = system.getItemByName("Beans");
		Item beanie = system.getItemByName("Beanie");
		Item chair = system.getItemByName("Beanbag Chair");
		
		// 4.4.4 - Req 1.1
		assertEquals(beans,itemOrder.get(0));
		assertEquals(beanie,itemOrder.get(1));
		assertEquals(chair,itemOrder.get(2));
		
		// 4.4.4 - Req 1.3
		assertEquals(0.5 * customer.quantityInCart(beans) * store.getDensity(store.getItemCategory(beans)), timeOrder.get(0), EPSILON);
		assertEquals(0.5 * customer.quantityInCart(beanie) * store.getDensity(store.getItemCategory(beanie)), timeOrder.get(1), EPSILON);
		assertEquals(0.5 * customer.quantityInCart(chair) * store.getDensity(store.getItemCategory(chair)), timeOrder.get(2), EPSILON);
	}

}
