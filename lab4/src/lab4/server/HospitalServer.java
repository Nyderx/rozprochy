package lab4.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import lab4.gen.BooleanResponse;
import lab4.gen.DoctorQuery;
import lab4.gen.DoctorServiceGrpc.DoctorServiceImplBase;
import lab4.gen.LabServiceGrpc.LabServiceImplBase;
import lab4.gen.PatientQuery;
import lab4.gen.PatientServiceGrpc.PatientServiceImplBase;
import lab4.gen.ResultRow;
import lab4.gen.ResultsRecord;
import lab4.gen.RowQuery;

public class HospitalServer {
	private static final Logger logger = Logger.getLogger(HospitalServer.class.getName());

	private final int port = 10000;
	private final Server server;

	private final Map<String, List<ResultsRecord>> results = new HashMap<>();

	public HospitalServer() {
		server = ServerBuilder.forPort(port).addService(new DoctorService()).addService(new LabService())
				.addService(new PatientService()).build();
	}

	public void start() throws IOException {
		SampleData.RECORDS.forEach(this::add);

		server.start();
		logger.info("Server started");
		while (true) {
			;
		}
	}

	public void add(final ResultsRecord result) {
		if (!results.containsKey(result.getPatient())) {
			results.put(result.getPatient(), new LinkedList<>());
		}
		results.get(result.getPatient()).add(result);
	}

	public void stop() {
		server.shutdown();
	}

	private class DoctorService extends DoctorServiceImplBase {

		@Override
		public void getAll(final DoctorQuery request, final StreamObserver<ResultsRecord> responseObserver) {
			if (results.containsKey(request.getPatient())) {
				results.get(request.getPatient()).stream().filter(result -> {
					final List<ResultRow> allRows = result.getResultRowsGroupsList().stream()
							.flatMap(group -> group.getResultRowsList().stream()).collect(Collectors.toList());
					if (checkIfAllRowQueriesAreMet(request.getRowsRequirementsList(), allRows)) {
						return true;
					}
					return false;
				}).forEach(result -> {
					try {
						Thread.sleep(5000);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
					responseObserver.onNext(result);
				});
			}
			responseObserver.onCompleted();
		}

		private boolean checkIfAllRowQueriesAreMet(final List<RowQuery> queries, final List<ResultRow> allRows) {
			for (final RowQuery query : queries) {
				if (!checkIfRowQueryIsMet(query, allRows)) {
					return false;
				}
			}
			return true;
		}

		private boolean checkIfRowQueryIsMet(final RowQuery query, final List<ResultRow> allRows) {
			if (allRows.stream().filter(row -> row.getName().equals(query.getName()) && query.getMin() < row.getValue()
					&& row.getValue() < query.getMax()).count() > 0) {
				return true;
			}
			return false;
		}
	}

	private class PatientService extends PatientServiceImplBase {

		@Override
		public void getAll(final PatientQuery request, final StreamObserver<ResultsRecord> responseObserver) {
			if (results.containsKey(request.getPatient())) {
				results.get(request.getPatient()).forEach(result -> {
					try {
						Thread.sleep(5000);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
					responseObserver.onNext(result);
				});
			}
			responseObserver.onCompleted();
		}
	}

	private class LabService extends LabServiceImplBase {

		@Override
		public void addResult(final ResultsRecord request, final StreamObserver<BooleanResponse> responseObserver) {
			add(request);
			System.out.println("Added: " + request);
			responseObserver.onNext(BooleanResponse.newBuilder().setSuccess(true).build());
			responseObserver.onCompleted();
		}

	}

	public static void main(final String[] args) throws IOException {
		new HospitalServer().start();
	}
}
