package com.stackroute.swisit.intentparser;
/*-----------Importing Libraries-----------*/
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.swisit.intentparser.domain.CrawlerResult;
import com.stackroute.swisit.intentparser.domain.IntentParserResult;
import com.stackroute.swisit.intentparser.service.IntentParseAlgo;
import com.stackroute.swisit.intentparser.service.IntentParseAlgoImpl;
import com.stackroute.swisit.intentparser.subscriber.SubscriberImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
/*-------------Spring Boot Application Main Class--------------*/
@EnableDiscoveryClient
@EnableEurekaClient
@SpringBootApplication
@EnableNeo4jRepositories(basePackages = "com.stackroute.swisit.intentparser.repository")
public class IntentParserApplication {
   // private static final Logger logger = LoggerFactory.getLogger(this.getClass());
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext applicationContext =SpringApplication.run(IntentParserApplication.class, args);
        SubscriberImpl subscriberImpl = applicationContext.getBean(SubscriberImpl.class);
        List<IntentParserResult> result=null;

//        ArrayList<CrawlerResult> intentInput = new ArrayList<CrawlerResult>();
//						ObjectMapper mapper = new ObjectMapper();
//						File file = new ClassPathResource("input.json").getFile();
//						CrawlerResult[] intentInputarr=mapper.readValue(file,CrawlerResult[].class);
//						for(CrawlerResult c:intentInputarr){
//				        	intentInput.add(c);
//				        }
        Iterable<CrawlerResult> intentInput=subscriberImpl.receivingMessage("tointent");
//        if(intentInput==null){
//            //String message = messageSource.getMessage ("user.excep.data", null, locale );
//            //return new ResponseEntity(message, HttpStatus.OK);
//        }

//        for(CrawlerResult lr:intentInput){
//            System.out.println("link is "+lr.getLink());
////            logger.info("link is "+lr.getLink());
////            logger.info("query is "+lr.getQuery());
////            logger.info("content schema is "+lr.getTerms()[0].getWord()+"---"+lr.getTerms()[0].getIntensity());
////            logger.info("snippet is "+lr.getSnippet());
////            logger.info("title is  "+lr.getTitle());
////            logger.info("date is "+lr.getLastindexedof());
//        }

			/*-------Resulted List from Intent Parser Algo-------*/
        IntentParseAlgoImpl intentParseAlgo = applicationContext.getBean(IntentParseAlgoImpl.class);
        result=(List<IntentParserResult>)intentParseAlgo.calculateConfidence(intentInput);
        for(IntentParserResult intentParserResult:result)
            System.out.println(intentParserResult);

    }
    /*-----------------Resolving Locale-----------------*/
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.US);
        return slr;
    }
    /*------------------Loading Message Bundles--------------------*/
    @Bean
    public MessageSource messageSource() {
         ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
         messageSource.setBasename("classpath:/messages/messages");
         return messageSource;
    }
    /*-------------Changing Language with User Preference------------*/
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
