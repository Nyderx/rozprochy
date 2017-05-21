package lab5.kamcio.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Server {
	private ActorSystem system;
	private ActorRef searchActor;
	private ActorRef ordersActor;
	private ActorRef streamActor;

	public void start() {
		final File configFile = new File(getClass().getClassLoader().getResource("server.conf").getFile());
		final Config config = ConfigFactory.parseFile(configFile);

		system = ActorSystem.create("server", config);
		searchActor = system.actorOf(Props.create(SearchActor.class), "search");
		ordersActor = system.actorOf(Props.create(OrdersWriterActor.class), "orders");
		streamActor = system.actorOf(Props.create(FilesStreamingActor.class), "stream");
	}

	public void stop() {
		system.stop(searchActor);
		system.stop(ordersActor);
		system.stop(streamActor);
		system.terminate();
	}

	public static void main(final String[] args) throws IOException {
		final Server server = new Server();
		server.start();

		System.out.println("Starting");
		final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			final String line = br.readLine();
			if (line.equals("q")) {
				break;
			}
		}

		server.stop();
	}
}
