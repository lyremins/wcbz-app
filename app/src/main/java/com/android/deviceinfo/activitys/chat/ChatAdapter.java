package com.android.deviceinfo.activitys.chat;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.deviceinfo.MyApp;
import com.android.deviceinfo.R;
import com.android.deviceinfo.base.SimpleRecyclerAdapter;
import com.android.deviceinfo.base.SimpleViewHolder;

import androidx.annotation.NonNull;

public class ChatAdapter extends SimpleRecyclerAdapter<ChatBean.DataBean> {

    public static final int OTHER = 1;
    public static final int MINE = 2;

    @NonNull
    @Override
    public SimpleViewHolder<ChatBean.DataBean> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == OTHER){
            return new ChatLeftViewHolder(LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_chat_left, parent, false));
        }else {
            return new ChatRightViewHolder(LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_chat_right, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        int fromid = mListData.get(position).fromuid;
        if (fromid == MyApp.sharedPreferences.getInt("user_id",0)){
            return MINE;
        }else {
            return OTHER;
        }
    }
}
