package com.samule.example.elastic.pojo;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.time.LocalDateTime;

import static com.samule.example.elastic.constant.ElasticConstants.INDEX_NAME;
import static com.samule.example.elastic.constant.ElasticConstants.TYPE_NAME;

@Document(indexName = INDEX_NAME, type = TYPE_NAME)
@Mapping(mappingPath = "mappings.json")
@Setting(settingPath = "settings.json")
public class MyDocument {
	private long id;
	private String province;
	private String city;
	private String county;
	private String subname;
	private String type;
	private String name;
	private String xcoord;
	private String ycoord;
	private LocalDateTime insertTime;

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

	public String getSubname() {
		return subname;
	}

	public void setSubname(String subname) {
		this.subname = subname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public LocalDateTime getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(LocalDateTime insertTime) {
		this.insertTime = insertTime;
	}


	public static final class MyDocumentBuilder {
		private MyDocument myDocument;

		private MyDocumentBuilder() {
			myDocument = new MyDocument();
		}

		public static MyDocumentBuilder builder() {
			return new MyDocumentBuilder();
		}

		public MyDocumentBuilder withId(long id) {
			myDocument.setId(id);
			return this;
		}

		public MyDocumentBuilder withProvince(String province) {
			myDocument.setProvince(province);
			return this;
		}

		public MyDocumentBuilder withCity(String city) {
			myDocument.setCity(city);
			return this;
		}

		public MyDocumentBuilder withCounty(String county) {
			myDocument.setCounty(county);
			return this;
		}

		public MyDocumentBuilder withSubname(String subname) {
			myDocument.setSubname(subname);
			return this;
		}

		public MyDocumentBuilder withType(String type) {
			myDocument.setType(type);
			return this;
		}

		public MyDocumentBuilder withName(String name) {
			myDocument.setName(name);
			return this;
		}

		public MyDocumentBuilder withXcoord(String xcoord) {
			myDocument.setXcoord(xcoord);
			return this;
		}

		public MyDocumentBuilder withYcoord(String ycoord) {
			myDocument.setYcoord(ycoord);
			return this;
		}

		public MyDocumentBuilder withInsertTime(LocalDateTime insertTime) {
			myDocument.setInsertTime(insertTime);
			return this;
		}

		public MyDocument build() {
			return myDocument;
		}
	}
}
