package com.beekeeper.app.presentation.apiary;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.beekeeper.app.R;
import com.beekeeper.app.data.local.entity.Apiary;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ApiaryDialogFragment extends DialogFragment {

    private static final String ARG_APIARY = "apiary";
    private static final String ARG_MODE = "mode";

    public static final int MODE_CREATE = 0;
    public static final int MODE_EDIT = 1;

    private EditText nameInput;
    private EditText locationInput;
    private Apiary apiary;
    private int mode;
    private OnApiaryDialogListener listener;

    public interface OnApiaryDialogListener {
        void onApiaryCreate(String name, String location);
        void onApiaryUpdate(Apiary apiary);
    }

    public static ApiaryDialogFragment newInstance(int mode, @Nullable Apiary apiary) {
        ApiaryDialogFragment fragment = new ApiaryDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MODE, mode);
        if (apiary != null) {
            args.putSerializable(ARG_APIARY, apiary);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getInt(ARG_MODE, MODE_CREATE);
            apiary = (Apiary) getArguments().getSerializable(ARG_APIARY);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_apiary, null);

        nameInput = view.findViewById(R.id.input_apiary_name);
        locationInput = view.findViewById(R.id.input_apiary_location);

        if (mode == MODE_EDIT && apiary != null) {
            nameInput.setText(apiary.getName());
            locationInput.setText(apiary.getLocation());
        }

        String title = mode == MODE_CREATE ? "Nová včelnica" : "Upraviť včelnicu";
        String positiveButton = mode == MODE_CREATE ? "Vytvoriť" : "Uložiť";

        return new MaterialAlertDialogBuilder(requireContext())
                .setTitle(title)
                .setView(view)
                .setPositiveButton(positiveButton, (dialog, which) -> {
                    String name = nameInput.getText().toString().trim();
                    String location = locationInput.getText().toString().trim();

                    if (name.isEmpty()) {
                        nameInput.setError("Zadajte názov včelnice");
                        return;
                    }

                    if (listener != null) {
                        if (mode == MODE_CREATE) {
                            listener.onApiaryCreate(name, location);
                        } else if (apiary != null) {
                            apiary.setName(name);
                            apiary.setLocation(location);
                            listener.onApiaryUpdate(apiary);
                        }
                    }
                })
                .setNegativeButton("Zrušiť", null)
                .create();
    }

    public void setOnApiaryDialogListener(OnApiaryDialogListener listener) {
        this.listener = listener;
    }
}
