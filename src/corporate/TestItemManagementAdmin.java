package corporate;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestItemManagementAdmin {
	ShopperSystem system;
	Store manStore;
	Administrator admin;
	public static final double EPSILON = 0.00001;
	
	@Before
	public void setUp() {
		system = ShopperSystem.getInstance();
		manStore = system.getStoreByExactAddress("Toronto");
		admin = (Administrator) system.getUserByName("admin");
		admin.changeMySelectedStore(manStore);
	}
	
	// tests 4.3.1 Req 4
	@Test
	public void testadminItemManagement() {
		Item item1 = system.getItemByName("Beanie");
		Item item2 = system.getItemByName("Broccoli");
		Item item3 = system.getItemByName("Freebie");
		assertEquals(-1,manStore.getItemAvail(item1));
		assertFalse(admin.addItem(item1, "Clothing"));
		assertTrue(admin.addItem(item1, "Other"));
		assertFalse(admin.addItem(item2, -20, "Grocery"));
		assertTrue(admin.addItem(item2, 20, "Grocery"));
		
		assertFalse(admin.getItemList().contains(item3));
		assertTrue(admin.getItemList().contains(item2));
		assertTrue(admin.getItemList().contains(item1));
		
		assertEquals(0,manStore.getItemAvail(item1));
		assertEquals(20,manStore.getItemAvail(item2));
		assertEquals("other",manStore.getItemCategory(item1));
		assertEquals("grocery",manStore.getItemCategory(item2));
		
		assertFalse(admin.updateItemStock(item3, 10));
		assertFalse(admin.updateItemStock(item2, -50));
		assertTrue(admin.updateItemStock(item1, 10));
		assertEquals(10, manStore.getItemAvail(item1));
		
		assertFalse(admin.updateItemCategory(item3, "Other"));
		assertFalse(admin.updateItemCategory(item2, "Entrance"));
		assertFalse(admin.updateItemCategory(item1, "casHier"));
		assertFalse(admin.updateItemCategory(item1, "Clothing"));
		assertTrue(admin.updateItemCategory(item1, "Grocery"));
		assertNotEquals("other",manStore.getItemCategory(item1));
		assertEquals("grocery",manStore.getItemCategory(item1));
		assertEquals("grocery",manStore.getItemCategory(item2));
		
		
		// 4.3.1 Req 3 - sale list
		
		assertEquals(0,admin.getSaleList().size());
		assertFalse(manStore.isOnSale(item1));
		assertFalse(admin.addToSaleList(item3, 0.2));
		assertFalse(admin.addToSaleList(item2, -1.0));
		assertTrue(admin.addToSaleList(item1, 0.02));
		
		assertEquals(1, admin.getSaleList().size());
		assertFalse(admin.getSaleList().contains(item3));
		assertFalse(admin.getSaleList().contains(item2));
		assertTrue(admin.getSaleList().contains(item1));
		assertTrue(manStore.isOnSale(item1));
		assertFalse(manStore.isOnSale(item2));
		assertFalse(manStore.isOnSale(item3));
		assertEquals(0.0, manStore.getItemReduction(item3),EPSILON);
		assertEquals(0.0, manStore.getItemReduction(item2),EPSILON);
		assertEquals(0.02, manStore.getItemReduction(item1),EPSILON);
		
		assertFalse(admin.updateInSaleList(item3, 0.2));
		assertEquals(0.0, manStore.getItemReduction(item3),EPSILON);
		
		assertFalse(admin.updateInSaleList(item1, 2.0));
		assertEquals(0.02, manStore.getItemReduction(item1),EPSILON);
		
		assertTrue(admin.updateInSaleList(item1, 0.3));
		assertEquals(0.3, manStore.getItemReduction(item1),EPSILON);
		assertEquals(item1.getPrice() * (1-0.3), manStore.getItemPrice(item1), EPSILON);
		
		assertTrue(admin.addToSaleList(item2, 0.04));
		assertTrue(manStore.isOnSale(item2));
		assertEquals(0.04, manStore.getItemReduction(item2),EPSILON);
		assertFalse(admin.remFromSaleList(item3));
		assertTrue(admin.remFromSaleList(item2));
		assertFalse(manStore.isOnSale(item2));
		assertEquals(0.0, manStore.getItemReduction(item2),EPSILON);
		
		// removal from list
		
		assertFalse(admin.removeItem(item3));
		assertTrue(admin.removeItem(item2));
		assertTrue(admin.removeItem(item1));
		
		assertFalse(admin.getItemList().contains(item3));
		assertFalse(admin.getItemList().contains(item2));
		assertFalse(admin.getItemList().contains(item1));
		assertEquals(-1,manStore.getItemAvail(item3));
		assertEquals(-1,manStore.getItemAvail(item2));
		assertEquals(-1,manStore.getItemAvail(item1));
		assertFalse(admin.updateItemStock(item1, 10));
		assertFalse(admin.updateItemCategory(item1, "Other"));
		assertFalse(manStore.isOnSale(item1));
	}
}
