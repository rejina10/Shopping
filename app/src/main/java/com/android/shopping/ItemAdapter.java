package com.android.shopping;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderAdapter;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> implements StickyHeaderAdapter<ItemAdapter.HeadHolder> {

    private Context cntx = null;
    private List<ItemModel> data;

    public ItemAdapter(List<ItemModel> data) {
        this.data = data;
        updateListAndHeader(this.data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mDefinition;
        private ImageView mthumbnail;

        public ViewHolder(View viewItem)  {
            super(viewItem);
            viewItem.setOnClickListener(this);
            mDefinition = (TextView) viewItem.findViewById(R.id.itemName);
            mthumbnail = (ImageView) viewItem.findViewById(R.id.itemImage);
        }

        @Override
        public void onClick(View view){
            cntx = view.getContext();//Toast.makeText(view.getContext(),data.get(getAdapterPosition()).getCategory(),Toast.LENGTH_SHORT).show();
            fragmentJump(data.get(getAdapterPosition()));
        }
    }

    public class HeadHolder extends RecyclerView.ViewHolder {
        private TextView txtHeader;

        public HeadHolder(View itemView) {
            super(itemView);
            txtHeader = (TextView) itemView.findViewById(R.id.txtHeader);
        }
    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_groupitem, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemModel eachItem = data.get(position);
        holder.mDefinition.setText(eachItem.getName());
        holder.mthumbnail.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public long getHeaderId(int position) {
//        if (position <= 2) {
//            return 2;
//        } else if(position<=6){
//            return 6;
//        }
//        return 6;

//        boolean isSame = position > 0 && data.get(position).getCategory().equalsIgnoreCase(data.get(position - 1).getCategory());
//        if (position>0 && !isSame) {
//            lastHeaderId = position;
//        }
//        return lastHeaderId;
        return data.get(position).getHeaderId();
    }

    @Override
    public HeadHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_header, parent, false);
        HeadHolder headHolder = new HeadHolder(headerView);
        return headHolder;
    }

    @Override
    public void onBindHeaderViewHolder(HeadHolder viewholder, int position) {
        Log.e("Header", data.get(position).getCategory() + "," + data.get(position).getName());
        viewholder.txtHeader.setText(data.get(position).getCategory());
    }

    private void fragmentJump(ItemModel mItemSelected) {
        Fragment mFragment = new ItemFragment();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("item_selected_key", mItemSelected);
        mFragment.setArguments(mBundle);
        switchCnt(R.id.fragment_container, mFragment);
    }

    public void switchCnt(int id, Fragment fragment) {
        if (cntx == null)
            return;
        if (cntx instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) cntx;
            mainActivity.switchContent(id, fragment);
        }
    }

    public void swapCursor(List<ItemModel> data){
        this.data = data;
        updateListAndHeader(this.data);
    }

    public void updateListAndHeader(List<ItemModel> data){
        if (data.size() > 0) {
            data.get(0).setHeaderId(0);
            int lastId = 0;
            for (int i = 1; i < data.size(); i++) {
                if (!data.get(i).getCategory().equalsIgnoreCase(data.get(i - 1).getCategory())) {
                    lastId = i;
                }
                data.get(i).setHeaderId(lastId);
            }
        }

    }

}
