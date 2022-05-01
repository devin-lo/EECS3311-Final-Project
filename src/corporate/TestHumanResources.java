package corporate;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class TestHumanResources {
	ShopperSystem system;
	Administrator admin;

	@Before
	public void setUp() {
		system = ShopperSystem.getInstance();
		admin = (Administrator) system.getUserByName("admin");
	}
	
	// test 4.3.3 Req 1, part 1 (adding a new manager)
	@Test
	public void testAddManager() {
		Manager oldMan = (Manager) system.getUserByName("tor1");
		assertFalse(admin.createManager("tor1", "password"));
		assertTrue(admin.createManager("testman", "password"));
		Manager newMan = (Manager) system.getUserByName("testman");
		assertNull(newMan.getManagedStore());
		
		// test assignment of a manager to a store
		Store tor = system.getStoreByExactAddress("Toronto");
		assertFalse(admin.addManager(oldMan, tor));
		assertTrue(admin.addManager(newMan, tor));
		assertTrue(tor.getManagerList().contains(newMan));
		assertEquals(tor, newMan.getManagedStore());
	}

	// test 4.3.3 Req 1 part 2 (remove manager) and 4.3.3 Req 2
	@Test
	public void testRemoveManager() {
		Manager tor2 = (Manager) system.getUserByName("tor2");
		Store tor = tor2.getManagedStore();
		assertTrue(admin.removeManager(tor2, tor));
		assertFalse(admin.removeManager(tor2, tor)); // can't remove someone who doesn't work there!
		assertNull(tor2.getManagedStore());
		assertFalse(tor.getManagerList().contains(tor2));
		assertFalse(tor2.updateStoreOpenTime(50));
		assertNull(tor2.getSaleList());
		assertNotEquals(50,tor.getStoreOpenTime()); // should not have changed
	}
	
	// test 4.3.3 Req 1 part 3 (update manager) and 4.3.3 Req 3
	@Test
	public void testUpdateManager() {
		Manager hkMan = (Manager) system.getUserByName("hk");
		Store york = system.getStoreByExactAddress("York");
		Store hk = hkMan.getManagedStore();
		assertNotEquals(york, hkMan.getManagedStore());
		assertTrue(admin.updateManager(hkMan, hk, hk)); // technically this should be true, even if redundant
		assertFalse(admin.updateManager(hkMan, york, hk)); // false because hkMan doesn't work at york
		assertTrue(admin.updateManager(hkMan, hk, york));
		assertNotEquals(hk, hkMan.getManagedStore());
		assertEquals(york, hkMan.getManagedStore());
		assertFalse(hk.getManagerList().contains(hkMan));
		assertTrue(york.getManagerList().contains(hkMan));
		assertTrue(hkMan.updateStoreOpenTime(3));
		assertEquals(3,york.getStoreOpenTime());
		assertNotEquals(3,hk.getStoreOpenTime());
		assertTrue(admin.updateManager(hkMan, york, hk));
	}
}
