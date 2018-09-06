package com.samule.example.elastic.service;

import com.alibaba.fastjson.JSONReader;
import com.samule.example.elastic.pojo.MyDocument;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.samule.example.elastic.constant.ElasticConstants.*;

@Service
public class ElasticService {

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	@Value("${mappings.path}")
	private String mappingsPath;

	@Value("${settings.path}")
	private String settingsPath;

	@Value("${index.name}")
	private String indexName;

	@Value("${type.name}")
	private String typeName;

	@PostConstruct
	public void createIndex() throws IOException {
		if (!elasticsearchTemplate.indexExists(indexName)
				&& !elasticsearchTemplate.createIndex(indexName, readJsonFile(settingsPath))) {
			throw new RuntimeException("Failed to build Addr_index in elasticsearch");
		}
		if (!elasticsearchTemplate.putMapping(indexName, typeName, readJsonFile(mappingsPath))) {
			throw new RuntimeException("Failed to build Addr_index in elasticsearch");
		}
	}

	public void index(MyDocument document) {
		elasticsearchTemplate.index(getIndexQuery(document));
	}

	public List search(MyDocument document) {
		return elasticsearchTemplate.queryForList(getSearchQuery(document), MyDocument.class);
	}

	private IndexQuery getIndexQuery(MyDocument document) {
		IndexQuery indexQuery = new IndexQuery();
		indexQuery.setIndexName(indexName);
		indexQuery.setType(typeName);
		indexQuery.setObject(document);
		return indexQuery;
	}

	private SearchQuery getSearchQuery(MyDocument document) {
		QueryBuilder queryBuilder = QueryBuilders.boolQuery()
//				.must(QueryBuilders.termQuery(INDEX_FIELD_ID, document.getId()))
//				.must(QueryBuilders.termQuery(INDEX_FIELD_PROVINCE, document.getProvince()))
//				.must(QueryBuilders.termQuery(INDEX_FIELD_CITY, document.getCity()))
//				.must(QueryBuilders.termQuery(INDEX_FIELD_COUNTY, document.getCounty()))
//				.must(QueryBuilders.termQuery(INDEX_FIELD_TYPE, document.getType()))
				.should(QueryBuilders.constantScoreQuery(QueryBuilders.matchQuery(INDEX_FIELD_NAME, document.getName()).operator(Operator.AND)))
				.should(QueryBuilders.constantScoreQuery(QueryBuilders.matchQuery(INDEX_FIELD_SUBNAME, document.getSubname()).operator(Operator.AND)));
//				.must(QueryBuilders.termQuery(INDEX_FIELD_XCOORD, document.getXcoord()))
//				.must(QueryBuilders.termQuery(INDEX_FIELD_YCOORD, document.getYcoord()));

		return new NativeSearchQueryBuilder()
				.withIndices(indexName)
				.withTypes(typeName)
				.withQuery(queryBuilder)
//				.withPageable(PageRequest.of(0, 100))
//				.withSort(SortBuilders.scoreSort())
				.withSourceFilter(new FetchSourceFilter(new String[]{"userInfo"}, null))
				.build();
	}

	private String readJsonFile(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		try (BufferedReader reader = Files.newBufferedReader(path);
		     JSONReader jsonReader = new JSONReader(reader);) {
			return jsonReader.readString();
		}
	}
}
