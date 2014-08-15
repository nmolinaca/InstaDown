package com.instadown.threads;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.widget.Toast;

import com.instadown.dto.InstaDownTO;
import com.instadown.types.InstaDownType;
import com.instadown.utils.Utils;
import com.instadown.utils.Validations;
import com.nm.instadown.R;

public class DownloadTask extends AsyncTask<String, Integer, String> {

	private Context context;
	private PowerManager.WakeLock mWakeLock;
	private InstaDownTO instaDown;
	private ProgressDialog mProgressDialog;
	private List<InstaDownTO> instaDownLst;
	public DownloadTask(Context context, ProgressDialog mProgressDialog, InstaDownTO instaDown) {
		this.context = context;
		this.mProgressDialog = mProgressDialog;
		this.instaDown = instaDown;
	}

	@Override
	protected String doInBackground(String... sUrl) {
		InputStream input = null;
		OutputStream output = null;
		HttpURLConnection connection = null;
		int fileLength = 0;
		try {			
			String title = new SimpleDateFormat(context.getString(R.string.imagesNamePattern)).format(new Date());
			
			if(InstaDownType.IMAGE.getCode().equals(instaDown.getInstaDownType())){
				String path = Environment.getExternalStorageDirectory().toString(); // Getting SDCard Path
				String imagesUrl = context.getString(R.string.imagesUrl);
				File fileDirectory = new File(path,imagesUrl);
				if(!fileDirectory.exists())
					fileDirectory.mkdirs();
				
				path += imagesUrl;
				OutputStream fOut = null;
				File file = new File(path, title + ".jpg");
				fOut = new FileOutputStream(file);
				
				instaDown.getIGBitmap().compress(Bitmap.CompressFormat.JPEG, 100, fOut);
				instaDown.setName(title + ".jpg");
				fOut.flush();
				fOut.close();
				// Saving Image
				MediaStore.Images.Media.insertImage(context.getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
				publishProgress((int) (100));
				
			}else{
				// Getting Connection
				connection = Utils.getUrlConnection(connection, instaDown.getIGVideoUrl());
				
				if (Validations.validateIsNotNull(connection)) {
					// this will be useful to display download percentage
					// might be -1: server did not report the length
					fileLength = connection.getContentLength();
				}
				
				String path = Environment.getExternalStorageDirectory().toString(); // Getting SDCard Path
				String videosUrl = context.getString(R.string.videosUrl);
				File fileDirectory = new File(path,videosUrl);
				if(!fileDirectory.exists())
					fileDirectory.mkdirs();
				
				output = new FileOutputStream(path + videosUrl + "/" + title + ".mp4");
				instaDown.setName(title + ".mp4");
				// download the file
				input = connection.getInputStream();

				byte data[] = new byte[4096];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					// allow canceling with back button
					if (isCancelled()) {
						input.close();
						return null;
					}
					total += count;
					// publishing the progress....
					if (fileLength > 0) // only if total length is known
						publishProgress((int) (total * 100 / fileLength));
					output.write(data, 0, count);
				}
			}					
		} catch (Exception e) {
			return e.toString();
		} finally {
			try {
				if (output != null)
					output.close();
				if (input != null)
					input.close();
			} catch (IOException ignored) {
			}

			if (connection != null)
				connection.disconnect();
		}
		return null;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		// take CPU lock to prevent CPU from going off if the user
		// presses the power button during download
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
		mWakeLock.acquire();
		mProgressDialog.show();
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		super.onProgressUpdate(progress);
		// if we get here, length is known, now set indeterminate to false
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setMax(100);
		mProgressDialog.setProgress(progress[0]);
	}

	@Override
	protected void onPostExecute(String result) {
		mWakeLock.release();
		mProgressDialog.dismiss();
		if (result != null){
			Toast.makeText(context, "Download error: " + result,Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
			
			Utils.saveInstaDown(context,instaDown);
		}
	}
}