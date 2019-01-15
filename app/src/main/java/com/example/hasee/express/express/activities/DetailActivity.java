package com.example.hasee.express.express.activities;

import com.example.hasee.express.R;
import com.example.hasee.express.express.beans.ListItemMessage;
import com.example.hasee.express.express.beans.ListItemMessageLab;
import com.example.hasee.express.express.component.DetailAdapter;
import com.example.hasee.express.express.component.DetailViewHolder;
import com.example.hasee.express.express.component.ItemRemoveRecyclerView;
import com.example.hasee.express.express.component.OnItemClickListener;
import com.example.hasee.express.express.netThread.RequestTask;
import com.example.hasee.express.express.utils.PostUtil;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PipedOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {
    //CurrentUser
    private String currentUser;

    //UI references
    private TextView mUsernameTextView;
    private ItemRemoveRecyclerView mRecyclerView;
    private FloatingActionButton mFloatBtn;

    //Adapter
    private DetailAdapter mAdapter;

    private Context thisContext = this;


    public static Intent newIntend(Context context) {
        return new Intent(context, DetailActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //初始化UI组件
        mUsernameTextView = (TextView) findViewById(R.id.current_username);
        mFloatBtn = (FloatingActionButton) findViewById(R.id.add_floatBtn);
        mFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入添加对话框
                showAddDialog();
            }
        });
        mRecyclerView = (ItemRemoveRecyclerView) findViewById(R.id.receiver_recycleview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(DetailViewHolder viewHolder, int position) {
                //进入二维码对话框
                String receiverName = viewHolder.getMessage().getReceiver();
                String courierName = viewHolder.getMessage().getSender();

                Map<String, String> paramap = new HashMap<>();
                paramap.put("receiver_name", receiverName);
                paramap.put("username", courierName);

                RequestTask requestTask = new RequestTask(PostUtil.DETAIL_ROUTE, paramap);
                requestTask.setCallbacks(new RequestTask.Callbacks() {
                    @Override
                    public void updateUI(JSONObject jsonObject) {
                        try {
                            String responeCode = jsonObject.getString("status");
                            if (responeCode.equals("100")) {
                                String base64Img = jsonObject.getString("data");
                                Log.i("接收到的img为：", base64Img);

                                //TODO:将图片解码并显示出来


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                requestTask.execute();
            }

            @Override
            public void onDeleteClick(int position, String receiverName, String courierUsername) {
                //删除请求
                showDeleteDialog(receiverName, courierUsername);
                mAdapter.removeItem(position);
            }
        });

        //设置组件
        currentUser = getIntent().getStringExtra("username");
        mUsernameTextView.setText(currentUser);

        //更新UI
        List<ListItemMessage> list = ListItemMessageLab.getListItemMessageLab().getList();
        if (mAdapter == null) {
            mAdapter = new DetailAdapter(list, thisContext);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setList(list);
            mAdapter.notifyDataSetChanged();
        }

    }

    /**
     * 弹出添加对话框
     */
    private void showAddDialog() {
        final View dialog = LayoutInflater.from(this).inflate(R.layout.dialog_add, null);
        final EditText courierNameEditText = (EditText) dialog.findViewById(R.id.courier_name_editText);
        final EditText receiverNameEditText = (EditText) dialog.findViewById(R.id.receiver_name_editText);
        final EditText receiverAddressEditText = (EditText) dialog.findViewById(R.id.receiver_address_editText);
        final EditText receiverTelEditText = (EditText) dialog.findViewById(R.id.receiver_tel_editText);
//        Button commitBtn = (Button) dialog.findViewById(R.id.commit_button);
//        Button cancelBtn = (Button) dialog.findViewById(R.id.cancel_button);

        //如果用户不是root,则快递员固定是自己
        if (!currentUser.equals("root")) {
            courierNameEditText.setText(currentUser);
            courierNameEditText.setFocusable(false);
        }

        final AlertDialog.Builder layoutDialog = new AlertDialog.Builder(this);
        layoutDialog.setTitle("请输入添加的收件人信息：");
        layoutDialog.setView(dialog);

        layoutDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String courierName = courierNameEditText.getText().toString();
                final String receiverName = receiverNameEditText.getText().toString();
                String receiverAddress = receiverAddressEditText.getText().toString();
                String receiverTel = receiverTelEditText.getText().toString();

                Map<String, String> paramap = new HashMap<>();
                paramap.put("receiver_name", receiverName);
                paramap.put("address", receiverAddress);
                paramap.put("tel", receiverTel);
                paramap.put("username", courierName);

                RequestTask requestTask = new RequestTask(PostUtil.ADD_ROUTE, paramap);

                requestTask.setCallbacks(new RequestTask.Callbacks() {
                    @Override
                    public void updateUI(JSONObject jsonObject) {
                        try {
                            String responeCode = jsonObject.getString("status");
                            if (responeCode.equals("100")) {
                                ListItemMessageLab.getListItemMessageLab().add(receiverName, courierName);
                                mAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                requestTask.execute();
            }
        });

        layoutDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //啥都不做
            }
        });

        layoutDialog.create().show();
    }

    /**
     * 弹出添加对话框
     */
    private void showDeleteDialog(final String receiverName, final String courierUsername) {
        AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
        normalDialog.setTitle("确定要删除吗？");
        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Map<String, String> paramap = new HashMap<>();
                paramap.put("receiver_name", receiverName);
                paramap.put("username", courierUsername);

                RequestTask requestTask = new RequestTask(PostUtil.DELETE_ROUTE, paramap);

                requestTask.setCallbacks(new RequestTask.Callbacks() {
                    @Override
                    public void updateUI(JSONObject jsonObject) {
                        try {
                            String responeCode = jsonObject.getString("status");
                            if (responeCode.equals("100")) {
                                ListItemMessageLab.getListItemMessageLab().remove(receiverName, courierUsername);
                                mAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                requestTask.execute();
            }
        });
        normalDialog.create().show();
    }

}
