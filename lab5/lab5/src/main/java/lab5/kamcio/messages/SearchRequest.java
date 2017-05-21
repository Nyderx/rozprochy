package lab5.kamcio.messages;

import java.io.Serializable;

public class SearchRequest implements Serializable {
	private static final long serialVersionUID = 2532065383757788393L;

	private final String title;

	public SearchRequest(final String title) {
		this.title = title;
	}

	public String title() {
		return title;
	}
}
