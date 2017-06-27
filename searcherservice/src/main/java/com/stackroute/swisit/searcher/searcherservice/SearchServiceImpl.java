package com.stackroute.swisit.searcher.searcherservice;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import com.stackroute.swisit.searcher.domain.ResponsiveBean;
import com.stackroute.swisit.searcher.domain.SearcherJob;
import com.stackroute.swisit.searcher.domain.SearcherResult;
import com.stackroute.swisit.searcher.exception.SearcherServiceException;
import com.stackroute.swisit.searcher.messageservice.MessageService;
import com.stackroute.swisit.searcher.repository.SearcherJobRepository;
import com.stackroute.swisit.searcher.repository.SearcherResultRepository;



@Service
public class SearchServiceImpl implements SearchService {
	
	@Autowired
	private SearcherResultRepository searcherResultRepository;
	
	@Autowired
	private SearcherJobRepository searcherJobRepository;
	
	@Autowired
	MessageService kafkaconfig;

	
	ResponsiveBean responsiveBean = new ResponsiveBean();
	SearcherJob searcherJob = new SearcherJob();
	SearcherResult searcherResult=new SearcherResult();
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/* used to get the values from application.yml */
	@Value("${url}")
	String url;
	@Value("${key}")
	String key;
	String domain="";
	String url1;
	List<String> concept;
	List<LinkedHashMap<String,String>> engineid = new ArrayList<LinkedHashMap<String,String>>();
	

	/* save the search result for the query into SearchResult class */
	@Override
	public Iterable<SearcherResult> saveAllSearcherResult() {
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
					String query = domain+" "+concept.get(0);	
					engineid = qb.getEngineId();
					for(Map<String, String> map : engineid){
						url1=map.get(key);
						break;
					}
					url1 = query+url1;
				}
				String finalUrl = url+url1;
				/* RestTemplate is a class that contains getForObject method 
		   		to get the value from the Google Api and stored as object */ 
				RestTemplate restTemplate = new RestTemplate();
				responsiveBean = restTemplate.getForObject(finalUrl,ResponsiveBean.class);
				if(responsiveBean==null)
				{
					throw new SearcherServiceException("url is incorrect");
				}

				/* set the values to SearcherResult class and send the object to
		   		crawlerService via Kafka */ 
				List<SearcherResult> searcherResultList=new ArrayList<SearcherResult>();
				for(SearcherResult b:responsiveBean.getS())
				{
					searcherResult.setQuery(responsiveBean.getQueries());
					searcherResult.setUrl(b.getUrl());
					searcherResult.setTitle(b.getTitle());
					searcherResult.setDescription(b.getDescription());
					searcherResultRepository.save(searcherResult);
					try {
						kafkaconfig.publishmessage("testcontrol", searcherResult);
					} 
					catch (JsonProcessingException e) {
						e.printStackTrace();
					}
					searcherResultList.add(searcherResult);
				}
			}

		}
		catch(SearcherServiceException e)
		{
			logger.debug("Exception "+e.getMessage());
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
			logger.debug("Exception"+e.getMessage());
		}
	return searcherResultRepository.findAll();
	}

	
	
	/* Created Search Job in the mongodb database */
	@Override
	public Iterable<SearcherJob> saveAllSearcherJob(SearcherJob sj) {
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
			logger.debug("Exception"+e.getMessage());
		}
	return searcherJobRepository.findAll();	
	}


}
