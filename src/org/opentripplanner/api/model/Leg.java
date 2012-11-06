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

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.opentripplanner.routing.core.TraverseMode;
import org.opentripplanner.routing.patch.Alerts;

/**
 * One leg of a trip -- that is, a temporally continuous piece of the journey that takes place on a
 * particular vehicle (or on foot).
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
public class Leg {
	
	private static final String TAG = "OTP";
	
	public Leg() {
	}

    /**
     * The date and time this leg begins.
     */
	//TODO - fix dates
	
    public String startTime = null;
//	public Date startTime = null;
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
     * The date and time this leg ends.
     */
	
    public String endTime = null;
//	public Date endTime = null;
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
     * The distance traveled while traversing the leg in meters.
     */
	
    public Double distance = null;

    /**
     * The mode (e.g., <code>Walk</code>) used when traversing this leg.
     */
    //@XmlAttribute
	//TODO - fix mode
    //public String mode = TraverseMode.WALK.toString();
	
	public String mode = "";
	
    /**
     * For transit legs, the route of the bus or train being used. For non-transit legs, the name of
     * the street being traversed.
     */
    //@XmlAttribute
    
    public String route = "";
    
    
    public String agencyName;
    
    
    public String agencyUrl;
    
    
    public int agencyTimeZoneOffset;
    
    /**
     * For transit leg, the route's (background) color (if one exists). For non-transit legs, null.
     */
    
    public String routeColor = null;

    /**
     * For transit leg, the route's text color (if one exists). For non-transit legs, null.
     */
    
    public String routeTextColor = null;

    /**
     * For transit legs, if the rider should stay on the vehicle as it changes route names.
     */
    
    public Boolean interlineWithPreviousLeg;

    /**
     * For transit leg, the trip's short name (if one exists). For non-transit legs, null.
     */
    //@XmlAttribute
    
    public String tripShortName = null;

    /**
     * For transit legs, the headsign of the bus or train being used. For non-transit legs, null.
     */
    //@XmlAttribute
    
    public String headsign = null;

    /**
     * For transit legs, the ID of the transit agency that operates the service used for this leg.
     * For non-transit legs, null.
     */
    //@XmlAttribute
    
    public String agencyId = null;
    
    
    public String tripId = null;
    
    /**
     * The Place where the leg originates.
     */
    
    public Place from = null;
    
    /**
     * The Place where the leg begins.
     */
    
    public Place to = null;

    /**
     * For transit legs, intermediate stops between the Place where the leg originates and the Place where the leg ends.
     * For non-transit legs, null.
     * This field is optional i.e. it is always null unless "showIntermediateStops" parameter is set to "true" in the planner request.
     */
    //@XmlElementWrapper(name = "intermediateStops")
    //@SerializedName("intermediateStops")
    
    @Getter @Setter public List<Place> intermediateStops;
    
    /**
     * The leg's geometry.
     */
    
    public EncodedPolylineBean legGeometry;
    
    /**
     * A series of turn by turn instructions used for walking, biking and driving. 
     */
    //@XmlElementWrapper(name = "steps")
    //@SerializedName("steps")
    
    @Getter @Setter private List<WalkStep> steps;

    /**
     * Deprecated field formerly used for notes -- will be removed.  See
     * alerts
     */

    @Getter @Setter private ArrayList<Note> notes;

    
    @Getter @Setter private ArrayList<Alerts> alerts;

    
	public String routeShortName;

    
	public String routeLongName;

    
    public String boardRule;

    
    public String alightRule;
    
    
    public Boolean rentedBike;
    
    @Getter @Setter public Boolean bogusNonTransitLeg;

//    /**
//     * bogus walk/bike/car legs are those that have 0.0 distance, 
//     * and just one instruction
//     * 
//     * @return boolean true if the leg is bogus 
//     */
//    public boolean isBogusNonTransitLeg() {
//        boolean retVal = false;
//        if( (TraverseMode.WALK.toString().equals(this.mode) ||
//             TraverseMode.CAR.toString().equals(this.mode) ||
//             TraverseMode.BICYCLE.toString().equals(this.mode)) &&
//            (this.walkSteps == null || this.walkSteps.size() <= 1) && 
//            this.distance == 0) {
//            retVal = true;
//        }
//        return retVal;
//    }
    
    /** 
     * The leg's duration in milliseconds
     */
//    
//    public long getDuration() {
//        return 0;//endTime.getTime() - startTime.getTime();
//    }
    
    public long duration;
    
    public void addNote(Note note) {
//    	Log.v(TAG, note.text);
        if (note == null) {
            notes = new ArrayList<Note>();
        }
        if (alerts == null) {
            alerts = new ArrayList<Alerts>();
        }
        
        if (!notes.contains(note)) {
            notes.add(note);
        }
    }

    public void addAlert(Alerts alert) {
        if (notes == null) {
            notes = new ArrayList<Note>();
        }
        if (alerts == null) {
            alerts = new ArrayList<Alerts>();
        }
        String text = alert.getAlertHeaderText().getSomeTranslation();
        if (text == null) {
            text = alert.getAlertHeaderText().getSomeTranslation();
        }
        if (text == null) {
            text = alert.alertUrl.getSomeTranslation();
        }
        
        if (!notes.contains(text)) {
            notes.add(new Note(text));
        }
        if (!alerts.contains(alert)) {
            alerts.add(alert);
        }
    }
    
    
    /**
     * bogus walk legs are those that have 0.0 distance, and just one instruction 
     * @return boolean true if the leg is bogus 
     */
    public boolean isBogusWalkLeg() {
        boolean retVal = false;
//        if( TraverseMode.WALK.toString().equals(this.mode)         &&
//            (this.walkSteps == null || this.walkSteps.size() <= 1) && 
//            this.distance == 0) {
//            retVal = true;
//        }
        return retVal;
    }
}