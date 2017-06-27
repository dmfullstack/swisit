package com.stackroute.swisit.searcher.hateoes;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import com.stackroute.swisit.searcher.controller.SearchController;
import com.stackroute.swisit.searcher.domain.SearcherJob;
import com.stackroute.swisit.searcher.domain.SearcherResult;


@Service
public class HateoesAssemblerImpl implements HateoesAssembler{
	/* link used in post method */
	@Override
	public List getlinkspost() {
		List sb=new ArrayList();
		Link postQuery = linkTo(SearchController.class).slash("/urlpostquery").withSelfRel();
		sb.add(postQuery);
		        
		Link getAll = linkTo(SearchController.class).slash("/urlget").withRel("PostJob");
		sb.add(getAll);
		    
		 return sb;
		}
	
	/* Hateoas link used in get method */
	@Override
	public List<SearcherResult> getalllinks(List<SearcherResult> all) {
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
	/* Hateoas link used in urlgetquery */
	@Override
	public List<SearcherJob> getallquery(List<SearcherJob> all) {
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
