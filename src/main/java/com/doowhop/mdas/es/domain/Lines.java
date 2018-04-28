package com.doowhop.mdas.es.domain;

import java.util.Set;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class Lines {

	@Field(type = FieldType.String)
	String mchtNo;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	Set<String> lineSet;
		
	public Lines() {
	}
	
	public Lines(String mchtNo, Set<String> lineSet) {
		this.mchtNo = mchtNo;
		this.lineSet = lineSet;
	}

	public String getMchtNo() {
		return mchtNo;
	}

	public void setMchtNo(String mchtNo) {
		this.mchtNo = mchtNo;
	}

	public Set<String> getLineSet() {
		return lineSet;
	}

	public void setLineSet(Set<String> lineSet) {
		this.lineSet = lineSet;
	}

}
