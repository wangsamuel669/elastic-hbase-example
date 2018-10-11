package com.samule.example.elastic.service;

import com.alibaba.fastjson.JSONReader;
import com.samule.example.elastic.pojo.MyDocument;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
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

import static com.samule.example.elastic.constant.ElasticConstants.INDEX_FIELD_NAME;
import static com.samule.example.elastic.constant.ElasticConstants.INDEX_FIELD_SIZE;

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

	/*private SearchQuery getSearchQuery(MyDocument document) {
		QueryBuilder queryBuilder = QueryBuilders.boolQuery()
//				.must(QueryBuilders.termQuery(INDEX_FIELD_ID, document.getId()))
//				.must(QueryBuilders.termQuery(INDEX_FIELD_PROVINCE, document.getProvince()))
//				.must(QueryBuilders.termQuery(INDEX_FIELD_CITY, document.getCity()))
//				.must(QueryBuilders.termQuery(INDEX_FIELD_COUNTY, document.getCounty()))
//				.must(QueryBuilders.termQuery(INDEX_FIELD_TYPE, document.getType()))
				.must(QueryBuilders.constantScoreQuery(
						QueryBuilders.matchQuery(INDEX_FIELD_NAME, document.getName())
								.operator(Operator.OR).minimumShouldMatch(document.getName().length() - 1 + "")));
//				.should(QueryBuilders.constantScoreQuery(QueryBuilders.matchQuery(INDEX_FIELD_SUBNAME, document.getSubname()).operator(Operator.AND)));
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
	}*/

	private SearchQuery getSearchQuery(MyDocument document) {
		MyDocument.Detail detail = document.getDetail().get(0);
		QueryBuilder queryBuilder = null;
		int size = Integer.parseInt(detail.getSize());
		if (size == 1) {
			queryBuilder = getSameQuery(detail);
		} else if (size == 2) {
			QueryBuilder bool = QueryBuilders.boolQuery()
					.should(QueryBuilders.boolQuery()
							.filter(QueryBuilders.boolQuery()
									.should(QueryBuilders.termQuery(INDEX_FIELD_SIZE, size + 1 + ""))
									.should(QueryBuilders.termQuery(INDEX_FIELD_SIZE, detail.getSize())))
							.must(QueryBuilders.constantScoreQuery(QueryBuilders.matchQuery(INDEX_FIELD_NAME, detail.getName()).operator(Operator.AND))))
					.minimumShouldMatch(1);
			queryBuilder = QueryBuilders.nestedQuery("detail", bool, ScoreMode.Max);
		} else {
			queryBuilder = getOneQuery(detail);
		}

		return new NativeSearchQueryBuilder()
				.withIndices(indexName)
				.withTypes(typeName)
				.withQuery(queryBuilder)
				.build();
	}

	private QueryBuilder getSameQuery(MyDocument.Detail detail) {
		return QueryBuilders.nestedQuery("detail", QueryBuilders.boolQuery()
				.filter(QueryBuilders.termQuery(INDEX_FIELD_SIZE, detail.getSize()))
				.must(QueryBuilders.constantScoreQuery(QueryBuilders.matchQuery(INDEX_FIELD_NAME, detail.getName()).operator(Operator.AND))), ScoreMode.Max);
	}

	private QueryBuilder getOneQuery(MyDocument.Detail detail) {
		int size = Integer.parseInt(detail.getSize());

		QueryBuilder longQuery = QueryBuilders.boolQuery()
				.filter(QueryBuilders.boolQuery()
						.should(QueryBuilders.termQuery(INDEX_FIELD_SIZE, size + 2 + ""))
						.should(QueryBuilders.termQuery(INDEX_FIELD_SIZE, size + 1 + ""))
						.should(QueryBuilders.termQuery(INDEX_FIELD_SIZE, detail.getSize())))
				.must(QueryBuilders.constantScoreQuery(QueryBuilders.matchQuery(INDEX_FIELD_NAME, detail.getName()).operator(Operator.AND)));
		QueryBuilder shortQuery = QueryBuilders.boolQuery()
				.filter(QueryBuilders.boolQuery()
						.should(QueryBuilders.termQuery(INDEX_FIELD_SIZE, Integer.parseInt(detail.getSize()) - 1 + ""))
						.should(QueryBuilders.termQuery(INDEX_FIELD_SIZE, Integer.parseInt(detail.getSize()) - 2 + "")))
				.must(QueryBuilders.constantScoreQuery(QueryBuilders.matchQuery(INDEX_FIELD_NAME, detail.getName()).operator(Operator.OR).minimumShouldMatch((size - 2 + ""))));
		return QueryBuilders.nestedQuery("detail", QueryBuilders.boolQuery().should(longQuery).should(shortQuery).minimumShouldMatch(1), ScoreMode.Max);
	}

	private String readJsonFile(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		try (BufferedReader reader = Files.newBufferedReader(path);
		     JSONReader jsonReader = new JSONReader(reader);) {
			return jsonReader.readString();
		}
	}
}
