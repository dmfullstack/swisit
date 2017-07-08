package com.stackroute.swisit.intentparser.service;
/*-------Importing Libraries-------*/
import com.stackroute.swisit.intentparser.domain.Intent;
import com.stackroute.swisit.intentparser.domain.DocumentParserResult;
import com.stackroute.swisit.intentparser.domain.IntentParserResult;

import java.util.ArrayList;
import java.util.List;
/*-------Intent Parser Algorithms Interface Class--------*/
public interface IntentParseAlgo {

    public List<IntentParserResult> calculateConfidence(Iterable<DocumentParserResult> intentInput);
    public ArrayList<IntentParserResult> calculateConfidenceScore(DocumentParserResult intentParserInput, List<Intent> intentList);
}
