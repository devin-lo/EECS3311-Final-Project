package base;

import corporate.DeleteResponse;
import corporate.Store;
import corporate.ShopperSystem;
import exceptions.InvalidResponseType;

import java.util.Map;
import java.util.HashMap;

public abstract class User {
	private String username;
	private String password;
	private Store prefStore;
	private String twoFactorPhone;
	protected ShopperSystem system;
	
	// private default Constructor sets all the default values
	// needed for 4.1 Req 2
	private User() {
		username = null;
		password = null;
		prefStore = null;
		twoFactorPhone = "";
		system = ShopperSystem.getInstance();
	}
	
	// public Constructor requires a username and a password
	// needed for 4.1 Req 2
	public User(String username, String password) {
		this(); // call private default Constructor
		this.username = username;
		this.password = password;
	}
	
	// needed for 4.1 Req 1 as well as other account management options
	public boolean verifyPassword(String checkPW) {
		return checkPW.equals(password);
	}
	
	// dummy method so that the two factor auth of 4.2 Req 2 looks useful
	public boolean twoFactorAuth() {
		if (!(twoFactorPhone.isEmpty()))
			return system.twoFactorAuth(twoFactorPhone);
		return true; // if there's no twoFactorAuth enabled, then be permissive.
	}
	
	// required for all of the sensitive account changes (username, twoFactorAuth, password, delete acct, getAccountInfo)
	private boolean securityCheck(String checkPW) {
		return (verifyPassword(checkPW) && twoFactorAuth());
	}
	
	// required for 4.2. Req 1, part 1
	public boolean changeUsername(String newUsername, String checkPW) {
		if (securityCheck(checkPW)) {
			if (system.changeUsername(this, newUsername)) {
				username = newUsername;
				return true;
			}
		}
		return false;
	}
	
	// required for 4.2, Req 1, part 2
	public boolean changePassword(String oldPW, String newPW) {
		if (securityCheck(oldPW)) {
			this.password = newPW;
			return true;
		}
		return false;
	}
	
	// required for 4.1 Req 1
	public String getUsername() {
		return this.username;
	}
	
	// required for 4.2 Req 3
	public Store getPrefStore() {
		return this.prefStore;
	}
	
	// required for 4.2 Req 3
	public String setPrefStore(Store newPrefStore) {
		this.prefStore = newPrefStore;
		return "Set the preferred store.";
	}
	
	// required for 4.2 Req 2
	public boolean setTwoFactor(String checkPW, String phone) {
		if (securityCheck(checkPW)) {
			this.twoFactorPhone = phone;
			return true;
		}
		return false;
	}
	
	// required for 4.2 Req 2
	public boolean clearTwoFactor(String checkPW) {
		return setTwoFactor(checkPW, "");
	}
	
	public boolean hasTwoFactor() {
		return !(twoFactorPhone.isEmpty());
	}
	
	// required for 4.2 Req 4
	public DeleteResponse deleteUser(String checkPW) {
		DeleteResponse del = null;
		if (securityCheck(checkPW)) {
			del = system.removeUser(username);
		}
		else {
			try {
				del = new DeleteResponse(DeleteResponse.WRONGPASS);
			} catch (InvalidResponseType e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return del;
	}
	
	// required for 4.2 Req 4
	public Map<String, String> getAccountInfo(String checkPW) {
		Map<String, String> accountInfo = null;
		if (securityCheck(checkPW)) {
			accountInfo = new HashMap<String, String>();
			// add the info to it
			accountInfo.put("Username",getUsername());
			if (hasTwoFactor())
				accountInfo.put("Two-Factor Phone", getTwoFactor());
			else
				accountInfo.put("Two-Factor Phone", "N/A");
			if (prefStore == null)
				accountInfo.put("Preferred Store", "None");
			else
				accountInfo.put("Preferred Store", prefStore.getAddress());
		}
		return accountInfo;
	}
	
	public String getTwoFactor() {
		return twoFactorPhone;
	}
}
