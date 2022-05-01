package corporate;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;

import base.User;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AdminGUIAddItem {

	private JFrame frAdminAddItem;
	private JTextField sysItemNameField;
	private JTextField sysSizeField;
	private JTextField sysPriceField;
	private JTextArea sysDescField;
	
	private Administrator user;
	private ShopperSystem system;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminGUIAddItem window = new AdminGUIAddItem();
					window.frAdminAddItem.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AdminGUIAddItem() {
		system = ShopperSystem.getInstance();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frAdminAddItem = new JFrame();
		frAdminAddItem.setTitle("Admin: Add Item to System");
		frAdminAddItem.setBounds(100, 100, 530, 430);
		frAdminAddItem.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frAdminAddItem.getContentPane().add(panel, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("Name:");
		
		JLabel lblNewLabel_1 = new JLabel("Size:");
		
		sysItemNameField = new JTextField();
		sysItemNameField.setColumns(10);
		
		sysSizeField = new JTextField();
		sysSizeField.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Price:");
		
		sysPriceField = new JTextField();
		sysPriceField.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Description:");
		
		sysDescField = new JTextArea();
		
		ActionListener goBack = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminGUIHome adminHome = new AdminGUIHome();
				adminHome.getUser(user);
				adminHome.showWhichTab("System Items List");
				adminHome.setVisible(true);
				frAdminAddItem.dispose();
				frAdminAddItem.setVisible(false);
			}
		};
		
		JButton clearButton = new JButton("Clear Form");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sysItemNameField.setText(null);
				sysSizeField.setText(null);
				sysPriceField.setText(null);
				sysDescField.setText(null);
			}
		});
		
		JButton submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String itemName = sysItemNameField.getText();
				if (!(itemName == null || itemName.isEmpty())) {
					boolean b = false;
					Item i = new Item(itemName);
					if (sysSizeField.getText() != null && !sysSizeField.getText().isEmpty())
						i.setSize(Double.parseDouble(sysSizeField.getText()));
					if (sysPriceField.getText() != null && !sysPriceField.getText().isEmpty())
						i.setPrice(Double.parseDouble(sysPriceField.getText()));
					if (sysDescField.getText() != null)
						i.setDescription(sysDescField.getText());
					b = user.addItemToSystem(i);
					if (b) {
						JOptionPane.showMessageDialog(submitButton, "Added item with name " + itemName);
						AdminGUIHome adminHome = new AdminGUIHome();
						adminHome.getUser(user);
						adminHome.setSysItemOfInterest(i);
						adminHome.showSysItemDetails(i);
						adminHome.setVisible(true);
						frAdminAddItem.dispose();
						frAdminAddItem.setVisible(false);
					}
					else
						JOptionPane.showMessageDialog(submitButton, "Couldn't create that item.");
				}
				else {
					JOptionPane.showMessageDialog(submitButton, "Item name can't be blank.");
				}
			}
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(goBack);
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(sysDescField, GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
								.addGroup(gl_panel.createSequentialGroup()
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblNewLabel)
										.addComponent(lblNewLabel_1)
										.addComponent(lblNewLabel_2))
									.addGap(18)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(sysPriceField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(sysSizeField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(sysItemNameField, GroupLayout.PREFERRED_SIZE, 262, GroupLayout.PREFERRED_SIZE)))
								.addComponent(lblNewLabel_3))
							.addContainerGap())
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(clearButton)
							.addGap(120)
							.addComponent(submitButton)
							.addPreferredGap(ComponentPlacement.RELATED, 105, Short.MAX_VALUE)
							.addComponent(cancelButton)
							.addGap(40))))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(sysItemNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1)
						.addComponent(sysSizeField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_2)
						.addComponent(sysPriceField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(39)
					.addComponent(lblNewLabel_3)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(sysDescField, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(clearButton)
						.addComponent(submitButton)
						.addComponent(cancelButton))
					.addContainerGap(51, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
	}
	
	public void getUser(User c) {
		user = (Administrator) c;
		
	}

	public void setVisible(boolean b) {
		frAdminAddItem.setVisible(b);
	}
}
