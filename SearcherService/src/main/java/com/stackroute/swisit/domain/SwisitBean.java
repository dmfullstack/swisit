package com.stackroute.swisit.domain;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@JsonIgnoreProperties(ignoreUnknown=true)
public class SwisitBean extends ResourceSupport implements Serializer<SwisitBean>{
	
	@JsonProperty("query")
	String query;
	@JsonProperty("link")
	String url;
	@JsonProperty("title")
	String title;
	@JsonProperty("snippet")
	String description;
	private String encoding="UTF-8";
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public byte[] serialize(String arg0, SwisitBean arg1) {
		// TODO Auto-generated method stub
		byte[] retVal = null;
	    ObjectMapper objectMapper = new ObjectMapper();
	    try {
	      retVal = objectMapper.writeValueAsString(arg1).getBytes();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return retVal;
//		try {
//	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//	        ObjectOutputStream oos = new ObjectOutputStream(baos);
//	        oos.writeObject(arg1);
//	        oos.close();
//	        byte[] b = baos.toByteArray();
//	        return b;
//	    } catch (IOException e) {
//	        return new byte[0];
//	    }
		
		//		int sizeOfName;
//        int sizeOfDesc;
//        byte[] serializedName;
//        //byte[] serializedDesc = null;
//        byte[] serializedLink;
//        byte[] serializedQuery;
//        byte[] desc;
//try {
//	
//    if (arg1 == null)
//        return null;
//                    desc = arg1.getDescription().getBytes(encoding);
//                      //  sizeOfDesc = serializedName.length;
//                        serializedName = arg1.getTitle().getBytes(encoding);
//                        //sizeOfName = serializedDesc.length;
//                        serializedLink=arg1.getUrl().getBytes(encoding);
//                        serializedQuery=arg1.getQuery().getBytes(encoding);
//                        
//                        ByteBuffer buf = ByteBuffer.allocate(1000);
//                        buf.put(serializedName);
//                        buf.put(serializedLink);
//                        //buf.putInt(sizeOfName);
//                        buf.put(serializedQuery);
//                        //buf.putInt(sizeOfDate);
//                        buf.put(desc);
//
//
//        return buf.array();
//
//} catch (Exception e) {
//    throw new SerializationException("Error when serializing Supplier to byte[]"+e);
//}
}
		
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void configure(Map<String, ?> arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}
//	@Override
//	public byte[] serialize(String arg0, SwisitBean arg1) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
//	@Override
//	public byte[] serialize(String arg0, Object arg1) {
//		 try {
//	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//	            ObjectOutputStream oos = new ObjectOutputStream(baos);
//	            oos.writeObject(arg1);
//	            oos.close();
//	            byte[] b = baos.toByteArray();
//	            return b;
//	        } catch (IOException e) {
//	            return new byte[0];
//	        }
//	    }
	}
	
	
	
	
	
	


