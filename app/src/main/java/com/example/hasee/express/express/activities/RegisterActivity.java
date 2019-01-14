package com.example.hasee.express.express.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hasee.express.R;
import com.example.hasee.express.express.netThread.RequestTask;
import com.example.hasee.express.express.utils.PostUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    // UI references.
    private EditText mUserlEditText;
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private Button mRegisterButton;
    private View mProgressView;
    private View mRegisterFormView;

    //把该Activity的Context单独提取出来方便在回调函数中使用
    private Context thisContext = this;

    public static Intent newIntend(Context context) {
        return new Intent(context, RegisterActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //初始化组件引用
        mUserlEditText = (EditText) findViewById(R.id.user_editText);
        mUsernameEditText = (EditText) findViewById(R.id.username_editText);
        mPasswordEditText = (EditText) findViewById(R.id.password_editText);
        mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRigster();
            }
        });
        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);
    }

    /**
     * 注册网络请求
     */
    private void requestRigster() {
        //暂存这次的输入信息
        String user = mUserlEditText.getText().toString();
        String username = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 检查密码格式
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordEditText.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordEditText;
            cancel = true;
        }

        //满足登录要求检查
        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);

            //构造Map参数映射
            Map<String, String> paramap = new HashMap<>();
            paramap.put("username", user);
            paramap.put("name", username);
            paramap.put("password", password);

            //构造后台线程发送网络请求
            RequestTask requestTask = new RequestTask(PostUtil.REGISTER_ROUTE, paramap);
            requestTask.setCallbacks(new RequestTask.Callbacks() {
                @Override
                public void updateUI(JSONObject jsonObject) {
                    try {
                        String responseCode = jsonObject.getString("status");
                        showProgress(false);

                        if (responseCode.equals("101")) {
                            Toast.makeText(thisContext, "账号已存在,请注册一个新账号", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(thisContext, "注册成功", Toast.LENGTH_SHORT)
                                    .show();
                            startActivity(LoginActivity.newIntend(thisContext));
                        }

                        Log.i("注册返回:", "hhhhhhhhh");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            requestTask.execute();
        }
    }


    /**
     * 检查密码格式
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * 隐藏表单
     */
    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
