package corporate;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import base.User;

public class TestAdminItem {
	// additional tests for Administrator and Item to increase their coverage
	ShopperSystem system;
	Administrator admin;
	public static final double EPSILON = 0.00001;
	
	@Before
	public void setUp() {
		system = ShopperSystem.getInstance();
		admin = (Administrator) system.getUserByName("admin");
	}

	@Test
	public void testItemEditName() {
		Item blah = system.getItemByName("Blah");
		assertEquals("Blah",blah.toString());
		blah.setName("what");
		assertNotEquals("Blah",blah.toString());
		assertEquals("what",blah.toString());
		assertNull(system.getItemByName("Blah"));
		blah.setName("Blah");
	}

	@Test
	public void testAdminAddRemSysItem() {
		Item blah = system.getItemByName("Blah");
		Item newItem = new Item("Poison");
		// before adding the new item to list
		assertFalse(admin.addItemToSystem(blah));
		assertFalse(system.getSystemItemList().contains(newItem));
		
		// add to sys list
		assertTrue(admin.addItemToSystem(newItem));
		assertTrue(system.getSystemItemList().contains(newItem));
		
		// remove from sys list
		assertTrue(admin.removeItemFromSystem(newItem));
		assertFalse(admin.removeItemFromSystem(newItem));
		assertFalse(system.getSystemItemList().contains(newItem));
	}
	
	@Test
	public void testAdminAddRemAdmin() {
		assertFalse(admin.createAdmin("abd", "password")); // existing username
		assertTrue(admin.createAdmin("tempAdmin","password"));
		assertTrue(system.getUserList().contains("tempAdmin"));
		assertTrue(admin.removeUser("tempAdmin"));
		assertFalse(admin.removeUser("tempAdmin"));
		assertFalse(system.getUserList().contains("tempAdmin"));
	}
	
	@Test
	public void testAdminAddRemCustomer() {
		assertFalse(admin.createCustomer("abd", "password")); // existing username
		assertTrue(admin.createCustomer("tempCust","password"));
		assertTrue(system.getUserList().contains("tempCust"));
		assertTrue(admin.removeUser("tempCust"));
		assertFalse(admin.removeUser("tempCust"));
		assertFalse(system.getUserList().contains("tempCust"));
	}
	
	@Test
	public void testAdminDeleteManager() {
		Manager tokyo = (Manager) system.getUserByName("tokyo");
		Store tokStore = tokyo.getManagedStore();
		assertTrue(admin.removeUser("tokyo"));
		assertFalse(tokStore.getManagerList().contains(tokyo));
	}
	
	@Test
	public void testAdminDeleteStore() {
		Store seoulStore = system.getStoreByExactAddress("Seoul");
		Manager seoul = (Manager) system.getUserByName("seoul");
		admin.changeMySelectedStore(seoulStore);
		assertEquals(seoulStore,admin.getManagedStore());
		assertTrue(admin.removeStore(seoulStore));
		assertNull(seoul.getManagedStore());
		assertNull(admin.getManagedStore());
	}
}
