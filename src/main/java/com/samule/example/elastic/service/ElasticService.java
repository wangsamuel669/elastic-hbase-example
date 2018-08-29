package com.samule.example.elastic.service;

import com.samule.example.elastic.pojo.MyDocument;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import static com.samule.example.elastic.constant.ElasticConstants.*;

@Service
public class ElasticService {

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	public void index(MyDocument document) {
		elasticsearchTemplate.index(getIndexQuery(document));
	}

	public void search(MyDocument document) {
		elasticsearchTemplate.queryForList(getSearchQuery(document), MyDocument.class);
	}

	private IndexQuery getIndexQuery(MyDocument document) {
		IndexQuery indexQuery = new IndexQuery();
		indexQuery.setIndexName(INDEX_NAME);
		indexQuery.setType(TYPE_NAME);
		indexQuery.setObject(document);
		return indexQuery;
	}

	private SearchQuery getSearchQuery(MyDocument document) {
		QueryBuilder queryBuilder = QueryBuilders.boolQuery()
				.filter(QueryBuilders.termQuery(INDEX_FIELD_ID, document.getName()))
				.filter(QueryBuilders.termQuery(INDEX_FIELD_PROVINCE, document.getProvince()))
				.filter(QueryBuilders.termQuery(INDEX_FIELD_CITY, document.getCity()))
				.filter(QueryBuilders.termQuery(INDEX_FIELD_COUNTY, document.getCounty()))
				.filter(QueryBuilders.termQuery(INDEX_FIELD_SUBNAME, document.getSubname()))
				.filter(QueryBuilders.termQuery(INDEX_FIELD_TYPE, document.getType()))
				.filter(QueryBuilders.termQuery(INDEX_FIELD_NAME, document.getName()))
				.filter(QueryBuilders.termQuery(INDEX_FIELD_XCOORD, document.getXcoord()))
				.filter(QueryBuilders.termQuery(INDEX_FIELD_YCOORD, document.getYcoord()));

		return new NativeSearchQueryBuilder()
				.withIndices(INDEX_NAME)
				.withTypes(TYPE_NAME)
				.withQuery(queryBuilder)
				.build();
	}

}
