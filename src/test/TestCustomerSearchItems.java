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

public class TestCustomerSearchItems {
	ShopperSystem system;
	Customer customer1;
	Customer customer2;
	Store york;
	Store montreal;
	public static final double EPSILON = 0.00001;
	
	@Before
	public void setUp() throws InvalidResponseType {
		system = ShopperSystem.getInstance();
		customer1 = (Customer) system.loginUser("test", "password").getUser();
		york = system.getStoreByExactAddress("York");
		customer2 = (Customer) system.loginUser("me", "password").getUser();
		montreal = system.getStoreByExactAddress("Montreal");
	}

	// tests 4.4.2 Req 1.1 and 4.4.2 Req 2
	@Test
	public void testSearchByName() {
		Item broccoli = system.getItemByName("Broccoli");
		Item chair = system.getItemByName("Beanbag Chair");
		Item beans = system.getItemByName("Beans");
		List<Item> nameSearch = customer1.search("bean", Customer.BYNAME);
		assertEquals(3,nameSearch.size());
		assertTrue(nameSearch.contains(chair));
		assertFalse(nameSearch.contains(broccoli));
		double reducedBy = york.getItemReduction(beans);
		StringBuilder resultIntermediate = new StringBuilder();
		resultIntermediate.append("Name: Beans\n");
		resultIntermediate.append("Description: 25 oz can of beans\n");
		resultIntermediate.append("Price: " + 3.0 * (1-reducedBy) + "\n");
		resultIntermediate.append("Size: 25.0\n");
		resultIntermediate.append("Number available in your selected store: " + york.getItemAvail(beans));
		String result = resultIntermediate.toString();
		assertEquals(result,customer1.getItemInfo(beans));
	}
	
	// test 4.4.2 Req 1.2
	@Test
	public void testSearchByCat() {
		Item beans = system.getItemByName("Beans");
		Item broccoli = system.getItemByName("Broccoli");
		Item chair = system.getItemByName("Beanbag Chair");
		List<Item> catSearch = customer2.search("Grocery", Customer.BYCATEGORY);
		assertEquals(2, catSearch.size());
		assertTrue(catSearch.contains(beans));
		assertTrue(catSearch.contains(broccoli));
		assertFalse(catSearch.contains(chair));
	}
}
