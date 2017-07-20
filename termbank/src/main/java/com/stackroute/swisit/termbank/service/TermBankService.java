package com.stackroute.swisit.termbank.service;

import java.util.List;

import com.stackroute.swisit.termbank.domain.Input;
import com.stackroute.swisit.termbank.domain.ResponsiveBean;
import com.stackroute.swisit.termbank.domain.TermToIntentRelation;

public interface TermBankService {
	public List<TermToIntentRelation> getTerms();
	List<ResponsiveBean> getWords(String terms);
	public void insertData(Input input);
}
