package lab4.server;

import java.util.LinkedList;
import java.util.List;

import lab4.gen.ResultRow;
import lab4.gen.ResultRowsGroup;
import lab4.gen.ResultsRecord;

public class SampleData {
	public static List<ResultsRecord> RECORDS = new LinkedList<>();

	// @formatter:off
	static {
		RECORDS.add(ResultsRecord.newBuilder()
				.setDoctor("Dr Koperta")
				.setPatient("Krzysztof")
				.setDate("15.02.2017")
				.addResultRowsGroups(ResultRowsGroup.newBuilder()
						.setName("Biochemia")
						.addResultRows(ResultRow.newBuilder()
								.setName("Cholesterol")
								.setValue(150)
								.setUnit("mg/dl")
								.build())
						.build())
				.build());
		
		RECORDS.add(ResultsRecord.newBuilder()
				.setDoctor("Dr Koperta")
				.setPatient("Krzysztof")
				.setDate("16.03.2017")
				.addResultRowsGroups(ResultRowsGroup.newBuilder()
						.setName("Biochemia")
						.addResultRows(ResultRow.newBuilder()
								.setName("Cholesterol")
								.setValue(250)
								.setUnit("mg/dl")
								.build())
						.addResultRows(ResultRow.newBuilder()
								.setName("Zelazo")
								.setValue(10)
								.setUnit("ug/dl")
								.build())
						.build())
				.build());
		
		RECORDS.add(ResultsRecord.newBuilder()
				.setDoctor("Dr Ignacy")
				.setPatient("Marek")
				.setDate("28.02.2017")
				.addResultRowsGroups(ResultRowsGroup.newBuilder()
						.setName("Biochemia")
						.addResultRows(ResultRow.newBuilder()
								.setName("Cholesterol")
								.setValue(100)
								.setUnit("mg/dl")
								.build())
						.addResultRows(ResultRow.newBuilder()
								.setName("Żelazo")
								.setValue(50)
								.setUnit("ug/dl")
								.build())
						.build())
				.build());
	}
	// @formatter:on
}
