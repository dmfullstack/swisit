package com.stackroute.swisit.documentparser.service;

import com.uttesh.exude.ExudeData;

import com.uttesh.exude.exception.InvalidDataException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.*;
import java.util.Map;


/**
 * Created by user on 30/6/17.
 */
@Service
public class WordCheckerServiceImpl implements WordCheckerService{
	public HashMap<String,List<String>> getWordCheckerByWord(HashMap<String,String> input){
		HashMap<String,List<String>> tockenizedWords = new HashMap<>();
		HashMap<String,String> map = new HashMap<>();
		Iterator<Map.Entry<String, String>> entries = input.entrySet().iterator();
		while(entries.hasNext()) {
			Map.Entry<String, String> entry =  entries.next();
			String key = entry.getKey();
			String inputData = entry.getValue();
			String filteredWords = "";
			String swearWords = "";
			try {

					if (inputData == null ){
						throw new InvalidDataException("Empty input data");
					}
				filteredWords = ExudeData.getInstance().filterStoppings(inputData);
				swearWords = ExudeData.getInstance().getSwearWords(inputData);
			} catch (InvalidDataException e) {

			}
			String filteredSpecialChar = filteredWords.replaceAll("[$_&+,:;=?@#|'<>.-^*()%!]", "");
			List<String> result = new ArrayList<>();
			for (String string : filteredSpecialChar.split(" ")) {
				if(string.isEmpty())
					continue;
					result.add(string.trim());
			}
			tockenizedWords.put(key,result);
		}
		return tockenizedWords;
	}
}
