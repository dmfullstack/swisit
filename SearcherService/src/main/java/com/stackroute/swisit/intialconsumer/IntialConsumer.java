package com.stackroute.swisit.intialconsumer;

import com.stackroute.swisit.domain.SearcherJob;

public interface IntialConsumer {
	public SearcherJob listenmessage(String topic);
}
