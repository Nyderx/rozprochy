package lab5.kamcio.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import lab5.kamcio.BookRecord;
import lab5.kamcio.messages.FileStreamRequest;
import lab5.kamcio.messages.OrderAnswer;
import lab5.kamcio.messages.OrderRequest;
import lab5.kamcio.messages.SearchAnswer;
import lab5.kamcio.messages.SearchRequest;
import lab5.kamcio.messages.StreamControlMessages;

public class Client {

	ActorSystem system;
	ActorRef clientActor;

	public void start() {
		final File configFile = new File(getClass().getClassLoader().getResource("client.conf").getFile());
		final Config config = ConfigFactory.parseFile(configFile);

		system = ActorSystem.create("client", config);
		clientActor = system.actorOf(Props.create(ClientActor.class), "clientActor");
	}

	public void stop() {
		system.stop(clientActor);
		system.terminate();
	}

	public void search(final String title) {
		clientActor.tell(new SearchRequest(title), null);
	}

	public void order(final String title) {
		clientActor.tell(new OrderRequest(title), null);
	}

	public void requestStreaming(final String title) {
		clientActor.tell(new FileStreamRequest(title), null);
	}

	private static class ClientActor extends AbstractActor {
		@Override
		public Receive createReceive() {
			return receiveBuilder().match(SearchRequest.class, this::handleSearchRequest)
								   .match(SearchAnswer.class, this::handleSearchAnswer)
								   .match(OrderRequest.class, this::handleOrderRequest)
								   .match(OrderAnswer.class, this::handleOrderAnswer)
								   .match(FileStreamRequest.class, this::handleFileStreamRequest)
								   .match(String.class, this::handleString)
								   .matchAny(o -> System.out.println("received unknown message"))
								   .build();
		}

		private void handleSearchRequest(final SearchRequest request) {
			getContext().actorSelection("akka.tcp://server@127.0.0.1:2552/user/search").tell(request, getSelf());
		}

		private void handleSearchAnswer(final SearchAnswer answer) {
			final BookRecord record = answer.bookRecord();
			if (record == null) {
				System.out.println("Nothing was found");
			} else {
				System.out.println(record.title() + " is available for " + record.price());
			}
		}

		private void handleOrderRequest(final OrderRequest request) {
			getContext().actorSelection("akka.tcp://server@127.0.0.1:2552/user/orders").tell(request, getSelf());
		}

		private void handleOrderAnswer(final OrderAnswer answer) {
			if (answer.isRealized()) {
				System.out.println("Your order was realized");
			} else {
				System.out.println("Order could not be realized");
			}
		}

		private void handleFileStreamRequest(final FileStreamRequest request) {
			getContext().actorSelection("akka.tcp://server@127.0.0.1:2552/user/stream").tell(request, getSelf());
		}

		private void handleString(final String text) {
			if (text.equals(StreamControlMessages.INIT)) {
				System.out.println("Starting streaming");
			} else if (text.equals(StreamControlMessages.COMPLETED)) {
				System.out.println("Streaming completed");
			} else {
				System.out.println(text);
			}
			getSender().tell(StreamControlMessages.ACK, getSelf());
		}
	}

	public static void main(final String[] args) throws IOException {
		final Client client = new Client();
		client.start();

		System.out.println("Starting");
		final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			final String line = br.readLine();
			if (line.equals("q")) {
				break;
			} else if (line.startsWith("order")) {
				client.order(br.readLine());
			} else if (line.startsWith("search")) {
				client.search(br.readLine());
			} else if (line.startsWith("stream")) {
				client.requestStreaming(br.readLine());
			}
		}

		client.stop();

	}
}
