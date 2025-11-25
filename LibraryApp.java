package library;

import java.util.*;
import java.io.*;

public class LibraryApp {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		Library lib = new Library("books.txt", "magazines.txt", "ebooks.txt", "members.txt", "transactions.txt");

		try {
			lib.load();
		} catch (IOException e) {
			System.out.println("Could not load data files.");
		}

		while (true) {
			System.out.println("\n=== Login Menu ===");
			System.out.println("1. Member Login");
			System.out.println("2. Admin Login");
			System.out.println("3. Register as Member");
			System.out.println("4. Exit");
			System.out.print("Choose: ");

			String choice = sc.nextLine();

			switch (choice) {
			case "1":
				memberLogin(sc, lib);
				break;

			case "2":
				adminLogin(sc, lib);
				break;

			case "3":
				registerMember(sc, lib);
				break;

			case "4":
				System.out.println("Exiting...");
				try {
					lib.save();
				} catch (Exception e) {
				}
				return;

			default:
				System.out.println("Invalid choice.");
			}
		}
	}


	// MEMBER LOGIN
	
	private static void memberLogin(Scanner sc, Library lib) {
		System.out.print("Member ID: ");
		String id = sc.nextLine();

		System.out.print("Password: ");
		String pass = sc.nextLine();

		Member m = lib.getMember(id);

		if (m == null || !m.getPassword().equals(pass)) {
			System.out.println("Incorrect ID or password!");
			return;
		}

		memberMenu(sc, lib, m);
	}

	
	// ADMIN LOGIN
	
	private static void adminLogin(Scanner sc, Library lib) {
		System.out.print("Admin ID: ");
		String id = sc.nextLine();

		System.out.print("Password: ");
		String pass = sc.nextLine();

		if (id.equals("admin1") && pass.equals("admin123")) {
			adminMenu(sc, lib);
		} else if (id.equals("admin2") && pass.equals("admin123")) {
			adminMenu(sc, lib);
		} else {
			System.out.println("Wrong admin ID or password!");
		}
	}

	
	// REGISTER MEMBER
	
	private static void registerMember(Scanner sc, Library lib) {
		try {
			System.out.print("Enter new Member ID: (Member id starts with U00): ");
			String id = sc.nextLine();

			System.out.print("Enter name: ");
			String name = sc.nextLine();

			System.out.print("Enter password: ");
			String pass = sc.nextLine();

			Member m = new Member(id, name, pass);
			lib.addMember(m);

			lib.save();

			System.out.println("Member registered successfully!");
		} catch (Exception e) {
			System.out.println("Sorry! " + e.getMessage());
		}
	}

	
	// ADMIN MENU
	
	private static void adminMenu(Scanner sc, Library lib) {
		while (true) {
			System.out.println("\n=== Admin Menu ===");
			System.out.println("1. Add Book");
			System.out.println("2. Add Magazine");
			System.out.println("3. Add EBook");
			System.out.println("4. View All Items");
			System.out.println("5. View All Transactions");
			System.out.println("6. Logout");
			System.out.print("Choose: ");

			String ch = sc.nextLine();

			try {
				switch (ch) {
				case "1":
					addBook(sc, lib);
					break;

				case "2":
					addMagazine(sc, lib);
					break;

				case "3":
					addEBook(sc, lib);
					break;

				case "4":
					viewAllItems(lib);
					break;

				case "5":
					viewAllTransactions(lib);
					break;

				case "6":
					return;

				default:
					System.out.println("Invalid choice.");
				}
			} catch (Exception e) {
				System.out.println("Sorry " + e.getMessage());
			}
		}
	}

	
	// MEMBER MENU
	
	private static void memberMenu(Scanner sc, Library lib, Member m) {
		while (true) {
			System.out.println("\n=== Member Menu ===");
			System.out.println("1. Borrow Item");
			System.out.println("2. Return Item");
			System.out.println("3. View All Items");
			System.out.println("4. View My Transactions");
			System.out.println("5. Logout");
			System.out.print("Choose: ");

			String ch = sc.nextLine();

			try {
				switch (ch) {
				case "1":
					borrowItem(sc, lib, m);
					break;

				case "2":
					returnItem(sc, lib, m);
					break;

				case "3":
					viewAllItems(lib);
					break;

				case "4":
					viewMemberTransactions(lib, m);
					break;

				case "5":
					return;

				default:
					System.out.println("Invalid choice.");
				}
			} catch (Exception e) {
				System.out.println("Sorry " + e.getMessage());
			}
		}
	}

	
	// ADMIN ADD ITEMS
	
	private static void addBook(Scanner sc, Library lib) throws Exception {
		System.out.print("Book ID: ");
		String id = sc.nextLine();

		System.out.print("Title: ");
		String title = sc.nextLine();

		System.out.print("Author: ");
		String author = sc.nextLine();

		System.out.print("ISBN: ");
		String isbn = sc.nextLine();

		System.out.print("Pages: ");
		int pages = Integer.parseInt(sc.nextLine());

		System.out.print("Price: ");
		double price = Double.parseDouble(sc.nextLine());

		lib.addBook(new Book(id, title, author, isbn, pages, price, true));
		lib.save();

		System.out.println("Book added.");
	}

	private static void addMagazine(Scanner sc, Library lib) throws Exception {
		System.out.print("Magazine ID: ");
		String id = sc.nextLine();

		System.out.print("Title: ");
		String title = sc.nextLine();

		System.out.print("Issue Number: ");
		int issue = Integer.parseInt(sc.nextLine());

		System.out.print("Pages: ");
		int pages = Integer.parseInt(sc.nextLine());

		lib.addMagazine(new Magazine(id, title, issue, pages, true));
		lib.save();

		System.out.println(" Magazine added.");
	}

	private static void addEBook(Scanner sc, Library lib) throws Exception {
		System.out.print("EBook ID: ");
		String id = sc.nextLine();

		System.out.print("Title: ");
		String title = sc.nextLine();

		System.out.print("File Size (MB): ");
		double size = Double.parseDouble(sc.nextLine());

		lib.addEBook(new EBook(id, title, size, true));
        lib.addBook(new Book("B001", "Harry Potter", "J. K. Rowling", "HP001", 350, 300.0, true));
        lib.addBook(new Book("B002", "The Alchemist", "Paulo Coelho", "ALC001", 200, 250.0, true));
		lib.save();

		System.out.println("EBook added.");
	}

	
	// MEMBER ACTIONS – BORROW & RETURN
	
	private static void borrowItem(Scanner sc, Library lib, Member m) throws Exception {
		System.out.print("Item Type (BOOK / MAGAZINE / EBOOK): ");
		String type = sc.nextLine().toUpperCase();

		System.out.print("Item ID: ");
		String id = sc.nextLine();

		lib.borrowItem(m.getId(), type, id);
		lib.save();

		System.out.println("Borrowed successfully.");
	}

	private static void returnItem(Scanner sc, Library lib, Member m) throws Exception {
		System.out.print("Item Type (BOOK / MAGAZINE / EBOOK): ");
		String type = sc.nextLine().toUpperCase();

		System.out.print("Item ID: ");
		String id = sc.nextLine();

		lib.returnItem(m.getId(), type, id);
		lib.save();

		System.out.println("Returned successfully.");
	}

	
	// VIEWING FUNCTIONS
	
	private static void viewAllItems(Library lib) {
		System.out.println("\n=== All Items ===");


		for (Item it : lib.listAllItems()) {
			System.out.println(it.displayInfo());
		}
	}

	private static void viewAllTransactions(Library lib) {
		try {
			System.out.println("\n=== All Transactions ===");
			for (Transaction t : lib.getAllTransactions()) {
				System.out.println(t.toLine());
			}
		} catch (Exception e) {
			System.out.println("Unable to read transactions.");
		}
	}

	private static void viewMemberTransactions(Library lib, Member m) {
		try {
			System.out.println("\n=== My Transactions ===");
			for (Transaction t : lib.getTransactionsForMember(m.getId())) {
				System.out.println(t.toLine());
			}
		} catch (Exception e) {
		}
	}

}
