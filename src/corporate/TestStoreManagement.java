package corporate;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestStoreManagement {
	ShopperSystem system;
	Manager manager;
	Manager michael;
	Store manStore;
	Administrator admin;
	int currOpenTime;
	int currCloseTime;
	public static final double EPSILON = 0.00001;

	@Before
	public void setUp() {
		system = ShopperSystem.getInstance();
		manager = (Manager) system.getUserByName("tor1");
		manStore = manager.getManagedStore();
		admin = (Administrator) system.getUserByName("admin");
		currOpenTime = manStore.getStoreOpenTime();
		currCloseTime = manStore.getStoreCloseTime();
		michael = (Manager) system.getUserByName("mscott");
	}

	// test 4.3.2 Req 2
	@Test
	public void testManUpdateTime() {
		int newOpenTime = 440;
		int newCloseTime = 1280;
		assertFalse(manager.updateStoreOpenTime(currCloseTime + 10));
		assertFalse(manager.updateStoreCloseTime(currOpenTime - 10));
		assertTrue(manager.updateStoreOpenTime(newOpenTime));
		assertTrue(manager.updateStoreCloseTime(newCloseTime));
		assertEquals(newOpenTime, manStore.getStoreOpenTime());
		assertEquals(newCloseTime, manStore.getStoreCloseTime());
		assertTrue(manager.updateStoreOpenTime(currOpenTime));
		assertTrue(manager.updateStoreCloseTime(currCloseTime));
	}

	// test 4.3.2 Req 4
	@Test
	public void testAdminUpdateTime() {
		admin.changeMySelectedStore(manStore);
		int newOpenTime = 440;
		int newCloseTime = 1280;
		assertFalse(admin.updateStoreOpenTime(currCloseTime + 10));
		assertFalse(admin.updateStoreCloseTime(currOpenTime - 10));
		assertTrue(admin.updateStoreOpenTime(newOpenTime));
		assertTrue(manager.updateStoreCloseTime(newCloseTime));
		assertEquals(newOpenTime, manStore.getStoreOpenTime());
		assertEquals(newCloseTime, manStore.getStoreCloseTime());
		assertTrue(admin.updateStoreOpenTime(currOpenTime));
		assertTrue(admin.updateStoreCloseTime(currCloseTime));
	}

	// test 4.3.2 Req 5
	@Test
	public void testManagerUpdateCat() {
		String newCat1 = "Power Tools";
		String newCat2 = "Gardening";
		assertFalse(manStore.getCategories().contains(newCat1));
		assertNull(manStore.getDensity(newCat1));
		assertFalse(manager.removeCategory(newCat2));
		assertFalse(manager.updateDensity(newCat2, 2.4));
		assertFalse(manager.addCategory("entrance"));
		assertFalse(manager.addCategory("casHier"));
		assertTrue(manager.addCategory(newCat1, "Grocery"));
		assertTrue(manager.addCategory(newCat2));
		assertEquals(manStore.getCategories().indexOf("grocery") + 1,
				manStore.getCategories().indexOf(newCat1.toLowerCase()));
		assertEquals(manStore.getCategories().indexOf("cashier") - 1,
				manStore.getCategories().indexOf(newCat2.toLowerCase()));
		assertEquals(1.0, manStore.getDensity(newCat1), EPSILON);
		assertTrue(manager.updateDensity(newCat1, 1.1));
		assertEquals(1.1, manStore.getDensity(newCat1), EPSILON);
		assertTrue(manager.removeCategory(newCat1));
		assertTrue(manager.removeCategory(newCat2));
		assertFalse(manStore.getCategories().contains(newCat1));
		assertNull(manStore.getDensity(newCat1));
		assertFalse(manager.updateDensity(newCat2, 2.4));
	}

	// test 4.3.2 Req 3
	@Test
	public void testAdminUpdateCat() {
		admin.changeMySelectedStore(manStore);
		String newCat1 = "Power Tools";
		String newCat2 = "Gardening";
		assertFalse(manStore.getCategories().contains(newCat1));
		assertNull(manStore.getDensity(newCat1));
		assertFalse(admin.removeCategory(newCat2));
		assertFalse(admin.updateDensity(newCat2, 2.4));
		assertFalse(admin.addCategory("entrance"));
		assertFalse(admin.addCategory("casHier"));
		assertTrue(admin.addCategory(newCat1, "Grocery"));
		assertTrue(admin.addCategory(newCat2));
		assertEquals(manStore.getCategories().indexOf("grocery") + 1, manStore.getCategories().indexOf(newCat1.toLowerCase()));
		assertEquals(manStore.getCategories().indexOf("cashier") - 1, manStore.getCategories().indexOf(newCat2.toLowerCase()));
		assertEquals(1.0, manStore.getDensity(newCat1), EPSILON);
		assertTrue(admin.updateDensity(newCat1, 1.1));
		assertEquals(1.1, manStore.getDensity(newCat1), EPSILON);
		assertTrue(admin.removeCategory(newCat1));
		assertTrue(admin.removeCategory(newCat2));
		assertFalse(manStore.getCategories().contains(newCat1));
		assertNull(manStore.getDensity(newCat1));
		assertFalse(admin.updateDensity(newCat2, 2.4));
	}

	// test 4.3.2 Req 1
	@Test
	public void testAdminAddRemStore() {
		assertFalse(admin.addStore(manStore));
		String newAddress = "Vaughan";
		Store newStore = new Store(newAddress, 420, 1380);
		assertFalse(admin.removeStore(newStore)); // store not added to system yet
		assertTrue(admin.addStore(newStore));
		assertTrue(system.getStores().contains(newStore));
		assertEquals(newStore, system.getStoreByExactAddress(newAddress));
		assertTrue(admin.removeStore(newStore));
		assertNull(system.getStoreByExactAddress(newAddress));
	}
	
	// increase manager %
	@Test
	public void testManNoStoreManage() {
		assertNull(michael.getManagedStore());
		int newOpenTime = 440;
		int newCloseTime = 1280;
		assertFalse(michael.updateStoreOpenTime(currCloseTime + 10));
		assertFalse(michael.updateStoreCloseTime(currOpenTime - 10));
		assertFalse(michael.updateStoreOpenTime(newOpenTime));
		assertFalse(michael.updateStoreCloseTime(newCloseTime));
		String newCat1 = "Power Tools";
		String newCat2 = "Gardening";
		assertFalse(michael.removeCategory(newCat2));
		assertFalse(michael.updateDensity(newCat2, 2.4));
		assertFalse(michael.addCategory("entrance"));
		assertFalse(michael.addCategory("casHier"));
		assertFalse(michael.addCategory(newCat1, "Grocery"));
		assertFalse(michael.addCategory(newCat2));
		assertFalse(michael.updateDensity(newCat1, 1.1));
		assertFalse(michael.removeCategory(newCat1));
		assertFalse(michael.removeCategory(newCat2));
		assertFalse(michael.updateDensity(newCat2, 2.4));
		assertNull(michael.getItemList());
		assertNull(michael.getSaleList());
	}
}
