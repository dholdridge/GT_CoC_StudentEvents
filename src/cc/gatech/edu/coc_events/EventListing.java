/** 
 * EventListing
 * 
 * A data structure to contain information about a single event.
 * 
 * @author Dan Holdridge
 * @version 1.0
 * 
 */

package cc.gatech.edu.coc_events;

import java.io.Serializable;

/**
 * @author dan
 *
 */
public class EventListing implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8533577364611689860L;
	private String eventName;
	private String location;
	private String time;
	private String description;
	
	/**
	 * Creates a new event
	 * @param eventName Title of event
	 * @param location Locaiton of event
	 * @param time Start time of event
	 * @param description A description of the event
	 */
	public EventListing(String eventName, String location, String time,
			String description) {
		super();
		this.eventName = eventName;
		this.location = location;
		this.time = time;
		this.description = description;
	}
	
	public EventListing() {
		super();
		this.eventName = "";
		this.location = "";
		this.time = "";
		this.description = "";
	}

	/** Gets the eventName
	 * @return Title of the event
	 */
	public String getEventName() {
		return eventName;
	}

	/** Sets the eventName
	 * @param eventName The new event title
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	/** Gets the location
	 * @return The event location
	 */
	public String getLocation() {
		return location;
	}

	/** Sets the location
	 * @param location The new event location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/** Gets the starting time
	 * @return The starting time of the event
	 */
	public String getTime() {
		return time;
	}
	
	/** Sets the starting time
	 * @param time The new starting time
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/** Gets the description
	 * @return A description of the event
	 */
	public String getDescription() {
		return description;
	}

	/** Sets the description
	 * @param description A new description for the event
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@ Override
	public String toString() {
		return eventName + '\n' + location + '\n' + time + '\n' + description + '\n';
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@ Override
	public boolean equals(Object obj){
		if (!(obj instanceof EventListing)) {
			return false;
		}
		EventListing e = (EventListing) obj;
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
