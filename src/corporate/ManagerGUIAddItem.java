package corporate;

import java.awt.EventQueue;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.text.NumberFormatter;

import base.User;
import exceptions.ItemStoreException;
import gui.CustomerHome;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.awt.event.ActionEvent;

public class ManagerGUIAddItem {

	private JFrame frMgrAddItem;

	private Manager user;
	private Store manStore;
	private ShopperSystem system;
	private Item itemInterested;

	private DefaultListModel<String> itemsModel;
	private JComboBox<String> categoryComboBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ManagerGUIAddItem window = new ManagerGUIAddItem();
					window.frMgrAddItem.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ManagerGUIAddItem() {
		system = ShopperSystem.getInstance();
		itemsModel = new DefaultListModel<String>();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frMgrAddItem = new JFrame();
		frMgrAddItem.setTitle("Manager Add Item From System");
		frMgrAddItem.setBounds(100, 100, 500, 500);
		frMgrAddItem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frMgrAddItem.getContentPane().add(panel, BorderLayout.CENTER);

		JLabel lblNewLabel = new JLabel("Pick an Item from the System (added by Administrators):");

		JScrollPane fullItemScroll = new JScrollPane();

		JLabel lblNewLabel_1 = new JLabel("Initial Stock:");

		// https://stackoverflow.com/a/16228698
		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(999);
		formatter.setAllowsInvalid(false);

		JFormattedTextField newStockField = new JFormattedTextField(formatter);
		newStockField.setHorizontalAlignment(SwingConstants.RIGHT);

		JLabel lblNewLabel_2 = new JLabel("Category:");

		categoryComboBox = new JComboBox<String>();

		JButton submitButton = new JButton("Submit");
		JButton cancelButton = new JButton("Cancel");

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				gl_panel.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(fullItemScroll, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 464,
										Short.MAX_VALUE)
								.addComponent(lblNewLabel, Alignment.LEADING).addGroup(Alignment.LEADING,
										gl_panel.createSequentialGroup()
												.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
														.addComponent(lblNewLabel_1).addComponent(lblNewLabel_2))
												.addGap(18)
												.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
														.addComponent(categoryComboBox, 0, GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(newStockField, GroupLayout.PREFERRED_SIZE, 208,
																GroupLayout.PREFERRED_SIZE))
												.addPreferredGap(ComponentPlacement.RELATED, 114, Short.MAX_VALUE)
												.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
														.addComponent(submitButton).addComponent(cancelButton))))
						.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addContainerGap().addComponent(lblNewLabel)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addComponent(fullItemScroll, GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE).addGap(18)
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup().addGap(4)
								.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel_1)
										.addComponent(newStockField, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGap(18)
								.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel_2)
										.addComponent(categoryComboBox, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panel.createSequentialGroup().addComponent(submitButton).addGap(18)
								.addComponent(cancelButton)))
				.addContainerGap()));

		JList<String> fullItemList = new JList<String>(itemsModel);
		fullItemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fullItemScroll.setViewportView(fullItemList);
		panel.setLayout(gl_panel);
		Set<Item> itemSet = system.getSystemItemList();
		for (Item i : itemSet)
			itemsModel.addElement(i.getName());

		ActionListener goBack = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (user.getClass() == Administrator.class) {
					AdminGUIHome adminHome = new AdminGUIHome();
					adminHome.getUser(user);
					adminHome.showWhichTab("Store Item List");
					adminHome.setVisible(true);
				}
				else {
					ManagerGUIHome manHome = new ManagerGUIHome();
					manHome.getUser(user);
					manHome.showWhichTab("Store Item List");
					manHome.setVisible(true);
				}
				frMgrAddItem.dispose();
				frMgrAddItem.setVisible(false);
			}
		};

		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String itemName = fullItemList.getSelectedValue();
				if (!(itemName == null || itemName.isEmpty())) {
					if (manStore != null) {
						Item item = system.getItemByName(itemName);
						boolean b = false;
						int stock = 0;
						String stockStr = newStockField.getText();
						if (!(stockStr == null || stockStr.isEmpty()))
							stock = Integer.parseInt(stockStr);
						b = user.addItem(item, stock, (String) categoryComboBox.getSelectedItem());

						if (b) {
							JOptionPane.showMessageDialog(submitButton, "Successfully added the item to the store.");
							goBack.actionPerformed(e);
						} else {
							JOptionPane.showMessageDialog(submitButton, "Couldn't add the item to the Store. Either your Store has no categories, or your stock is negative.");
						}
					} else {
						JOptionPane.showMessageDialog(submitButton, "You are currently not managing a store.");
					}
				} else {
					JOptionPane.showMessageDialog(submitButton, "No item was selected.");
				}
			}
		});

		cancelButton.addActionListener(goBack);
	}

	// initialize items that require the Manager's attributes
	public void getUser(User m) {
		user = (Manager) m;
		manStore = user.getManagedStore();

		if (manStore != null) {
			LinkedList<String> cats = manStore.getCategories();
			for (String c : cats) {
				if (!(c.equals("entrance") || c.equals("cashier")))
					categoryComboBox.addItem(c);
			}
		}
	}

	public void setVisible(boolean b) {
		frMgrAddItem.setVisible(b);
	}
}
