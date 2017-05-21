package lab5.kamcio;

import java.io.Serializable;

public class BookRecord implements Serializable {
	private static final long serialVersionUID = 5041595232689527686L;

	private final String title;
	private final int price;

	public BookRecord(final String title, final int price) {
		this.title = title;
		this.price = price;
	}

	public String title() {
		return title;
	}

	public int price() {
		return price;
	}
}
