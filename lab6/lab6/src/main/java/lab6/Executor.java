package lab6;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

public class Executor {

	private final String host;
	private final int port;
	private final String execCmd;
	private final String znodePath;

	private ZooKeeper zooKeeper;

	public Executor(final String host, final int port, final String execCmd, final String znodePath) {
		this.host = host;
		this.port = port;
		this.execCmd = execCmd;
		this.znodePath = znodePath;
	}

	public void start() {
		try {
			zooKeeper = new ZooKeeper(host + ":" + port, 10000, null);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		final NodeChangesWatcher nodeChangesWatcher = new NodeChangesWatcher(zooKeeper, znodePath);
		nodeChangesWatcher.setListener(new NodeChangesListener(execCmd, this::countChildren));
		nodeChangesWatcher.register();
	}

	public void printNodeStructure() {
		final Queue<String> remainedNodes = new LinkedList<>();
		remainedNodes.add(znodePath);
		try {
			while (!remainedNodes.isEmpty()) {
				final String node = remainedNodes.poll();
				final List<String> children = zooKeeper.getChildren(node, false);

				System.out.println("Children for " + node);
				children.forEach(zNode -> System.out.println(zNode));
				remainedNodes.addAll(
						children.stream().map(childrenName -> node + "/" + childrenName).collect(Collectors.toList()));
			}
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			zooKeeper.close();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	private int countChildren() {
		final Queue<String> remainedNodes = new LinkedList<>();
		remainedNodes.add(znodePath);

		int count = 0;

		try {
			while (!remainedNodes.isEmpty()) {
				final String node = remainedNodes.poll();
				count++;
				final List<String> children = zooKeeper.getChildren(node, false);
				remainedNodes.addAll(
						children.stream().map(childrenName -> node + "/" + childrenName).collect(Collectors.toList()));
			}
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
			return -1;
		}
		return count;

	}

}
