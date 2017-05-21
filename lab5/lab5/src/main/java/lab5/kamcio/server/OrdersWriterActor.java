package lab5.kamcio.server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import akka.actor.AbstractActor;
import lab5.kamcio.messages.OrderAnswer;
import lab5.kamcio.messages.OrderRequest;

public class OrdersWriterActor extends AbstractActor {

	private static final String ORDERS_FILE = "orders.txt";

	private final BufferedWriter writer;

	public OrdersWriterActor() throws IOException {
		writer = new BufferedWriter(new FileWriter(ORDERS_FILE));
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(OrderRequest.class, this::handleOrderRequest)
							   .match(String.class, s -> save(new OrderRequest(s)))
							   .build();
	}

	private void handleOrderRequest(final OrderRequest order) throws IOException {
		save(order);
		getSender().tell(new OrderAnswer(true), getSender());
	}

	private void save(final OrderRequest order) throws IOException {
		writer.write(order.title());
		writer.flush();
		System.out.println(order.title());
	}

	@Override
	public void postStop() throws Exception {
		writer.close();
	}
}
