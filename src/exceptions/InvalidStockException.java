package exceptions;

public class InvalidStockException extends ItemStoreException {
	public InvalidStockException(String message) {
		super(message);
	}
}
