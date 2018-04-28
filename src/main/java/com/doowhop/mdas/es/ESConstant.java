package com.doowhop.mdas.es;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("esConstant")
public class ESConstant {

    @Value("${es.index}")
    private String index;
    
    @Value("${es.type}")
    private String type;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}