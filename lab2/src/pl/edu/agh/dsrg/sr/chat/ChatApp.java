package pl.edu.agh.dsrg.sr.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ChatApp {

	private final static String FIRST_CHANNEL_NAME = "230.0.0.36";

	public static void main(final String args[]) throws Exception {
		System.setProperty("java.net.preferIPv4Stack", "true");
		final Scanner scanner = new Scanner(System.in);
		System.out.println("Type you nickname: ");
		final String nickname = scanner.nextLine();
		System.out.println("Hi, " + nickname);

		final Chat chat = new Chat(nickname);
		chat.start();
		chat.connectToChannel(FIRST_CHANNEL_NAME);

		final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		boolean isValid = true;
		while (isValid) {
			System.out.print(chat.getActiveChannel() + "> ");

			final String line = in.readLine();

			if (line.startsWith("/c")) {
				chat.connectToChannel(line.substring(3));
			} else if (line.startsWith("/d")) {
				chat.disconnectFromChannel(line.substring(3));
			} else if (line.startsWith("/s")) {
				chat.switchChannel(line.substring(3));
			} else if (line.startsWith("/l")) {
				chat.getChannels().forEach(System.out::println);
			} else if (line.startsWith("/c")) {
				chat.getMembers(line.substring(3)).forEach(System.out::println);
			} else if (line.startsWith("/a")) {
				chat.getChannels().forEach(channel -> {
					System.out.println(channel);
					chat.getMembers(channel).forEach(System.out::println);
				});
			} else if (line.startsWith("/q")) {
				chat.stop();
				isValid = false;
			} else {
				chat.send(line);
			}
		}
		scanner.close();
		in.close();
	}
}
