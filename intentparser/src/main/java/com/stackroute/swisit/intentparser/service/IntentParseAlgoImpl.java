package com.stackroute.swisit.intentparser.service;
/*-------Importing Liberaries------*/
import com.stackroute.swisit.intentparser.domain.*;
import com.stackroute.swisit.intentparser.exception.ConfidenceScoreNotCalculatedException;
import com.stackroute.swisit.intentparser.repository.DocToConcept;
import com.stackroute.swisit.intentparser.repository.IntentRepository;
import com.stackroute.swisit.intentparser.repository.RelationshipRepository;
import com.stackroute.swisit.intentparser.repository.TermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
/*-------Implementation of IntentParseAlgo Interface class------*/
@Service
public class IntentParseAlgoImpl implements IntentParseAlgo {

    /*-------Autowired Repositories-------*/
	@Autowired
    IntentRepository intentRepository;
	@Autowired
    RelationshipRepository relationshipRepository;
	@Autowired
	TermRepository termRepository ;
	@Autowired
	DocToConcept docToConcept;
    /*------------CalculateConfidence method for getting List of IntentParserResult-----------*/
    @Override
    public ArrayList<IntentParserResult> calculateConfidence(Iterable<CrawlerResult> intentInput){
    	List<Intent> intentsList = intentRepository.findIntents();
        ArrayList<IntentParserResult> intentParserResultList = new ArrayList<IntentParserResult>();
        for(CrawlerResult intentParserInput : intentInput){
            intentParserResultList.addAll(calculateConfidenceScore(intentParserInput,intentsList));
        }
        Collections.sort(intentParserResultList, new Comparator<IntentParserResult>() {
            @Override
            public int compare(IntentParserResult o1, IntentParserResult o2) {
                return (int)(o2.getConfidenceScore()-o1.getConfidenceScore());
            }
        });
        return intentParserResultList;
    }

    /*-----------CalculateConfidenceScore Method to calculate confidence score for each
                 IntentParserResult bean and Creting an Object of IntentParserResult and
                 also saves the result in Neo4jDatabse. It returns the arraylist of
                 IntentParseResult for Each Intent-----------*/
	@Override
	public ArrayList<IntentParserResult> calculateConfidenceScore(CrawlerResult intentParserInput,List<Intent> intentList){
		ArrayList<IntentParserResult> results=new ArrayList<IntentParserResult>();
		for (Intent intent : intentList) {
			List<Map<String, String>> relList = relationshipRepository.getAllTermsRelationOfIntent(intent.getName());
			/*exception handling*/
				try {
					if (relList == null) {
					throw new ConfidenceScoreNotCalculatedException("Empty data in database");
					}
				} catch (ConfidenceScoreNotCalculatedException e) {
					e.printStackTrace();
				}
			
			/*exception handling*/
			ArrayList<ContentSchema> contentSchemas = intentParserInput.getTerms();
			ArrayList<Relationships> relationsList = new ArrayList<Relationships>();
			for (Map<String, String> map : relList) {
				Relationships r = new Relationships();
				r.setIntentName(map.get("intentName"));
				r.setTermName(map.get("termName"));
				r.setRelName(map.get("relName"));
				r.setWeight(Float.parseFloat(map.get("weight")));
				relationsList.add(r);
			}
			float in = 0f, ci = 0f, confidenceScore;
			for (ContentSchema contentSchema : contentSchemas) {
				for (Relationships relationships : relationsList) {
					if (contentSchema.getWord() == null) { continue; }
					if (contentSchema.getWord().equalsIgnoreCase(relationships.getTermName())) {
						if (relationships.getRelName().equalsIgnoreCase("indicatorOf")) {
							in += (contentSchema.getIntensity() * relationships.getWeight());
						}
						if (relationships.getRelName().equalsIgnoreCase("counterIndicatorOf")) {
							ci += (contentSchema.getIntensity() * relationships.getWeight());
						}
					}
				}
			}
			confidenceScore = in - ci;
			IntentParserResult intentParserResult = new IntentParserResult(intentParserInput.getLink(), intent.getName(), confidenceScore, intentParserInput.getQuery());
			intentRepository.createDocumentNode(intentParserResult.getUrl());
			Map<String,String> map = docToConcept.createDocToConceptRels(intentParserResult.getUrl(),intentParserResult.getIntent(),intentParserResult.getConfidenceScore(),intentParserResult.getConcept());
			results.add(intentParserResult);
		}
		return results;
	}
}

