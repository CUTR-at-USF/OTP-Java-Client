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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.opentripplanner.api.model.Itinerary;
import org.opentripplanner.api.model.Leg;
import org.opentripplanner.api.ws.Request;
import org.opentripplanner.api.ws.Response;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import de.mastacode.http.Http;

/**
 * Modified by Khoa Tran
 *
 */

public class TripRequest extends AsyncTask<String, Integer, Long> {
	private Response response;
	private static final String TAG = "OTP";
	private ProgressDialog progressDialog;
	private MainActivity mainActivity;

	public TripRequest(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
		progressDialog = new ProgressDialog(mainActivity);
	}

	protected void onPreExecute() {
		progressDialog = ProgressDialog.show(mainActivity, "",
				"Generating trip. Please wait... ", true);
	}

	protected Long doInBackground(String... reqs) {
		int count = reqs.length;
		long totalSize = 0;
		for (int i = 0; i < count; i++) {
			response = requestPlan(reqs[i]);
		}
		return totalSize;
	}

	protected void onPostExecute(Long result) {
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		
		if (response != null && response.getPlan() != null && response.getPlan().itinerary.get(0) != null) {
			
			Log.v(TAG, "Success!!");
		} else {
			// TODO - handle errors here?
			if(response != null && response.getError() != null) {
				String msg = response.getError().getMsg();
				AlertDialog.Builder feedback = new AlertDialog.Builder(mainActivity);
				feedback.setTitle("Error Planning Trip");
				feedback.setMessage(msg);
				feedback.setNeutralButton("OK", null);
				feedback.create().show();
			}
			Log.e(TAG, "No route to display!");
		}
	}
	
	private Response requestPlan(String requestUrl) {			
		HttpClient client = new DefaultHttpClient();
		String result = "";
		try {
			result = Http.get(requestUrl).use(client).header("Accept", "application/xml").header("Keep-Alive","timeout=60, max=100").charset("UTF-8").followRedirects(true).asString();
			Log.d(TAG, "Result: " + result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "Error Http Request: "+e.getMessage());
			e.printStackTrace();
			return null;
		}
		
		Serializer serializer = new Persister();

		Response plan = null;
		try {
			plan = serializer.read(Response.class, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			return null;
		}
		//TODO - handle errors and error responses
		if(plan == null) {
			Log.d(TAG, "No response?");
			return null;
		}
		return plan;
	}
}
