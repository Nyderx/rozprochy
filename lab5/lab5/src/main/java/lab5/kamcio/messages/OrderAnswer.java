package lab5.kamcio.messages;

import java.io.Serializable;

public class OrderAnswer implements Serializable {
	private static final long serialVersionUID = 6869055793213564266L;

	private final boolean isRealized;

	public OrderAnswer(final boolean isRealized) {
		this.isRealized = isRealized;
	}

	public boolean isRealized() {
		return isRealized;
	}

}
