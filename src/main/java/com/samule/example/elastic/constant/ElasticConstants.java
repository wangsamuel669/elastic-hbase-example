package com.samule.example.elastic.constant;

import java.time.format.DateTimeFormatter;

public class ElasticConstants {

	//index_filed_name
	public static final String INDEX_FIELD_ID = "id";
	public static final String INDEX_FIELD_PROVINCE = "province";
	public static final String INDEX_FIELD_CITY = "city";
	public static final String INDEX_FIELD_COUNTY = "county";
	public static final String INDEX_FIELD_SUBNAME = "subname";
	public static final String INDEX_FIELD_TYPE = "type";
	public static final String INDEX_FIELD_NAME = "name";
	public static final String INDEX_FIELD_XCOORD = "xcoord";
	public static final String INDEX_FIELD_YCOORD = "ycoord";
	public static final String INDEX_FIELD_TIME = "insertTime";
	public static final String INDEX_FIELD_SIZE = "size";

	public static final DateTimeFormatter STANDARD_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public static final DateTimeFormatter STANDARD_ZONE_FORMATTER =
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSZZ");
}
