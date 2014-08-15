package com.instadown.threads;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.instadown.dto.InstaDownTO;
import com.instadown.types.InstaDownType;
import com.instadown.utils.Utils;
import com.instadown.utils.Validations;
import com.nm.instadown.R;

public class ValidateUrlTask extends AsyncTask<String, Integer, String> {

	private Context context;
	private String url;
	private ProgressDialog progress;
	private InstaDownTO instaDown;
	private LinearLayout imagesLayout;	
	public ValidateUrlTask(Context context, String url, LinearLayout imagesLayout, InstaDownTO instaDown) {
		this.context = context;
		this.url = url;
		this.imagesLayout = imagesLayout;
		this.instaDown = instaDown;
	}

	@Override
	protected String doInBackground(String... params) {
		try {
			Document doc = Jsoup.parse(Utils.getHtml(url));
			Elements image = doc.select("meta[property=og:image]");
			Elements video = doc.select("meta[property=og:video]");

			if (Validations.validateIsNotNull(image) & Validations.validateIsNotEmpty(image)) {
				String imageUrl = image.attr("content");
				instaDown.setIGBitmap((Utils.getImageUrl(imageUrl)));
				instaDown.setIGImageUrl(imageUrl);
				instaDown.setInstaDownType(InstaDownType.IMAGE.getCode());
			}
			
			if (Validations.validateIsNotNull(video) && Validations.validateIsNotEmpty(video)) {
				String videoUrl = video.attr("content");				
				instaDown.setIGVideoUrl(videoUrl);
				instaDown.setInstaDownType(InstaDownType.VIDEO.getCode());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		String title = context.getString(R.string.loading);
		String message = context.getString(R.string.pleaseWait);
		if(Validations.validateIsNull(progress)){
			progress = new ProgressDialog(context);
			progress.setTitle(title);
			progress.setMessage(message);
			progress.setCanceledOnTouchOutside(false);
		}
		progress.show();
		
	}
	@Override
	protected void onPostExecute(String result) {	
		super.onPostExecute(result);
		if(Validations.validateIsNotNull(progress))
			progress.dismiss();
		
		if(Validations.validateIsNotNull(instaDown.getIGBitmap())){			
			//Setting bitmap to imageView
			instaDown.getIGImageView().setImageBitmap(Utils.getRoundedCornerBitmap(instaDown.getIGBitmap(),30));
			imagesLayout.setVisibility(View.VISIBLE);
		}
		
	}
}
