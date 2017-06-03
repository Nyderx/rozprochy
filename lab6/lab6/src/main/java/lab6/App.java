package lab6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(final String[] args) throws InterruptedException, IOException {
		final Executor executor = new Executor("localhost", Integer.parseInt(args[0]), args[1], "/znodeTestowy");

		executor.start();

		final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.equals("q")) {
				executor.stop();
				break;
			} else if (line.equals("show")) {
				executor.printNodeStructure();
			} else {
				System.out.println("Wrong command");
			}
		}
	}
}
