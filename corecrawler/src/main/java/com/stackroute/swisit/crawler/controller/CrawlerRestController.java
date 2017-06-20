package com.stackroute.swisit.crawler.controller;

import java.io.File;


import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.swisit.crawler.domain.IntensityBean;
//import com.stackroute.swisit.crawler.domain.CrawlerBean;
import com.stackroute.swisit.crawler.domain.SearcherResult;
import com.stackroute.swisit.crawler.service.KeywordScannerServiceImpl;
import com.stackroute.swisit.crawler.service.MasterScannerService;
import com.stackroute.swisit.crawler.subscriber.KafkaSubscriber;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@RequestMapping(value="/v1/api/swisit/crawler")
@Api(value="onlinestore", description="Operations pertaining to CrawlerService")
public class CrawlerRestController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private MasterScannerService masterScannerService;
	
	@Autowired
	public void setMasterScannerService(MasterScannerService masterScannerService){
		this.masterScannerService=masterScannerService;
	}	
	
	private KafkaSubscriber subscriber;
	
	@Autowired
	public void setSubscriber(KafkaSubscriber subscriber) {
		this.subscriber = subscriber;
	}
	
	@Autowired
	private KeywordScannerServiceImpl keywordScannerServiceImpl;

	
	@Autowired
	private MessageSource messageSource;
	
	
	@ApiOperation(value="SearcherValue",response = SearcherResult.class)
	   @ApiResponses(value = {
	           @ApiResponse(code = 200, message = "Successfully retrieved SearcherValue"),
	           @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
	           @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
	           @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	   }
	   )
	@RequestMapping(value="/receiver", method=RequestMethod.GET)
	public ResponseEntity<Map<String,String>> receiveMessage() throws JsonParseException, JsonMappingException, IOException{
		
		
		/*testcontrol is my topic name*/
		
		/*
		List<SwisitBean> c=subscriber.receivingMessage("testcontrol");
		SwisitBean crawlerbean[]= new SwisitBean[c.size()];
		c.toArray(crawlerbean);
		
		for(SwisitBean sb:c){
			System.out.println(sb.getLink());
			System.out.println(sb.getQuery());
			System.out.println(sb.getSnippet());
			System.out.println(sb.getTitle());
		}*/
		
		
		ObjectMapper mapper = new ObjectMapper();
		File file = new File("./src/main/resources/common/sample.json");
	    SearcherResult[] searcherResult=mapper.readValue(file, SearcherResult[].class);
	    if(searcherResult == null){
	    	Locale locale = LocaleContextHolder.getLocale();
	    	String message = messageSource.getMessage ("user.excep.null", null, locale );
	    	return new ResponseEntity(message,HttpStatus.NOT_FOUND);
	    	
		    /*Map<String,String> map= new HashMap<String,String>();
		    map.put("message","Data not received");
		    return new ResponseEntity<Map<String,String>>(map,HttpStatus.NOT_FOUND);*/  
		    
	    }
	    masterScannerService.scanDocument(searcherResult);
	    /*Map<String,String> map= new HashMap<String,String>();
	    map.put("message","Data received successfully");
	    return new ResponseEntity<Map<String,String>>(map,HttpStatus.OK);*/
	    
	    Locale locale = LocaleContextHolder.getLocale();
    	String message = messageSource.getMessage ("user.success.receive", null, locale );
    	return new ResponseEntity(message,HttpStatus.OK);
	
	}
	
	@ApiOperation(value="ToPublisher")
    @ApiResponses(value = {
              @ApiResponse(code = 200, message = "Successfully retrieved Publisher"),
              @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
              @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
              @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
      }
      )
	@RequestMapping(value="/publisher" , method=RequestMethod.GET)
		public ResponseEntity<Map<String,String>> finalmethod() throws JsonProcessingException {
		keywordScannerServiceImpl.publishingMessage();
		Locale locale = LocaleContextHolder.getLocale();
    	String message = messageSource.getMessage ("user.success.publish", null, locale );
    	return new ResponseEntity(message,HttpStatus.OK);
		
		/*Map<String,String> map= new HashMap<String,String>();
	    map.put("message","Data sent successfully");
	    return new ResponseEntity<Map<String,String>>(map,HttpStatus.OK);*/
	}
	
	
	/*================================INTERNATIONALIZATION============================================*/
	
	/*@RequestMapping(value = "/test", method = RequestMethod.GET)
    ResponseEntity test() {
      Locale locale = LocaleContextHolder.getLocale();
      String message = messageSource.getMessage ("user.excep.url", null, locale );
      return  new ResponseEntity(message,HttpStatus.OK); 
    }
	*/
	
/*	@RequestMapping(value="Demojson" ,method=RequestMethod.GET)
		public @ResponseBody Iterable<IntensityBean> saveQuery(IntensityBean ibean)  {
        
        ObjectMapper mapper = new ObjectMapper();
        try {
                    System.out.println("hi i am in Demo");
            File file = new File("./src/main/resources/common/intensity.json");
           List<LinkedHashMap<String,String>> list= (List<LinkedHashMap<String,String>>) mapper.readValue(file,ArrayList.class);
          // IntensityBean i=list.readValue(file, IntensityBean.class);
           
            System.out.println(list);
            //System.out.println(ibean.getIntensity());
            //System.out.println(ibean.getTitle());
           
            //for(Parent s:i.getP()){
            	//System.out.println("inside parent "+s.getTitle());
            //}
            //System.out.println(i.setP(p.getTitle());
            //System.out.println(i.setP(p.getIntensity()));
            //System.out.println(i.setP(p.getChild()));
            //intensityBean.setIntensity(i.getIntensity());
            //intensityBean.setTitle(i.getTitle());
        
             }
        catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        
        return null;
    } 
	
	@RequestMapping(value="" , method=RequestMethod.POST)
	public ResponseEntity<Map<String,String>> getJsonInput(@RequestBody SearcherResult crawlerBean){
		//masterScannerService.scanDocument(crawlerBean);
		Map<String,String> map= new HashMap<String,String>();
        map.put("message","Data sent successfully");
        return new ResponseEntity<Map<String,String>>(map,HttpStatus.OK);

	}

	@RequestMapping(value="JSON" , method=RequestMethod.GET)
	public ResponseEntity<Map<String,String>> getJsonInputDemo() throws JsonParseException, JsonMappingException, IOException, ParseException, JSONException{
		//masterScannerService.scanDocument(crawlerBean);
		//ObjectMapper mapper = new ObjectMapper();
		File file = new File("./src/main/resources/common/intensity.json");
	    //List<LinkedHashMap<String,String>> list= (List<LinkedHashMap<String,String>>) mapper.readValue(file, ArrayList.class);	    
	    //System.out.println(list);
	    //List<CrawlerBean> cb=(List<CrawlerBean>) mapper.readValue(new File("./src/main/resources/common/sample.json"),ArrayList.class);
		//SwisitBean[] crawlerbean=mapper.readValue(file, SwisitBean[].class);
		//@SuppressWarnings("deprecation")
		
		JSONParser jp=new JSONParser();
		//JsonParser jp=new JsonParser();
		org.json.simple.JSONObject obj=(org.json.simple.JSONObject) jp.parse(new FileReader("./src/main/resources/common/intensity.json"));
		//JSONObject jo=(JSONObject) obj;
		//JSONObject j=jo.getJSONObject("child");
		//System.out.println(j);
		//Map<String,String> m=(Map<String, String>) obj.get("child");
		//obj.
		//String s= (String) obj.get("child");
		System.out.println(obj);
		//System.out.println(m);
		JSONArray ja=(JSONArray) obj.get("parent");
		System.out.println(ja);
		Map<String,String> map= new HashMap<String,String>();
        map.put("message","Data sent successfully");
        return new ResponseEntity<Map<String,String>>(map,HttpStatus.OK);

	}*/

	
}
