package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.LinkedHashMap;
import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;

import base.User;
import client.Customer;
import corporate.Item;
import corporate.Store;
import corporate.ShopperSystem;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.NumberFormatter;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Font;
import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class CustomerHome {

	private JFrame frmSmartshoppersCustomerInterface;
	private Customer user;
	private ShopperSystem system;
	private JTextField homeSearchField;
	private static final int BYNAME = 0;
	private static final int BYCAT = 1;
	private int searchInt;
	private JTextField searchField_RESULT;
	private DefaultListModel<String> searchModel;
	private DefaultListModel<String> suggestedModel;
	private ShoppingCartModel cartModel;
	private JFormattedTextField newQuantityField;
	private BestOrderModel bestOrderModel;

	private String totalAmountString = "$0.00";
	private JLabel totalPriceLabel;
	private JList<String> searchResultList;

	private JLabel itemNameLabel = new JLabel("nameHere");
	private JLabel descriptionLabel = new JLabel("descHere");
	private JLabel sizeLabel = new JLabel("sizeHere");
	private JLabel priceLabel = new JLabel("priceHere");
	private JLabel quantityLabel = new JLabel("quantHere");
	private JLabel categoryLabel = new JLabel("catHere");
	private JLabel noPrefStoreWarnLabel = new JLabel();
	private JTabbedPane tabbedPane;
	private JLabel usernameLabel;
	private JLabel twoFactorLabel;
	private JLabel prefStoreLabel;
	private Item itemInterested;
	private JLabel inCartLabel;
	private JFormattedTextField addQuantityField;
	private JTable bestOrderTable;
	private JLabel lblYouNeedTo;
	private JLabel timeNeededLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CustomerHome window = new CustomerHome();
					window.frmSmartshoppersCustomerInterface.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CustomerHome() {
		searchModel = new DefaultListModel<String>();
		suggestedModel = new DefaultListModel<String>();
		cartModel = new ShoppingCartModel();
		bestOrderModel = new BestOrderModel();
		user = null;
		system = ShopperSystem.getInstance();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// totalPriceLabel on Shopping Cart tab
		totalPriceLabel = new JLabel();
		totalPriceLabel.setText("$0.00");

		// JFrame
		frmSmartshoppersCustomerInterface = new JFrame();
		frmSmartshoppersCustomerInterface.setTitle("SmartShoppers Customer Interface");
		frmSmartshoppersCustomerInterface.setBounds(100, 100, 720, 500);
		frmSmartshoppersCustomerInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// tab container
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmSmartshoppersCustomerInterface.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		// https://www.javatpoint.com/java-jtabbedpane

		// home, search, and item panel declarations
		JPanel customerHome = new JPanel();
		JPanel searchResults = new JPanel();
		JPanel itemDetails = new JPanel();
		tabbedPane.add("Customer Home", customerHome);

		// home page components
		JLabel lblNewLabel = new JLabel("Suggested Items For You");
		JLabel lblNewLabel_1 = new JLabel("Search for Something:");

		// search functionality components declaration
		// is for both the home and search pages
		
		// could this be useful? making enter run the same action as button - https://stackoverflow.com/a/4420301
		homeSearchField = new JTextField();
		homeSearchField.setColumns(10);

		searchField_RESULT = new JTextField();
		searchField_RESULT.setColumns(10);

		JRadioButton nameRadioButton = new JRadioButton("Name");
		JRadioButton catRadioButton = new JRadioButton("Category");

		JRadioButton nameRadioButton_RESULT = new JRadioButton("Name");
		JRadioButton catRadioButton_RESULT = new JRadioButton("Category");

		nameRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchInt = BYNAME;
				nameRadioButton_RESULT.setSelected(true);
			}
		});
		nameRadioButton_RESULT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchInt = BYNAME;
				nameRadioButton.setSelected(true);
			}
		});
		nameRadioButton.setSelected(true);
		nameRadioButton_RESULT.setSelected(true);

		catRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchInt = BYCAT;
				catRadioButton_RESULT.setSelected(true);
			}
		});

		catRadioButton_RESULT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchInt = BYCAT;
				catRadioButton.setSelected(true);
			}
		});

		// Group the radio buttons.
		// https://docs.oracle.com/javase/tutorial/uiswing/components/button.html#radiobutton
		ButtonGroup searchTypeHome = new ButtonGroup();
		searchTypeHome.add(nameRadioButton);
		searchTypeHome.add(catRadioButton);

		ButtonGroup searchTypeResults = new ButtonGroup();
		searchTypeResults.add(nameRadioButton_RESULT);
		searchTypeResults.add(catRadioButton_RESULT);

		// search button declarations for the home and search pages
		// their ActionEvents are defined later
		JButton searchButton = new JButton("SEARCH");
		JButton searchButton_RESULT = new JButton("SEARCH");

		// remaining home page components
		JScrollPane suggestedItemsScroll = new JScrollPane();
		JButton getSuggestedItemDetails = new JButton("View Item Details");

		// home page layout
		GroupLayout gl_customerHome = new GroupLayout(customerHome);
		gl_customerHome.setHorizontalGroup(gl_customerHome.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_customerHome.createSequentialGroup().addContainerGap()
						.addGroup(gl_customerHome.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_customerHome.createSequentialGroup().addComponent(lblNewLabel_1)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(homeSearchField, GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(nameRadioButton)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(catRadioButton)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(searchButton))
								.addComponent(lblNewLabel)
								.addComponent(suggestedItemsScroll, GroupLayout.PREFERRED_SIZE, 659,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(getSuggestedItemDetails))
						.addContainerGap()));
		gl_customerHome.setVerticalGroup(gl_customerHome.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_customerHome.createSequentialGroup().addContainerGap()
						.addGroup(gl_customerHome.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel_1)
								.addComponent(homeSearchField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(nameRadioButton).addComponent(catRadioButton).addComponent(searchButton))
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(lblNewLabel)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(suggestedItemsScroll, GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(getSuggestedItemDetails)
						.addContainerGap(33, Short.MAX_VALUE)));

		// list on the home page
		JList<String> suggestedItems = new JList<String>(suggestedModel);
		suggestedItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		suggestedItemsScroll.setViewportView(suggestedItems);
		customerHome.setLayout(gl_customerHome);

		// search page components
		JLabel lblNewLabel_1_1 = new JLabel("Search for Something:");
		JLabel lblNewLabel_2 = new JLabel("Search Results");
		JScrollPane searchResultScroll = new JScrollPane();
		JButton getSearchItemDetails = new JButton("View Item Details");

		// search result list component
		searchResultList = new JList<String>(searchModel);
		searchResultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		searchResultScroll.setViewportView(searchResultList);

		// search page layout
		GroupLayout gl_searchResults = new GroupLayout(searchResults);
		gl_searchResults.setHorizontalGroup(gl_searchResults.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_searchResults.createSequentialGroup().addContainerGap().addGroup(gl_searchResults
						.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_searchResults.createSequentialGroup().addComponent(lblNewLabel_1_1)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(searchField_RESULT, GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(nameRadioButton_RESULT)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(catRadioButton_RESULT)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(searchButton_RESULT))
						.addComponent(lblNewLabel_2)
						.addComponent(searchResultScroll, GroupLayout.PREFERRED_SIZE, 659, GroupLayout.PREFERRED_SIZE)
						.addComponent(getSearchItemDetails)).addContainerGap()));
		gl_searchResults.setVerticalGroup(gl_searchResults.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_searchResults.createSequentialGroup().addContainerGap()
						.addGroup(gl_searchResults.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel_1_1)
								.addComponent(searchField_RESULT, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(nameRadioButton_RESULT).addComponent(catRadioButton_RESULT)
								.addComponent(searchButton_RESULT))
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(lblNewLabel_2)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(searchResultScroll, GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(getSearchItemDetails)
						.addContainerGap(33, Short.MAX_VALUE)));

		searchResults.setLayout(gl_searchResults);
		
		// uncomment to make search results tab visible in Design
		// tabbedPane.addTab("Search Results", null, searchResults, null);
		
		// item details components
		JLabel lblNewLabel_8 = new JLabel("Item Name:");
		JLabel lblNewLabel_9 = new JLabel("Description:");
		JLabel lblNewLabel_10 = new JLabel("Size:");
		JLabel lblNewLabel_11 = new JLabel("Unit Price at Preferred Store:");
		JLabel lblNewLabel_12 = new JLabel("Amount Available at Preferred Store:");
		JLabel lblNewLabel_13 = new JLabel("Category at Preferred Store:");
		
		// https://stackoverflow.com/a/16228698
		NumberFormat format = NumberFormat.getInstance();
	    NumberFormatter formatter = new NumberFormatter(format);
	    formatter.setValueClass(Integer.class);
	    formatter.setMinimum(0);
	    formatter.setMaximum(999);
	    formatter.setAllowsInvalid(false);
		
	    addQuantityField = new JFormattedTextField(formatter);
		addQuantityField.setHorizontalAlignment(SwingConstants.RIGHT);
		addQuantityField.setText("1");
		addQuantityField.setColumns(10);
		
		JButton addCartButton = new JButton("Add to Cart");
		
		
		JLabel lblNewLabel_16 = new JLabel("Amount to add to cart:");
		
		JLabel lblNewLabel_17 = new JLabel("Amount you already have in cart:");
		
		inCartLabel = new JLabel("inCartHere");
		
		JLabel lblNewLabel_18 = new JLabel("Note: items in your cart are not held for you. If someone checks out before you, then the store's stock will be changed.");
		lblNewLabel_18.setFont(new Font("Tahoma", Font.BOLD, 11));

		// item details layout
		GroupLayout gl_itemDetails = new GroupLayout(itemDetails);
		gl_itemDetails.setHorizontalGroup(
			gl_itemDetails.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_itemDetails.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_itemDetails.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_itemDetails.createSequentialGroup()
							.addComponent(lblNewLabel_8)
							.addGap(18)
							.addComponent(itemNameLabel))
						.addGroup(gl_itemDetails.createSequentialGroup()
							.addGroup(gl_itemDetails.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel_9)
								.addComponent(lblNewLabel_10))
							.addGap(18)
							.addGroup(gl_itemDetails.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_itemDetails.createSequentialGroup()
									.addComponent(sizeLabel)
									.addPreferredGap(ComponentPlacement.RELATED, 491, Short.MAX_VALUE)
									.addComponent(inCartLabel))
								.addGroup(gl_itemDetails.createSequentialGroup()
									.addComponent(descriptionLabel)
									.addPreferredGap(ComponentPlacement.RELATED, 378, Short.MAX_VALUE)
									.addComponent(lblNewLabel_17))))
						.addGroup(gl_itemDetails.createSequentialGroup()
							.addGroup(gl_itemDetails.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel_12)
								.addComponent(lblNewLabel_11)
								.addComponent(lblNewLabel_13))
							.addGap(18)
							.addGroup(gl_itemDetails.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_itemDetails.createSequentialGroup()
									.addComponent(priceLabel)
									.addPreferredGap(ComponentPlacement.RELATED, 307, Short.MAX_VALUE)
									.addComponent(lblNewLabel_16))
								.addGroup(gl_itemDetails.createSequentialGroup()
									.addGroup(gl_itemDetails.createParallelGroup(Alignment.LEADING)
										.addComponent(quantityLabel)
										.addComponent(categoryLabel))
									.addPreferredGap(ComponentPlacement.RELATED, 323, Short.MAX_VALUE)
									.addGroup(gl_itemDetails.createParallelGroup(Alignment.TRAILING)
										.addComponent(addCartButton)
										.addComponent(addQuantityField, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)))))
						.addComponent(lblNewLabel_18))
					.addContainerGap())
		);
		gl_itemDetails.setVerticalGroup(
			gl_itemDetails.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_itemDetails.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_itemDetails.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_8)
						.addComponent(itemNameLabel))
					.addGap(18)
					.addGroup(gl_itemDetails.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_9)
						.addComponent(descriptionLabel)
						.addComponent(lblNewLabel_17))
					.addGap(18)
					.addGroup(gl_itemDetails.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_10)
						.addComponent(sizeLabel)
						.addComponent(inCartLabel))
					.addGap(73)
					.addGroup(gl_itemDetails.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_11)
						.addComponent(priceLabel)
						.addComponent(lblNewLabel_16))
					.addGap(18)
					.addGroup(gl_itemDetails.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_12)
						.addComponent(quantityLabel)
						.addComponent(addQuantityField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_itemDetails.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_13)
						.addComponent(categoryLabel)
						.addComponent(addCartButton))
					.addGap(18)
					.addComponent(lblNewLabel_18)
					.addContainerGap(24, Short.MAX_VALUE))
		);
		itemDetails.setLayout(gl_itemDetails);

		// uncomment to show item details tab in design tab
		// tabbedPane.addTab("Item Details", null, itemDetails, null);

		// shopping cart tab declaration
		JPanel shoppingCart = new JPanel();
		tabbedPane.addTab("Shopping Cart", null, shoppingCart, "View the items in your cart.");

		// shopping cart components
		JButton getCartItemDetails = new JButton("View Item Details");

		// cart table view - important!! Might need this for best order
		JTable cartTable = new JTable(cartModel);
		cartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// https://stackoverflow.com/a/25150803

		JScrollPane tableScroll = new JScrollPane(cartTable);
		JButton updateQuantityButton = new JButton("Update Quantity");
		

		// this component is an attribute of the class
		newQuantityField = new JFormattedTextField(formatter);
		newQuantityField.setColumns(10);

		JLabel lblNewLabel_4 = new JLabel("Total to Pay:");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 11));
		JButton removeItemButton = new JButton("Remove selected item");
		JLabel lblNewLabel_3 = new JLabel("New quantity for selected item:");
		noPrefStoreWarnLabel.setFont(new Font("Tahoma", Font.BOLD, 11));

		// This component is an attribute of the class
		noPrefStoreWarnLabel.setText("You need to select a preferred store before we show a cart!");
		
		JButton cartRefreshButton = new JButton("Refresh Cart");
		
		// shopping cart layout
		GroupLayout gl_shoppingCart = new GroupLayout(shoppingCart);
		gl_shoppingCart.setHorizontalGroup(
			gl_shoppingCart.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_shoppingCart.createSequentialGroup()
					.addGap(32)
					.addGroup(gl_shoppingCart.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_shoppingCart.createSequentialGroup()
							.addComponent(cartRefreshButton)
							.addGap(53)
							.addComponent(noPrefStoreWarnLabel))
						.addGroup(gl_shoppingCart.createParallelGroup(Alignment.LEADING, false)
							.addComponent(tableScroll, GroupLayout.PREFERRED_SIZE, 619, GroupLayout.PREFERRED_SIZE)
							.addGroup(gl_shoppingCart.createSequentialGroup()
								.addGroup(gl_shoppingCart.createParallelGroup(Alignment.TRAILING)
									.addComponent(getCartItemDetails, Alignment.LEADING)
									.addComponent(removeItemButton, Alignment.LEADING))
								.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblNewLabel_3)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_shoppingCart.createParallelGroup(Alignment.TRAILING)
									.addComponent(newQuantityField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(lblNewLabel_4))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_shoppingCart.createParallelGroup(Alignment.TRAILING)
									.addComponent(updateQuantityButton)
									.addComponent(totalPriceLabel)))))
					.addContainerGap(48, Short.MAX_VALUE))
		);
		gl_shoppingCart.setVerticalGroup(
			gl_shoppingCart.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_shoppingCart.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_shoppingCart.createParallelGroup(Alignment.BASELINE)
						.addComponent(noPrefStoreWarnLabel)
						.addComponent(cartRefreshButton))
					.addGap(13)
					.addComponent(tableScroll, GroupLayout.PREFERRED_SIZE, 186, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addGroup(gl_shoppingCart.createParallelGroup(Alignment.BASELINE)
						.addComponent(getCartItemDetails, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblNewLabel_4)
						.addComponent(totalPriceLabel, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_shoppingCart.createParallelGroup(Alignment.BASELINE)
						.addComponent(removeItemButton)
						.addComponent(updateQuantityButton)
						.addComponent(newQuantityField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_3))
					.addContainerGap(19, Short.MAX_VALUE))
		);
		shoppingCart.setLayout(gl_shoppingCart);

		// best order tab declaration
		JPanel bestOrderPanel = new JPanel();
		tabbedPane.addTab("Best Order of Cart", null, bestOrderPanel,
				"The optimal order to get your items in, based on the layout of your preferred store");

		// best order labels
		JLabel lblNewLabel_14 = new JLabel("You should get your items in this order:");
		JLabel lblNewLabel_15 = new JLabel("At what time are you planning to visit?");

		// time input box
		// http://www.java2s.com/Tutorial/Java/0240__Swing/FormattedDateandTimeInputDateFormatgetTimeInstanceDateFormatSHORT.htm
		Format shortTime = DateFormat.getTimeInstance(DateFormat.SHORT);
		JFormattedTextField visitTimeField = new JFormattedTextField(shortTime);
		visitTimeField.setValue(new Date());
		visitTimeField.setColumns(20);
		visitTimeField.setHorizontalAlignment(SwingConstants.CENTER);
		visitTimeField.setToolTipText("Please input a time in a valid format (XX:XX a.m. or p.m.)");

		JButton makeBestOrderButton = new JButton("Submit");

		JScrollPane bestOrderScroll = new JScrollPane();
		
		lblYouNeedTo = new JLabel();
		lblYouNeedTo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblYouNeedTo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblYouNeedTo.setText("You need to select a preferred store before we show this!");
		
		timeNeededLabel = new JLabel("");
		
		JLabel lblNewLabel_19 = new JLabel("Total Time Needed:");
		
		// best order layout
		GroupLayout gl_bestOrderPanel = new GroupLayout(bestOrderPanel);
		gl_bestOrderPanel.setHorizontalGroup(
			gl_bestOrderPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_bestOrderPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_bestOrderPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(bestOrderScroll, GroupLayout.DEFAULT_SIZE, 679, Short.MAX_VALUE)
						.addGroup(gl_bestOrderPanel.createSequentialGroup()
							.addComponent(lblNewLabel_14)
							.addPreferredGap(ComponentPlacement.RELATED, 132, Short.MAX_VALUE)
							.addComponent(lblYouNeedTo, GroupLayout.PREFERRED_SIZE, 359, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_bestOrderPanel.createSequentialGroup()
							.addComponent(lblNewLabel_15)
							.addGap(18)
							.addComponent(visitTimeField, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
							.addGap(32)
							.addComponent(makeBestOrderButton))
						.addGroup(Alignment.TRAILING, gl_bestOrderPanel.createSequentialGroup()
							.addComponent(lblNewLabel_19)
							.addGap(117)
							.addComponent(timeNeededLabel)))
					.addContainerGap())
		);
		gl_bestOrderPanel.setVerticalGroup(
			gl_bestOrderPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_bestOrderPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_bestOrderPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_15)
						.addComponent(visitTimeField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(makeBestOrderButton))
					.addPreferredGap(ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
					.addGroup(gl_bestOrderPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_14)
						.addComponent(lblYouNeedTo))
					.addGap(10)
					.addComponent(bestOrderScroll, GroupLayout.PREFERRED_SIZE, 191, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_bestOrderPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(timeNeededLabel)
						.addComponent(lblNewLabel_19))
					.addGap(20))
		);
		
		bestOrderTable = new JTable(bestOrderModel);
		bestOrderScroll.setViewportView(bestOrderTable);
		bestOrderPanel.setLayout(gl_bestOrderPanel);

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
				frmSmartshoppersCustomerInterface.dispose();
				chgUsername.setVisible(true);
				frmSmartshoppersCustomerInterface.setVisible(false);
			}
		});
		JLabel lblNewLabel_7 = new JLabel("Two-Factor Authentication:");
		twoFactorLabel = new JLabel("2FAhere");
		JButton chgTFAButton = new JButton("Change 2FA Settings");
		chgTFAButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChangeTFA chgTFA = new ChangeTFA();
				chgTFA.getUser(user);
				frmSmartshoppersCustomerInterface.dispose();
				chgTFA.setVisible(true);
				frmSmartshoppersCustomerInterface.setVisible(false);
			}
		});
		
		JButton chgPWButton = new JButton("Change Password");
		chgPWButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChangePassword chgPW = new ChangePassword();
				chgPW.getUser(user);
				frmSmartshoppersCustomerInterface.dispose();
				chgPW.setVisible(true);
				frmSmartshoppersCustomerInterface.setVisible(false);
			}
		});
		
		JButton deleteAccountButton = new JButton("Delete My Account");
		deleteAccountButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeleteAccountPage del = new DeleteAccountPage();
				del.getUser(user);
				frmSmartshoppersCustomerInterface.dispose();
				del.setVisible(true);
				frmSmartshoppersCustomerInterface.setVisible(false);
			}
		});
		
		// customer-specific stuff
		JLabel lblNewLabel_6 = new JLabel("Preferred Store:");
		prefStoreLabel = new JLabel("prefStoreHere");
		JButton btnNewButton = new JButton("Change Preferred Store Setting");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CustomerChooseStore chooseStore = new CustomerChooseStore();
				chooseStore.getUser(user);
				frmSmartshoppersCustomerInterface.dispose();
				chooseStore.setVisible(true);
				frmSmartshoppersCustomerInterface.setVisible(false);
			}
		});
		
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
									.addComponent(usernameLabel, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE))
								.addComponent(chgUsernameButton))
							.addGap(55)
							.addGroup(gl_accountDetails.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_accountDetails.createSequentialGroup()
									.addComponent(lblNewLabel_7)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(twoFactorLabel))
								.addGroup(gl_accountDetails.createSequentialGroup()
									.addComponent(chgTFAButton)
									.addPreferredGap(ComponentPlacement.RELATED, 160, Short.MAX_VALUE)
									.addComponent(chgPWButton))))
						.addGroup(gl_accountDetails.createSequentialGroup()
							.addComponent(lblNewLabel_6)
							.addGap(18)
							.addComponent(prefStoreLabel, GroupLayout.PREFERRED_SIZE, 336, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnNewButton)
						.addComponent(deleteAccountButton))
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
						.addComponent(prefStoreLabel))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnNewButton)
					.addPreferredGap(ComponentPlacement.RELATED, 197, Short.MAX_VALUE)
					.addComponent(deleteAccountButton)
					.addContainerGap())
		);
		accountDetails.setLayout(gl_accountDetails);

		// logout menu
		JMenuBar menuBar = new JMenuBar();
		frmSmartshoppersCustomerInterface.setJMenuBar(menuBar);
		JMenu logoutMenu = new JMenu("Logout");
		menuBar.add(logoutMenu);
		JMenuItem logoutMenuItem = new JMenuItem("Logout", KeyEvent.VK_L);
		logoutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmSmartshoppersCustomerInterface.dispose();
				WelcomePage welcome = new WelcomePage();
				welcome.setVisible(true);
				frmSmartshoppersCustomerInterface.setVisible(false);
			}
		});
		logoutMenu.add(logoutMenuItem);

		// search button action (home page)
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String query = homeSearchField.getText();
				if (!(query == null || query.isEmpty())) {
					if (user.getPrefStore() != null) {
						searchField_RESULT.setText(query);
						List<Item> searchResult = doSearch(query);
						searchModel = new DefaultListModel<String>(); // https://stackoverflow.com/a/16774255
						for (Item i : searchResult) {
							searchModel.addElement(i.getName());
						}
						searchResultList.setModel(searchModel); // https://www.daniweb.com/posts/jump/1882259
						if (tabbedPane.indexOfTab("Search Results") == -1)
							tabbedPane.insertTab("Search Results", null, searchResults, null, 1);
						tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Search Results"));
					}
					else {
						JOptionPane.showMessageDialog(searchButton, "You haven't set a preferred store yet.");
					}
				}
			}
		});

		// search button action (result page)
		searchButton_RESULT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String query = searchField_RESULT.getText();
				if (!(query == null || query.isEmpty())) {
					if (user.getPrefStore() != null) {
						homeSearchField.setText(query);
						List<Item> searchResult = doSearch(query);
						searchModel = new DefaultListModel<String>(); // https://stackoverflow.com/a/16774255
						for (Item i : searchResult) {
							searchModel.addElement(i.getName());
						}
						searchResultList.setModel(searchModel); // https://www.daniweb.com/posts/jump/1882259
					}
					else {
						JOptionPane.showMessageDialog(searchButton_RESULT, "You haven't set a preferred store yet.");
					}
				}
			}
		});

		// item details getter (home page)
		getSuggestedItemDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String itemName = suggestedItems.getSelectedValue();
				if (!(itemName == null || itemName.isEmpty())) {
					if (user.getPrefStore() != null) {
						dispItemDetails(itemName);

						if (tabbedPane.indexOfTab("Item Details") == -1) {
							tabbedPane.insertTab("Item Details", null, itemDetails, null, tabbedPane.indexOfTab("Shopping Cart"));
						}
						tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Item Details"));
						// tabbedPane.addTab("Item Details", null, itemDetails, null);
					} else {
						JOptionPane.showMessageDialog(getSuggestedItemDetails, "You haven't set a preferred store yet.");
					}
				} else {
					JOptionPane.showMessageDialog(getSuggestedItemDetails, "No item was selected.");
				}
			}
		});
		
		// item details getter (cart)
		getCartItemDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowInd = cartTable.getSelectedRow();
				if (rowInd >= 0) {
					String itemName = (String) cartTable.getValueAt(rowInd, 0);
					if (!(itemName == null || itemName.isEmpty())) {
						if (user.getPrefStore() != null) {
							dispItemDetails(itemName);

							if (tabbedPane.indexOfTab("Item Details") == -1) {
								tabbedPane.insertTab("Item Details", null, itemDetails, null, tabbedPane.indexOfTab("Shopping Cart"));
							}
							tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Item Details"));
							// tabbedPane.addTab("Item Details", null, itemDetails, null);
						}
						else {
							JOptionPane.showMessageDialog(getCartItemDetails, "You haven't set a preferred store yet.");
						}
					}
					else {
						JOptionPane.showMessageDialog(getCartItemDetails, "No item was selected.");
					}
				}
				else {
					JOptionPane.showMessageDialog(getCartItemDetails, "No item was selected.");
				}
			}
		});
		// tabbedPane.addTab("Item Details", null, itemDetails, null);

		// item details getter (search page)
		getSearchItemDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String itemName = searchResultList.getSelectedValue();
				if (!(itemName == null || itemName.isEmpty())) {
					if (user.getPrefStore() != null) {
						dispItemDetails(itemName);

						if (tabbedPane.indexOfTab("Item Details") == -1) {
							tabbedPane.insertTab("Item Details", null, itemDetails, null, tabbedPane.indexOfTab("Shopping Cart"));
						}
						tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Item Details"));
						// tabbedPane.addTab("Item Details", null, itemDetails, null);
					} else {
						JOptionPane.showMessageDialog(getSearchItemDetails, "You haven't set a preferred store yet.");
					}
				} else {
					JOptionPane.showMessageDialog(getSearchItemDetails, "No item was selected.");
				}
			}
		});
		
		// add to cart from item details
		addCartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (user.getCart() != null) {
					if (itemInterested != null) {
						boolean hadItemBefore = user.isItemInCart(itemInterested);
						int quantity = Integer.parseInt(addQuantityField.getText());
						JOptionPane.showMessageDialog(addCartButton, user.addItemToCart(itemInterested, quantity));
						dispItemDetails(itemNameLabel.getText()); // update the details - quantity avail and quantity in cart will have changed
						if (!hadItemBefore) {
							cartModel.add(itemInterested);
						}
						cartModel.update();
						// tabbedPane.removeTabAt(tabbedPane.indexOfTab("Item Details"));
					}
					else {
						JOptionPane.showMessageDialog(addCartButton, "Somehow, you're trying to add nothing to your cart.");
					}
				}
				else {
					JOptionPane.showMessageDialog(addCartButton, "You don't have a cart, because you haven't set a preferred store yet.");
				}
			}
		});
		
		// remove item from cart
		removeItemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowInd = cartTable.getSelectedRow();
				if (rowInd >= 0) {
					String itemName = (String) cartTable.getValueAt(rowInd, 0);
					if (!(itemName == null || itemName.isEmpty())) {
						Store myStore = user.getPrefStore();
						if (myStore != null) {
							Item i = myStore.getItemByName(itemName);
							user.removeItemFromCart(i);
							JOptionPane.showMessageDialog(removeItemButton, "Removed item from cart.");
							cartModel.remove(i);
							// tabbedPane.removeTabAt(tabbedPane.indexOfTab("Item Details"));
						} else {
							JOptionPane.showMessageDialog(removeItemButton, "You haven't set a preferred store yet.");
						}
					} else {
						JOptionPane.showMessageDialog(removeItemButton, "No item was selected.");
					}
				}
				else {
					JOptionPane.showMessageDialog(removeItemButton, "No item was selected.");
				}
			}
		});
		
		// update quantity in cart
		updateQuantityButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rowInd = cartTable.getSelectedRow();
				if (rowInd >= 0) {
					String itemName = (String) cartTable.getValueAt(rowInd, 0);
					if (!(itemName.isEmpty())) {
						Store myStore = user.getPrefStore();
						if (myStore != null) {
							Item item = myStore.getItemByName(itemName);
							int newQuant = Integer.parseInt(newQuantityField.getText());
							String message = user.changeItemQuantityInCart(item, newQuant);
							cartModel.update();
							if (newQuant == 0) {
								cartModel.remove(item);
								message = "Item removed from cart.";
							}
							JOptionPane.showMessageDialog(updateQuantityButton, message);
							// tabbedPane.removeTabAt(tabbedPane.indexOfTab("Item Details"));
						} else {
							JOptionPane.showMessageDialog(updateQuantityButton, "You haven't set a preferred store yet.");
						}
					} else {
						JOptionPane.showMessageDialog(updateQuantityButton, "No item was selected.");
					}
				}
				else {
					JOptionPane.showMessageDialog(updateQuantityButton, "No item was selected.");
				}
			}
		});
		
		// make best order
		makeBestOrderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (user.getCart() != null) {
					bestOrderModel.reset();
					String timeStr = visitTimeField.getText();
					int time = UtilitiesGUI.convertTimeToInt(timeStr);
					
					LinkedHashMap<Item,Double> bestOrder = user.getBestOrder(time);
					List<Item> itemOrder = new ArrayList<Item>();
					List<Double> timeOrder = new ArrayList<Double>();
					double totalTime = 0.0;
					// https://stackoverflow.com/a/12310930
					for (Map.Entry<Item, Double> entry : bestOrder.entrySet()) {
						itemOrder.add(entry.getKey());
						timeOrder.add(entry.getValue());
						totalTime += entry.getValue();
					}
					bestOrderModel.add(itemOrder, timeOrder, totalTime);
				}
			}
		});
		
		// cart refresh
		cartRefreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cartModel.update();
				JOptionPane.showMessageDialog(cartRefreshButton, "Cart has been updated.");
			}
		});
	}

	// get item details
	private void dispItemDetails(String itemName) {
		Store myStore = user.getPrefStore();
		itemInterested = myStore.getItemByName(itemName);
		itemNameLabel.setText(itemInterested.getName());
		descriptionLabel.setText(itemInterested.getDescription());
		sizeLabel.setText("" + itemInterested.getSize());
		priceLabel.setText("$" + String.format("%.2f", myStore.getItemPrice(itemInterested)));
		quantityLabel.setText("" + myStore.getItemAvail(itemInterested));
		categoryLabel.setText(myStore.getItemCategory(itemInterested));
		inCartLabel.setText("" + user.getCart().getItemQuantity(itemInterested));
		addQuantityField.setText("1");
	}

	// get user info from login page Response
	public void getUser(User c) {
		user = (Customer) c;

		// populate suggestedItems
		List<Item> searchResult = system.suggestedItems(user);
		if (searchResult != null) {
			for (Item i : searchResult) {
				suggestedModel.addElement(i.getName());
			}
		}

		// populate cart
		checkPrefStore(); // TODO - if prefstore becomes null, then the cart has to be deleted. Also, add
							// mechanism for hiding and showing best order.
		if (user.getCart() != null) {
			cartModel.add(user.getCart().getItems());
		}
		
		// populate user details
		usernameLabel.setText(user.getUsername());

		if (user.hasTwoFactor())
			twoFactorLabel.setText("Active");
		else
			twoFactorLabel.setText("Inactive");
		if (user.getPrefStore() != null)
			prefStoreLabel.setText(user.getPrefStore().getAddress());
		else
			prefStoreLabel.setText("No preferred store selected");
	}

	public void setVisible(boolean b) {
		frmSmartshoppersCustomerInterface.setVisible(b);
	}

	// perform item search
	private List<Item> doSearch(String query) {
		List<Item> searchResult = null;
		Store myStore = user.getPrefStore();
		if (myStore != null) {
			if (query.isEmpty()) {
				searchResult = new ArrayList<Item>();
				searchResult.addAll(myStore.getItemList());
			} else if (searchInt == BYNAME) {
				searchResult = user.search(query, searchInt);
			} else if (searchInt == BYCAT) {
				searchResult = user.search(query, searchInt);
			}
		}
		return searchResult;
	}

	// check if user has a prefStore set, and toggle the shopping cart warning
	private void checkPrefStore() {
		if (user.getPrefStore() == null) {
			noPrefStoreWarnLabel.setVisible(true);
			lblYouNeedTo.setVisible(true);
		} else {
			noPrefStoreWarnLabel.setVisible(false);
			lblYouNeedTo.setVisible(false);
		}
	}

	// adapted from https://stackoverflow.com/a/25150803
	// shopping cart table layout requires me making an inner class of CustomerHome
	class ShoppingCartModel extends AbstractTableModel {
		protected final String[] COLUMN_NAMES = { "Item Name", "Price per Unit", "Quantity", "Total Price" };
		private List<Item> rowData;

		public ShoppingCartModel() {
			rowData = new ArrayList<>();
		}
		
		public void update() {
			updateTotalPrice();
			fireTableDataChanged();
			bestOrderModel.reset();
		}
		
		private void updateTotalPrice() {
			double updateTotal = 0.0;
			for (Item i : rowData)
				updateTotal += (user.getPrefStore().getItemPrice(i) * user.getCart().getItemQuantity(i));
			totalAmountString = "$" + String.format("%.2f", updateTotal);
			totalPriceLabel.setText(totalAmountString); // https://stackoverflow.com/a/6578222
		}

		private void add(Set<Item> cartItems) {
			rowData.addAll(cartItems);
			updateTotalPrice();
			fireTableDataChanged();
			bestOrderModel.reset();
		}
		
		private void remove(Item item) {
			rowData.remove(item);
			updateTotalPrice();
			fireTableDataChanged();
			bestOrderModel.reset();
		}
		
		private void add(Item item) {
			if (!rowData.contains(item)) {
				rowData.add(item);
				fireTableDataChanged();
			}
			updateTotalPrice();
			bestOrderModel.reset();
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
				value = user.getPrefStore().getItemPrice(item);
				break;
			case 2:
				value = user.getCart().getItemQuantity(item);
				break;
			case 3:
				value = user.getPrefStore().getItemPrice(item) * user.getCart().getItemQuantity(item);
				break;
			}
			return value;
		}
	}

	// adapted from https://stackoverflow.com/a/25150803
	// shopping cart table layout requires me making an inner class of CustomerHome
	class BestOrderModel extends AbstractTableModel {
		protected final String[] COLUMN_NAMES = { "Item Name", "Quantity", "Approx. Time to Obtain (Minutes)" };
		private List<Item> rowData;
		private List<Double> timeData;

		public BestOrderModel() {
			reset();
		}
		
		public void reset() {
			rowData = new ArrayList<>();
			timeData = new ArrayList<>();
			fireTableDataChanged();
		}
		
		private void add(List<Item> itemList, List<Double> timeList, double totalTime) {
			rowData.addAll(itemList);
			timeData.addAll(timeList);
			StringBuilder timeNeeded = new StringBuilder();
			int totalInt = (int) Math.round(totalTime);
			if (totalInt / 1440 > 0) {
				timeNeeded.append(totalInt / 1440 + " days ");
				totalInt %= 1440;
			}
			if (totalInt / 60 > 0) {
				timeNeeded.append(totalInt / 60 + " hours ");
				totalInt %= 60;
			}
			timeNeeded.append(totalInt + " min");
			
			timeNeededLabel.setText(timeNeeded.toString());
			fireTableDataChanged();
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

		// may need to fix this
		public Item getItemDataAt(int row) {
			return rowData.get(row);
		}
		
		private double getTimeDataAt(int row) {
			return timeData.get(row);
		}

		// need to fix this
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Item item = getItemDataAt(rowIndex);
			double time = getTimeDataAt(rowIndex);
			Object value = null;
			switch (columnIndex) {
			case 0:
				value = item.getName();
				break;
			case 1:
				value = user.getCart().getItemQuantity(item);
				break;
			case 2:
				value = String.format("%.2f", time);
				break;
			}
			return value;
		}
	}
	
	// need this to return to account management tab after changing stuff
	public void showWhichTab(String tabName) {
		tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Account Management"));
	}
}
