package corporate;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;

import base.User;
import client.Customer;
import corporate.ManagerGUIHome.SaleItemModel;
import corporate.ManagerGUIHome.StoreItemModel;
import gui.ChangePassword;
import gui.ChangeTFA;
import gui.ChangeUsername;
import gui.CustomerChooseStore;
import gui.DeleteAccountPage;
import gui.UtilitiesGUI;
import gui.WelcomePage;

import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.NumberFormatter;
import javax.swing.JTextField;
import javax.swing.JTextArea;

public class AdminGUIHome {

	private JFrame frAdminHome;
	private Administrator user;
	private ShopperSystem system;
	private Store manStore;
	private Item sysItemInterested;
	private Item storeItemInterested;
	private User userInterested;

	private JLabel usernameLabel_DETAILS;
	private JLabel userTypeLabel;
	private JLabel manStoreLabel_DETAILS;
	
	private JLabel usernameLabel;
	private JLabel twoFactorLabel;
	private JLabel manStoreLabel;
	private JLabel searchFreqLabel;
	private JTextArea sysDescField;
	
	private JPanel userDetailPanel;
	private DefaultListModel<String> storeModel;
	private DefaultListModel<String> sysItemModel;
	
	private JPanel sysItemDetailPanel;
	
	// manager elements
	private JTable storeItemTable;
	private JTable saleItemTable;
	private JLabel itemNameLabel;
	private JLabel descriptionLabel;
	private JLabel sizeLabel;
	private JLabel priceLabel;
	private JLabel currStockLabel;
	private JLabel currCategoryLabel;
	private JLabel saleStatusLabel;
	private JFormattedTextField openTimeField;
	private JFormattedTextField closeTimeField;

	private StoreItemModel storeItemModel;
	private SaleItemModel saleItemModel;
	private DefaultListModel<String> catModel;
	private DefaultListModel<String> catItemModel;
	private JList<String> catList;
	private JList<String> itemsInCatList;
	private JComboBox<String> categoryComboBox;
	private UserListModel userListModel;
	private UserListModel manListModel;
	private DefaultListModel<String> manStoreListModel;
	private JList<String> managerStoreList;

	private JTabbedPane tabbedPane;
	private JTable fullUserTable;
	private JTable managerTable;

	// manager panels
	private JPanel storeItemPanel;
	private JPanel saleListPanel;
	private JPanel storePropertiesPanel;
	private JPanel categoryDetailPanel;
	private JPanel storeItemDetailsPanel;
	private JTextField sysItemNameField;
	private JTextField sysSizeField;
	private JTextField sysPriceField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminGUIHome window = new AdminGUIHome();
					window.frAdminHome.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AdminGUIHome() {
		system = ShopperSystem.getInstance();
		storeItemModel = new StoreItemModel();
		saleItemModel = new SaleItemModel();
		catModel = new DefaultListModel<String>();
		catItemModel = new DefaultListModel<String>();
		userListModel = new UserListModel();
		manListModel = new UserListModel();
		storeModel = new DefaultListModel<String>();
		sysItemModel = new DefaultListModel<String>();
		manStoreListModel = new DefaultListModel<String>();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frAdminHome = new JFrame();
		frAdminHome.setTitle("SmartShoppers Administration Access Panel");
		frAdminHome.setBounds(100, 100, 1000, 600);
		frAdminHome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frAdminHome.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		// logout menu
		JMenuBar menuBar = new JMenuBar();
		frAdminHome.setJMenuBar(menuBar);
		JMenu logoutMenu = new JMenu("Logout");
		menuBar.add(logoutMenu);
		JMenuItem logoutMenuItem = new JMenuItem("Logout", KeyEvent.VK_L);
		logoutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frAdminHome.dispose();
				WelcomePage welcome = new WelcomePage();
				welcome.setVisible(true);
				frAdminHome.setVisible(false);
			}
		});
		logoutMenu.add(logoutMenuItem);
		
		// number formatters
		// https://stackoverflow.com/a/16228698
		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(999);
		formatter.setAllowsInvalid(false);

		// https://stackoverflow.com/a/16228698
		NumberFormatter doubleFormatter = new NumberFormatter(format);
		doubleFormatter.setValueClass(Integer.class);
		doubleFormatter.setMinimum(0);
		doubleFormatter.setMaximum(100);
		doubleFormatter.setAllowsInvalid(false);
		
		// time input box formatter
		// http://www.java2s.com/Tutorial/Java/0240__Swing/FormattedDateandTimeInputDateFormatgetTimeInstanceDateFormatSHORT.htm
		Format shortTime = DateFormat.getTimeInstance(DateFormat.SHORT);

		// full user list
		JPanel userListPanel = new JPanel();
		tabbedPane.addTab("Full User List", null, userListPanel, "See and modify all Users here");

		JButton adminDelUserButton = new JButton("Delete User"); // NOTE - cannot delete yourself here
		JButton adminAddUserButton = new JButton("Add a User...");
		JButton adminViewDetailsUserButton = new JButton("View User Details");

		JScrollPane fullUserTableScroll = new JScrollPane();
		
		GroupLayout gl_userListPanel = new GroupLayout(userListPanel);
		gl_userListPanel.setHorizontalGroup(gl_userListPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_userListPanel.createSequentialGroup().addContainerGap().addGroup(gl_userListPanel
						.createParallelGroup(Alignment.LEADING)
						.addComponent(fullUserTableScroll, GroupLayout.DEFAULT_SIZE, 959, Short.MAX_VALUE)
						.addGroup(gl_userListPanel.createSequentialGroup().addComponent(adminDelUserButton).addGap(18)
								.addComponent(adminAddUserButton).addGap(18).addComponent(adminViewDetailsUserButton)))
						.addContainerGap()));
		gl_userListPanel.setVerticalGroup(gl_userListPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_userListPanel.createSequentialGroup().addContainerGap()
						.addComponent(fullUserTableScroll, GroupLayout.PREFERRED_SIZE, 408, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
						.addGroup(gl_userListPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(adminDelUserButton).addComponent(adminAddUserButton)
								.addComponent(adminViewDetailsUserButton))
						.addContainerGap()));

		fullUserTable = new JTable(userListModel);
		fullUserTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fullUserTableScroll.setViewportView(fullUserTable);
		userListPanel.setLayout(gl_userListPanel);
		// full user table contents are initialized at the end of initialize in separate fcn

		// manager list panel
		JPanel managerListPanel = new JPanel();
		tabbedPane.addTab("Manager List", null, managerListPanel, "Add, remove, modify Managers here");

		JScrollPane managerScroll = new JScrollPane();

		JButton adminAddManButton = new JButton("Add Manager");
		JButton adminDelManButton = new JButton("Remove Manager from System");
		JButton adminViewManDetailsButton = new JButton("View Manager Details");
		JButton mgrRemFromStoreListButton = new JButton("Remove Manager from their Store");
		
		
		GroupLayout gl_managerListPanel = new GroupLayout(managerListPanel);
		gl_managerListPanel.setHorizontalGroup(
			gl_managerListPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_managerListPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_managerListPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(managerScroll, GroupLayout.DEFAULT_SIZE, 959, Short.MAX_VALUE)
						.addGroup(gl_managerListPanel.createSequentialGroup()
							.addComponent(adminAddManButton)
							.addGap(18)
							.addComponent(mgrRemFromStoreListButton)
							.addGap(18)
							.addComponent(adminDelManButton)
							.addGap(18)
							.addComponent(adminViewManDetailsButton)))
					.addContainerGap())
		);
		gl_managerListPanel.setVerticalGroup(
			gl_managerListPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_managerListPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(managerScroll, GroupLayout.PREFERRED_SIZE, 349, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_managerListPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(adminAddManButton)
						.addComponent(mgrRemFromStoreListButton)
						.addComponent(adminDelManButton)
						.addComponent(adminViewManDetailsButton))
					.addContainerGap(110, Short.MAX_VALUE))
		);

		managerTable = new JTable(manListModel);
		managerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		managerScroll.setViewportView(managerTable);
		managerListPanel.setLayout(gl_managerListPanel);

		// manager table contents are initialized at the end of initialize in separate fcn

		// user details
		userDetailPanel = new JPanel();
		// uncomment to view user details in design tab
		// tabbedPane.addTab("User Details", null, userDetailPanel, "See and modify details of a user here");
		
		JLabel lblNewLabel_5_1 = new JLabel("Username:");
		usernameLabel_DETAILS = new JLabel("usernamehere");
		JLabel lblNewLabel_21 = new JLabel("User Type:");
		userTypeLabel = new JLabel("userTypeHere");

		JLabel lblNewLabel_6_1 = new JLabel("Associated Store:");
		manStoreLabel_DETAILS = new JLabel("manStoreHere");
		JButton changeUserStoreButton = new JButton("Change their Store");
		JButton resetUserStoreButton = new JButton("Remove from Store");
		JButton delAccountDetailsButton = new JButton("Delete Account");
		
		GroupLayout gl_userDetailPanel = new GroupLayout(userDetailPanel);
		gl_userDetailPanel.setHorizontalGroup(
			gl_userDetailPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_userDetailPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_userDetailPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(delAccountDetailsButton)
						.addGroup(gl_userDetailPanel.createSequentialGroup()
							.addGroup(gl_userDetailPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_userDetailPanel.createSequentialGroup()
									.addGroup(gl_userDetailPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblNewLabel_6_1, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE)
										.addComponent(changeUserStoreButton))
									.addGap(18))
								.addComponent(lblNewLabel_5_1, GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE))
							.addGroup(gl_userDetailPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(manStoreLabel_DETAILS, GroupLayout.PREFERRED_SIZE, 210, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_userDetailPanel.createSequentialGroup()
									.addGap(39)
									.addGroup(gl_userDetailPanel.createParallelGroup(Alignment.TRAILING)
										.addComponent(resetUserStoreButton)
										.addGroup(gl_userDetailPanel.createSequentialGroup()
											.addComponent(usernameLabel_DETAILS, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
											.addGap(18)
											.addComponent(lblNewLabel_21)))))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(userTypeLabel, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)))
					.addGap(483))
		);
		gl_userDetailPanel.setVerticalGroup(
			gl_userDetailPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_userDetailPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_userDetailPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_userDetailPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblNewLabel_21)
							.addComponent(userTypeLabel)
							.addComponent(usernameLabel_DETAILS))
						.addComponent(lblNewLabel_5_1))
					.addGap(117)
					.addGroup(gl_userDetailPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_6_1)
						.addComponent(manStoreLabel_DETAILS))
					.addGap(18)
					.addGroup(gl_userDetailPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(changeUserStoreButton)
						.addComponent(resetUserStoreButton))
					.addGap(140)
					.addComponent(delAccountDetailsButton)
					.addContainerGap(135, Short.MAX_VALUE))
		);
		userDetailPanel.setLayout(gl_userDetailPanel);
		
		// system items panel
		JPanel sysItemListPanel = new JPanel();
		tabbedPane.addTab("System Items List", null, sysItemListPanel,
				"Add, remove, modify all Items in the system here");
		
		JScrollPane sysItemScroll = new JScrollPane();
		
		JButton sysItemAddButton = new JButton("Add New Item");
		JButton sysItemRemButton = new JButton("Remove Item");
		JButton sysItemViewButton = new JButton("View Item Details");
		
		GroupLayout gl_sysItemListPanel = new GroupLayout(sysItemListPanel);
		gl_sysItemListPanel.setHorizontalGroup(
			gl_sysItemListPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_sysItemListPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_sysItemListPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(sysItemScroll, GroupLayout.DEFAULT_SIZE, 959, Short.MAX_VALUE)
						.addGroup(gl_sysItemListPanel.createSequentialGroup()
							.addComponent(sysItemAddButton)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(sysItemRemButton)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(sysItemViewButton)))
					.addContainerGap())
		);
		gl_sysItemListPanel.setVerticalGroup(
			gl_sysItemListPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_sysItemListPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(sysItemScroll, GroupLayout.PREFERRED_SIZE, 375, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_sysItemListPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(sysItemAddButton)
						.addComponent(sysItemRemButton)
						.addComponent(sysItemViewButton))
					.addContainerGap(68, Short.MAX_VALUE))
		);
		
		JList<String> sysItemList = new JList<String>(sysItemModel);
		sysItemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sysItemScroll.setViewportView(sysItemList);
		sysItemListPanel.setLayout(gl_sysItemListPanel);
		Set<Item> items = system.getSystemItemList();
		for (Item i : items) {
			sysItemModel.add(sysItemModel.size(), i.getName());
		}

		// system item details (for editing name, desc, etc.)
		sysItemDetailPanel = new JPanel();
		// uncomment to show system item details (for editing name, desc, etc.)
		// tabbedPane.addTab("System Item Details", null, sysItemDetailPanel, null);
		
		JLabel lblNewLabel_8_1 = new JLabel("Item Name:");
		lblNewLabel_8_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JLabel lblNewLabel_9_1 = new JLabel("Description:");
		lblNewLabel_9_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JLabel lblNewLabel_10_1 = new JLabel("Size:");
		lblNewLabel_10_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JLabel lblNewLabel_11_1 = new JLabel("Unit Price:");
		lblNewLabel_11_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		sysItemNameField = new JTextField();
		sysItemNameField.setColumns(10);
		
		sysSizeField = new JTextField();
		sysSizeField.setHorizontalAlignment(SwingConstants.RIGHT);
		sysSizeField.setColumns(10);
		
		sysPriceField = new JTextField();
		sysPriceField.setHorizontalAlignment(SwingConstants.RIGHT);
		sysPriceField.setColumns(10);
		
		JButton saveItemButton = new JButton("Save Item Details");
		JButton sysItemRemDetailsButton = new JButton("Delete Item");
		
		JLabel lblNewLabel_28 = new JLabel("Search Frequency:");
		
		searchFreqLabel = new JLabel("searchFreqHere");
		
		sysDescField = new JTextArea();
		GroupLayout gl_sysItemDetailPanel = new GroupLayout(sysItemDetailPanel);
		gl_sysItemDetailPanel.setHorizontalGroup(
			gl_sysItemDetailPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_sysItemDetailPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_sysItemDetailPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_sysItemDetailPanel.createSequentialGroup()
							.addComponent(lblNewLabel_9_1, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(sysDescField, GroupLayout.PREFERRED_SIZE, 368, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_sysItemDetailPanel.createSequentialGroup()
							.addGroup(gl_sysItemDetailPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel_8_1, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel_10_1, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel_11_1, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_sysItemDetailPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_sysItemDetailPanel.createSequentialGroup()
									.addComponent(sysItemNameField, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE)
									.addGap(55)
									.addComponent(lblNewLabel_28)
									.addGap(18)
									.addComponent(searchFreqLabel))
								.addGroup(gl_sysItemDetailPanel.createParallelGroup(Alignment.TRAILING, false)
									.addComponent(sysSizeField, Alignment.LEADING)
									.addComponent(sysPriceField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))))
						.addComponent(saveItemButton)
						.addComponent(sysItemRemDetailsButton))
					.addContainerGap(464, Short.MAX_VALUE))
		);
		gl_sysItemDetailPanel.setVerticalGroup(
			gl_sysItemDetailPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_sysItemDetailPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_sysItemDetailPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_8_1)
						.addComponent(sysItemNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_28)
						.addComponent(searchFreqLabel))
					.addGap(18)
					.addGroup(gl_sysItemDetailPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_10_1)
						.addComponent(sysSizeField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_sysItemDetailPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_11_1)
						.addComponent(sysPriceField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(66)
					.addGroup(gl_sysItemDetailPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_9_1)
						.addComponent(sysDescField, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 176, Short.MAX_VALUE)
					.addComponent(saveItemButton)
					.addGap(18)
					.addComponent(sysItemRemDetailsButton)
					.addGap(78))
		);
		sysItemDetailPanel.setLayout(gl_sysItemDetailPanel);

		// full store list
		JPanel storeListPanel = new JPanel();
		tabbedPane.addTab("Full Store List", null, storeListPanel, "Add, remove, modify Stores here");
		
		JScrollPane fullStoreScroll = new JScrollPane();
		
		JButton addStoreButton = new JButton("Add Store");
		JButton remStoreFromListButton = new JButton("Remove Store");
		JButton selectManStoreButton = new JButton("Select Store to Manage");
		
		GroupLayout gl_storeListPanel = new GroupLayout(storeListPanel);
		gl_storeListPanel.setHorizontalGroup(
			gl_storeListPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_storeListPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_storeListPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(fullStoreScroll, GroupLayout.DEFAULT_SIZE, 959, Short.MAX_VALUE)
						.addGroup(gl_storeListPanel.createSequentialGroup()
							.addComponent(addStoreButton)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(remStoreFromListButton)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(selectManStoreButton)))
					.addContainerGap())
		);
		gl_storeListPanel.setVerticalGroup(
			gl_storeListPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_storeListPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(fullStoreScroll, GroupLayout.PREFERRED_SIZE, 352, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_storeListPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(addStoreButton)
						.addComponent(remStoreFromListButton)
						.addComponent(selectManStoreButton))
					.addContainerGap(91, Short.MAX_VALUE))
		);
		
		JList<String> fullStoreList = new JList<String>(storeModel);
		fullStoreScroll.setViewportView(fullStoreList);
		storeListPanel.setLayout(gl_storeListPanel);
		Set<Store> stores = system.getStores();
		for (Store s : stores) {
			storeModel.add(storeModel.size(), s.getAddress());
		}
		
		// TODO - you can set the Admin's managedStore in the full store list

		// store item list - manager
		storeItemPanel = new JPanel();
		// uncomment to see store item panel in Design tab
		tabbedPane.addTab("Store Item List", null, storeItemPanel, "View all Items sold in Store. Select Items to remove or modify, or add an Item to sell in Store");

		JLabel lblNewLabel = new JLabel("All Items sold at the Store:");

		JScrollPane storeItemScroll = new JScrollPane();

		JButton storeItemDetailButton = new JButton("View Item Details (Store)");
		JButton removeItemButton = new JButton("Remove Item");
		JButton addItemButton = new JButton("Add Item");
		
		JButton storeItemSysDetailButton = new JButton("View Item Details (System)");

		GroupLayout gl_storeItemPanel = new GroupLayout(storeItemPanel);
		gl_storeItemPanel.setHorizontalGroup(
			gl_storeItemPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_storeItemPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_storeItemPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(storeItemScroll, GroupLayout.DEFAULT_SIZE, 959, Short.MAX_VALUE)
						.addComponent(lblNewLabel)
						.addGroup(gl_storeItemPanel.createSequentialGroup()
							.addComponent(storeItemDetailButton)
							.addPreferredGap(ComponentPlacement.RELATED, 652, Short.MAX_VALUE)
							.addComponent(addItemButton)
							.addGap(18)
							.addComponent(removeItemButton))
						.addComponent(storeItemSysDetailButton))
					.addContainerGap())
		);
		gl_storeItemPanel.setVerticalGroup(
			gl_storeItemPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_storeItemPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addGap(18)
					.addComponent(storeItemScroll, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_storeItemPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(storeItemDetailButton)
						.addComponent(removeItemButton)
						.addComponent(addItemButton))
					.addGap(18)
					.addComponent(storeItemSysDetailButton)
					.addContainerGap(86, Short.MAX_VALUE))
		);

		storeItemTable = new JTable(storeItemModel);
		storeItemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		storeItemScroll.setViewportView(storeItemTable);
		storeItemPanel.setLayout(gl_storeItemPanel);

		// sale list panel - manager
		saleListPanel = new JPanel();
		// uncomment to see sale list panel in Design tab
		tabbedPane.addTab("On Sale List", null, saleListPanel, null);

		JScrollPane saleItemScroll = new JScrollPane();

		JLabel lblNewLabel_1 = new JLabel("All Items on sale at the Store:");

		JButton saleItemDetailButton = new JButton("View Item Details");

		JButton saleRemoveButton = new JButton("Remove from Sale");
		saleRemoveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowInd = saleItemTable.getSelectedRow();
				if (rowInd >= 0) {
					String itemName = (String) saleItemTable.getValueAt(rowInd, 0);
					if (!(itemName == null || itemName.isEmpty())) {
						if (manStore != null) {
							Item rem = manStore.getItemByName(itemName);
							int confirm = JOptionPane.showConfirmDialog(removeItemButton,
									"Are you sure you want to remove that object from the sale?",
									"Confirm Item Removal from sale", JOptionPane.YES_NO_OPTION);
							if (confirm == JOptionPane.YES_OPTION) {
								if (user.remFromSaleList(rem)) {
									JOptionPane.showMessageDialog(removeItemButton, "Item removed from the sale.");
									saleItemModel.remove(rem);
									storeItemModel.update();
									saleItemModel.update();
									if (storeItemInterested.equals(rem)) {
										initStoreItemDetails(itemName);
									}
								} else {
									JOptionPane.showMessageDialog(removeItemButton,
											"Item couldn't be removed from the sale.");
								}
							}
						} else {
							JOptionPane.showMessageDialog(removeItemButton, "You're not managing a store right now.");
						}
					} else {
						JOptionPane.showMessageDialog(removeItemButton, "No item was selected.");
					}
				} else {
					JOptionPane.showMessageDialog(removeItemButton, "No item was selected.");
				}
			}
		});

		JLabel lblNewLabel_27 = new JLabel(
				"To add an item to the sale, go to Store Item List, select an Item, and go to its Details page.");
		GroupLayout gl_saleListPanel = new GroupLayout(saleListPanel);
		gl_saleListPanel.setHorizontalGroup(gl_saleListPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_saleListPanel.createSequentialGroup().addContainerGap().addGroup(gl_saleListPanel
						.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(saleItemScroll, GroupLayout.PREFERRED_SIZE, 759, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_saleListPanel.createSequentialGroup()
								.addComponent(saleItemDetailButton, GroupLayout.PREFERRED_SIZE, 134,
										GroupLayout.PREFERRED_SIZE)
								.addGap(26).addComponent(lblNewLabel_27)
								.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(saleRemoveButton))
						.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 193, GroupLayout.PREFERRED_SIZE))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		gl_saleListPanel.setVerticalGroup(gl_saleListPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_saleListPanel.createSequentialGroup().addContainerGap().addComponent(lblNewLabel_1)
						.addGap(18)
						.addComponent(saleItemScroll, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_saleListPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_saleListPanel.createSequentialGroup().addGap(18)
										.addGroup(gl_saleListPanel.createParallelGroup(Alignment.BASELINE)
												.addComponent(saleItemDetailButton).addComponent(saleRemoveButton)))
								.addGroup(gl_saleListPanel.createSequentialGroup().addGap(22)
										.addComponent(lblNewLabel_27)))
						.addContainerGap(27, Short.MAX_VALUE)));

		saleItemTable = new JTable(saleItemModel);
		saleItemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		saleItemScroll.setViewportView(saleItemTable);
		saleListPanel.setLayout(gl_saleListPanel);

		// store details - manager
		storePropertiesPanel = new JPanel();
		// uncomment to see store details in Design
		tabbedPane.addTab("Store Details", null, storePropertiesPanel,"View and modify open/close times and categories");

		JLabel lblNewLabel_3 = new JLabel("Opening Time:");

		JLabel lblNewLabel_14 = new JLabel("Closing Time:");

		openTimeField = new JFormattedTextField(shortTime);
		openTimeField.setValue(new Date());
		openTimeField.setColumns(20);
		openTimeField.setHorizontalAlignment(SwingConstants.CENTER);
		openTimeField.setToolTipText("Please input a time in a valid format (XX:XX a.m. or p.m.)");

		closeTimeField = new JFormattedTextField(shortTime);
		closeTimeField.setValue(new Date());
		closeTimeField.setColumns(20);
		closeTimeField.setHorizontalAlignment(SwingConstants.CENTER);
		closeTimeField.setToolTipText("Please input a time in a valid format (XX:XX a.m. or p.m.)");

		JButton updateHoursButton = new JButton("Update Hours");

		JLabel lblNewLabel_19 = new JLabel("Categories at this Store from Entrance to Cashier:");

		JScrollPane catScroll = new JScrollPane();

		JButton categoryDetailButton = new JButton("View Category Details");

		JButton remCatButton = new JButton("Remove Category");
		JButton addCatButton = new JButton("Add Category");
		addCatButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ManagerGUIAddCategory addCat = new ManagerGUIAddCategory();
				addCat.getUser(user);
				frAdminHome.dispose();
				addCat.setVisible(true);
				frAdminHome.setVisible(false);
			}
		});

		JLabel lblNewLabel_17 = new JLabel(
				"NOTE: Removing a Category will remove the associated Items from your Store.");
		lblNewLabel_17.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JButton remStoreFromDetailsButton = new JButton("Delete Store");
		
		JScrollPane managerStoreScroll = new JScrollPane();
		
		JLabel lblNewLabel_29 = new JLabel("Managers at this Store:");
		
		JButton managerDetailsFromStoreButton = new JButton("View Manager Details");
		JButton remManagerFromStoreButton = new JButton("Remove Manager");
		
		JLabel lblNewLabel_30 = new JLabel("You can add a Manager to the Store from the Manager List tab.");
		
		GroupLayout gl_storePropertiesPanel = new GroupLayout(storePropertiesPanel);
		gl_storePropertiesPanel.setHorizontalGroup(
			gl_storePropertiesPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_storePropertiesPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_storePropertiesPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_storePropertiesPanel.createSequentialGroup()
							.addGroup(gl_storePropertiesPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_storePropertiesPanel.createSequentialGroup()
									.addComponent(lblNewLabel_3)
									.addGap(18)
									.addComponent(openTimeField, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(lblNewLabel_14)
									.addGap(18)
									.addComponent(closeTimeField, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED, 162, Short.MAX_VALUE)
									.addComponent(updateHoursButton)
									.addGap(329))
								.addGroup(gl_storePropertiesPanel.createSequentialGroup()
									.addComponent(remStoreFromDetailsButton)
									.addGap(119)
									.addComponent(lblNewLabel_17))
								.addGroup(gl_storePropertiesPanel.createSequentialGroup()
									.addGroup(gl_storePropertiesPanel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_storePropertiesPanel.createParallelGroup(Alignment.TRAILING, false)
											.addGroup(gl_storePropertiesPanel.createSequentialGroup()
												.addComponent(categoryDetailButton)
												.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(addCatButton)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(remCatButton))
											.addComponent(catScroll, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 416, GroupLayout.PREFERRED_SIZE))
										.addComponent(lblNewLabel_19))
									.addGroup(gl_storePropertiesPanel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_storePropertiesPanel.createSequentialGroup()
											.addGap(18)
											.addGroup(gl_storePropertiesPanel.createParallelGroup(Alignment.LEADING)
												.addComponent(lblNewLabel_29)
												.addComponent(managerStoreScroll, GroupLayout.PREFERRED_SIZE, 523, GroupLayout.PREFERRED_SIZE)))
										.addGroup(Alignment.TRAILING, gl_storePropertiesPanel.createSequentialGroup()
											.addPreferredGap(ComponentPlacement.RELATED, 136, Short.MAX_VALUE)
											.addComponent(managerDetailsFromStoreButton)
											.addGap(33)
											.addComponent(remManagerFromStoreButton)
											.addGap(120)))))
							.addContainerGap())
						.addGroup(Alignment.TRAILING, gl_storePropertiesPanel.createSequentialGroup()
							.addComponent(lblNewLabel_30)
							.addGap(121))))
		);
		gl_storePropertiesPanel.setVerticalGroup(
			gl_storePropertiesPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_storePropertiesPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_storePropertiesPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_3)
						.addComponent(openTimeField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_14)
						.addComponent(updateHoursButton)
						.addComponent(closeTimeField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_storePropertiesPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_19)
						.addComponent(lblNewLabel_29))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_storePropertiesPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(catScroll, GroupLayout.PREFERRED_SIZE, 259, GroupLayout.PREFERRED_SIZE)
						.addComponent(managerStoreScroll, GroupLayout.PREFERRED_SIZE, 258, GroupLayout.PREFERRED_SIZE))
					.addGroup(gl_storePropertiesPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_storePropertiesPanel.createSequentialGroup()
							.addGap(18)
							.addComponent(categoryDetailButton))
						.addGroup(gl_storePropertiesPanel.createSequentialGroup()
							.addGap(18)
							.addGroup(gl_storePropertiesPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(managerDetailsFromStoreButton)
								.addComponent(remManagerFromStoreButton)
								.addGroup(gl_storePropertiesPanel.createParallelGroup(Alignment.BASELINE)
									.addComponent(remCatButton)
									.addComponent(addCatButton)))))
					.addGap(18)
					.addComponent(lblNewLabel_30)
					.addPreferredGap(ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
					.addGroup(gl_storePropertiesPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_17)
						.addComponent(remStoreFromDetailsButton))
					.addGap(44))
		);
		
		JList<String> managerStoreList = new JList<String>(manStoreListModel);
		managerStoreList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		managerStoreScroll.setViewportView(managerStoreList);

		catList = new JList<String>(catModel);
		catList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		catScroll.setViewportView(catList);
		storePropertiesPanel.setLayout(gl_storePropertiesPanel);

		// update store hours
		updateHoursButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (manStore != null) {
					String openStr = openTimeField.getText();
					int openTime = UtilitiesGUI.convertTimeToInt(openStr);
					String closeStr = closeTimeField.getText();
					int closeTime = UtilitiesGUI.convertTimeToInt(closeStr);
					if (closeTime < openTime) {
						JOptionPane.showMessageDialog(updateHoursButton,
								"Invalid input - close time is earlier than open time!");
					} else {
						if (user.updateStoreOpenTime(openTime) && user.updateStoreCloseTime(closeTime)) {
							JOptionPane.showMessageDialog(updateHoursButton, "Store hours changed successfully.");
						} else {
							JOptionPane.showMessageDialog(updateHoursButton, "Store hours couldn't be changed!");
						}
					}
				} else {
					JOptionPane.showMessageDialog(removeItemButton, "You're not managing a store right now.");
				}
			}
		});

		// remove a category (and associated items)
		remCatButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String catName = catList.getSelectedValue();
				if (!(catName == null || catName.isEmpty())) {
					if (manStore != null) {
						if (user.removeCategory(catName)) {
							JOptionPane.showMessageDialog(categoryDetailButton,
									"Category " + catName + " and any associated Items have been removed.");
							catModel.removeElement(catName);
							storeItemModel.removeAll();
							storeItemModel.update();
							storeItemModel.add(manStore.getItemList());
							saleItemModel.removeAll();
							saleItemModel.add(manStore.getSaleList());
							saleItemModel.update();
							int itemDeet = tabbedPane.indexOfTab("Store Item Details");
							if (!(itemDeet == -1))
								tabbedPane.removeTabAt(itemDeet);
							int catDeet = tabbedPane.indexOfTab("Category Details");
							if (!(catDeet == -1))
								tabbedPane.removeTabAt(catDeet);
						} else
							JOptionPane.showMessageDialog(categoryDetailButton, "Category " + catName
									+ " couldn't be removed, but its associated Items may have been removed.");
					} else {
						JOptionPane.showMessageDialog(categoryDetailButton, "You are currently not managing a store.");
					}
				} else {
					JOptionPane.showMessageDialog(categoryDetailButton, "No category was selected.");
				}
			}
		});

		categoryDetailPanel = new JPanel();
		// uncomment to show Category Details in Design Tab
		tabbedPane.addTab("Category Details", null, categoryDetailPanel, null);

		JLabel lblNewLabel_20 = new JLabel("Name:");

		JLabel catNameLabel = new JLabel("catNameHere");

		JLabel lblNewLabel_22 = new JLabel("Preceding Category:");

		JLabel lblNewLabel_23 = new JLabel("Trailing Category:");

		JLabel prevCatLabel = new JLabel("prevCatHere");

		JLabel nextCatLabel = new JLabel("nextCatHere");

		JLabel lblNewLabel_24 = new JLabel("Density:");

		JFormattedTextField densityField = new JFormattedTextField();
		densityField.setHorizontalAlignment(SwingConstants.RIGHT);
		densityField.setText("1.0");

		JButton upDenButton = new JButton("Update Density");
		upDenButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!densityField.getText().isEmpty()) {
					double density = Double.parseDouble(densityField.getText());
					if (!(catNameLabel.getText() == null) && !(catNameLabel.getText().isEmpty())) {
						if (manStore != null) {
							if (user.updateDensity(catNameLabel.getText(), density)) {
								JOptionPane.showMessageDialog(upDenButton,
										"Density for category " + catNameLabel.getText() + " has been updated.");
							} else {
								JOptionPane.showMessageDialog(upDenButton,
										"Density for category " + catNameLabel.getText() + " has NOT been updated.");
							}
						} else {
							JOptionPane.showMessageDialog(removeItemButton, "You're not managing a store right now.");
						}
					} else {
						JOptionPane.showMessageDialog(removeItemButton, "No category was selected.");
					}
				}
			}
		});

		JScrollPane catItemsScroll = new JScrollPane();

		JButton catItemDetailButton = new JButton("View Item Details");

		JLabel lblNewLabel_25 = new JLabel("All Items in this Category at this Store:");
		GroupLayout gl_categoryDetailPanel = new GroupLayout(categoryDetailPanel);
		gl_categoryDetailPanel.setHorizontalGroup(gl_categoryDetailPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_categoryDetailPanel.createSequentialGroup().addContainerGap()
						.addGroup(gl_categoryDetailPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(catItemsScroll, GroupLayout.DEFAULT_SIZE, 759, Short.MAX_VALUE)
								.addGroup(gl_categoryDetailPanel.createSequentialGroup().addComponent(lblNewLabel_20)
										.addGap(18).addComponent(catNameLabel).addGap(61).addComponent(lblNewLabel_22)
										.addGap(18).addComponent(prevCatLabel).addGap(67).addComponent(lblNewLabel_23)
										.addGap(18).addComponent(nextCatLabel))
								.addGroup(gl_categoryDetailPanel.createSequentialGroup().addComponent(lblNewLabel_24)
										.addGap(18)
										.addComponent(densityField, GroupLayout.PREFERRED_SIZE, 100,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18).addComponent(upDenButton))
								.addComponent(catItemDetailButton).addComponent(lblNewLabel_25))
						.addContainerGap()));
		gl_categoryDetailPanel.setVerticalGroup(gl_categoryDetailPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_categoryDetailPanel.createSequentialGroup().addContainerGap()
						.addGroup(gl_categoryDetailPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel_20).addComponent(catNameLabel).addComponent(lblNewLabel_22)
								.addComponent(prevCatLabel).addComponent(lblNewLabel_23).addComponent(nextCatLabel))
						.addGap(18)
						.addGroup(gl_categoryDetailPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel_24)
								.addComponent(densityField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(upDenButton))
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblNewLabel_25).addGap(8)
						.addComponent(catItemsScroll, GroupLayout.PREFERRED_SIZE, 246, GroupLayout.PREFERRED_SIZE)
						.addGap(18).addComponent(catItemDetailButton).addContainerGap(25, Short.MAX_VALUE)));

		itemsInCatList = new JList<String>(catItemModel);
		itemsInCatList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		catItemsScroll.setViewportView(itemsInCatList);
		categoryDetailPanel.setLayout(gl_categoryDetailPanel);
		
		// store item details - manager
		storeItemDetailsPanel = new JPanel();
		// uncomment to see item details in design tab
		tabbedPane.addTab("Store Item Details", null, storeItemDetailsPanel, null);
				
		// item details components
		itemNameLabel = new JLabel("nameHere");
		descriptionLabel = new JLabel("descHere");
		sizeLabel = new JLabel("sizeHere");
		priceLabel = new JLabel("priceHere");
		currStockLabel = new JLabel("quantHere");
		currCategoryLabel = new JLabel("catHere");
						
		JLabel lblNewLabel_8 = new JLabel("Item Name:");
		lblNewLabel_8.setFont(new Font("Tahoma", Font.BOLD, 11));
		JLabel lblNewLabel_9 = new JLabel("Description:");
		lblNewLabel_9.setFont(new Font("Tahoma", Font.BOLD, 11));
		JLabel lblNewLabel_10 = new JLabel("Size:");
		lblNewLabel_10.setFont(new Font("Tahoma", Font.BOLD, 11));
		JLabel lblNewLabel_11 = new JLabel("Unit Price:");
		lblNewLabel_11.setFont(new Font("Tahoma", Font.BOLD, 11));
		JLabel lblNewLabel_12 = new JLabel("Current Stock Level:");
		lblNewLabel_12.setFont(new Font("Tahoma", Font.BOLD, 11));
		JLabel lblNewLabel_13 = new JLabel("Category at this Store:");
		lblNewLabel_13.setFont(new Font("Tahoma", Font.BOLD, 11));
								
		JFormattedTextField newStockField = new JFormattedTextField(formatter);
		newStockField.setHorizontalAlignment(SwingConstants.RIGHT);
		newStockField.setColumns(10);
										
		JButton updateStockButton = new JButton("Update Stock");
		updateStockButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (manStore != null) {
					int newStock = Integer.parseInt(newStockField.getText());
					if (manStore.updateItemStock(storeItemInterested, newStock)) {
						JOptionPane.showMessageDialog(updateStockButton, "Item stock updated successfully.");
						currStockLabel.setText("" + newStock);
						storeItemModel.update();
						saleItemModel.update();
					} else {
						JOptionPane.showMessageDialog(updateStockButton, "Item stock couldn't be updated.");
					}
				} else {
					JOptionPane.showMessageDialog(updateStockButton, "You're not managing a store right now.");
				}
			}
		});
												
		JLabel lblNewLabel_16 = new JLabel("Updated Stock Level:");

		JLabel lblNewLabel_2 = new JLabel("Updated Category Name:");

		categoryComboBox = new JComboBox<String>();

		JButton updateCategoryButton = new JButton("Update Category");
		updateCategoryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (manStore != null) {
					String newCat = (String) categoryComboBox.getSelectedItem();
					if (manStore.updateItemCategory(storeItemInterested, newCat)) {
						JOptionPane.showMessageDialog(updateCategoryButton, "Item category updated successfully.");
						currCategoryLabel.setText(newCat);
						storeItemModel.update();
						saleItemModel.update();
					} else {
						JOptionPane.showMessageDialog(updateCategoryButton, "Item category couldn't be updated.");
					}
				} else {
					JOptionPane.showMessageDialog(updateCategoryButton, "You're not managing a store right now.");
				}
			}
		});

		JLabel lblNewLabel_4 = new JLabel("On Sale?");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 11));

		saleStatusLabel = new JLabel("saleStatusHere");

		JLabel lblNewLabel_15 = new JLabel("Sale Reduction Amount (%):");

		JFormattedTextField saleReductionField = new JFormattedTextField(doubleFormatter);
		saleReductionField.setHorizontalAlignment(SwingConstants.RIGHT);
		saleReductionField.setText("0");

		JButton updateSaleButton = new JButton("Update Sale Status");
		updateSaleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (manStore != null) {
					double reduceBy = (double) Integer.parseInt(saleReductionField.getText()) / 100;
					if (reduceBy == 0.0) {
						if (manStore.remFromSaleList(storeItemInterested)) {
							JOptionPane.showMessageDialog(updateSaleButton, "Item removed from sale successfully.");
							saleStatusLabel.setText("No");
							storeItemModel.update();
							saleItemModel.remove(storeItemInterested);
						} else {
							JOptionPane.showMessageDialog(updateSaleButton, "Item sale status couldn't be updated.");
						}
					} else {
						if (manStore.addToSaleList(storeItemInterested, reduceBy)) {
							JOptionPane.showMessageDialog(updateSaleButton, "Item added to sale successfully.");
							saleStatusLabel.setText("Yes, reduced by " + String.format("%.2f", reduceBy * 100) + "%");
							storeItemModel.update();
							saleItemModel.add(storeItemInterested);
						} else {
							JOptionPane.showMessageDialog(updateSaleButton, "Item sale status couldn't be updated.");
						}
					}
				} else {
					JOptionPane.showMessageDialog(updateSaleButton, "You're not managing a store right now.");
				}
			}
		});

		JLabel lblNewLabel_26 = new JLabel("To remove from sale, put 0");
		
		JButton goToSysItemDetailButton = new JButton("Go to System Item Details");

		// item details layout
		GroupLayout gl_storeItemDetailsPanel = new GroupLayout(storeItemDetailsPanel);
		gl_storeItemDetailsPanel.setHorizontalGroup(
			gl_storeItemDetailsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_storeItemDetailsPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_storeItemDetailsPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_storeItemDetailsPanel.createSequentialGroup()
							.addComponent(lblNewLabel_8)
							.addGap(18)
							.addComponent(itemNameLabel))
						.addGroup(gl_storeItemDetailsPanel.createSequentialGroup()
							.addGroup(gl_storeItemDetailsPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_storeItemDetailsPanel.createSequentialGroup()
									.addGroup(gl_storeItemDetailsPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblNewLabel_12)
										.addComponent(lblNewLabel_13)
										.addComponent(lblNewLabel_4))
									.addGap(18)
									.addGroup(gl_storeItemDetailsPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(currStockLabel)
										.addComponent(currCategoryLabel)
										.addComponent(saleStatusLabel)))
								.addGroup(gl_storeItemDetailsPanel.createSequentialGroup()
									.addGroup(gl_storeItemDetailsPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblNewLabel_9)
										.addComponent(lblNewLabel_10)
										.addComponent(lblNewLabel_11))
									.addGap(18)
									.addGroup(gl_storeItemDetailsPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(priceLabel)
										.addComponent(sizeLabel)
										.addComponent(descriptionLabel))))
							.addGap(51)
							.addGroup(gl_storeItemDetailsPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(goToSysItemDetailButton)
								.addGroup(gl_storeItemDetailsPanel.createSequentialGroup()
									.addGroup(gl_storeItemDetailsPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblNewLabel_2)
										.addComponent(lblNewLabel_16)
										.addComponent(lblNewLabel_15))
									.addGap(18)
									.addGroup(gl_storeItemDetailsPanel.createParallelGroup(Alignment.LEADING, false)
										.addComponent(saleReductionField)
										.addComponent(newStockField, Alignment.TRAILING)
										.addComponent(lblNewLabel_26)
										.addComponent(categoryComboBox, GroupLayout.PREFERRED_SIZE, 205, GroupLayout.PREFERRED_SIZE))
									.addGap(48)
									.addGroup(gl_storeItemDetailsPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(updateSaleButton)
										.addComponent(updateCategoryButton)
										.addComponent(updateStockButton))))))
					.addContainerGap(166, Short.MAX_VALUE))
		);
		gl_storeItemDetailsPanel.setVerticalGroup(
			gl_storeItemDetailsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_storeItemDetailsPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_storeItemDetailsPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_8)
						.addComponent(itemNameLabel))
					.addGap(18)
					.addGroup(gl_storeItemDetailsPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_9)
						.addComponent(descriptionLabel))
					.addGap(18)
					.addGroup(gl_storeItemDetailsPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_10)
						.addComponent(sizeLabel))
					.addGap(18)
					.addGroup(gl_storeItemDetailsPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_11)
						.addComponent(priceLabel)
						.addComponent(goToSysItemDetailButton))
					.addGap(73)
					.addGroup(gl_storeItemDetailsPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_storeItemDetailsPanel.createSequentialGroup()
							.addComponent(updateStockButton)
							.addGap(18)
							.addComponent(updateCategoryButton)
							.addGap(18)
							.addComponent(updateSaleButton))
						.addGroup(gl_storeItemDetailsPanel.createSequentialGroup()
							.addGroup(gl_storeItemDetailsPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel_12)
								.addComponent(currStockLabel)
								.addComponent(lblNewLabel_16)
								.addComponent(newStockField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_storeItemDetailsPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel_13)
								.addComponent(currCategoryLabel)
								.addComponent(lblNewLabel_2)
								.addComponent(categoryComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_storeItemDetailsPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel_4)
								.addComponent(saleStatusLabel)
								.addComponent(lblNewLabel_15)
								.addComponent(saleReductionField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblNewLabel_26)))
					.addContainerGap(178, Short.MAX_VALUE))
		);
		storeItemDetailsPanel.setLayout(gl_storeItemDetailsPanel);

		// account details declaration
		JPanel accountDetails = new JPanel();
		tabbedPane.addTab("Account Management", null, accountDetails, "Manage your account details.");

		// default account management stuff
		JLabel lblNewLabel_5 = new JLabel("Username:");
		usernameLabel = new JLabel("usernamehere");
		JButton chgUsernameButton = new JButton("Change Username");
		chgUsernameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChangeUsername chgUsername = new ChangeUsername();
				chgUsername.getUser(user);
				frAdminHome.dispose();
				chgUsername.setVisible(true);
				frAdminHome.setVisible(false);
			}
		});
		JLabel lblNewLabel_7 = new JLabel("Two-Factor Authentication:");
		twoFactorLabel = new JLabel("2FAhere");
		JButton chgTFAButton = new JButton("Change 2FA Settings");
		chgTFAButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChangeTFA chgTFA = new ChangeTFA();
				chgTFA.getUser(user);
				frAdminHome.dispose();
				chgTFA.setVisible(true);
				frAdminHome.setVisible(false);
			}
		});

		JButton chgPWButton = new JButton("Change Password");
		chgPWButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChangePassword chgPW = new ChangePassword();
				chgPW.getUser(user);
				frAdminHome.dispose();
				chgPW.setVisible(true);
				frAdminHome.setVisible(false);
			}
		});

		JButton deleteAccountButton = new JButton("Delete My Account");
		deleteAccountButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeleteAccountPage del = new DeleteAccountPage();
				del.getUser(user);
				frAdminHome.dispose();
				del.setVisible(true);
				frAdminHome.setVisible(false);
			}
		});

		// manager's store info
		JLabel lblNewLabel_6 = new JLabel("Currently editing this Store:");
		manStoreLabel = new JLabel("manStoreHere");
		
		JLabel lblNewLabel_18 = new JLabel("You can change this to another Store in the Full Store List tab.");
		
		JButton resetManStoreButton = new JButton("Reset this value"); // TODO put an ActionListener here

		// account details layout
		GroupLayout gl_accountDetails = new GroupLayout(accountDetails);
		gl_accountDetails.setHorizontalGroup(
			gl_accountDetails.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_accountDetails.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_accountDetails.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_accountDetails.createSequentialGroup()
							.addGroup(gl_accountDetails.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_accountDetails.createSequentialGroup()
									.addComponent(lblNewLabel_5)
									.addGap(18)
									.addComponent(usernameLabel, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE))
								.addComponent(chgUsernameButton))
							.addGap(68)
							.addGroup(gl_accountDetails.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_accountDetails.createSequentialGroup()
									.addComponent(chgTFAButton)
									.addPreferredGap(ComponentPlacement.RELATED, 440, Short.MAX_VALUE)
									.addComponent(chgPWButton))
								.addGroup(gl_accountDetails.createSequentialGroup()
									.addComponent(lblNewLabel_7)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(twoFactorLabel))))
						.addGroup(gl_accountDetails.createSequentialGroup()
							.addComponent(lblNewLabel_6)
							.addGap(18)
							.addComponent(manStoreLabel, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE))
						.addComponent(deleteAccountButton)
						.addComponent(resetManStoreButton)
						.addComponent(lblNewLabel_18))
					.addContainerGap())
		);
		gl_accountDetails.setVerticalGroup(
			gl_accountDetails.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_accountDetails.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_accountDetails.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_5)
						.addComponent(usernameLabel)
						.addComponent(lblNewLabel_7)
						.addComponent(twoFactorLabel))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_accountDetails.createParallelGroup(Alignment.BASELINE)
						.addComponent(chgUsernameButton)
						.addComponent(chgTFAButton)
						.addComponent(chgPWButton))
					.addGap(73)
					.addGroup(gl_accountDetails.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_6)
						.addComponent(manStoreLabel))
					.addGap(18)
					.addComponent(resetManStoreButton)
					.addGap(18)
					.addComponent(lblNewLabel_18)
					.addPreferredGap(ComponentPlacement.RELATED, 258, Short.MAX_VALUE)
					.addComponent(deleteAccountButton)
					.addContainerGap())
		);
		accountDetails.setLayout(gl_accountDetails);

		// view item from store item page
		storeItemDetailButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowInd = storeItemTable.getSelectedRow();
				if (rowInd >= 0) {
					String itemName = (String) storeItemTable.getValueAt(rowInd, 0);
					if (!(itemName == null || itemName.isEmpty())) {
						if (manStore != null) {
							initStoreItemDetails(itemName);

							if (tabbedPane.indexOfTab("Store Item Details") == -1)
								tabbedPane.insertTab("Store Item Details", null, storeItemDetailsPanel, null, tabbedPane.indexOfTab("Account Management"));
							tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Store Item Details"));
							// tabbedPane.addTab("Item Details", null, itemDetails, null);
						} else {
							JOptionPane.showMessageDialog(storeItemDetailButton,
									"You're not managing a store right now.");
						}
					} else {
						JOptionPane.showMessageDialog(storeItemDetailButton, "No item was selected.");
					}
				} else {
					JOptionPane.showMessageDialog(storeItemDetailButton, "No item was selected.");
				}
			}
		});

		// view item from sale page
		saleItemDetailButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowInd = saleItemTable.getSelectedRow();
				if (rowInd >= 0) {
					String itemName = (String) saleItemTable.getValueAt(rowInd, 0);
					if (!(itemName == null || itemName.isEmpty())) {
						if (manStore != null) {
							initStoreItemDetails(itemName);

							if (tabbedPane.indexOfTab("Store Item Details") == -1)
								tabbedPane.insertTab("Store Item Details", null, storeItemDetailsPanel, null, tabbedPane.indexOfTab("Account Management"));
							tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Store Item Details"));
							// tabbedPane.addTab("Item Details", null, itemDetails, null);
						} else {
							JOptionPane.showMessageDialog(saleItemDetailButton,
									"You're not managing a store right now.");
						}
					} else {
						JOptionPane.showMessageDialog(saleItemDetailButton, "No item was selected.");
					}
				} else {
					JOptionPane.showMessageDialog(saleItemDetailButton, "No item was selected.");
				}
			}
		});

		// view item from category page
		catItemDetailButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String itemName = itemsInCatList.getSelectedValue();
				if (!(itemName == null || itemName.isEmpty())) {
					if (manStore != null) {
						initStoreItemDetails(itemName);

						if (tabbedPane.indexOfTab("Store Item Details") == -1)
							tabbedPane.insertTab("Store Item Details", null, storeItemDetailsPanel, null, tabbedPane.indexOfTab("Account Management"));
						tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Store Item Details"));
						// tabbedPane.addTab("Item Details", null, itemDetails, null);
					} else {
						JOptionPane.showMessageDialog(saleItemDetailButton, "You're not managing a store right now.");
					}
				} else {
					JOptionPane.showMessageDialog(saleItemDetailButton, "No item was selected.");
				}
			}
		});

		// add item from store item list page
		addItemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ManagerGUIAddItem addItem = new ManagerGUIAddItem();
				addItem.getUser(user);
				frAdminHome.dispose();
				addItem.setVisible(true);
				frAdminHome.setVisible(false);
			}
		});

		// remove selected item from store item list page
		removeItemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowInd = storeItemTable.getSelectedRow();
				if (rowInd >= 0) {
					String itemName = (String) storeItemTable.getValueAt(rowInd, 0);
					if (!(itemName == null || itemName.isEmpty())) {
						if (manStore != null) {
							Item rem = manStore.getItemByName(itemName);
							int confirm = JOptionPane.showConfirmDialog(removeItemButton,
									"Are you sure you want to remove that object?", "Confirm Item Removal from Store",
									JOptionPane.YES_NO_OPTION);
							if (confirm == JOptionPane.YES_OPTION) {
								if (user.removeItem(rem)) {
									JOptionPane.showMessageDialog(removeItemButton, "Item removed.");
									storeItemModel.remove(rem);
									storeItemModel.update();
									saleItemModel.update();
									int itemDeet = tabbedPane.indexOfTab("Store Item Details");
									if (!(itemDeet == -1))
										tabbedPane.removeTabAt(itemDeet);
									int catDeet = tabbedPane.indexOfTab("Category Details");
									if (!(catDeet == -1))
										tabbedPane.removeTabAt(catDeet);
								} else {
									JOptionPane.showMessageDialog(removeItemButton, "Item couldn't be removed.");
								}
							}
						} else {
							JOptionPane.showMessageDialog(removeItemButton, "You're not managing a store right now.");
						}
					} else {
						JOptionPane.showMessageDialog(removeItemButton, "No item was selected.");
					}
				} else {
					JOptionPane.showMessageDialog(removeItemButton, "No item was selected.");
				}
			}
		});

		// go to category details from store properties page
		categoryDetailButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String catName = catList.getSelectedValue();
				if (!(catName == null || catName.isEmpty())) {
					if (manStore != null) {
						LinkedList<String> cat = manStore.getCategories();
						int catInd = cat.indexOf(catName);
						String prev = "";
						String next = "";
						if (catInd != 0)
							prev = cat.get(catInd - 1);
						if (catInd != cat.size() - 1)
							next = cat.get(catInd + 1);
						catNameLabel.setText(catName);
						prevCatLabel.setText(prev);
						nextCatLabel.setText(next);
						double density = manStore.getDensity(catName);
						densityField.setText(String.format("%.2f", density));

						Set<Item> items = manStore.getItemList();
						catItemModel.removeAllElements();
						for (Item i : items) {
							if (manStore.getItemCategory(i).equals(catName))
								catItemModel.addElement(i.getName());
						}

						if (tabbedPane.indexOfTab("Category Details") == -1)
							tabbedPane.insertTab("Category Details", null, categoryDetailPanel, null,
									tabbedPane.indexOfTab("Store Details") + 1);
						tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Category Details"));
					} else {
						JOptionPane.showMessageDialog(categoryDetailButton, "You are currently not managing a store.");
					}
				} else {
					JOptionPane.showMessageDialog(categoryDetailButton, "No category was selected.");
				}
			}
		});
		
		// delete user from Full User List
		adminDelUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowInd = fullUserTable.getSelectedRow();
				if (rowInd >= 0) {
					String username = (String) fullUserTable.getValueAt(rowInd, 0);
					if (!(username == null || username.isEmpty())) {
						userInterested = system.getUserByName(username);
						if (!username.equals(user.getUsername())) {
							int response = JOptionPane.showConfirmDialog(adminDelUserButton,
									"Are you sure you want to delete " + username + " from the system?",
									"Confirm User Deletion from System", JOptionPane.YES_NO_OPTION);
							if (response == JOptionPane.YES_OPTION) {
								if (system.removeUser(username).getResponse() == DeleteResponse.SUCCESS) {
									JOptionPane.showMessageDialog(adminDelUserButton,
											"Deleted the user " + username + " from the system.");
									int deets = tabbedPane.indexOfTab("User Details");
									if (deets != -1)
										tabbedPane.removeTabAt(deets);
									userListModel.remove(userInterested);
									userListModel.update();
									if (userInterested.getClass() == Manager.class)
										manListModel.remove(userInterested);
									manListModel.update();
									if (manStoreListModel.contains(username))
										manStoreListModel.removeElement(username);
									userInterested = null;
									tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Full User List"));
								} else {
									JOptionPane.showMessageDialog(adminDelUserButton,
											"Couldn't delete the user " + username + " from the system.");
								}
							}
						} else {
							JOptionPane.showMessageDialog(adminDelUserButton,
									"You can only delete your own account from the Account Management tab.");
						}
					} else {
						JOptionPane.showMessageDialog(adminDelUserButton, "No user was selected.");
					}
				} else {
					JOptionPane.showMessageDialog(adminDelUserButton, "No user was selected.");
				}
			}
		});
		
		// delete user from Manager List
		adminDelManButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowInd = managerTable.getSelectedRow();
				if (rowInd >= 0) {
					String username = (String) managerTable.getValueAt(rowInd, 0);
					if (!(username == null || username.isEmpty())) {
						userInterested = system.getUserByName(username);
						if (!username.equals(user.getUsername())) {
							int response = JOptionPane.showConfirmDialog(adminDelManButton,
									"Are you sure you want to delete " + username + " from the system?",
									"Confirm User Deletion from System", JOptionPane.YES_NO_OPTION);
							if (response == JOptionPane.YES_OPTION) {
								if (system.removeUser(username).getResponse() == DeleteResponse.SUCCESS) {
									JOptionPane.showMessageDialog(adminDelManButton,
											"Deleted the user " + username + " from the system.");
									int deets = tabbedPane.indexOfTab("User Details");
									if (deets != -1)
										tabbedPane.removeTabAt(deets);
									userListModel.remove(userInterested);
									userListModel.update();
									if (userInterested.getClass() == Manager.class)
										manListModel.remove(userInterested);
									manListModel.update();
									if (manStoreListModel.contains(username))
										manStoreListModel.removeElement(username);
									userInterested = null;
									tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Manager List"));
								} else {
									JOptionPane.showMessageDialog(adminDelManButton,
											"Couldn't delete the user " + username + " from the system.");
								}
							}
						} else {
							JOptionPane.showMessageDialog(adminDelManButton,
									"You can only delete your own account from the Account Management tab.");
						}
					} else {
						JOptionPane.showMessageDialog(adminDelManButton, "No user was selected.");
					}
				} else {
					JOptionPane.showMessageDialog(adminDelManButton, "No user was selected.");
				}
			}
		});
		
		// view user details
		adminViewDetailsUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowInd = fullUserTable.getSelectedRow();
				if (rowInd >= 0) {
					String username = (String) fullUserTable.getValueAt(rowInd, 0);
					if (!(username == null || username.isEmpty())) {
						dispUserDetails(username);
					}
					else {
						JOptionPane.showMessageDialog(adminDelUserButton, "No user was selected.");
					}
				}
				else {
					JOptionPane.showMessageDialog(adminDelUserButton, "No user was selected.");
				}
			}
		});
		
		// go to add user window from user list
		adminAddUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminGUIAddUser addUser = new AdminGUIAddUser();
				addUser.getUser(user);
				frAdminHome.dispose();
				addUser.setVisible(true);
				frAdminHome.setVisible(false);
			}
		});
		
		// go to add user window from manager list
		adminAddManButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminGUIAddUser addUser = new AdminGUIAddUser();
				addUser.getUser(user);
				addUser.setUserManager();
				frAdminHome.dispose();
				addUser.setVisible(true);
				frAdminHome.setVisible(false);
			}
		});
		
		// delete user from user details
		delAccountDetailsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (userInterested != null) {
					String username = userInterested.getUsername();
					if (!username.equals(user.getUsername())) {
						int response = JOptionPane.showConfirmDialog(adminDelUserButton, "Are you sure you want to delete " + username + " from the system?","Confirm User Deletion from System", JOptionPane.YES_NO_OPTION);
						if (response == JOptionPane.YES_OPTION) {
							if (system.removeUser(username).getResponse() == DeleteResponse.SUCCESS) {
								JOptionPane.showMessageDialog(adminDelUserButton, "Deleted the user " + username + " from the system.");
								tabbedPane.removeTabAt(tabbedPane.indexOfTab("User Details"));
								userListModel.remove(userInterested);
								userListModel.update();
								if (userInterested.getClass() == Manager.class)
									manListModel.remove(userInterested);
								manListModel.update();
								if (manStoreListModel.contains(username))
									manStoreListModel.removeElement(username);
								userInterested = null;
								tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Full User List"));
							}
							else {
								JOptionPane.showMessageDialog(adminDelUserButton, "Couldn't delete the user " + username + " from the system.");
							}
						}
					}
					else {
						JOptionPane.showMessageDialog(adminDelUserButton, "You can only delete your own account from the Account Management tab.");
					}
				}
				else {
					JOptionPane.showMessageDialog(adminDelUserButton, "No user is selected.");
				}
			}
		});
		
		// go to separate window to change user's store
		changeUserStoreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (userInterested != null) {
					AdminGUIChooseStore changeUserStore = new AdminGUIChooseStore();
					changeUserStore.getUser(user);
					changeUserStore.getUserOfInterest(userInterested);
					frAdminHome.dispose();
					changeUserStore.setVisible(true);
					frAdminHome.setVisible(false);
				}
			}
		});
		
		// remove user's store from user details
		resetUserStoreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (userInterested != null) {
					if (userInterested.getClass() == Manager.class || userInterested.getClass() == Administrator.class) {
						Manager m = (Manager) userInterested;
						Store manStore = m.getManagedStore();
						if (user.removeManager(m, manStore)) {
							JOptionPane.showMessageDialog(resetUserStoreButton, "Removed " + userInterested.getUsername() + " from store " + manStore.getAddress());
							userListModel.update();
							if (manStoreListModel.contains(m.getUsername()))
								manStoreListModel.removeElement(m.getUsername());
						}
						else
							JOptionPane.showMessageDialog(resetUserStoreButton, "Couldn't remove " + userInterested.getUsername() + " from store " + manStore.getAddress() + ".\nMaybe they're already not working there?");
					}
					else
						JOptionPane.showMessageDialog(resetUserStoreButton, userInterested.setPrefStore(null));
					userListModel.update();
					manStoreLabel_DETAILS.setText("N/A");
				}
			}
		});
		
		// remove manager from store on mgr list
		mgrRemFromStoreListButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowInd = managerTable.getSelectedRow();
				if (rowInd >= 0) {
					String username = (String) managerTable.getValueAt(rowInd, 0);
					if (!(username == null || username.isEmpty())) {
						Manager m = (Manager) system.getUserByName(username);
						Store mStore = m.getManagedStore();
						if (user.removeManager(m, mStore)) {
							userListModel.update();
							manListModel.update();
							if (manStoreListModel.contains(username))
								manStoreListModel.removeElement(username);
							if (userInterested != null && userInterested.getUsername().equals(username))
								initUserDetails(username);
							JOptionPane.showMessageDialog(mgrRemFromStoreListButton,
									"Removed manager " + username + " from their store.");
						} else {
							JOptionPane.showMessageDialog(mgrRemFromStoreListButton,
									"Couldn't remove manager " + username + " from their store. Maybe they're already not managing a store.");
						}
					} else {
						JOptionPane.showMessageDialog(mgrRemFromStoreListButton, "No manager was selected.");
					}
				} else {
					JOptionPane.showMessageDialog(mgrRemFromStoreListButton, "No manager was selected.");
				}
			}
		});
		
		// view manager from manager list
		adminViewManDetailsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowInd = managerTable.getSelectedRow();
				if (rowInd >= 0) {
					String username = (String) managerTable.getValueAt(rowInd, 0);
					if (!(username == null || username.isEmpty())) {
						dispUserDetails(username);
					}
					else {
						JOptionPane.showMessageDialog(adminDelUserButton, "No user was selected.");
					}
				}
				else {
					JOptionPane.showMessageDialog(adminDelUserButton, "No user was selected.");
				}
			}
		});
		
		// view manager from store list
		managerDetailsFromStoreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String manName = managerStoreList.getSelectedValue();
				if (!(manName == null || manName.isEmpty())) {
					dispUserDetails(manName);
				} else {
					JOptionPane.showMessageDialog(managerDetailsFromStoreButton, "No manager was selected.");
				}
			}
		});
		
		// see system item details from system item list
		sysItemViewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String itemName = sysItemList.getSelectedValue();
				if (!(itemName == null || itemName.isEmpty())) {
					dispSysItemDetails(itemName);
				} else {
					JOptionPane.showMessageDialog(saleItemDetailButton, "No item was selected.");
				}
			}
		});
		
		// see system item details from store item details
		goToSysItemDetailButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sysItemInterested = storeItemInterested;
				dispSysItemDetails(sysItemInterested.getName());
			}
		});
		
		// see system item details from store item list
		storeItemSysDetailButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowInd = storeItemTable.getSelectedRow();
				if (rowInd >= 0) {
					String itemName = (String) storeItemTable.getValueAt(rowInd, 0);
					if (!(itemName == null || itemName.isEmpty())) {
						dispSysItemDetails(itemName);
					} else {
						JOptionPane.showMessageDialog(saleItemDetailButton, "No item was selected.");
					}
				}
				else {
					JOptionPane.showMessageDialog(saleItemDetailButton, "No item was selected.");
				}
			}
		});
		
		// change system item details
		saveItemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (sysItemInterested != null) {
					String oldName = sysItemInterested.getName();
					String itemName = sysItemNameField.getText();
					if (!(itemName == null || itemName.isEmpty())) {
						sysItemInterested.setName(itemName);

						String sizeStr = sysSizeField.getText();
						if (!(sizeStr == null || sizeStr.isEmpty())) {
							sysItemInterested.setSize(Double.parseDouble(sizeStr));
						} else {
							sysItemInterested.setSize(0.0);
						}

						String priceStr = sysPriceField.getText();
						if (!(priceStr == null || priceStr.isEmpty())) {
							sysItemInterested.setPrice(Double.parseDouble(priceStr));
						} else {
							sysItemInterested.setPrice(0.0);
						}

						sysItemInterested.setDescription(sysDescField.getText());
						storeItemModel.update();
						JOptionPane.showMessageDialog(adminDelUserButton, "Changed item details.");
						sysItemModel.removeElement(oldName);
						sysItemModel.addElement(itemName);
						if (sysItemInterested.equals(storeItemInterested))
							initStoreItemDetails(itemName);
						initSysItemDetails(itemName);
					} else {
						JOptionPane.showMessageDialog(adminDelUserButton, "Item name can't be blank.");
					}
				} else {
					JOptionPane.showMessageDialog(adminDelUserButton, "Somehow, you're trying to edit nothing.");
				}
			}
		});
		
		// delete system item from details page
		sysItemRemDetailsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (sysItemInterested != null) {
					String itemName = sysItemInterested.getName();
					if (!(itemName == null || itemName.isEmpty())) {
						boolean b = user.removeItemFromSystem(sysItemInterested);
						if (b) {
							JOptionPane.showMessageDialog(adminDelUserButton, "Deleted item " + itemName + ".");
							sysItemModel.removeElement(itemName);
							int ind = tabbedPane.indexOfTab("Store Item Details");
							if (ind != -1)
								tabbedPane.removeTabAt(ind);
							tabbedPane.removeTabAt(tabbedPane.indexOfTab("System Item Details"));
							storeItemModel.remove(sysItemInterested);
							saleItemModel.remove(sysItemInterested);
							if (storeItemInterested != null && storeItemInterested.equals(sysItemInterested))
								storeItemInterested = null;
							sysItemInterested = null;
							tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("System Items List"));
						} else {
							JOptionPane.showMessageDialog(adminDelUserButton, "Failed to delete item " + itemName + ".");
						}
					} else {
						JOptionPane.showMessageDialog(adminDelUserButton, "Item name can't be blank.");
					}
				} else {
					JOptionPane.showMessageDialog(adminDelUserButton, "Somehow, you're trying to delete nothing.");
				}
			}
		});
		
		// delete system item from system items list page
		sysItemRemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String itemName = sysItemList.getSelectedValue();
				if (!(itemName == null || itemName.isEmpty())) {
					sysItemInterested = system.getItemByName(itemName);
					boolean b = user.removeItemFromSystem(sysItemInterested);
					if (b) {
						JOptionPane.showMessageDialog(sysItemRemButton, "Deleted item " + itemName + ".");
						sysItemModel.removeElement(itemName);
						int ind = tabbedPane.indexOfTab("Store Item Details");
						if (ind != -1)
							tabbedPane.removeTabAt(ind);
						ind = tabbedPane.indexOfTab("System Item Details");
						if (ind != -1)
							tabbedPane.removeTabAt(ind);
						storeItemModel.remove(sysItemInterested);
						saleItemModel.remove(sysItemInterested);
						if (storeItemInterested != null && storeItemInterested.equals(sysItemInterested))
							storeItemInterested = null;
						sysItemInterested = null;
						tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("System Items List"));
					} else {
						JOptionPane.showMessageDialog(sysItemRemButton, "Failed to delete item " + itemName + ".");
					}
				} else {
					JOptionPane.showMessageDialog(sysItemRemButton, "No item was selected.");
				}
			}
		});
		
		// add store to system from full store list
		addStoreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminGUIAddStore addStore = new AdminGUIAddStore();
				addStore.getUser(user);
				frAdminHome.dispose();
				addStore.setVisible(true);
				frAdminHome.setVisible(false);
			}
		});
		
		// select store to manage
		selectManStoreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String storeName = fullStoreList.getSelectedValue();
				if (!(storeName == null || storeName.isEmpty())) {
					manStore = system.getStoreByExactAddress(storeName);
					user.changeMySelectedStore(manStore);
					JOptionPane.showMessageDialog(selectManStoreButton, "Now managing the " + storeName + " store.");
					setUpManagerTabs();
					tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Store Item List"));
				} else {
					JOptionPane.showMessageDialog(selectManStoreButton, "No store was selected.");
				}
			}
		});
		
		// delete store from store list page
		remStoreFromListButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String storeName = fullStoreList.getSelectedValue();
				if (!(storeName == null || storeName.isEmpty())) {
					Store delStore = system.getStoreByExactAddress(storeName);
					boolean b = user.removeStore(delStore);
					if (b) {
						JOptionPane.showMessageDialog(remStoreFromListButton, "Store at " + delStore.getAddress() + " was deleted successfully.");
						storeModel.removeElement(delStore.getAddress());
						if (!(manStore == null) && manStore.equals(delStore)) {
							manStore = null;
							setUpManagerTabs();
							removeManagerTabs();
						}
						userListModel.update();
						manListModel.update();
					}
					else {
						JOptionPane.showMessageDialog(remStoreFromListButton, "Couldn't delete store " + delStore.getAddress() + ".");
					}
				} else {
					JOptionPane.showMessageDialog(remStoreFromListButton, "No store was selected.");
				}
			}
		});
		
		// delete store from store details page
		remStoreFromDetailsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (manStore != null) {
					boolean b = user.removeStore(manStore);
					if (b) {
						JOptionPane.showMessageDialog(remStoreFromListButton, "Store at " + manStore.getAddress() + " was deleted successfully.");
						storeModel.removeElement(manStore.getAddress());
						manStore = null;
						setUpManagerTabs();
						removeManagerTabs();
						userListModel.update();
						manListModel.update();
						tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Full Store List"));
					}
					else {
						JOptionPane.showMessageDialog(remStoreFromListButton, "Couldn't delete store " + manStore.getAddress() + ".");
					}
				}
				else {
					JOptionPane.showMessageDialog(remStoreFromListButton, "You are not currently managing a store.");
				}
			}
		});
		
		// add item to system from system item list
		sysItemAddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminGUIAddItem addItem = new AdminGUIAddItem();
				addItem.getUser(user);
				frAdminHome.dispose();
				addItem.setVisible(true);
				frAdminHome.setVisible(false);
			}
		});
		
		// remove manager from store details
		remManagerFromStoreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String manName = managerStoreList.getSelectedValue();
				if (!(manName == null || manName.isEmpty())) {
					Manager m = (Manager) system.getUserByName(manName);
					if (user.removeManager(m, manStore)) {
						userListModel.update();
						manListModel.update();
						if (manStoreListModel.contains(manName))
							manStoreListModel.removeElement(manName);
						if (userInterested != null && userInterested.getUsername().equals(manName))
							initUserDetails(manName);
						JOptionPane.showMessageDialog(remManagerFromStoreButton, "Removed manager " + manName + " from the store.");
					}
					else {
						JOptionPane.showMessageDialog(remManagerFromStoreButton, "Couldn't remove " + userInterested.getUsername() + " from store " + manStore.getAddress() + ".\nMaybe they're already not working there?");
					}
				} else {
					JOptionPane.showMessageDialog(remManagerFromStoreButton, "No manager was selected.");
				}
			}
		});
		
		// unset store of Admin
		resetManStoreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manStore = null;
				setUpManagerTabs();
				removeManagerTabs();
				JOptionPane.showMessageDialog(resetManStoreButton, "Store deselected.");
			}
		});
		
		initUserTables();
	}
	
	private void dispUserDetails(String username) {
		initUserDetails(username);

		if (tabbedPane.indexOfTab("User Details") == -1)
			tabbedPane.insertTab("User Details", null, userDetailPanel, null, tabbedPane.indexOfTab("Manager List")+1);
		tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("User Details"));
		// tabbedPane.addTab("Item Details", null, itemDetails, null);
	}
	
	private void dispSysItemDetails(String itemName) {
		initSysItemDetails(itemName);

		if (tabbedPane.indexOfTab("System Item Details") == -1)
			tabbedPane.insertTab("System Item Details", null, sysItemDetailPanel, null,
					tabbedPane.indexOfTab("System Items List") + 1);
		tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("System Item Details"));
	}

	private void showManagerTabs() {
		if (manStore != null) {
			tabbedPane.insertTab("Store Item List", null, storeItemPanel,
					"View all Items sold in Store. Select Items to remove or modify, or add an Item to sell in Store",tabbedPane.indexOfTab("Full Store List")+1);
			tabbedPane.insertTab("On Sale List", null, saleListPanel, null,tabbedPane.indexOfTab("Store Item List")+1);
			tabbedPane.insertTab("Store Details", null, storePropertiesPanel,"View and modify open/close times and categories",tabbedPane.indexOfTab("On Sale List")+1);
		}
	}

	private void removeManagerTabs() {
		String[] manTabNames = {"Store Item List","On Sale List","Store Details","Category Details","Store Item Details"};
		for (int i = 0; i < manTabNames.length; i++) {
			int ind = tabbedPane.indexOfTab(manTabNames[i]);
			if (ind != -1)
				tabbedPane.removeTabAt(ind);
		}
	}

	private void initUserTables() {
		Set<String> userStr = system.getUserList();
		for (String s : userStr)
			userListModel.add(system.getUserByName(s));
		for (String s : userStr) {
			User u = system.getUserByName(s);
			if (u.getClass() == Manager.class)
				manListModel.add(u);
		}
	}

	public void getUser(User a) {
		user = (Administrator) a;
		manStore = user.getManagedStore();

		// populate user details
		usernameLabel.setText(user.getUsername());

		if (user.hasTwoFactor())
			twoFactorLabel.setText("Active");
		else
			twoFactorLabel.setText("Inactive");

		setUpManagerTabs();
	}
	
	private void setUpManagerTabs() {
		if (manStore != null) {
			manStoreLabel.setText(user.getManagedStore().getAddress());
			
			// reset tables and lists
			storeItemModel.removeAll();
			saleItemModel.removeAll();
			catModel.removeAllElements();
			categoryComboBox.removeAll();
			manStoreListModel.removeAllElements();
			
			storeItemModel.add(manStore.getItemList());
			saleItemModel.add(manStore.getSaleList());
			LinkedList<String> cats = manStore.getCategories();
			cats.remove("entrance");
			cats.remove("cashier");
			for (String c : cats) {
				catModel.addElement(c);
				categoryComboBox.addItem(c);
			}
			openTimeField.setText(UtilitiesGUI.convertIntToTime(manStore.getStoreOpenTime()));
			closeTimeField.setText(UtilitiesGUI.convertIntToTime(manStore.getStoreCloseTime()));
			
			Set<Manager> managers = manStore.getManagerList();
			for (Manager m : managers) {
				manStoreListModel.add(manStoreListModel.size(), m.getUsername());
			}
			
			// hide store item details and category details; they won't be the same at the other store.
			int ind = tabbedPane.indexOfTab("Store Item Details");
			if (ind != -1)
				tabbedPane.removeTabAt(ind);
			ind = tabbedPane.indexOfTab("Category Details");
			if (ind != -1)
				tabbedPane.removeTabAt(ind);
			
			showManagerTabs();
		} else {
			manStoreLabel.setText("Currently not managing a store");
			removeManagerTabs();
		}
	}
	
	private void initSysItemDetails(String itemName) {
		sysItemInterested = system.getItemByName(itemName);
		sysItemNameField.setText(itemName);
		sysSizeField.setText("" + sysItemInterested.getSize());
		sysPriceField.setText("" + sysItemInterested.getPrice());
		sysDescField.setText(sysItemInterested.getDescription());
		searchFreqLabel.setText("" + sysItemInterested.getSearchFreq());
	}

	private void initStoreItemDetails(String itemName) {
		storeItemInterested = manStore.getItemByName(itemName);
		itemNameLabel.setText(storeItemInterested.getName());
		descriptionLabel.setText(storeItemInterested.getDescription());
		sizeLabel.setText("" + storeItemInterested.getSize());
		priceLabel.setText("$" + String.format("%.2f", manStore.getItemPrice(storeItemInterested)));
		currStockLabel.setText("" + manStore.getItemAvail(storeItemInterested));
		String cat = manStore.getItemCategory(storeItemInterested);
		currCategoryLabel.setText(cat);
		categoryComboBox.setSelectedItem(cat);
		if (manStore.isOnSale(storeItemInterested)) {
			saleStatusLabel.setText(
					"Yes, reduced by " + String.format("%.2f", manStore.getItemReduction(storeItemInterested) * 100) + "%");
		} else {
			saleStatusLabel.setText("No");
		}
	}
	
	private void initUserDetails(String username) {
		userInterested = system.getUserByName(username);
		usernameLabel_DETAILS.setText(userInterested.getUsername());
		userTypeLabel.setText(userInterested.getClass().getSimpleName());
		Store store = null;
		if (userInterested.getClass() == Manager.class || userInterested.getClass() == Administrator.class) {
			Manager m = (Manager) userInterested;
			store = m.getManagedStore();
		}
		else {
			store = userInterested.getPrefStore();
		}
		if (store != null)
			manStoreLabel_DETAILS.setText(store.getAddress());
		else
			manStoreLabel_DETAILS.setText("N/A");
	}

	public void setVisible(boolean b) {
		frAdminHome.setVisible(b);
	}

	// adapted from https://stackoverflow.com/a/25150803
	// shopping cart table layout requires me making an inner class of CustomerHome
	class StoreItemModel extends AbstractTableModel {
		protected final String[] COLUMN_NAMES = { "Item Name", "Category Name", "Total Stock" };
		private List<Item> rowData;

		public StoreItemModel() {
			rowData = new ArrayList<>();
		}

		public void update() {
			fireTableDataChanged();
		}

		private void add(Set<Item> storeItems) {
			rowData.addAll(storeItems);
			fireTableDataChanged();
		}

		private void remove(Item item) {
			rowData.remove(item);
			fireTableDataChanged();
		}

		private void removeAll() {
			rowData.clear();
		}

		private void add(Item item) {
			if (!rowData.contains(item)) {
				rowData.add(item);
				fireTableDataChanged();
			}
		}

		@Override
		public int getRowCount() {
			return rowData.size();
		}

		@Override
		public int getColumnCount() {
			return COLUMN_NAMES.length;
		}

		@Override
		public String getColumnName(int column) {
			return COLUMN_NAMES[column];
		}

		public Item getItemDataAt(int row) {
			return rowData.get(row);
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Item item = getItemDataAt(rowIndex);
			Object value = null;
			switch (columnIndex) {
			case 0:
				value = item.getName();
				break;
			case 1:
				value = manStore.getItemCategory(item);
				break;
			case 2:
				value = manStore.getItemAvail(item);
				break;
			}
			return value;
		}
	}

	class SaleItemModel extends AbstractTableModel {
		protected final String[] COLUMN_NAMES = { "Item Name", "Sale Price ($)", "Price Reduction By (%)",
				"Total Stock" };
		private List<Item> rowData;

		public SaleItemModel() {
			rowData = new ArrayList<>();
		}

		public void update() {
			fireTableDataChanged();
		}

		private void removeAll() {
			rowData.clear();
		}

		private void add(Set<Item> storeItems) {
			rowData.addAll(storeItems);
			fireTableDataChanged();
		}

		private void remove(Item item) {
			rowData.remove(item);
			fireTableDataChanged();
		}

		private void add(Item item) {
			if (!rowData.contains(item)) {
				rowData.add(item);
				fireTableDataChanged();
			}
		}

		@Override
		public int getRowCount() {
			return rowData.size();
		}

		@Override
		public int getColumnCount() {
			return COLUMN_NAMES.length;
		}

		@Override
		public String getColumnName(int column) {
			return COLUMN_NAMES[column];
		}

		public Item getItemDataAt(int row) {
			return rowData.get(row);
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Item item = getItemDataAt(rowIndex);
			Object value = null;
			switch (columnIndex) {
			case 0:
				value = item.getName();
				break;
			case 1:
				value = String.format("%.2f", manStore.getItemPrice(item));
				break;
			case 2:
				value = manStore.getItemReduction(item) * 100;
				break;
			case 3:
				value = manStore.getItemAvail(item);
				break;
			}
			return value;
		}
	}

	class UserListModel extends AbstractTableModel {
		protected final String[] COLUMN_NAMES = { "Username", "User Type", "Associated Store" };
		private List<User> rowData;

		public UserListModel() {
			rowData = new ArrayList<>();
		}

		public void update() {
			fireTableDataChanged();
		}

		private void removeAll() {
			rowData.clear();
		}

		private void add(Set<User> users) {
			rowData.addAll(users);
			fireTableDataChanged();
		}

		private void remove(User user) {
			rowData.remove(user);
			fireTableDataChanged();
		}

		private void add(User user) {
			if (!rowData.contains(user)) {
				rowData.add(user);
				fireTableDataChanged();
			}
		}

		@Override
		public int getRowCount() {
			return rowData.size();
		}

		@Override
		public int getColumnCount() {
			return COLUMN_NAMES.length;
		}

		@Override
		public String getColumnName(int column) {
			return COLUMN_NAMES[column];
		}

		public User getItemDataAt(int row) {
			return rowData.get(row);
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			User user = getItemDataAt(rowIndex);
			Object value = null;
			switch (columnIndex) {
			case 0:
				value = user.getUsername();
				break;
			case 1:
				value = user.getClass().getSimpleName();
				break;
			case 2:
				if (user.getClass() == Administrator.class)
					value = "SysAdmin";
				else if (user.getClass() == Manager.class) {
					Manager m = (Manager) user;
					Store s = m.getManagedStore();
					if (s != null)
						value = s.getAddress();
				} else {
					Store s = user.getPrefStore();
					if (s != null)
						value = s.getAddress();
				}
				break;
			}
			return value;
		}
	}
	
	public void setUserOfInterest(User user) {
		userInterested = user;
		if (userInterested != null)
			initUserDetails(userInterested.getUsername());
	}
	
	public void showUserDetails(User user) {
		if (user != null) {
			String username = user.getUsername();
			if (!(username == null || username.isEmpty()))
				dispUserDetails(username);
		}
	}

	// need this to return to certain tabs after changing stuff
	public void showWhichTab(String tabName) {
		tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabName));
	}
	
	public void setSysItemOfInterest(Item item) {
		sysItemInterested = item;
	}
	
	public void showSysItemDetails(Item item) {
		if (item != null) {
			String itemName = item.getName();
			if (!(itemName == null) || itemName.isEmpty())
				dispSysItemDetails(itemName);
		}
	}
}
