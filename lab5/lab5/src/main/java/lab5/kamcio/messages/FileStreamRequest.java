package lab5.kamcio.messages;

import java.io.Serializable;

public class FileStreamRequest implements Serializable {
	private static final long serialVersionUID = -3021736810124892512L;

	private final String title;

	public FileStreamRequest(final String title) {
		this.title = title;
	}

	public String title() {
		return title;
	}
}
