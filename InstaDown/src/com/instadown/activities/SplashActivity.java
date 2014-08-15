package com.instadown.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.nm.instadown.R;

public class SplashActivity extends Activity{

	private ImageView splashImageView;
    private boolean splashloading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
		this.initSplashScreen();	
    }
    public void initSplashScreen(){

		splashImageView = new ImageView(this);
        splashImageView.setScaleType(ScaleType.FIT_CENTER);        
        splashImageView.setImageResource(R.drawable.ic_launcher);        
        setContentView(splashImageView);
        splashloading = true;
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                splashloading = false;
             // Start main activity
                Intent intent = new Intent(SplashActivity.this, InstaDownActitivy.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();              
            }
        }, 3000);
    }
}
