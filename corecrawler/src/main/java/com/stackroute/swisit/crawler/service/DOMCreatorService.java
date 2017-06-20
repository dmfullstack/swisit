package com.stackroute.swisit.crawler.service;

import org.jsoup.nodes.Document;

public interface DOMCreatorService {
	
	public Document constructDOM(String link);
}
