package edu.usf.cutr.opentripplanner.android.serialization;

import java.util.List;

import org.opentripplanner.api.model.Itinerary;

public interface TripRequestCompleteListener {
	public void onTripRequestComplete(String result);
}
