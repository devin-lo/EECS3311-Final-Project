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

public class ChangePassword {

	private JFrame frmChgPW;
	private JPasswordField newPWField;
	private JPasswordField confirmPWField;
	private ShopperSystem system;
	private User user;
	private JPasswordField oldPWField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChangePassword window = new ChangePassword();
					window.frmChgPW.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ChangePassword() {
		system = ShopperSystem.getInstance();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChgPW = new JFrame();
		frmChgPW.setTitle("Change Password");
		frmChgPW.setBounds(100, 100, 450, 300);
		frmChgPW.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmChgPW.getContentPane().add(panel, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("Old Password:");
		
		JLabel lblNewLabel_1 = new JLabel("New Password:");
		
		JLabel lblNewLabel_2 = new JLabel("Confirm New Password:");
		
		newPWField = new JPasswordField();
		
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
				
				frmChgPW.dispose();
				frmChgPW.setVisible(false);
			}
		};
		
		JButton submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (newPWField.getText().isEmpty())
					JOptionPane.showMessageDialog(submitButton, "New password field can't be blank.");
				else {
					if (newPWField.getText().equals(confirmPWField.getText())) {
						if (!user.changePassword(oldPWField.getText(),newPWField.getText())) {
							JOptionPane.showMessageDialog(submitButton, "Old password is wrong.");
						}
						else {
							JOptionPane.showMessageDialog(submitButton, "Password changed successfully, taking you back to Account Managemenet...");
							goBack.actionPerformed(e);
						}
					}	
					else {
						JOptionPane.showMessageDialog(submitButton, "New password doesn't match the confirmation.");
					}
				}
			}
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(goBack);
		
		oldPWField = new JPasswordField();
		
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
								.addComponent(oldPWField, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
								.addComponent(newPWField)
								.addComponent(confirmPWField)))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(157)
							.addComponent(submitButton))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(158)
							.addComponent(cancelButton)))
					.addContainerGap(35, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(21)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(oldPWField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1)
						.addComponent(newPWField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_2)
						.addComponent(confirmPWField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(40)
					.addComponent(submitButton)
					.addGap(12)
					.addComponent(cancelButton)
					.addContainerGap(43, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);

		// https://stackoverflow.com/a/13731739
		frmChgPW.getRootPane().setDefaultButton(submitButton);
	}
	
	public void setVisible(boolean b) {
		frmChgPW.setVisible(b);
	}
	
	public void getUser(User u) {
		user = u;
	}
}
