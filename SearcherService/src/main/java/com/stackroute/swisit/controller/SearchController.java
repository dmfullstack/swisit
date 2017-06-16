package com.stackroute.swisit.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stackroute.swisit.domain.QueryBean;
import com.stackroute.swisit.domain.SwisitBean;
import com.stackroute.swisit.exception.SearcherServiceException;
import com.stackroute.swisit.hateoes.LinkHateoes;
import com.stackroute.swisit.messageservice.MessageService;
import com.stackroute.swisit.repository.QueryRepository;
import com.stackroute.swisit.searchservice.SearchService;

@RestController
public class SearchController {
	
		@Autowired
	QueryBean queryBean;
	
	
	@Autowired
	private SearchService searchService;
	
	
	@Autowired
	private LinkHateoes linkHateoes;
	
	
	@RequestMapping(value="urlget", method=RequestMethod.GET)
	public ResponseEntity<List<SwisitBean>> get()
	{
		
		List<SwisitBean> all = null;
        QueryRepository queryrepo=null;
        try{
        	if(searchService.getAll()==null) {
        		return new ResponseEntity(HttpStatus.NO_CONTENT);
        	}
        	else {
        		List<SwisitBean> alldata = (List<SwisitBean>) searchService.getAll();
        		all=linkHateoes.getalllinks(alldata);
        	}
        }
        catch(SearcherServiceException searching) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return  new ResponseEntity(all,HttpStatus.OK);
	}
	
	
	@RequestMapping(value="urlpostquery", method=RequestMethod.POST)
    public ResponseEntity saveQuery(@RequestBody QueryBean queryBean) throws SearcherServiceException, Exception
    {
        
        try {
            if(searchService.saveQuery(queryBean)==null) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            else
            {
                searchService.saveQuery(queryBean);
                searchService.save();
            }
        
        } catch (SearcherServiceException e) {
            // TODO Auto-generated catch block
            return new ResponseEntity<QueryBean>(HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<Map<String,String>>( HttpStatus.OK);
    }
	
	
	@RequestMapping(value="urlgetquery",method=RequestMethod.GET)
	public ResponseEntity<List<QueryBean>> getQuery()
	{
		
		List<QueryBean> all = null;
        QueryRepository queryrepo=null;
        try{
        	if(searchService.getQuery()==null) {
        		return new ResponseEntity(HttpStatus.NO_CONTENT);
        	}
        	else {  
        		List<QueryBean> alldata = (List<QueryBean>) searchService.getQuery();
        		all=linkHateoes.getallquery(alldata);
        	}
        }
        catch(SearcherServiceException searching) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return  new ResponseEntity(all,HttpStatus.OK);
	}
	
	
}
	