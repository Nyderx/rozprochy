package pl.edu.agh.dsrg.sr.chat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos.ChatAction;
import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos.ChatAction.ActionType;
import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos.ChatState;
import pl.edu.agh.dsrg.sr.chat.protos.ChatOperationProtos.ChatState.Builder;

public class State {
	private final Map<String, Set<String>> membersMap = new HashMap<>();

	public synchronized void addMember(final String channel, final String nickname) {
		if (!membersMap.containsKey(channel)) {
			membersMap.put(channel, new HashSet<>());
		}
		membersMap.get(channel).add(nickname);
		System.out.println(nickname + " joined " + channel);
	}

	public synchronized void removeMember(final String channel, final String nickname) {
		membersMap.get(channel).remove(nickname);
		if (membersMap.get(channel).isEmpty()) {
			membersMap.remove(channel);
		}
		System.out.println(nickname + " left " + channel);
	}

	public synchronized void retainMembers(final Set<String> retainedMembers) {
		membersMap.keySet().stream().map(membersMap::get).forEach(members -> members.retainAll(retainedMembers));
	}

	public synchronized Set<String> getChannels() {
		return new HashSet<>(membersMap.keySet());
	}

	public synchronized Set<String> getMembers(final String channel) {
		return new HashSet<>(membersMap.get(channel));
	}

	public synchronized ChatState toChatStateMessage() {
		final Builder builder = ChatState.newBuilder();
		for (final String channel : membersMap.keySet()) {
			for (final String nickname : membersMap.get(channel)) {
				builder.addState(
						ChatAction.newBuilder().setChannel(channel).setNickname(nickname).setAction(ActionType.JOIN));
			}
		}
		return builder.build();
	}

	public synchronized void fromChatStateMessage(final ChatState chatState) {
		membersMap.clear();
		chatState.getStateList().forEach(chatAction -> {
			addMember(chatAction.getChannel(), chatAction.getNickname());
		});
	}
}
