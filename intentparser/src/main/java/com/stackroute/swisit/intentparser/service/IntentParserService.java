package com.stackroute.swisit.intentparser.service;

import com.stackroute.swisit.intentparser.domain.Intent;
import com.stackroute.swisit.intentparser.domain.Term;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IntentParserService {
    List<Intent> getAllIntents();
    List<Term> getAllTerms();

    Iterable<Map<String,Object>> getAllIndicator();
    Iterable<Map<String,Object>> getBothRelationships();
    Iterable<Map<String,Object>> getAllRelationships();
    List<Map<String,String>> fetchAllRelationships();
    Iterable<Map<String,String>> getAllTermsRelationOfIntent(String intentName);
}
