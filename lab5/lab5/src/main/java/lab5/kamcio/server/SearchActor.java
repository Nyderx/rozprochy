package lab5.kamcio.server;

import static akka.actor.SupervisorStrategy.restart;

import java.util.HashMap;
import java.util.Map;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.japi.pf.DeciderBuilder;
import akka.routing.RoundRobinPool;
import lab5.kamcio.messages.SearchRequest;
import lab5.kamcio.messages.server.InternalSearchAnswer;
import lab5.kamcio.messages.server.InternalSearchRequest;
import scala.concurrent.duration.Duration;

public class SearchActor extends AbstractActor {
	private static final int DATABASE_WORKERS_NUMBER = 2;
	private static final String DATABASE1_FILENAME = "database1.txt";
	private static final String DATABASE2_FILENAME = "database2.txt";

	private long currentId = 1;
	private ActorRef database1Router;
	private ActorRef database2Router;

	private final Map<Long, StateRecord> state = new HashMap<>();

	@Override
	public void preStart() throws Exception {
		super.preStart();

		database1Router = getContext().actorOf(new RoundRobinPool(DATABASE_WORKERS_NUMBER).props(
				Props.create(DatabaseReaderWorker.class, DATABASE1_FILENAME)), "database1Router");
		database2Router = getContext().actorOf(new RoundRobinPool(DATABASE_WORKERS_NUMBER).props(
				Props.create(DatabaseReaderWorker.class, DATABASE2_FILENAME)), "database2Router");
	}

	@Override
	public void postStop() throws Exception {
		context().stop(database1Router);
		context().stop(database2Router);

		super.postStop();
	}

	@Override
	public SupervisorStrategy supervisorStrategy() {
		return new OneForOneStrategy(10, Duration.create("1 minute"), DeciderBuilder.matchAny(o -> restart()).build());
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(SearchRequest.class, this::handleSearchRequest)
							   .match(InternalSearchAnswer.class, this::handleInternalSearchAnswer)
							   .build();
	}

	private void handleSearchRequest(final SearchRequest request) {
		final InternalSearchRequest internalRequest = new InternalSearchRequest(currentId, request);
		state.put(currentId, new StateRecord(getSender()));
		currentId++;

		database1Router.tell(internalRequest, getSelf());
		database2Router.tell(internalRequest, getSelf());
	}

	private void handleInternalSearchAnswer(final InternalSearchAnswer answer) {
		final long id = answer.id();
		if (state.containsKey(id)) {
			if (answer.answer().bookRecord() == null) {
				if (state.get(id).processingState().equals(ProcessingState.ONE_DATABASE)) {
					state.remove(id);
				} else {
					state.get(id).oneDatabaseProceeded();
				}
			} else {
				state.get(id).sender.tell(answer.answer(), getSelf());
				state.remove(id);
			}
		}
	}

	private static class StateRecord {
		private final ActorRef sender;
		private ProcessingState processingState = ProcessingState.START;

		public StateRecord(final ActorRef sender) {
			super();
			this.sender = sender;
		}

		public void oneDatabaseProceeded() {
			processingState = ProcessingState.ONE_DATABASE;
		}

		public ProcessingState processingState() {
			return processingState;
		}
	}

	private static enum ProcessingState {
		START, ONE_DATABASE
	}
}
