package com.stackroute.swisit.intentparser.assembler;

import com.stackroute.swisit.intentparser.domain.IntentParserResult;
import java.util.List;

import org.springframework.hateoas.Link;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import org.springframework.stereotype.Service;

@Service
public class HeteoasLinkAssemblerImpl implements HeteoasLinkAssembler {

@Override

    public  List<IntentParserResult> calculateConfidence(List<IntentParserResult> results)
    {
        for(IntentParserResult intentParserResult : results) {
            Link selfLink =linkTo(IntentParserResult.class).slash("intentParserResult.getUrl()").withSelfRel();
            intentParserResult.add(selfLink);
        }
        return results;
    }
}
