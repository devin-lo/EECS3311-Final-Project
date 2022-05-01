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
import corporate.DeleteResponse;
import corporate.Manager;
import corporate.ManagerGUIHome;
import corporate.ShopperSystem;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class DeleteAccountPage {

	private JFrame frmDelAcct;
	private JTextField usernameField;
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
					DeleteAccountPage window = new DeleteAccountPage();
					window.frmDelAcct.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DeleteAccountPage() {
		system = ShopperSystem.getInstance();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDelAcct = new JFrame();
		frmDelAcct.setTitle("Confirm Account Deletion");
		frmDelAcct.setBounds(100, 100, 450, 300);
		frmDelAcct.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmDelAcct.getContentPane().add(panel, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("Username:");
		
		JLabel lblNewLabel_1 = new JLabel("Password:");
		
		JLabel lblNewLabel_2 = new JLabel("Confirm Password:");
		
		usernameField = new JTextField();
		usernameField.setColumns(10);
		
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
				
				frmDelAcct.dispose();
				frmDelAcct.setVisible(false);
			}
		};
		
		JButton submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!usernameField.getText().equals(user.getUsername()))
					JOptionPane.showMessageDialog(submitButton, "That is not the correct username.");
				else {
					if (passwordField.getText().equals(confirmPWField.getText())) {
						DeleteResponse response = user.deleteUser(passwordField.getText());
						if (response.getResponse() == DeleteResponse.WRONGPASS) {
							JOptionPane.showMessageDialog(submitButton, "Your password is wrong.");
						}
						else {
							if (response.getResponse() == DeleteResponse.WRONGUSER) {
								JOptionPane.showMessageDialog(submitButton, "Username doesn't exist in the system.");
							}
							else if (response.getResponse() == DeleteResponse.ONEADMINLEFT) {
								JOptionPane.showMessageDialog(submitButton, response.getComment());
							}
							else if (response.getResponse() == DeleteResponse.SUCCESS) {
								JOptionPane.showMessageDialog(submitButton, "Account deleted. Returning to the Welcome screen...");
								frmDelAcct.dispose();
								WelcomePage welcome = new WelcomePage();
								welcome.setVisible(true);
								frmDelAcct.setVisible(false);
							}
						}
					}
					else {
						JOptionPane.showMessageDialog(submitButton, "Passwords don't match.");
					}
				}
			}
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(goBack);
		
		JLabel lblNewLabel_3 = new JLabel("Please confirm account details below to delete your account:");
		
		JLabel lblNewLabel_4 = new JLabel("Once you hit submit, this action can't be undone!");
		lblNewLabel_4.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblNewLabel_2)
										.addComponent(lblNewLabel_1)
										.addComponent(lblNewLabel))
									.addGap(18)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
										.addComponent(passwordField)
										.addComponent(usernameField, GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
										.addComponent(confirmPWField)))
								.addComponent(lblNewLabel_3)))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(60)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel_4)
								.addGroup(gl_panel.createSequentialGroup()
									.addGap(121)
									.addComponent(submitButton))
								.addGroup(gl_panel.createSequentialGroup()
									.addGap(122)
									.addComponent(cancelButton)))))
					.addContainerGap(27, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap(14, Short.MAX_VALUE)
					.addComponent(lblNewLabel_3)
					.addGap(18)
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
						.addComponent(confirmPWField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(lblNewLabel_4)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(submitButton)
					.addGap(12)
					.addComponent(cancelButton)
					.addContainerGap())
		);
		panel.setLayout(gl_panel);

		// https://stackoverflow.com/a/13731739
		frmDelAcct.getRootPane().setDefaultButton(cancelButton);
	}
	
	public void setVisible(boolean b) {
		frmDelAcct.setVisible(b);
	}
	
	public void getUser(User u) {
		user = u;
	}
}
