package com.art.tech.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


public class ImageGridFragment extends Fragment {

	public static final String[] IMAGES = new String[] {
		// Heavy images
		"file://sdcard/image01.jpg",
		"file://sdcard/image02.jpg",
		"file://sdcard/image03.jpg",
		"file://sdcard/image01.jpg",
		"file://sdcard/image02.jpg",
		"file://sdcard/image03.jpg",
		"file://sdcard/image01.jpg",
		"file://sdcard/image02.jpg",
		"file://sdcard/image03.jpg",
		"file://sdcard/image01.jpg",
		"file://sdcard/image02.jpg",
		"file://sdcard/image03.jpg",
		"file://sdcard/image01.jpg",
		"file://sdcard/image02.jpg",
		"file://sdcard/image03.jpg",
		"file://sdcard/image01.jpg",
		"file://sdcard/image02.jpg",
		"file://sdcard/image03.jpg",
		"file://sdcard/image01.jpg",
		"file://sdcard/image02.jpg",
		"file://sdcard/image03.jpg",
		"file://sdcard/image01.jpg",
		"file://sdcard/image02.jpg",
		"file://sdcard/image03.jpg",
		"file://sdcard/image01.jpg",
		"file://sdcard/image02.jpg",
		"file://sdcard/image03.jpg",
		"file://sdcard/image01.jpg",
		"file://sdcard/image02.jpg",
		"file://sdcard/image03.jpg",
		"file://sdcard/image01.jpg",
		"file://sdcard/image02.jpg",
		"file://sdcard/image03.jpg",
		"file://sdcard/image01.jpg",
		"file://sdcard/image02.jpg",
		"file://sdcard/image03.jpg",
		"file://sdcard/image01.jpg",
		"file://sdcard/image02.jpg",
		"file://sdcard/image03.jpg",
		"file://sdcard/image01.jpg",
		"file://sdcard/image02.jpg",
		"file://sdcard/image03.jpg",
		"file://sdcard/image01.jpg",
		"file://sdcard/image02.jpg",
		"file://sdcard/image03.jpg",
		"file://sdcard/image01.jpg",
		"file://sdcard/image02.jpg",
		"file://sdcard/image03.jpg",
		"file://sdcard/image04.jpg"
	};


	protected static final String TAG = "ImageGridFragment";
	
	String[] imageUrls = IMAGES;

	DisplayImageOptions options;

	private GridView gridView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(com.art.tech.R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		
		
		
	}
	
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fr_image_grid, container, false);
		gridView = (GridView) rootView.findViewById(R.id.grid);
		((GridView) gridView).setAdapter(new ImageAdapter());
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.v(TAG, "position " + position + " id : " + id);
			}
		});
		return rootView;
	}

	public class ImageAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		ImageAdapter() {
			inflater = LayoutInflater.from(getActivity());
		}

		@Override
		public int getCount() {
			return imageUrls.length;
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
				view = inflater.inflate(R.layout.item_grid_image, parent, false);
				holder = new ViewHolder();
				assert view != null;
				holder.imageView = (ImageView) view.findViewById(R.id.image);
				holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			ImageLoader.getInstance()
					.displayImage(imageUrls[position], holder.imageView, options, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							holder.progressBar.setProgress(0);
							holder.progressBar.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
							holder.progressBar.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
							holder.progressBar.setVisibility(View.GONE);
						}
					}, new ImageLoadingProgressListener() {
						@Override
						public void onProgressUpdate(String imageUri, View view, int current, int total) {
							holder.progressBar.setProgress(Math.round(100.0f * current / total));
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