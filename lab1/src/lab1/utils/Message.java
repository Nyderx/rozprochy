package lab1.utils;

import java.io.*;
import java.util.Optional;

public class Message implements Serializable {
	private final String message;
	private final String clientId;

	public Message(final String message, final String clientId) {
		this.message = message;
		this.clientId = clientId;
	}

	@Override
	public String toString() {
		return clientId + ": " +  message;
	}

	public byte[] toBytesArray() {
		try {
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(this);
			return byteArrayOutputStream.toByteArray();
		} catch (final IOException e) {
		}
		return null;
	}

	public static Optional<Message> fromBytesArray(final byte[] bytes) {
		try {
			final ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
			final Object object = objectInputStream.readObject();
			if(object instanceof Message) {
				return Optional.of((Message) object);
			}
		} catch (final IOException | ClassNotFoundException e) {

		}
		return Optional.empty();
	}
}
