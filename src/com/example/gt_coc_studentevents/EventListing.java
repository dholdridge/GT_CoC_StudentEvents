/** A data structure to contain information about a single event.
 * 
 * 
 */

package com.example.gt_coc_studentevents;

import java.io.Serializable;

public class EventListing implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8533577364611689860L;
	private String eventName;
	private String location;
	private String time;
	private String description;
	
	public EventListing(){
		super();
	}
	
	public EventListing(String eventName, String location, String time,
			String description) {
		super();
		this.eventName = eventName;
		this.location = location;
		this.time = time;
		this.description = description;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTime() {
		return time;
	}

	
	
	public void setTime(String time) {
		this.time = time;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String toString() {
		return eventName + '\n' + location + '\n' + time + '\n' + description + '\n';
	}
	
	public boolean isEqual(EventListing e){
		if (! (this.getDescription().equals(e.getDescription() ))) {
			return false;
		} else if (! (this.getEventName().equals(e.getEventName() ))) {
			return false;
		} else if (! (this.getLocation().equals(e.getLocation() ))){
			return false;
		} else if (! (this.getTime().equals(e.getTime() ))){
			return false;
		} else {
			return true;
		}
	}
	

}
