package lab1.command_handler;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler {

	private final Map<String, Runnable> commandsHandlers = new HashMap<>();

	public CommandHandler() {
		//empty
	}

	public void addHandler(final String command, final Runnable handler) {
		commandsHandlers.put(command, handler);
	}

	public boolean canHandle(final String command) {
		return commandsHandlers.containsKey(command);
	}

	public void handle(final String command) {
		commandsHandlers.get(command).run();
	}
}
