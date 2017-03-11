package lab1.client;

import lab1.command_handler.CommandHandler;
import lab1.utils.CommunicationUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientApp implements Runnable {
	private final static Logger logger = Logger.getLogger(ClientApp.class.toString());

	private final Client client;

	public ClientApp() {
		client = new Client(CommunicationUtils.DEFAULT_ADDRESS);
	}

	@Override
	public void run() {
		System.out.println("Starting client");

		final AtomicBoolean taskValidProperty = new AtomicBoolean(true);

		final CommandHandler handler = new CommandHandler();
		handler.addHandler("/q", () -> {
			taskValidProperty.set(false);
			client.close();
		});
		handler.addHandler("/m", client::sendAsciiArtByUdp);
		handler.addHandler("/n", client::sendMulticast);

		new Thread(client).start();

		try(final BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in))) {
			String line;
			while(taskValidProperty.get() && (line = systemIn.readLine()) != null) {
				if(handler.canHandle(line)) {
					handler.handle(line);
				} else {
					client.send(line);
				}
			}
		} catch(final IOException e) {
			logger.log(Level.SEVERE, e.toString());
		}
	}

	public static void main(final String[] args) {
		new ClientApp().run();
	}

}
