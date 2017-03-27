package pl.edu.agh.dsrg.sr.chat;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.stream.Collectors;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
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

import com.google.protobuf.InvalidProtocolBufferException;

import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos.ChatAction;
import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos.ChatState;

public class ManagementChannel extends ReceiverAdapter {

	private final JChannel channel;
	private final State state;

	public ManagementChannel(final String name, final State state, final String nickname) throws Exception {
		this.state = state;
		channel = new JChannel(false);
		channel.setName(nickname);
		setStack();
		channel.setReceiver(this);
		channel.connect(name);
		channel.getState(null, 10000);
	}

	public synchronized void send(final ChatAction chatAction) throws Exception {
		channel.send(new Message(null, null, chatAction.toByteArray()));
	}

	@Override
	public void viewAccepted(final View view) {
		state.retainMembers(view.getMembers().stream().map(channel::getName).collect(Collectors.toSet()));
	}

	@Override
	public synchronized void receive(final Message message) {
		ChatAction action;
		try {
			action = ChatAction.parseFrom(message.getRawBuffer());

			switch (action.getAction()) {
			case JOIN:
				state.addMember(action.getChannel(), action.getNickname());
				break;
			case LEAVE:
				state.removeMember(action.getChannel(), action.getNickname());
				break;
			}
		} catch (final InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void getState(final OutputStream output) throws Exception {
		state.toChatStateMessage().writeTo(output);
	}

	@Override
	public synchronized void setState(final InputStream input) throws Exception {
		final ChatState chatState = ChatState.parseFrom(input);
		state.fromChatStateMessage(chatState);
	}

	public synchronized void close() {
		channel.close();
	}

	private void setStack() throws Exception {
		final ProtocolStack stack = new ProtocolStack();
		channel.setProtocolStack(stack);

		stack.addProtocol(new UDP()).addProtocol(new PING()).addProtocol(new MERGE3()).addProtocol(new FD_SOCK())
				.addProtocol(new FD_ALL().setValue("timeout", 12000).setValue("interval", 3000))
				.addProtocol(new VERIFY_SUSPECT()).addProtocol(new BARRIER()).addProtocol(new NAKACK2())
				.addProtocol(new UNICAST3()).addProtocol(new STABLE()).addProtocol(new GMS()).addProtocol(new UFC())
				.addProtocol(new MFC()).addProtocol(new FRAG2()).addProtocol(new STATE_TRANSFER())
				.addProtocol(new FLUSH());
		stack.init();
	}
}
