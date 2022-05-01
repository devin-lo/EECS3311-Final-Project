package corporate;

import exceptions.InvalidResponseType;

public class DeleteResponse {
	private String comment;
	private int responseType;
	public static final int WRONGPASS = 0;
	public static final int WRONGUSER = 1;
	public static final int ONEADMINLEFT = 2;
	public static final int SUCCESS = 3;
	
	public DeleteResponse(int responseType) throws InvalidResponseType {
		if (responseType < 0 || responseType > 3)
			throw new InvalidResponseType(responseType + " is not a valid response type");
		this.responseType = responseType;
		if (responseType == 0) {
			comment = "Wrong password.";
		}
		else if (responseType == 1) {
			comment = "No user with that username exists.";
		}
		else if (responseType == 2) {
			comment = "You can't delete your account because there is only one registered administrator in the system.";
		}
		else if (responseType == 3) {
			comment = "Account purged from the system successfully.";
		}
	}
	
	public int getResponse() {
		return responseType;
	}
	
	public String getComment() {
		return comment;
	}
}
