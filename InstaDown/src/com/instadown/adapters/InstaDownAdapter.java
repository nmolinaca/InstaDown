package com.instadown.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.instadown.dto.InstaDownTO;
import com.nm.instadown.R;

public class InstaDownAdapter extends ArrayAdapter<InstaDownTO>{
	private Context context;
	private List<InstaDownTO> instaDownLst;
	
	public InstaDownAdapter(Context context, int layoutResourceId,ArrayList<InstaDownTO> instaDownLst) {
		super(context, R.layout.instadown_list, instaDownLst);
		this.context = context;
		this.instaDownLst = instaDownLst;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		InstaDownToList instaDownToList = null;
		
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(R.layout.instadown_list, parent, false);

			instaDownToList = new InstaDownToList();
			instaDownToList.downloadedDate = (TextView) row.findViewById(R.id.downloadedDate);
			instaDownToList.logo = (ImageView) row.findViewById(R.id.logo);
			row.setTag(instaDownToList);
		} else {
			instaDownToList = (InstaDownToList) row.getTag();
		}

		InstaDownTO instaDown = instaDownLst.get(position);
		instaDownToList.downloadedDate.setText(instaDown.getDownloadedDate().toString());
		instaDownToList.logo.setImageBitmap(instaDown.getIGBitmap());
		return row;
	}
	
	static class InstaDownToList {
		TextView downloadedDate;
		ImageView logo;

	}
}
