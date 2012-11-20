/*
 * Copyright 2012 University of South Florida
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package edu.usf.cutr.opentripplanner.android.serialization;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.opentripplanner.api.model.Itinerary;
import org.opentripplanner.api.ws.Request;
import org.opentripplanner.routing.core.OptimizeType;
import org.opentripplanner.routing.core.TraverseMode;
import org.opentripplanner.routing.core.TraverseModeSet;
import org.opentripplanner.v092snapshot.api.ws.Response;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @author Khoa Tran
 *
 */

public class MainActivity extends Activity implements TripRequestCompleteListener{
    /** Called when the activity is first created. */
	private static final String TAG = "OTP";
	
	public static String[] requestUrls;
	
	public static Response[] responses;
	
	public static void setRequestUrl(String[] requestUrl) {
		MainActivity.requestUrls = requestUrl;
	}
	
	public Response[] getResponses() {
		return responses;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    @Override
    protected void onStart (){
    	super.onStart();
//    	String requestUrl = getRequestUrl();
//    	
    	//Walk only
//    	String requestUrl = "http://opentripplanner.usf.edu/opentripplanner-api-webapp/ws/plan?" +
//				"optimize=QUICK&showIntermediateStops=true&time=07:47am&arriveBy=false" +
//				"&wheelchair=false&maxWalkDistance=1600.0" +
//				"&fromPlace=28.033521%2C+-82.520831&toPlace=27.985912%2C+-82.479171" +
//				"&date=06/05/12" +
//				"&mode=WALK,TRAM,SUBWAY,RAIL,BUS,FERRY,CABLE_CAR,GONDOLA,FUNICULAR,TRANSIT,TRAINISH,BUSISH";
    	
    	//Different server
    	//String requestUrl = "http://mapserv.askmorgan.net:8080/opentripplanner-api-webapp/ws/plan?fromPlace=15.600110,32.523766&toPlace=15.570678,32.542649&mode=WALK&min=QUICK&maxWalkDistance=840&time=5:58%20pm&date=7/14/2012&arriveBy=false&itinID=1&wheelchair=false";//getRequestUrl();//"http://opentripplanner.usf.edu/opentripplanner-api-webapp/ws/plan?optimize=QUICK&showIntermediateStops=true&time=02:04pm&arriveBy=false&wheelchair=false&maxWalkDistance=1600.0&fromPlace=28.058984%2C+-82.412473&toPlace=28.011376%2C+-82.390251&date=06/06/12&mode=WALK,TRAM,SUBWAY,RAIL,BUS,FERRY,CABLE_CAR,GONDOLA,FUNICULAR,TRANSIT,TRAINISH,BUSISH";
    	
    	requestUrls = new String[3];
    	
    	//Walk and Transit
    	requestUrls[0] = "http://opentripplanner.usf.edu/opentripplanner-api-webapp/ws/plan?optimize=QUICK&showIntermediateStops=true&fromPlace=28.065041,-82.412778&toPlace=28.022204,-82.431101&mode=TRANSIT,WALK&min=QUICK&maxWalkDistance=840&time=5:32%20pm&date=10/26/2012&arr=Depart&itinID=1&wheelchair=false"; 
		requestUrls[1] = "http://rtp.trimet.org/opentripplanner-api-webapp/ws/plan?optimize=QUICK&showIntermediateStops=true&fromPlace=45.522998,-122.676181&toPlace=45.491479,-122.696266&mode=TRANSIT,WALK&min=QUICK&maxWalkDistance=840&time=5:32%20pm&date=11/26/2012&arr=Depart&itinID=1&wheelchair=false";
		requestUrls[2] = "http://mapserv.askmorgan.net:8080/opentripplanner-api-webapp/ws/plan?fromPlace=15.600110,32.523766&toPlace=15.570678,32.542649&mode=WALK&min=QUICK&maxWalkDistance=840&time=5:58%20pm&date=7/14/2012&arriveBy=false&itinID=1&wheelchair=false";//getRequestUrl();//"http://opentripplanner.usf.edu/opentripplanner-api-webapp/ws/plan?optimize=QUICK&showIntermediateStops=true&time=02:04pm&arriveBy=false&wheelchair=false&maxWalkDistance=1600.0&fromPlace=28.058984%2C+-82.412473&toPlace=28.011376%2C+-82.390251&date=06/06/12&mode=WALK,TRAM,SUBWAY,RAIL,BUS,FERRY,CABLE_CAR,GONDOLA,FUNICULAR,TRANSIT,TRAINISH,BUSISH
    	
    	for(int i = 0; i < requestUrls.length; i++){
    		Log.d(TAG, "Request " + i + ": " + requestUrls[i]);
    	}
		
    	
		new TripRequest(this, this).execute(requestUrls);
    }
    
    private String getRequestUrl(){
		Request request = new Request();
		try {
			request.setFrom(URLEncoder.encode("15.671063,32.469521", "UTF-8"));
			request.setTo(URLEncoder.encode("15.582228,32.571145", "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		request.setArriveBy(false);

		request.setOptimize(OptimizeType.QUICK);
		request.setModes(new TraverseModeSet(TraverseMode.TRANSIT, TraverseMode.WALK));
		
		request.setMaxWalkDistance(1600.00);

		request.setWheelchair(false);

		request.setDateTime(DateFormat.format("MM/dd/yy", System.currentTimeMillis()).toString(), 
				DateFormat.format("hh:mmaa", System.currentTimeMillis()).toString());

		request.setShowIntermediateStops(Boolean.TRUE);
		
		
		
		HashMap<String, String> tmp = request.getParameters();

		Collection c = tmp.entrySet();
		Iterator itr = c.iterator();

		String params = "";
		boolean first = true;
		while(itr.hasNext()){
			if(first) {
				params += "?" + itr.next();
				first = false;
			} else {
				params += "&" + itr.next();						
			}
		}
		
		String baseUrl = "http://opentripplanner.usf.edu/opentripplanner-api-webapp/ws/plan";
//		String baseUrl = "http://mapserv.askmorgan.net:8080/opentripplanner-api-webapp/ws/plan";
		
		String requestUrl = baseUrl + params;
		
		return requestUrl;
	}

	@Override
	public void onTripRequestComplete(String result) {
		// TODO Auto-generated method stub
		Toast toast = Toast.makeText(this.getApplicationContext(), result, Toast.LENGTH_LONG);
		toast.show();
	}
	
	@Override
	public void onTripBatchRequestComplete(Response[] responses) {
		//Batch result of responses for all results is passed here, so save it locally
		this.responses = responses;
		
		int countFailures = 0;
		int countSuccesses = 0;
		
		for (int i = 0; i < responses.length; i++){
			if(responses[i] == null){
				countFailures++;
			}else{
				countSuccesses++;
			}
		}
		
		Log.v(TAG, "Batch Results = " + countSuccesses + " out of " + responses.length + " passed");
	}
}