package com.example.hasee.express.express.activities;

import com.example.hasee.express.R;
import com.example.hasee.express.express.beans.ListItemMessage;
import com.example.hasee.express.express.beans.ListItemMessageLab;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    //UI references
    private TextView mUsernameTextView;
    private RecyclerView mRecyclerView;

    //Adapter
    private DetailAdapter mAdapter;

    private Context thisContext;


    public static Intent newIntend(Context context) {
        return new Intent(context, DetailActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //初始化UI组件
        mUsernameTextView = (TextView) findViewById(R.id.current_username);
        mRecyclerView = (RecyclerView) findViewById(R.id.receiver_recycleview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //设置组件
        String currentName = getIntent().getStringExtra("name");
        mUsernameTextView.setText(currentName);

        //更新UI
        List<ListItemMessage> list = ListItemMessageLab.getListItemMessageLab().getList();
        if (mAdapter == null) {
            mAdapter = new DetailAdapter(list);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setList(list);
            mAdapter.notifyDataSetChanged();
        }

    }

    /**
     * DetailViewHolder内部类
     */
    private class DetailViewHolder extends RecyclerView.ViewHolder {

        private ListItemMessage message;

        //UI references
        private TextView mReceiverTextView;
        private TextView mSenderTextView;

        public DetailViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.recyclerview_viewholder, parent, false));

            mReceiverTextView = (TextView) itemView.findViewById(R.id.receiver_textView);
            mSenderTextView = (TextView) itemView.findViewById(R.id.sender_textView);

        }

        /**
         * 绑定组件，用于动态改变itemView中各组件的值
         * @param item 模型层数据
         */
        public void bind(ListItemMessage item) {
            message = item;

            mReceiverTextView.setText(item.getReceiver());
            mSenderTextView.setText(item.getSender());
        }
    }


    /**
     * DetailAdapter内部类
     */
    private class DetailAdapter extends RecyclerView.Adapter<DetailViewHolder>{

        private List<ListItemMessage> mList;

        public DetailAdapter(List<ListItemMessage> mList) {
            this.mList = mList;
        }

        @NonNull
        @Override
        public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(thisContext);
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
    }
}
