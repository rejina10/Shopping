package com.android.shopping;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.shopping.connectivity.NetworkChangeReceiver;
import com.android.shopping.connectivity.NetworkUtil;
import com.android.shopping.database.DatabaseQueryManager;
import com.android.shopping.util.SortItemByCategory;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderDecoration;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView.LayoutManager mLayoutManager;
    private WeakReference<MyAsyncTask> asyncTaskWeakRef;
    private StickyHeaderDecoration mDecor;
    private ItemAdapter adapter;
    private NetworkChangeReceiver networkChgReceiver;
    private ProgressDialog progressDialog;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void startNewAsyncTask() {
        MyAsyncTask asyncTask = new MyAsyncTask(this);
        this.asyncTaskWeakRef = new WeakReference<MyAsyncTask>(asyncTask);
        asyncTask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView)getActivity().findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        swipeRefreshLayout = (SwipeRefreshLayout)getActivity().findViewById(R.id.swiper);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtil.getConnectivityStatus(getActivity()) == NetworkUtil.TYPE_NOT_CONNECTED) {
                    Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    startNewAsyncTask();
                }
            }
        });

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        List<ItemModel> itemsList = DatabaseQueryManager.getDbInstance(getActivity()).getItemsList();
        if (NetworkUtil.getConnectivityStatus(getActivity()) == NetworkUtil.TYPE_NOT_CONNECTED) {

            if (itemsList.size() > 0) {
                loadItems(itemsList);
            }
             Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        } else {
            startNewAsyncTask();
        }
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, List<ItemModel>> {
        private WeakReference<HomeFragment> fragmentWeakRef;

        private MyAsyncTask(HomeFragment fragment) {
            this.fragmentWeakRef = new WeakReference<HomeFragment>(fragment);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!swipeRefreshLayout.isRefreshing()) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Loading.....");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        }

        @Override
        protected List<ItemModel> doInBackground(Void... params) {
            List<ItemModel> result = null;
            try {
                result = HttpConnection.executeHttpRequest();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<ItemModel> response) {
            super.onPostExecute(response);
            if (swipeRefreshLayout.isRefreshing()) {
                Collections.sort(response, new SortItemByCategory());
                adapter.swapCursor(response);
                recyclerView.removeItemDecoration(mDecor);
                mDecor = new StickyHeaderDecoration(adapter);
                recyclerView.addItemDecoration(mDecor, 0);
                recyclerView.getAdapter().notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                DatabaseQueryManager.getDbInstance(getActivity()).storeItems(response);
            } else {
                if (this.fragmentWeakRef.get() != null) {
                    DatabaseQueryManager.getDbInstance(getActivity()).storeItems(response);
                    loadItems(response);
                    progressDialog.dismiss();
                }
            }
        }
    }

    public void loadItems(List<ItemModel> itemModelsList) {
        Collections.sort(itemModelsList, new SortItemByCategory());
        adapter = new ItemAdapter(itemModelsList);
        mDecor = new StickyHeaderDecoration(adapter);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(mDecor, 0);
    }


}