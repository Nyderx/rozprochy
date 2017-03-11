package lab1.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class CommunicationUtils {

	private CommunicationUtils() {
		throw new UnsupportedOperationException();
	}

	public static InetAddress DEFAULT_ADDRESS;
	static {
		try {
			DEFAULT_ADDRESS = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static final byte[] ASCII_ART;

	public static final int ASCII_ART_LENGTH;
	static {
		final BufferedImage image = new BufferedImage(64, 32, BufferedImage.TYPE_INT_RGB);
		final Graphics g = image.getGraphics();
		g.setFont(new Font("Dialog", Font.PLAIN, 12));
		final Graphics2D graphics = (Graphics2D) g;
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics.drawString("Ohayo", 0, 24);
		try {
			final File file = new File("text.png");
			ImageIO.write(image, "png", file);
			file.delete();
		} catch (final IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		final StringBuilder sb = new StringBuilder();

		for (int y = 0; y < 32; y++) {
			for (int x = 0; x < 64; x++)
				sb.append(image.getRGB(x, y) == -16777216 ? " " : image.getRGB(x, y) == -1 ? "#" : "*");
			if (sb.toString().trim().isEmpty()) continue;
			sb.append("\n");
		}
		ASCII_ART = sb.toString().getBytes();
		ASCII_ART_LENGTH = ASCII_ART.length;
	}

	public static final int DEFAULT_PORT = 4444;

	public static final int DATAGRAM_SIZE = 8192;

	public static final int DEFAULT_MULTICAST_PORT = 4446;

	public static InetAddress DEFAULT_MULTICAST_ADDRESS;

	static {
		try {
			DEFAULT_MULTICAST_ADDRESS = InetAddress.getByName("225.0.113.7");
		} catch (final UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
