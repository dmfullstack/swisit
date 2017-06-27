package com.stackroute.swisit.searcher.intialconsumer;

import com.stackroute.swisit.searcher.domain.SearcherJob;

public interface IntialConsumer {
	public SearcherJob listenmessage(String topic);
}
