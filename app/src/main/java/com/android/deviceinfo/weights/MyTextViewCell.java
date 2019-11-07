package com.android.deviceinfo.weights;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.TextView;

import com.android.deviceinfo.R;

/**
 * Created by Yuniko on 2015/6/1.
 * 带有图片的TextView
 */
public class MyTextViewCell extends TextView {
    private Drawable imgInable;
    private Drawable bottomLine;
    private Context mContext;

    public MyTextViewCell(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public MyTextViewCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public MyTextViewCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        imgInable = mContext.getResources().getDrawable(R.drawable.link_cell);
        bottomLine = mContext.getResources().getDrawable(R.drawable.bottom_line);

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });
        setDrawable();
    }

    private void setDrawable() {
        if (length() < 1) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, imgInable, bottomLine);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, imgInable, bottomLine);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
