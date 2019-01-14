package com.example.hasee.express.express.netThread;

import android.os.AsyncTask;

import com.example.hasee.express.express.utils.PostUtil;

import org.json.JSONObject;

import java.util.Map;

/**
 * 后台请求线程
 */
public class RequestTask extends AsyncTask<Void, Void, JSONObject> {
    //Activity应该实现的回调接口，用于网络请求结束后更新UI
    public interface Callbacks {
        void updateUI(JSONObject jsonObject);
    }

    private PostUtil postUtil;
    private Callbacks mCallbacks;


    public RequestTask(String route, Map<String, String> paramap) {
        postUtil = new PostUtil(route, paramap);
    }

    public void setCallbacks(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {
        return postUtil.post();
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        mCallbacks.updateUI(jsonObject);
    }
}
