package com.infostudio.ba.service.helper.model;

public class ApConstants {
	private Long id;

    private String key;

    private String value;

    public ApConstants() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ApConstants{" +
				"id=" + id +
				", key='" + key + '\'' +
				", value='" + value + '\'' +
				'}';
	}
}
