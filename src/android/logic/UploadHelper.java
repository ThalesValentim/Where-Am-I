package com.thalesvalentim.phonegap.plugin.whereAmI.sample.logic;

import com.thalesvalentim.phonegap.plugin.whereAmI.sample.exceptions.NotYetImplementedException;
import com.thalesvalentim.phonegap.plugin.whereAmI.sample.models.ServerModel;

import android.util.Log;

public class UploadHelper implements Runnable {

	/*
	 ************************************************************************************************
	 * Static values 
	 ************************************************************************************************
	 */
	private static String TAG = UploadHelper.class.getSimpleName();


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
	private IListener mListener;
	private IUploadModel mModel;

	/*
	 ************************************************************************************************
	 * Constructor 
	 ************************************************************************************************
	 */
	public UploadHelper(IUploadModel model, IListener listener) {
		this.mModel = model;
		this.mListener = listener;
	}

	/*
	 ************************************************************************************************
	 * Fields 
	 ************************************************************************************************
	 */

	/*
	 ************************************************************************************************
	 * Public Methods 
	 ************************************************************************************************
	 */
	@Override
	public void run() {
		Log.d(TAG, "Starting upload");
		ServerModel result = null;
		try {
			result = this.mModel.Upload();
		} catch (NotYetImplementedException e) {
			Log.d(TAG, "NotYetImplementedException hit");
		}

		if (result != null) {
			if (this.mListener != null)
				this.mListener.onUploaded(result);

			Log.d(TAG, "Completed upload");
		} else {
			Log.d(TAG, "Upload failed");
		}
	}

	/*
	 ************************************************************************************************
	 * Private methods 
	 ************************************************************************************************
	 */
	
	/*
	 ************************************************************************************************
	 * Exposed Interfaces 
	 ************************************************************************************************
	 */
	public interface IListener {
		public void onUploaded(ServerModel result);
	}

	public interface IUploadModel {
		public ServerModel Upload() throws NotYetImplementedException;
	}

}
