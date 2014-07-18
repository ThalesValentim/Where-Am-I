package com.thalesvalentim.phonegap.plugin.whereAmI.sample.models;

import java.util.Map;

import com.thalesvalentim.phonegap.plugin.whereAmI.sample.exceptions.NotYetImplementedException;
import com.thalesvalentim.phonegap.plugin.whereAmI.sample.interfaces.IConfig;
import com.thalesvalentim.phonegap.plugin.whereAmI.sample.logic.TransportStrategy;
import com.thalesvalentim.phonegap.plugin.whereAmI.sample.logic.UploadHelper;

public class UploadModel implements UploadHelper.IUploadModel, TransportStrategy.ITransportData {

	/*
	 ************************************************************************************************
	 * Static values 
	 ************************************************************************************************
	 */
	private final static String TAG = UploadModel.class.getSimpleName();

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
	private IConfig mConfig = null;
	private LocationModel mModel = null;
	
	/*
	 ************************************************************************************************
	 * Constructor 
	 ************************************************************************************************
	 */
	public UploadModel(IConfig config, LocationModel model) {
		this.mConfig = config;
		this.mModel = model;
	}


	/*
	 ************************************************************************************************
	 * Fields 
	 ************************************************************************************************
	 */
	public LocationModel getLocation() {
		return this.mModel;
	}

	/*
	 ************************************************************************************************
	 * Public Methods 
	 ************************************************************************************************
	 */

	/*
	 ************************************************************************************************
	 * Private methods 
	 ************************************************************************************************
	 */

	/*
	 ************************************************************************************************
	 * Implemented method 
	 ************************************************************************************************
	 */

	@Override
	public ServerModel Upload() throws NotYetImplementedException {
		return TransportStrategy.save(this.mConfig.getMethod(), this.mConfig.getURL(), this.mConfig.getMap(), this.mConfig.getAndroidID(), this);
	}


	@Override
	public String getQueryString(Map<String, String> map, String androidid) {
		
		return this.mModel.getQueryString(map, androidid);
	}

}
