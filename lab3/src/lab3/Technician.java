package lab3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Technician extends InfoReceiver implements Runnable {

	final BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

	private final Consumer examinationConsumer = new DefaultConsumer(channel) {
		@Override
		public void handleDelivery(final String consumerTag, final Envelope envelope,
				final AMQP.BasicProperties properties, final byte[] body) throws IOException {
			final AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
					.correlationId(properties.getCorrelationId()).build();

			final String message = new String(body, "UTF-8");
			System.out.println("Examinating " + message + " for " + envelope.getRoutingKey());

			try {
				Thread.sleep(10000);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}

			channel.basicPublish(Utils.EXCHANGE_NAME, properties.getReplyTo(), replyProps,
					new String(message + " examination").getBytes());
			channel.basicAck(envelope.getDeliveryTag(), false);

			System.out.println("Examinating " + message + " for " + envelope.getRoutingKey() + " ended");
		}
	};

	@Override
	public void run() {
		try {
			System.out.println("Technician");

			final ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			final Connection connection = factory.newConnection();
			channel = connection.createChannel();

			startReceivingInfo();

			channel.exchangeDeclare(Utils.EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
			prepareQueues();
			channel.basicQos(1);

			registerForInjuries();

			System.out.println("Listening");

			String line;
			while ((line = inputReader.readLine()) != null) {
				if (line.equals("quit")) {
					break;
				}
			}

			channel.close();
			connection.close();
		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void registerForInjuries() throws IOException {
		String line;
		line = inputReader.readLine();
		channel.basicConsume(Injury.valueOf(line).toString(), false, examinationConsumer);

		line = inputReader.readLine();
		channel.basicConsume(Injury.valueOf(line).toString(), false, examinationConsumer);
	}

	private void prepareQueues() throws IOException {
		prepareQueue(Injury.ANKLE.toString());
		prepareQueue(Injury.ELBOW.toString());
		prepareQueue(Injury.KNEE.toString());
	}

	private void prepareQueue(final String key) throws IOException {
		channel.queueDeclare(key, false, false, false, null);
		channel.queueBind(key, Utils.EXCHANGE_NAME, key);
	}

	public static void main(final String[] args) {
		new Technician().run();
	}
}
