package com.instadown.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.instadown.dto.InstaDownTO;
import com.instadown.threads.DownloadTask;
import com.instadown.threads.ValidateUrlTask;
import com.instadown.utils.CommonProperties;
import com.instadown.utils.Utils;
import com.nm.instadown.R;

public class InstaDownActitivy extends ActionBarActivity {
	private ProgressDialog mProgressDialog;
	private EditText urlTxt;
	private LinearLayout imagesLinear;
	private InstaDownTO instaDownTO;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        urlTxt = (EditText) findViewById(R.id.urlTxt);
        
        instaDownTO = new InstaDownTO();        
        imagesLinear = (LinearLayout) findViewById(R.id.imagesLayout);
        instaDownTO.setIGImageView((ImageView) findViewById(R.id.imageFromIG));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        
        Utils.changeActionBarColor(getActionBar(),CommonProperties.ACTION_BAR_COLOR);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onInstaDownload(View v){
    	mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage(getString(R.string.downloading));
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setCanceledOnTouchOutside(false);
		// execute this when the downloader must be fired
		final DownloadTask downloadTask = new DownloadTask(this,mProgressDialog,instaDownTO);
		downloadTask.execute();
		
		mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
		    @Override
		    public void onCancel(DialogInterface dialog) {
		        downloadTask.cancel(true);
		    }
		});
    }
    
    public void paste(View v){
    	urlTxt.setText(Utils.paste(this));
    	urlTxt.setFocusable(false);
    	
    	ValidateUrlTask urlTask = new ValidateUrlTask(this,urlTxt.getText().toString(), this.imagesLinear,this.instaDownTO);
    	urlTask.execute();
    }
}


