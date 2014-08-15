package com.instadown.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.instadown.dto.InstaDownTO;
import com.nm.instadown.R;

public class Utils {
	
	public static HttpURLConnection getUrlConnection(HttpURLConnection connection, String link) throws IOException{
		URL url = new URL(link);
        connection = (HttpURLConnection) url.openConnection();
        connection.connect();        
		return connection;
	}
	
	public static String getUrlConnectionState(HttpURLConnection connection) throws IOException{
		// expect HTTP 200 OK, so we don't mistakenly save error report
        // instead of the file
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            return "Server returned HTTP " + connection.getResponseCode()
                    + " " + connection.getResponseMessage();
        }else{
        	return "";
        }
	}
	
	public static String getHtml(String url) throws ClientProtocolException, IOException{
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet http = new HttpGet(url);		
		HttpResponse resp = httpClient.execute(http);		
		return EntityUtils.toString(resp.getEntity());
	}
	/***
	 * 
	 * @param url
	 * @return Bitmap
	 * @throws IOException
	 */
	public static Bitmap getImageUrl(String url) throws IOException {
	    Bitmap x = null;

	    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
	    connection.connect();
	    InputStream input = connection.getInputStream();
	    
	    x = BitmapFactory.decodeStream(input);
	    return x;
	}
	/***
	 * This method Resizes a bitmap
	 * @param bitmap
	 * @param newWidth
	 * @param newHeight
	 * @return Bitmap
	 */
	public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // CREATE A MATRIX FOR THE MANIPULATION
	    Matrix matrix = new Matrix();
	    // RESIZE THE BIT MAP
	    matrix.postScale(scaleWidth, scaleHeight);

	    // "RECREATE" THE NEW BITMAP
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	    return resizedBitmap;
	}
	
	public static void saveImageToGallery(Context context, Bitmap image){
		String title = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		MediaStore.Images.Media.insertImage(context.getContentResolver(),image,title, title.toString());
	}
	
	/***
	 * *
	 * @param actionBar
	 * @param color
	 */
	public static void changeActionBarColor(ActionBar actionBar, String color) {
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));		
		
	}
	public static void showProgressDialog(ProgressDialog progress,Context context, String title, String message){
		if(Validations.validateIsNull(progress)){
			progress = new ProgressDialog(context);
			progress.setTitle(title);
			progress.setMessage(message);
			progress.setCanceledOnTouchOutside(false);
		}
		progress.show();
	}
	public static void hideProgressDialog(ProgressDialog progress){
		if(Validations.validateIsNotNull(progress) && progress.isShowing())
			progress.dismiss();
	}
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
	    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
	            .getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);

	    final int color = 0xff424242;
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	    final RectF rectF = new RectF(rect);
	    final float roundPx = pixels;

	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);

	    return output;
	}
	
	/***
	 * 
	 * @param fileName
	 * @param data
	 * @param context
	 * @throws IOException
	 */
	public static void saveInstaDown(Context context, InstaDownTO instaDownTO){
		FileOutputStream stream = null;
		List<InstaDownTO> data  = null;
		String fileName = getString(context,R.string.INSTADOWN_CACHE_LIST);
		try{
			if(Utils.existFile(fileName, context)){
				context.deleteFile(fileName);
				data = (List<InstaDownTO>) Utils.getArrayListFromCache(fileName, context);
			}else{
				data = new ArrayList<InstaDownTO>();
			}
						
			data.add(instaDownTO);
			
			stream = context.openFileOutput(fileName, Context.MODE_PRIVATE);			   
			ObjectOutputStream  dout = new ObjectOutputStream (stream);
			   
			dout.writeObject(data);
			dout.flush();
			dout.close();			   
			stream.getFD().sync();
			stream.close();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String getString(Context context, int resId){
		return context.getString(resId);
	}
	/***
	 * 
	 * @param fileName
	 * @param context
	 * @return true or false
	 */
	public static Boolean existFile(String fileName, Context context){
		Boolean returnValue = Boolean.TRUE;		
		try {
			context.openFileInput(fileName);
		} catch (FileNotFoundException e) {
			returnValue = Boolean.FALSE;
		}
        
        return returnValue;
		
	}
	/***
	 * This method get the array List from Cache
	 * @param fileName
	 * @param context
	 * @return
	 */
	public static ArrayList<?> getArrayListFromCache(String fileName,Context context){
		ArrayList<?> data = null;
		try{
		    FileInputStream fileIn = context.openFileInput(fileName);		    
	        ObjectInputStream in = new ObjectInputStream(fileIn);
	        
	        data = (ArrayList<?>) in.readObject();	        
	        in.close();	        
	        fileIn.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return data;
	}
	/***
	 * This method clear the cache
	 * @param context
	 */
	public static void clearCache(Context context){
		String[] fileList = context.fileList();
		for (int i = 0; i < fileList.length; i++) {
			context.deleteFile(fileList[i]);
		}
	}
	
	public static String paste(Context context) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context
                    .getSystemService(context.CLIPBOARD_SERVICE);
            return clipboard.getText().toString();
        } else {
            ClipboardManager clipboard = (ClipboardManager) context
                    .getSystemService(Context.CLIPBOARD_SERVICE);

            // Gets a content resolver instance
            ContentResolver cr = context.getContentResolver();

            // Gets the clipboard data from the clipboard
            ClipData clip = clipboard.getPrimaryClip();
            if (clip != null) {

                String text = null;
                String title = null;

                // Gets the first item from the clipboard data
                ClipData.Item item = clip.getItemAt(0);

                // Tries to get the item's contents as a URI pointing to a note
                Uri uri = item.getUri();

                // If the contents of the clipboard wasn't a reference to a
                // note, then
                // this converts whatever it is to text.
                if (text == null) {
                    text = coerceToText(context, item).toString();
                }

                return text;
            }
        }
        return "";
    }
	
	public static CharSequence coerceToText(Context context, ClipData.Item item) {
        // If this Item has an explicit textual value, simply return that.
        CharSequence text = item.getText();
        if (text != null) {
            return text;
        }

        // If this Item has a URI value, try using that.
        Uri uri = item.getUri();
        if (uri != null) {

            // First see if the URI can be opened as a plain text stream
            // (of any sub-type). If so, this is the best textual
            // representation for it.
            FileInputStream stream = null;
            try {
                // Ask for a stream of the desired type.
                AssetFileDescriptor descr = context.getContentResolver()
                        .openTypedAssetFileDescriptor(uri, "text/*", null);
                stream = descr.createInputStream();
                InputStreamReader reader = new InputStreamReader(stream,
                        "UTF-8");

                // Got it... copy the stream into a local string and return it.
                StringBuilder builder = new StringBuilder(128);
                char[] buffer = new char[8192];
                int len;
                while ((len = reader.read(buffer)) > 0) {
                    builder.append(buffer, 0, len);
                }
                return builder.toString();

            } catch (FileNotFoundException e) {
                // Unable to open content URI as text... not really an
                // error, just something to ignore.

            } catch (IOException e) {
                // Something bad has happened.
                Log.w("ClippedData", "Failure loading text", e);
                return e.toString();

            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                    }
                }
            }

            // If we couldn't open the URI as a stream, then the URI itself
            // probably serves fairly well as a textual representation.
            return uri.toString();
        }

        // Finally, if all we have is an Intent, then we can just turn that
        // into text. Not the most user-friendly thing, but it's something.
        Intent intent = item.getIntent();
        if (intent != null) {
            return intent.toUri(Intent.URI_INTENT_SCHEME);
        }

        // Shouldn't get here, but just in case...
        return "";
    }
}
