package com.samule.example.elastic.service;

import com.samule.example.elastic.pojo.MyDocument;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.StreamUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.function.Consumer;

import static com.samule.example.elastic.constant.ElasticConstants.STANDARD_FORMATTER;

@Service
public class MyService {
	@Autowired
	private ElasticService elasticService;

	@Value("${source.file.path}")
	private String sourceFilePath;

	public void load() throws IOException {
		Path path = Files.createFile(Paths.get(sourceFilePath));
		Consumer<CSVRecord> consumer = this::processCsvRecord;
		readFile(path, consumer);
	}

	private void readFile(Path path, Consumer<CSVRecord> consumer) throws IOException {
		CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withSkipHeaderRecord();
		BufferedReader reader = Files.newBufferedReader(path);
		CSVParser csvParser = csvFormat.parse(reader);
		Iterator<CSVRecord> csvRecordIterator = csvParser.iterator();
		StreamUtils.createStreamFromIterator(csvRecordIterator).forEach(consumer);
	}

	private void processCsvRecord(CSVRecord csvRecord) {
		MyDocument document = getMyDocumentFromCsv(csvRecord);
		elasticService.index(document);
	}

	private MyDocument getMyDocumentFromCsv(CSVRecord csvRecord) {
		int i = 0;
		MyDocument.MyDocumentBuilder builder = MyDocument.MyDocumentBuilder.builder()
				.withId(Long.parseLong(csvRecord.get(i++)))
				.withProvince(csvRecord.get(i++))
				.withCity(csvRecord.get(i++))
				.withCounty(csvRecord.get(i++))
				.withSubname(csvRecord.get(i++))
				.withType(csvRecord.get(i++))
				.withName(csvRecord.get(i++))
				.withXcoord(csvRecord.get(i++))
				.withYcoord(csvRecord.get(i++));
		i++;
		return builder.withInsertTime(LocalDateTime.parse(csvRecord.get(i), STANDARD_FORMATTER))
				.build();
	}
}
