package corporate;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JComboBox;
import javax.swing.JFrame;

import base.User;
import exceptions.ItemStoreException;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;

public class ManagerGUIAddCategory {

	private JFrame frmManagerAddCategory;
	
	private Manager user;
	private Store manStore;
	private ShopperSystem system;
	private JComboBox<String> categoryComboBox;
	private JLabel lblNewLabel;
	private JTextField categoryNameField;
	private JLabel lblNewLabel_1;
	private JButton submitButton;
	private JLabel lblNewLabel_2;
	private JFormattedTextField densityField;
	private JButton cancelButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ManagerGUIAddCategory window = new ManagerGUIAddCategory();
					window.frmManagerAddCategory.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ManagerGUIAddCategory() {
		system = ShopperSystem.getInstance();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmManagerAddCategory = new JFrame();
		frmManagerAddCategory.setTitle("Manager Add Category to Store");
		frmManagerAddCategory.setBounds(100, 100, 450, 300);
		frmManagerAddCategory.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmManagerAddCategory.getContentPane().add(panel, BorderLayout.CENTER);
		
		categoryComboBox = new JComboBox<String>();
		
		lblNewLabel = new JLabel("Category Name:");
		
		categoryNameField = new JTextField();
		categoryNameField.setColumns(10);
		
		lblNewLabel_1 = new JLabel("Preceding Category:");
		
		lblNewLabel_2 = new JLabel("Density (optional):");
		
		densityField = new JFormattedTextField();
		densityField.setHorizontalAlignment(SwingConstants.RIGHT);
		densityField.setText("1.0");
		
		ActionListener goBack = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (user.getClass() == Administrator.class) {
					AdminGUIHome adminHome = new AdminGUIHome();
					adminHome.getUser(user);
					adminHome.showWhichTab("Store Details");
					adminHome.setVisible(true);
				}
				else {
					ManagerGUIHome manHome = new ManagerGUIHome();
					manHome.getUser(user);
					manHome.showWhichTab("Store Details");
					manHome.setVisible(true);
				}
				frmManagerAddCategory.dispose();
				frmManagerAddCategory.setVisible(false);
			}
		};
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(goBack);

		submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String catName = categoryNameField.getText();
				if (!(catName == null || catName.isEmpty())) {
					if (manStore != null) {
						String prevName = (String) categoryComboBox.getSelectedItem();
						boolean b = false;
						if (prevName == null)
							b = manStore.addCategory(catName);
						else
							b = manStore.addCategory(catName, prevName);
						if (!densityField.getText().isEmpty()) {
							double density = Double.parseDouble(densityField.getText());
							if (density != 1.0)
								manStore.changeCategoryDensity(catName, density);
						}
						if (b) {
							JOptionPane.showMessageDialog(submitButton, "Successfully added the category to the store.");
							goBack.actionPerformed(e);
						} else {
							JOptionPane.showMessageDialog(submitButton, "Couldn't add the category to the store.");
						}
					} else {
						JOptionPane.showMessageDialog(submitButton, "You are currently not managing a store.");
					}
				} else {
					JOptionPane.showMessageDialog(submitButton, "No item was selected.");
				}
			}
		});

		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_1)
						.addComponent(lblNewLabel)
						.addComponent(lblNewLabel_2))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(cancelButton)
						.addComponent(submitButton)
						.addComponent(categoryNameField, GroupLayout.PREFERRED_SIZE, 197, GroupLayout.PREFERRED_SIZE)
						.addComponent(categoryComboBox, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE)
						.addComponent(densityField, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(110, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(categoryNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1)
						.addComponent(categoryComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_2)
						.addComponent(densityField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(submitButton)
					.addGap(18)
					.addComponent(cancelButton)
					.addContainerGap(70, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		
	}

	// initialize items that require the Manager's attributes
		public void getUser(User m) {
			user = (Manager) m;
			manStore = user.getManagedStore();

			if (manStore != null) {
				LinkedList<String> cats = manStore.getCategories();
				for (String c : cats) {
					if (!c.equals("cashier"))
						categoryComboBox.addItem(c);
				}
			}
		}

		public void setVisible(boolean b) {
			frmManagerAddCategory.setVisible(b);
		}
}
