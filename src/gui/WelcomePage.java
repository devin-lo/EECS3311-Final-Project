package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class WelcomePage {

	private JFrame frmSmartshoppersWelcomePage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WelcomePage window = new WelcomePage();
					window.frmSmartshoppersWelcomePage.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WelcomePage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSmartshoppersWelcomePage = new JFrame();
		frmSmartshoppersWelcomePage.setTitle("SmartShoppers Welcome Page");
		frmSmartshoppersWelcomePage.setBounds(100, 100, 450, 300);
		frmSmartshoppersWelcomePage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmSmartshoppersWelcomePage.getContentPane().add(panel, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("Welcome to SmartShoppers!");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmSmartshoppersWelcomePage.dispose();
				LoginPage login = new LoginPage();
				login.setVisible(true);
				frmSmartshoppersWelcomePage.setVisible(false);
			}
		});
		
		JButton registerButton = new JButton("Register");
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmSmartshoppersWelcomePage.dispose();
				RegisterCustomerPage register = new RegisterCustomerPage();
				register.setVisible(true);
				frmSmartshoppersWelcomePage.setVisible(false);
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap(137, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(loginButton)
							.addGap(39)
							.addComponent(registerButton))
						.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
							.addGap(16)
							.addComponent(lblNewLabel)
							.addPreferredGap(ComponentPlacement.RELATED, 17, GroupLayout.PREFERRED_SIZE)))
					.addGap(128))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(60)
					.addComponent(lblNewLabel)
					.addGap(65)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(loginButton)
						.addComponent(registerButton))
					.addContainerGap(99, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
	}
	
	public void setVisible(boolean b) {
		frmSmartshoppersWelcomePage.setVisible(b);
	}
}
