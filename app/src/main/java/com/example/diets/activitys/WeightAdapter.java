package com.example.diets.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diets.R;
import com.example.diets.model.WeightRecord;

import java.util.List;

public class WeightAdapter extends RecyclerView.Adapter<WeightAdapter.WeightViewHolder> {

    private final List<WeightRecord> weightRecords;

    public WeightAdapter(List<WeightRecord> weightRecords) {
        this.weightRecords = weightRecords;
    }

    @NonNull
    @Override
    public WeightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weight_record, parent, false);
        return new WeightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeightViewHolder holder, int position) {
        WeightRecord record = weightRecords.get(position);
        holder.weightText.setText("Peso: " + record.getWeight() + " kg");
        holder.dateText.setText("Fecha: " + record.getDate());
    }

    @Override
    public int getItemCount() {
        return weightRecords.size();
    }

    static class WeightViewHolder extends RecyclerView.ViewHolder {
        TextView weightText, dateText;

        public WeightViewHolder(@NonNull View itemView) {
            super(itemView);
            weightText = itemView.findViewById(R.id.weightText);
            dateText = itemView.findViewById(R.id.dateText);
        }
    }
}
