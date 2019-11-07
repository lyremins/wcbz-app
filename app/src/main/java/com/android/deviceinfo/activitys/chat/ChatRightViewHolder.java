package com.android.deviceinfo.activitys.chat;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.deviceinfo.R;
import com.android.deviceinfo.base.SimpleViewHolder;
import com.android.deviceinfo.constants.NetContants;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 */
public class ChatRightViewHolder extends SimpleViewHolder<ChatBean.DataBean> {

    private TextView tvTime;
    private CircleImageView imgHead;
    private TextView tvContent;
    private ImageView imgImage;
    private TextView tvName;

    public ChatRightViewHolder(View itemView) {
        super(itemView);
        tvTime = itemView.findViewById(R.id.tv_time);
        imgHead = itemView.findViewById(R.id.img_head);
        tvContent = itemView.findViewById(R.id.tv_content);
        imgImage = itemView.findViewById(R.id.img_image);
        imgHead.setVisibility(View.VISIBLE);
        tvName = itemView.findViewById(R.id.tv_name);
    }

    @Override
    protected void refreshView(ChatBean.DataBean data) {
        super.refreshView(data);
        tvTime.setText(data.createtime);
        if (TextUtils.equals(data.contentType,"0")) {
            tvContent.setText(data.content);
            tvContent.setVisibility(View.VISIBLE);
            imgImage.setVisibility(View.GONE);
        }else {
            Glide.with(getContext()).load(NetContants.IMAGE_URL + data.image_path)
                    .placeholder(R.color.bg_gray).into(imgImage);
            imgImage.setVisibility(View.VISIBLE);
            tvContent.setVisibility(View.GONE);
        }
    }
}
