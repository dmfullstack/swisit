package com.stackroute.swisit.searcher.searcherservice;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.swisit.searcher.domain.SearcherResponse;
import com.stackroute.swisit.searcher.domain.SearcherJob;
import com.stackroute.swisit.searcher.domain.SearcherResult;
import com.stackroute.swisit.searcher.exception.SearcherServiceException;
import com.stackroute.swisit.searcher.messageservice.MessageService;
import com.stackroute.swisit.searcher.messageservice.MessageServiceImpl;
import com.stackroute.swisit.searcher.publisher.Publisher;
import com.stackroute.swisit.searcher.repository.SearcherJobRepository;
import com.stackroute.swisit.searcher.repository.SearcherResultRepository;
@Service
public class SearchServiceImpl implements SearchService {
	
	@Autowired
	private SearcherResultRepository searcherResultRepository;
	
	@Autowired
	private SearcherJobRepository searcherJobRepository;
	
	@Autowired
	Publisher kafkaconfig;
	
	SearcherResponse responsiveBean = new SearcherResponse();
	SearcherJob searcherJob = new SearcherJob();
	SearcherResult searcherResult=new SearcherResult();
	SearchServiceAsync searchServiceAsync = new SearchServiceAsync();
	RestTemplate restTemplate =  new RestTemplate();
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/* used to get the values from application.yml */
	@Value("${url}")
	String url;
	@Value("${key}")
	String key;
	String domain="";
	String url1;
	String url2;
	List<String> concept;
	List<LinkedHashMap<String,String>> engineid = new ArrayList<LinkedHashMap<String,String>>();
	

	/* save the search result for the query into SearchResult class */
    @Override
    public Iterable<SearcherResult> saveAllSearcherResult() {
        
        SearcherResult searcherResult =  new SearcherResult();
        //CompletableFuture<SearcherResponse> searcherResponse=null;
        SearcherResponse searcherResponse = new SearcherResponse();
        //MessageServiceImpl kafkaconfig = new MessageServiceImpl(); 
        /* Get the data from the SearchJob class */
        try
        {
            if(searcherJob.getDomain()==null || searcherJob.getConcept()==null) {
                throw new SearcherServiceException("SearcherJob has no value");
                
            }
            else
            {
                for(SearcherJob qb: searcherJobRepository.findAll())
                {
                    domain = qb.getDomain();
                    logger.debug(domain);
                    concept = qb.getConcept();
                    /* To iterate concept */
                    for(int k=0;k<concept.size();k++)
                    {
                    	//StartValue=0;
                    	String query = domain+" "+concept.get(k); 
                        engineid = qb.getEngineId();
                        for(Map<String, String> map : engineid){
                            url1=map.get(key);
                            System.out.println("url "+url1);
                            break;
                        }
                        
                        /* iterate to get various value from Google Api */
                       	for(int i=1;i<=11;i=i+10)
                        {   url2 = query+"&start="+i+url1;
                            String finalUrl = url+url2;
                            searcherResponse = restTemplate.getForObject(finalUrl, SearcherResponse.class);             
                            if(searcherResponse==null)
                            {
                                throw new SearcherServiceException("url is incorrect");
                            }
                            /* set the values to SearcherResult class and send the object to
                    crawlerService via Kafka */ 
                            try
                            {   
                                for(SearcherResult searcherResultRef:searcherResponse.getS())
                                {
                                    searcherResult.setQuery(searcherResponse.getQueries());
                                    searcherResult.setUrl(searcherResultRef.getUrl());
                                    searcherResult.setTitle(searcherResultRef.getTitle());
                                    searcherResult.setDescription(searcherResultRef.getDescription());
                                    searcherResult.setConcept(concept.get(k));
                                    try {               
                                        kafkaconfig.publishMessage("testcontrol", searcherResult);
                                    } 
                                    catch (JsonProcessingException e) {
                                        e.printStackTrace();
                                    }
                                    /* To save Searcher Result in MongoDB */
                                    searcherResultRepository.save(searcherResult);
                                }
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                            
                        }
                        //StartValue=StartValue+10;
                    }
                   
                }
            }
        }
        catch(SearcherServiceException e)
        {
            logger.error("Exception "+e.getMessage());
        }
        
    return searcherResultRepository.findAll();
    }	

	/* get all data from the SearchResult class */
	@Override
	public Iterable<SearcherResult> getAllSearcherResult() {
		try
		{
			if(searcherResultRepository.findAll()==null)
			{
				throw new SearcherServiceException("No data available");
			}
			else if(domain==null) {
				throw new SearcherServiceException("Domain was not Found");
			}
			else
			{
				/* method to get all data from the database */
				searcherResultRepository.findAll();
			}
		}
		catch(SearcherServiceException e)
		{
			logger.error("Exception"+e.getMessage());
		}
	return searcherResultRepository.findAll();
	}

	
	
	/* Created Search Job in the mongodb database */
	@Override
	public Iterable<SearcherJob> saveAllSearcherJob(SearcherJob sj) {
		System.out.println("inside saveallsearcherjob"+sj.getDomain());
		try 
		{
			searcherJob.setDomain(sj.getDomain());
			searcherJob.setConcept(sj.getConcept());
			if(searcherJob.getConcept()==null) {
				throw new SearcherServiceException("No domain name");
			}
			else
			{
				/*mapper is used to read the engineid and Api key from a JSON file
				 * included in the common folder
				 */
				ObjectMapper mapper = new ObjectMapper();
				File file = new File("./src/main/resources/common/googleEngine.json");
				List<LinkedHashMap<String,String>> list= (List<LinkedHashMap<String,String>>) mapper.readValue(file, ArrayList.class);
				searcherJob.setEngineId(list);
				searcherJob.setResults(sj.getResults());
				searcherJob.setSitesearch(sj.getSitesearch());
				searcherJobRepository.save(searcherJob);
				System.out.println("finsih saveallsearcherjob");
			}
		}
		catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	return searcherJobRepository.findAll();
	}

	/* Get All data from SearcherJob Class */
	@Override
	public Iterable<SearcherJob> getAllSearcherJob() {
		
		try
		{
			if(searcherJobRepository.findAll()==null)
			{
			throw new SearcherServiceException("No Data is Found");
			}
			else
			{
				/* method to get all data from the database */
				return searcherJobRepository.findAll();
			}
		}
		catch(SearcherServiceException e)
		{
			logger.error("Exception"+e.getMessage());
		}
	return searcherJobRepository.findAll();	
	}


}
