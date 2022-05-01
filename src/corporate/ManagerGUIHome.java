package corporate;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import base.User;
import exceptions.ItemStoreException;
import gui.ChangePassword;
import gui.ChangeTFA;
import gui.ChangeUsername;
import gui.CustomerChooseStore;
import gui.DeleteAccountPage;
import gui.UtilitiesGUI;
import gui.WelcomePage;

import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.NumberFormatter;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

public class ManagerGUIHome {

	private JFrame frManHome;
	private Manager user;
	private Store manStore;
	private ShopperSystem system;
	private Item itemInterested;

	private JTabbedPane tabbedPane;

	private JLabel usernameLabel;
	private JLabel twoFactorLabel;
	private JLabel manStoreLabel;
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

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ManagerGUIHome window = new ManagerGUIHome();
					window.frManHome.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ManagerGUIHome() {
		system = ShopperSystem.getInstance();
		storeItemModel = new StoreItemModel();
		saleItemModel = new SaleItemModel();
		catModel = new DefaultListModel<String>();
		catItemModel = new DefaultListModel<String>();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frManHome = new JFrame();
		frManHome.setTitle("SmartShoppers Management Module");
		frManHome.setBounds(100, 100, 900, 600);
		frManHome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// logout menu
		JMenuBar menuBar = new JMenuBar();
		frManHome.setJMenuBar(menuBar);
		JMenu logoutMenu = new JMenu("Logout");
		menuBar.add(logoutMenu);
		JMenuItem logoutMenuItem = new JMenuItem("Logout", KeyEvent.VK_L);
		logoutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frManHome.dispose();
				WelcomePage welcome = new WelcomePage();
				welcome.setVisible(true);
				frManHome.setVisible(false);
			}
		});
		logoutMenu.add(logoutMenuItem);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frManHome.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel storeItemPanel = new JPanel();
		tabbedPane.addTab("Store Item List", null, storeItemPanel,
				"View all Items sold in Store. Select Items to remove or modify, or add an Item to sell in Store");

		JLabel lblNewLabel = new JLabel("All Items sold at the Store:");

		JScrollPane storeItemScroll = new JScrollPane();

		JButton storeItemDetailButton = new JButton("View Item Details");
		JButton removeItemButton = new JButton("Remove Item");
		JButton addItemButton = new JButton("Add Item");

		GroupLayout gl_storeItemPanel = new GroupLayout(storeItemPanel);
		gl_storeItemPanel.setHorizontalGroup(gl_storeItemPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_storeItemPanel.createSequentialGroup().addContainerGap()
						.addGroup(gl_storeItemPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(storeItemScroll, GroupLayout.DEFAULT_SIZE, 759, Short.MAX_VALUE)
								.addComponent(lblNewLabel)
								.addGroup(gl_storeItemPanel.createSequentialGroup().addComponent(storeItemDetailButton)
										.addPreferredGap(ComponentPlacement.RELATED, 440, Short.MAX_VALUE)
										.addComponent(addItemButton).addGap(18).addComponent(removeItemButton)))
						.addContainerGap()));
		gl_storeItemPanel.setVerticalGroup(gl_storeItemPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_storeItemPanel.createSequentialGroup().addContainerGap().addComponent(lblNewLabel)
						.addGap(18)
						.addComponent(storeItemScroll, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
						.addGap(18)
						.addGroup(gl_storeItemPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(storeItemDetailButton).addComponent(removeItemButton)
								.addComponent(addItemButton))
						.addContainerGap(27, Short.MAX_VALUE)));

		storeItemTable = new JTable(storeItemModel);
		storeItemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		storeItemScroll.setViewportView(storeItemTable);
		storeItemPanel.setLayout(gl_storeItemPanel);

		JPanel saleListPanel = new JPanel();
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
									if (itemInterested.equals(rem)) {
										dispItemDetails(itemName);
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

		JPanel itemDetailsPanel = new JPanel();
		// uncomment to see item details in design tab
		// tabbedPane.addTab("Store Item Details", null, itemDetailsPanel, null);

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

		// https://stackoverflow.com/a/16228698
		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(Integer.MAX_VALUE);
		formatter.setAllowsInvalid(false);

		JFormattedTextField newStockField = new JFormattedTextField(formatter);
		newStockField.setHorizontalAlignment(SwingConstants.RIGHT);
		newStockField.setColumns(10);

		JButton updateStockButton = new JButton("Update Stock");
		updateStockButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (manStore != null) {
					int newStock = Integer.parseInt(newStockField.getText());
					if (manStore.updateItemStock(itemInterested, newStock)) {
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
					if (manStore.updateItemCategory(itemInterested, newCat)) {
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

		// https://stackoverflow.com/a/16228698
		NumberFormatter doubleFormatter = new NumberFormatter(format);
		doubleFormatter.setValueClass(Integer.class);
		doubleFormatter.setMinimum(0);
		doubleFormatter.setMaximum(100);
		doubleFormatter.setAllowsInvalid(false);

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
						if (manStore.remFromSaleList(itemInterested)) {
							JOptionPane.showMessageDialog(updateSaleButton, "Item removed from sale successfully.");
							saleStatusLabel.setText("No");
							storeItemModel.update();
							saleItemModel.remove(itemInterested);
						} else {
							JOptionPane.showMessageDialog(updateSaleButton, "Item sale status couldn't be updated.");
						}
					} else {
						if (manStore.addToSaleList(itemInterested, reduceBy)) {
							JOptionPane.showMessageDialog(updateSaleButton, "Item added to sale successfully.");
							saleStatusLabel.setText("Yes, reduced by " + String.format("%.2f", reduceBy * 100) + "%");
							storeItemModel.update();
							saleItemModel.add(itemInterested);
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

		// item details layout
		GroupLayout gl_itemDetails = new GroupLayout(itemDetailsPanel);
		gl_itemDetails
				.setHorizontalGroup(gl_itemDetails.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_itemDetails.createSequentialGroup().addContainerGap()
								.addGroup(gl_itemDetails.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_itemDetails.createSequentialGroup().addComponent(lblNewLabel_8)
												.addGap(18).addComponent(itemNameLabel))
										.addGroup(gl_itemDetails.createSequentialGroup()
												.addGroup(gl_itemDetails.createParallelGroup(Alignment.LEADING)
														.addComponent(lblNewLabel_9).addComponent(lblNewLabel_10)
														.addComponent(lblNewLabel_11))
												.addGap(18)
												.addGroup(gl_itemDetails.createParallelGroup(Alignment.LEADING)
														.addComponent(priceLabel).addComponent(sizeLabel)
														.addComponent(descriptionLabel)))
										.addGroup(gl_itemDetails.createSequentialGroup()
												.addGroup(gl_itemDetails.createParallelGroup(Alignment.LEADING)
														.addComponent(lblNewLabel_12).addComponent(lblNewLabel_13)
														.addComponent(lblNewLabel_4))
												.addGap(18)
												.addGroup(gl_itemDetails.createParallelGroup(Alignment.LEADING)
														.addComponent(currStockLabel).addComponent(currCategoryLabel)
														.addComponent(saleStatusLabel))
												.addGap(51)
												.addGroup(gl_itemDetails.createParallelGroup(Alignment.LEADING)
														.addComponent(lblNewLabel_2).addComponent(lblNewLabel_16)
														.addComponent(lblNewLabel_15))
												.addGap(18)
												.addGroup(gl_itemDetails.createParallelGroup(Alignment.LEADING, false)
														.addComponent(saleReductionField)
														.addComponent(categoryComboBox, Alignment.TRAILING, 0, 168,
																Short.MAX_VALUE)
														.addComponent(newStockField, Alignment.TRAILING)
														.addComponent(lblNewLabel_26))
												.addGap(33)
												.addGroup(gl_itemDetails.createParallelGroup(Alignment.LEADING)
														.addComponent(updateSaleButton)
														.addComponent(updateCategoryButton)
														.addComponent(updateStockButton))))
								.addContainerGap(59, Short.MAX_VALUE)));
		gl_itemDetails
				.setVerticalGroup(
						gl_itemDetails.createParallelGroup(Alignment.LEADING)
								.addGroup(
										gl_itemDetails.createSequentialGroup().addContainerGap()
												.addGroup(gl_itemDetails.createParallelGroup(Alignment.BASELINE)
														.addComponent(lblNewLabel_8).addComponent(itemNameLabel))
												.addGap(18)
												.addGroup(gl_itemDetails.createParallelGroup(Alignment.BASELINE)
														.addComponent(lblNewLabel_9).addComponent(descriptionLabel))
												.addGap(18)
												.addGroup(gl_itemDetails.createParallelGroup(Alignment.BASELINE)
														.addComponent(lblNewLabel_10).addComponent(sizeLabel))
												.addGap(18)
												.addGroup(gl_itemDetails.createParallelGroup(Alignment.BASELINE)
														.addComponent(lblNewLabel_11).addComponent(priceLabel))
												.addGap(73)
												.addGroup(gl_itemDetails.createParallelGroup(Alignment.BASELINE)
														.addComponent(lblNewLabel_12).addComponent(currStockLabel)
														.addComponent(lblNewLabel_16)
														.addComponent(newStockField, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(updateStockButton))
												.addGap(18)
												.addGroup(gl_itemDetails.createParallelGroup(Alignment.BASELINE)
														.addComponent(lblNewLabel_13).addComponent(currCategoryLabel)
														.addComponent(lblNewLabel_2)
														.addComponent(categoryComboBox, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(updateCategoryButton))
												.addGap(18)
												.addGroup(gl_itemDetails.createParallelGroup(Alignment.BASELINE)
														.addComponent(lblNewLabel_4).addComponent(saleStatusLabel)
														.addComponent(lblNewLabel_15)
														.addComponent(saleReductionField, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(updateSaleButton))
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(lblNewLabel_26).addContainerGap(87, Short.MAX_VALUE)));
		itemDetailsPanel.setLayout(gl_itemDetails);

		JPanel storePropertiesPanel = new JPanel();
		tabbedPane.addTab("Store Details", null, storePropertiesPanel,
				"View and modify open/close times and categories");

		JLabel lblNewLabel_3 = new JLabel("Opening Time:");

		JLabel lblNewLabel_14 = new JLabel("Closing Time:");

		// time input box
		// http://www.java2s.com/Tutorial/Java/0240__Swing/FormattedDateandTimeInputDateFormatgetTimeInstanceDateFormatSHORT.htm
		Format shortTime = DateFormat.getTimeInstance(DateFormat.SHORT);

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
				frManHome.dispose();
				addCat.setVisible(true);
				frManHome.setVisible(false);
			}
		});
		
		JLabel lblNewLabel_17 = new JLabel("NOTE: Removing a Category will remove the associated Items from your Store.");
		lblNewLabel_17.setFont(new Font("Tahoma", Font.BOLD, 11));
		GroupLayout gl_storePropertiesPanel = new GroupLayout(storePropertiesPanel);
		gl_storePropertiesPanel.setHorizontalGroup(
			gl_storePropertiesPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_storePropertiesPanel.createSequentialGroup()
					.addGroup(gl_storePropertiesPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_storePropertiesPanel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_storePropertiesPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(catScroll, GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE)
								.addGroup(gl_storePropertiesPanel.createSequentialGroup()
									.addComponent(lblNewLabel_3)
									.addGap(18)
									.addComponent(openTimeField, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(lblNewLabel_14)
									.addGap(18)
									.addComponent(closeTimeField, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
									.addComponent(updateHoursButton)
									.addGap(329))
								.addGroup(gl_storePropertiesPanel.createSequentialGroup()
									.addComponent(categoryDetailButton)
									.addPreferredGap(ComponentPlacement.RELATED, 486, Short.MAX_VALUE)
									.addComponent(addCatButton)
									.addGap(18)
									.addComponent(remCatButton))
								.addComponent(lblNewLabel_19)))
						.addGroup(gl_storePropertiesPanel.createSequentialGroup()
							.addGap(218)
							.addComponent(lblNewLabel_17)))
					.addContainerGap())
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
					.addComponent(lblNewLabel_19)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(catScroll, GroupLayout.PREFERRED_SIZE, 259, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_storePropertiesPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(categoryDetailButton)
						.addComponent(remCatButton)
						.addComponent(addCatButton))
					.addPreferredGap(ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
					.addComponent(lblNewLabel_17)
					.addGap(44))
		);

		catList = new JList<String>(catModel);
		catList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		catScroll.setViewportView(catList);
		storePropertiesPanel.setLayout(gl_storePropertiesPanel);

		JPanel categoryDetailPanel = new JPanel();
		// uncomment to show Category Details in Design Tab
		// tabbedPane.addTab("Category Details", null, categoryDetailPanel, null);

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
								JOptionPane.showMessageDialog(upDenButton, "Density for category " + catNameLabel.getText() + " has been updated.");
							}
							else {
								JOptionPane.showMessageDialog(upDenButton, "Density for category " + catNameLabel.getText() + " has NOT been updated.");
							}
						}
						else {
							JOptionPane.showMessageDialog(removeItemButton, "You're not managing a store right now.");
						}
					}
					else {
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
				frManHome.dispose();
				chgUsername.setVisible(true);
				frManHome.setVisible(false);
			}
		});
		JLabel lblNewLabel_7 = new JLabel("Two-Factor Authentication:");
		twoFactorLabel = new JLabel("2FAhere");
		JButton chgTFAButton = new JButton("Change 2FA Settings");
		chgTFAButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChangeTFA chgTFA = new ChangeTFA();
				chgTFA.getUser(user);
				frManHome.dispose();
				chgTFA.setVisible(true);
				frManHome.setVisible(false);
			}
		});

		JButton chgPWButton = new JButton("Change Password");
		chgPWButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChangePassword chgPW = new ChangePassword();
				chgPW.getUser(user);
				frManHome.dispose();
				chgPW.setVisible(true);
				frManHome.setVisible(false);
			}
		});

		JButton deleteAccountButton = new JButton("Delete My Account");
		deleteAccountButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeleteAccountPage del = new DeleteAccountPage();
				del.getUser(user);
				frManHome.dispose();
				del.setVisible(true);
				frManHome.setVisible(false);
			}
		});

		// manager's store info
		JLabel lblNewLabel_6 = new JLabel("Managed Store:");
		manStoreLabel = new JLabel("manStoreHere");

		// account details layout
		GroupLayout gl_accountDetails = new GroupLayout(accountDetails);
		gl_accountDetails.setHorizontalGroup(
			gl_accountDetails.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_accountDetails.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_accountDetails.createParallelGroup(Alignment.LEADING)
						.addComponent(deleteAccountButton)
						.addGroup(gl_accountDetails.createSequentialGroup()
							.addGroup(gl_accountDetails.createParallelGroup(Alignment.TRAILING, false)
								.addGroup(gl_accountDetails.createSequentialGroup()
									.addComponent(lblNewLabel_6)
									.addGap(18)
									.addComponent(manStoreLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addGroup(Alignment.LEADING, gl_accountDetails.createSequentialGroup()
									.addComponent(lblNewLabel_5)
									.addGap(18)
									.addComponent(usernameLabel, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE))
								.addComponent(chgUsernameButton, Alignment.LEADING))
							.addGap(46)
							.addGroup(gl_accountDetails.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_accountDetails.createSequentialGroup()
									.addComponent(lblNewLabel_7)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(twoFactorLabel))
								.addGroup(gl_accountDetails.createSequentialGroup()
									.addComponent(chgTFAButton)
									.addPreferredGap(ComponentPlacement.RELATED, 340, Short.MAX_VALUE)
									.addComponent(chgPWButton)))))
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
					.addPreferredGap(ComponentPlacement.RELATED, 331, Short.MAX_VALUE)
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
							dispItemDetails(itemName);

							if (tabbedPane.indexOfTab("Item Details") == -1)
								tabbedPane.insertTab("Item Details", null, itemDetailsPanel, null, 2);
							tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Item Details"));
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
							dispItemDetails(itemName);

							if (tabbedPane.indexOfTab("Item Details") == -1)
								tabbedPane.insertTab("Item Details", null, itemDetailsPanel, null, 2);
							tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Item Details"));
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
						dispItemDetails(itemName);

						if (tabbedPane.indexOfTab("Item Details") == -1)
							tabbedPane.insertTab("Item Details", null, itemDetailsPanel, null, 2);
						tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Item Details"));
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
				frManHome.dispose();
				addItem.setVisible(true);
				frManHome.setVisible(false);
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
									int itemDeet = tabbedPane.indexOfTab("Item Details");
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
		
		// update store hours
		updateHoursButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (manStore != null) {
					String openStr = openTimeField.getText();
					int openTime = UtilitiesGUI.convertTimeToInt(openStr);
					String closeStr = closeTimeField.getText();
					int closeTime = UtilitiesGUI.convertTimeToInt(closeStr);
					if (closeTime < openTime) {
						JOptionPane.showMessageDialog(updateHoursButton, "Invalid input - close time is earlier than open time!");
					}
					else {
						if (user.updateStoreOpenTime(openTime) && user.updateStoreCloseTime(closeTime)) {
							JOptionPane.showMessageDialog(updateHoursButton, "Store hours changed successfully.");
						}
						else {
							JOptionPane.showMessageDialog(updateHoursButton, "Store hours couldn't be changed!");
						}
					}
				}
				else {
					JOptionPane.showMessageDialog(removeItemButton, "You're not managing a store right now.");
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
							prev = cat.get(catInd-1);
						if (catInd != cat.size() - 1)
							next = cat.get(catInd+1);
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
							tabbedPane.insertTab("Category Details", null, categoryDetailPanel, null, tabbedPane.indexOfTab("Store Details")+1);
						tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Category Details"));
					}
					else {
						JOptionPane.showMessageDialog(categoryDetailButton, "You are currently not managing a store.");
					}
				}
				else {
					JOptionPane.showMessageDialog(categoryDetailButton, "No category was selected.");
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
							JOptionPane.showMessageDialog(categoryDetailButton, "Category " + catName + " and any associated Items have been removed.");
							catModel.removeElement(catName);
							storeItemModel.removeAll();
							storeItemModel.update();
							storeItemModel.add(manStore.getItemList());
							saleItemModel.removeAll();
							saleItemModel.add(manStore.getSaleList());
							saleItemModel.update();
							int itemDeet = tabbedPane.indexOfTab("Item Details");
							if (!(itemDeet == -1))
								tabbedPane.removeTabAt(itemDeet);
							int catDeet = tabbedPane.indexOfTab("Category Details");
							if (!(catDeet == -1))
								tabbedPane.removeTabAt(catDeet);
						}
						else
							JOptionPane.showMessageDialog(categoryDetailButton, "Category " + catName + " couldn't be removed, but its associated Items may have been removed.");
					}
					else {
						JOptionPane.showMessageDialog(categoryDetailButton, "You are currently not managing a store.");
					}
				}
				else {
					JOptionPane.showMessageDialog(categoryDetailButton, "No category was selected.");
				}
			}
		});

	}

	// get item details
	private void dispItemDetails(String itemName) {
		itemInterested = manStore.getItemByName(itemName);
		itemNameLabel.setText(itemInterested.getName());
		descriptionLabel.setText(itemInterested.getDescription());
		sizeLabel.setText("" + itemInterested.getSize());
		priceLabel.setText("$" + String.format("%.2f", manStore.getItemPrice(itemInterested)));
		currStockLabel.setText("" + manStore.getItemAvail(itemInterested));
		String cat = manStore.getItemCategory(itemInterested);
		currCategoryLabel.setText(cat);
		categoryComboBox.setSelectedItem(cat);
		if (manStore.isOnSale(itemInterested)) {
			saleStatusLabel.setText(
					"Yes, reduced by " + String.format("%.2f", manStore.getItemReduction(itemInterested) * 100) + "%");
		} else {
			saleStatusLabel.setText("No");
		}
	}

	// initialize items that require the Manager's attributes
	public void getUser(User m) {
		user = (Manager) m;
		manStore = user.getManagedStore();

		// populate user details
		usernameLabel.setText(user.getUsername());

		if (user.hasTwoFactor())
			twoFactorLabel.setText("Active");
		else
			twoFactorLabel.setText("Inactive");

		if (manStore != null) {
			manStoreLabel.setText(user.getManagedStore().getAddress());
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
		} else
			manStoreLabel.setText("Currently not managing a store");
	}

	public void setVisible(boolean b) {
		frManHome.setVisible(b);
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

	// need this to return to certain tabs after changing stuff in separate windows
	public void showWhichTab(String tabName) {
		tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(tabName));
	}
}
