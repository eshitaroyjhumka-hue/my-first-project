package library;

import java.io.*;
import java.util.*;

public class Library {

	
	private ArrayList<Book> books = new ArrayList<>();
	private ArrayList<Magazine> magazines = new ArrayList<>();
	private ArrayList<EBook> ebooks = new ArrayList<>();
	private ArrayList<Member> members = new ArrayList<>();

	private File booksFile;
	private File magazinesFile;
	private File ebooksFile;
	private File membersFile;
	private File transactionsFile;

	private static final int borrowlimit= 3;

	public Library(String booksFileName, String magazinesFileName, String ebooksFileName, String membersFileName,
			String transactionsFileName) {

		booksFile = new File(booksFileName);
		magazinesFile = new File(magazinesFileName);
		ebooksFile = new File(ebooksFileName);
		membersFile = new File(membersFileName);
		transactionsFile = new File(transactionsFileName);
	}

	// Utility finders 

	private Book findBook(String id) {
		for (Book b : books)
			if (b.getId().equals(id))
				return b;
		return null;
	}

	private Magazine findMagazine(String id) {
		for (Magazine m : magazines)
			if (m.getId().equals(id))
				return m;
		return null;
	}

	private EBook findEBook(String id) {
		for (EBook e : ebooks)
			if (e.getId().equals(id))
				return e;
		return null;
	}

	private Member findMember(String id) {
		for (Member m : members)
			if (m.getId().equals(id))
				return m;
		return null;
	}

	//  Add / get 

	public void addBook(Book b) throws LibraryException {
		if (findBook(b.getId()) != null)
			throw new LibraryException("Book ID already exists.");
		books.add(b);
	}

	public void addMagazine(Magazine m) throws LibraryException {
		if (findMagazine(m.getId()) != null)
			throw new LibraryException("Magazine ID already exists.");
		magazines.add(m);
	}

	public void addEBook(EBook e) throws LibraryException {
		if (findEBook(e.getId()) != null)
			throw new LibraryException("EBook ID already exists.");
		ebooks.add(e);
	}

	// PUBLIC getter for Member (needed by LibraryApp)
	public Member getMember(String id) {
		return findMember(id);
	}

	public void addMember(Member m) throws LibraryException {
		if (findMember(m.getId()) != null)
			throw new LibraryException("Member ID already exists.");
		members.add(m);
	}

	//  List all items 

	public List<Item> listAllItems() {
		ArrayList<Item> list = new ArrayList<>();
		list.addAll(books);
		list.addAll(magazines);
		list.addAll(ebooks);
		list.sort(Comparator.comparing(Item::getTitle));
		return list;
	}

	//  Search 

	public List<Book> searchBooksByTitle(String q) {
		ArrayList<Book> result = new ArrayList<>();
		for (Book b : books)
			if (b.getTitle().toLowerCase().contains(q.toLowerCase()))
				result.add(b);
		return result;
	}

	//  Borrow / Return 

	public void borrowItem(String memberId, String itemType, String itemId) throws LibraryException {

		Member m = findMember(memberId);
		if (m == null)
			throw new LibraryException("Member not found.");

		Borrowable item = getBorrowable(itemType, itemId);
		if (item == null)
			throw new LibraryException("Item not found.");

		if (m.borrowedCount() >= borrowlimit)
			throw new LibraryException("Borrow limit reached.");

		// item.borrowItem();
		m.borrowBook(itemId);

		recordTransaction(new Transaction(memberId, itemType, itemId, "BORROW"));
	}

	public void returnItem(String memberId, String itemType, String itemId) throws LibraryException {

		Member m = findMember(memberId);
		if (m == null)
			throw new LibraryException("Member not found.");

		Borrowable item = getBorrowable(itemType, itemId);
		if (item == null)
			throw new LibraryException("Item not found.");

		if (!m.hasBorrowed(itemId))
			throw new LibraryException("This member did not borrow that item.");

		// item.returnItem();
		m.returnBook(itemId);

		recordTransaction(new Transaction(memberId, itemType, itemId, "RETURN"));
	}

	private Borrowable getBorrowable(String type, String id) {

		switch (type.toUpperCase()) {
		case "BOOK":
			return findBook(id);
		case "MAGAZINE":
			return findMagazine(id);
		case "EBOOK":
			return findEBook(id);
		}
		return null;
	}

	//  Load Files 

	public void load() throws IOException {
		loadBooks();
		loadMagazines();
		loadEBooks();
		loadMembers();
	}

	private void loadBooks() throws IOException {
		if (!booksFile.exists())
			return;

		BufferedReader br = new BufferedReader(new FileReader(booksFile));
		String id;

		while ((id = br.readLine()) != null) {

			id = id.trim();
			if (id.isEmpty())
				continue;

			String title = br.readLine();
			String author = br.readLine();
			String isbn = br.readLine();
			int pages = Integer.parseInt(br.readLine());
			double price = Double.parseDouble(br.readLine());
			boolean available = Boolean.parseBoolean(br.readLine());
			br.readLine(); // ---

			books.add(new Book(id, title, author, isbn, pages, price, available));
		}
		br.close();
	}

	private void loadMagazines() throws IOException {
		if (!magazinesFile.exists())
			return;

		BufferedReader br = new BufferedReader(new FileReader(magazinesFile));
		String id;

		while ((id = br.readLine()) != null) {

			id = id.trim();
			if (id.isEmpty())
				continue;

			String title = br.readLine();
			int issue = Integer.parseInt(br.readLine());
			int pages = Integer.parseInt(br.readLine());
			boolean available = Boolean.parseBoolean(br.readLine());
			br.readLine(); // ---

			magazines.add(new Magazine(id, title, issue, pages, available));
		}
		br.close();
	}

	private void loadEBooks() throws IOException {
		if (!ebooksFile.exists())
			return;

		BufferedReader br = new BufferedReader(new FileReader(ebooksFile));
		String id;

		while ((id = br.readLine()) != null) {

			id = id.trim();
			if (id.isEmpty())
				continue;

			String title = br.readLine();
			double size = Double.parseDouble(br.readLine());
			boolean available = Boolean.parseBoolean(br.readLine());
			br.readLine(); // ---

			ebooks.add(new EBook(id, title, size, available));
		}
		br.close();
	}

	private void loadMembers() throws IOException {
		if (!membersFile.exists())
			return;

		BufferedReader br = new BufferedReader(new FileReader(membersFile));
		String id;

		while ((id = br.readLine()) != null) {

			id = id.trim();
			if (id.isEmpty())
				continue;

			String name = br.readLine();
			String password = br.readLine();
			String borrowed = br.readLine();
			br.readLine(); // ---

			Member m = new Member(id, name, password);

			if (borrowed != null && !borrowed.isEmpty()) {
				String[] arr = borrowed.split(";");
				for (String item : arr)
					if (!item.isEmpty())
						m.borrowBook(item);
			}
			members.add(m);
		}
		br.close();
	}

	//  Save Files 

	public void save() throws IOException {
		saveBooks();
		saveMagazines();
		saveEBooks();
		saveMembers();
	}

	private void saveBooks() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(booksFile));

		for (Book b : books) {
			bw.write(b.getId());
			bw.newLine();
			bw.write(b.getTitle());
			bw.newLine();
			bw.write(b.getAuthor());
			bw.newLine();
			bw.write(b.getIsbn());
			bw.newLine();
			bw.write(String.valueOf(b.getPages()));
			bw.newLine();
			bw.write(String.valueOf(b.getPrice()));
			bw.newLine();
			bw.write(String.valueOf(b.isAvailable()));
			bw.newLine();
			bw.write("---");
			bw.newLine();
		}
		bw.close();
	}

	private void saveMagazines() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(magazinesFile));

		for (Magazine m : magazines) {
			bw.write(m.getId());
			bw.newLine();
			bw.write(m.getTitle());
			bw.newLine();
			bw.write(String.valueOf(m.getIssueNumber()));
			bw.newLine();
			bw.write(String.valueOf(m.getPages()));
			bw.newLine();
			bw.write(String.valueOf(m.isAvailable()));
			bw.newLine();
			bw.write("---");
			bw.newLine();
		}
		bw.close();
	}

	private void saveEBooks() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(ebooksFile));

		for (EBook e : ebooks) {
			bw.write(e.getId());
			bw.newLine();
			bw.write(e.getTitle());
			bw.newLine();
			bw.write(String.valueOf(e.getFileSizeMB()));
			bw.newLine();
			bw.write(String.valueOf(e.isAvailable()));
			bw.newLine();
			bw.write("---");
			bw.newLine();
		}
		bw.close();
	}

	private void saveMembers() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(membersFile));

		for (Member m : members) {
			bw.write(m.getId());
			bw.newLine();
			bw.write(m.getName());
			bw.newLine();
			bw.write(m.getPassword());
			bw.newLine();
			bw.write(m.borrowedIdsAsLine());
			bw.newLine();
			bw.write("---");
			bw.newLine();
		}
		bw.close();
	}

	//  Transactions 

	public void recordTransaction(Transaction t) throws LibraryException {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(transactionsFile, true));
			bw.write(t.toLine());
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			throw new LibraryException("Failed to record transaction.");
		}
	}

	public List<Transaction> getAllTransactions() throws IOException {

		ArrayList<Transaction> list = new ArrayList<>();

		if (!transactionsFile.exists())
			return list;

		BufferedReader br = new BufferedReader(new FileReader(transactionsFile));
		String line;

		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (!line.isEmpty()) {
				Transaction t = Transaction.fromLine(line);
				if (t != null)
					list.add(t);
			}
		}
		br.close();
		return list;
	}

	public List<Transaction> getTransactionsForMember(String memberId) throws IOException {

		ArrayList<Transaction> result = new ArrayList<>();

		for (Transaction t : getAllTransactions()) {
			if (t.getMemberId().equals(memberId))
				result.add(t);
		}
		return result;
	}

	//  Sample Data
	public void seedSampleData() {
		try {
			if (books.isEmpty()) {
				addBook(new Book("B001", "Harry Potter", "J. K. Rowling", "HP001", 350, 300.0, true));
				addBook(new Book("B002", "The Alchemist", "Paulo Coelho", "ALC001", 200, 250.0, true));
				addBook(new Book("B003", "The Alchemist", "Paulo Coelho", "9780134685991", 416, 500.0, true));
			}
			if (magazines.isEmpty()) {
				addMagazine(new Magazine("M001", "National Geographic", 42, 56, true));
			}
			if (ebooks.isEmpty()) {
				addEBook(new EBook("E001", "Java Essentials", 2.5, true));
			}
			if (members.isEmpty()) {
				addMember(new Member("U001", "Eshita Roy", "pass1"));
				addMember(new Member("U002", "Eillin", "pass2"));
			}
		} catch (LibraryException e) {
		}
	}
}
