	package com.stackroute.swisit.searcher.controller;

import java.util.ArrayList;
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
import com.stackroute.swisit.searcher.domain.SearcherJob;
import com.stackroute.swisit.searcher.domain.SearcherResult;
import com.stackroute.swisit.searcher.exception.SearcherServiceException;
import com.stackroute.swisit.searcher.hateoes.HateoesAssembler;
import com.stackroute.swisit.searcher.intialconsumer.IntialConsumer;
import com.stackroute.swisit.searcher.intialproducer.IntialProducer;
import com.stackroute.swisit.searcher.loadbalancing.LoadBalancing;
import com.stackroute.swisit.searcher.messageservice.MessageService;
import com.stackroute.swisit.searcher.repository.SearcherJobRepository;
import com.stackroute.swisit.searcher.searcherservice.SearchServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@RestController
@RequestMapping(value="v1/api/swisit/searcher")
@Api(value="SWIS-IT", description="Operations pertaining to the SearcherService")
public class SearchController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	MessageSource messageSource;
	@Autowired
	private SearchServiceImpl searchServiceImpl;
	@Autowired
	private  IntialProducer intialproducer;
    @Autowired
    private  IntialConsumer intialConsumer;
	@Autowired
	private HateoesAssembler hateoesAssembler;
	@Autowired
	LoadBalancing loadBal;
	
	SearcherJob searcherJob = new SearcherJob();
	
	List hateoasLink = null;
	
	
	@ApiOperation(value = "View a list of URLs from Google")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
	
	/*------------------------To get data from Google API----------------------------------------------*/
	
	@RequestMapping(value="", method=RequestMethod.GET)
	public ResponseEntity<List<SearcherResult>> getSearcherResult()
	{
		Locale locale = LocaleContextHolder.getLocale();
		try{
        		/* Get all data with hateoas link */
        		List<SearcherResult> searcherResultList = (List<SearcherResult>) searchServiceImpl.getAllSearcherResult();
        	    hateoasLink = hateoesAssembler.getAllLinks(searcherResultList);
        }
        catch(SearcherServiceException searching) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return  new ResponseEntity(hateoasLink ,HttpStatus.OK);
	}
	
	/*------------------------posting the data to mongo DB----------------------------------------------*/
	
	@ApiOperation(value = "Posting the Domain and Concept")
	@RequestMapping(value="", method=RequestMethod.POST)
    public ResponseEntity saveSearcherJob(@RequestBody SearcherJob produceSearcherJob) throws SearcherServiceException, Exception
    {
		Locale locale = LocaleContextHolder.getLocale();
		
		/* This is used for producing dummy messages */
		//SearcherJob produceSearcherJob=AssignSearcherJob();
        intialproducer.publishMessage("tosearcher", produceSearcherJob);
        
        
        /* This is used to get message from kafka */
        SearcherJob consumeSearcherJob = intialConsumer.listenMessage("tosearcher");
        logger.debug(consumeSearcherJob.getSitesearch()+" "+consumeSearcherJob.getResults());
        try {
            
            	searchServiceImpl.saveAllSearcherJob(consumeSearcherJob);
            	searchServiceImpl.saveAllSearcherResult();
                hateoasLink = hateoesAssembler.getLinksPost();
        
        } 
        catch (SearcherServiceException e) {
            return new ResponseEntity<SearcherJob>(HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity(hateoasLink,HttpStatus.OK);
        
    }
	
	/*------------------------To fetch data from mongo DB----------------------------------------------*/
	
	@ApiOperation(value = "Get the URLs stored in MongoDB")
	@RequestMapping(value="urlgetquery",method=RequestMethod.GET)
	public ResponseEntity<List<SearcherJob>> getAllSearcherJob()
	{
		Locale locale = LocaleContextHolder.getLocale();
        try{
        		List<SearcherJob> alldata = (List<SearcherJob>) searchServiceImpl.getAllSearcherJob();
        		hateoasLink = hateoesAssembler.getAllQuery(alldata);
        	
        }
        catch(SearcherServiceException searching) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return  new ResponseEntity(hateoasLink,HttpStatus.OK);
	}
	
	/*--------------------Dummy producer for Searcher Service--------------------------- */
	public SearcherJob AssignSearcherJob(){
        List<String> list=new ArrayList<String>();
        list.add("class");
        list.add("interface");
        /* setting all values for SearcherJob */
        searcherJob.setDomain("java");
        searcherJob.setConcept(list);
        searcherJob.setSitesearch("none");
        searcherJob.setResults("10");
        return searcherJob;
    }
	
	/*-----------------produce message through kafka for load balancing------------------- */
	@RequestMapping(value="producer",method=RequestMethod.GET)
    public ResponseEntity producer()
    {
        logger.debug("load balancer");
        loadBal.LoadProducer();
        return new ResponseEntity("success",HttpStatus.OK);
    }
    
	/*-----------------------consume message from kafka------------------------------------ */
    @RequestMapping(value="consumer",method=RequestMethod.GET)
    public ResponseEntity consumer()
    {
        logger.debug("load consumer");
        loadBal.LoadConsumer();
        return new ResponseEntity("success",HttpStatus.OK);
        
    }
	
}
	
