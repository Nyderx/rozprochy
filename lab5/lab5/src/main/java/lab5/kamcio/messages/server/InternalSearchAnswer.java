package lab5.kamcio.messages.server;

import lab5.kamcio.messages.SearchAnswer;

public class InternalSearchAnswer {
	private final long id;
	private final SearchAnswer answer;

	public InternalSearchAnswer(final long id, final SearchAnswer answer) {
		this.id = id;
		this.answer = answer;
	}

	public long id() {
		return id;
	}

	public SearchAnswer answer() {
		return answer;
	}
}
