package pl.edu.agh.dsrg.sr.chat;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos.ChatAction;
import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos.ChatAction.ActionType;

public class Chat {
	private static final String DEFAULT_MANAGEMENT_CHANNEL_NAME = "ChatManagement321321";

	private final String nickname;
	private final State state = new State();
	private final Map<String, Channel> channels = new HashMap<>();
	private ManagementChannel managementChannel;
	private Channel activeChannel;

	public Chat(final String nickname) {
		this.nickname = nickname;
	}

	public void start() throws Exception {
		managementChannel = new ManagementChannel(DEFAULT_MANAGEMENT_CHANNEL_NAME, state, nickname);
	}

	public void connectToChannel(final String name) throws Exception {
		if (!channels.containsKey(name)) {
			channels.put(name, new Channel(name, nickname));
			managementChannel.send(
					ChatAction.newBuilder().setAction(ActionType.JOIN).setChannel(name).setNickname(nickname).build());
		}

		switchChannel(name);
	}

	public void disconnectFromChannel(final String name) throws Exception {
		if (channels.containsKey(name) && !channels.isEmpty()) {
			final Channel channel = channels.get(name);
			channels.remove(name);
			if (activeChannel == channel) {
				activeChannel = channels.get(0);
			}
			channel.close();
			managementChannel.send(
					ChatAction.newBuilder().setAction(ActionType.LEAVE).setChannel(name).setNickname(nickname).build());
		}
	}

	public void switchChannel(final String name) {
		if (activeChannel != null) {
			activeChannel.setInactive();
		}
		if (channels.containsKey(name)) {
			activeChannel = channels.get(name);
			activeChannel.setActive();
		} else {
			System.out.println("There is no channel " + name);
		}
	}

	public void send(final String line) throws Exception {
		activeChannel.send(line);
	}

	public String getActiveChannel() {
		return activeChannel.getName();
	}

	public Set<String> getChannels() {
		return state.getChannels();
	}

	public Set<String> getMembers(final String channel) {
		return state.getMembers(channel);
	}

	public void stop() throws Exception {
		for (final String channelName : channels.keySet()) {
			disconnectFromChannel(channelName);
		}
	}
}
