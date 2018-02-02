package net.hongqianfly.photopicker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by HongQian.Wang on 2018/2/1.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private ArrayList<Image> imagesList;

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_gallery,null, false);

        return new GalleryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, final int position) {
        Glide.with(holder.cell_mImage.getContext())
                .load(imagesList.get(position).getPath())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .into(holder.cell_mImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener!=null) {
                    onItemClickListener.onItemClick(imagesList.get(position),position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagesList==null?0:imagesList.size();
    }

    public void replaceAll(ArrayList<Image> imagesList) {
             this.imagesList=imagesList;
             notifyDataSetChanged();
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder{
        ImageView cell_mImage;
        View itemView;
        public GalleryViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
             cell_mImage = (ImageView) itemView.findViewById(R.id.cell_mImage);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public interface  OnItemClickListener{
        void onItemClick(Image image,int position);
    }
}
