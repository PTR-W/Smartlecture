package de.SmartLecture.application.helper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.SmartLecture.R;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectHolder> {

    private List<Subject> subjects = new ArrayList<>();

    @NonNull
    @Override
    public SubjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item, parent, false);
        return new SubjectHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectHolder holder, int position) {
        Subject currentSubject = subjects.get(position);
        holder.textViewTitle.setText(currentSubject.getTitle());
        holder.textViewDateEnd.setText(currentSubject.getDateEnd());
        holder.textViewDateStart.setText(currentSubject.getDateStart());
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public void setSubjects(List<Subject> subjects)
    {
        this.subjects = subjects;
        notifyDataSetChanged();
    }

    class SubjectHolder extends RecyclerView.ViewHolder
    {
        private TextView textViewTitle;
        private TextView textViewDateStart;
        private TextView textViewDateEnd;

        public SubjectHolder(View itemView)
        {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDateStart = itemView.findViewById(R.id.text_view_date_start);
            textViewDateEnd = itemView.findViewById(R.id.text_view_date_end);
        }
    }
}
