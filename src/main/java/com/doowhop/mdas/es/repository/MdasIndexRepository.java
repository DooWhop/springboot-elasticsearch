package com.doowhop.mdas.es.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.doowhop.mdas.es.domain.MdasIndex;

public interface MdasIndexRepository extends ElasticsearchRepository<MdasIndex, String>{
		
	public MdasIndex findByFileName(String fileName);
	
	//public MdasIndex findByTransDateBetween(Date begin, Date end); 
	
	//public List<MdasIndex> findByLineNo_MchtNo(String mchtNo);
}
