package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;

import corporate.ShopperSystem;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegisterCustomerPage {

	private JFrame frmSmartshoppersRegistrationPage;
	private corporate.ShopperSystem system;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JPasswordField passwordConfirmField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegisterCustomerPage window = new RegisterCustomerPage();
					window.frmSmartshoppersRegistrationPage.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RegisterCustomerPage() {
		initialize();
		system = ShopperSystem.getInstance();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSmartshoppersRegistrationPage = new JFrame();
		frmSmartshoppersRegistrationPage.setTitle("SmartShoppers Registration Page (Customers)");
		frmSmartshoppersRegistrationPage.setBounds(100, 100, 450, 300);
		frmSmartshoppersRegistrationPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmSmartshoppersRegistrationPage.getContentPane().add(panel, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("Username");
		
		usernameField = new JTextField();
		usernameField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Password");
		
		passwordField = new JPasswordField();
		passwordField.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Repeat Password");
		
		passwordConfirmField = new JPasswordField();
		passwordConfirmField.setColumns(10);
		
		JButton registerButton = new JButton("Register");
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				String password = passwordField.getText();
				String passwordConfirm = passwordConfirmField.getText();
				if (!(username.isEmpty()) && !(password.isEmpty())) {
					if (!(password.equals(passwordConfirm))) {
						JOptionPane.showMessageDialog(registerButton, "Passwords don't match!");
					}
					else {
						boolean createdAccount = system.createCustomerAccount(username, password);
						if (!(createdAccount)) {
							JOptionPane.showMessageDialog(registerButton, "That username is already taken, please try a different username.");
						}
						else {
							JOptionPane.showMessageDialog(registerButton, "Account created successfully! Taking you to the login screen.");
							frmSmartshoppersRegistrationPage.dispose();
							LoginPage login = new LoginPage();
							login.setVisible(true);
							frmSmartshoppersRegistrationPage.setVisible(false);
						}
					}
				}
				else {
					JOptionPane.showMessageDialog(registerButton, "Username or password field was empty!");
				}
			}
		});
		
		JButton loginButton = new JButton("Go to Login");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmSmartshoppersRegistrationPage.dispose();
				LoginPage login = new LoginPage();
				login.setVisible(true);
				frmSmartshoppersRegistrationPage.setVisible(false);
			}
		});
		
		JButton welcomeButton = new JButton("Back to Welcome");
		welcomeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmSmartshoppersRegistrationPage.dispose();
				WelcomePage welcome = new WelcomePage();
				welcome.setVisible(true);
				frmSmartshoppersRegistrationPage.setVisible(false);
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(37)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblNewLabel_2)
							.addGap(18)
							.addComponent(passwordConfirmField))
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel)
								.addComponent(lblNewLabel_1))
							.addGap(54)
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(usernameField)
								.addComponent(passwordField, GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE))))
					.addContainerGap(56, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap(197, Short.MAX_VALUE)
					.addComponent(registerButton)
					.addGap(172))
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap(177, Short.MAX_VALUE)
					.addComponent(welcomeButton)
					.addGap(152))
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap(190, Short.MAX_VALUE)
					.addComponent(loginButton)
					.addGap(165))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(35)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(usernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_2)
						.addComponent(passwordConfirmField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(registerButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(loginButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(welcomeButton)
					.addContainerGap(31, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		// https://stackoverflow.com/a/13731739
		frmSmartshoppersRegistrationPage.getRootPane().setDefaultButton(registerButton);
	}
	
	public void setVisible(boolean b) {
		frmSmartshoppersRegistrationPage.setVisible(b);
	}
}
