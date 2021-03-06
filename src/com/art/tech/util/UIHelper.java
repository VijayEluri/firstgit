package com.art.tech.util;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

public class UIHelper {

	public static Uri capureImage(Activity activity, int actionCode, String saveLocation) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String cameraPicName = System.currentTimeMillis() + ".jpg";
		
		File photofile = FileUtils.createFile(saveLocation, cameraPicName);
		
		Uri uri = Uri.fromFile(photofile);
		Log.d("debug capureImage uri path : ", uri.getPath());
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		
		activity.startActivityForResult(intent, actionCode);
		return uri;
	}
	
	public static Uri capureImage(Fragment f, int actionCode, String saveLocation) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String cameraPicName = System.currentTimeMillis() + ".jpg";
		
		File photofile = FileUtils.createFile(saveLocation, cameraPicName);
		
		Uri uri = Uri.fromFile(photofile);
		Log.d("debug capureImage uri path : ", uri.getPath());
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		
		f.startActivityForResult(intent, actionCode);
		return uri;
	}
}
