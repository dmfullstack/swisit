package com.stackroute.swisit.intentparser.assembler;

import com.stackroute.swisit.intentparser.domain.IntentParserResult;

import java.util.List;

public interface HeteoasLinkAssembler{

    public  List<IntentParserResult> calculateConfidence(List<IntentParserResult> results);
}