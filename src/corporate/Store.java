package corporate;

import java.util.Set;

import exceptions.*;

import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;

public class Store {
	private Set<Manager> managerList; // can't have repeated managers
	private int openTime;
	private int closeTime;
	private Map<Item, ItemStoreAttributes> itemList;
	private Map<Item, Double> saleList; // can't have repeated items on sale
	private LinkedList<String> storeCategories;
	private String address;
	private ShopperSystem system;
	private Map<String, Double> categoryDensities;
	private static final int DEFAULTDENSITY = 1;
	// storeDensity
	
	private Store() {
		managerList = new HashSet<Manager>();
		itemList = new HashMap<Item, ItemStoreAttributes>();
		saleList = new HashMap<Item, Double>();
		storeCategories = new LinkedList<String>();
		storeCategories.addFirst("entrance");
		storeCategories.addLast("cashier");
		system = ShopperSystem.getInstance();
		categoryDensities = new HashMap<String, Double>();
	}
	
	protected Store(String address, int openTime, int closeTime) {
		this();
		this.address = address;
		this.openTime = openTime;
		this.closeTime = closeTime;
	}
	
	protected Set<Manager> getManagerList() {
		return new HashSet<Manager>(managerList);
	}
	
	// adding an item is different from updating an item
	// if you tried to add an existing item, then it won't allow you
	protected boolean addItem(Item item, int stock, String category) {
		ItemStoreAttributes theseAtt = null;
		try {
			theseAtt = new ItemStoreAttributes(stock, category.toLowerCase());
		}
		catch(Exception e) {
			return false;
		}
		if (itemList.get(item) == null) {
			itemList.put(item, theseAtt);
			return true;
		}
		return false;
	}
	
	protected boolean updateItemStock(Item item, int stock) {
		ItemStoreAttributes theseAtt = itemList.get(item);
		if (theseAtt != null && stock >= 0) {
			theseAtt.setStock(stock);
			return true;
		}
		return false;
	}
	
	protected boolean updateItemCategory(Item item, String category) {
		ItemStoreAttributes theseAtt = itemList.get(item);
		if (theseAtt != null) {
			String lowered = category.toLowerCase();
			if (!(lowered.equals("entrance") || lowered.equals("cashier")) && storeCategories.indexOf(lowered) != -1) {
				theseAtt.setCategory(lowered);
				return true;
			}
		}
		return false;
	}
	
	protected boolean removeItem(Item item) {
		if (itemList.remove(item) != null) {
			if (saleList.containsKey(item)) { // remove from sale list too if it's in there
				saleList.remove(item);
			}
			return true;
		}
		return false;
	}
	
	public Set<Item> getItemList() {
		return itemList.keySet();
	}
	
	public Item getItemByName(String name) {
		for (Item i : itemList.keySet()) {
			if (i.getName().equals(name))
				return i;
		}
		return null;
	}
	
	public int getItemAvail(Item item) {
		ItemStoreAttributes theseAtt = itemList.get(item);
		if (theseAtt != null) {
			return theseAtt.getStock();
		}
		return -1; // -1 would mean that the item is not sold at this store.
	}
	
	public String getItemCategory(Item item) {
		ItemStoreAttributes theseAtt = itemList.get(item);
		if (theseAtt != null) {
			return theseAtt.category;
		}
		return null; // null would mean that the item is not sold at this store.
	}
	
	public Set<Item> getSaleList() {
		return saleList.keySet();
	}
	
	// the item must be sold at the store to be on the sale list (i.e. must have an entry on itemList)
	// if you tried to add an existing item, then the reduction is changed accordingly
	protected boolean addToSaleList(Item item, double reduceBy) {
		if (itemList.get(item) != null && reduceBy >= 0 && reduceBy <= 1) {
			saleList.put(item, reduceBy);
			return true;
		}
		return false;
	}
	
	protected boolean updateInSaleList(Item item, double reduceBy) {
		if (itemList.get(item) != null && reduceBy >= 0 && reduceBy <= 1) {
			saleList.put(item, reduceBy);
			return true;
		}
		return false;
	}
	
	protected boolean remFromSaleList(Item item) {
		if (saleList.remove(item) != null)
			return true;
		return false;
	}
	
	protected boolean addManager(Manager newMan) {
		return managerList.add(newMan);
	}
	
	protected boolean removeManager(Manager oldMan) {
		return managerList.remove(oldMan);
	}
	
	protected boolean updateStoreOpenTime(int newTime) {
		if (newTime >= 0 && newTime < 1440 && newTime <= closeTime) {
			openTime = newTime;
			return true;
		}
		return false;
	}
	
	protected boolean updateStoreCloseTime(int newTime) {
		if (newTime >= 0 && newTime < 1440 && newTime >= openTime) {
			closeTime = newTime;
			return true;
		}
		return false;
	}
	
	public int getStoreOpenTime() {
		return this.openTime;
	}
	
	public int getStoreCloseTime() {
		return this.closeTime;
	}
	
	public double getItemPrice(Item item) {
		double price = item.getPrice();
		Double reduceBy = saleList.get(item);
		if (reduceBy != null) {
			price *= (1.0 - reduceBy);
		}
		return price;
	}
	
	public double getItemReduction(Item item) {
		Double reduceBy = saleList.get(item);
		if (reduceBy != null) {
			return reduceBy;
		}
		else {
			return 0.0;
		}
	}
	
	private class ItemStoreAttributes {
		private int stock;
		private String category;
		
		ItemStoreAttributes(int stock, String category) throws ItemStoreException {
			if (stock < 0)
				throw new InvalidStockException(stock + " is less than 0!");
			String lowered = category.toLowerCase();
			if (lowered.equals("entrance") || lowered.equals("cashier"))
				throw new InvalidCategoryException("Entrance and cashier are not valid category choices for placing items into");
			if (storeCategories.indexOf(category) == -1)
				throw new NonexistentCategoryException("The category " + category + "doesn't exist yet at this store");
			this.stock = stock;
			this.category = category;
		}
		
		private void setCategory(String categoryName) {
			this.category = categoryName;
		}
		
		public String getCategory() {
			return category;
		}
		
		private void setStock(int stock) {
			if (stock >= 0)
				this.stock = stock;
		}
		
		public int getStock() {
			return stock;
		}
	}
	
	protected boolean changeCategoryDensity(String category, double density) {
		String lowered = category.toLowerCase();
		if (storeCategories.indexOf(lowered) >= 0) {
			categoryDensities.put(lowered, density);
			return true;
		}
		return false;
	}
	
	private void removeCategoryDensity(String category) {
		categoryDensities.remove(category);
	}
	
	protected boolean addCategory(String category) {
		String lowered = category.toLowerCase();
		if (!(lowered.equals("entrance") || lowered.equals("cashier")) && storeCategories.indexOf(lowered) == -1) {
			storeCategories.add(storeCategories.indexOf("cashier"),lowered);
			changeCategoryDensity(lowered, DEFAULTDENSITY);
			return true;
		}
		return false;
	}
	
	protected boolean addCategory(String newCategory, String precedingCategory) {
		String loweredNew = newCategory.toLowerCase();
		String loweredEx = precedingCategory.toLowerCase();
		int exInd = storeCategories.indexOf(loweredEx);
		if (exInd >= 0 && !(loweredNew.equals("entrance") || loweredNew.equals("cashier")) && storeCategories.indexOf(loweredNew) == -1) {
			storeCategories.add(exInd+1, loweredNew);
			changeCategoryDensity(loweredNew, DEFAULTDENSITY);
			return true;
		}
		return false;
	}
	
	protected boolean removeCategory(String category) {
		String lowered = category.toLowerCase();
		if (lowered.equals("entrance") || lowered.equals("cashier")) {
			return false;
		}
		removeCategoryDensity(lowered);
		Map<Item, ItemStoreAttributes> cloned = new HashMap<Item, ItemStoreAttributes>(itemList);
		
		// also, all Items associated with the category will be removed.
		Set<Map.Entry<Item, ItemStoreAttributes>> entries = cloned.entrySet();
		for (Map.Entry<Item, ItemStoreAttributes> e : entries) {
			if (e.getValue().getCategory().equals(lowered))
				itemList.remove(e.getKey());
		}
		return storeCategories.remove(lowered);
	}
	
	public String getAddress() {
		return address;
	}
	
	protected void setAddress(String address) {
		this.address = address;
	}
	
	public LinkedList<String> getCategories() {
		return new LinkedList<String>(storeCategories);
	}
	
	protected boolean isItemInCategory(Item item, String categoryName) {
		return itemList.get(item).getCategory().equals(categoryName.toLowerCase());
	}
	
	public Map<String, Double> getDensities() {
		return new HashMap<String, Double>(categoryDensities);
	}
	
	public boolean hasItem(Item i) {
		ItemStoreAttributes a = itemList.get(i);
		if (a != null) {
			if (a.getStock() > 0) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isOnSale(Item i) {
		return (saleList.get(i) != null);
	}
	
	public Double getDensity(String category) {
		return categoryDensities.get(category.toLowerCase());
	}
	
	/*
	 * unused buyItem function
	protected void buyItem(Item item, int holdHowMany) {
		ItemStoreAttributes a = itemList.get(item);
		a.setStock(a.getStock() - holdHowMany);
	}
	 */
	
	@Override
	public String toString() {
		return getAddress();
	}
}
