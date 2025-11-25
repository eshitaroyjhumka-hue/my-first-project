package library;

public abstract class Item {
	protected String id;
	protected String title;
	protected boolean available;

	public Item(String id, String title, boolean available) {
		this.id = id;
		this.title = title;
		this.available = available;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	} 

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean a) {
		this.available = a;
	}

	public abstract String displayInfo();

}
