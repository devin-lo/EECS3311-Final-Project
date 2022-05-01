package corporate;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.LayoutStyle.ComponentPlacement;

import base.User;

import javax.swing.JButton;

public class AdminGUIAddUser {

	private JFrame frAdminAddUser;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JComboBox<String> usertypeComboBox;
	private Administrator user;
	private JPasswordField confirmPWField;
	private ShopperSystem system;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminGUIAddUser window = new AdminGUIAddUser();
					window.frAdminAddUser.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AdminGUIAddUser() {
		system = ShopperSystem.getInstance();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frAdminAddUser = new JFrame();
		frAdminAddUser.setTitle("Admin: Add User Panel");
		frAdminAddUser.setBounds(100, 100, 500, 300);
		frAdminAddUser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frAdminAddUser.getContentPane().add(panel, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("Username:");
		
		usernameField = new JTextField();
		usernameField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Password:");
		
		passwordField = new JPasswordField();
		
		JLabel lblNewLabel_2 = new JLabel("User type:");
		
		usertypeComboBox = new JComboBox<String>();
		usertypeComboBox.addItem("Customer");
		usertypeComboBox.addItem("Manager");
		usertypeComboBox.addItem("Administrator");
		
		JLabel manWarnLabel = new JLabel("If you're adding a Manager, set their Store later in their User Details.");
		
		JButton submitButton = new JButton("Submit");
		JButton clearFormButton = new JButton("Clear Form");
		JButton cancelButton = new JButton("Cancel");
		
		JLabel lblNewLabel_1_1 = new JLabel("Confirm PW:");
		
		confirmPWField = new JPasswordField();
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblNewLabel_1)
										.addComponent(lblNewLabel))
									.addGap(39)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(usernameField, GroupLayout.PREFERRED_SIZE, 206, GroupLayout.PREFERRED_SIZE)
										.addComponent(passwordField, 208, 208, 208)))
								.addGroup(gl_panel.createSequentialGroup()
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblNewLabel_1_1, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblNewLabel_2))
									.addGap(18)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
										.addComponent(usertypeComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(confirmPWField, GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE))
									.addPreferredGap(ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
									.addComponent(clearFormButton)))
							.addContainerGap())
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(submitButton)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(cancelButton)
							.addGap(144))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(manWarnLabel)
							.addGap(43))))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(usernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(21)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1_1)
						.addComponent(confirmPWField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_2)
						.addComponent(usertypeComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(clearFormButton))
					.addPreferredGap(ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(cancelButton)
						.addComponent(submitButton))
					.addGap(18)
					.addComponent(manWarnLabel)
					.addContainerGap())
		);
		panel.setLayout(gl_panel);
		
		ActionListener goBack = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminGUIHome adminHome = new AdminGUIHome();
				adminHome.getUser(user);
				// TODO if it's possible, open to the User Details instead?
				adminHome.showWhichTab("Full User List");
				adminHome.setVisible(true);
				frAdminAddUser.dispose();
				frAdminAddUser.setVisible(false);
			}
		};
		
		clearFormButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				usernameField.setText(null);
				passwordField.setText(null);
				usertypeComboBox.setSelectedIndex(0);
			}
		});
		
		cancelButton.addActionListener(goBack);
		
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				String password = passwordField.getText();
				if (!(username.isEmpty() && password.isEmpty())) {
					if (password.equals(confirmPWField.getText())) {
						boolean b = false;
						int userType = usertypeComboBox.getSelectedIndex();
						if (userType == 1) {
							b = user.createManager(username, password);
						}
						else if (userType == 2) {
							b = user.createAdmin(username, password);
						}
						else {
							b = user.createCustomer(username, password);
						}
						if (b) {
							JOptionPane.showMessageDialog(submitButton, "Created user " + username);
							User userInterested = system.getUserByName(username);
							AdminGUIHome adminHome = new AdminGUIHome();
							adminHome.getUser(user);
							adminHome.setUserOfInterest(userInterested);
							adminHome.showUserDetails(userInterested);
							adminHome.setVisible(true);
							frAdminAddUser.dispose();
							frAdminAddUser.setVisible(false);
						}
						else
							JOptionPane.showMessageDialog(submitButton, "Couldn't create a user with the username " + username + ", this name is already taken.");
					}
					else {
						JOptionPane.showMessageDialog(submitButton, "Passwords don't match.");
					}
				}
				else {
					JOptionPane.showMessageDialog(submitButton, "Username and/or password can't be blank.");
				}
			}
		});
	}
	
	public void setUserManager() {
		usertypeComboBox.setSelectedIndex(1);
	}
	
	public void getUser(User c) {
		user = (Administrator) c;
		
	}
	
	public void setVisible(boolean b) {
		frAdminAddUser.setVisible(b);
	}
}
