package org.opentripplanner.routing.patch;

import lombok.Getter;
import lombok.Setter;

public class Entry{
	
	@Getter @Setter private String key = null;
	
	@Getter @Setter private String value = null;
	   	
	public Entry(String key, String value){
		this.key = key;
		this.value = key;
	}
	
	public Entry(){
	}
	
	public Entry(String value){
		this.value = value;
	}    	    	
}