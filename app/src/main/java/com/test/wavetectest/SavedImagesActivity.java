package com.test.wavetectest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.test.wavetectest.adapter.RVMainClickInterface;
import com.test.wavetectest.adapter.RecyclerAdapterSaved;
import com.test.wavetectest.database.DBClass;
import com.test.wavetectest.models.ImageDBModel;

import java.util.List;

public class SavedImagesActivity extends AppCompatActivity implements RVMainClickInterface {

    private RecyclerView recyclerView;
    private RecyclerAdapterSaved adapter;
    private GridLayoutManager gridLayoutManager;
    private List<ImageDBModel> photoModelList;
    private TextView tvNoImg;

    DBClass dbClass = new DBClass(SavedImagesActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_images);
        initVariables();
        getDBdata();

    }

    public void initVariables(){

        recyclerView = findViewById(R.id.recyclerView_savedimg);
        gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        tvNoImg = findViewById(R.id.tv_noImage);


    }

    public void getDBdata(){

        photoModelList = dbClass.getAllData();
        if(photoModelList.isEmpty()){
            tvNoImg.setVisibility(View.VISIBLE);
        }else {
            tvNoImg.setVisibility(View.GONE);
            adapter = new RecyclerAdapterSaved(SavedImagesActivity.this,photoModelList,SavedImagesActivity.this);
            recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public void onItemClick(int position) {

    }
}