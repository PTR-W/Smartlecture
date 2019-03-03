package de.SmartLecture.application.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import de.SmartLecture.R;

public class EditPhoto extends Fragment {
    private FragmentListener listener;
    private EditText editText;

    public interface FragmentListener{
        void onInputSent(String input);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_photo, container, false);
        editText = view.findViewById(R.id.edit_photo_text);
        editText.setText(((ViewPhotos)getActivity()).getSubject());
        Button button  = view.findViewById(R.id.edit_photo_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString();
                listener.onInputSent(input);
                InputMethodManager softKeyboard = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                softKeyboard.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                getFragmentManager().popBackStack();
                ((ViewPhotos)getActivity()).setBgVisibility(false);
                getActivity().getSupportFragmentManager().beginTransaction().remove(EditPhoto.this).commit();
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
