package com.zhouw.platenumberview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class PlateNumberLayout extends LinearLayout {
    private OnPlateNumberLayoutListener listener;
    private PlateNumberView carNumberView;

    public PlateNumberLayout(Context context) {
        this(context, null);
    }

    public PlateNumberLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlateNumberLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_plate_number, this);
        TextView complete = findViewById(R.id.complete);
        carNumberView = findViewById(R.id.carnumber);
        complete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCompleteClick();
                }
            }
        });
    }

    public void setListener(OnPlateNumberLayoutListener listener) {
        this.listener = listener;
        carNumberView.setListener(listener);
    }

    public void changeCarNumberKeyboard(boolean isProvincePanel) {
        if (carNumberView != null) {
            carNumberView.changeKeyBoard(isProvincePanel);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public interface OnPlateNumberLayoutListener extends OnKeyClickListener {
        void onCompleteClick();
    }
}
