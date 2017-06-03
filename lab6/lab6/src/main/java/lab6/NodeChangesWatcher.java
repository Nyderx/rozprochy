package lab6;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class NodeChangesWatcher implements Watcher {

	private final ZooKeeper zooKeeper;
	private final String znodePath;
	private NodeChangesListener listener;
	private boolean shouldBeRegistered = false;

	public NodeChangesWatcher(final ZooKeeper zooKeeper, final String znodePath) {
		this.zooKeeper = zooKeeper;
		this.znodePath = znodePath;
	}

	@Override
	public void process(final WatchedEvent event) {
		if (shouldBeRegistered) {
			registerAsWatcher();

			switch (event.getType()) {
			case NodeCreated:
				if (listener != null) {
					listener.onNodeCreated();
				}
				break;
			case NodeDeleted:
				if (listener != null) {
					listener.onNodeDeleted();
				}
				break;
			case NodeChildrenChanged:
				if (listener != null) {
					listener.onChildrenChange();
				}
				break;
			default:
				System.out.println("Not served eventType");
				break;
			}
		}
	}

	public void register() {
		shouldBeRegistered = true;
		registerAsWatcher();
	}

	public void unregister() {
		shouldBeRegistered = false;
	}

	public void setListener(final NodeChangesListener listener) {
		this.listener = listener;
	}

	private void registerAsWatcher() {
		try {
			if (zooKeeper.exists(znodePath, this) != null) {
				final Queue<String> remainedNodes = new LinkedList<>();
				remainedNodes.add(znodePath);

				while (!remainedNodes.isEmpty()) {
					final String node = remainedNodes.poll();
					final List<String> children = zooKeeper.getChildren(node, this);
					remainedNodes.addAll(children.stream().map(childrenName -> node + "/" + childrenName).collect(
							Collectors.toList()));
				}
			}
		} catch (final KeeperException e) {
			e.printStackTrace();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

	}

}
