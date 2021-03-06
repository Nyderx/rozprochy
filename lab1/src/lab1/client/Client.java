package lab1.client;

import lab1.utils.CommunicationUtils;
import lab1.utils.Message;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements Runnable {

	private final static Logger logger = Logger.getLogger(Client.class.toString());

	private final AtomicBoolean isValid = new AtomicBoolean(true);

	private final InetAddress address;

	private ObjectOutputStream socketOut;

	private Socket tcpSocket;

	private DatagramSocket datagramSocket;

	private MulticastSocket multicastSocket;

	public Client(final InetAddress address) {
		this.address = address;

	}

	@Override
	public void run() {
		try {
			tcpSocket = new Socket(address, CommunicationUtils.DEFAULT_PORT);
			datagramSocket = new DatagramSocket(tcpSocket.getLocalPort());
			multicastSocket = new MulticastSocket(CommunicationUtils.DEFAULT_MULTICAST_PORT);
			socketOut = new ObjectOutputStream(tcpSocket.getOutputStream());

		} catch(final IOException e) {
			logger.log(Level.SEVERE, e.toString());
			close();
			System.exit(1);
		}


		new Thread(this::runTcpReceiver).start();
		new Thread(this::runUdpReceiver).start();
		new Thread(this::runMulticastReceiver).start();
	}

	private void runTcpReceiver() {
		try (ObjectInputStream input = new ObjectInputStream(tcpSocket.getInputStream())) {
			Object object;
			try {
				while (isValid.get() && (object = input.readObject()) != null) {
					if(object instanceof Message) {
						System.out.println("Received by TCP chat: " + object.toString());
					}
				}
			} catch (final ClassNotFoundException e) {
				logger.log(Level.SEVERE, e.toString());
				close();
				System.exit(1);
			}
			close();
		} catch(final IOException e) {
			if(!(e instanceof EOFException)) {
				logExceptionIfNeeded(e);
			}
		}
	}

	private void runUdpReceiver() {
		try {
			while(isValid.get()) {
				final byte[] buffer = new byte[CommunicationUtils.DATAGRAM_SIZE];
				final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				datagramSocket.receive(packet);

				System.out.println("Received by UDP:" + Message.fromBytesArray(packet.getData()).toString());
			}
		} catch(final IOException e) {
			logExceptionIfNeeded(e);
		}
	}
	
	private void runMulticastReceiver() {
		try {
			multicastSocket.joinGroup(CommunicationUtils.DEFAULT_MULTICAST_ADDRESS);

			while(isValid.get()) {
				final byte[] buffer = new byte[CommunicationUtils.DATAGRAM_SIZE];
				final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				multicastSocket.receive(packet);

				System.out.println("Received by UDP multicast:" + Message.fromBytesArray(packet.getData()).toString());
			}
		} catch(final IOException e) {
			logExceptionIfNeeded(e);
		}
	}

	public void send(final String message) throws IOException {
		System.out.println(this.toString());
		socketOut.writeObject(new Message(message, this.toString()));
	}

	public void sendAsciiArtByUdp() {
		final byte[] bytes = new Message(new String(CommunicationUtils.ASCII_ART), this.toString()).toBytesArray();
		final DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, CommunicationUtils.DEFAULT_PORT);
		try {
			datagramSocket.send(packet);
		} catch (final IOException e) {
			logger.log(Level.SEVERE, e.toString());
		}
	}

	public void sendMulticast() {
		final byte[] bytes = new Message(new String(CommunicationUtils.ASCII_ART), this.toString()).toBytesArray();

		final DatagramPacket packet = new DatagramPacket(bytes, bytes.length, CommunicationUtils.DEFAULT_MULTICAST_ADDRESS, CommunicationUtils.DEFAULT_MULTICAST_PORT);
		try {
			datagramSocket.send(packet);
		} catch (final IOException e) {
			logger.log(Level.SEVERE, e.toString());
		}
	}

	public void close() {
		System.out.println("Closing client");

		isValid.set(false);

		if(socketOut != null) {
			try {
				socketOut.close();
			} catch (final IOException e) {
				logger.log(Level.SEVERE, e.toString());
			}
		}

		if(tcpSocket != null) {
			try {
				tcpSocket.close();
			} catch (final IOException e) {
				logger.log(Level.SEVERE, e.toString());
			}
		}


		if(datagramSocket != null) {
			datagramSocket.close();
		}

		if(multicastSocket != null) {
			multicastSocket.close();
		}
	}

	private void logExceptionIfNeeded(final Exception e) {
		if(isValid.get()) {
			logger.log(Level.SEVERE, e.toString());
		}
	}
}
