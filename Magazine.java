package library;

public class Magazine extends Item implements Borrowable {

	private int issueNumber;
	private int pages;

	public Magazine(String id, String title, int issueNumber, int pages, boolean available) {

		super(id, title, available);

		this.issueNumber = issueNumber;
		this.pages = pages;
	}

	public int getIssueNumber() {
		return issueNumber;
	}

	public int getPages() {
		return pages;
	}

	@Override
	public void borrowItem() throws LibraryException {
		if (!available)
			throw new LibraryException("Magazine not available.");
		available = false;
	}

	@Override
	public void returnItem() throws LibraryException {
		if (available)
			throw new LibraryException("Magazine is not borrowed.");
		available = true;
	}

	@Override
	public String displayInfo() {
		return "[Magazine] " + id + " - " + title + " | Issue: " + issueNumber + " | Pages: " + pages + " | Available: "
				+ available;
	}

}
