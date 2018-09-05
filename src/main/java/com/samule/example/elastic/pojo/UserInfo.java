package com.samule.example.elastic.pojo;

import java.util.List;

public class UserInfo {
	private String name;
	private int age;
	private String address;
	private List<Other> others;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<Other> getOthers() {
		return others;
	}

	public void setOthers(List<Other> others) {
		this.others = others;
	}

	public class Other {
		private String address;
		private String type;
		private double price;

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}
	}
}
