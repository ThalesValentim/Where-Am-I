package com.thalesvalentim.phonegap.plugin.whereAmI.sample.interfaces;

import com.thalesvalentim.phonegap.plugin.whereAmI.sample.models.ServerModel;

public interface IServerListener {
	
	public void onResponseReceived(ServerModel oldResponse, ServerModel newResponse);

}
