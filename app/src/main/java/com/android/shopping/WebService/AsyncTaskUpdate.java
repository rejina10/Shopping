package com.android.shopping.WebService;

import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.android.shopping.HomeFragment;
import com.android.shopping.HttpConnection;
import com.android.shopping.ItemFragment;
import com.android.shopping.MainActivity;
import com.android.shopping.R;

import java.lang.ref.WeakReference;

/**
 * Created by rejina on 11/14/2015.
 */
public class AsyncTaskUpdate extends AsyncTask<String, Void, Boolean> {

    private WeakReference<ItemFragment> fragmentWeakRef;
    private Activity activity;

    public AsyncTaskUpdate(ItemFragment fragment,  Activity activity) {
        this.fragmentWeakRef = new WeakReference<ItemFragment>(fragment);
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        boolean result = false;
        try {
            result = HttpConnection.executePutRequest(params[0], params[1]);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return result;
    }

    @Override
    protected void onPostExecute(Boolean isUpdated) {
        super.onPostExecute(isUpdated);
        try{
        if (fragmentWeakRef.get() != null && isUpdated) {
            ((MainActivity)activity).displayView(0);
        }}
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}



