package com.instadown.dto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class InstaDownTO {
	private String name;
	private Bitmap IGBitmap;
	private Integer instaDownType;
	private ImageView IGImageView;
	private String IGImageUrl;
	private String IGVideoUrl;

	public String getIGImageUrl() {
		return IGImageUrl;
	}

	public void setIGImageUrl(String iGImageUrl) {
		IGImageUrl = iGImageUrl;
	}

	public String getIGVideoUrl() {
		return IGVideoUrl;
	}

	public void setIGVideoUrl(String iGVideoUrl) {
		IGVideoUrl = iGVideoUrl;
	}

	public Bitmap getIGBitmap() {
		return IGBitmap;
	}

	public void setIGBitmap(Bitmap IGBitmap) {
		this.IGBitmap = IGBitmap;
	}

	
	public Integer getInstaDownType() {
		return instaDownType;
	}

	public void setInstaDownType(Integer instaDownType) {
		this.instaDownType = instaDownType;
	}
	private void writeObject(ObjectOutputStream oos) throws IOException {
		// This will serialize all fields that you did not mark with 'transient'
		// (Java's default behaviour)
		oos.defaultWriteObject();
		// Now, manually serialize all transient fields that you want to be
		// serialized
		if (IGBitmap != null) {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			boolean success = IGBitmap.compress(Bitmap.CompressFormat.PNG, 100,
					byteStream);
			if (success) {
				oos.writeObject(byteStream.toByteArray());
			}
		}
	}

	private void readObject(ObjectInputStream ois) throws IOException,
			ClassNotFoundException {
		// Now, all again, deserializing - in the SAME ORDER!
		// All non-transient fields
		ois.defaultReadObject();
		// All other fields that you serialized
		byte[] image = (byte[]) ois.readObject();
		if (image != null && image.length > 0) {
			IGBitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
		}
	}

	public ImageView getIGImageView() {
		return IGImageView;
	}

	public void setIGImageView(ImageView iGImageView) {
		IGImageView = iGImageView;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
