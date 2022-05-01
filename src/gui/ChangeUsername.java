package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

import base.User;
import client.Customer;
import corporate.Administrator;
import corporate.AdminGUIHome;
import corporate.Manager;
import corporate.ManagerGUIHome;
import corporate.ShopperSystem;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ChangeUsername {

	private JFrame frmChgUsername;
	private JTextField newUsernameField;
	private JPasswordField passwordField;
	private JPasswordField confirmPWField;
	private ShopperSystem system;
	private User user;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChangeUsername window = new ChangeUsername();
					window.frmChgUsername.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ChangeUsername() {
		system = ShopperSystem.getInstance();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChgUsername = new JFrame();
		frmChgUsername.setTitle("Change Username");
		frmChgUsername.setBounds(100, 100, 450, 300);
		frmChgUsername.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmChgUsername.getContentPane().add(panel, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("New Username:");
		
		JLabel lblNewLabel_1 = new JLabel("Password:");
		
		JLabel lblNewLabel_2 = new JLabel("Confirm Password:");
		
		newUsernameField = new JTextField();
		newUsernameField.setColumns(10);
		
		passwordField = new JPasswordField();
		
		confirmPWField = new JPasswordField();
		
		ActionListener goBack = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (user.getClass() == Customer.class) {
					CustomerHome custHome = new CustomerHome();
					custHome.getUser(user);
					custHome.showWhichTab("Account Management");
					custHome.setVisible(true);
				}
				else if (user.getClass() == Manager.class) {
					ManagerGUIHome manHome = new ManagerGUIHome();
					manHome.getUser(user);
					// manHome.showWhichTab("Account Management");
					manHome.setVisible(true);
				}
				else if (user.getClass() == Administrator.class) {
					AdminGUIHome adminHome = new AdminGUIHome();
					adminHome.getUser(user);
					// manHome.showWhichTab("Account Management");
					adminHome.setVisible(true);
				}
				
				frmChgUsername.dispose();
				frmChgUsername.setVisible(false);
			}
		};
		
		JButton submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (newUsernameField.getText().isEmpty())
					JOptionPane.showMessageDialog(submitButton, "Username field can't be blank.");
				else {
					if (passwordField.getText().equals(confirmPWField.getText()))
						if (!(user.changeUsername(newUsernameField.getText(), passwordField.getText()))) {
							JOptionPane.showMessageDialog(submitButton, "That username is taken, or your password is wrong.");
						}
						else {
							JOptionPane.showMessageDialog(submitButton, "Username changed successfully, taking you back to Account Management...");
							goBack.actionPerformed(e);
						}
					else {
						JOptionPane.showMessageDialog(submitButton, "Passwords don't match.");
					}
				}
			}
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(goBack);
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel_2)
								.addComponent(lblNewLabel_1)
								.addComponent(lblNewLabel))
							.addGap(18)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(passwordField)
								.addComponent(newUsernameField, GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
								.addComponent(confirmPWField)))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(157)
							.addComponent(submitButton))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(158)
							.addComponent(cancelButton)))
					.addContainerGap(27, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(21)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(newUsernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_2)
						.addComponent(confirmPWField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(40)
					.addComponent(submitButton)
					.addGap(12)
					.addComponent(cancelButton)
					.addContainerGap(33, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);

		// https://stackoverflow.com/a/13731739
		frmChgUsername.getRootPane().setDefaultButton(submitButton);
	}
	
	public void setVisible(boolean b) {
		frmChgUsername.setVisible(b);
	}
	
	public void getUser(User u) {
		user = u;
	}
}
