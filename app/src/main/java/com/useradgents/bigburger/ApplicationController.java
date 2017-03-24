package com.useradgents.bigburger;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by AUBERT on 11/03/2017.
 */

public class ApplicationController extends Application {
    public static final String TAG = ApplicationController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static ApplicationController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        ActiveAndroid.initialize(mInstance);
    }

    public static synchronized ApplicationController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
}
