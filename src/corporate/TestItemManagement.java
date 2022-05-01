package corporate;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestItemManagement {
	ShopperSystem system;
	Manager manager;
	Manager michael;
	Store manStore;
	public static final double EPSILON = 0.00001;
	
	@Before
	public void setUp() {
		system = ShopperSystem.getInstance();
		manager = (Manager) system.getUserByName("tor1");
		manStore = manager.getManagedStore();
		michael = (Manager) system.getUserByName("mscott");
	}
	
	// tests 4.3.1 Req 1 and 2 and 3
	@Test
	public void testManagerItemManagement() {
		Item item1 = system.getItemByName("Beanie");
		Item item2 = system.getItemByName("Broccoli");
		Item item3 = system.getItemByName("Freebie");
		assertEquals(-1,manStore.getItemAvail(item1));
		assertFalse(manager.addItem(item1, "Clothing"));
		assertTrue(manager.addItem(item1, "Other"));
		assertFalse(manager.addItem(item2, -20, "Grocery"));
		assertTrue(manager.addItem(item2, 20, "Grocery"));
		
		assertFalse(manager.getItemList().contains(item3));
		assertTrue(manager.getItemList().contains(item2));
		assertTrue(manager.getItemList().contains(item1));
		
		assertEquals(0,manStore.getItemAvail(item1));
		assertEquals(20,manStore.getItemAvail(item2));
		assertEquals("other",manStore.getItemCategory(item1));
		assertEquals("grocery",manStore.getItemCategory(item2));
		
		assertFalse(manager.updateItemStock(item3, 10));
		assertFalse(manager.updateItemStock(item2, -50));
		assertTrue(manager.updateItemStock(item1, 10));
		assertEquals(10, manStore.getItemAvail(item1));
		
		assertFalse(manager.updateItemCategory(item3, "Other"));
		assertFalse(manager.updateItemCategory(item2, "Entrance"));
		assertFalse(manager.updateItemCategory(item1, "casHier"));
		assertFalse(manager.updateItemCategory(item1, "Clothing"));
		assertTrue(manager.updateItemCategory(item1, "Grocery"));
		assertNotEquals("other",manStore.getItemCategory(item1));
		assertEquals("grocery",manStore.getItemCategory(item1));
		assertEquals("grocery",manStore.getItemCategory(item2));
		
		
		// 4.3.1 Req 3 - sale list
		
		assertEquals(0,manager.getSaleList().size());
		assertFalse(manStore.isOnSale(item1));
		assertFalse(manager.addToSaleList(item3, 0.2));
		assertFalse(manager.addToSaleList(item2, -1.0));
		assertTrue(manager.addToSaleList(item1, 0.02));
		
		assertEquals(1, manager.getSaleList().size());
		assertFalse(manager.getSaleList().contains(item3));
		assertFalse(manager.getSaleList().contains(item2));
		assertTrue(manager.getSaleList().contains(item1));
		assertTrue(manStore.isOnSale(item1));
		assertFalse(manStore.isOnSale(item2));
		assertFalse(manStore.isOnSale(item3));
		assertEquals(0.0, manStore.getItemReduction(item3),EPSILON);
		assertEquals(0.0, manStore.getItemReduction(item2),EPSILON);
		assertEquals(0.02, manStore.getItemReduction(item1),EPSILON);
		
		assertFalse(manager.updateInSaleList(item3, 0.2));
		assertEquals(0.0, manStore.getItemReduction(item3),EPSILON);
		
		assertFalse(manager.updateInSaleList(item1, 2.0));
		assertEquals(0.02, manStore.getItemReduction(item1),EPSILON);
		
		assertTrue(manager.updateInSaleList(item1, 0.3));
		assertEquals(0.3, manStore.getItemReduction(item1),EPSILON);
		assertEquals(item1.getPrice() * (1-0.3), manStore.getItemPrice(item1), EPSILON);
		
		assertTrue(manager.addToSaleList(item2, 0.04));
		assertTrue(manStore.isOnSale(item2));
		assertEquals(0.04, manStore.getItemReduction(item2),EPSILON);
		assertFalse(manager.remFromSaleList(item3));
		assertTrue(manager.remFromSaleList(item2));
		assertFalse(manStore.isOnSale(item2));
		assertEquals(0.0, manStore.getItemReduction(item2),EPSILON);
		
		// removal from list
		
		assertFalse(manager.removeItem(item3));
		assertTrue(manager.removeItem(item2));
		assertTrue(manager.removeItem(item1));
		
		assertFalse(manager.getItemList().contains(item3));
		assertFalse(manager.getItemList().contains(item2));
		assertFalse(manager.getItemList().contains(item1));
		assertEquals(-1,manStore.getItemAvail(item3));
		assertEquals(-1,manStore.getItemAvail(item2));
		assertEquals(-1,manStore.getItemAvail(item1));
		assertFalse(manager.updateItemStock(item1, 10));
		assertFalse(manager.updateItemCategory(item1, "Other"));
		assertFalse(manStore.isOnSale(item1));
	}
	
	// tests to get manager coverage % above 80
	@Test
	public void testManagerNoStoreItemMan() {
		Item item1 = system.getItemByName("Beanie");
		Item item2 = system.getItemByName("Broccoli");
		Item item3 = system.getItemByName("Freebie");
		assertFalse(michael.addItem(item1, "Clothing"));
		assertFalse(michael.addItem(item1, "Other"));
		assertFalse(michael.addItem(item2, -20, "Grocery"));
		assertFalse(michael.addItem(item2, 20, "Grocery"));

		assertFalse(michael.updateItemStock(item3, 10));
		assertFalse(michael.updateItemStock(item2, -50));
		assertFalse(michael.updateItemStock(item1, 10));

		assertFalse(michael.updateItemCategory(item3, "Other"));
		assertFalse(michael.updateItemCategory(item2, "Entrance"));
		assertFalse(michael.updateItemCategory(item1, "casHier"));
		assertFalse(michael.updateItemCategory(item1, "Clothing"));
		assertFalse(michael.updateItemCategory(item1, "Grocery"));

		// sale list

		assertNull(michael.getSaleList());
		assertFalse(michael.addToSaleList(item3, 0.2));
		assertFalse(michael.addToSaleList(item2, -1.0));
		assertFalse(michael.addToSaleList(item1, 0.02));
		
		assertNull(michael.getSaleList());

		assertFalse(michael.updateInSaleList(item3, 0.2));
		assertFalse(michael.remFromSaleList(item3));
		assertFalse(michael.remFromSaleList(item2));

		// removal from list

		assertFalse(michael.removeItem(item3));
		assertFalse(michael.removeItem(item2));
		assertFalse(michael.removeItem(item1));
		
		assertNull(michael.getSaleList());
	}
}
