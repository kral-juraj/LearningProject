package com.beekeeper.app.presentation.apiary;

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
import com.beekeeper.app.data.local.entity.Apiary;
import com.beekeeper.app.util.DateUtils;

public class ApiaryAdapter extends ListAdapter<Apiary, ApiaryAdapter.ApiaryViewHolder> {

    private OnApiaryClickListener listener;

    public interface OnApiaryClickListener {
        void onApiaryClick(Apiary apiary);
        void onApiaryEdit(Apiary apiary);
        void onApiaryDelete(Apiary apiary);
    }

    public ApiaryAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Apiary> DIFF_CALLBACK = new DiffUtil.ItemCallback<Apiary>() {
        @Override
        public boolean areItemsTheSame(@NonNull Apiary oldItem, @NonNull Apiary newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Apiary oldItem, @NonNull Apiary newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                   oldItem.getLocation().equals(newItem.getLocation()) &&
                   oldItem.getUpdatedAt() == newItem.getUpdatedAt();
        }
    };

    @NonNull
    @Override
    public ApiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_apiary, parent, false);
        return new ApiaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApiaryViewHolder holder, int position) {
        Apiary apiary = getItem(position);
        holder.bind(apiary, listener);
    }

    public void setOnApiaryClickListener(OnApiaryClickListener listener) {
        this.listener = listener;
    }

    static class ApiaryViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameText;
        private final TextView locationText;
        private final TextView dateText;
        private final ImageButton editButton;
        private final ImageButton deleteButton;

        public ApiaryViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.text_apiary_name);
            locationText = itemView.findViewById(R.id.text_apiary_location);
            dateText = itemView.findViewById(R.id.text_apiary_date);
            editButton = itemView.findViewById(R.id.button_edit_apiary);
            deleteButton = itemView.findViewById(R.id.button_delete_apiary);
        }

        public void bind(Apiary apiary, OnApiaryClickListener listener) {
            nameText.setText(apiary.getName());
            locationText.setText(apiary.getLocation() != null && !apiary.getLocation().isEmpty()
                ? apiary.getLocation()
                : "Bez lokality");
            dateText.setText("Vytvorené: " + DateUtils.formatDate(apiary.getCreatedAt()));

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onApiaryClick(apiary);
                }
            });

            editButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onApiaryEdit(apiary);
                }
            });

            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onApiaryDelete(apiary);
                }
            });
        }
    }
}
