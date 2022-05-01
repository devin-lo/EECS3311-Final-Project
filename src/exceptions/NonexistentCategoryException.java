package exceptions;

public class NonexistentCategoryException extends ItemStoreException {
	public NonexistentCategoryException(String message) {
		super(message);
	}
}
