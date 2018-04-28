package com.doowhop.mdas.es.enums;

public enum MdasIndexEnum {
	
	LOGINFO ("0","loginfo"),

	RXINFO("1","rxinfo");	
	
    
	private String type;
	private String index;
	
	MdasIndexEnum(String type,String index) {
		this.type = type;
		this.index = index;
	}
	
	
	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getIndex() {
		return index;
	}


	public void setIndex(String index) {
		this.index = index;
	}
	
	
	public static MdasIndexEnum geMdasIndex(String type) {
		if (type != null) {
			for (MdasIndexEnum mdasIndexEnum : values()) {
				if (mdasIndexEnum.getType().equals(type)) {
					return mdasIndexEnum;
				}
			}
		}
		return null;
	}

}
