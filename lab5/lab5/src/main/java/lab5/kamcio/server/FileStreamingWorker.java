package lab5.kamcio.server;

import java.util.concurrent.TimeUnit;

import akka.actor.AbstractActor;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.ThrottleMode;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import lab5.kamcio.messages.FileStreamRequest;
import lab5.kamcio.messages.StreamControlMessages;
import lab5.kamcio.utils.IterableFile;
import scala.concurrent.duration.FiniteDuration;

public class FileStreamingWorker extends AbstractActor {

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(FileStreamRequest.class, this::handleFileStreamRequest).build();
	}

	private void handleFileStreamRequest(final FileStreamRequest request) {
		final Materializer materializer = ActorMaterializer.create(context());
		Source.from(new IterableFile(request.title() + ".txt"))
			  .throttle(1, FiniteDuration.create(5, TimeUnit.SECONDS), 1, ThrottleMode.shaping())
			  .to(Sink.actorRefWithAck(getSender(), StreamControlMessages.INIT, StreamControlMessages.ACK,
					  StreamControlMessages.COMPLETED, StreamControlMessages.FAILURE))
			  .run(materializer);
	}
}
