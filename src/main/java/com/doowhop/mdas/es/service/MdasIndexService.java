package com.doowhop.mdas.es.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.doowhop.mdas.es.domain.MdasIndex;

public interface MdasIndexService {
	
	/**
	 * @param fileName
	 * @return
	 */
	boolean isExist(String fileName);
	
	
	/**
	 * @param fileName
	 * @return
	 */
	MdasIndex findById(String fileName);
	   
    /**
     * @param fileType
     * @param mdasList
     * @return
     */
    Iterable<MdasIndex> save(String fileType, List<MdasIndex> mdasList);
    
    /**
     * @param fileType
     * @param mdasIndex
     * @return
     */
    MdasIndex save(String fileType, MdasIndex mdasIndex);
      
    /**
     * @param fileType
     * @param mchtNo
     * @return
     */
    Map<String, Set<String>> findByMchtNo(String fileType, String mchtNo);
    
    /**
     * @param fileType
     * @param mchtNo
     * @param from  yyyy-MM-dd
     * @param to yyyy-MM-dd
     * @return
     */
    Map<String, Set<String>> findByMchtNoAndRangeDate(String fileType, String mchtNo, String from, String to); 
		  
}
