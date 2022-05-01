package corporate;

import java.util.Set;

public class Administrator extends Manager {
	ShopperSystem system;
	protected Administrator(String username, String password) {
		super(username, password);
		system = ShopperSystem.getInstance();
		system.addAdmin();
	}
	
	boolean addItemToSystem(Item item) {
		return system.addItem(item);
	}
	
	boolean removeItemFromSystem(Item item) {
		return system.removeItem(item);
	}
	
	boolean addStore(Store store) {
		return system.addStore(store);
	}
	
	boolean removeStore(Store store) {
		boolean b = system.removeStore(store);
		if (b && getManagedStore() != null && getManagedStore().equals(store))
			changeMySelectedStore(null);
		return b;
	}
	
	protected boolean createCustomer(String username, String password) {
		return system.createAccount(username, password, ShopperSystem.CUSTOMER);
	}
	
	protected boolean createManager(String username, String password) {
		return system.createAccount(username, password, ShopperSystem.MANAGER);
	}
	
	protected boolean createAdmin(String username, String password) {
		return system.createAccount(username, password, ShopperSystem.ADMINISTRATOR);
	}
	
	protected Set<String> getUserList() {
		return system.getUserList();
	}
	
	protected boolean removeUser(String username) {
		DeleteResponse rep = system.removeUser(username);
		if (rep.getResponse() == DeleteResponse.SUCCESS)
			return true;
		return false;
	}
	
	boolean addManager(Manager manager, Store store) {
		if (manager.managedStore == null)
			return system.addManager(manager, store);
		return false;
	}
	
	boolean removeManager(Manager manager, Store store) {
		if (manager.managedStore != null)
			return system.removeManager(manager, store);
		return false; // true if their store is already null
	}
	
	boolean updateManager(Manager manager, Store oldStore, Store newStore) {
		return removeManager(manager, oldStore) && addManager(manager, newStore);
	}
	
	void changeMySelectedStore(Store newStore) {
		super.managedStore = newStore;
	}
}
