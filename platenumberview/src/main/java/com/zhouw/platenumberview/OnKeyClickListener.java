package com.zhouw.platenumberview;

public interface OnKeyClickListener {
    void onDeleteClick();

    void onItemClick(CharSequence text);

    void onSwitchClick(boolean isProvince);
}
