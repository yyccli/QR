package com.example.hasee.express.express.component;

import android.view.View;

public interface OnItemClickListener {
    /**
     * item点击回调
     *
     * @param viewHolder
     * @param position
     */
    void onItemClick(DetailViewHolder viewHolder, int position);

    /**
     * 删除按钮回调
     *
     * @param position
     */
    void onDeleteClick(int position, String receiver, String courierUsername);
}
