package com.android.shopping;


import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.shopping.WebService.AsyncTaskDelete;
import com.android.shopping.WebService.AsyncTaskUpdate;
import com.android.shopping.connectivity.NetworkUtil;
import com.android.shopping.util.JsonFormatter;
import com.android.shopping.util.Validation;

import java.lang.ref.WeakReference;

public class ItemFragment extends Fragment{

    private Button btnSave;
    private Button btnCancel;
    private Button btnDelete;
    private TextView txtCategory;
    private TextView txtName;
    private WeakReference<MyAsyncTask> asyncTaskWeakRef;
    private WeakReference<AsyncTaskUpdate> asyncTaskWeakRefe;
    private WeakReference<AsyncTaskDelete> asyncTaskWeakRefDel;
    private ItemModel obj;

    static boolean isCategoryValid;
    static boolean isItemNameValid;
    public ItemFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        txtCategory   = (TextView)getActivity().findViewById(R.id.category_Name);
        txtName       = (TextView)getActivity().findViewById(R.id.item_Name);
        btnSave       = (Button)getActivity().findViewById(R.id.btn_add);
        btnCancel     = (Button)getActivity().findViewById(R.id.btn_cancel);
        btnDelete     =  (Button)getActivity().findViewById(R.id.btn_delete);
        btnDelete.setVisibility(View.GONE);

        savedInstanceState = getArguments();
        if(savedInstanceState!=null) {
            obj = savedInstanceState.getParcelable("item_selected_key");
            txtCategory.setText(obj.getCategory());
            txtName.setText(obj.getName());
            btnSave.setText("Update");
            btnDelete.setVisibility(View.VISIBLE);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (obj != null) {
                    updateCategory(obj);
                } else {
                    addCategory();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).displayView(0);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(obj.getId() + "");
            }
        });

    }

    public void addCategory(){
        String categoryName = txtCategory.getText().toString();
        String itemName     = txtName.getText().toString();

        isCategoryValid  = Validation.validateView((EditText) txtCategory);
        isItemNameValid  = Validation.validateView((EditText) txtName);

        if(!isCategoryValid || !isItemNameValid){
            return;
        }
        if (NetworkUtil.getConnectivityStatus(getActivity()) == NetworkUtil.TYPE_NOT_CONNECTED) {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        } else {
            JsonFormatter format = new JsonFormatter();
            String jsonStr = format.convertToJson(categoryName, itemName);
            startNewAsyncTask(jsonStr);
        }

    }

    public void updateCategory(ItemModel obj){
        String categoryName = txtCategory.getText().toString();
        String itemName     = txtName.getText().toString();

        isCategoryValid  = Validation.validateView((EditText) txtCategory);
        isItemNameValid  = Validation.validateView((EditText)txtName);

        if(!isCategoryValid || !isItemNameValid){
            return;
        }

        if (NetworkUtil.getConnectivityStatus(getActivity()) == NetworkUtil.TYPE_NOT_CONNECTED) {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        } else {
            JsonFormatter format = new JsonFormatter();
            String jsonStr = format.convertToJson(categoryName, itemName);

            String arr[] = new String[2];
            arr[0] = jsonStr;
            arr[1] = obj.getId() + "";

            AsyncTaskUpdate asyncTask = new AsyncTaskUpdate(this, this.getActivity());
            asyncTaskWeakRefe = new WeakReference<AsyncTaskUpdate>(asyncTask);
            asyncTask.execute(arr);
        }

    }

    public void deleteItem(String id){
        isCategoryValid  = Validation.validateView((EditText) txtCategory);
        isItemNameValid  = Validation.validateView((EditText)txtName);

        if(!isCategoryValid || !isItemNameValid){
            return;
        }

        if (NetworkUtil.getConnectivityStatus(getActivity()) == NetworkUtil.TYPE_NOT_CONNECTED) {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        } else {
            AsyncTaskDelete asyncTask = new AsyncTaskDelete(this, this.getActivity());
            asyncTaskWeakRefDel = new WeakReference<AsyncTaskDelete>(asyncTask);
            asyncTask.execute(id);
        }
    }

    private void startNewAsyncTask(String jsonStr) {
        MyAsyncTask asyncTask = new MyAsyncTask(this);
        this.asyncTaskWeakRef = new WeakReference<MyAsyncTask >(asyncTask);
        asyncTask.execute(jsonStr);
    }

    private class MyAsyncTask extends AsyncTask<String, Void, Boolean> {

        private WeakReference<ItemFragment> fragmentWeakRef;

        private MyAsyncTask (ItemFragment fragment) {
            this.fragmentWeakRef = new WeakReference<ItemFragment>(fragment);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = false;
            try {
                result = HttpConnection.executePostRequest(params[0]);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean isCreated) {
            super.onPostExecute(isCreated);
            if (this.fragmentWeakRef.get() != null || isCreated == true) {
                Toast.makeText(getActivity(), "Item created", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
