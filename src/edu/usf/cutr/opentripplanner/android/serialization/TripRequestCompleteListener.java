package edu.usf.cutr.opentripplanner.android.serialization;

import org.opentripplanner.v092snapshot.api.ws.Response;

public interface TripRequestCompleteListener {
	public void onTripRequestComplete(String result);
	
	public void onTripBatchRequestComplete(Response[] responses);
}
