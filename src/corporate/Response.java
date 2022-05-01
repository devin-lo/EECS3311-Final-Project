package corporate;

import exceptions.InvalidResponseType;
import base.User;

public class Response {
	private String comment;
	private int responseType;
	private User loggedUser;
	public static final int NOUSER = 1;
	public static final int WRONGPASS = 2;
	public static final int TWOAUTHFAIL = 3;
	public static final int SUCCESS = 4;
	
	public Response(int responseType) throws InvalidResponseType {
		if (responseType < 1 || responseType > 4)
			throw new InvalidResponseType(responseType + " is not a valid response type");
		this.responseType = responseType;
		if (responseType == 1) {
			comment = "No user with that username exists.";
		}
		else if (responseType == 2) {
			comment = "Wrong password.";
		}
		else if (responseType == 3) {
			comment = "Two factor authentication failed.";
		}
		else if (responseType == 4) {
			comment = "Successful login!";
		}
	}
	
	public Response (int responseType, User loggedUser) throws InvalidResponseType {
		this(responseType);
		this.loggedUser = loggedUser;
	}
	
	public Response(int responseType, User loggedUser, String addComment) throws InvalidResponseType {
		this(responseType, loggedUser);
		comment += "\n" + addComment;
	}
	
	public User getUser() {
		return loggedUser;
	}
	
	public String getComment() {
		return comment;
	}
	
	public int getResponse() {
		return this.responseType;
	}
}
