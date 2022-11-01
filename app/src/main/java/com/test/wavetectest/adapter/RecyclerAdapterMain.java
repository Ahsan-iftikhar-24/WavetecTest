package com.test.wavetectest.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.test.wavetectest.R;
import com.test.wavetectest.models.PhotoModel;
import com.test.wavetectest.models.PhotosRoot;

import java.util.List;

public class RecyclerAdapterMain extends RecyclerView.Adapter<RecyclerAdapterMain.MyViewHolder> {

    private Context context;
    private List<PhotoModel> photosList;
    private RVMainClickInterface rvMainClickInterface;


    public RecyclerAdapterMain(Context context, List<PhotoModel> photosList, RVMainClickInterface rvMainClickInterface) {
        this.context = context;
        this.photosList = photosList;
        this.rvMainClickInterface = rvMainClickInterface;
    }


    @NonNull
    @Override
    public RecyclerAdapterMain.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_grid,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterMain.MyViewHolder holder, int position) {

        String imgMedium = this.photosList.get(position).getImages().getMedium();
        String photoName = this.photosList.get(position).getPhotographer();

        if(photoName.equals(""))
        {
            holder.tvPhotographer.setText("Unknown");

        }else {
            holder.tvPhotographer.setText(photoName);
        }

        if(imgMedium.equals("")){

        }else {

            Glide.with(context)
                    .load(imgMedium)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;

                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(holder.imageView);

        }




    }

    @Override
    public int getItemCount() {

        if(this.photosList!=null && !this.photosList.isEmpty()){
           return this.photosList.size();
        }
        else {
            return 0;
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tvPhotographer;
        ProgressBar progressBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img);
            tvPhotographer = itemView.findViewById(R.id.tv_main_photographer_name);
            progressBar = itemView.findViewById(R.id.progBarMain);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rvMainClickInterface.onItemClick(getAdapterPosition());
                }
            });

        }
    }
}
