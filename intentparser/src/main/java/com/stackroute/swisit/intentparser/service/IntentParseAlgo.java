package com.stackroute.swisit.intentparser.service;

import com.stackroute.swisit.intentparser.domain.Intent;
import com.stackroute.swisit.intentparser.domain.CrawlerResult;
import com.stackroute.swisit.intentparser.domain.IntentParserResult;

import java.util.ArrayList;
import java.util.List;

public interface IntentParseAlgo {

    public List<IntentParserResult> calculateConfidence(Iterable<CrawlerResult> intentInput);
    public ArrayList<IntentParserResult> calculateConfidenceScore(CrawlerResult intentParserInput, List<Intent> intentList);
}