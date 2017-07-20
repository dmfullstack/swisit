/*******This Class is used for Testing the Service only,
 * will finally be removed from the Final product******/
package com.stackroute.swisit.termbank.assembler;
import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Link;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import org.springframework.stereotype.Service;

import com.stackroute.swisit.termbank.controller.TermBankRestController;
import com.stackroute.swisit.termbank.domain.ResponsiveBean;
import com.stackroute.swisit.termbank.domain.TermToIntentRelation;

/*-------Implementation to HateoasLinkAssembler interface------*/
@Service
public class HateoasLinkAssemblerImpl implements HateoasLinkAssembler {

	/*------Overriding getTerms method of Interface Class------*/

	@Override
	public List<TermToIntentRelation> getTerms() {
		List sb=new ArrayList();
        Link postQuery = linkTo(TermBankRestController.class).slash("/terms").withSelfRel();
        sb.add(postQuery);
		return null;
	}
	
	/*------Overriding getWords method of Interface Class------*/

	@Override
	public List<ResponsiveBean> getWords(List<ResponsiveBean> wordsList) {
		List sb=new ArrayList();
        Link postQuery = linkTo(TermBankRestController.class).slash("/words").withSelfRel();
        sb.add(postQuery);		return null;
	}

}
