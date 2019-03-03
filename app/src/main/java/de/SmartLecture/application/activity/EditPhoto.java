package de.SmartLecture.application.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import de.SmartLecture.R;

public class EditPhoto extends Fragment {
    private FragmentListener listener;
    //private Spinner spinner;
    private EditText editText;
    private Button button;

    public interface FragmentListener{
        void onInputSent(String input);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_photo, container, false);

        ////////
        //spinner = view.findViewById(R.id.edit_photo_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
        //        R.array.planets_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        //spinner.setAdapter(adapter);

        ////////
        editText = view.findViewById(R.id.edit_photo_text);
        button  = view.findViewById(R.id.edit_photo_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString();
                listener.onInputSent(input);
                getFragmentManager().popBackStackImmediate();
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListener){
            listener = (FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
            + "must implement FragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
