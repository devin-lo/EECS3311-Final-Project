package client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import base.User;
import corporate.Item;
import corporate.Store;

public class Customer extends User {
	Cart shoppingCart;
	public static final int BYNAME = 0;
	public static final int BYCATEGORY = 1;
	public static final String CARTERR = "Cart not initiated yet";
	
	public Customer(String username, String password) {
		super(username, password);
	}
	
	public List<Store> searchLocations(String address, int range) {
		return super.system.getStoresByLocation(address, range);
	}
	
	public Cart getCart() {
		if (shoppingCart != null) {
			updateCartAvail();
			return new Cart(this.shoppingCart);
		}
		return null;
	}
	
	@Override
	public String setPrefStore(Store store) {
		if (store == null) {
			return unsavePrefStore();
		}
		else {
			String result = "Set the preferred store.\n";
			super.setPrefStore(store);
			if (shoppingCart == null)
				shoppingCart = new Cart();
			else {
				result += shoppingCart.updateItemAvail();
			}
			return result;
		}
	}
	
	public String unsavePrefStore() {
		super.setPrefStore(null);
		shoppingCart = null; // delete the existing cart if pref store is unsaved
		return "Deleted preferred store from settings. (This also deletes your existing cart)";
	}
	
	public List<Item> search(String query, int searchType) {
		if (searchType == BYCATEGORY)
			return system.searchByCategory(getPrefStore(), query);
		else
			return system.searchByName(getPrefStore(), query);
	}
	
	public String getItemInfo(Item item) {
		StringBuilder result = new StringBuilder();
		result.append("Name: " + item.getName() + "\n");
		result.append("Description: " + item.getDescription() + "\n");
		result.append("Price: " + getPrefStore().getItemPrice(item) + "\n");
		result.append("Size: " + item.getSize() + "\n");
		result.append("Number available in your selected store: " + getPrefStore().getItemAvail(item));
		return result.toString();
	}
	
	public String addItemToCart(Item item) {
		if (shoppingCart != null)
			return shoppingCart.handle(1, item, 1);
		return CARTERR;
	}
	
	public String addItemToCart(Item item, int stock) {
		if (shoppingCart != null)
			return shoppingCart.handle(1, item, stock);
		return CARTERR;
	}
	
	public String removeItemFromCart(Item item) {
		if (shoppingCart != null)
			return shoppingCart.handle(2, item, 0);
		return CARTERR;
	}
	
	public String changeItemQuantityInCart(Item item, int newAmount) {
		if (shoppingCart != null)
			return shoppingCart.handle(3, item, newAmount - shoppingCart.getItemQuantity(item));
		return CARTERR;
	}
	
	public String updateCartAvail() {
		if (shoppingCart != null)
			return shoppingCart.updateItemAvail();
		return CARTERR;
	}
	
	public LinkedHashMap<Item, Double> getBestOrder(int time) {
		if (shoppingCart != null)
			return shoppingCart.makeBestOrder(time);
		return null;
	}
	
	public boolean isItemInCart(Item item) {
		if (shoppingCart != null)
			return shoppingCart.getItems().contains(item);
		return false;
	}
	
	public int quantityInCart(Item item) {
		if (shoppingCart != null)
			return shoppingCart.getItemQuantity(item);
		return 0;
	}
	
	public class Cart {
		Map<Item, Integer> cartItems;
		
		Cart() {
			cartItems = new HashMap<Item, Integer>();
		}
		
		Cart(Cart existingCart) {
			cartItems = new HashMap<Item, Integer>(existingCart.cartItems);
		}
		
		String handle(int type, Item item, int itemAmt) {
			boolean handled = false;
			if (type == 1) {
				handled = addItem(item, itemAmt);
				if (!handled)
					return "Item couldn't be added to the cart.";
			}
			else if (type == 2) {
				handled = removeItem(item);
				if (!handled)
					return "That item couldn't be removed from the cart.";
			}
			else {
				handled = updateItemAmount(item, itemAmt);
				if (!handled)
					return "Couldn't modify the quantity of that item in the cart.";
			}
			String result = updateItemAvail();
			if (result.isEmpty()) {
				if (type == 1)
					return "Item added successfully.";
				else if (type == 2)
					return "Item removed successfully.";
				else
					return "Item quantity modified successfully.";
			}
			return result;
		}
		
		String updateItemAvail() {
			String result = "";
			Set<Item> existingCartList = new HashSet<Item>(getItems());
			Store myStore = getPrefStore();
			for (Item i : existingCartList) {
				int inCart = cartItems.get(i);
				int actualAvail = myStore.getItemAvail(i);
				if (actualAvail < 0) { // actualAvail is -1 if the store doesn't have an Item in its itemsList
					result += "Item " + i.getName() + " is not sold at this store, so it was removed from the cart.\n";
					removeItem(i);
				}
				else if (inCart > actualAvail) {
					result += "Insufficient quantity of item " + i.getName() + "; maximum available is " + actualAvail + ". We adjusted your cart quantity to this amount.\n";
					updateItemAmount(i, actualAvail - inCart);
				}
			}
			return result;
		}
		
		boolean addItem(Item item, int stock) {
			if (cartItems.get(item) == null) {
				cartItems.put(item, stock);
				return true;
			}
			return updateItemAmount(item, stock); // if item's already in the cart, then update by the # added
		}
		
		boolean updateItemAmount(Item item, int changeBy) {
			Integer oldAmount = cartItems.get(item);
			if (cartItems.get(item) != null) {
				if (oldAmount + changeBy <= 0) {
					return removeItem(item);
				}
				else {
					cartItems.put(item, oldAmount + changeBy);
				}
				return true;
			}
			return false;
		}
		
		boolean removeItem(Item item) {
			Integer putBack = cartItems.remove(item);
			if (putBack != null) {
				return true;
			}
			return false;
		}
		
		public int getItemQuantity(Item item) {
			Integer amount = cartItems.get(item);
			if (amount == null)
				return 0;
			return amount;
		}
		
		LinkedHashMap<Item, Double> makeBestOrder(int time) {
			Store myStore = getPrefStore();
			LinkedHashMap<Item, Double> bestOrder = new LinkedHashMap<Item, Double>();
			LinkedList<String> categoryOrder = myStore.getCategories();
			Map<String, Double> categoryDensities = myStore.getDensities();
			Set<Item> itemList = getItems();
			
			for (String c : categoryOrder) {
				if (!(c.equals("entrance") || c.equals("cashier"))) {
					double density = categoryDensities.get(c);
					for (Item i : itemList) {
						String itemCat = myStore.getItemCategory(i);
						if (itemCat != null) {
							if (itemCat.equals(c)) {
								bestOrder.put(i, 1 * density * 0.5 * cartItems.get(i));
							}
						}
					}
				}
			}
			// Map<String, Map<Integer, Double>>
			return bestOrder;
		}
		
		public Set<Item> getItems() {
			return cartItems.keySet();
		}
	}
}
