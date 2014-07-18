package com.thalesvalentim.phonegap.plugin.whereAmI.sample.models;

import java.util.Map;

import com.thalesvalentim.phonegap.plugin.whereAmI.sample.interfaces.IConfig;
import com.thalesvalentim.phonegap.plugin.whereAmI.sample.logic.HTTPMethod;

public class ServiceConfig implements IConfig{

	private HTTPMethod mMethod;
	private String mURL;
	private Map<String, String> mMap;
	private String mAndroidID;
	
	public ServiceConfig(HTTPMethod method, String url, Map<String, String> map, String androidid) {
		this.mMethod = method;
		this.mURL = url;
		this.mMap = map;
		this.mAndroidID = androidid;
	}
	
	@Override
	public HTTPMethod getMethod() {
		return this.mMethod;
	}

	@Override
	public String getURL() {
		return this.mURL;
	}

	@Override
	public Map<String, String> getMap() {
		return this.mMap;
	}
	
	@Override
	public String getAndroidID() {
		return this.mAndroidID;
	}
	
}
