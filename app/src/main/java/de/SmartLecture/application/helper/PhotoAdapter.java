package de.SmartLecture.application.helper;

import java.util.List;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.View;
import java.util.ArrayList;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.view.LayoutInflater;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import de.SmartLecture.R;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {
    private OnItemClickListener listener;
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

    public Photo getPhotoAt(int position) {

        return photos.get(position);
    }

    class PhotoHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        private ImageView imageView;

        PhotoHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.photo_item_view);

            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.OnItemClick(photos.get(getAdapterPosition()));
                    }
                }
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), R.id.option_edit, 0, "Edit");
            menu.add(this.getAdapterPosition(), R.id.option_delete, 0, "Delete");
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(Photo photo);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}

