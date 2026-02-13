package com.beekeeper.app.presentation.feeding;

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
import com.beekeeper.app.data.local.entity.Feeding;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FeedingAdapter extends ListAdapter<Feeding, FeedingAdapter.FeedingViewHolder> {

    private OnFeedingClickListener listener;

    public interface OnFeedingClickListener {
        void onFeedingClick(Feeding feeding);
        void onFeedingEdit(Feeding feeding);
        void onFeedingDelete(Feeding feeding);
    }

    public FeedingAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Feeding> DIFF_CALLBACK = new DiffUtil.ItemCallback<Feeding>() {
        @Override
        public boolean areItemsTheSame(@NonNull Feeding oldItem, @NonNull Feeding newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Feeding oldItem, @NonNull Feeding newItem) {
            return oldItem.getFeedingDate() == newItem.getFeedingDate() &&
                   oldItem.getFeedType().equals(newItem.getFeedType()) &&
                   oldItem.getAmountKg() == newItem.getAmountKg() &&
                   oldItem.getWeightBefore() == newItem.getWeightBefore() &&
                   oldItem.getWeightAfter() == newItem.getWeightAfter();
        }
    };

    @NonNull
    @Override
    public FeedingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_feeding, parent, false);
        return new FeedingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedingViewHolder holder, int position) {
        Feeding feeding = getItem(position);
        holder.bind(feeding, listener);
    }

    public void setOnFeedingClickListener(OnFeedingClickListener listener) {
        this.listener = listener;
    }

    static class FeedingViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateText;
        private final Chip feedTypeChip;
        private final TextView amountText;
        private final TextView weightBeforeText;
        private final TextView weightAfterText;
        private final TextView weightDiffText;
        private final TextView notesPreviewText;
        private final ImageButton editButton;
        private final ImageButton deleteButton;

        private final SimpleDateFormat dateFormat = new SimpleDateFormat("d. MMMM yyyy", new Locale("sk"));

        public FeedingViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.text_date);
            feedTypeChip = itemView.findViewById(R.id.chip_feed_type);
            amountText = itemView.findViewById(R.id.text_amount);
            weightBeforeText = itemView.findViewById(R.id.text_weight_before);
            weightAfterText = itemView.findViewById(R.id.text_weight_after);
            weightDiffText = itemView.findViewById(R.id.text_weight_diff);
            notesPreviewText = itemView.findViewById(R.id.text_notes_preview);
            editButton = itemView.findViewById(R.id.button_edit);
            deleteButton = itemView.findViewById(R.id.button_delete);
        }

        public void bind(Feeding feeding, OnFeedingClickListener listener) {
            // Format date
            Date date = new Date(feeding.getFeedingDate());
            dateText.setText(dateFormat.format(date));

            // Feed type
            String feedTypeDisplay = getFeedTypeDisplay(feeding.getFeedType());
            feedTypeChip.setText(feedTypeDisplay);

            // Amount
            if (feeding.getAmountKg() > 0) {
                amountText.setText(String.format(Locale.getDefault(), "%.1f kg", feeding.getAmountKg()));
            } else {
                amountText.setText("-");
            }

            // Weight before
            if (feeding.getWeightBefore() > 0) {
                weightBeforeText.setText(String.format(Locale.getDefault(), "%.1f kg", feeding.getWeightBefore()));
            } else {
                weightBeforeText.setText("-");
            }

            // Weight after
            if (feeding.getWeightAfter() > 0) {
                weightAfterText.setText(String.format(Locale.getDefault(), "%.1f kg", feeding.getWeightAfter()));
            } else {
                weightAfterText.setText("-");
            }

            // Weight difference
            if (feeding.getWeightBefore() > 0 && feeding.getWeightAfter() > 0) {
                double diff = feeding.getWeightAfter() - feeding.getWeightBefore();
                String sign = diff >= 0 ? "+" : "";
                weightDiffText.setText(String.format(Locale.getDefault(), "%s%.1f kg", sign, diff));

                // Color based on positive/negative
                if (diff > 0) {
                    weightDiffText.setTextColor(itemView.getContext().getColor(R.color.success));
                } else if (diff < 0) {
                    weightDiffText.setTextColor(itemView.getContext().getColor(R.color.error));
                } else {
                    weightDiffText.setTextColor(itemView.getContext().getColor(R.color.text_secondary));
                }
            } else {
                weightDiffText.setText("-");
            }

            // Notes preview
            if (feeding.getNotes() != null && !feeding.getNotes().trim().isEmpty()) {
                notesPreviewText.setVisibility(View.VISIBLE);
                notesPreviewText.setText("Poznámka: " + feeding.getNotes());
            } else {
                notesPreviewText.setVisibility(View.GONE);
            }

            // Click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onFeedingClick(feeding);
                }
            });

            editButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onFeedingEdit(feeding);
                }
            });

            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onFeedingDelete(feeding);
                }
            });
        }

        private String getFeedTypeDisplay(String feedType) {
            if (feedType == null) return "Neznámy";

            switch (feedType) {
                case "SYRUP_1_1":
                    return "Sirup 1:1";
                case "SYRUP_3_2":
                    return "Sirup 3:2";
                case "FONDANT":
                    return "Fondant";
                case "POLLEN_PATTY":
                    return "Peľový koláč";
                default:
                    return feedType;
            }
        }
    }
}
