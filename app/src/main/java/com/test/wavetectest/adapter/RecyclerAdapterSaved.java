package com.test.wavetectest.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.test.wavetectest.R;
import com.test.wavetectest.database.DBClass;
import com.test.wavetectest.models.ImageDBModel;
import com.test.wavetectest.models.PhotoModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerAdapterSaved extends RecyclerView.Adapter<RecyclerAdapterSaved.MyViewHolder> {

    private Context context;
    private List<ImageDBModel> photosList;
    private RVMainClickInterface rvMainClickInterface;
    boolean isEnable=false;
    boolean isSelectAll=false;
    //ArrayList<String> selectList=new ArrayList<>();
    ArrayList<Integer> selectList=new ArrayList<>();

    public RecyclerAdapterSaved(Context context, List<ImageDBModel> photosList, RVMainClickInterface rvMainClickInterface) {
        this.context = context;
        this.photosList = photosList;
        this.rvMainClickInterface = rvMainClickInterface;
    }


    @NonNull
    @Override
    public RecyclerAdapterSaved.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_grid_saved,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterSaved.MyViewHolder holder, int position) {

        DBClass databaseHelper = new DBClass(context);
        String imgMedium = this.photosList.get(position).getImgMed();
        String photoName = this.photosList.get(position).getPhotographerName();

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

        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!isEnable)
                {

                    ActionMode.Callback callback=new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                            MenuInflater menuInflater= actionMode.getMenuInflater();

                            menuInflater.inflate(R.menu.menu_saved,menu);

                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {

                            isEnable=true;

                            ClickItem(holder);
                            return true;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

                            int id=menuItem.getItemId();

                            switch(id)
                            {
                                case R.id.menu_delete:

                                    Log.e("Select","Select:"+selectList);
                                    for(Integer i_id:selectList)
                                    {

                                        boolean successDelete = databaseHelper.deleteImageRow(i_id);
                                        Log.e("ROW DELETED","DELETE:"+successDelete);



                                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                        try {
                                            photosList.remove(holder.getAdapterPosition());

                                        } catch (ArrayIndexOutOfBoundsException e) {
                                            e.printStackTrace();
                                        }

                                        notifyItemRemoved(holder.getAdapterPosition());

                                        notifyItemRangeChanged(holder.getAdapterPosition(),getItemCount());
                                    }
                                    photosList = databaseHelper.getAllData();
                                    notifyDataSetChanged();

                                    if(photosList.size()==0|| photosList.isEmpty())
                                    {

                                        Toast.makeText(context, "Empty", Toast.LENGTH_SHORT).show();
                                    }

                                    actionMode.finish();
                                    break;

                                case R.id.menu_select_all:

                                    if(selectList.size()==photosList.size())
                                    {

                                        isSelectAll=false;

                                        selectList.clear();
                                    }
                                    else
                                    {

                                        isSelectAll=true;

                                        selectList.clear();

                                        for (ImageDBModel imageDBModel : photosList){
                                            selectList.add(imageDBModel.getImgID());
                                        }

                                    }


                                    notifyDataSetChanged();
                                    break;
                            }

                            return true;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode actionMode) {
                            isEnable=false;

                            isSelectAll=false;

                            selectList.clear();

                            notifyDataSetChanged();
                        }
                    };

                    ((AppCompatActivity) view.getContext()).startActionMode(callback);
                }
                else
                {

                    ClickItem(holder);
                }

                return true;
            }

        });


        // check condition
        if(isSelectAll)
        {

            holder.checkImg.setVisibility(View.VISIBLE);

            holder.itemView.setBackgroundColor(Color.LTGRAY);
        }
        else
        {

            holder.checkImg.setVisibility(View.GONE);

            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

    }
    private void ClickItem(MyViewHolder holder) {


        int i_id= photosList.get(holder.getAdapterPosition()).getImgID();

        if(holder.checkImg.getVisibility()==View.GONE)
        {

            holder.checkImg.setVisibility(View.VISIBLE);

            holder.itemView.setBackgroundColor(Color.LTGRAY);

            selectList.add(i_id);
        }
        else
        {

            holder.checkImg.setVisibility(View.GONE);

            holder.itemView.setBackgroundColor(Color.TRANSPARENT);

            selectList.remove(i_id);

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
        CardView cvSaved;
        ImageView checkImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img);
            tvPhotographer = itemView.findViewById(R.id.tv_main_photographer_name);
            progressBar = itemView.findViewById(R.id.progBarMain);
            cvSaved = itemView.findViewById(R.id.cvSaved);
            checkImg = itemView.findViewById(R.id.check_box);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rvMainClickInterface.onItemClick(getAdapterPosition());
                }
            });

        }
    }
}
