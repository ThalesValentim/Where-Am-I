package com.thalesvalentim.phonegap.plugin.whereAmI.sample.interfaces;

import java.util.Map;

import com.thalesvalentim.phonegap.plugin.whereAmI.sample.logic.HTTPMethod;

public interface IConfig {
	public HTTPMethod getMethod();
	
	public String getURL();
	
	public Map<String, String> getMap();
	
	public String getAndroidID();
}
