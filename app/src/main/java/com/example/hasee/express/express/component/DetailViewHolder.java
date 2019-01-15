package com.example.hasee.express.express.component;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hasee.express.R;
import com.example.hasee.express.express.beans.ListItemMessage;

public class DetailViewHolder extends RecyclerView.ViewHolder {

        private ListItemMessage message;

        //UI references
        private TextView mReceiverTextView;
        private TextView mSenderTextView;

        public LinearLayout mLayout;

        public DetailViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.recyclerview_viewholder, parent, false));

            mReceiverTextView = (TextView) itemView.findViewById(R.id.receiver_textView);
            mSenderTextView = (TextView) itemView.findViewById(R.id.sender_textView);
            mLayout = (LinearLayout) itemView.findViewById(R.id.item_layout);

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

    public ListItemMessage getMessage() {
        return message;
    }
}
