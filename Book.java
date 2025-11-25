package library;

public class Book extends Item implements Borrowable {
	private String author;
	private String isbn;
	private int pages;
	private double price;

	public Book(String id, String title, String author, String isbn, int pages, double price, boolean available) {

		super(id, title, available);

		this.author = author;
		this.isbn = isbn;
		this.pages = pages;
		this.price = price;
	}

	public String getAuthor() {
		return author;
	}

	public String getIsbn() {
		return isbn;
	}

	public int getPages() {
		return pages;
	}

	public double getPrice() {
		return price;
	}

	@Override
	public void borrowItem() throws LibraryException {
		if (!available)
			throw new LibraryException("Book not available.");
		available = false;
	}

	@Override
	public void returnItem() throws LibraryException {
		if (available)
			throw new LibraryException("Book is not borrowed.");
		available = true;
	}

	@Override
	public String displayInfo() {
		return "[Book] " + id + " - " + title + " | Author: " + author + " | Pages: " + pages + " | Price: " + price
				+ " | Available: " + available;
	}

}
