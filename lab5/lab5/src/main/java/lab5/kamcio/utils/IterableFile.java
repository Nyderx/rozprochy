package lab5.kamcio.utils;

import java.io.FileNotFoundException;
import java.util.Iterator;

public class IterableFile implements Iterable<String> {

	private final String path;

	public IterableFile(final String path) {
		this.path = path;
	}

	@Override
	public Iterator<String> iterator() {
		try {
			return new FileIterator(path);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("File does not exist");
		}
	}
}
