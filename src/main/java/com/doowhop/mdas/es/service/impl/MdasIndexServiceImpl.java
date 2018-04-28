package com.doowhop.mdas.es.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.doowhop.mdas.es.ESConstant;
import com.doowhop.mdas.es.domain.Lines;
import com.doowhop.mdas.es.domain.MdasIndex;
import com.doowhop.mdas.es.enums.MdasIndexEnum;
import com.doowhop.mdas.es.repository.MdasIndexRepository;
import com.doowhop.mdas.es.service.MdasIndexService;

@Service
public class MdasIndexServiceImpl implements MdasIndexService{
	
	@Autowired 
	private MdasIndexRepository mdasRepository;  
	
    @Autowired  
    private ElasticsearchTemplate esTemplate; 
    
    @Autowired  
    private ESConstant esConstant;
    	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
	@Override
	public boolean isExist(String fileName) {
		boolean ret = mdasRepository.exists(fileName);
		logger.info("the index of {} is exist: {}.", fileName, ret);
		return ret;
	}
	
	@Override
	public MdasIndex findById(String fileName) {
		logger.info("findById根据文件名查询已入库的索引.");
		return mdasRepository.findByFileName(fileName);
	}

	@Override
	public Iterable<MdasIndex> save(String fileType, List<MdasIndex> mdasList) {
		logger.info("es保存索引,fileType:{}, mdasList size: {}", fileType, mdasList.size());
		init(fileType);
		//覆盖写的内容索引直接重建, 追加写的内容合并后重建
		if(fileType.equals(MdasIndexEnum.LOGINFO.getType())){
			for (MdasIndex newMI : mdasList) {
				String fileName = newMI.getFileName();
				//如果该fileName的索引已经存在,进行合并
				if(isExist(fileName)){
					MdasIndex srcMI = findById(fileName);
					List<Lines> srcLines = srcMI.getLines();
					List<Lines> newLines = newMI.getLines();
					List<Lines> mergeLines = new ArrayList<>();
					for (Lines srcLine : srcLines) {
						boolean needAdd = true;
						for (Lines newLine : newLines) {
							//如果该商户号的lineSet已经存在,合并
							if(newLine.getMchtNo().equals(srcLine.getMchtNo())){
								newLine.getLineSet().addAll(srcLine.getLineSet());
								needAdd = false;						
							}
						}
						//如果该商户号的lineSet不存在,添加到该fileName下的List<Lines>中
						if(needAdd){
							mergeLines.add(srcLine);
						}	
					}
					newLines.addAll(mergeLines);
				//如果该fileName的索引不存在
				}else{
					//do nothing
				}
			}
		}
				
		return mdasRepository.save(mdasList);
	}
	
	@Override
	public MdasIndex save(String fileType, MdasIndex mdasIndex) {
		logger.info("es保存索引,fileType:{}, mdasIndex fileName:{}", fileType, mdasIndex.getFileName());
		init(fileType);
		//覆盖写的内容索引直接重建, 追加写的内容合并后重建
		if(fileType.equals(MdasIndexEnum.LOGINFO.getType())){		
			String fileName = mdasIndex.getFileName();
			//如果该fileName的索引已经存在,进行合并
			if(isExist(fileName)){
				MdasIndex srcMI = findById(fileName);
				List<Lines> srcLines = srcMI.getLines();
				List<Lines> newLines = mdasIndex.getLines();
				List<Lines> mergeLines = new ArrayList<>();
				for (Lines srcLine : srcLines) {
					boolean needAdd = true;
					for (Lines newLine : newLines) {
						//如果该商户号的lineSet已经存在,合并
						if(newLine.getMchtNo().equals(srcLine.getMchtNo())){
							newLine.getLineSet().addAll(srcLine.getLineSet());
							needAdd = false;						
						}
					}
					//如果该商户号的lineSet不存在,添加到该fileName下的List<Lines>中
					if(needAdd){
						mergeLines.add(srcLine);
					}	
				}
				newLines.addAll(mergeLines);
			//如果该fileName的索引不存在
			}else{
				//do nothing
			}

		}
						
		return mdasRepository.save(mdasIndex);
	}

	@Override
	public Map<String, Set<String>> findByMchtNo(String fileType, String mchtNo) {	
		logger.info("findByMchtNo param fileType:{}, mchtNo:{}", fileType, mchtNo);
		 Map<String, Set<String>> retMap = new HashMap<>();
		 //查询
	     esConstant.setIndex(MdasIndexEnum.geMdasIndex(fileType).getIndex());	     
	     QueryBuilder queryBuilder = QueryBuilders.nestedQuery("lines", QueryBuilders.boolQuery()
	    		 .must(QueryBuilders.termQuery("lines.mchtNo", mchtNo)));     
	     Iterable<MdasIndex> mdasIter =mdasRepository.search(queryBuilder);
	     for (MdasIndex mdasIndex : mdasIter) {
	    	 for (Lines line : mdasIndex.getLines()) {
				if(line.getMchtNo().equals(mchtNo)){
					retMap.put(mdasIndex.getFileName(), line.getLineSet());
				}
			}
		}
	    logger.info("findByMchtNo result:{}.", JSON.toJSONString(retMap));
		return retMap;
	}

	@Override
	public Map<String, Set<String>> findByMchtNoAndRangeDate(String fileType, String mchtNo,
			String from, String to) {
		logger.info("findByMchtNoAndRangeDate param fileType:{}, mchtNo:{}, from:{}, to:{}.", fileType, mchtNo, from, to);
		 Map<String, Set<String>> retMap = new HashMap<>();
		 esConstant.setIndex(MdasIndexEnum.geMdasIndex(fileType).getIndex());
	     //查询
	     QueryBuilder nestQB = QueryBuilders.nestedQuery("lines", QueryBuilders.boolQuery()
	    		 .must(QueryBuilders.termQuery("lines.mchtNo", mchtNo)));
	     QueryBuilder rangeQB = QueryBuilders.rangeQuery("transDate").from(from).to(to);	     
	     QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(nestQB).must(rangeQB);	     
	     Iterable<MdasIndex> mdasIter =mdasRepository.search(queryBuilder);
	     for (MdasIndex mdasIndex : mdasIter) {
	    	 for (Lines line : mdasIndex.getLines()) {
				if(line.getMchtNo().equals(mchtNo)){
					retMap.put(mdasIndex.getFileName(), line.getLineSet());
				}
			}
		}	
	   logger.info("findByMchtNoAndRangeDate result:{}.", JSON.toJSONString(retMap));
		return retMap;
	}
	
	
	void init(String fileType){
		esConstant.setIndex(MdasIndexEnum.geMdasIndex(fileType).getIndex());
	    esTemplate.createIndex(MdasIndex.class); 
	    esTemplate.putMapping(MdasIndex.class);
	    esTemplate.refresh(MdasIndex.class);
	}

}
