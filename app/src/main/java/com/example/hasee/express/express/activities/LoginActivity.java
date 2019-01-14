package com.example.hasee.express.express.activities;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hasee.express.R;
import com.example.hasee.express.express.beans.ListItemMessageLab;
import com.example.hasee.express.express.netThread.RequestTask;
import com.example.hasee.express.express.utils.PostUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * app登录界面
 */
public class LoginActivity extends Activity {
    // UI references.
    private EditText mUserlEditText;
    private EditText mPasswordEditText;
    private Button mRegisterButton;
    private Button mLoginButton;
    private View mProgressView;
    private View mLoginFormView;

    //把该Activity的Context单独提取出来方便在回调函数中使用
    private Context thisContext = this;

    public static Intent newIntend(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //初始化组件引用
        mUserlEditText = (EditText) findViewById(R.id.user_editText);
        mPasswordEditText = (EditText) findViewById(R.id.password_editText);
        mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRigster();
            }
        });
        mLoginButton = (Button) findViewById(R.id.sign_in_button);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * 响应点击注册按钮
     */
    private void attemptRigster() {
        startActivity(RegisterActivity.newIntend(this));
    }

    /**
     * 响应点击登录按钮
     */
    private void attemptLogin() {
        // 重置错误
        mUserlEditText.setError(null);
        mPasswordEditText.setError(null);

        // 存储此次登陆的账号和密码
        final String user = mUserlEditText.getText().toString();
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
            paramap.put("password", password);

            //构造后台线程发送网络请求
            RequestTask requestTask = null;
            if (user.equals("root")) {
                requestTask = new RequestTask(PostUtil.ROOT_ROUTE, paramap);
            } else {
                requestTask = new RequestTask(PostUtil.LOGIN_ROUTE, paramap);
            }
            requestTask.setCallbacks(new RequestTask.Callbacks() {
                @Override
                public void updateUI(JSONObject jsonObject) {
                    try {
                        String responseCode = jsonObject.getString("status");
                        showProgress(false);

                        if (responseCode.equals("101")) {
                            Toast.makeText(thisContext, "用户名或密码错误", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            //登陆成功，将服务器返回的数据保存
                            //因为json格式设计的问题，这里存储数据很复杂
                            if (user.equals("root")) {
                                JSONArray tempjsonArray = jsonObject.getJSONObject("data")
                                        .getJSONArray("array");
                                for (int i = 0; i < tempjsonArray.length(); i++){
                                    JSONObject tempjson = (JSONObject) tempjsonArray.get(i);
                                    saveOneCourierMes(tempjson);
                                }
                            } else {
                                JSONObject tempjson = jsonObject.getJSONObject("data");
                                saveOneCourierMes(tempjson);
                            }

                            //进入用户页面
                            Intent intent = DetailActivity.newIntend(thisContext);
                            intent.putExtra("name", user);
                            startActivity(intent);
                        }

                        Log.i("登录返回:", "hhhhhhhhh");

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
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }


    /**
     * 将单个courier的JSON存储到模型层中
     * @param jsonObject courier
     */
    private void saveOneCourierMes(JSONObject jsonObject) {
        String sender = null;
        try {
            sender = jsonObject.getJSONObject("courier").getString("name");

            JSONArray tempjsonArray = jsonObject.getJSONArray("receivers");
            for (int i = 0; i < tempjsonArray.length(); i++) {
                ListItemMessageLab.getListItemMessageLab()
                        .add((String) tempjsonArray.get(i), sender);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}

