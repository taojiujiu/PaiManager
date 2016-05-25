package com.pgyer.paimanager.paimanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends Activity {

    ListView mListView;
    Button delete_btn;
    Button reflash_btn;
    Button upload_btn;
    ProgressBar mProgressBar;
    MainListAdapter adapter;
    List<ItemVideo> lists = new ArrayList<ItemVideo>();
    String uploadURL = "http://zc.pgyer.com/apiv1/video/upload";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        PgyUpdateManager.register(this);

        setContentView(R.layout.activity_main);
        DataReader.readerdata(lists);
        mListView = (ListView) findViewById(R.id.mian_list);
        adapter = new MainListAdapter(MainActivity.this,lists);
        mListView.setAdapter(adapter);

        delete_btn = (Button) findViewById(R.id.delete_btn);
        reflash_btn = (Button) findViewById(R.id.reflash_btn);
        upload_btn = (Button) findViewById(R.id.uoload_btn);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ItemVideo itemVideo : lists) {
                    if (itemVideo.ischeck == true) {
                        String path = itemVideo.getPath();
                        DataReader.DeleteViedoFile(path);
                    }
                }
                ReflashData();
            }
        });

        reflash_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReflashData();
            }
        });

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(hasCheck()){
                if(isNetworkConnected(MainActivity.this)){
                    doUpload();
                }else{
                    Toast.makeText(MainActivity.this, "现在网络不可用!",
                            Toast.LENGTH_LONG).show();

                }
            }
            }
        });

        PgyUpdateManager.register(MainActivity.this,
                new UpdateManagerListener() {

                    @Override
                    public void onUpdateAvailable(final String result) {

                        // 将新版本信息封装到AppBean中
                        final AppBean appBean = getAppBeanFromString(result);
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("更新")
                                .setMessage("")
                                .setNegativeButton(
                                        "确定",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {

                                                String url = appBean.getDownloadURL();

                                                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download",
                                                        UUID.randomUUID() + ".apk");
                                                AsyncHttpClient client = new AsyncHttpClient();
                                                client.get(url, new FileAsyncHttpResponseHandler(file) {
                                                    @Override
                                                    public void onSuccess(int statusCode, Header[] hander, File file) {
                                                        if (statusCode == 200) {
                                                            Toast.makeText(getApplicationContext(), "文件下载成功", Toast.LENGTH_LONG).show();
                                                            UpdateManagerListener.updateLocalBuildNumber(result);

                                                            /*调用是否安装对话框*/
                                                            Intent install = new Intent();
                                                            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            install.setAction(android.content.Intent.ACTION_VIEW);
                                                            install.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
                                                            startActivity(install);
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(int statusCode, Header[] hander, Throwable throwable, File file) {
                                                        Toast.makeText(getApplicationContext(), "文件下载失败", Toast.LENGTH_LONG).show();
                                                        throwable.printStackTrace();
                                                    }
                                                });
                                            }
                                        }).show();
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ReflashData();
    }

    public void ReflashData() {
                lists.clear();
                DataReader.readerdata(lists);
                adapter.notifyDataSetChanged();
    }


    public void doUpload(){


        mProgressBar.setVisibility(View.VISIBLE);
        for( ItemVideo itemVideo :lists){

            if (itemVideo.ischeck == true ) {

                final String path = itemVideo.getPath();
                File video = new File(path);
                final RequestParams params = new RequestParams();

                try {
                    params.put("videoFile", video);
                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

                    asyncHttpClient.setMaxRetriesAndTimeout(3,20000);
                    asyncHttpClient.post(uploadURL, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {

                            Toast.makeText(MainActivity.this, "上传成功!",
                                    Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                            Toast.makeText(MainActivity.this, "上传失败!",
                                    Toast.LENGTH_LONG).show();
                        }


                        @Override
                        public void onProgress(long bytesWritten, long totalSize) {

                            super.onProgress(bytesWritten, totalSize);
                            double doing = (double) bytesWritten / (double) totalSize * 100;
                            mProgressBar.setProgress((int) doing);
                        }

                        @Override
                        public void onFinish() {

                            super.onFinish();
                            mProgressBar.setVisibility(View.GONE);
                            ReflashData();

                        }
                    });

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
        adapter.notifyDataSetChanged();
    }

    public boolean hasCheck(){
        int checknum = 0;
        for (ItemVideo item : lists){
            if(item.ischeck)
                checknum++;
        }
        if(checknum > 0){
            return true;
        }else {
            return false;
        }
    }

    public boolean isNetworkConnected(Context context) {

        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
 }
