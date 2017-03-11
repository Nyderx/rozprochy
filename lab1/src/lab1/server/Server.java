package lab1.server;

import lab1.utils.CommunicationUtils;
import lab1.utils.Message;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable {

	private final static Logger logger = Logger.getLogger(Server.class.toString());

	private final static int MAX_CLIENTS = 5;

	private final List<Client> clients = new LinkedList<>();

	private final AtomicBoolean taskValidProperty = new AtomicBoolean(true);

	private final ExecutorService clientsExecutor = Executors.newFixedThreadPool(MAX_CLIENTS);

	private final ExecutorService sendingExecutor = Executors.newSingleThreadExecutor();

	private ServerSocket serverSocket;

	private DatagramSocket datagramSocket;

	private MulticastSocket multicastSocket;


	public Server() {
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(CommunicationUtils.DEFAULT_PORT);
			datagramSocket = new DatagramSocket(CommunicationUtils.DEFAULT_PORT);
			multicastSocket = new MulticastSocket(CommunicationUtils.DEFAULT_MULTICAST_PORT);
		} catch(final IOException e) {
			logger.log(Level.SEVERE, e.toString());
			close();
			System.exit(1);
		}

		new Thread(this::runUdpServer).start();
		new Thread(this::runMulticastServer).start();
		new Thread(this::runTcpServer).start();

	}

	private void runTcpServer() {
		try {
			while (taskValidProperty.get()) {

				final Socket clientSocket = serverSocket.accept();
				final Client client = new Client(clientSocket, this);
				clientsExecutor.submit(client);
			}
		} catch (final IOException e) {
			logExceptionIfNeeded(e);
		}
	}

	private void runUdpServer() {
		try  {
			while (taskValidProperty.get()) {
				final byte[] buffer = new byte[CommunicationUtils.DATAGRAM_SIZE];
				final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				datagramSocket.receive(packet);

				synchronized (clients) {
					clients.stream()
							.filter(client -> packet.getAddress() != client.getAddress()
									                  && packet.getPort() != client.getPort())
							.forEach(client -> {
								try {
									datagramSocket.send(new DatagramPacket(buffer, buffer.length, client.getAddress(), client.getPort()));
								} catch (final IOException e) {
									logger.log(Level.SEVERE, e.toString());
								}
							});
				}

				System.out.println("Got by UDP: " + Message.fromBytesArray(packet.getData()).toString());
			}
		} catch (final IOException e) {
			logExceptionIfNeeded(e);
		}
	}

	private void runMulticastServer() {
		try {
			multicastSocket.joinGroup(CommunicationUtils.DEFAULT_MULTICAST_ADDRESS);

			while(taskValidProperty.get()) {
				final byte[] buffer = new byte[CommunicationUtils.DATAGRAM_SIZE];
				final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				multicastSocket.receive(packet);

				System.out.println("Received by multicast UDP:" + Message.fromBytesArray(packet.getData()).toString());
			}

		} catch (final IOException e) {
			logExceptionIfNeeded(e);
		}
	}

	private void broadcast(final Message message, final Client source) {
		sendingExecutor.submit(() -> {
			synchronized(clients) {
				clients.stream().filter(client -> !client.equals(source)).filter(Client::isConnected).forEach(client -> {
					client.send(message);
				});
			}
		});
	}

	private void addClient(final Client client) {
		synchronized (clients) {
			clients.add(client);
		}
	}

	private void removeClient(final Client client) {
		synchronized (clients) {
			clients.remove(client);
		}
	}

	public void close() {
		taskValidProperty.set(false);
		synchronized (clients) {
			clients.forEach(Client::clean);
		}
		try {
			serverSocket.close();
		} catch (final IOException e) {
			logger.log(Level.SEVERE, e.toString());
		}
		datagramSocket.close();
		multicastSocket.close();
	}

	private void logExceptionIfNeeded(final Exception e) {
		if(taskValidProperty.get()) {
			logger.log(Level.SEVERE, e.toString());
		}
	}

	private static final class Client implements Runnable {
		private final static Logger clientLogger = Logger.getLogger(Client.class.toString());

		private final Socket socket;
		private final Server server;
		private ObjectOutputStream out;
		private ObjectInputStream in;

		public Client(final Socket socket, final Server server) {
			this.socket = socket;
			this.server = server;
			try {
				out = new ObjectOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());
			} catch (final IOException e) {
				clientLogger.log(Level.SEVERE, e.toString());
			}
		}

		public void run() {
			System.out.println("Client connected");
			server.addClient(this);

			try {
				Message message;
				while ((message = readMessage()) != null) {
					System.out.println(message.toString());
					server.broadcast(message, this);
				}
				clean();
			} catch (final IOException e) {
				if(!(e instanceof EOFException)) {
					clientLogger.log(Level.SEVERE, e.toString());
				}
				clean();
			}
		}

		public boolean isConnected() {
			return socket.isConnected();
		}

		public InetAddress getAddress() {
			return socket.getInetAddress();
		}

		public int getPort() {
			return socket.getPort();
		}

		public void send(final Message message) {
			try {
				out.writeObject(message);
			} catch (final IOException e) {
				clientLogger.log(Level.SEVERE, "Could not send message to client due to {}", e);
			}
		}

		public Message readMessage() throws IOException {
			try {
				final Object object = in.readObject();
				if(object instanceof Message) {
					return (Message) object;
				}
			} catch (final ClassNotFoundException e) {
				clientLogger.log(Level.SEVERE, e.toString());
			}
			return null;
		}

		public void clean() {
			System.out.println("Client disconnected");

			server.removeClient(this);
			if(out != null) {
				try {
					out.close();
				} catch (final IOException e) {
					logger.log(Level.SEVERE, e.toString());
				}			}

			if(in != null) {
				try {
					in.close();
				} catch (final IOException e) {
					logger.log(Level.SEVERE, e.toString());
				}
			}

			if(socket != null) {
				try {
					socket.close();
				} catch (final IOException e) {
					clientLogger.log(Level.SEVERE, e.toString());
				}
			}
		}
	}

}
