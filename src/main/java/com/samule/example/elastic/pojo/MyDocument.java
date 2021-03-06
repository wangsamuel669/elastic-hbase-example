package com.samule.example.elastic.pojo;

import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Document(indexName = "#{applicationProperty.indexName}", type = "#{applicationProperty.typeName}", createIndex = false)
public class MyDocument {
	private long id;
	private String province;
	private String city;
	private String county;
	private String subname;
	private String type;
//	private List<Detail> details;
	private String name;
	private String xcoord;
	private String ycoord;

	private String userInfo;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/*public List<Detail> getDetails() {
		return details;
	}

	public void setDetails(List<Detail> details) {
		this.details = details;
	}*/

	public String getXcoord() {
		return xcoord;
	}

	public void setXcoord(String xcoord) {
		this.xcoord = xcoord;
	}

	public String getYcoord() {
		return ycoord;
	}

	public void setYcoord(String ycoord) {
		this.ycoord = ycoord;
	}

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public String getSubname() {
		return subname;
	}

	public void setSubname(String subname) {
		this.subname = subname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*public static class Detail {
		private String name;
		private String subname;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSubname() {
			return subname;
		}

		public void setSubname(String subname) {
			this.subname = subname;
		}
	}*/
}
