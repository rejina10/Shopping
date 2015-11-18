package com.android.shopping.WebService;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.android.shopping.HttpConnection;
import com.android.shopping.ItemFragment;
import com.android.shopping.MainActivity;
import com.android.shopping.database.DatabaseQueryManager;

import java.lang.ref.WeakReference;

/**
 * Created by rejina on 11/14/2015.
 */
public class AsyncTaskDelete extends AsyncTask<String, Void, Boolean> {

    private ItemFragment container;
    private WeakReference<ItemFragment> fragmentWeakRef;
    private static String itemId;
    private Activity activity;

    public AsyncTaskDelete(ItemFragment fragment, Activity activity) {
        this.fragmentWeakRef = new WeakReference<ItemFragment>(fragment);
        this.container = fragment;
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        boolean result = false;
        try {
            this.itemId = params[0];
            result = HttpConnection.executeDeleteRequest(params[0]);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(Boolean isCreated) {
        super.onPostExecute(isCreated);
        if (fragmentWeakRef.get() != null && container !=null) {
            ((MainActivity)activity).displayView(0);
             DatabaseQueryManager.getDbInstance(activity).deleteItems(itemId);
        }
    }
}