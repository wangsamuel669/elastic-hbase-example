package com.samule.example.elastic.config;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class ElasticConfig {

	@Bean
	public ElasticsearchTemplate elasticsearchTemplate() throws UnknownHostException {
		TransportClient transportClient = new PreBuiltTransportClient(Settings.EMPTY);
		transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
		return new ElasticsearchTemplate(transportClient, new MyEntityMapper());
	}

	private class MyEntityMapper implements EntityMapper {
		@Override
		public String mapToString(Object o) throws IOException {
			return JSON.toJSONString(o);
		}

		@Override
		public <T> T mapToObject(String s, Class<T> aClass) throws IOException {
			return JSON.parseObject(s, aClass);
		}
	}

}
