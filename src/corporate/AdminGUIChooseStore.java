// THIS CLASS NEEDS TO BE CHANGED - IT'S FOR EDITING OTHER USERS' STORES

package corporate;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;

import base.User;
import client.Customer;

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

public class AdminGUIChooseStore {

	private JFrame frmAdminStoreSettings;
	private Administrator user;
	private ShopperSystem system;
	private DefaultListModel<String> storeModel;
	private JLabel prefStoreLabel;
	private JList<String> storeList;
	private User userInterested;
	private JButton submitButton;
	private JLabel usernameField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminGUIChooseStore window = new AdminGUIChooseStore();
					window.frmAdminStoreSettings.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AdminGUIChooseStore() {
		system = ShopperSystem.getInstance();
		storeModel = new DefaultListModel<String>();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAdminStoreSettings = new JFrame();
		frmAdminStoreSettings.setTitle("Admin: Change User's Store Setting");
		frmAdminStoreSettings.setBounds(100, 100, 450, 500);
		frmAdminStoreSettings.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmAdminStoreSettings.getContentPane().add(panel, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("Current Modifying this User:");
		
		prefStoreLabel = new JLabel("prefHere");
		
		JScrollPane scrollPane = new JScrollPane();
		
		ActionListener goBack = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminGUIHome adminHome = new AdminGUIHome();
				adminHome.getUser(user);
				adminHome.setUserOfInterest(userInterested);
				adminHome.showUserDetails(userInterested);
				adminHome.setVisible(true);
				frmAdminStoreSettings.dispose();
				frmAdminStoreSettings.setVisible(false);
			}
		};
		
		submitButton = new JButton("Set Preferred Store");
		
		JButton clearButton = new JButton("Clear settings");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				storeSetter(null);
				JOptionPane.showMessageDialog(clearButton, "Store unsaved. Returning to Account Management...");
				goBack.actionPerformed(e);
			}
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(goBack);
		
		JLabel lblNewLabel_1 = new JLabel("Full Store List:");
		
		// https://stackoverflow.com/a/16228698
		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(Integer.MAX_VALUE);
		formatter.setAllowsInvalid(false);
		
		JLabel lblNewLabel_2 = new JLabel("User's Current Store:");
		
		usernameField = new JLabel("usernameHere");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(154)
					.addComponent(submitButton)
					.addContainerGap(153, Short.MAX_VALUE))
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap(168, Short.MAX_VALUE)
					.addComponent(clearButton)
					.addGap(167))
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(185)
					.addComponent(cancelButton)
					.addContainerGap(184, Short.MAX_VALUE))
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_1)
					.addContainerGap(356, Short.MAX_VALUE))
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblNewLabel_2)
							.addGap(50)
							.addComponent(prefStoreLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
							.addComponent(lblNewLabel)
							.addGap(18)
							.addComponent(usernameField, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(81, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(usernameField))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_2)
						.addComponent(prefStoreLabel))
					.addGap(18)
					.addComponent(lblNewLabel_1)
					.addPreferredGap(ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 239, GroupLayout.PREFERRED_SIZE)
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
		Set<Store> stores = system.getStores();
		for (Store s : stores) {
			storeModel.add(storeModel.size(), s.getAddress());
		}
		
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String storeName = storeList.getSelectedValue();
				if (!(storeName.isEmpty())) {
					Store myStore = system.getStoreByExactAddress(storeName);
					if (myStore != null) {
						JOptionPane.showMessageDialog(submitButton, storeSetter(myStore));
						goBack.actionPerformed(e);
					}
					else {
						JOptionPane.showMessageDialog(submitButton, "Couldn't select that store to manage. Perhaps try searching for a store again?");
					}
				}
				else {
					JOptionPane.showMessageDialog(submitButton, "No store was selected.");
				}
			}
		});
	}

	public void getUser(User c) {
		user = (Administrator) c;
		
	}
	
	public void getUserOfInterest(User user) {
		userInterested = user;
		if (userInterested != null) {
			usernameField.setText(user.getUsername());
			Store store = null;
			if (userInterested.getClass() == (Manager.class) || userInterested.getClass() == Administrator.class) {
				Manager m = (Manager) userInterested;
				store = m.getManagedStore();
			}
			else {
				store = userInterested.getPrefStore();
			}
			if (store != null)
				prefStoreLabel.setText(store.getAddress());
			else
				prefStoreLabel.setText("None set");
		}
	}
	
	public void setVisible(boolean b) {
		frmAdminStoreSettings.setVisible(b);
	}
	
	private String storeSetter(Store store) {
		if (userInterested.getClass() == (Manager.class) || userInterested.getClass() == Administrator.class) {
			Manager m = (Manager) userInterested;
			if (store == null) {
				if (user.removeManager(m, m.getManagedStore()));
					return "Cleared the store setting successfully";
			}
			else {
				boolean b = true;
				if(m.managedStore != null) {
					b = user.removeManager(m, m.managedStore);
				}
				b = b && user.addManager(m,store);
				if (b)
					return "Set the store successfully";
			}
		}
		else {
			String what = userInterested.setPrefStore(store);
			return what;
		}
		return "Failed to set the store";
	}
}
