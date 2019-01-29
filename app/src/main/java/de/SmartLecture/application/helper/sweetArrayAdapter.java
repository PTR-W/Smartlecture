package de.SmartLecture.application.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import de.SmartLecture.R;
import de.SmartLecture.domain.Lecture.Lecture;

public class sweetArrayAdapter extends ArrayAdapter<Lecture> {
    private AppCompatActivity activity;
    private List<Lecture> lectureList;

    public sweetArrayAdapter(@NonNull AppCompatActivity activity, List<Lecture> lectureList) {
        super(activity, 0, lectureList);
        this.activity = activity;
        this.lectureList = lectureList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = activity.getLayoutInflater().inflate(R.layout.list_element, parent, false);

        TextView name = view.findViewById(R.id.nameofLecture);
        TextView weekday = view.findViewById(R.id.weekday);
        TextView timefrom = view.findViewById(R.id.timefrom);
        TextView timeto = view.findViewById(R.id.timeto);

        name.setText(lectureList.get(position).getName());
        weekday.setText(lectureList.get(position).getWeekday().toString());
        timefrom.setText(lectureList.get(position).getTimefrom().toString());
        timeto.setText(lectureList.get(position).getTimeto().toString());

        return view;
    }
}


