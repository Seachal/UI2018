package com.zinc.bezier.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zinc.bezier.R;
import com.zinc.bezier.widget.HeartView;

/**
 * *
 * *
 * Project_Name:UI2018
 *
 * @author zhangxc
 * @date 2020-02-12 15:52
 * *
 */
public class GridActivity extends AppCompatActivity {



    private HeartView heartView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        heartView = findViewById(R.id.heart_view);

    }

}
