package com.stackroute.swisit.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stackroute.swisit.domain.SearcherJob;
import com.stackroute.swisit.domain.SearcherResult;
import com.stackroute.swisit.exception.SearcherServiceException;
import com.stackroute.swisit.hateoes.HateoesAssembler;
import com.stackroute.swisit.messageservice.MessageService;
import com.stackroute.swisit.repository.QueryRepository;
import com.stackroute.swisit.searchservice.SearchService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;


@Api(value="SWIS-IT", description="Operations pertaining to the SWIS-IT App")
@RestController
@RequestMapping(value="v1/api/swisit/searcher")
public class SearchController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
		@Autowired
	SearcherJob queryBean;
	@Autowired
	MessageSource messageSource;
	
	@Autowired
	private SearchService searchService;
	
	
	@Autowired
	private HateoesAssembler hateoesAssembler;
	
	@ApiOperation(value = "View a list of URLs from Google")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
	
	/*------------------------To get data from Google API----------------------------------------------*/
	
	@RequestMapping(value="urlget", method=RequestMethod.GET)
	public ResponseEntity<List<SearcherResult>> get()
	{
		
		List<SearcherResult> all = null;
        QueryRepository queryrepo=null;
        try{
        	if(searchService.getAll()==null) {
        		return new ResponseEntity(HttpStatus.NO_CONTENT);
        	}
        	else {
        		List<SearcherResult> alldata = (List<SearcherResult>) searchService.getAll();
        		all=hateoesAssembler.getalllinks(alldata);
        	}
        }
        catch(SearcherServiceException searching) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return  new ResponseEntity(all,HttpStatus.OK);
	}
	
	/*------------------------posting the data to mongo DB----------------------------------------------*/
	
	@ApiOperation(value = "Posting the Domain and Concept")
	@RequestMapping(value="urlpostquery", method=RequestMethod.POST)
    public ResponseEntity saveQuery(@RequestBody SearcherJob queryBean) throws SearcherServiceException, Exception
    {
		Locale locale = LocaleContextHolder.getLocale();
        
        try {
            if(searchService.saveQuery(queryBean)==null) {
            	  String message = messageSource.getMessage ("user.excep.nodata", null, locale );
                return new ResponseEntity(message,HttpStatus.NOT_FOUND);
            }
            else
            {
                searchService.saveQuery(queryBean);
                searchService.save();
            }
        
        } catch (SearcherServiceException e) {
            // TODO Auto-generated catch block
            return new ResponseEntity<SearcherJob>(HttpStatus.NOT_FOUND);
        }
        String message = messageSource.getMessage ("user.msg.receive", null, locale );
        System.out.println(message);
        return new ResponseEntity(message,HttpStatus.OK);
        
    }
	
	/*------------------------To fetch data from mongo DB----------------------------------------------*/
	
	@ApiOperation(value = "Get the URLs stored in MongoDB")
	@RequestMapping(value="urlgetquery",method=RequestMethod.GET)
	public ResponseEntity<List<SearcherJob>> getQuery()
	{
		
		List<SearcherJob> all = null;
        QueryRepository queryrepo=null;
        try{
        	if(searchService.getQuery()==null) {
        		return new ResponseEntity(HttpStatus.NO_CONTENT);
        	}
        	else {  
        		List<SearcherJob> alldata = (List<SearcherJob>) searchService.getQuery();
        		all=hateoesAssembler.getallquery(alldata);
        	}
        }
        catch(SearcherServiceException searching) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return  new ResponseEntity(all,HttpStatus.OK);
	}
	
	
}
	