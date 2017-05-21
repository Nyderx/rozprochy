package lab5.kamcio.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import lab5.kamcio.BookRecord;
import lab5.kamcio.messages.SearchAnswer;
import lab5.kamcio.messages.SearchRequest;
import lab5.kamcio.messages.server.InternalSearchAnswer;
import lab5.kamcio.messages.server.InternalSearchRequest;

public class DatabaseReaderWorker extends AbstractActor {
	private static String DELIMS = ";";

	private final String databaseFile;

	public DatabaseReaderWorker(final String databaseFile) throws IOException {
		this.databaseFile = databaseFile;
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(InternalSearchRequest.class, this::handleInternalSearchRequest).build();
	}

	private void handleInternalSearchRequest(final InternalSearchRequest request) {
		final SearchAnswer answer = new SearchAnswer(search(request.request()));
		final InternalSearchAnswer internalAnswer = new InternalSearchAnswer(request.id(), answer);
		getSender().tell(internalAnswer, getSelf());

	}

	private BookRecord search(final SearchRequest request) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(databaseFile));

			String line;
			while ((line = reader.readLine()) != null) {
				final String[] tokens = line.split(DELIMS);
				if (tokens[0].equals(request.title())) {
					return new BookRecord(tokens[0], Integer.parseInt(tokens[1]));
				}
			}
		} catch (final IOException e) {
			System.out.println("Database is not available");
		} finally {
			try {
				reader.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	public static void main(final String[] args) throws IOException {
		final ActorSystem system = ActorSystem.create("local_system");
		final ActorRef actor = system.actorOf(Props.create(DatabaseReaderWorker.class, "database1.txt"),
				"databaseReader");

		System.out.println("Starting");
		// read line & send to actor
		final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			final String line = br.readLine();
			if (line.equals("q")) {
				break;
			}
			actor.tell(line, null); // send message to actor
		}

		// finish
		system.terminate();
	}

}
