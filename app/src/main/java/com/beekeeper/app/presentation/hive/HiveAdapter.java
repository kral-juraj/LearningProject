package com.beekeeper.app.presentation.hive;

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
import com.beekeeper.app.data.local.entity.Hive;
import com.beekeeper.app.util.Constants;

public class HiveAdapter extends ListAdapter<Hive, HiveAdapter.HiveViewHolder> {

    private OnHiveClickListener listener;

    public interface OnHiveClickListener {
        void onHiveClick(Hive hive);
        void onHiveEdit(Hive hive);
        void onHiveDelete(Hive hive);
    }

    public HiveAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Hive> DIFF_CALLBACK = new DiffUtil.ItemCallback<Hive>() {
        @Override
        public boolean areItemsTheSame(@NonNull Hive oldItem, @NonNull Hive newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Hive oldItem, @NonNull Hive newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                   oldItem.getType().equals(newItem.getType()) &&
                   oldItem.isActive() == newItem.isActive() &&
                   oldItem.getQueenId().equals(newItem.getQueenId());
        }
    };

    @NonNull
    @Override
    public HiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hive, parent, false);
        return new HiveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HiveViewHolder holder, int position) {
        Hive hive = getItem(position);
        holder.bind(hive, listener);
    }

    public void setOnHiveClickListener(OnHiveClickListener listener) {
        this.listener = listener;
    }

    static class HiveViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameText;
        private final TextView typeText;
        private final TextView queenText;
        private final TextView statusText;
        private final ImageButton editButton;
        private final ImageButton deleteButton;

        public HiveViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.text_hive_name);
            typeText = itemView.findViewById(R.id.text_hive_type);
            queenText = itemView.findViewById(R.id.text_hive_queen);
            statusText = itemView.findViewById(R.id.text_hive_status);
            editButton = itemView.findViewById(R.id.button_edit_hive);
            deleteButton = itemView.findViewById(R.id.button_delete_hive);
        }

        public void bind(Hive hive, OnHiveClickListener listener) {
            nameText.setText(hive.getName());

            // Type display
            String typeDisplay = "Typ: ";
            switch (hive.getType()) {
                case Constants.HIVE_TYPE_VERTICAL:
                    typeDisplay += "Vertikálny";
                    break;
                case Constants.HIVE_TYPE_HORIZONTAL:
                    typeDisplay += "Ležatý";
                    break;
                case Constants.HIVE_TYPE_NUKE:
                    typeDisplay += "Oddielok";
                    break;
                default:
                    typeDisplay += hive.getType();
            }
            typeText.setText(typeDisplay);

            // Queen info
            if (hive.getQueenId() != null && !hive.getQueenId().isEmpty()) {
                String queenInfo = "Matka: " + hive.getQueenId();
                if (hive.getQueenYear() > 0) {
                    queenInfo += " (" + hive.getQueenYear() + ")";
                }
                queenText.setText(queenInfo);
                queenText.setVisibility(View.VISIBLE);
            } else {
                queenText.setVisibility(View.GONE);
            }

            // Status
            if (hive.isActive()) {
                statusText.setText("Aktívny");
                statusText.setTextColor(itemView.getContext().getColor(R.color.success));
            } else {
                statusText.setText("Neaktívny");
                statusText.setTextColor(itemView.getContext().getColor(R.color.text_hint));
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onHiveClick(hive);
                }
            });

            editButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onHiveEdit(hive);
                }
            });

            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onHiveDelete(hive);
                }
            });
        }
    }
}
