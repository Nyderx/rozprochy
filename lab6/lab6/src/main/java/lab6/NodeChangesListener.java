package lab6;

import java.io.IOException;
import java.util.function.Supplier;

public class NodeChangesListener {
	private final String execCmd;
	private final Supplier<Integer> childrenCountSupplier;

	Process process;
	Thread shutdownHook;
	Runtime runtime;

	public NodeChangesListener(final String execCmd, final Supplier<Integer> childrenCountSupplier) {
		this.execCmd = execCmd;
		this.childrenCountSupplier = childrenCountSupplier;
	}

	public void onNodeCreated() {
		runtime = Runtime.getRuntime();
		try {
			process = runtime.exec(execCmd);
			runtime.addShutdownHook(new Thread(() -> process.destroy()));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public void onNodeDeleted() {
		if (process != null) {
			process.destroyForcibly();
			process = null;
		}

		if (shutdownHook != null) {
			runtime.removeShutdownHook(shutdownHook);
			shutdownHook = null;
		}
	}

	public void onChildrenChange() {
		System.out.println("Children count: " + childrenCountSupplier.get());
	}
}
