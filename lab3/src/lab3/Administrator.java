package lab3;

import java.io.IOException;
import java.util.Scanner;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Administrator implements Runnable {

	private Channel channel;

	@Override
	public void run() {
		try {
			final ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			final Connection connection = factory.newConnection();
			channel = connection.createChannel();

			channel.exchangeDeclare(Utils.EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

			final String queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, Utils.EXCHANGE_NAME, "#");

			channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(final String consumerTag, final Envelope envelope,
						final AMQP.BasicProperties properties, final byte[] body) throws IOException {
					System.out.println("log: " + new String(body) + " " + envelope.getRoutingKey());
				}
			});

			channel.exchangeDeclare(Utils.INFO_EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
			channel.queueDeclare(Utils.INFO_QUEUE_NAME, false, false, false, null);

			System.out.println("Administrator is coming...");

			final Scanner scanner = new Scanner(System.in);
			boolean isValid = true;
			while (isValid) {
				final String line = scanner.nextLine();
				if (line.equals("quit")) {
					isValid = false;
					break;
				}
				sendInfo(line);
				System.out.println("Info sended: " + line);
			}

			scanner.close();
			channel.close();
			connection.close();
		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void sendInfo(final String message) throws IOException {
		channel.basicPublish(Utils.INFO_EXCHANGE_NAME, Utils.INFO_QUEUE_NAME, null, message.getBytes());
	}

	public static void main(final String[] args) {
		new Administrator().run();
	}

}
