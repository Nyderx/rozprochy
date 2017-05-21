package lab5.kamcio.server;

import static akka.actor.SupervisorStrategy.restart;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.japi.pf.DeciderBuilder;
import akka.routing.RoundRobinPool;
import lab5.kamcio.messages.FileStreamRequest;
import scala.concurrent.duration.Duration;

public class FilesStreamingActor extends AbstractActor {
	private final static int FILE_STREAMING_ACTORS = 2;
	private ActorRef filesStreamingRouter;

	@Override
	public void preStart() throws Exception {
		super.preStart();

		filesStreamingRouter = getContext().actorOf(
				new RoundRobinPool(FILE_STREAMING_ACTORS).props(Props.create(FileStreamingWorker.class)),
				"filesStream");
	}

	@Override
	public void postStop() throws Exception {
		context().stop(filesStreamingRouter);

		super.postStop();
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(FileStreamRequest.class, this::handleFileStreamRequest).build();
	}

	private void handleFileStreamRequest(final FileStreamRequest request) {
		filesStreamingRouter.tell(request, getSender());
	}

	@Override
	public SupervisorStrategy supervisorStrategy() {
		return new OneForOneStrategy(10, Duration.create("1 minute"), DeciderBuilder.matchAny(o -> restart()).build());
	}
}
