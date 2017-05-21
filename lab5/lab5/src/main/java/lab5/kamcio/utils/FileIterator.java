package lab5.kamcio.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class FileIterator implements Iterator<String> {
	private static int MAX_LINE_LENGTH = 8192;

	private final BufferedReader reader;

	public FileIterator(final String path) throws FileNotFoundException {
		reader = new BufferedReader(new FileReader(path));
	}

	@Override
	public boolean hasNext() {
		try {
			reader.mark(MAX_LINE_LENGTH);
			if (reader.readLine() == null) {
				return false;
			}
			reader.reset();
			return true;
		} catch (final IOException e) {
			return false;
		}
	}

	@Override
	public String next() {
		try {
			return reader.readLine();
		} catch (final IOException e) {
			return null;
		}
	}
}