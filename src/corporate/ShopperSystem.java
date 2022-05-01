package corporate;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale.Category;
import java.util.ArrayList;
import java.util.Comparator;

import base.User;
import client.Customer;
import exceptions.InvalidResponseType;
import exceptions.ItemStoreException;

public class ShopperSystem {
	// the volatile keyword - this was shown to me in EECS 2030
	private static volatile ShopperSystem instance = null; // the System is a singleton.
	private static volatile Map<String, User> users = null;
	private static volatile Set<Store> stores = null;
	private static volatile Set<Item> items = null;
	private static volatile int numAdmins;

	// if possible, replace the following with enum later
	public static final int CUSTOMER = 1;
	public static final int MANAGER = 2;
	public static final int ADMINISTRATOR = 3;

	private ShopperSystem() {
		// private default constructor
		users = new HashMap<String, User>();
		stores = new HashSet<Store>();
		items = new HashSet<Item>();
		numAdmins = 0;
	}

	protected User getUserByName(String username) {
		return users.get(username);
	}

	/**
	 * getInstance - obtain the reference to the single instance of the Singleton
	 * class
	 * 
	 * @return the single instance of the Singleton object
	 */
	public static ShopperSystem getInstance() {
		// check that there is no instance of the class created yet
		if (null == instance) {
			// synchronize thread access to shared data - ensure that only one instance is
			// made
			synchronized (ShopperSystem.class) {
				// double check that there is still no instance created yet
				if (null == instance) {
					// create the instance and also initialize the cache as a HashMap
					instance = new ShopperSystem();
					getData();
				}
			}
		}

		return instance;
	}

	// maybe I should return a Response class - type 1 is no user, type 2 is wrong
	// password, and type 3 is successful with User object contained.
	// 4.1 Req 1
	// 4.1 Req 3 - handle the response in the GUI (again, a good reason to return a
	// Response class)
	public Response loginUser(String username, String password) throws InvalidResponseType {
		User gotUser = users.get(username);
		if (gotUser != null) {
			if (gotUser.verifyPassword(password)) {
				if (gotUser.twoFactorAuth()) {
					if (gotUser.getClass().equals(Customer.class)) {
						Customer gotCustomer = (Customer) gotUser;
						if (gotCustomer.getCart() != null) // if the customer doesn't have a cart, don't do this.
							return new Response(Response.SUCCESS, gotUser, gotCustomer.updateCartAvail());
						// if User is a customer, update their cart's availability - 4.4.3 Req 3.1
					}
					// if User is a manager or admin, if their managed store got deleted then clear
					// that attribute!
					else if (gotUser.getClass().equals(Manager.class)
							|| gotUser.getClass().equals(Administrator.class)) {
						Manager gotMan = (Manager) gotUser;
						Store store = gotMan.getManagedStore();
						if (store != null && !stores.contains(store))
							gotMan.managedStore = null;
					}
					return new Response(Response.SUCCESS, gotUser);
				} else {
					return new Response(Response.TWOAUTHFAIL);
				}
			} else {
				return new Response(Response.WRONGPASS);
			}
		}
		return new Response(Response.NOUSER);
	}

	// 4.2 Req 1
	public boolean changeUsername(User existingUser, String newUsername) {
		if (users.get(newUsername) == null) {
			String oldUsername = existingUser.getUsername();
			if (users.get(oldUsername).equals(existingUser)) {
				users.remove(oldUsername);
				users.put(newUsername, existingUser);
				return true;
			}
		}
		return false;
	}

	// 4.1 Req 2
	// I decided that only Customer accounts can be created publicly. Admins can
	// create Manager and other Admins
	public boolean createCustomerAccount(String username, String password) {
		return createAccount(username, password, CUSTOMER);
	}

	// should I have a method to changeUserAccountType that only Admins can use?

	// the real createAccount can only be accessed by stuff in the corporate package
	// - because we can only have Administrator reaching this
	// this keeps the Factory pattern
	// 4.1 Req 2
	protected boolean createAccount(String username, String password, int accountType) {
		User newUser = null;
		if (users.get(username) == null) {
			if (accountType == CUSTOMER)
				newUser = new Customer(username, password);
			else if (accountType == MANAGER)
				newUser = new Manager(username, password);
			else if (accountType == ADMINISTRATOR)
				newUser = new Administrator(username, password);
			// or else don't do anything, invalid accountType argument

			// only put the newUser into the users map if the object was created
			// successfully
			if (newUser != null) {
				users.put(username, newUser);
				return true;
			}
		}

		return false; // if a user with that username already exists, or an invalid accountType
						// argument was used.
	}

	// 4.2 Req 4 - delete user
	public DeleteResponse removeUser(String remUsername) {
		DeleteResponse result = null;
		User gotUser = users.get(remUsername);

		try {
			if (gotUser != null) {
				if (gotUser.getClass() == Administrator.class && numAdmins == 1) {
					result = new DeleteResponse(DeleteResponse.ONEADMINLEFT);
				} else {
					User removed = users.remove(remUsername);
					if (removed.getClass().getName().equals(Manager.class.getName())
							|| removed.getClass().getName().equals(Administrator.class.getName())) {
						Manager rem = (Manager) removed;
						if (rem.managedStore != null)
							this.removeManager(rem, rem.managedStore);
						if (rem.getClass() == Administrator.class) {
							numAdmins--;
						}
					}
					result = new DeleteResponse(DeleteResponse.SUCCESS);
				}
			} else {
				result = new DeleteResponse(DeleteResponse.WRONGUSER);
			}
		} catch (InvalidResponseType e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	protected Set<String> getUserList() {
		return users.keySet();
	}

	// dummy method to make the two-factor auth thing work - it doesn't actually do
	// anything lol
	public boolean twoFactorAuth(String phone) {
		return true;
	}

	protected boolean addStore(Store store) {
		return stores.add(store);
	}

	protected boolean removeStore(Store store) {
		if (stores.contains(store)) {
			for (Manager m : store.getManagerList()) {
				store.removeManager(m);
				m.managedStore = null;
			}
		}
		for (Map.Entry<String, User> e : users.entrySet()) {
			User u = e.getValue();
			if (u.getPrefStore() == store) {
				if (u.getClass() == Customer.class) {
					Customer c = (Customer) u;
					c.unsavePrefStore();
				} else {
					u.setPrefStore(null);
				}
			}
		}
		return stores.remove(store);
	}

	public Set<Store> getStores() {
		return new HashSet<Store>(stores);
	}

	protected boolean addItem(Item item) {
		return items.add(item);
	}

	protected boolean removeItem(Item item) {
		if (items.contains(item)) {
			for (Store s : stores) {
				s.removeItem(item);
			}
			for (String s : users.keySet()) {
				User u = users.get(s);
				if (u.getClass() == Customer.class) {
					Customer c = (Customer) u;
					if (c.getCart() != null)
						c.removeItemFromCart(item);
				}
			}
		}
		return items.remove(item);
	}

	protected Set<Item> getSystemItemList() {
		return new HashSet<Item>(items);
	}

	protected boolean addManager(Manager manager, Store store) {
		boolean didIt = store.addManager(manager);
		if (didIt) {
			manager.managedStore = store;
			return true;
		}
		return false;
	}

	protected boolean removeManager(Manager manager, Store store) {
		boolean didIt = store.removeManager(manager);
		if (didIt) {
			manager.managedStore = null;
			return true;
		}
		return false;
	}

	public List<Store> getStoresByLocation(String address, int range) {
		// int range is not actually used
		List<Store> result = new ArrayList<Store>();
		Set<Store> unsorted = new HashSet<Store>();
		for (Store s : stores) {
			if (s.getAddress().toLowerCase().contains(address.toLowerCase()))
				unsorted.add(s);
		}

		// need to sort the search result by how lexographically similar the item names
		// are
		// I do this using a Priority Queue
		PriorityQueue<NodeStore> pq = new PriorityQueue<NodeStore>(new NodeStoreComparator());
		for (Store s : unsorted) {
			NodeStore n = new NodeStore(s, Math.abs(s.getAddress().toLowerCase().compareTo(address.toLowerCase())));
			pq.add(n);
		}

		while (!(pq.isEmpty())) {
			result.add(pq.poll().getVal());
		}

		return result;
	}

	// https://www.javatpoint.com/java-integer-compareto-method
	class NodeItem {
		Item value;
		Integer similarity;

		NodeItem(Item val, int sim) {
			value = val;
			similarity = sim;
		}

		Item getVal() {
			return value;
		}

		Integer getSim() {
			return similarity;
		}
	}

	class NodeStore {
		Store value;
		Integer similarity;

		NodeStore(Store val, int sim) {
			value = val;
			similarity = sim;
		}

		Store getVal() {
			return value;
		}

		Integer getSim() {
			return similarity;
		}
	}

	// https://www.geeksforgeeks.org/implement-priorityqueue-comparator-java/
	class NodeComparator implements Comparator<NodeItem> {
		@Override
		public int compare(NodeItem n1, NodeItem n2) {
			return n1.getSim().compareTo(n2.getSim());
		}
	}

	// https://www.geeksforgeeks.org/implement-priorityqueue-comparator-java/
	class NodeStoreComparator implements Comparator<NodeStore> {
		@Override
		public int compare(NodeStore n1, NodeStore n2) {
			return n1.getSim().compareTo(n2.getSim());
		}
	}

	public List<Item> searchByName(Store store, String itemName) {
		List<Item> searchResult = new ArrayList<Item>();

		// need to sort the search result by how lexographically similar the item names
		// are
		// I do this using a Priority Queue
		PriorityQueue<NodeItem> pq = new PriorityQueue<NodeItem>(new NodeComparator());

		Set<Item> itemsHere = store.getItemList();
		for (Item i : itemsHere) {
			if (i.getName().toLowerCase().contains(itemName)) {
				NodeItem n = new NodeItem(i, Math.abs(i.getName().compareTo(itemName)));
				pq.add(n);
				i.increaseSearchFreq();
			}
		}

		while (!(pq.isEmpty())) {
			searchResult.add(pq.poll().getVal());
		}

		return searchResult;
	}

	public List<Item> searchByCategory(Store store, String categoryName) {
		String lowered = categoryName.toLowerCase();
		List<Item> searchResult = new ArrayList<Item>();

		// need to sort the search result by how lexographically similar the category
		// names are
		// I do this using a Priority Queue
		PriorityQueue<NodeItem> pq = new PriorityQueue<NodeItem>(new NodeComparator());

		Set<Item> itemsHere = store.getItemList();

		for (Item i : itemsHere) {
			String cats = store.getItemCategory(i);
			if (cats.contains(lowered)) {
				NodeItem n = new NodeItem(i, Math.abs(cats.compareTo(lowered)));
				pq.add(n);
				i.increaseSearchFreq();
			}
		}

		while (!(pq.isEmpty())) {
			searchResult.add(pq.poll().getVal());
		}

		return searchResult;
	}

	/**
	 * 
	 * @param user
	 * @return an ordered list of suggested items, or null if the Customer has no
	 *         pref store or cart
	 */
	public List<Item> suggestedItems(Customer user) {
		List<Item> suggested = null;
		Map<Item, Double> itemScore = new HashMap<Item, Double>();
		Store myStore = user.getPrefStore();
		Set<Item> myCart = null;
		if (user.getCart() != null) {
			myCart = user.getCart().getItems();
		}
		if (myStore != null) {
			Set<Item> storeSale = myStore.getSaleList();
			for (Item i : storeSale) {
				itemScore.put(i, 2.0);
			}

			if (myCart != null) {
				for (User u : users.values()) {
					if (!(u.equals(user)) && u.getClass() == Customer.class) {
						Customer c = (Customer) u;
						if (c.getPrefStore() != null && c.getPrefStore() == myStore) { // ONLY look at the carts of other customers at that store
							if (c.getCart() != null) {
								Set<Item> theirCart = c.getCart().getItems();
								boolean b = false;
								for (Item i : theirCart) {
									if (myCart.contains(i))
										b = true;
								}
								// only look at the others' cart if my cart has at least one common item
								if (b) {
									for (Item i : theirCart) {
										if (!(myCart.contains(i)) && myStore.hasItem(i)) {
											double score = 3.0;
											if (itemScore.containsKey(i)) {
												score += itemScore.get(i);
											}
											itemScore.put(i, score);
										}
									}
								}
							}
						}
					}
				}
			}

			Set<Item> storeItems = myStore.getItemList();
			for (Item i : storeItems) {
				int freq = i.getSearchFreq();
				double score = freq * 0.01;
				if (itemScore.containsKey(i)) {
					score += itemScore.get(i);
				}
				// only put an item if it has any search score
				if (score > 0.0)
					itemScore.put(i, score);
			}

			// https://stackoverflow.com/questions/30425836/java-8-stream-map-to-list-of-keys-sorted-by-values
			suggested = itemScore.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getValue))
					.map(Map.Entry::getKey).collect(Collectors.toList());
		}

		return suggested;
	}

	/*
	 * unused buyItemFromStore function public void buyItemFromStore(Store store,
	 * Item item, int changeBy) { store.buyItem(item, changeBy); }
	 */

	protected void addAdmin() {
		numAdmins++;
	}

	public Store getStoreByExactAddress(String address) {
		Store result = null;
		for (Store s : stores) {
			if (s.getAddress().equals(address))
				return s;
		}
		return result;
	}

	public Item getItemByName(String itemName) {
		for (Item i : items) {
			if (i.getName().equals(itemName))
				return i;
		}
		return null;
	}

	/**
	 * Hardcoded default data to have in the system at initialization
	 */
	private static void getData() {
		Administrator defaultAdmin = new Administrator("admin", "password");
		users.put("admin", defaultAdmin);

		Manager torMan1 = new Manager("tor1", "password");
		users.put("tor1", torMan1);

		Manager torMan2 = new Manager("tor2", "password");
		users.put("tor2", torMan2);

		Manager monMan = new Manager("mon", "password");
		users.put("mon", monMan);

		Manager calgMan = new Manager("calg", "password");
		users.put("calg", calgMan);

		Manager yorkMan = new Manager("york", "password");
		users.put("york", yorkMan);

		Manager northYorkMan = new Manager("northyork", "password");
		users.put("northyork", northYorkMan);

		Manager scarbMan1 = new Manager("scarb1", "password");
		users.put("scarb1", scarbMan1);

		Manager scarbMan2 = new Manager("scarb2", "password");
		users.put("scarb2", scarbMan2);

		Manager hkMan = new Manager("hk", "password");
		users.put("hk", hkMan);

		Manager tokyoMan = new Manager("tokyo", "password");
		users.put("tokyo", tokyoMan);

		Manager seoulMan = new Manager("seoul", "password");
		users.put("seoul", seoulMan);

		Manager parisMan = new Manager("paris", "password");
		users.put("paris", parisMan);

		Item i1 = new Item("Guitar");
		i1.setPrice(2000.0);
		i1.setSize(23.0);
		i1.setDescription("Fender Stratocaster");

		Item i2 = new Item("MacBook Pro");
		i2.setPrice(4000.0);
		i2.setSize(17.0);
		i2.setDescription("2011 Macbook Pro 17 inch");

		Item i3 = new Item("Beans");
		i3.setPrice(3.0);
		i3.setSize(25.0);
		i3.setDescription("25 oz can of beans");

		Item i4 = new Item("Beanie");
		i4.setPrice(15.0);
		i4.setSize(2.0);
		i4.setDescription(null);

		Item i5 = new Item("Beanbag Chair");
		i5.setPrice(10.0);

		Item i6 = new Item("Broccoli");
		i6.setPrice(4.0);
		i6.setSize(2.0);
		i6.setDescription("Organic Broccoli, $4 per lb");

		Item i7 = new Item("Super Popular Thing");
		i7.setPrice(40.0);
		for (int i = 0; i < 50; i++) {
			i7.increaseSearchFreq();
		}

		Item i8 = new Item("Freebie");
		for (int i = 0; i < 20; i++) {
			i8.increaseSearchFreq();
		}
		
		Item i9 = new Item("Test Item");
		i9.setPrice(35.0);
		i9.setSize(2.0);
		i9.setDescription("This is a test item");
		
		Item i10 = new Item("Blah");

		items.add(i1);
		items.add(i2);
		items.add(i3);
		items.add(i4);
		items.add(i5);
		items.add(i6);
		items.add(i7);
		items.add(i8);
		items.add(i9);
		items.add(i10);

		Store branch1 = new Store("Toronto", 420, 1380);
		branch1.addManager(torMan1);
		torMan1.managedStore = branch1;
		branch1.addManager(torMan2);
		torMan2.managedStore = branch1;
		branch1.addCategory("Instruments");
		branch1.addCategory("Grocery", "Instruments");
		branch1.addCategory("Other");
		branch1.addItem(i1, 3, "Instruments");
		branch1.addItem(i2, 5, "Other");
		branch1.addItem(i3, 50, "Grocery");
		branch1.addItem(i7, 30, "Other");

		Store branch2 = new Store("Montreal", 420, 1380);
		branch2.addManager(monMan);
		monMan.managedStore = branch2;
		branch2.addCategory("Grocery");
		branch2.addCategory("Furniture");
		branch2.addCategory("Clothes", "Grocery");
		branch2.addItem(i3, 20, "Grocery");
		branch2.addItem(i4, 10, "Clothes");
		branch2.addItem(i5, 5, "Furniture");
		branch2.addItem(i6, 30, "Grocery");

		Store branch3 = new Store("Calgary", 420, 1380);
		branch3.addManager(calgMan);
		calgMan.managedStore = branch3;
		branch3.addCategory("Instruments");
		branch3.addCategory("Grocery", "Instruments");
		branch3.addCategory("Other");
		branch3.addItem(i1, 5, "Instruments");
		branch3.addItem(i2, 1, "Other");
		branch3.addItem(i3, 10, "Grocery");
		branch3.addToSaleList(i3, 0.1);

		Store branch4 = new Store("York", 540, 1200);
		branch4.addManager(yorkMan);
		yorkMan.managedStore = branch4;
		branch4.addCategory("Everything");
		branch4.addItem(i1, 3, "Everything");
		branch4.addItem(i2, 3, "Everything");
		branch4.addItem(i3, 3, "Everything");
		branch4.addItem(i4, 3, "Everything");
		branch4.addItem(i5, 3, "Everything");
		branch4.addItem(i6, 3, "Everything");
		branch4.addItem(i7, 3, "Everything");
		branch4.addItem(i8, 30, "Everything");
		branch4.addItem(i9, 320, "Everything");
		branch4.addToSaleList(i1, 0.1);
		branch4.addToSaleList(i3, 0.2);
		branch4.addToSaleList(i2, 0.15);
		branch4.addToSaleList(i6, 0.05);

		Store branch5 = new Store("North York", 570, 1140);
		branch5.addManager(northYorkMan);
		northYorkMan.managedStore = branch5;
		branch5.addCategory("Board Games");
		branch5.addCategory("Video Games");
		branch5.addItem(i2, 50, "Video Games");
		branch5.addItem(i6, 1, "Board Games");
		branch5.addItem(i1, 10, "Board Games");

		Store branch6 = new Store("Scarborough", 570, 1140);
		branch6.addManager(scarbMan1);
		scarbMan1.managedStore = branch6;
		branch6.addManager(scarbMan2);
		scarbMan2.managedStore = branch6;
		branch6.addCategory("Groceries");
		branch6.addCategory("Clothing");
		branch6.addItem(i3, 50, "Groceries");
		branch6.addItem(i4, 20, "Clothing");
		branch6.addItem(i6, 10, "Groceries");
		branch6.changeCategoryDensity("Groceries", 1.2);
		branch6.changeCategoryDensity("Clothing", 0.9);

		Store branch7 = new Store("Hong Kong", 420, 1380);
		branch7.addManager(hkMan);
		hkMan.managedStore = branch7;
		branch7.addCategory("Everything");
		branch7.addItem(i1, 100, "Everything");
		branch7.addItem(i2, 100, "Everything");
		branch7.addItem(i3, 100, "Everything");
		branch7.addItem(i4, 100, "Everything");
		branch7.addItem(i5, 100, "Everything");
		branch7.addItem(i6, 100, "Everything");
		branch7.addItem(i7, 100, "Everything");
		branch7.addItem(i8, 100, "Everything");

		Store branch8 = new Store("Tokyo", 420, 1380);
		branch8.addManager(tokyoMan);
		tokyoMan.managedStore = branch8;

		Store branch9 = new Store("Seoul", 420, 1380);
		branch9.addManager(seoulMan);
		seoulMan.managedStore = branch9;

		Store branch10 = new Store("Paris", 420, 1380);
		branch10.addManager(parisMan);
		parisMan.managedStore = branch10;
		branch10.addCategory("Grocery");
		branch10.addCategory("Furniture");
		branch10.addCategory("Clothes", "Grocery");
		branch10.addItem(i3, 20, "Grocery");
		branch10.addItem(i4, 10, "Clothes");
		branch10.addItem(i5, 5, "Furniture");
		branch10.changeCategoryDensity("Grocery", 1.2);
		branch10.changeCategoryDensity("Clothes", 0.9);

		stores.add(branch1);
		stores.add(branch2);
		stores.add(branch3);
		stores.add(branch4);
		stores.add(branch5);
		stores.add(branch6);
		stores.add(branch7);
		stores.add(branch8);
		stores.add(branch9);
		stores.add(branch10);

		Customer abd = new Customer("abd", "abd");
		users.put("abd", abd);

		Customer test1 = new Customer("test", "password");
		users.put("test", test1);
		test1.setPrefStore(branch4);
		test1.addItemToCart(i4);
		test1.addItemToCart(i2);
		test1.addItemToCart(i8, 3);
		test1.addItemToCart(i9);

		Customer test2 = new Customer("who", "password");
		users.put("who", test2);
		test2.setPrefStore(branch4);
		test2.addItemToCart(i4);
		test2.setTwoFactor("password", "4161234567");

		Customer test3 = new Customer("me", "password");
		users.put("me", test3);
		test3.setPrefStore(branch2);
		test3.addItemToCart(i3);
		test3.addItemToCart(i4);
		test3.addItemToCart(i5);
		test3.addItemToCart(i6);

		Customer test4 = new Customer("thicc", "password");
		users.put("thicc", test4);
		test4.setPrefStore(branch6);
		test4.addItemToCart(i3, 10);
		test4.addItemToCart(i4, 2);
		test4.addItemToCart(i6, 3);

		Customer test5 = new Customer("superthicc", "password");
		users.put("superthicc", test5);
		test5.setPrefStore(branch7);
		test5.addItemToCart(i1, 50);
		test5.addItemToCart(i2, 50);
		test5.addItemToCart(i3, 50);
		test5.addItemToCart(i4, 50);
		test5.addItemToCart(i5, 50);
		test5.addItemToCart(i6, 50);
		test5.addItemToCart(i7, 50);
		test5.addItemToCart(i8, 50);

		Manager test6 = new Manager("mscott", "password");
		users.put("mscott", test6);
		
		Customer test7 = new Customer("abcdefghi", "password");
		users.put("abcdefghi", test7);
		
		Customer test8 = new Customer("frank", "password");
		users.put("frank", test8);
		test8.setPrefStore(branch7);
		
		Customer test9 = new Customer("test123", "password");
		users.put("test123", test9);
		test9.setPrefStore(branch4);
		test9.addItemToCart(i2);
		test9.addItemToCart(i8, 3);
		test9.addItemToCart(i5);
		
		Customer test10 = new Customer("ordered", "password");
		users.put("ordered", test10);
		test10.setPrefStore(branch10);
		test10.addItemToCart(i3, 10);
		test10.addItemToCart(i4, 2);
		test10.addItemToCart(i5, 3);
		
		Manager test11 = new Manager("deleteMe", "password");
		users.put("deleteMe", test11);
	}
}
