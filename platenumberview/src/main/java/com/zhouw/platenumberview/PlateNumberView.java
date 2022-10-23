package com.zhouw.platenumberview;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

public class PlateNumberView extends FrameLayout {
    private static final int DEFAULT_SPACE = 3;
    private static final int DEFAULT_TEXTSIZE = 16;
    private OnKeyClickListener listener;
    private boolean isProvince = true;
    private int maxColumns;
    private int space;
    private String[][] data;
    private static final String[][] character_data = {{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"}
            , {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"}
            , {"A", "S", "D", "F", "G", "H", "J", "K", "L"}
            , {"省份", "Z", "X", "C", "V", "B", "N", "M", "\uD83D\uDD19"}};
    private static final String[][] province_data = {{"京", "津", "渝", "沪", "冀", "晋", "辽", "吉", "黑", "苏"}
            , {"浙", "皖", "闽", "赣", "鲁", "豫", "鄂", "湘", "粤", "琼"}
            , {"川", "贵", "云", "陕", "甘", "青", "蒙", "桂", "宁", "新"}
            , {"ABC", "藏", "使", "领", "警", "学", "港", "澳", "\uD83D\uDD19"}};
    private int textColor;
    private float txtSize;

    public PlateNumberView(Context context) {
        this(context, null);
    }

    public PlateNumberView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlateNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PlateNumberView);
        float defaultSpace = array.getDimension(R.styleable.PlateNumberView_space, DEFAULT_SPACE);
        space = dp2px((int) defaultSpace);
        textColor = array.getColor(R.styleable.PlateNumberView_textColor, Color.parseColor("#888888"));
        txtSize = array.getDimension(R.styleable.PlateNumberView_txtSize, DEFAULT_TEXTSIZE);
        array.recycle();
        initView(getContext());
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    private void initView(Context context) {
        removeAllViews();
        data = isProvince ? province_data : character_data;
        for (int i = 0; i < data.length; i++) {
            maxColumns = Math.max(maxColumns, data[i].length);
            for (int j = 0; j < data[i].length; j++) {
                Button view = new Button(context);
                view.setText(data[i][j]);
                view.setTextColor(textColor);
                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, txtSize);
                view.setPadding(dp2px(5), dp2px(5), dp2px(5), dp2px(5));
                ColorStateList colorStateList = getResources().getColorStateList(R.color.selector_platenumber);
                view.setBackgroundTintList(colorStateList);
                if (data[i][j].equals("I")) {
                    view.setEnabled(false);
                }
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener == null) {
                            return;
                        }
                        if (((Button) v).getText().equals("省份")) {
                            isProvince = true;
                            listener.onSwitchClick(isProvince);
                            initView(getContext());
                            requestLayout();
                        } else if (((Button) v).getText().equals("ABC")) {
                            isProvince = false;
                            listener.onSwitchClick(isProvince);
                            initView(getContext());
                            requestLayout();
                        } else if (((Button) v).getText().equals("\uD83D\uDD19")) {
                            listener.onDeleteClick();
                        } else {
                            listener.onItemClick(((Button) v).getText());
                        }
                    }
                });
                addView(view, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int baseChildWidth = (measureWidth - space * (maxColumns + 1)) / maxColumns;
        int childWidth = MeasureSpec.makeMeasureSpec(baseChildWidth, MeasureSpec.EXACTLY);
        int useWidth = baseChildWidth * (data[data.length - 1].length - 2) + (data[data.length - 1].length + 1) * space;
        int remainWidth = (measureWidth - useWidth) / 2;
        int largeChildWidth = MeasureSpec.makeMeasureSpec(remainWidth, MeasureSpec.EXACTLY);
        int childHeight = MeasureSpec.makeMeasureSpec((measureHeight - space * (data.length + 1)) / (data.length), MeasureSpec.EXACTLY);
        int k = 0;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (i == data.length - 1 && (j == 0 || j == data[i].length - 1)) {
                    getChildAt(k).measure(largeChildWidth, childHeight);
                } else {
                    getChildAt(k).measure(childWidth, childHeight);
                }
                k++;
            }
        }
        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int k = 0;
        int left;
        for (int i = 0; i < data.length; i++) {
            left = space;
            int top = space * (i + 1) + getChildAt(0).getMeasuredHeight() * i;
            if (i != data.length - 1 && data[i].length < maxColumns) {
                int size = data[i].length;
                int chidwidth = getChildAt(0).getMeasuredWidth();
                int remainWidth = (maxColumns - size) * (chidwidth + space);
                left = remainWidth / 2 + left;
            }
            for (int j = 0; j < data[i].length; j++) {
                View child = getChildAt(k);
                child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
                left = child.getRight() + space;
                k++;
            }
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public void changeKeyBoard(boolean provincePanel) {
        if (provincePanel == true && isProvince == false) {
            isProvince = true;
            initView(getContext());
            requestLayout();
        } else if (provincePanel == false && isProvince == true) {
            isProvince = false;
            initView(getContext());
            requestLayout();
        }

    }

    public void setListener(OnKeyClickListener listener) {
        this.listener = listener;
    }


}
