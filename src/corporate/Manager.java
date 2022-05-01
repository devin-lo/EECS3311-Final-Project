package corporate;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import base.User;
import exceptions.ItemStoreException;

public class Manager extends User {
	protected Store managedStore;
	
	protected Manager(String username, String password) {
		super(username, password);
		this.managedStore = null; // note: a new manager is not assigned to a store yet
	}
	
	@Override
	public Map<String, String> getAccountInfo(String checkPW) {
		Map<String, String> accountInfo = super.getAccountInfo(checkPW);
		if (managedStore != null)
			accountInfo.put("Managed Store", managedStore.toString());
		else
			accountInfo.put("Managed Store", "N/A");
		
		return accountInfo;
	}
	
	@Override
	public Store getPrefStore() {
		return managedStore;
	}
	
	public Store getManagedStore() {
		return managedStore;
	}
	
	protected boolean addItem(Item item, String category) {
		return this.addItem(item, 0, category);
	}
	
	protected boolean addItem(Item item, int stock, String category) {
		if (managedStore != null)
			return managedStore.addItem(item, stock, category);
		return false;
	}
	
	protected boolean removeItem(Item item) {
		if (managedStore != null)
			return managedStore.removeItem(item);
		return false;
	}
	
	protected boolean updateItemStock(Item item, int stock) {
		if (managedStore != null)
			return managedStore.updateItemStock(item, stock);
		return false;
	}
	
	protected boolean updateItemCategory(Item item, String category) {
		if (managedStore != null)
			return managedStore.updateItemCategory(item, category);
		return false;
	}
	
	protected Set<Item> getSaleList() {
		if (managedStore != null)
			return managedStore.getSaleList();
		return null;
	}
	
	protected Set<Item> getItemList() {
		if (managedStore != null)
			return managedStore.getItemList();
		return null;
	}
	
	protected boolean addToSaleList(Item item, double reduceBy) {
		if (managedStore != null)
			return managedStore.addToSaleList(item, reduceBy);
		return false;
	}
	
	protected boolean remFromSaleList(Item item) {
		if (managedStore != null)
			return managedStore.remFromSaleList(item);
		return false;
	}
	
	protected boolean updateInSaleList(Item item, double reduceBy) {
		if (managedStore != null)
			return managedStore.updateInSaleList(item, reduceBy);
		return false;
	}
	
	protected boolean updateStoreOpenTime(int newTime) {
		if (managedStore != null)
			return managedStore.updateStoreOpenTime(newTime);
		return false;
	}
	
	protected boolean updateStoreCloseTime(int newTime) {
		if (managedStore != null)
			return managedStore.updateStoreCloseTime(newTime);
		return false;
	}
	
	protected boolean addCategory(String category) {
		if (managedStore != null)
			return managedStore.addCategory(category);
		return false;
	}
	
	protected boolean addCategory(String newCategory, String precedingCategory) {
		if (managedStore != null)
			return managedStore.addCategory(newCategory, precedingCategory);
		return false;
	}
	
	protected boolean removeCategory(String category) {
		if (managedStore != null)
			return managedStore.removeCategory(category);
		return false;
	}
	
	protected boolean updateDensity(String category, double density) {
		if (managedStore != null)
			return managedStore.changeCategoryDensity(category, density);
		return false;
	}
}