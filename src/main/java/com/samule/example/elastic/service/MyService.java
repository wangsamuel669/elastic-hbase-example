package com.samule.example.elastic.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.samule.example.elastic.pojo.MyDocument;
import com.samule.example.elastic.pojo.UserInfo;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;

@Service
public class MyService {

	@Autowired
	private ThreadPoolExecutor executor;

	@Autowired
	private ElasticService elasticService;

	@Value("${source.file.path}")
	private String sourceFilePath;

	public void load() throws IOException {
		Path path = Paths.get(sourceFilePath);
		Consumer<CSVRecord> consumer = record -> executor.execute(() -> processCsvRecord(record));
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
		MyDocument document = new MyDocument();
		document.setId(csvRecord.get("id"));
		document.setProvince(csvRecord.get("province"));
		document.setCity(csvRecord.get("city"));
		document.setCounty(csvRecord.get("county"));
		document.setType(csvRecord.get("type"));
		document.setXcoord(csvRecord.get("xcoord"));
		document.setYcoord(csvRecord.get("ycoord"));
		document.setUserInfo(JSON.toJSONString(getUserInfo()));
		document.setName(csvRecord.get("name"));
		document.setSubname(csvRecord.get("subname"));
		document.setSubsize(csvRecord.get("subname").length() + "");
		document.setSize(csvRecord.get("name").length() + "");
		return document;
	}

	public List search(MyDocument document) {
		document.setSize(document.getName().length() + "");
		document.setSubsize(document.getSubname().length() + "");
		return elasticService.search(document);
	}

	private UserInfo getUserInfo() {
		UserInfo userInfo = new UserInfo();
		userInfo.setAddress("广东省江门市新会区会城潮兴路63号玉圭园68座402");
		userInfo.setAge(100);
		userInfo.setName("广东省江门市新会区会城潮兴路63号玉圭园68座402");
		userInfo.setOthers(new ArrayList<>());
		for (int i = 0; i < 3; i++) {
			UserInfo.Other other = userInfo.new Other();
			other.setAddress("广东省江门市新会区会城潮兴路63号玉圭园68座402");
			other.setPrice(1000.10);
			other.setType("住宅");
			userInfo.getOthers().add(other);
		}
		return userInfo;
	}

	/*private List<MyDocument.Detail> getDetails(CSVRecord csvRecord) {
		List<MyDocument.Detail> list = new ArrayList<>();
		String name = csvRecord.get("name");
		String subname = csvRecord.get("subname");
		for (int i = 0; i < 5; i++) {
			MyDocument.Detail detail = new MyDocument.Detail();
			if (i > 0) {
				detail.setName(name + i);
				detail.setSubname(subname + i);
			} else {
				detail.setName(name);
				detail.setSubname(subname);
			}
			list.add(detail);
		}
		return list;
	}*/

	public static void main(String[] args) {
		System.out.println(JSON.toJSONString(new MyDocument(), SerializerFeature.WriteNullStringAsEmpty));
	}
}
