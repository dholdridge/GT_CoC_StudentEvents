/** A data structure to contain information about a single event.
 * 
 * 
 */

package com.example.gt_coc_studentevents;

import java.util.Date;

public class EventListing {
	
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
	
	

}
