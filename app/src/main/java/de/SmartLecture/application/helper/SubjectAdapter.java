package de.SmartLecture.application.helper;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.SmartLecture.R;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectHolder> {
//public class SubjectAdapter extends ListAdapter<Subject, SubjectAdapter.SubjectHolder> {

    private List<Subject> subjects = new ArrayList<>();
    private OnItemClickListener listener;

//    public SubjectAdapter() {
//        super(DIFF_CALLBACK);
//    }
//    private static final DiffUtil.ItemCallback<Subject> DIFF_CALLBACK = new DiffUtil.ItemCallback<Subject>() {
//        @Override
//        public boolean areItemsTheSame(Subject oldItem, Subject newItem) {
//            return oldItem.getId() == newItem.getId();
//        }
//
//        @Override
//        public boolean areContentsTheSame(Subject oldItem, Subject newItem) {
//            return oldItem.getTitle().equals(newItem.getTitle())
//                && oldItem.getDateStart().equals(newItem.getDateStart())
//                && oldItem.getDateEnd().equals(newItem.getDateEnd());
//        }
//    };

    @NonNull
    @Override
    public SubjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item, parent, false);
        return new SubjectHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubjectHolder holder, int position) {
        Subject currentSubject = subjects.get(position);
        //Subject currentSubject = getItem(position);
        holder.textViewTitle.setText(currentSubject.getTitle());
        holder.textViewDateEnd.setText(currentSubject.getDateEnd());
        holder.textViewDateStart.setText(currentSubject.getDateStart());
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
        notifyDataSetChanged();
    }

    public Subject getSubjectAt(int position) {
        return subjects.get(position);
        //return getItem(position);
    }

    public class SubjectHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnClickListener {
        private TextView textViewTitle;
        private TextView textViewDateStart;
        private TextView textViewDateEnd;


        public SubjectHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDateStart = itemView.findViewById(R.id.text_view_date_start);
            textViewDateEnd = itemView.findViewById(R.id.text_view_date_end);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), R.id.option_edit,   0,"Edit");
            menu.add(this.getAdapterPosition(), R.id.option_delete, 0,"Delete");
        }

        //itemView.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if (listener != null)
            {
                //listener.OnItemClick(getItem(getAdapterPosition()));
                listener.OnItemClick(subjects.get(getAdapterPosition()));
            }
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(Subject subject);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}