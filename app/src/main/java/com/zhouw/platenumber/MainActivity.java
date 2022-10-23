package com.zhouw.platenumber;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.zhouw.platenumberview.PlateNumberLayout;

public class MainActivity extends AppCompatActivity implements PlateNumberLayout.OnPlateNumberLayoutListener {

    private BottomSheetDialog bottomSheetDialog;
    private PlateNumberLayout plateNumberLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomSheetDialog = new BottomSheetDialog(this);
        plateNumberLayout = new PlateNumberLayout(this);
        plateNumberLayout.setListener(this);
        bottomSheetDialog.setContentView(plateNumberLayout);
        bottomSheetDialog.show();
    }

    @Override
    public void onDeleteClick() {
        Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(CharSequence text) {
        Toast.makeText(this, text.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSwitchClick(boolean isProvince) {
        Toast.makeText(this, isProvince ? "切换到省份" : "切换到字母和数字", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCompleteClick() {
        Toast.makeText(this, "点击了完成", Toast.LENGTH_SHORT).show();
        bottomSheetDialog.dismiss();
    }

    public void test(View view) {
        if (bottomSheetDialog.isShowing()) {
            bottomSheetDialog.dismiss();
        } else {
            bottomSheetDialog.show();
        }
    }
}