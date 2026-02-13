package com.beekeeper.app.presentation.inspection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.beekeeper.app.R;
import com.beekeeper.app.data.local.entity.Inspection;
import com.beekeeper.app.util.DateUtils;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InspectionAdapter extends ListAdapter<Inspection, InspectionAdapter.InspectionViewHolder> {

    private OnInspectionClickListener listener;

    public interface OnInspectionClickListener {
        void onInspectionClick(Inspection inspection);
        void onInspectionEdit(Inspection inspection);
        void onInspectionDelete(Inspection inspection);
    }

    public InspectionAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Inspection> DIFF_CALLBACK = new DiffUtil.ItemCallback<Inspection>() {
        @Override
        public boolean areItemsTheSame(@NonNull Inspection oldItem, @NonNull Inspection newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Inspection oldItem, @NonNull Inspection newItem) {
            return oldItem.getInspectionDate() == newItem.getInspectionDate() &&
                   oldItem.getTemperature() == newItem.getTemperature() &&
                   oldItem.getStrengthEstimate() == newItem.getStrengthEstimate() &&
                   oldItem.getBroodFrames() == newItem.getBroodFrames() &&
                   oldItem.isQueenSeen() == newItem.isQueenSeen() &&
                   oldItem.isVarroa() == newItem.isVarroa();
        }
    };

    @NonNull
    @Override
    public InspectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inspection, parent, false);
        return new InspectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InspectionViewHolder holder, int position) {
        Inspection inspection = getItem(position);
        holder.bind(inspection, listener);
    }

    public void setOnInspectionClickListener(OnInspectionClickListener listener) {
        this.listener = listener;
    }

    static class InspectionViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateText;
        private final TextView timeText;
        private final TextView temperatureText;
        private final TextView strengthText;
        private final TextView broodFramesText;
        private final Chip queenSeenChip;
        private final Chip varroaChip;
        private final TextView notesPreviewText;
        private final ImageButton editButton;
        private final ImageButton deleteButton;

        private final SimpleDateFormat dateFormat = new SimpleDateFormat("d. MMMM yyyy", new Locale("sk"));
        private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        public InspectionViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.text_date);
            timeText = itemView.findViewById(R.id.text_time);
            temperatureText = itemView.findViewById(R.id.text_temperature);
            strengthText = itemView.findViewById(R.id.text_strength);
            broodFramesText = itemView.findViewById(R.id.text_brood_frames);
            queenSeenChip = itemView.findViewById(R.id.chip_queen_seen);
            varroaChip = itemView.findViewById(R.id.chip_varroa);
            notesPreviewText = itemView.findViewById(R.id.text_notes_preview);
            editButton = itemView.findViewById(R.id.button_edit);
            deleteButton = itemView.findViewById(R.id.button_delete);
        }

        public void bind(Inspection inspection, OnInspectionClickListener listener) {
            // Format date and time
            Date date = new Date(inspection.getInspectionDate());
            dateText.setText(dateFormat.format(date));
            timeText.setText(timeFormat.format(date));

            // Temperature
            if (inspection.getTemperature() > 0) {
                temperatureText.setText(String.format(Locale.getDefault(), "%.1f°C", inspection.getTemperature()));
            } else {
                temperatureText.setText("-");
            }

            // Strength
            if (inspection.getStrengthEstimate() > 0) {
                strengthText.setText(String.format(Locale.getDefault(), "Sila: %d/10", inspection.getStrengthEstimate()));
            } else {
                strengthText.setText("Sila: -");
            }

            // Brood frames
            if (inspection.getBroodFrames() > 0) {
                broodFramesText.setText(String.format(Locale.getDefault(), "Plod: %d r.", inspection.getBroodFrames()));
            } else {
                broodFramesText.setText("Plod: -");
            }

            // Queen seen indicator
            if (inspection.isQueenSeen()) {
                queenSeenChip.setVisibility(View.VISIBLE);
            } else {
                queenSeenChip.setVisibility(View.GONE);
            }

            // Varroa indicator
            if (inspection.isVarroa()) {
                varroaChip.setVisibility(View.VISIBLE);
                if (inspection.getVarroaCount() > 0) {
                    varroaChip.setText(String.format(Locale.getDefault(), "⚠️ Klieštik (%d)", inspection.getVarroaCount()));
                }
            } else {
                varroaChip.setVisibility(View.GONE);
            }

            // Notes preview
            if (inspection.getNotes() != null && !inspection.getNotes().trim().isEmpty()) {
                notesPreviewText.setVisibility(View.VISIBLE);
                notesPreviewText.setText("Poznámka: " + inspection.getNotes());
            } else {
                notesPreviewText.setVisibility(View.GONE);
            }

            // Click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onInspectionClick(inspection);
                }
            });

            editButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onInspectionEdit(inspection);
                }
            });

            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onInspectionDelete(inspection);
                }
            });
        }
    }
}
