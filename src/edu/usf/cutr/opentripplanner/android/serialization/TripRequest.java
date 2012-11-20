/*
 * Copyright 2011 Marcy Gordon
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package edu.usf.cutr.opentripplanner.android.serialization;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.opentripplanner.v092snapshot.api.ws.*;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Modified by Khoa Tran
 * 
 */

public class TripRequest extends AsyncTask<String, Integer, Long> {
	private Response[] responses;
	private static final String TAG = "OTP";
	private ProgressDialog progressDialog;
	// private MainActivity mainActivity;
	private Context context;
	private TripRequestCompleteListener callback;
	
	public static final String SUCCESS = "De-serialization SUCCESSFUL!!";
	public static final String FAILURE = "De-serialization ERROR!!";

	public TripRequest(Context context, TripRequestCompleteListener callback) {
		this.context = context;
		this.callback = callback;
		progressDialog = new ProgressDialog(context);		
	}

	protected void onPreExecute() {
		progressDialog = ProgressDialog.show(context, "",
				"Generating trip. Please wait... ", true);
	}

	protected Long doInBackground(String... reqs) {
		int count = reqs.length;
		
		responses = new Response[count];
		long totalSize = 0;
		
		for (int i = 0; i < count; i++) {
			responses[i] = requestPlan(reqs[i]);
		}
		
		return totalSize;
	}

	protected void onPostExecute(Long result) {
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		
		for(int i = 0; i < responses.length; i++){
			if (responses[i] != null && responses[i].getPlan() != null
					&& responses[i].getPlan().getItinerary().get(0) != null) {
				callback.onTripRequestComplete(SUCCESS);
				Log.d(TAG, "Response " + i + ": " + SUCCESS);
			} else {
				// TODO - handle errors here?
				if (responses[i] != null && responses[i].getError() != null) {
					String msg = String.valueOf(responses[i].getError().getId());
					AlertDialog.Builder feedback = new AlertDialog.Builder(context);
					feedback.setTitle("Error Planning Trip");
					feedback.setMessage(msg);
					feedback.setNeutralButton("OK", null);
					feedback.create().show();
				}
				callback.onTripRequestComplete(FAILURE);
				Log.e(TAG, "Response " + i + ": " + FAILURE);
			}
		}
		
		callback.onTripBatchRequestComplete(responses);
			
		
	}

	/**
	 * Makes the actual request to the server and returns the Plan response, or null if there was an error
	 * @param requestUrl full URL for the OTP server request
	 * @return returns the Plan response, or null if there was an error
	 */
	private Response requestPlan(String requestUrl) {

		// HttpClient client = new DefaultHttpClient();
		//String result = "";
		// try {
		// result = Http.get(requestUrl).use(client).header("Accept",
		// "application/json").header("Keep-Alive","timeout=60, max=100").charset("UTF-8").followRedirects(true).asString();
		//
		// Log.d(TAG, "Result: " + result);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// Log.e(TAG, "Error Http Request: "+e.getMessage());
		// e.printStackTrace();
		// return null;
		// }

		HttpURLConnection urlConnection = null;
		URL url = null;
		Response plan = null;

		try {

			url = new URL(requestUrl);

			disableConnectionReuseIfNecessary(); // For bugs in
			// HttpURLConnection
			// pre-Froyo

			// Serializer serializer = new Persister();
			ObjectMapper mapper = new ObjectMapper();
           
		    mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		    mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		    mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
		    mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);

			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestProperty("Accept", "application/json");
	
			// plan = serializer.read(Response.class, result);
			plan = mapper.readValue(urlConnection.getInputStream(),
					Response.class);
			
		} catch (IOException e) {
			Log.e(TAG, "Error fetching JSON or XML: " + e);
			e.printStackTrace();
			// Reset timestamps to show there was an error
			// requestStartTime = 0;
			// requestEndTime = 0;
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}

		// TODO - handle error messages and error responses from OTP server

		return plan;
	}

	/**
	 * Disable HTTP connection reuse which was buggy pre-froyo
	 */
	private void disableConnectionReuseIfNecessary() {
		//if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {  //Should change to this once we update to Android 4.1 SDK
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ECLAIR_MR1) {
			System.setProperty("http.keepAlive", "false");
		}
	}
}
