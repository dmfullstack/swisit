package com.stackroute.swisit.crawler.service;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DOMCreatorServiceImpl implements DOMCreatorService{
   
	HashSet<String> links=new HashSet<>();
	
	@Override
	public Document constructDOM(String link) {
		Document doc = null;
		if(!links.contains(link)){
			try{
				doc=Jsoup.connect(link).get();
				
			}catch(Exception e){
				System.out.println(e);
			}
		}		
		
		/*try{
		System.out.println("inside constructDom "+link);
		URL url = new URL(link);
		doc = Jsoup.parse(url, 3000);
		//System.out.println("inside constructDom document "+doc);
		
		
		}catch(Exception e){
			System.out.println(e);
		}*/
		return doc;
	}

}
