package library;

public interface Borrowable {
	void borrowItem() throws LibraryException;

	void returnItem() throws LibraryException;
}
