package library;

public class EBook extends Item implements Borrowable {

	private double fileSizeMB;

	public EBook(String id, String title, double fileSizeMB, boolean available) {
		super(id, title, available);
		this.fileSizeMB = fileSizeMB;
	}

	public double getFileSizeMB() {
		return fileSizeMB;
	}

	@Override
	public void borrowItem() throws LibraryException {
		if (!available)
			throw new LibraryException("EBook not available.");
		available = false;
	}

	@Override
	public void returnItem() throws LibraryException {
		if (available)
			throw new LibraryException("EBook is not borrowed.");
		available = true;
	}

	@Override
	public String displayInfo() {
		return "[EBook] " + id + " - " + title + " | File Size: " + fileSizeMB + "MB" + " | Available: " + available;
	}

}
