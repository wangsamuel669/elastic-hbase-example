package com.samule.example.elastic.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationProperty {
	@Value("${mappings.path}")
	public String mappingsPath;

	@Value("${settings.path}")
	public String settingsPath;

	@Value("${index.name}")
	public String indexName;

	@Value("${type.name}")
	public String typeName;

}
