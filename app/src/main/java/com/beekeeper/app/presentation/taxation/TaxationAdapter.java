package com.beekeeper.app.presentation.taxation;

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
import com.beekeeper.app.data.local.entity.Taxation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaxationAdapter extends ListAdapter<Taxation, TaxationAdapter.TaxationViewHolder> {

    private OnTaxationClickListener listener;

    public interface OnTaxationClickListener {
        void onTaxationClick(Taxation taxation);
        void onTaxationView(Taxation taxation);
        void onTaxationDelete(Taxation taxation);
    }

    public TaxationAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Taxation> DIFF_CALLBACK = new DiffUtil.ItemCallback<Taxation>() {
        @Override
        public boolean areItemsTheSame(@NonNull Taxation oldItem, @NonNull Taxation newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Taxation oldItem, @NonNull Taxation newItem) {
            return oldItem.getTaxationDate() == newItem.getTaxationDate() &&
                   oldItem.getTemperature() == newItem.getTemperature() &&
                   oldItem.getTotalFrames() == newItem.getTotalFrames() &&
                   oldItem.getFoodStoresKg() == newItem.getFoodStoresKg();
        }
    };

    @NonNull
    @Override
    public TaxationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_taxation, parent, false);
        return new TaxationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaxationViewHolder holder, int position) {
        Taxation taxation = getItem(position);
        holder.bind(taxation, listener);
    }

    public void setOnTaxationClickListener(OnTaxationClickListener listener) {
        this.listener = listener;
    }

    static class TaxationViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateText;
        private final TextView temperatureText;
        private final TextView totalFramesText;
        private final TextView foodStoresText;
        private final TextView notesPreviewText;
        private final ImageButton viewButton;
        private final ImageButton deleteButton;

        private final SimpleDateFormat dateFormat = new SimpleDateFormat("d. MMMM yyyy", new Locale("sk"));

        public TaxationViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.text_date);
            temperatureText = itemView.findViewById(R.id.text_temperature);
            totalFramesText = itemView.findViewById(R.id.text_total_frames);
            foodStoresText = itemView.findViewById(R.id.text_food_stores);
            notesPreviewText = itemView.findViewById(R.id.text_notes_preview);
            viewButton = itemView.findViewById(R.id.button_view);
            deleteButton = itemView.findViewById(R.id.button_delete);
        }

        public void bind(Taxation taxation, OnTaxationClickListener listener) {
            // Format date
            Date date = new Date(taxation.getTaxationDate());
            dateText.setText(dateFormat.format(date));

            // Temperature
            if (taxation.getTemperature() > 0) {
                temperatureText.setText(String.format(Locale.getDefault(), "%.1f°C", taxation.getTemperature()));
            } else {
                temperatureText.setText("-");
            }

            // Total frames
            totalFramesText.setText(String.valueOf(taxation.getTotalFrames()));

            // Food stores
            if (taxation.getFoodStoresKg() > 0) {
                foodStoresText.setText(String.format(Locale.getDefault(), "%.1f kg", taxation.getFoodStoresKg()));
            } else {
                foodStoresText.setText("-");
            }

            // Notes preview
            if (taxation.getNotes() != null && !taxation.getNotes().trim().isEmpty()) {
                notesPreviewText.setVisibility(View.VISIBLE);
                notesPreviewText.setText("Poznámka: " + taxation.getNotes());
            } else {
                notesPreviewText.setVisibility(View.GONE);
            }

            // Click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTaxationClick(taxation);
                }
            });

            viewButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTaxationView(taxation);
                }
            });

            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTaxationDelete(taxation);
                }
            });
        }
    }
}
