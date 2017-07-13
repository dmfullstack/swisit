package com.stackroute.swisit.usersearchservice.service;
/*-----------Importing Libraries------------*/
import com.stackroute.swisit.usersearchservice.domain.*;
import com.stackroute.swisit.usersearchservice.exception.NeoDataNotFetchedException;
import com.stackroute.swisit.usersearchservice.repository.UserSearchServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*-------Implementation of UserSearchService Interface class------*/
@Service
public class UserSearchServiceImpl implements UserSearchService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    /*-------Autowired Repositories-------*/
    @Autowired
    UserSearchServiceRepository userSearchServiceRepository;


    UserInput userInput = new UserInput();

    ArrayList<UserSearchResult> userSearchResults = new ArrayList<UserSearchResult>();



    /*------------fetchConcept method for getting List of ConceptResult-----------*/
    @Override
    public List<String> fetchConcept() {

        List<String> conceptResult = userSearchServiceRepository.findConcepts();
        return conceptResult;
    }



    /*------------fetchTerm method for getting List of TermResult-----------*/
    @Override
    public List<String> fetchTerm() {
        List<String> termResults = userSearchServiceRepository.findTerms();
        return termResults;
    }

    /*------------fetchNeoData method for getting List of UserSearchResult-----------*/
    @Override
    public List<UserSearchResult> fetchNeoData(UserInput userInputRef) {

        userInput.setConcept(userInputRef.getConcept());
        userInput.setDomain(userInputRef.getDomain());
        userInput.setTerm(userInputRef.getTerm());
        logger.debug(userInput.getConcept());
        List<Map<String, Object>> intentResultIndicatorOfRelation = userSearchServiceRepository.getAllIndicatorRelation(userInput.getTerm());
        List<Map<String, Object>> intentResultRelatesRelation = userSearchServiceRepository.getAllRelatesRelation(userInput.getConcept());

        /*exception handling*/
        try {
            if (intentResultRelatesRelation == null) {
                throw new NeoDataNotFetchedException("Empty data in database");
            }
        } catch (NeoDataNotFetchedException e) {
            e.printStackTrace();
        }

			/* To store the data in a arraylist to return url, confident score and description*/
			for(Map<String,Object> indicatorOfResult : intentResultIndicatorOfRelation) {
                for (Map<String,Object> relatesRelationResult : intentResultRelatesRelation) {
                    if (indicatorOfResult.get("intentName").equals(relatesRelationResult.get("intent"))) {

                        for (Map<String, Object> map : intentResultRelatesRelation) {
                            UserSearchResult userSearchResult = new UserSearchResult();
                            userSearchResult.setUrl("" + map.get("url"));
                            userSearchResult.setDescription("" + map.get("description"));
                            userSearchResult.setConfidenceScore((float) Double.parseDouble("" + map.get("confidenceScore")));
                            userSearchResults.add(userSearchResult);
                        }

                    }
                }

            }

        return userSearchResults;
        }

    }





