package lab5.kamcio.messages;

import java.io.Serializable;

public class OrderRequest implements Serializable {
	private static final long serialVersionUID = 873011811900290793L;

	private final String title;

	public OrderRequest(final String title) {
		this.title = title;
	}

	public String title() {
		return title;
	}

}
