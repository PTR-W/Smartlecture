package de.SmartLecture.application.helper;

import java.util.List;
import android.net.Uri;
import android.view.View;
import java.util.ArrayList;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import de.SmartLecture.R;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {
    private List<Photo> photos = new ArrayList<>();

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);
        return new PhotoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PhotoHolder holder, int position) {
        Photo currentPhoto = photos.get(position);
        holder.imageView.setImageURI(Uri.parse(currentPhoto.getFileName()));

    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void setPhotos(List<Photo> photos){
        this.photos = photos;
        notifyDataSetChanged();
    }

    class PhotoHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;

        public PhotoHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.photo_item_view);
        }
    }
}
