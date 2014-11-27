package com.art.tech.fragment;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.art.tech.R;
import com.art.tech.db.DBHelper;
import com.art.tech.db.ImageCacheColumn;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ScanFragment extends Fragment {

	private static final String TAG = "ImageGridFragment";
	private static final int MSG_QUERY_IMAGE = 1;
	
	List<String> imageUrls = new LinkedList<String>();

	DisplayImageOptions options;
	private ImageAdapter imageAdapter;
	private GridView gridView;
	private Handler uiHandler;

	public void addImageUrl(String url) {
		if (imageUrls != null)
			imageUrls.add(url);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.detail_view_container, container,
				false);

		return rootView;
	}
	
	public interface AsyncListener {
		void updateImageUrls(Cursor c);
	}
	
	private class UiHandler extends Handler {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_QUERY_IMAGE:
				if (imageAdapter != null)
					imageAdapter.notifyDataSetChanged();
				break;
			}
		}
	}
	
	private class ImageQueryThread extends Thread {
		private WeakReference<Handler> weakHandler;
		ImageQueryThread(Handler h) {
			weakHandler = new WeakReference<Handler>(h);
		}
		
		@Override
		public void run() {
			String columns[] = { ImageCacheColumn.Url };
			DBHelper helper = DBHelper.getInstance(ScanFragment.this.getActivity());
			Cursor c = helper.query(ImageCacheColumn.TABLE_NAME, columns, null,
					null);
			if (c != null && c.moveToFirst()) {
				do {
					ScanFragment.this.imageUrls.add("file://"
							+ new File(c.getString(c
									.getColumnIndex(ImageCacheColumn.Url)))
									.getAbsolutePath());
				} while (c.moveToNext());
				c.close();
				
				if (weakHandler.get() != null) {
					weakHandler.get().sendEmptyMessage(MSG_QUERY_IMAGE);
				}				
			}
		}
	}
		
	private void asyncQueryGalleryFirstImage() {
		int token = 0;
		Object cookie = new AsyncListener() {
			@Override
			public void updateImageUrls(Cursor c) {
				if (c != null && c.moveToFirst()) {
					do {
						imageUrls.add("file://"
								+ new File(c.getString(c
										.getColumnIndex(ImageCacheColumn.Url)))
										.getAbsolutePath());
					} while (c.moveToNext());
				}
			}
		};
		
		String columns[] = { ImageCacheColumn.Url } ;
		String selection = null;
		String[] selectionArgs = null;
		String sortOrder = null;
				
		ImageCacheColumn.asyncQuery(getActivity(), token, cookie, ImageCacheColumn.CONTENT_URI, 
				columns, selection, selectionArgs, sortOrder);
	}

	private void initImageUrls() {
		//getImageListFromDB();
		//asyncQueryGalleryFirstImage();
		new ImageQueryThread(uiHandler).start();

	}

	private void getImageListFromDB() {
		String columns[] = { ImageCacheColumn.Url };
		DBHelper helper = DBHelper.getInstance(getActivity());
		Cursor c = helper.query(ImageCacheColumn.TABLE_NAME, columns, null,
				null);
		if (c != null && c.moveToFirst()) {
			do {
				imageUrls.add("file://"
						+ new File(c.getString(c
								.getColumnIndex(ImageCacheColumn.Url)))
								.getAbsolutePath());
			} while (c.moveToNext());
			c.close();
		}
	}

	public class ImageAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		ImageAdapter() {
			inflater = LayoutInflater.from(getActivity());
		}

		@Override
		public int getCount() {
			return imageUrls.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View view = convertView;
			if (view == null) {
				view = inflater
						.inflate(R.layout.item_grid_image, parent, false);
				holder = new ViewHolder();
				assert view != null;
				holder.imageView = (ImageView) view.findViewById(R.id.image);
				holder.progressBar = (ProgressBar) view
						.findViewById(R.id.progress);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			ImageLoader.getInstance().displayImage(imageUrls.get(position),
					holder.imageView, options,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							holder.progressBar.setProgress(0);
							holder.progressBar.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							holder.progressBar.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							holder.progressBar.setVisibility(View.GONE);
						}
					}, new ImageLoadingProgressListener() {
						@Override
						public void onProgressUpdate(String imageUri,
								View view, int current, int total) {
							holder.progressBar.setProgress(Math.round(100.0f
									* current / total));
						}
					});

			return view;
		}
	}

	static class ViewHolder {
		ImageView imageView;
		ProgressBar progressBar;
	}
}