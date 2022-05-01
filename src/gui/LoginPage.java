package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import java.awt.BorderLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

import base.User;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import corporate.ShopperSystem;
import exceptions.InvalidResponseType;
import corporate.Response;
import base.User;
import client.Customer;
import corporate.Administrator;
import corporate.AdminGUIHome;
import corporate.Manager;
import corporate.ManagerGUIHome;

public class LoginPage {

	private JFrame frmSmartshoppersLoginPage;
	private JTextField usernameField;
	private JTextField passwordField;
	private corporate.ShopperSystem system;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginPage window = new LoginPage();
					window.frmSmartshoppersLoginPage.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginPage() {
		initialize();
		system = ShopperSystem.getInstance();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSmartshoppersLoginPage = new JFrame();
		frmSmartshoppersLoginPage.setTitle("SmartShoppers Login Page");
		frmSmartshoppersLoginPage.setBounds(100, 100, 450, 300);
		frmSmartshoppersLoginPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmSmartshoppersLoginPage.getContentPane().add(panel, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("Username");
		
		usernameField = new JTextField();
		usernameField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Password");
		
		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				String password = passwordField.getText();
				if (!(username.isEmpty()) && !(password.isEmpty())) {
					Response login = null;
					try {
						login = system.loginUser(username, password);
					} catch (InvalidResponseType e1) {
						// Auto-generated catch block
						e1.printStackTrace();
					}
					if (login != null) {
						JOptionPane.showMessageDialog(loginButton, login.getComment());
						// TODO hand off the user if there is one
						User user = login.getUser();
						if (user != null) {
							frmSmartshoppersLoginPage.dispose();
							if (user.getClass() == Administrator.class) {
								AdminGUIHome adminHome = new AdminGUIHome();
								adminHome.getUser(user);
								adminHome.setVisible(true);
							}
							else if (user.getClass() == Manager.class) {
								ManagerGUIHome manHome = new ManagerGUIHome();
								manHome.getUser(user);
								manHome.setVisible(true);
							}
							else {
								CustomerHome custHome = new CustomerHome();
								custHome.getUser(user);
								custHome.setVisible(true);
							}
							frmSmartshoppersLoginPage.setVisible(false);
						}
					}
				}
				else {
					JOptionPane.showMessageDialog(loginButton, "Username or password field was empty!");
				}
			}
		});
		
		JButton registerButton = new JButton("Register");
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmSmartshoppersLoginPage.dispose();
				RegisterCustomerPage register = new RegisterCustomerPage();
				register.setVisible(true);
				frmSmartshoppersLoginPage.setVisible(false);
			}
		});
		
		JButton welcomeButton = new JButton("Back to Welcome");
		welcomeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmSmartshoppersLoginPage.dispose();
				WelcomePage welcome = new WelcomePage();
				welcome.setVisible(true);
				frmSmartshoppersLoginPage.setVisible(false);
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(42)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel)
								.addComponent(lblNewLabel_1))
							.addGap(18)
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(passwordField, Alignment.LEADING)
								.addComponent(usernameField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(153)
							.addComponent(welcomeButton))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(173)
							.addComponent(registerButton))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(183)
							.addComponent(loginButton)))
					.addContainerGap(72, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(47)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(usernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_1))
					.addGap(18)
					.addComponent(loginButton)
					.addGap(11)
					.addComponent(registerButton)
					.addGap(11)
					.addComponent(welcomeButton)
					.addContainerGap(47, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		// https://stackoverflow.com/a/13731739
		frmSmartshoppersLoginPage.getRootPane().setDefaultButton(loginButton);
	}
	
	public void setVisible(boolean b) {
		frmSmartshoppersLoginPage.setVisible(b);
	}
}
