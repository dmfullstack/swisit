package com.stackroute.swisit.hateoes;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import com.stackroute.swisit.controller.SearchController;
import com.stackroute.swisit.domain.QueryBean;
import com.stackroute.swisit.domain.SwisitBean;


@Service
public class LinkHateoesImpl implements LinkHateoes{
	@Override
	public List<SwisitBean> getalllinks(List<SwisitBean> all) {
		// TODO Auto-generated method stub
		 for ( SwisitBean sb : all) {
		        Link postQuery = linkTo(SearchController.class).slash("/urlpostquery").withRel("PostJob");
		        sb.add(postQuery);
		        Link getQuery = linkTo(SearchController.class).slash("/urlgetquery").withRel("GetJob");
		        sb.add(getQuery);
		        Link getAll = linkTo(SearchController.class).slash("/urlget").withSelfRel();
		        sb.add(getAll);
		    }
		 return all;
		}
	
	@Override
	public List<QueryBean> getallquery(List<QueryBean> all) {
		// TODO Auto-generated method stub
		 for ( QueryBean sb : all) {
		        Link postQuery = linkTo(SearchController.class).slash("/urlpostquery").withRel("PostJob");
		        sb.add(postQuery);
		        Link getQuery = linkTo(SearchController.class).slash("/urlgetquery").withRel("GetJob");
		        sb.add(getQuery);
		        Link getAll = linkTo(SearchController.class).slash("/urlget").withRel("GetAll");
		        sb.add(getAll);
		    }
		 return all;
		}

	

}
