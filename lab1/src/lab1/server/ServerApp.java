package lab1.server;

import lab1.command_handler.CommandHandler;
import lab1.utils.CommunicationUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerApp {

	private final static Logger logger = Logger.getLogger(ServerApp.class.toString());


	public static void main(final String[] args) {
		System.out.println("Starting server app");

		final Server server = new Server(CommunicationUtils.DEFAULT_ADDRESS);

		new Thread(server).start();

		final AtomicBoolean isValid = new AtomicBoolean(true);

		final CommandHandler handler = new CommandHandler();
		handler.addHandler("/q", () -> {
			server.close();
			isValid.set(false);

		});

		try {
			final BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));
			String line;
			while(isValid.get() && (line = systemIn.readLine()) != null) {
				if(handler.canHandle(line)) {
					handler.handle(line);
				} else {
					System.out.println("Wrong command");
				}
			}
		} catch(final IOException e) {
			logger.log(Level.INFO, "IOException thrown, closing app {}", e);
			server.close();
			System.exit(1);
		}


	}

}
