package com.pgyer.paimanager.paimanager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

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
        setContentView(R.layout.activity_main);
//        lists.clear();
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

                doUpload();
            }


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ReflashData();
    }

    public void ReflashData(){

                lists.clear();
                DataReader.readerdata(lists);
                adapter.notifyDataSetChanged();
    }

    public void EnableBtn(boolean enable){
        if(enable == true){

            upload_btn.setEnabled(true);
            delete_btn.setEnabled(true);
            reflash_btn.setEnabled(true);

        }else {

            upload_btn.setEnabled(false);
            delete_btn.setEnabled(false);
            reflash_btn.setEnabled(false);

        }
    }

    public void doUpload(){

        mProgressBar.setVisibility(View.VISIBLE);
//        EnableBtn(false);
        for( ItemVideo itemVideo :lists){

            if (itemVideo.ischeck == true ) {

                final String path = itemVideo.getPath();
                File video = new File(path);
                final RequestParams params = new RequestParams();

                try {
                    params.put("videoFile", video);
                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

                    asyncHttpClient.setMaxRetriesAndTimeout(3,2000);
                    asyncHttpClient.post(uploadURL, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {

//                            itemVideo.isUpload = true;
//                            DataReader.DeleteViedoFile(path);
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
//
                            mProgressBar.setProgress((int) doing);
                        }




                        @Override
                        public void onFinish() {

                            super.onFinish();
                            mProgressBar.setVisibility(View.GONE);
//                            EnableBtn(true);
                            ReflashData();

                        }
                    });

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
//

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
 }
