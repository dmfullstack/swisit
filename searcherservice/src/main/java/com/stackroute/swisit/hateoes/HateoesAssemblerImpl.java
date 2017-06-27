package com.stackroute.swisit.hateoes;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import com.stackroute.swisit.controller.SearchController;
import com.stackroute.swisit.domain.SearcherJob;
import com.stackroute.swisit.domain.SearcherResult;


@Service
public class HateoesAssemblerImpl implements HateoesAssembler{
	
	@Override
	public List getlinkspost() {
		// TODO Auto-generated method stub
		List sb=new ArrayList();
		        Link postQuery = linkTo(SearchController.class).slash("/urlpostquery").withSelfRel();
		        sb.add(postQuery);
		        
		        Link getAll = linkTo(SearchController.class).slash("/urlget").withRel("PostJob");
		        sb.add(getAll);
		    
		 return sb;
		}
	
	
	@Override
	public List<SearcherResult> getalllinks(List<SearcherResult> all) {
		// TODO Auto-generated method stub
		 for ( SearcherResult sb : all) {
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
	public List<SearcherJob> getallquery(List<SearcherJob> all) {
		// TODO Auto-generated method stub
		 for ( SearcherJob sb : all) {
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
