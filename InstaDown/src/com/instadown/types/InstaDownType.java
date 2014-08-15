package com.instadown.types;

public enum InstaDownType {
	IMAGE(new Integer(1),"Image"),
	VIDEO(new Integer(0),"Video");
	
	private final Integer code;
	private final String value;
	
	InstaDownType(Integer code, String value){
		this.code=code;
		this.value = value;
	}

	public Integer getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}
}
