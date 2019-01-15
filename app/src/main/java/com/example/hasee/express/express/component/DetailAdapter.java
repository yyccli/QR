package com.example.hasee.express.express.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.hasee.express.express.beans.ListItemMessage;

import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailViewHolder>{
    private Context mContext;

    private List<ListItemMessage> mList;

    public DetailAdapter(List<ListItemMessage> mList, Context context) {
        this.mList = mList;
        mContext = context;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new DetailViewHolder(inflater, viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder viewHolder, int i) {
        ListItemMessage message = mList.get(i);
        viewHolder.bind(message);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setList(List<ListItemMessage> list) {
        mList = list;
    }

    public void removeItem(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }
}
