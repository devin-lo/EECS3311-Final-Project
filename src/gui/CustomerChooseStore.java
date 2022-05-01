package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.List;

import javax.swing.JFrame;

import base.User;
import client.Customer;
import corporate.ShopperSystem;
import corporate.Item;
import corporate.Store;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.text.NumberFormatter;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class CustomerChooseStore {

	private JFrame frmPrefStoreSettings;
	private Customer user;
	private ShopperSystem system;
	private DefaultListModel<String> storeModel;
	private JLabel prefStoreLabel;
	private JTextField locationField;
	private JList<String> storeList;
	private JFormattedTextField distField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CustomerChooseStore window = new CustomerChooseStore();
					window.frmPrefStoreSettings.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CustomerChooseStore() {
		system = ShopperSystem.getInstance();
		storeModel = new DefaultListModel<String>();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPrefStoreSettings = new JFrame();
		frmPrefStoreSettings.setTitle("Preferred Store Settings");
		frmPrefStoreSettings.setBounds(100, 100, 450, 500);
		frmPrefStoreSettings.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmPrefStoreSettings.getContentPane().add(panel, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("Current Preferred Store:");
		
		prefStoreLabel = new JLabel("prefHere");
		
		JScrollPane scrollPane = new JScrollPane();
		
		ActionListener goBack = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmPrefStoreSettings.dispose();
				CustomerHome custHome = new CustomerHome();
				custHome.getUser(user);
				custHome.showWhichTab("Account Management");
				custHome.setVisible(true);
				frmPrefStoreSettings.setVisible(false);
			}
		};
		
		JButton submitButton = new JButton("Set Preferred Store");
		
		JButton clearButton = new JButton("Clear settings");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				user.unsavePrefStore();
				JOptionPane.showMessageDialog(clearButton, "Store unsaved. Returning to Account Management...");
				goBack.actionPerformed(e);
			}
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(goBack);
		
		locationField = new JTextField();
		locationField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Search near location:");
		
		JButton locationSearchButton = new JButton("SEARCH");
		
		JLabel lblNewLabel_2 = new JLabel("Range:");
		
		// https://stackoverflow.com/a/16228698
		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(Integer.MAX_VALUE);
		formatter.setAllowsInvalid(false);
		
		distField = new JFormattedTextField(formatter);
		distField.setHorizontalAlignment(SwingConstants.RIGHT);
		distField.setColumns(10);
		distField.setText("20");
		
		JLabel lblNewLabel_3 = new JLabel("km");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addGap(18)
					.addComponent(prefStoreLabel)
					.addContainerGap(244, Short.MAX_VALUE))
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblNewLabel_2)
							.addGap(18)
							.addComponent(distField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(locationField, GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(locationSearchButton)
						.addComponent(lblNewLabel_3))
					.addContainerGap())
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_1)
					.addContainerGap(322, Short.MAX_VALUE))
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(154)
					.addComponent(submitButton)
					.addContainerGap(153, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap(176, Short.MAX_VALUE)
					.addComponent(clearButton)
					.addGap(167))
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(185)
					.addComponent(cancelButton)
					.addContainerGap(184, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(prefStoreLabel))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblNewLabel_1)
					.addGap(12)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(locationField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(locationSearchButton))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(distField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_3)
						.addComponent(lblNewLabel_2))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 223, GroupLayout.PREFERRED_SIZE)
					.addGap(29)
					.addComponent(submitButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(clearButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cancelButton)
					.addContainerGap())
		);
		
		storeList = new JList<String>(storeModel);
		storeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(storeList);
		panel.setLayout(gl_panel);
		
		locationSearchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int range = Integer.parseInt(distField.getText()); // is not actually used lol
				String query = locationField.getText();
				List<Store> searchResult = user.searchLocations(query, range);
				storeModel = new DefaultListModel<String>(); // https://stackoverflow.com/a/16774255
				for (Store s : searchResult) {
					storeModel.addElement(s.getAddress());
				}
				storeList.setModel(storeModel);
			}
		});
		
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String storeName = storeList.getSelectedValue();
				if (!(storeName.isEmpty())) {
					Store myStore = system.getStoreByExactAddress(storeName);
					if (myStore != null) {
						String result = user.setPrefStore(myStore);
						JOptionPane.showMessageDialog(submitButton, result + "Returning to Account Management...");
						goBack.actionPerformed(e);
					}
					else {
						JOptionPane.showMessageDialog(submitButton, "Couldn't save the preferred store. Perhaps try searching for a store again?");
					}
				}
				else {
					JOptionPane.showMessageDialog(submitButton, "No store was selected.");
				}
			}
		});
		
		// https://stackoverflow.com/a/13731739
		frmPrefStoreSettings.getRootPane().setDefaultButton(locationSearchButton);
	}

	public void getUser(User c) {
		user = (Customer) c;
		if (user.getPrefStore() != null)
			prefStoreLabel.setText(user.getPrefStore().getAddress());
		else
			prefStoreLabel.setText("No preferred store selected");
	}
	
	public void setVisible(boolean b) {
		frmPrefStoreSettings.setVisible(b);
	}
}
