package com.stackroute.swisit.crawler.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.net.MediaType;
import com.stackroute.swisit.crawler.controller.CrawlerRestController;
import com.stackroute.swisit.crawler.domain.IntensityBean;
import com.stackroute.swisit.crawler.domain.SearcherResult;
import com.stackroute.swisit.crawler.service.KeywordScannerServiceImpl;
import com.stackroute.swisit.crawler.service.MasterScannerService;
import com.stackroute.swisit.crawler.subscriber.KafkaSubscriber;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import javax.validation.Validator;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes =SearcherResult.class, loader = AnnotationConfigContextLoader.class)
@WebMvcTest(controllers = CrawlerRestController.class)


public class CrawlerRestControllerTest {

	private MockMvc mockMvc;
    
   /* @Autowired
    private WebApplicationContext webApplicationContext;*/
     @MockBean
	private KeywordScannerServiceImpl keywordScannerServiceImpl;
     @MockBean
     private MasterScannerService masterScannerService;
     @MockBean
 	 private KafkaSubscriber subscriber;
     @MockBean
     private Validator validator;
    // public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
  /*  @Before
    public void setUp() {
    	mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
*/ /*   @Test
    public void test() {
    	fail("Not yet implemented");
    }*/
    
     SearcherResult[] searcherResult ;
    @Test
  public void receiveMessage() throws Exception {
      String expectedstring="{\n" +
              "  \"message\": \"Data received successfully\"\n" +
              "}";
      
//      SearcherResult[] searcherResult = [ {"query":"Angular Component",
//    	  "link":"https://github.com/toddmotto/angular-1-5-components-app",
//    	  "title":"GitHub - toddmotto/angular-1-5-components-app: A Contacts ...",
//    	  "snippet":"angular-1-5-components-app - A Contacts Manager application built on Angular \n1.5 components, ui-router 1.0.0, Firebase."
//    		  }
//      SearcherResult searcherResult =new SearcherResult("Angular Component","https://github.com/toddmotto/angular-1-5-components-app",
//    		  "GitHub - toddmotto/angular-1-5-components-app: A Contacts ...","angular-1-5-components-app - A Contacts Manager application built on Angular \n1.5 components, ui-router 1.0.0, Firebase.");
//      searcherResult[0].setQuery("Angular Component");
  //    searcherResult[0].setLink("https://github.com/toddmotto/angular-1-5-components-app");
    //  searcherResult[0].setTitle("GitHub - toddmotto/angular-1-5-components-app: A Contacts ...");
      //searcherResult[0].setSnippet("angular-1-5-components-app - A Contacts Manager application built on Angular \n1.5 components, ui-router 1.0.0, Firebase.");
      

     when(masterScannerService.scanDocument(searcherResult)).thenReturn(expectedstring);

    
      mockMvc.perform(get("/v1/api/swisit/crawler/receiver")
    		  .accept("application/json;charset=UTF-8"))
  	//.accept("Searcher data received successsfully")
  	.andExpect(status().isOk())
  	.andExpect(content().string(expectedstring));
              
      verify(masterScannerService, times(1)).scanDocument(searcherResult);
      verifyNoMoreInteractions(masterScannerService);
  }
  
    /*@Test
    public void receiveMessage() throws Exception {
    	String expectedstring="/n"+"Searcher data received successsfully"+"/n";
    	doNothing().when(masterScannerService).scanDocument(searcherResult) ;

    	mockMvc.perform(get("/v1/api/crawler/receiver").accept("application/json;charset=UTF-8"))
    	//.accept("Searcher data received successsfully")
    	.andExpect(status().isOk())
    	.andExpect(content().string("Searcher data received successsfully"));
    	verify(masterScannerService, times(1)).scanDocument(searcherResult);
    	verifyNoMoreInteractions(masterScannerService);
    	
    }*/

}
