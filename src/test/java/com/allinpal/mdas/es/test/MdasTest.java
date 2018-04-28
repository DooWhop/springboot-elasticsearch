package com.allinpal.mdas.es.test;

import com.alibaba.fastjson.JSON;
import com.doowhop.mdas.es.ESConstant;
import com.doowhop.mdas.es.MdasEsApplication;
import com.doowhop.mdas.es.domain.Lines;
import com.doowhop.mdas.es.domain.MdasIndex;
import com.doowhop.mdas.es.enums.MdasIndexEnum;
import com.doowhop.mdas.es.repository.MdasIndexRepository;
import com.doowhop.mdas.es.service.MdasIndexService;
import com.google.common.collect.Lists;

import org.apache.lucene.queryparser.xml.FilterBuilder;
import org.apache.lucene.queryparser.xml.FilterBuilderFactory;
import org.apache.lucene.queryparser.xml.ParserException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.lucene.search.function.FiltersFunctionScoreQuery.ScoreMode;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Before;  
import org.junit.Test;
import org.junit.runner.RunWith;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.boot.test.context.SpringBootTest;  
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;  
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;  

import org.w3c.dom.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;  
  











import java.util.Map;
import java.util.Set;
import java.util.TreeSet;  

import org.apache.lucene.queryparser.xml.builders.FilteredQueryBuilder;  
import org.apache.lucene.search.Filter;
  
@RunWith(SpringRunner.class)  
@SpringBootTest(classes = MdasEsApplication.class)  
public class MdasTest {  
  
    @Autowired  
    private MdasIndexService mdasService;  
    
    @Autowired 
    private MdasIndexRepository mdasRepository;
  
    @Autowired  
    private ElasticsearchTemplate esTemplate;  
    
    @Autowired  
    private ESConstant esConstant;
  
    @Before  
    public void before() {  
//        esTemplate.deleteIndex(MdasIndex.class);  
//        esTemplate.createIndex(MdasIndex.class);  
//        esTemplate.putMapping(MdasIndex.class);  
//        esTemplate.refresh(MdasIndex.class);  
    }  
  
//    @Test
//    public void testSave() {  
//    	List<String> lineNo = new ArrayList<>();
//    	lineNo.add("11");
//    	lineNo.add("22");
//    	esConstant.setIndex(MdasIndexEnum.SYB_LOGINFO.getIndex());
//    	esConstant.setType("syb_loginfo_20180301");
//    	MdasFile mdasFile = new MdasFile("1001","syb_loginfo_20180301", MdasIndexEnum.SYB_LOGINFO.getType(), lineNo);  
//        MdasFile testMdasFile = mdasService.save(mdasFile);  
//  
//        assertNotNull(testMdasFile.getMchtId());  
///*        assertEquals(testMdasFile.getTitle(), MdasFile.getTitle());  
//        assertEquals(testMdasFile.getAuthor(), MdasFile.getAuthor());  
//        assertEquals(testMdasFile.getReleaseDate(), MdasFile.getReleaseDate());  */
//  
//    }  
//    
//    
//    
//    @Test
//    public void testSave2() {  
//    	List<String> lineNo = new ArrayList<>();
//    	lineNo.add("3333");
//    	lineNo.add("4444");
//    	esConstant.setIndex("ldp");
//    	esConstant.setType("llldddppp");
//    	MdasFile mdasFile = new MdasFile("1001","syb_loginfo_20180301", MdasIndexEnum.SYB_LOGINFO.getType(), lineNo);  
//        MdasFile testMdasFile = mdasService.save(mdasFile);  
//  
//        assertNotNull(testMdasFile.getMchtId());  
///*        assertEquals(testMdasFile.getTitle(), MdasFile.getTitle());  
//        assertEquals(testMdasFile.getAuthor(), MdasFile.getAuthor());  
//        assertEquals(testMdasFile.getReleaseDate(), MdasFile.getReleaseDate());  */
//  
//    } 
    
    @Test  
    public void testFindBy() throws ParseException { 
        esConstant.setIndex(MdasIndexEnum.RXINFO.getIndex());
        esTemplate.createIndex(MdasIndex.class); 
        esTemplate.putMapping(MdasIndex.class);
        esTemplate.refresh(MdasIndex.class);
    	 esConstant.setIndex(MdasIndexEnum.LOGINFO.getIndex());
//    	 MdasIndex mdas = new MdasIndex();
//    	 mdas.setFileName("agt_loginfo_20180420");
//    	 mdas.setTransDate(new Date());
//    	 
//    	 List lines = new ArrayList();
//    	 Lines line = new Lines();
//    	 line.setMchtNo("12345");
//    	 line.setLineSet(null);
//    	 mdas.setLines(lines);
//    	 
//    	 IndexQuery indexQuery = new IndexQuery();
//    	 
//         indexQuery.setId(mdas.getFileName());
//         indexQuery.setObject(mdas);

        //creating mapping
        esTemplate.createIndex(MdasIndex.class); 
        esTemplate.putMapping(MdasIndex.class);
        esTemplate.refresh(MdasIndex.class);

    }
    
    
    @Test  
    public void testFindService() throws ParseException { 
    	
    	Map<String, Set<String>> ret = mdasService.findByMchtNoAndRangeDate("0", "1009", "2018-04-26", "2018-04-28");
    	System.out.println(JSON.toJSONString(ret));
    	
    }
    
    @Test  
    public void testFind() throws ParseException {  
    	
     esConstant.setIndex(MdasIndexEnum.RXINFO.getIndex());
     
     QueryBuilder queryBuilder1 = QueryBuilders.nestedQuery("lines", QueryBuilders.boolQuery()
    		 .must(QueryBuilders.termQuery("lines.mchtNo", "990614055336011")));
     QueryBuilder queryBuilder2 = QueryBuilders.rangeQuery("transDate").from("2017-04-25").to("2018-04-26");
     
     QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(queryBuilder1).must(queryBuilder2);
    // Iterable<MdasIndex> mdasIter =mdasRepository.search(queryBuilder);
      SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
      List<MdasIndex> mdasList =  esTemplate.queryForList(searchQuery, MdasIndex.class);

     //List<MdasIndex> mdasList = Lists.newArrayList(mdasIter);
     
     System.out.println(JSON.toJSONString(mdasList));    	
    }
  
    @Test  
    public void testSave() throws ParseException {  
    	   	
    	List<Lines> linList = new ArrayList<>();
    	for(int i=0; i<500000; i++){
    		Set<String> lineSet = new TreeSet<>();
    		lineSet.add("444");
    		lineSet.add("333");
    		lineSet.add("222");
         	lineSet.add("111");
        	Lines lineNo = new Lines(Integer.toString(i), lineSet);
        	linList.add(lineNo);
    	}
    	MdasIndex mdasIndex = new MdasIndex("/datas/mcht/loginfo/201804/all_loginfo_20180401.txt", "2018-04-01", linList);  	
    	
    	List<MdasIndex> mdasList = new ArrayList<>();
    	//mdasList.add(mdasIndex1);
    	mdasList.add(mdasIndex);
    	
        mdasService.save("0", mdasList); 
        linList = null;
        mdasList = null;
      //  List<MdasIndex> testMdasFile = mdasService.findByMchtNo("1001");  
    	// MdasIndex testMdasFile = mdasService.findById("1003"); 
     // System.out.println(JSON.toJSONString(it));
    }  
   
  
} 