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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;

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

        //System.out.println(conceptResult.size());
        return conceptResult;
    }



    /*------------fetchTerm method for getting List of TermResult-----------*/
    @Override
    public List<String> fetchTerm() {
        List<String> termResults = userSearchServiceRepository.findTerms();
       // System.out.println(termResults.size());
        return termResults;
    }

    /*------------fetchNeoData method for getting List of UserSearchResult-----------*/
    @Override
    public List<UserSearchResult> fetchNeoData(UserInput userInputRef) {
    	Set<UserSearchResult> s=new HashSet<UserSearchResult>();
    	ArrayList<UserSearchResult> l=new ArrayList<UserSearchResult>();
        userInput.setConcept(userInputRef.getConcept());
        userInput.setDomain(userInputRef.getDomain());
        userInput.setTerm(userInputRef.getTerm());
       
        logger.debug(userInput.getConcept());
       // System.out.println("hai" + userInput.getTerm());
        List<Map<String, Object>> intentResultIndicatorOfRelation = userSearchServiceRepository.getAllIndicatorRelation(userInput.getTerm());
        //System.out.println(intentResultIndicatorOfRelation.size());
       
        //.out.println("" + intentResultRelatesRelation.size());

        /*exception handling*/
        /*try {
            if (intentResultRelatesRelation == null) {
                throw new NeoDataNotFetchedException("Empty data in database");
            }
        } catch (NeoDataNotFetchedException e) {
            e.printStackTrace();
        }*/

			/*exception handling*/
        for(Map<String,Object> result1 : intentResultIndicatorOfRelation) {
			System.out.println(result1.get("intentName"));}
			for(Map<String,Object> result1 : intentResultIndicatorOfRelation) {
				System.out.println(result1.get("intentName")); 
				List<Map<String, Object>> intentResultRelatesRelation = userSearchServiceRepository.getAllRelatesRelation(userInput.getConcept(),""+result1.get("intentName"));
				 for(Map<String,Object> i : intentResultRelatesRelation){
					 //System.out.println(i.get("url"));
					 //System.out.println(i.get("description"));
					 //System.out.println(i.get("title"));
					 //System.out.println(i.get("confidenceScore"));
					 UserSearchResult userSearchResult = new UserSearchResult();
                     userSearchResult.setUrl("" + i.get("url"));
                     userSearchResult.setDescription("" + i.get("description"));
                     userSearchResult.setConfidenceScore((float) Double.parseDouble("" + i.get("confidenceScore")));
                     userSearchResult.setTitle(""+i.get("title"));
                     userSearchResults.add(userSearchResult);

				 }
			}
			Set<String> titles = new HashSet<String>();
			System.out.println(userSearchResults.size());
			for(UserSearchResult userSearchResult:userSearchResults){
				if(titles.add(userSearchResult.getUrl())){
					l.add(userSearchResult);
				}
			}
			//			for(int i=0;i<userSearchResults.size();i++){
//				System.out.println(i);
//				if(l.size() == 0){
//					s.add(userSearchResults.get(i));
//				}
//				else
//				{
//				if(s..g.contains(userSearchResults.get(i))){System.out.println("in if "+userSearchResults.get(i).toString());l.add(userSearchResults.get(i));}else{}
//				for(int j=1;j<userSearchResults.size();j++){
//					
//					if(userSearchResults.get(i).getUrl().equals( userSearchResults.get(j).getUrl())){
//						System.out.println(userSearchResults.get(i).getUrl()+" "+userSearchResults.get(j).getUrl());
//						System.out.println(userSearchResults.get(i).getConfidenceScore()+" "+userSearchResults.get(j).getConfidenceScore());
//						//if(userSearchResults.get(i).getConfidenceScore() >= userSearchResults.get(j).getConfidenceScore()){
//						//float f = userSearchResults.get(i).getConfidenceScore();
//							//System.out.println("if is "+userSearchResults.get(i));
//							s.add(userSearchResults.get(i));
//							//s.add(userSearchResults.get(j));
//							System.out.println("size of set "+s.size());
//						//l.add(userSearchResults.get(j));
//						//userSearchResults.remove(j);
//						}
						//else{
							//System.out.println("else is"+userSearchResults.get(j));
						//s.add(userSearchResults.get(j));
						//System.out.println("size of set "+s.size());
						//l.add(userSearchResults.get(i));
						
						//userSearchResults.remove(i);
						//}
						
						
				//	}
				//}
				
			/*for (Map<String,Object> result2 : intentResultRelatesRelation) {
                    if (result1.get("intentName").equals(result2.get("intent"))) {

                        for (Map<String, Object> map1 : intentResultRelatesRelation) {
                            UserSearchResult userSearchResult = new UserSearchResult();
                            userSearchResult.setUrl("" + map1.get("url"));
                            userSearchResult.setDescription("" + map1.get("description"));
                            userSearchResult.setConfidenceScore((float) Double.parseDouble("" + map1.get("confidenceScore")));
                            userSearchResult.setTitle(""+map1.get("title"));
                            userSearchResults.add(userSearchResult);

                            System.out.println(userSearchResults);


                        }

                    }
*/              //  }

            //}
//			for(Set<UserSearchResult> s1:s)
//			{
//				System.out.println(s1);
//			}
			//System.out.println(s.size());
			
			for(UserSearchResult u:l){
			System.out.println("hi this is url"+u.getUrl());
			System.out.println("hi this is cc "+u.getConfidenceScore());}
        return l;
        }

    }





