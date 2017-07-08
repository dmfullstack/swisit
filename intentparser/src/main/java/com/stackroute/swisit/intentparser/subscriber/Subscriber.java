package com.stackroute.swisit.intentparser.subscriber;
/*-------Importing Libraries-------*/
import com.stackroute.swisit.intentparser.domain.DocumentParserResult;

import java.util.List;
/*--------Interface class for Kafka subscriber-------*/
public interface Subscriber {
    public List<DocumentParserResult> receivingMessage(String string);
}
