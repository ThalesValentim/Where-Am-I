package com.red_folder.phonegap.plugin.backgroundservice.sample.interfaces;

import java.util.Map;

import com.red_folder.phonegap.plugin.backgroundservice.sample.logic.HTTPMethod;

public interface IConfig {
	public HTTPMethod getMethod();
	
	public String getURL();
	
	public Map<String, String> getMap();
	
	public String getAndroidID();
}
