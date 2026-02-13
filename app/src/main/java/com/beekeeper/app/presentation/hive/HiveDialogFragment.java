package com.beekeeper.app.presentation.hive;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.beekeeper.app.R;
import com.beekeeper.app.data.local.entity.Hive;
import com.beekeeper.app.util.Constants;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Calendar;

public class HiveDialogFragment extends DialogFragment {

    private static final String ARG_HIVE = "hive";
    private static final String ARG_MODE = "mode";
    private static final String ARG_APIARY_ID = "apiary_id";

    public static final int MODE_CREATE = 0;
    public static final int MODE_EDIT = 1;

    private EditText nameInput;
    private Spinner typeSpinner;
    private EditText queenIdInput;
    private EditText queenYearInput;
    private Hive hive;
    private int mode;
    private String apiaryId;
    private OnHiveDialogListener listener;

    public interface OnHiveDialogListener {
        void onHiveCreate(String name, String type, String queenId, int queenYear);
        void onHiveUpdate(Hive hive);
    }

    public static HiveDialogFragment newInstance(int mode, String apiaryId, @Nullable Hive hive) {
        HiveDialogFragment fragment = new HiveDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MODE, mode);
        args.putString(ARG_APIARY_ID, apiaryId);
        if (hive != null) {
            args.putSerializable(ARG_HIVE, hive);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getInt(ARG_MODE, MODE_CREATE);
            apiaryId = getArguments().getString(ARG_APIARY_ID);
            hive = (Hive) getArguments().getSerializable(ARG_HIVE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_hive, null);

        nameInput = view.findViewById(R.id.input_hive_name);
        typeSpinner = view.findViewById(R.id.spinner_hive_type);
        queenIdInput = view.findViewById(R.id.input_queen_id);
        queenYearInput = view.findViewById(R.id.input_queen_year);

        // Setup type spinner
        String[] types = {
            getString(R.string.hive_type_vertical),
            getString(R.string.hive_type_horizontal),
            getString(R.string.hive_type_nuke)
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            types
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        // Populate fields if editing
        if (mode == MODE_EDIT && hive != null) {
            nameInput.setText(hive.getName());
            queenIdInput.setText(hive.getQueenId());
            if (hive.getQueenYear() > 0) {
                queenYearInput.setText(String.valueOf(hive.getQueenYear()));
            }

            // Set spinner selection
            int typePosition = 0;
            switch (hive.getType()) {
                case Constants.HIVE_TYPE_VERTICAL:
                    typePosition = 0;
                    break;
                case Constants.HIVE_TYPE_HORIZONTAL:
                    typePosition = 1;
                    break;
                case Constants.HIVE_TYPE_NUKE:
                    typePosition = 2;
                    break;
            }
            typeSpinner.setSelection(typePosition);
        }

        String title = mode == MODE_CREATE ? "Nový úľ" : "Upraviť úľ";
        String positiveButton = mode == MODE_CREATE ? "Vytvoriť" : "Uložiť";

        return new MaterialAlertDialogBuilder(requireContext())
                .setTitle(title)
                .setView(view)
                .setPositiveButton(positiveButton, (dialog, which) -> {
                    String name = nameInput.getText().toString().trim();
                    String queenId = queenIdInput.getText().toString().trim();
                    String queenYearStr = queenYearInput.getText().toString().trim();

                    if (name.isEmpty()) {
                        nameInput.setError("Zadajte názov úľa");
                        return;
                    }

                    // Convert spinner selection to type
                    String type = Constants.HIVE_TYPE_VERTICAL;
                    switch (typeSpinner.getSelectedItemPosition()) {
                        case 0:
                            type = Constants.HIVE_TYPE_VERTICAL;
                            break;
                        case 1:
                            type = Constants.HIVE_TYPE_HORIZONTAL;
                            break;
                        case 2:
                            type = Constants.HIVE_TYPE_NUKE;
                            break;
                    }

                    int queenYear = 0;
                    if (!queenYearStr.isEmpty()) {
                        try {
                            queenYear = Integer.parseInt(queenYearStr);
                        } catch (NumberFormatException e) {
                            queenYear = Calendar.getInstance().get(Calendar.YEAR);
                        }
                    }

                    if (listener != null) {
                        if (mode == MODE_CREATE) {
                            listener.onHiveCreate(name, type, queenId, queenYear);
                        } else if (hive != null) {
                            hive.setName(name);
                            hive.setType(type);
                            hive.setQueenId(queenId);
                            hive.setQueenYear(queenYear);
                            listener.onHiveUpdate(hive);
                        }
                    }
                })
                .setNegativeButton("Zrušiť", null)
                .create();
    }

    public void setOnHiveDialogListener(OnHiveDialogListener listener) {
        this.listener = listener;
    }
}
