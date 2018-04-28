package com.doowhop.mdas.es.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(indexName = "#{esConstant.index}", type="#{esConstant.type}", shards=1, replicas=0)
public class MdasIndex {
	
	@Id
	@Field(type = FieldType.String)
	String fileName;
	
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd",timezone="GMT+8")
	String transDate;
    
    @Field(type = FieldType.Nested)
    List<Lines> lines;
		
	public MdasIndex(){		
	}
	
	public MdasIndex(String fileName, String transDate, List<Lines> lines){
		this.fileName = fileName;
		this.transDate = transDate;
		this.lines = lines;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public List<Lines> getLines() {
		return lines;
	}

	public void setLines(List<Lines> lines) {
		this.lines = lines;
	}
}
