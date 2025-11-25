package library;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

	private String memberId;
	private String itemType;
	private String itemId;
	private String action;
	private String timestamp;

	private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public Transaction(String memberId, String itemType, String itemId, String action) {

		this.memberId = memberId;
		this.itemType = itemType;
		this.itemId = itemId;
		this.action = action;
		this.timestamp = LocalDateTime.now().format(F);
	}

	// Used when loading from file
	public Transaction(String memberId, String itemType, String itemId, String action, String timestamp) {

		this.memberId = memberId;
		this.itemType = itemType;
		this.itemId = itemId;
		this.action = action;
		this.timestamp = timestamp;
	}

	public String getMemberId() {
		return memberId;
	}

	public String toLine() {
		return memberId + " | " + itemType + " | " + itemId + " | " + action + " | " + timestamp;
	}

	public static Transaction fromLine(String line) {
		String[] p = line.split("\\s*\\|\\s*");
		if (p.length != 5)
			return null;

		return new Transaction(p[0], p[1], p[2], p[3], p[4]);
	}
}
