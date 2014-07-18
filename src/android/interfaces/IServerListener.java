package com.red_folder.phonegap.plugin.backgroundservice.sample.interfaces;

import com.red_folder.phonegap.plugin.backgroundservice.sample.models.ServerModel;

public interface IServerListener {
	
	public void onResponseReceived(ServerModel oldResponse, ServerModel newResponse);

}
