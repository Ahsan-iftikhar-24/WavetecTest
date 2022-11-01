package com.test.wavetectest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.test.wavetectest.adapter.RVMainClickInterface;
import com.test.wavetectest.adapter.RecyclerAdapterMain;
import com.test.wavetectest.database.DBClass;
import com.test.wavetectest.models.ImageDBModel;
import com.test.wavetectest.models.PhotoModel;
import com.test.wavetectest.viewModels.ViewModelMain;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RVMainClickInterface {

    private RecyclerView recyclerView;
    private RecyclerAdapterMain adapterMain;
    private GridLayoutManager gridLayoutManager;
    private int page = 1;
    private int perPage = 40;
    private String apiKey;
    private List<PhotoModel> photoModelList;
    boolean isNetworkOnline= false;
    private LinearLayout ll_response;
    private Button btn_response;
    private NestedScrollView nestedScrollView;
    private ProgressBar progressBar;
    DBClass dbClass = new DBClass(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiKey = getResources().getString(R.string.apiKey);

        initVariables();

        callApi();

        btn_response.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi();
            }
        });

        //Pagination
        setPagination(true);



    }


    public void initVariables(){

        recyclerView = findViewById(R.id.recyclerView);
        gridLayoutManager = new GridLayoutManager(this,2);
        ll_response = findViewById(R.id.ll_responseFailed);
        btn_response = findViewById(R.id.btn_res_retry);
        btn_response = findViewById(R.id.btn_res_retry);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        nestedScrollView = findViewById(R.id.nestedScroll);
        progressBar = findViewById(R.id.prog_load);


    }

    private void setPagination(boolean isPagination) {

        if(isPagination){
            nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {


                if (scrollY == 0)
                {
                    //Do something here when ur scroll reached the bottom by scrolling up
                    if(page > 0){
                        progressBar.setVisibility(View.VISIBLE);
                        getAllPictures(apiKey,--page,perPage);
                    }

                }
                if(scrollY == v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight()){
                    progressBar.setVisibility(View.VISIBLE);
                    getAllPictures(apiKey,++page,perPage);
                }

            });
        }
        else {
            nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {


            });
        }
    }


    public void callApi(){

        isNetworkOnline = isNetworkOnline1(MainActivity.this);

        if(isNetworkAvailable() && isNetworkOnline){
            ll_response.setVisibility(View.GONE);
            getAllPictures(apiKey,page,perPage);
        }
        else {
            ll_response.setVisibility(View.VISIBLE);
            photoModelList = new ArrayList<>();
            adapterMain = new RecyclerAdapterMain(MainActivity.this,photoModelList,MainActivity.this);
            recyclerView.setAdapter(adapterMain);


        }
    }

    public void getAllPictures(String auth_key,int pageCount,int per_Page){

        progressBar.setVisibility(View.GONE);

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Fetching Data....");
        progressDoalog.setTitle("Wavetec Test");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCanceledOnTouchOutside(false);
        progressDoalog.setCancelable(false);
        // show it
        progressDoalog.show();

        ViewModelMain viewModelMain = new ViewModelProvider(this).get(ViewModelMain.class);
        viewModelMain.makeApiCall(auth_key,pageCount,per_Page);

        try{
            viewModelMain.getMutableLiveData().observe(this, new Observer<List<PhotoModel>>() {
                @Override
                public void onChanged(List<PhotoModel> photoModels) {

                    if (photoModels != null){
                        if (!photoModels.isEmpty()){
                            progressDoalog.dismiss();
                            ll_response.setVisibility(View.GONE);
                            photoModelList = photoModels;
                            adapterMain = new RecyclerAdapterMain(MainActivity.this,photoModelList,MainActivity.this);
                            recyclerView.setAdapter(adapterMain);


                        }
                    }
                    else {
                        progressDoalog.dismiss();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{


            viewModelMain.getErrorCheck().observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    if(s.equals("500")){
                        progressDoalog.dismiss();
                        ll_response.setVisibility(View.VISIBLE);
                    }
                    else{
                        progressDoalog.dismiss();
                        ll_response.setVisibility(View.VISIBLE);
                    }

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.nav_saved:
                Intent intent = new Intent(MainActivity.this, SavedImagesActivity.class);
                startActivity(intent);
                break;

            default:
                return super.onOptionsItemSelected(item);

        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean isNetworkOnline1(Context context) {
        boolean isOnline = false;
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkCapabilities capabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());  // need ACCESS_NETWORK_STATE permission
            isOnline = capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isOnline;
    }


    @Override
    public void onItemClick(int position) {

        int imgID = photoModelList.get(position).getId();
        String p_name = photoModelList.get(position).getPhotographer();
        String width = String.valueOf(photoModelList.get(position).getWidth());
        String height = String.valueOf(photoModelList.get(position).getHeight());
        String url = photoModelList.get(position).getUrl();
        String p_url = photoModelList.get(position).getPhotographer_url();
        String imgUrl = photoModelList.get(position).getImages().getLarge();
        String imgUrlMed = photoModelList.get(position).getImages().getMedium();
        customDialog(imgID,p_name,width,height,url,p_url,imgUrl,imgUrlMed);

    }

    public void customDialog(int imgID,String p_name, String width, String height, String url, String p_url,String imgUrl,String imgUrlMed){
        //Custom Dialog
        AlertDialog.Builder dialoge = new AlertDialog.Builder(MainActivity.this);
        View layoutView = LayoutInflater.from(this).inflate(R.layout.dialog_custom_view,null);
        Button dialogButton = layoutView.findViewById(R.id.btn_dia_save);
        TextView tvPhotographer = layoutView.findViewById(R.id.tv_dia_photographer);
        TextView tvWidth = layoutView.findViewById(R.id.tv_dia_width);
        TextView tvHeight = layoutView.findViewById(R.id.tv_dia_height);
        TextView tvURL = layoutView.findViewById(R.id.tv_dia_url);
        TextView tvPUrl = layoutView.findViewById(R.id.tv_dia_p_url);
        ImageView imageView = layoutView.findViewById(R.id.img_dia);

        tvPhotographer.setText(p_name);
        tvWidth.setText(width);
        tvHeight.setText(height);
        tvURL.setText(url);
        tvPUrl.setText(p_url);

        if(imgUrl.equals("")){

        }else {

            Glide.with(this)
                    .load(imgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;

                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(imageView);

        }

        dialoge.setView(layoutView);
        Dialog dialog = dialoge.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean insertSuccess = dbClass.insertUserData(new ImageDBModel(imgID,p_name,width,height,url,p_url,imgUrlMed,imgUrl));
                Log.e("INSERT","SUCCESS: "+insertSuccess);

                Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        });

    }

}