/* This program is free software: you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public License
 as published by the Free Software Foundation, either version 3 of
 the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>. */
package org.opentripplanner.api.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.opentripplanner.routing.core.Fare;

import android.util.Log;


//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlElementWrapper;

//import org.opentripplanner.routing.core.Fare;

/**
 * An Itinerary is one complete way of getting from the start location to the end location.
 */
public class Itinerary {
	
	private static final String TAG = "OTP";
	
	public Itinerary(){
		
	}

    /**
     * Duration of the trip on this itineraries, in milliseconds.
     */
	
    public long duration = 0;

    /**
     * Time that the trip departs.
     */
	//TODO - fix date parsing
	
	public String startTime = null;
	
//	(name="startTime", required=false)
//	public void setStartTime(GregorianCalendar cal){
//		Log.v(TAG, "in Itinerary/setStartTime");
//	}
//	
//	(name="startTime", required=false)
//	public GregorianCalendar getStartTime(){
//		Log.v(TAG, "in Itinerary/getStartTime");
//		return startTime;
//	}
//    public Date startTime = null;
//    
//    public void setStartTime(Date entry){
//    	SimpleDateFormat parser = 
//    			new SimpleDateFormat("yyyy-MM-d'T'HH:mm:ssZ");
//    	try {
//    		startTime = parser.parse(entry.toString());
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  
//    }
//    
//    public Date getStartTime(){
//    	return startTime;
//    }
    /**
     * Time that the trip arrives.
     */
	
    public String endTime = null;
//    public Date endTime = null;
//    
//    public void setEndTime(Date entry){
//    	SimpleDateFormat parser = 
//    			new SimpleDateFormat("yyyy-MM-d'T'HH:mm:ssZ");
//    	try {
//    		endTime = parser.parse(entry.toString());
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  
//    }
//    
//    public Date getEndTime(){
//    	return endTime;
//    }

    /**
     * How much time is spent walking, in milliseconds.
     */
	
    public long walkTime = 0;
    /**
     * How much time is spent on transit, in milliseconds.
     */
	
    public long transitTime = 0;
    /**
     * How much time is spent waiting for transit to arrive, in milliseconds.
     */
	
    public long waitingTime = 0;

    /**
     * How far the user has to walk, in meters.
     */
	
    public Double walkDistance = 0.0;

    /**
     * How much elevation is lost, in total, over the course of the trip, in meters. As an example,
     * a trip that went from the top of Mount Everest straight down to sea level, then back up K2,
     * then back down again would have an elevationLost of Everest + K2.
     */
	
    public Double elevationLost = 0.0;
    /**
     * How much elevation is gained, in total, over the course of the trip, in meters. See
     * elevationLost.
     */
	
    public Double elevationGained = 0.0;

    /**
     * The number of transfers this trip has.
     */
	
    public Integer transfers = 0;

    /**
     * The cost of this trip
     */
	
    public Fare fare = new Fare();

    /**
     * A list of Legs. Each Leg is either a walking (cycling, car) portion of the trip, or a transit
     * trip on a particular vehicle. So a trip where the use walks to the Q train, transfers to the
     * 6, then walks to their destination, has four legs.
     */
    //@XmlElementWrapper(name = "legs")
    //@XmlElement(name = "leg")
    //@SerializedName("legs")
    
    public List<Leg> legs = new ArrayList<Leg>();

    /**
     * This itineraries has a greater slope than the user requested (but there are no possible 
     * itineraries with a good slope). 
     */
    
    public boolean tooSloped = false;

    /** 
     * adds leg to array list
     * @param leg
     */
    public void addLeg(Leg leg) {
        if(leg != null)
            legs.add(leg);
    }

    /** 
     * remove the leg from the list of legs 
     * @param leg object to be removed
     */
    public void removeLeg(Leg leg) {
        if(leg != null) {
            legs.remove(leg);
        }
    }
    
    public void removeBogusLegs() {
        Iterator<Leg> it = legs.iterator();
        while (it.hasNext()) {
            Leg leg = it.next();
            if (leg.isBogusWalkLeg()) {
                it.remove();
            }
        }
    }
}
