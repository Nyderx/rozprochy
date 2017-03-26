package pl.edu.agh.dsrg.sr.chat;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.Queue;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.protocols.BARRIER;
import org.jgroups.protocols.FD_ALL;
import org.jgroups.protocols.FD_SOCK;
import org.jgroups.protocols.FRAG2;
import org.jgroups.protocols.MERGE3;
import org.jgroups.protocols.MFC;
import org.jgroups.protocols.PING;
import org.jgroups.protocols.UDP;
import org.jgroups.protocols.UFC;
import org.jgroups.protocols.UNICAST3;
import org.jgroups.protocols.VERIFY_SUSPECT;
import org.jgroups.protocols.pbcast.FLUSH;
import org.jgroups.protocols.pbcast.GMS;
import org.jgroups.protocols.pbcast.NAKACK2;
import org.jgroups.protocols.pbcast.STABLE;
import org.jgroups.protocols.pbcast.STATE_TRANSFER;
import org.jgroups.stack.ProtocolStack;

import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos.ChatMessage;

public class Channel extends ReceiverAdapter {

	private final String name;
	private final JChannel channel;
	private final Queue<String> unreadMessages = new LinkedList<>();
	private boolean isActive = true;

	public Channel(final String name, final String nickname) throws Exception {
		this.name = name;
		channel = new JChannel(false);
		setStack();
		channel.setName(nickname);
		channel.connect(name);
		channel.setReceiver(this);
	}

	public synchronized void send(final String line) throws Exception {
		channel.send(new Message(null, null, ChatMessage.newBuilder().setMessage(line).build()));
	}

	@Override
	public void receive(final Message message) {
		final StringBuilder sb = new StringBuilder();
		sb.append("[").append(name).append("] ").append(channel.getName(message.getSrc())).append(": ")
				.append(((ChatMessage) message.getObject()).getMessage());
		final String line = sb.toString();
		if (!isActive) {
			unreadMessages.add(line);
		} else {
			System.out.println(line);
		}
	}

	public synchronized void close() {
		channel.close();
	}

	public String getName() {
		return name;
	}

	public void setActive() {
		isActive = true;
		while (!unreadMessages.isEmpty()) {
			System.out.println(unreadMessages.poll());
		}
	}

	public void setInactive() {
		isActive = false;
	}

	private void setStack() throws Exception {
		final ProtocolStack stack = new ProtocolStack();
		channel.setProtocolStack(stack);

		stack.addProtocol(new UDP().setValue("mcast_group_addr", InetAddress.getByName(name))).addProtocol(new PING())
				.addProtocol(new MERGE3()).addProtocol(new FD_SOCK())
				.addProtocol(new FD_ALL().setValue("timeout", 12000).setValue("interval", 3000))
				.addProtocol(new VERIFY_SUSPECT()).addProtocol(new BARRIER()).addProtocol(new NAKACK2())
				.addProtocol(new UNICAST3()).addProtocol(new STABLE()).addProtocol(new GMS()).addProtocol(new UFC())
				.addProtocol(new MFC()).addProtocol(new FRAG2()).addProtocol(new STATE_TRANSFER())
				.addProtocol(new FLUSH());
		stack.init();
	}
}
