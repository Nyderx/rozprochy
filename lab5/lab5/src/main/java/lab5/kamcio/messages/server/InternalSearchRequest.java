package lab5.kamcio.messages.server;

import lab5.kamcio.messages.SearchRequest;

public class InternalSearchRequest {
	private final long id;
	private final SearchRequest request;

	public InternalSearchRequest(final long id, final SearchRequest request) {
		super();
		this.id = id;
		this.request = request;
	}

	public long id() {
		return id;
	}

	public SearchRequest request() {
		return request;
	}
}
