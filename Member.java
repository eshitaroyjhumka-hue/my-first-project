package library;

import java.util.ArrayList;

public class Member {

	private String id;
	private String name;
	private String password;
	private ArrayList<String> borrowedIds = new ArrayList<>();

	public Member(String id, String name, String password) {
		this.id = id;
		this.name = name;
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public void borrowBook(String itemId) {
		borrowedIds.add(itemId);
	}

	public void returnBook(String itemId) {
		borrowedIds.remove(itemId);
	}

	public boolean hasBorrowed(String itemId) {
		return borrowedIds.contains(itemId);
	}

	public int borrowedCount() {
		return borrowedIds.size();
	}

	public String borrowedIdsAsLine() {
		if (borrowedIds.isEmpty())
			return "";
		StringBuilder sb = new StringBuilder();
		for (String s : borrowedIds)
			sb.append(s).append(";");
		return sb.toString();
	}
}
