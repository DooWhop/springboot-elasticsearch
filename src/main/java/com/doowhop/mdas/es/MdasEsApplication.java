package com.doowhop.mdas.es;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = {
		"file:/app/thfd/conf/mdas-es.properties"
		}, ignoreResourceNotFound = true)
public class MdasEsApplication {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public static void main(String[] args) {
		SpringApplication.run(MdasEsApplication.class, args);
	}
}
