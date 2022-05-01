package corporate;

public class Item {
	private String name;
	private String description;
	private double price;
	private double size;
	private int searchFrequency;
	
	private Item() {
		name = "";
		description = "";
		price = 0.0;
		size = 0.0;
	}
	
	protected Item(String name) {
		this();
		this.name = name;
	}
	
	protected void setName(String name) {
		this.name = name;
	}
	
	protected void setDescription(String description) {
		this.description = description;
	}
	
	protected void setPrice(double price) {
		this.price = price;
	}
	
	protected void setSize(double size) {
		this.size = size;
	}
	
	protected void increaseSearchFreq() {
		searchFrequency++;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public double getPrice() {
		return price;
	}
	
	public double getSize() {
		return size;
	}
	
	public int getSearchFreq() {
		return searchFrequency;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
