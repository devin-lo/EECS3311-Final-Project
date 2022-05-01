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
import java.awt.Font;

public class ChangeTFA {

	private JFrame frmChgTFA;
	private JPasswordField passwordField;
	private JPasswordField confirmPWField;
	private ShopperSystem system;
	private User user;
	private JTextField tfaField;
	private JLabel tfaStatusLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChangeTFA window = new ChangeTFA();
					window.frmChgTFA.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ChangeTFA() {
		system = ShopperSystem.getInstance();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChgTFA = new JFrame();
		frmChgTFA.setTitle("Change Two-Factor Authentication Settings");
		frmChgTFA.setBounds(100, 100, 450, 300);
		frmChgTFA.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmChgTFA.getContentPane().add(panel, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("TFA Phone Number:");
		
		JLabel lblNewLabel_1 = new JLabel("Password:");
		
		JLabel lblNewLabel_2 = new JLabel("Confirm Password:");
		
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
				
				frmChgTFA.dispose();
				frmChgTFA.setVisible(false);
			}
		};
		
		tfaField = new JTextField();
		tfaField.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("TFA Status:");
		
		tfaStatusLabel = new JLabel("TFAHere");
		
		JButton submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (passwordField.getText().equals(confirmPWField.getText())) {
					if (!user.setTwoFactor(passwordField.getText(), tfaField.getText())) {
						JOptionPane.showMessageDialog(submitButton, "Failed to change TFA settings; is your password entered correctly?");
					} else {
						JOptionPane.showMessageDialog(submitButton,
								"TFA settings changed successfully, taking you back to Account Managemenet...");
						goBack.actionPerformed(e);
					}
				} else {
					JOptionPane.showMessageDialog(submitButton, "Passwords don't match.");
				}
			}
		});
		
		JButton clearButton = new JButton("Clear Settings");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (passwordField.getText().equals(confirmPWField.getText())) {
					if (!user.clearTwoFactor(passwordField.getText())) {
						JOptionPane.showMessageDialog(submitButton, "Failed to change TFA settings; is your password entered correctly?");
					} else {
						JOptionPane.showMessageDialog(submitButton,
								"TFA settings changed successfully, taking you back to Account Managemenet...");
						goBack.actionPerformed(e);
					}
				} else {
					JOptionPane.showMessageDialog(submitButton, "Passwords don't match.");
				}
			}
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(goBack);
		
		JLabel lblNewLabel_4 = new JLabel("Note: to clear settings, you still have to enter your password.");
		lblNewLabel_4.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(47)
					.addComponent(submitButton)
					.addGap(29)
					.addComponent(clearButton)
					.addPreferredGap(ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
					.addComponent(cancelButton)
					.addGap(54))
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
							.addComponent(lblNewLabel_2)
							.addComponent(lblNewLabel_1)
							.addComponent(lblNewLabel))
						.addComponent(lblNewLabel_3))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
							.addComponent(tfaField)
							.addComponent(confirmPWField)
							.addComponent(passwordField, GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfaStatusLabel)))
					.addContainerGap(61, Short.MAX_VALUE))
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_4)
					.addContainerGap(383, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(21)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(tfaStatusLabel)
						.addComponent(lblNewLabel_3))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(tfaField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_2)
						.addComponent(confirmPWField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
					.addComponent(lblNewLabel_4)
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(submitButton)
						.addComponent(cancelButton)
						.addComponent(clearButton))
					.addGap(30))
		);
		panel.setLayout(gl_panel);

		// https://stackoverflow.com/a/13731739
		frmChgTFA.getRootPane().setDefaultButton(submitButton);
	}
	
	public void setVisible(boolean b) {
		frmChgTFA.setVisible(b);
	}
	
	public void getUser(User u) {
		user = u;
		if (user.hasTwoFactor()) {
			tfaStatusLabel.setText("Active");
			tfaField.setText(user.getTwoFactor());
		}
		else
			tfaStatusLabel.setText("Inactive");
	}
}
