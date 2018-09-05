package com.samule.example.elastic.config;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class ElasticConfig {

	@Value("${elastic.local.host}")
	private String host;

	@Value("${elastic.local.port}")
	private int port;

	@Value("${spring.data.elasticsearch.cluster-name}")
	private String clusterName;

	@Bean
	public ElasticsearchTemplate elasticsearchTemplate() throws UnknownHostException {
		Settings settings = Settings.builder()
				.put("xpack.security.user", "elastic:changeme")
				.put("cluster.name", clusterName)
				.build();
		TransportClient transportClient = new PreBuiltXPackTransportClient(settings);
		transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
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
