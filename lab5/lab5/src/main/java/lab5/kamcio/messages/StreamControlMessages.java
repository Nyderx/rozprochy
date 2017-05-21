package lab5.kamcio.messages;

import akka.japi.function.Function;

public class StreamControlMessages {
	public static String ACK = "ACK";
	public static String COMPLETED = "COMPLETED";
	public static String INIT = "INIT";

	public static Function<Throwable, Object> FAILURE = e -> "FAILURE: " + e.getMessage();

}
