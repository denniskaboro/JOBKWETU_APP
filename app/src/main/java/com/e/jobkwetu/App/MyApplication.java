package com.e.jobkwetu.App;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.e.jobkwetu.Helper.MyPreferenceManager;
import com.e.jobkwetu.Register_User.LoginActivity;
import com.e.jobkwetu.Utilities.ImageClass;

public class MyApplication extends Application {
	public static final String TAG = MyApplication.class
			.getSimpleName();

	private RequestQueue mRequestQueue;

	private static MyApplication mInstance;

	private MyPreferenceManager pref;

	private ImageLoader mImageLoader;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
	}

	public static synchronized MyApplication getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}
	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue,
					new ImageClass());
		}
		return this.mImageLoader;
	}

	public MyPreferenceManager getPrefManager() {
		if (pref == null) {
			pref = new MyPreferenceManager(this);
		}

		return pref;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	public void logout() {
		pref.clear();
		Intent intent = new Intent(this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}
}
