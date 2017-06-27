package com.stackroute.swisit.crawler;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.stackroute.swisit.crawler.domain.SearcherResult;
import com.stackroute.swisit.crawler.service.MasterScannerServiceImpl;
import com.stackroute.swisit.crawler.subscriber.KafkaSubscriberImpl;

@SpringBootApplication
//@EnableEurekaClient
public class CoreCrawlerMainApplication extends WebMvcConfigurerAdapter{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/*-----------------Main method where execution starts ------------------*/
	@Value("${topic-fromconsumer}")
	static String consumerTopic;
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		System.out.println(consumerTopic);
		
		ConfigurableApplicationContext applicationContext =SpringApplication.run(CoreCrawlerMainApplication.class, args);
		KafkaSubscriberImpl kafkaSubscriberImpl = applicationContext.getBean(KafkaSubscriberImpl.class);
		List<SearcherResult> list=kafkaSubscriberImpl.receivingMessage("testcontrol");
		SearcherResult searcherResult[]= new SearcherResult[list.size()];
		list.toArray(searcherResult);

		for(SearcherResult sr:list){
			System.out.println(sr.getLink());
			//System.out.println(sr.getQuery());
			//System.out.println(sr.getSnippet());
			//System.out.println(sr.getTitle());
		}
		MasterScannerServiceImpl masterScannerServiceImpl = new MasterScannerServiceImpl();
		masterScannerServiceImpl.scanDocument(searcherResult);
	}

	/*-------------- Methods to implement internationalization --------------*/

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.US);
		return slr;
	}
	
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:/messages/messages");
		return messageSource;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
}
