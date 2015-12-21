package com.pgyer.paimanager.paimanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao9jiu on 15/11/27.
 */
public class MainListAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    List<ItemVideo> allVideo = new ArrayList<>();
    Context mContext;

    public MainListAdapter(Context context, List<ItemVideo> lists) {
        this.mContext = context;
        this.allVideo = lists;
        this.mInflater = LayoutInflater.from(mContext);
    }
    public void onDateChange(List<ItemVideo> lists){
        this.allVideo = lists;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return allVideo.size();
    }

    @Override
    public Object getItem(int position) {
        return allVideo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ItemViewHolder holder = null;

        if (convertView == null) {

            holder = new ItemViewHolder();
            convertView = mInflater.inflate(R.layout.item_list, null);
//            if((position % 2)!=0){
//                convertView.setBackgroundColor(Color.argb(15,15,15,15));
//            }else {
//                convertView.setBackgroundColor(Color.WHITE);
//
//            }
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            holder.imageView = (ImageView) convertView.findViewById(R.id.item_image);
            holder.text = (TextView) convertView.findViewById(R.id.describe);
            holder.size = (TextView) convertView.findViewById(R.id.size);
            holder.uploaded = (TextView) convertView.findViewById(R.id.uoloaded);
            convertView.setTag(holder);
        } else {
            holder = (ItemViewHolder) convertView.getTag();
        }

        final ItemVideo item = allVideo.get(position);
        holder.imageView.setImageBitmap(item.getImage());
        holder.text.setText(item.getName());
        holder.size.setText(item.getSize()+" KB");
        final ItemViewHolder holderView;

        holderView = holder;
        final EditText editName = new EditText(mContext);
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                holderView.checkBox.setChecked(true);
//                item.setIscheck(true);
                final AlertDialog editNameDialog = new AlertDialog.Builder(mContext).setTitle("请修改名称")
                        .setView(editName)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Log.d("tao99",editName.getText().length()+"___________" );
                                if(editName.getText().length() != 0){
                                item.setName(editName.getText().toString() + ".mp4");
                                File file = new File(item.getPath());
                                String newpath = PathUtilReader.getPaiMasterVideoPath() + "/" + item.getName();
                                file.renameTo(new File(newpath));
                                Log.d("tao99","-------------------------"+newpath);
                                item.setPath(newpath);
                                holderView.text.setText(editName.getText().toString() + ".mp4");

                                }else if(editName.getText().length() == 0 ){
                                    Log.d("tao99",editName.getText().toString()+"___________" );


                                    item.setName(item.getName());
                                    holderView.text.setText(item.getName());
                                }

//                                File file = new File(item.getPath());
//                                String newpath = PathUtilReader.getPaiMasterVideoPath() + "/" + holderView.text.getText().toString();
//                                file.renameTo(new File(newpath));
                                notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();

            }
        });
//        holder.text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                File file = new File(item.getPath());
//                String newpath =PathUtilReader.getPaiMasterVideoPath()+"/"+holderView.text.getText().toString();
//                file.renameTo(new File(newpath));
//                Log.d("tao99", file.getName() + "ssssssssssssssss");
//                holderView.text.clearFocus();
//
//            }
//        });
//

        holder.checkBox.setChecked(item.ischeck);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.ischeck ){
                    item.setIscheck(false);
                }else {
                    item.setIscheck(true);

                }
            }
        });

        return convertView;
    }

    class ItemViewHolder {
        CheckBox checkBox;
        ImageView imageView;
        TextView text;
        TextView size;
        TextView uploaded;
    }
}