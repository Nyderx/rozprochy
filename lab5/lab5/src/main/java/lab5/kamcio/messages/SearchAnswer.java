package lab5.kamcio.messages;

import java.io.Serializable;

import lab5.kamcio.BookRecord;

public class SearchAnswer implements Serializable {
	private static final long serialVersionUID = 2534066383757788393L;

	private final BookRecord book;

	public SearchAnswer(final BookRecord book) {
		this.book = book;
	}

	public BookRecord bookRecord() {
		return book;
	}

}
