package de.SmartLecture.application.helper;

import android.net.sip.SipSession;
import android.support.annotation.NonNull;
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

    private List<Subject> subjects = new ArrayList<>();

    @NonNull
    @Override
    public SubjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item, parent, false);
        return new SubjectHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubjectHolder holder, int position) {
        Subject currentSubject = subjects.get(position);
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
    }

    public class SubjectHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private TextView textViewTitle;
        private TextView textViewDateStart;
        private TextView textViewDateEnd;

        public SubjectHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDateStart = itemView.findViewById(R.id.text_view_date_start);
            textViewDateEnd = itemView.findViewById(R.id.text_view_date_end);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), R.id.option_edit,   0,"Edit");
            menu.add(this.getAdapterPosition(), R.id.option_delete, 0,"Delete");
        }
    }
}