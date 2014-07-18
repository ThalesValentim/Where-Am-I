package com.red_folder.phonegap.plugin.backgroundservice.sample;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONArray;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import com.red_folder.phonegap.plugin.backgroundservice.BackgroundService;
import com.red_folder.phonegap.plugin.backgroundservice.sample.exceptions.NotYetImplementedException;
import com.red_folder.phonegap.plugin.backgroundservice.sample.interfaces.IConfig;
import com.red_folder.phonegap.plugin.backgroundservice.sample.logic.Controller;
import com.red_folder.phonegap.plugin.backgroundservice.sample.logic.HTTPMethod;
import com.red_folder.phonegap.plugin.backgroundservice.sample.logic.ResultFactory;
import com.red_folder.phonegap.plugin.backgroundservice.sample.models.LocationModel;
import com.red_folder.phonegap.plugin.backgroundservice.sample.models.ServiceConfig;

public class MyService extends BackgroundService implements Controller.IControllerListener {

	/*
	 ************************************************************************************************
	 * Static values 
	 ************************************************************************************************
	 */
	private final static String TAG = MyService.class.getSimpleName();
	
	
	/*
	 ************************************************************************************************
	 * Keys 
	 ************************************************************************************************
	 */

	
	/*
	 ************************************************************************************************
	 * Internal Data 
	 ************************************************************************************************
	 */
	Controller mController = null;
	
	/*
	 ************************************************************************************************
	 * Constructor 
	 ************************************************************************************************
	 */

	
	/*
	 ************************************************************************************************
	 * Fields 
	 ************************************************************************************************
	 */
	public HTTPMethod getMethod() {
		return HTTPMethod.GET;
	}
	
	public void setMethod(HTTPMethod method) throws NotYetImplementedException {
		throw new NotYetImplementedException();
	}
	
	public String getURL() {
		return "http://www.thalesvalentim.com.br/webserver/gps/post.php";
	}
	public String getAndroidID() {
		return Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
	}
	
	public void setURL(String url) throws NotYetImplementedException {
		throw new NotYetImplementedException();
	}
	
	public Map<String, String> getMap() {
		Map<String, String> result = new HashMap<String, String>();
		
		result.put(LocationModel.MAP_KEY_HEADING, "heading");
		result.put(LocationModel.MAP_KEY_ALTITUDE, "altitude");
		result.put(LocationModel.MAP_KEY_LATITUDE, "latitude");
		result.put(LocationModel.MAP_KEY_ACCURACY, "accuracy");
		result.put(LocationModel.MAP_KEY_LONGITUDE, "longitude");
		result.put(LocationModel.MAP_KEY_SPEED, "speed");
		result.put(LocationModel.MAP_KEY_ALTITUDE_ACCURACY, "altitudeAccuracy");
		result.put(LocationModel.MAP_KEY_TIMESTAMP, "timestamp");
		
		return result;
	}

	public void setMap(Map<String, String> map) throws NotYetImplementedException {
		throw new NotYetImplementedException();
	}
	
	public IConfig getServiceConfig() {
		return new ServiceConfig(this.getMethod(), this.getURL(), this.getMap(), this.getAndroidID());
	}

	/*
	 ************************************************************************************************
	 * Service functions
	 * Overriden from the BackgroundService 
	 ************************************************************************************************
	 */
	@Override
	public void onStart(Intent intent, int startId) {
		Log.i(TAG, "Service started");       
		super.onStart(intent, startId);
	}
	
	private void initialiseController() {
		this.mController = new Controller(this, this, this.getServiceConfig());
		
		this.mController.start();
	}
	
	@Override
	protected JSONObject initialiseLatestResult() {
		return ResultFactory.getResult();
	}
	
	@Override
	protected JSONObject doWork() {
		Log.d(TAG, "Started DoWork");
		if (this.mController == null)
			initialiseController();
		
		Log.d(TAG, "Calling Controller");
		this.mController.onTimer();
		if(newPlace()){
			showNotification("Nova Mensagem", "Você está perto de " +placeName);
		}
		if(this.mController.enviarNotification()){
		showNotification("Nova Mensagem", "Clique aqui!");
		}

		return this.mController.getResult();
	}

	@Override
	protected void onTimerEnabled() {
		initialiseController();
	}
	
	@Override
	protected void onTimerDisabled() {
		if (this.mController != null) {
			this.mController.stop();
			this.mController = null;
		}
	}

	@Override
	protected JSONObject getConfig() {
		return null;
	}

	@Override
	protected void setConfig(JSONObject arg0) {
		
	}

	@Override
	public void onNewTimerInterval(int milliseconds) {
		// Called by the controller when the server has requested we change the timer interval
		// There is overhead in changing, so only call if different
		if (this.getMilliseconds() != milliseconds) {
			this.setMilliseconds(milliseconds);
			this.restartTimer();
		}
	}

	@Override
	public void onLatestResult(JSONObject result) {
		this.setLatestResult(result);
		
	}
	public String placeName;
	public String placeID;
	public String oldPlaceID;
	public String placeNameDescription;
	
	private Boolean newPlace() {
		Boolean result = false;
		HttpClient httpClient;
		HttpGet getMethod;
		HttpResponse response;
		InputStream responseStream;
		try {
			httpClient = new DefaultHttpClient();
			getMethod = new HttpGet("http://www.thalesvalentim.com.br/webserver/gps/getPlaces.php?userid=" + getAndroidID());
			response = httpClient.execute(getMethod);
			responseStream = response.getEntity().getContent();
		
			StringBuilder dataString = new StringBuilder();
			char[] buffer = new char[1024];
			Reader reader = new InputStreamReader(responseStream, "UTF-8");
			int charCount;
			while ((charCount = reader.read(buffer, 0, buffer.length)) > 0){
				dataString.append(buffer, 0, charCount);
			}
			
			JSONArray data = new JSONArray(dataString.toString());
			if(!(data.toString().equals("{}"))){
				if(data.length() > 0){
					result = true;
				}
				if(oldPlaceID == null){
					oldPlaceID = "789841";
				}
				Log.d(TAG ,"oldPlaceID: "+oldPlaceID);
				Log.d(TAG ,"placeID: "+placeID);
				for(int i=0;i<data.length();i++){
					JSONObject json_data = data.getJSONObject(i);
					placeName = json_data.getString("Nome");
					placeID = json_data.getString("id");
					placeNameDescription = json_data.getString("Nome");
					Log.d(TAG ,"Nome: "+json_data.getString("Nome"));
					Log.d(TAG ,"Descricao: "+json_data.getString("Descricao"));
					if(!oldPlaceID.equals(placeID)){
						Log.d(TAG ,"oldPlaceID: "+oldPlaceID);
						Log.d(TAG ,"placeID: "+placeID);
						oldPlaceID = placeID;
						result = true;
					}else{
						result = false;
					}
				}
			}

		} catch (Exception ex) {
			// Do something with the error in production code
			Log.d(TAG, "ERROR");
			Log.d(TAG, ex.getMessage());
		} finally {
			// Close out the response stream and any open connections in production code
		}
		
		return result;
	}
	
	public void showNotification( String contentTitle, String contentText ) {
		int icon = android.R.drawable.btn_star_big_on;
        long when = System.currentTimeMillis();
        
        Notification notification = new Notification(icon, contentTitle, when);
		
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
		Intent notificationIntent = new Intent(this, MyService.class);
		
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(this, contentTitle, contentText, contentIntent);
        
        NotificationManager nm = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1, notification);
	}

	
}
