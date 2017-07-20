/*******This Class is used for Testing the Service only,
 * will finally be removed from the Final product******/

package com.stackroute.swisit.termbank.assembler;
import java.util.List;

import com.stackroute.swisit.termbank.domain.ResponsiveBean;
import com.stackroute.swisit.termbank.domain.TermToIntentRelation;

/*------Interface Class for Assembling Links-----*/
public interface HateoasLinkAssembler{
	public List<TermToIntentRelation> getTerms();
	List<ResponsiveBean> getWords(List<ResponsiveBean> wordsList);
    
}