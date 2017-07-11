	package com.stackroute.swisit.searcher.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stackroute.swisit.searcher.domain.SavingSearcherResult;
import com.stackroute.swisit.searcher.domain.SearcherJob;
import com.stackroute.swisit.searcher.domain.SearcherResult;
import com.stackroute.swisit.searcher.exception.SearcherServiceException;
import com.stackroute.swisit.searcher.hateoes.HateoesAssembler;
import com.stackroute.swisit.searcher.intialconsumer.IntialConsumer;
import com.stackroute.swisit.searcher.intialproducer.IntialProducer;
import com.stackroute.swisit.searcher.loadbalancing.LoadBalancing;
import com.stackroute.swisit.searcher.messageservice.MessageService;
import com.stackroute.swisit.searcher.repository.SearcherResultRepository;
import com.stackroute.swisit.searcher.searcherservice.SearcherServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@RestController
@RequestMapping(value="v1/api/swisit/searcher")
@Api(value="SWIS-IT", description="Operations pertaining to the SearcherService")
public class SearcherController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	MessageSource messageSource;
	@Autowired
	private SearcherServiceImpl searchServiceImpl;
	@Autowired
	private  IntialProducer intialproducer;
    @Autowired
    private  IntialConsumer intialConsumer;
	@Autowired
	private HateoesAssembler hateoesAssembler;
	@Autowired
	LoadBalancing loadBal;
	
	@Autowired
	private SearcherResultRepository searcherResultRepository;
	
	SearcherJob searcherJob = new SearcherJob();
	
	List hateoasLink = null;
	List hateoasLinkRef = null;
	
	
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
	public ResponseEntity<List<SavingSearcherResult>> getSearcherResult()
	{
		Locale locale = LocaleContextHolder.getLocale();
		try{
        		/* Get all data with hateoas link */
        		List<SavingSearcherResult> searcherResultList = (List<SavingSearcherResult>) searchServiceImpl.getAllSearcherResult();
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
        logger.info(consumeSearcherJob.getDomain()+" "+consumeSearcherJob.getConcept());
        String domain = consumeSearcherJob.getDomain();
		List concept = consumeSearcherJob.getConcept();
		List<String> queryList=new ArrayList<String>();
		List<String> conceptList =  new ArrayList<String>();
		int flag = 0;
        try {
        		
        		/* To find the given domain and concept already present in DB */
        		
        		if(searcherResultRepository.findAll().isEmpty())
        		{
        			 searchServiceImpl.saveAllSearcherResult(consumeSearcherJob);
        			 hateoasLink = hateoesAssembler.getLinksPost();
                     return new ResponseEntity(hateoasLink,HttpStatus.OK);
        		}
        		else
        		{
        			for(SavingSearcherResult savingSearcherResult:searcherResultRepository.findAll()){
            			queryList.add(savingSearcherResult.getQuery());
        			}
        			/* loop for different concept */
        			for(int i=0;i<concept.size();i++)
            		{
            			String query = domain+" "+concept.get(i);
            			/* loop for checking the query with the database */
            			for(int j=0;j<queryList.size();j++)
            			{
            				if(query.equals(queryList.get(j)))
            				{
            					flag++;
            				}
            				else
            				{
            					flag=0;
            				}
            			}
            			/* Add concept to the list which is not present in database */
            			if(flag==0)
            			{
            				conceptList.add((String) concept.get(i));
            			}
            				
            		}
        			if(flag>0)
        			{
        				hateoasLinkRef = hateoesAssembler.getLinksPostError();
        				return new ResponseEntity(hateoasLinkRef,HttpStatus.OK);
        			}
        			else
        			{
        				consumeSearcherJob.setConcept(conceptList);
        				/* To send the details to coreCrawler service */
        				searchServiceImpl.saveAllSearcherResult(consumeSearcherJob);
                        hateoasLink = hateoesAssembler.getLinksPost();
                        return new ResponseEntity(hateoasLink,HttpStatus.OK);
        			}
        		}
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
		return new ResponseEntity(hateoasLink,HttpStatus.OK);
    }		
        			
	/*-----------------produce message through kafka for load balancing------------------- */
	@RequestMapping(value="producer",method=RequestMethod.GET)
    public ResponseEntity producer()
    {
        logger.debug("load balancer");
        loadBal.LoadProducer();
        return new ResponseEntity("success",HttpStatus.OK);
    }
    
	/*-----------------------consume message from kafka for load balancing------------------------------------ */
    @RequestMapping(value="consumer",method=RequestMethod.GET)
    public ResponseEntity consumer()
    {
        logger.debug("load consumer");
        loadBal.LoadConsumer();
        return new ResponseEntity("success",HttpStatus.OK);
        
    }
	
}
	
