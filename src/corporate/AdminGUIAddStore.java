package corporate;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.Format;
import java.util.Date;

import javax.swing.JFrame;

import base.User;
import gui.UtilitiesGUI;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JFormattedTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

public class AdminGUIAddStore {

	private JFrame frAdminAddStore;
	private Administrator user;
	private JTextField addressField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminGUIAddStore window = new AdminGUIAddStore();
					window.frAdminAddStore.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AdminGUIAddStore() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frAdminAddStore = new JFrame();
		frAdminAddStore.setTitle("Admin: Add Store");
		frAdminAddStore.setBounds(100, 100, 450, 300);
		frAdminAddStore.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// time input box formatter
		// http://www.java2s.com/Tutorial/Java/0240__Swing/FormattedDateandTimeInputDateFormatgetTimeInstanceDateFormatSHORT.htm
		Format shortTime = DateFormat.getTimeInstance(DateFormat.SHORT);
		
		JPanel panel = new JPanel();
		frAdminAddStore.getContentPane().add(panel, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("Address:");
		
		addressField = new JTextField();
		addressField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Opening Time:");
		
		JLabel lblNewLabel_2 = new JLabel("Closing Time:");
		
		JFormattedTextField openingField = new JFormattedTextField();
		
		JFormattedTextField closingField = new JFormattedTextField();
		
		openingField.setValue(new Date());
		openingField.setColumns(20);
		openingField.setHorizontalAlignment(SwingConstants.CENTER);
		openingField.setToolTipText("Please input a time in a valid format (XX:XX a.m. or p.m.)");
		openingField.setText("7:00 a.m.");
		
		closingField.setValue(new Date());
		closingField.setColumns(20);
		closingField.setHorizontalAlignment(SwingConstants.CENTER);
		closingField.setToolTipText("Please input a time in a valid format (XX:XX a.m. or p.m.)");
		closingField.setText("11:00 p.m.");
		
		ActionListener goBack = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminGUIHome adminHome = new AdminGUIHome();
				adminHome.getUser(user);
				// TODO if it's possible, open to the User Details instead?
				adminHome.showWhichTab("Full Store List");
				adminHome.setVisible(true);
				frAdminAddStore.dispose();
				frAdminAddStore.setVisible(false);
			}
		};
		
		JButton clearButton = new JButton("Clear Form");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addressField.setText(null);
				openingField.setText("7:00 a.m.");
				closingField.setText("11:00 p.m.");
			}
		});
		
		JButton submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String address = addressField.getText();
				if (!(address == null || address.isEmpty())) {
					String openStr = openingField.getText();
					String closeStr = closingField.getText();
					if (!(openStr == null || openStr.isEmpty() || closeStr == null || closeStr.isEmpty())) {
						int open = UtilitiesGUI.convertTimeToInt(openStr);
						int close = UtilitiesGUI.convertTimeToInt(closeStr);
						if (!(close < open)) {
							Store store = new Store(address, open, close);
							boolean b = user.addStore(store);
							if (b) {
								JOptionPane.showMessageDialog(submitButton, "Added the store at " + address);
								goBack.actionPerformed(e);
							}
							else {
								JOptionPane.showMessageDialog(submitButton, "Couldn't add the store.");
							}
						}
						else {
							JOptionPane.showMessageDialog(submitButton, "Opening time can't be after the closing time!");
						}
					}
					else {
						JOptionPane.showMessageDialog(submitButton, "Operating hours can't be blank.");
					}
				}
				else {
					JOptionPane.showMessageDialog(submitButton, "Address can't be blank.");
				}
			}
		});
		
		JButton cancelButton = new JButton("Cancel");
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
										.addComponent(lblNewLabel_1)
										.addComponent(lblNewLabel)
										.addComponent(lblNewLabel_2))
									.addGap(18)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(addressField, GroupLayout.PREFERRED_SIZE, 265, GroupLayout.PREFERRED_SIZE)
										.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
											.addComponent(closingField, Alignment.LEADING)
											.addComponent(openingField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE))))
								.addComponent(clearButton)))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(168)
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(cancelButton)
								.addComponent(submitButton))))
					.addContainerGap(72, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(addressField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1)
						.addComponent(openingField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_2)
						.addComponent(closingField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(clearButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(submitButton)
					.addGap(18)
					.addComponent(cancelButton)
					.addContainerGap(43, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
	}

	public void getUser(User c) {
		user = (Administrator) c;
		
	}
	
	public void setVisible(boolean b) {
		frAdminAddStore.setVisible(b);
	}
}
